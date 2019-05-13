package com.yackeensolution.mystore.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide

class ImageSliderAdapter(private val mContext: Context, private val imageList: List<String>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return imageList.size
    }

    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val imageView = ImageView(mContext)
        Glide.with(mContext)
                .load("http://yakensolution.cloudapp.net/talentadmin/Content/images/" + imageList[position])
                .into(imageView)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        container.addView(imageView, 0)

        return imageView
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }

}