package com.yackeensolution.mystore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.models.Branch

class BranchAdapter(private val mContext: Context?) : ListAdapter<Branch, BranchAdapter.BranchViewHolder>(DIFF_CALLBACK) {
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BranchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_branch, parent, false)
        return BranchViewHolder(view)
    }

    override fun onBindViewHolder(holder: BranchViewHolder, position: Int) {
        if (mContext == null) {
            return
        }
        val name = getItem(position).name
        val address = getItem(position).address
        val startAt = getItem(position).startAt
        val endAt = getItem(position).endAt

        holder.nameTV.text = name
        holder.addressTV.text = address
        holder.startAtTV.text = startAt
        holder.endAtTV.text = endAt


    }

    private fun getMemberAt(position: Int): Branch {
        return getItem(position)
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(branch: Branch)
    }

    inner class BranchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nameTV: TextView
        var startAtTV: TextView
        var endAtTV: TextView
        var addressTV: TextView
        var imageView: ImageView
        var layout: ConstraintLayout

        init {

            nameTV = itemView.findViewById(R.id.tv_branch_name)
            startAtTV = itemView.findViewById(R.id.tv_start_at)
            endAtTV = itemView.findViewById(R.id.tv_end_at)
            addressTV = itemView.findViewById(R.id.tv_branch_address)
            imageView = itemView.findViewById(R.id.iv_branch_map)
            layout = itemView.findViewById(R.id.branch_layout)
            layout.setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(getItem(position))
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Branch>() {
            override fun areItemsTheSame(oldItem: Branch, newItem: Branch): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Branch, newItem: Branch): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}