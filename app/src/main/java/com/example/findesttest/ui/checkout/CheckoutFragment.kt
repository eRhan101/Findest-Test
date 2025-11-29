package com.example.findesttest.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findesttest.R
import com.example.findesttest.databinding.FragmentCheckoutBinding
import com.example.findesttest.utils.UiState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CheckoutViewModel by viewModel()

    private lateinit var adapter: CheckoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()

        binding.btnPlaceOrder.setOnClickListener {
            viewModel.placeOrder()
        }
    }

    private fun setupRecyclerView() {
        adapter = CheckoutAdapter()
        binding.rvCheckoutItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@CheckoutFragment.adapter
            isNestedScrollingEnabled = false
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartItems.collect { items ->
                adapter.submitList(items)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.totalPrice.collect { total ->
                binding.tvTotalPrice.text = String.format("$%.2f", total)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.orderState.collect { state ->
                if (state is UiState.Success && state.data) {
                    Toast.makeText(context, "Order Placed Successfully!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_checkoutFragment_to_nav_home)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}