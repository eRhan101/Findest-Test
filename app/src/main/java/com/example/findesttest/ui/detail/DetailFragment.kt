package com.example.findesttest.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.findesttest.databinding.FragmentDetailBinding
import com.example.findesttest.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()

    private val args: DetailFragmentArgs by navArgs()

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadProductDetail(args.productId)

        setupListener()
        observeObjects()
    }

    private fun setupListener() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnAddToCart.setOnClickListener {
            val currentState = viewModel.detailState.value
            if (currentState is UiState.Success) {
                viewModel.addToCart(currentState.data)
                Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeObjects() {
        viewModel.detailState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                    binding.nestedScrollView.visibility = View.GONE
                    binding.btnAddToCart.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.GONE
                    binding.nestedScrollView.visibility = View.VISIBLE
                    binding.btnAddToCart.visibility = View.VISIBLE

                    val product = state.data
                    binding.tvDetailTitle.text = product.title
                    binding.tvDetailCategory.text = product.category
                    binding.tvDetailPrice.text = "${product.price}$"
                    binding.tvDetailDescription.text = product.description

                    binding.ivDetailImage.load(product.image) {
                        crossfade(true)
                    }
                }

                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = state.message
                    binding.nestedScrollView.visibility = View.GONE
                    binding.btnAddToCart.visibility = View.GONE
                }

            }
        }
    }
}