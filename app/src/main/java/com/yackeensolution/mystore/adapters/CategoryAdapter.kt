package com.yackeensolution.mystore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.models.Category

class CategoryAdapter(private val mContext: Context?) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(DIFF_CALLBACK) {

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        if (mContext == null) {
            return
        }
        val title = getItem(position).title
        val imageUri = getItem(position).imageUrl

        if (!imageUri.equals("")) {
            Glide.with(holder.categoryImageVew)
                    .load("http://yakensolution.cloudapp.net/talentadmin/Content/images/" + getItem(position).imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.categoryImageVew)
        }

        holder.titleTextView.text = title

    }

    private fun getMemberAt(position: Int): Category {
        return getItem(position)
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(category: Category)
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var titleTextView: TextView
        var categoryImageVew: ImageView
        var layout: LinearLayout

        init {

            titleTextView = itemView.findViewById(R.id.tv_category_title)
            categoryImageVew = itemView.findViewById(R.id.iv_category_image)

            layout = itemView.findViewById(R.id.layout_category_item)
            layout.setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(getItem(position))
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}