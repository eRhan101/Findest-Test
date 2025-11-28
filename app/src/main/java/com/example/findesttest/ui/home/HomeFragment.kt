package com.example.findesttest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.findesttest.databinding.FragmentHomeBinding
import com.example.findesttest.utils.UiState
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeObjects()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(onItemClick = {
            val action = HomeFragmentDirections.actionNavHomeToNavDetail(productId = it.id)
            findNavController().navigate(action)
        }, onAddToCartClick = {
            //TODO: add to cart
            Toast.makeText(requireContext(), "Add to cart ${it.title}", Toast.LENGTH_SHORT).show()
        })

        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeObjects() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.productState.collect { state ->
                        when (state) {
                            is UiState.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                                binding.rvProducts.visibility = View.GONE
                                binding.tvError.visibility = View.GONE
                            }

                            is UiState.Success -> {
                                binding.progressBar.visibility = View.GONE
                                binding.rvProducts.visibility = View.VISIBLE
                                binding.tvError.visibility = View.GONE
                                productAdapter.submitList(state.data)
                            }

                            is UiState.Error -> {
                                binding.progressBar.visibility = View.GONE
                                binding.rvProducts.visibility = View.GONE
                                binding.tvError.visibility = View.VISIBLE
                                binding.tvError.text = state.message
                            }

                        }

                    }
                }

                launch {
                    viewModel.categoriesState.collect { state ->
                        when (state) {
                            is UiState.Loading -> {

                            }

                            is UiState.Success -> {
                                setupCategoryChips(state.data)
                            }

                            is UiState.Error -> {
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupCategoryChips(categories: List<String>) {
        binding.chipCategories.removeAllViews()

        categories.forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category.replaceFirstChar { it.uppercase() }
                isCheckable = true
                isClickable = true

                setOnClickListener {
                    viewModel.onCategorySelected(category)
                }
            }
            binding.chipCategories.addView(chip)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}