package com.example.findesttest.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findesttest.R
import com.example.findesttest.databinding.FragmentCartBinding
import com.example.findesttest.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CartViewModel by viewModels()

    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        observeData()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onIncreaseClick = {
                viewModel.increaseQuantity(it)
            },
            onDecreaseClick = {
                viewModel.decreaseQuantity(it)
            },
            onDeleteClick = {
                viewModel.deleteItem(it)
            }
        )

        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }
    }

    private fun setupListeners(){
        binding.btnCheckout.setOnClickListener {
            if (cartAdapter.currentList.isEmpty()){
                Toast.makeText(requireContext(), "Cart is empty!", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(R.id.action_nav_cart_to_checkoutFragment)
            }
        }
    }

    private fun observeData(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.cartState.collect { state ->
                        when (state) {
                            is UiState.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                                binding.tvError.visibility = View.GONE
                            }
                            is UiState.Success -> {
                                binding.progressBar.visibility = View.GONE
                                binding.tvError.visibility = View.GONE
                                val items = state.data
                                cartAdapter.submitList(items)

                                if (items.isEmpty()) {
                                    binding.tvEmptyCart.visibility = View.VISIBLE
                                    binding.rvCartItems.visibility = View.GONE
                                    binding.bottomBarContainer.visibility = View.GONE
                                } else {
                                    binding.tvEmptyCart.visibility = View.GONE
                                    binding.rvCartItems.visibility = View.VISIBLE
                                    binding.bottomBarContainer.visibility = View.VISIBLE

                                }
                            }
                            is UiState.Error -> {
                                binding.progressBar.visibility = View.GONE
                                binding.tvError.visibility = View.VISIBLE
                                binding.tvError.text = state.message
                            }

                        }
                    }

                }

                launch {
                    viewModel.totalPrice.collect {
                        binding.tvTotalPrice.text = String.format("$%.2f", it)
                    }
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}