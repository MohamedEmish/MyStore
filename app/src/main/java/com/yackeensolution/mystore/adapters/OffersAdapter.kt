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
import com.yackeensolution.mystore.models.Offer

class OffersAdapter(private val mContext: Context?) : ListAdapter<Offer, OffersAdapter.OfferViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_offer, parent, false)
        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        if (mContext == null) {
            return
        }
        val currentOffer = getMemberAt(position)

        Glide
                .with(holder.mOfferImage)
                .load("http://yakensolution.cloudapp.net/talentadmin/Content/images/" + currentOffer.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.mOfferImage)
        holder.mOfferTitle.text = currentOffer.title
        holder.mOfferDescription.text = currentOffer.description
        holder.mOfferDiscount.text = currentOffer.discount.toString()
    }

    private fun getMemberAt(position: Int): Offer {
        return getItem(position)
    }

    inner class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mOfferImage: ImageView
        var mOfferTitle: TextView
        var mOfferDescription: TextView
        var mOfferDiscount: TextView

        init {
            mOfferImage = itemView.findViewById(R.id.iv_my_review_product_image)
            mOfferTitle = itemView.findViewById(R.id.tv_my_review_title)
            mOfferDescription = itemView.findViewById(R.id.tv_offer_description)
            mOfferDiscount = itemView.findViewById(R.id.tv_discount)


        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Offer>() {
            override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}