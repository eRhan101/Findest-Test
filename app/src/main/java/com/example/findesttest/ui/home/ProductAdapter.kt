package com.example.findesttest.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.findesttest.data.model.ProductDto
import com.example.findesttest.databinding.ItemProductBinding

class ProductAdapter(
    private val onItemClick: (ProductDto) -> Unit,
    private val onAddToCartClick: (ProductDto) -> Unit
) : ListAdapter<ProductDto, ProductAdapter.ProductViewHolder>(DiffCallBack) {

    object DiffCallBack : DiffUtil.ItemCallback<ProductDto>() {
        override fun areItemsTheSame(oldItem: ProductDto, newItem: ProductDto): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ProductDto, newItem: ProductDto): Boolean =
            oldItem == newItem
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductDto) {
            binding.tvProductName.text = item.title
            binding.tvCategories.text = item.category
            binding.tvPrice.text = "${item.price}$"

            binding.ivProductImage.load(item.image) {
                crossfade(true)
                //still need error placeholder
            }

            binding.root.setOnClickListener { onItemClick(item) }
            binding.btnAdd.setOnClickListener { onAddToCartClick(item) }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductAdapter.ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}