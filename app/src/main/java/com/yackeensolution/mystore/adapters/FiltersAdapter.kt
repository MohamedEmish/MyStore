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
import com.yackeensolution.mystore.R

class FiltersAdapter(private val mContext: Context?) : ListAdapter<String, FiltersAdapter.StringViewHolder>(DIFF_CALLBACK) {
    private var listener: OnDeleteClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_filter, parent, false)
        return StringViewHolder(view)
    }

    override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
        if (mContext == null) {
            return
        }
        val currentFilter = getMemberAt(position)

        holder.mDeleteImageView.setImageResource(R.drawable.ic_delete)
        holder.mFilterTextView.text = currentFilter

    }

    private fun getMemberAt(position: Int): String {
        return getItem(position)
    }


    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        this.listener = listener
    }

    interface OnDeleteClickListener {
        fun onItemClick(position: Int)
    }

    inner class StringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mDeleteImageView: ImageView
        var mFilterTextView: TextView

        init {
            mDeleteImageView = itemView.findViewById(R.id.iv_delete_filter)
            mFilterTextView = itemView.findViewById(R.id.tv_filter_result)


            mDeleteImageView.setOnClickListener { v ->
                if (listener != null) {
                    val position = adapterPosition
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener!!.onItemClick(position)
                    }
                }
            }
        }

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}