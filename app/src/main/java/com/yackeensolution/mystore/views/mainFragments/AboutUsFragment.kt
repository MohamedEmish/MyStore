package com.yackeensolution.mystore.views.mainFragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.data.viewModels.CollectionViewModel
import com.yackeensolution.mystore.models.AboutUsResponse
import com.yackeensolution.mystore.views.aboutUsTabActivities.AboutUsActivity
import com.yackeensolution.mystore.views.aboutUsTabActivities.BranchesActivity
import com.yackeensolution.mystore.views.aboutUsTabActivities.ContactUsActivity

class AboutUsFragment : Fragment() {
    private var facebook: ImageView? = null
    private var twitter: ImageView? = null
    private var pintRest: ImageView? = null
    private var youtube: ImageView? = null
    private var inst: ImageView? = null
    private var contactUs: ImageView? = null
    private var location: ImageView? = null
    private var aboutUsTextView: ImageView? = null
    private var loadingView: View? = null
    private var rootView: View? = null
    private var collectionViewModel: CollectionViewModel? = null


    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val nullParent: ViewGroup? = null
        rootView = inflater.inflate(R.layout.fragment_about_us, nullParent)
        initViews()

        contactUs!!.setOnClickListener { startActivity(Intent(activity, ContactUsActivity::class.java)) }
        location!!.setOnClickListener { startActivity(Intent(activity, BranchesActivity::class.java)) }

        return rootView
    }

    private fun getAboutUs() {
        context?.let {
            collectionViewModel!!.aboutUs.observe(this, Observer<AboutUsResponse> { aboutUs ->
                if (aboutUs != null) {
                    loadingView?.visibility = View.GONE
                    setOnClickListeners(aboutUs)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        collectionViewModel = ViewModelProviders.of(this).get(CollectionViewModel::class.java)
        getAboutUs()
    }


    private fun initViews() {
        facebook = rootView!!.findViewById(R.id.iv_facebook)
        twitter = rootView!!.findViewById(R.id.iv_twitter)
        pintRest = rootView!!.findViewById(R.id.iv_pinterest)
        youtube = rootView!!.findViewById(R.id.iv_youtube)
        inst = rootView!!.findViewById(R.id.iv_instagram)
        contactUs = rootView!!.findViewById(R.id.iv_contact_us)
        location = rootView!!.findViewById(R.id.iv_location)
        aboutUsTextView = rootView!!.findViewById(R.id.iv_about_us)
        loadingView = rootView!!.findViewById(R.id.pb_about_us_fragment)
    }

    private fun setOnClickListeners(aboutUsResponse: AboutUsResponse?) {
        aboutUsTextView!!.setOnClickListener {
            val aboutUsIntent = Intent(activity, AboutUsActivity::class.java)
            aboutUsIntent.putExtra("description", aboutUsResponse!!.description)
            startActivity(aboutUsIntent)
        }

        youtube!!.setOnClickListener {
            val youtubeIntent = Intent(Intent.ACTION_VIEW)
            youtubeIntent.data = Uri.parse(aboutUsResponse!!.youtubeLink)
            startActivity(youtubeIntent)
        }


        pintRest!!.setOnClickListener {
            val pintRestIntent = Intent(Intent.ACTION_VIEW)
            pintRestIntent.data = Uri.parse(aboutUsResponse!!.pintrestLink)
            startActivity(pintRestIntent)
        }


        inst!!.setOnClickListener {
            val instIntent = Intent(Intent.ACTION_VIEW)
            instIntent.data = Uri.parse(aboutUsResponse!!.instagramLink)
            startActivity(instIntent)
        }

        twitter!!.setOnClickListener {
            val twitterIntent = Intent(Intent.ACTION_VIEW)
            twitterIntent.data = Uri.parse(aboutUsResponse!!.twitterLink)
            startActivity(twitterIntent)
        }

        facebook!!.setOnClickListener {
            val facebookIntent = Intent(Intent.ACTION_VIEW)
            facebookIntent.data = Uri.parse(aboutUsResponse!!.facebookLink)
            startActivity(facebookIntent)
        }
    }
}