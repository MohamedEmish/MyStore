package com.yackeensolution.mystore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.models.MyReview

class MyReviewsAdapter(private val mContext: Context?) : ListAdapter<MyReview, MyReviewsAdapter.MyReviewViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_my_review, parent, false)
        return MyReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyReviewViewHolder, position: Int) {
        if (mContext == null) {
            return
        }
        val currentMyReview = getMemberAt(position)

        Glide
                .with(holder.reviewImage)
                .load("http://yakensolution.cloudapp.net/talentadmin/Content/images/" + currentMyReview.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.reviewImage)
        if (currentMyReview.reviewTitle == "") {
            holder.reviewTitle.visibility = View.GONE
        } else {
            holder.reviewTitle.text = currentMyReview.reviewTitle
        }

        if (currentMyReview.review == "") {
            holder.reviewBody.visibility = View.GONE
        } else {
            holder.reviewBody.text = currentMyReview.review
        }
        holder.ratingBar.rating = currentMyReview.rate
        holder.productTitle.text = currentMyReview.productTitle


    }

    private fun getMemberAt(position: Int): MyReview {
        return getItem(position)
    }

    inner class MyReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var reviewImage: ImageView
        var reviewTitle: TextView
        var reviewBody: TextView
        var productTitle: TextView
        var ratingBar: RatingBar

        init {
            reviewImage = itemView.findViewById(R.id.iv_my_review_product_image)
            reviewTitle = itemView.findViewById(R.id.tv_my_review_title)
            reviewBody = itemView.findViewById(R.id.tv_my_review_body)
            productTitle = itemView.findViewById(R.id.tv_my_review_product_title)
            ratingBar = itemView.findViewById(R.id.rb_my_review)


        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MyReview>() {
            override fun areItemsTheSame(oldItem: MyReview, newItem: MyReview): Boolean {
                return oldItem.review == newItem.review
            }

            override fun areContentsTheSame(oldItem: MyReview, newItem: MyReview): Boolean {
                return oldItem.review == newItem.review
            }
        }
    }
}