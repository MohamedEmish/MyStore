package com.yackeensolution.mystore.views.aboutUsTabActivities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.adapters.BranchAdapter
import com.yackeensolution.mystore.data.viewModels.BranchesViewModel
import com.yackeensolution.mystore.models.Branch
import com.yackeensolution.mystore.utils.Utils
import java.util.*

class BranchesActivity : AppCompatActivity() {
    private var progressBar: ProgressBar? = null
    private var branchesRecycleView: RecyclerView? = null
    private var branchAdapter: BranchAdapter? = null
    private var branchesViewModel: BranchesViewModel? = null
    private var branchList = ArrayList<Branch>()


    override fun onResume() {
        super.onResume()
        branchesViewModel = ViewModelProviders.of(this).get(BranchesViewModel::class.java)
        setUpData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_branches)

        val mainContainer = findViewById<View>(R.id.branches_main_container)
        Utils.rtlSupport(this, mainContainer)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.branches)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        progressBar = findViewById(R.id.pb_branches)
        branchesRecycleView = findViewById(R.id.rv_branches)
        branchesRecycleView!!.layoutManager = LinearLayoutManager(this)
        branchesRecycleView!!.setHasFixedSize(true)
        branchAdapter = BranchAdapter(this)
        branchAdapter!!.submitList(branchList)
        branchesRecycleView!!.adapter = branchAdapter


        branchAdapter!!.setOnItemClickListener(object : BranchAdapter.OnItemClickListener {
            override fun onItemClick(branch: Branch) {
                val longitude = branch.lang
                val latitude = branch.lat
                goForDirection(latitude, longitude)
            }
        })
    }

    private fun goForDirection(latitude: Double?, longitude: Double?) {
        val uri = String.format(Locale.getDefault(), "geo:%f,%f", latitude, longitude)
        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        if (mapIntent.resolveActivity(this@BranchesActivity.packageManager) != null) {
            this@BranchesActivity.startActivity(mapIntent)
        }
        startActivity(mapIntent)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpData() {
        this.let {
            branchesViewModel!!.getAllBranches().observe(this, Observer<List<Branch>> { branches ->
                if (branches.isNotEmpty()) {
                    setData(branches)
                }
            })
        }
    }

    private fun setData(branches: List<Branch>?) {
        if (branches != null) {
            if (branches.isNotEmpty()) {
                progressBar?.visibility = View.GONE
                branchList.clear()
                branches.forEach {
                    branchList.add(it)
                    branchAdapter!!.notifyDataSetChanged()
                }
            }
        }
    }
}