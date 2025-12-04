package com.example.findesttest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.findesttest.R
import com.example.findesttest.databinding.FragmentHomeBinding
import com.example.findesttest.utils.UiState
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

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
        setupToolbar()
        observeObjects()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(onItemClick = {
            val action = HomeFragmentDirections.actionNavHomeToNavDetail(productId = it.id)
            findNavController().navigate(action)
        }, onAddToCartClick = {
            viewModel.addToCart(it)
            Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_SHORT).show()
        })

        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@HomeFragment.productAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeObjects() {

        viewModel.productState.observe(viewLifecycleOwner) { state ->
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

        viewModel.categoriesState.observe(viewLifecycleOwner) { state ->
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

    private fun setupCategoryChips(categories: List<String>) {
        binding.chipCategories.removeAllViews()

        categories.forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category.replaceFirstChar { it.uppercase() }
                isCheckable = true
                isClickable = true

                setOnClickListener {
                    val searchItem = binding.toolbarHome.menu.findItem(R.id.search)
                    if (searchItem.isActionViewExpanded) {
                        searchItem.collapseActionView()
                    }
                    viewModel.onCategorySelected(category)
                }
            }
            binding.chipCategories.addView(chip)

        }
    }

    private fun setupToolbar() {

        val searchItem = binding.toolbarHome.menu.findItem(R.id.search)
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

        searchView.queryHint = "Search products..."

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.onSearchQueryChanged("")
                } else {
                    binding.chipCategories.clearCheck()
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.chipCategories.clearCheck()
                viewModel.onSearchQueryChanged(query.orEmpty())
                searchView.clearFocus()
                return true
            }
        })

        searchItem.setOnActionExpandListener(object : android.view.MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                viewModel.onSearchQueryChanged("")
                return true
            }

            override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                binding.chipCategories.clearCheck()
                return true
            }
        })

        searchView.setOnCloseListener {
            viewModel.onSearchQueryChanged("")
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}