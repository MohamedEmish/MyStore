package com.yackeensolution.mystore.views

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yackeensolution.mystore.R
import com.yackeensolution.mystore.Utils
import com.yackeensolution.mystore.adapters.BranchAdapter
import com.yackeensolution.mystore.data.viewModels.BranchesViewModel
import com.yackeensolution.mystore.models.Branch
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
                Toast.makeText(this@BranchesActivity, branch.name + " clicked", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpData() {
        this.let {
            branchesViewModel!!.getAllBranches(it).observe(this, Observer<List<Branch>> { branches ->
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