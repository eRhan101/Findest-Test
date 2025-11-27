package com.example.findesttest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.findesttest.R
import com.example.findesttest.utils.UiState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()


    private lateinit var rvProducts: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvProducts = view.findViewById(R.id.rv_products)
        progressBar = view.findViewById(R.id.progress_bar)
        tvError = view.findViewById(R.id.tv_error)

        setupRecyclerView()
        observeObjects()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            onItemClick = {
                //TODO: navigate to detail
                Toast.makeText(requireContext(), it.title, Toast.LENGTH_SHORT).show()
            },
            onAddToCartClick = {
                //TODO: add to cart
                Toast.makeText(requireContext(), "Add to cart ${it.title}", Toast.LENGTH_SHORT)
                    .show()
            }
        )

        rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeObjects() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.productState.collect{ state ->
                    when(state){
                        is UiState.Loading -> {
                            progressBar.visibility = View.VISIBLE
                            rvProducts.visibility = View.GONE
                            tvError.visibility = View.GONE
                        }
                        is UiState.Success -> {
                            progressBar.visibility = View.GONE
                            rvProducts.visibility = View.VISIBLE
                            tvError.visibility = View.GONE
                            productAdapter.submitList(state.data)
                        }
                        is UiState.Error -> {
                            progressBar.visibility = View.GONE
                            rvProducts.visibility = View.GONE
                            tvError.visibility = View.VISIBLE
                            tvError.text = state.message
                        }

                    }

                }
            }
        }
    }
}