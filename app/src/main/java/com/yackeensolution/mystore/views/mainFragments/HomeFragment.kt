package com.yackeensolution.mystore.views.mainFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.adapters.CategoryAdapter
import com.yackeensolution.mystore.data.viewModels.CategoryViewModel
import com.yackeensolution.mystore.models.Category
import com.yackeensolution.mystore.views.homeTabActivities.ProductsActivity


class HomeFragment : Fragment() {

    private var categoryRecycleView: RecyclerView? = null
    private var categoryAdapter: CategoryAdapter? = null
    private var categoryViewModel: CategoryViewModel? = null
    private var progressBar: ProgressBar? = null


    override fun onResume() {
        super.onResume()
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        setUpData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val nullParent: ViewGroup? = null

        val rootView = inflater.inflate(R.layout.fragment_home, nullParent)
        progressBar = rootView.findViewById(R.id.pb_fragment_home)

        categoryRecycleView = rootView.findViewById(R.id.rv_categories)
        categoryRecycleView!!.layoutManager = GridLayoutManager(context, 2)
        categoryRecycleView!!.setHasFixedSize(true)
        categoryAdapter = CategoryAdapter(context)
        categoryRecycleView!!.adapter = categoryAdapter
        categoryAdapter!!.setOnItemClickListener(object : CategoryAdapter.OnItemClickListener {
            override fun onItemClick(category: Category) {
                val productsIntent = Intent(activity, ProductsActivity::class.java)
                productsIntent.putExtra("categoryId", category.id)
                productsIntent.putExtra("categoryTitle", category.title)
                startActivity(productsIntent)
            }
        })
        return rootView
    }

    private fun setUpData() {
        context?.let {
            categoryViewModel!!.getAllCategories(it).observe(this, Observer<List<Category>> { categories ->
                if (categories.isNotEmpty()) {
                    progressBar?.visibility = View.GONE
                    categoryAdapter!!.submitList(categories)
                    categoryAdapter!!.notifyDataSetChanged()
                }
            })
        }
    }
}
