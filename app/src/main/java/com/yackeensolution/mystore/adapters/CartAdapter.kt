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

        var productImage: ImageView = itemView.findViewById(R.id.iv_cart_image)
        var cartTitle: TextView = itemView.findViewById(R.id.tv_cart_title)
        var newPrice: TextView = itemView.findViewById(R.id.tv_cart_new_price)
        var oldPrice: TextView = itemView.findViewById(R.id.tv_cart_old_price)
        var productAmount: TextView = itemView.findViewById(R.id.tv_cart_amount)
        var oldPriceEgp: TextView = itemView.findViewById(R.id.tv_cart_old_price_egp)
        private var deleteProduct: ImageView = itemView.findViewById(R.id.iv_delete_image)
        private var addOne: ImageView = itemView.findViewById(R.id.iv_cart_plus)
        private var minusOne: ImageView = itemView.findViewById(R.id.iv_cart_minus)

        init {


            itemView.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener!!.onItemClick(getItem(position))
                    }
                }
            }

            deleteProduct.setOnClickListener {
                if (deleteListener != null) {
                    val position = adapterPosition
                    if (deleteListener != null && position != RecyclerView.NO_POSITION) {
                        deleteListener!!.onItemClick(getItem(position))
                    }
                }
            }

            addOne.setOnClickListener {
                if (addListener != null) {
                    val position = adapterPosition
                    if (addListener != null && position != RecyclerView.NO_POSITION) {
                        addListener!!.onItemClick(getItem(position))
                    }
                }
            }

            minusOne.setOnClickListener {
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