package com.yackeensolution.mystore.views.mainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.adapters.NewsAdapter
import com.yackeensolution.mystore.data.viewModels.NewsViewModel
import com.yackeensolution.mystore.models.News

class NewsFragment : Fragment() {
    private var progressBar: ProgressBar? = null
    private var rootView: View? = null
    private var emptyView: View? = null
    private var newsRecycleView: RecyclerView? = null
    private var newsAdapter: NewsAdapter? = null
    private var newsViewModel: NewsViewModel? = null


    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val nullParent: ViewGroup? = null
        rootView = inflater.inflate(R.layout.fragment_news, nullParent)

        progressBar = rootView!!.findViewById(R.id.pb_news_fragment)
        emptyView = rootView!!.findViewById(R.id.empty_news)

        newsRecycleView = rootView!!.findViewById(R.id.rv_news)
        newsRecycleView!!.layoutManager = LinearLayoutManager(context)
        newsRecycleView!!.setHasFixedSize(true)
        newsRecycleView!!.addItemDecoration(DividerItemDecoration(newsRecycleView!!.context, DividerItemDecoration.VERTICAL))
        newsAdapter = NewsAdapter(context)
        newsRecycleView!!.adapter = newsAdapter

        return rootView
    }


    override fun onResume() {
        super.onResume()
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)
        setUpData()
    }

    private fun setUpData() {
        context?.let {
            newsViewModel!!.getNews(it).observe(this, Observer<List<News>> { news ->
                if (news.isNotEmpty()) {
                    progressBar?.visibility = View.GONE
                    newsAdapter!!.submitList(news)
                    newsAdapter!!.notifyDataSetChanged()
                } else {
                    progressBar?.visibility = View.GONE
                    emptyView!!.visibility = View.VISIBLE

                }
            })
        }
    }
}