package com.yackeensolution.mystore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.models.Order

class MyOrdersAdapter(private val mContext: Context?) : ListAdapter<Order, MyOrdersAdapter.OrderViewHolder>(DIFF_CALLBACK) {
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        if (mContext == null) {
            return
        }
        val currentOrder = getMemberAt(position)

        holder.orderId.text = currentOrder.id.toString()
        holder.orderDate.text = currentOrder.dateTime
        val status = Order.Status.fromCode(currentOrder.statusValue)
        holder.orderStatus.text = status.name


    }

    private fun getMemberAt(position: Int): Order {
        return getItem(position)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(order: Order)
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var orderId: TextView = itemView.findViewById(R.id.tv_order_id)
        var orderDate: TextView = itemView.findViewById(R.id.tv_order_date)
        var orderStatus: TextView = itemView.findViewById(R.id.tv_order_status)

        init {

            itemView.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener!!.onItemClick(getItem(position))
                    }
                }

            }

        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}