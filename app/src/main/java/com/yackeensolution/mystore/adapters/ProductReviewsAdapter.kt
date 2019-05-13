package com.yackeensolution.mystore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.models.ProductReview

class ProductReviewsAdapter(private val mContext: Context?) : ListAdapter<ProductReview, ProductReviewsAdapter.ProductReviewViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_product_review, parent, false)
        return ProductReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductReviewViewHolder, position: Int) {
        if (mContext == null) {
            return
        }
        val currentReview = getItem(position)

        if (currentReview.reviewTitle == "") {
            holder.reviewTitle.visibility = View.GONE
        } else {
            holder.reviewTitle.text = currentReview.reviewTitle
        }
        if (currentReview.review == "") {
            holder.reviewBody.visibility = View.GONE
        } else {
            holder.reviewBody.text = currentReview.review
        }
        holder.reviewPublisher.text = currentReview.name
        holder.ratingBar.rating = currentReview.rate
        holder.reviewDate.text = currentReview.dateTime
    }

    inner class ProductReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var reviewTitle: TextView
        var reviewBody: TextView
        var reviewPublisher: TextView
        var reviewDate: TextView
        var ratingBar: RatingBar

        init {
            reviewTitle = itemView.findViewById(R.id.tv_my_review_title)
            reviewBody = itemView.findViewById(R.id.tv_product_review_body)
            reviewPublisher = itemView.findViewById(R.id.tv_review_publisher)
            ratingBar = itemView.findViewById(R.id.rb_my_review)
            reviewDate = itemView.findViewById(R.id.tv_review_date)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductReview>() {
            override fun areItemsTheSame(oldItem: ProductReview, newItem: ProductReview): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProductReview, newItem: ProductReview): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
