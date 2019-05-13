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
import com.yackeensolution.mystore.models.News

class NewsAdapter(private val mContext: Context?) : ListAdapter<News, NewsAdapter.NewsViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        if (mContext == null) {
            return
        }
        val currentNews = getMemberAt(position)

        Glide
                .with(holder.mNewsImage)
                .load("http://yakensolution.cloudapp.net/talentadmin/Content/images/" + currentNews.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.mNewsImage)
        holder.mNewsTitle.text = currentNews.title
        holder.mNewsDescription.text = currentNews.description


    }

    private fun getMemberAt(position: Int): News {
        return getItem(position)
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mNewsImage: ImageView
        var mNewsTitle: TextView
        var mNewsDescription: TextView

        init {
            mNewsImage = itemView.findViewById(R.id.iv_my_review_product_image)
            mNewsTitle = itemView.findViewById(R.id.tv_my_review_title)
            mNewsDescription = itemView.findViewById(R.id.tv_offer_description)


        }

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}