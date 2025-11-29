package com.example.findesttest.ui.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.findesttest.data.db.OrderEntity
import com.example.findesttest.databinding.ItemOrderBinding

class OrderAdapter : ListAdapter<OrderEntity, OrderAdapter.OrderViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<OrderEntity>() {
        override fun areItemsTheSame(oldItem: OrderEntity, newItem: OrderEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: OrderEntity, newItem: OrderEntity) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OrderViewHolder(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrderEntity) {
            binding.tvSummary.text = order.items
            binding.tvPrice.text = String.format("$%.2f", order.totalPrice)
            binding.tvOrderId.text = "Order #${order.id}"

            binding.ivPreview.load(order.images) {
                crossfade(true)
            }
        }
    }
}