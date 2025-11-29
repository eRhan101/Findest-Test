package com.example.findesttest.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.findesttest.data.db.CartEntity
import com.example.findesttest.databinding.ItemCartBinding

class CartAdapter(
    private val onIncreaseClick: (CartEntity) -> Unit,
    private val onDecreaseClick: (CartEntity) -> Unit,
    private val onDeleteClick: (CartEntity) -> Unit
) : ListAdapter<CartEntity, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    class CartDiffCallback : DiffUtil.ItemCallback<CartEntity>() {
        override fun areItemsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartEntity) {
            binding.tvProductName.text = item.title
            binding.tvCategories.text = item.category
            binding.tvPrice.text = "${item.price}$"
            binding.tvQuantity.text = item.quantity.toString()

            binding.ivProductImage.load(item.image) {
                crossfade(true)
            }

            binding.btnIncrease.setOnClickListener {
                onIncreaseClick(item)
            }
            binding.btnDecrease.setOnClickListener {
                onDecreaseClick(item)
            }
            binding.btnDelete.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }
}