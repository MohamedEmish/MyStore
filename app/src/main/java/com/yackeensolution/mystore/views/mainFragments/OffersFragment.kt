package com.yackeensolution.mystore.views.mainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.adapters.OffersAdapter
import com.yackeensolution.mystore.data.viewModels.OffersViewModel
import com.yackeensolution.mystore.models.Offer

class OffersFragment : Fragment() {
    private var progressBar: ProgressBar? = null
    private var rootView: View? = null
    private var emptyView: View? = null
    private var offersRecycleView: RecyclerView? = null
    private var offersAdapter: OffersAdapter? = null
    private var offersViewModel: OffersViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val nullParent: ViewGroup? = null
        rootView = inflater.inflate(R.layout.fragment_offers, nullParent)

        progressBar = rootView!!.findViewById(R.id.pb_offers_fragment)
        emptyView = rootView!!.findViewById(R.id.empty_offers)

        offersRecycleView = rootView!!.findViewById(R.id.rv_offers)
        offersRecycleView!!.layoutManager = LinearLayoutManager(context)
        offersRecycleView!!.setHasFixedSize(true)
        offersRecycleView!!.addItemDecoration(DividerItemDecoration(offersRecycleView!!.context, DividerItemDecoration.VERTICAL))
        offersAdapter = OffersAdapter(context)
        offersRecycleView!!.adapter = offersAdapter


        return rootView
    }

    override fun onResume() {
        super.onResume()
        offersViewModel = ViewModelProviders.of(this).get(OffersViewModel::class.java)
        setUpData()
    }

    private fun setUpData() {
        context?.let {
            offersViewModel!!.getNews(it).observe(this, Observer<List<Offer>> { offers ->
                if (offers.isNotEmpty()) {
                    progressBar?.visibility = View.GONE
                    offersAdapter!!.submitList(offers)
                    offersAdapter!!.notifyDataSetChanged()
                } else {
                    progressBar?.visibility = View.GONE
                    emptyView!!.visibility = View.VISIBLE

                }
            })
        }
    }
}