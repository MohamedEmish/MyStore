package com.yackeensolution.mystore.views


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yackeensolution.mystore.*
import com.yackeensolution.mystore.views.mainFragments.*

class MainActivity : AppCompatActivity() {

    private var key: String? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                this@MainActivity.supportActionBar!!.setTitle(R.string.home)
                val homeFragment = HomeFragment()
                fragmentTransaction(homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_news -> {
                this@MainActivity.supportActionBar!!.setTitle(R.string.news)
                val newsFragment = NewsFragment()
                fragmentTransaction(newsFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_offers -> {
                this@MainActivity.supportActionBar!!.setTitle(R.string.offers)
                val offersFragment = OffersFragment()
                fragmentTransaction(offersFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_me -> {
                this@MainActivity.supportActionBar!!.setTitle(R.string.me)
                val meFragment = MeFragment()
                fragmentTransaction(meFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_about_us -> {
                this@MainActivity.supportActionBar!!.setTitle(R.string.about_us)
                val aboutUsFragment = AboutUsFragment()
                fragmentTransaction(aboutUsFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this)
        setContentView(R.layout.activity_main)

        val mainContainer = findViewById<View>(R.id.main_container)
        Utils.rtlSupport(this, mainContainer)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.home)

        val navigation = findViewById<BottomNavigationView>(R.id.navigation_view)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.nav_home
    }

    private fun fragmentTransaction(fragment: Fragment) {

        fragment.enterTransition = Fade(Fade.IN)
        fragment.exitTransition = Fade(Fade.OUT)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false)

        val queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                key = query.trim { it <= ' ' }
                val intent = Intent(this@MainActivity, SearchResultsActivity::class.java)
                intent.putExtra("key", key)
                startActivity(intent)
                return true
            }
        }
        searchView.setOnQueryTextListener(queryTextListener)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.action_cart -> {

                val name = SaveSharedPreference.getUserId(this)
                if (name != -1) {
                    startActivity(Intent(this@MainActivity, CartActivity::class.java))
                } else {
                    val dialog = LoginDialog(this)
                    dialog.show()
                }
                return true
            }

            R.id.action_filter -> {
                val filterDialog = FilterDialog(this@MainActivity)
                filterDialog.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

