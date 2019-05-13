package com.yackeensolution.mystore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.models.Product

class ProductsToOrderAdapter(private val mContext: Context?) : ListAdapter<Product, ProductsToOrderAdapter.ProductViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_product_to_order, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        if (mContext == null) {
            return
        }
        val currentProduct = getMemberAt(position)

        if (currentProduct.imageUrl == "") {
            holder.productToOrderImage.visibility = View.GONE
        } else {
            Glide
                    .with(holder.productToOrderImage)
                    .load("http://yakensolution.cloudapp.net/talentadmin/Content/images/" + currentProduct.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.productToOrderImage)
        }
        holder.productToOrderTitle.text = currentProduct.title
        holder.productAmount.text = (currentProduct.amount).toString()
        holder.newPrice.text = (currentProduct.price).toString()
        if (currentProduct.discount == 0.toFloat()) {
            holder.oldPrice.visibility = View.GONE
            holder.oldPriceEgp.visibility = View.GONE
        } else {
            holder.oldPrice.text = (currentProduct.price).toString()
            val newPrice = currentProduct.price - currentProduct.discount
            holder.newPrice.text = newPrice.toString()
        }


    }

    private fun getMemberAt(position: Int): Product {
        return getItem(position)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var productToOrderImage: ImageView
        var productToOrderTitle: TextView
        var newPrice: TextView
        var oldPrice: TextView
        var productAmount: TextView
        var oldPriceEgp: TextView

        init {
            productToOrderImage = itemView.findViewById(R.id.iv_product_to_order_image)
            productToOrderTitle = itemView.findViewById(R.id.tv_product_to_order_title)
            newPrice = itemView.findViewById(R.id.tv_product_to_order_new_price)
            oldPrice = itemView.findViewById(R.id.tv_product_to_order_old_price)
            oldPriceEgp = itemView.findViewById(R.id.tv_product_to_order_egp)
            productAmount = itemView.findViewById(R.id.tv_product_to_order_amount)

        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
