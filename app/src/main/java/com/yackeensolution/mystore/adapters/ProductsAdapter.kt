package com.yackeensolution.mystore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.models.Product

class ProductsAdapter(private val mContext: Context?) : ListAdapter<Product, ProductsAdapter.ProductViewHolder>(DIFF_CALLBACK) {

    private var listener: OnItemClickListener? = null
    private var favListener: OnItemFavClickListener? = null
    private var cartListener: OnItemCartClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        if (mContext == null) {
            return
        }

        val isFav = getItem(position).isFav

        if (isFav) {
            holder.favoriteIndicator.setImageResource(R.drawable.ic_liked)
        } else {
            holder.favoriteIndicator.setImageResource(R.drawable.ic_like)
        }

        val isInCart = getItem(position).isInCart

        if (isInCart) {
            holder.addToCartIv.setImageResource(R.drawable.ic_added_to_shopping_bag)
        } else {
            holder.addToCartIv.setImageResource(R.drawable.ic_add_to_shopping_bag)
        }

        Glide.with(holder.productImageView)
                .load("http://yakensolution.cloudapp.net/talentadmin/Content/images/" + getItem(position).imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.productImageView)

        holder.productTitle.text = getItem(position).title
        holder.productPrice.text = (getItem(position).price).toString()
        holder.ratingBar.rating = (getItem(position).rate)

    }

    private fun getMemberAt(position: Int): Product {
        return getItem(position)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(product: Product)
    }

    fun setOnItemFavClickListener(listener: OnItemFavClickListener) {
        this.favListener = listener
    }

    interface OnItemFavClickListener {
        fun onItemClick(product: Product)
    }

    fun setOnItemCartClickListener(listener: OnItemCartClickListener) {
        this.cartListener = listener
    }

    interface OnItemCartClickListener {
        fun onItemClick(product: Product)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var layout: ConstraintLayout
        var productImageView: ImageView
        var productTitle: TextView
        var ratingBar: RatingBar
        var productPrice: TextView
        var favoriteIndicator: ImageView
        var addToCartIv: ImageView

        init {
            productImageView = itemView.findViewById(R.id.iv_product_image)
            productTitle = itemView.findViewById(R.id.tv_product_title)
            ratingBar = itemView.findViewById(R.id.one_product_rating_bar)
            productPrice = itemView.findViewById(R.id.tv_product_price)

            favoriteIndicator = itemView.findViewById(R.id.iv_favourite)
            favoriteIndicator.setOnClickListener {
                val position = adapterPosition
                if (favListener != null && position != RecyclerView.NO_POSITION) {
                    favListener!!.onItemClick(getItem(position))
                }
            }

            addToCartIv = itemView.findViewById(R.id.iv_add_to_cart)
            addToCartIv.setOnClickListener {
                val position = adapterPosition
                if (cartListener != null && position != RecyclerView.NO_POSITION) {
                    cartListener!!.onItemClick(getItem(position))
                }
            }

            layout = itemView.findViewById(R.id.item_product_layout)
            layout.setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(getItem(position))
                }
            }
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