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

class CartAdapter(private val mContext: Context?) : ListAdapter<Product, CartAdapter.CartViewHolder>(DIFF_CALLBACK) {

    private var listener: OnItemClickListener? = null
    private var addListener: OnItemAddClickListener? = null
    private var minusListener: OnItemMinusClickListener? = null
    private var deleteListener: OnItemDeleteClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        if (mContext == null) {
            return
        }

        val currentProduct = getItem(position)

        Glide
                .with(holder.productImage)
                .load("http://yakensolution.cloudapp.net/talentadmin/Content/images/" + currentProduct.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.productImage)
        holder.cartTitle.text = currentProduct.title
        holder.productAmount.text = currentProduct.amount.toString()
        holder.newPrice.text = currentProduct.price.toString()
        if (currentProduct.discount == 0f) {
            holder.oldPrice.visibility = View.GONE
            holder.oldPriceEgp.visibility = View.GONE
        }
        holder.oldPrice.text = currentProduct.price.toString()
        val newPrice = (currentProduct.price - currentProduct.discount).toDouble()
        holder.newPrice.text = newPrice.toString()
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

    fun setOnItemAddClickListener(listener: OnItemAddClickListener) {
        this.addListener = listener
    }

    interface OnItemAddClickListener {
        fun onItemClick(product: Product)
    }

    fun setOnItemMinusClickListener(listener: OnItemMinusClickListener) {
        this.minusListener = listener
    }

    interface OnItemMinusClickListener {
        fun onItemClick(product: Product)
    }

    fun setOnItemDeleteClickListener(listener: OnItemDeleteClickListener) {
        this.deleteListener = listener
    }

    interface OnItemDeleteClickListener {
        fun onItemClick(product: Product)
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var productImage: ImageView
        var cartTitle: TextView
        var newPrice: TextView
        var oldPrice: TextView
        var productAmount: TextView
        var oldPriceEgp: TextView
        var deleteProduct: ImageView
        var addOne: ImageView
        var minusOne: ImageView

        init {
            productImage = itemView.findViewById(R.id.iv_cart_image)
            productAmount = itemView.findViewById(R.id.tv_cart_amount)
            newPrice = itemView.findViewById(R.id.tv_cart_new_price)
            oldPrice = itemView.findViewById(R.id.tv_cart_old_price)
            deleteProduct = itemView.findViewById(R.id.iv_delete_image)
            addOne = itemView.findViewById(R.id.iv_cart_plus)
            minusOne = itemView.findViewById(R.id.iv_cart_minus)
            cartTitle = itemView.findViewById(R.id.tv_cart_title)
            oldPriceEgp = itemView.findViewById(R.id.tv_cart_old_price_egp)


            itemView.setOnClickListener { v ->
                if (listener != null) {
                    val position = adapterPosition
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener!!.onItemClick(getItem(position))
                    }
                }
            }

            deleteProduct.setOnClickListener { v ->
                if (deleteListener != null) {
                    val position = adapterPosition
                    if (deleteListener != null && position != RecyclerView.NO_POSITION) {
                        deleteListener!!.onItemClick(getItem(position))
                    }
                }
            }

            addOne.setOnClickListener { v ->
                if (addListener != null) {
                    val position = adapterPosition
                    if (addListener != null && position != RecyclerView.NO_POSITION) {
                        addListener!!.onItemClick(getItem(position))
                    }
                }
            }

            minusOne.setOnClickListener { v ->
                if (minusListener != null) {
                    val position = adapterPosition
                    if (minusListener != null && position != RecyclerView.NO_POSITION) {
                        minusListener!!.onItemClick(getItem(position))
                    }
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