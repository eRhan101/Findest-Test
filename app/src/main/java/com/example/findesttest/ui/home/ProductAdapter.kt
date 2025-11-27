package com.example.findesttest.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.findesttest.R
import com.example.findesttest.data.model.ProductDto
import com.google.android.material.button.MaterialButton

class ProductAdapter(
    private val onItemClick: (ProductDto) -> Unit,
    private val onAddToCartClick: (ProductDto) -> Unit
) : ListAdapter<ProductDto, ProductAdapter.ProductViewHolder>(DiffCallBack) {

    object DiffCallBack : DiffUtil.ItemCallback<ProductDto>() {
        override fun areItemsTheSame(oldItem: ProductDto, newItem: ProductDto): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ProductDto, newItem: ProductDto): Boolean = oldItem == newItem
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivProduct: ImageView = itemView.findViewById(R.id.iv_product_image)
        private val tvProductName: TextView = itemView.findViewById(R.id.tv_product_name)
        private val tvCategory: TextView = itemView.findViewById(R.id.tv_categories)
        private val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        private val btnAdd: MaterialButton = itemView.findViewById(R.id.btn_add)

        fun bind(item: ProductDto){
            tvProductName.text = item.title
            tvCategory.text = item.category
            tvPrice.text = "${item.price}$"

            ivProduct.load(item.image){
                crossfade(true)
                //still need error placeholder
            }

            itemView.setOnClickListener{
                onItemClick(item)
            }

            btnAdd.setOnClickListener{
                onAddToCartClick(item)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductAdapter.ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}