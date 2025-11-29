package com.example.findesttest.ui.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.findesttest.data.db.CartEntity
import com.example.findesttest.databinding.ItemCheckoutBinding

class CheckoutAdapter : ListAdapter<CartEntity, CheckoutAdapter.CheckoutViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<CartEntity>() {
        override fun areItemsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder {
        val binding = ItemCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CheckoutViewHolder(private val binding: ItemCheckoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartEntity) {
            binding.tvProductName.text = item.title
            binding.tvCategory.text = item.category
            binding.tvQuantity.text = "x${item.quantity}"

            val lineTotal = item.price * item.quantity
            binding.tvTotalPrice.text = String.format("$%.2f", lineTotal)

            binding.ivProductImage.load(item.image) {
                crossfade(true)
            }
        }
    }
}