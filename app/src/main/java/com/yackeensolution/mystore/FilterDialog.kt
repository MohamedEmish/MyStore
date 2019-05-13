package com.yackeensolution.mystore

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.yackeensolution.mystore.data.RetrofitClass
import com.yackeensolution.mystore.data.StoreApi
import com.yackeensolution.mystore.models.*
import com.yackeensolution.mystore.views.FilterResultsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class FilterDialog(context: Context) : Dialog(context) {
    private var mCategoryFilter: Spinner? = null
    private var mAgeFilter: Spinner? = null
    private var mGenderFilter: Spinner? = null
    private var mIntelligenceFilter: Spinner? = null
    private var mPlayersNumberFilter: Spinner? = null
    private var mFilterButton: Button? = null
    private var mLanguage: String? = ""
    private var gender: String = ""
    private var minPriceEditText: EditText? = null
    private var maxPriceEditText: EditText? = null
    private var minPrice: Int = 0
    private var maxPrice: Int = 0
    private var categoryId: Int = 0
    private var ageId: Int = 0
    private var intelligenceId: Int = 0
    private var numberOfPlayersId: Int = 0
    private var categoryIds: MutableList<Int>? = null
    private var ageIds: MutableList<Int>? = null
    private var intelligenceIds: MutableList<Int>? = null
    private var numberOfPlayersIds: MutableList<Int>? = null
    private val selectedFilters = ArrayList<Int>()
    private val selectedFiltersTitles = ArrayList<String>()
    private var loadingView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        Utils.setLocale(context)
        setContentView(R.layout.dialog_filter)
        val mainContainer = findViewById<View>(R.id.filter_dialog_main_container)
        Utils.rtlSupport(context, mainContainer)

        initViews()

        mLanguage = SaveSharedPreference.getLanguage(context)

        val apiInterface = RetrofitClass.storeApi
        getFilters(apiInterface)


        val genderArray = context.resources.getStringArray(R.array.gender_array)
        val list = Arrays.asList(*genderArray)
        val genderAdapter = createSpinnerAdapter(list)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mGenderFilter!!.adapter = genderAdapter
    }

    private fun getFilters(storeApi: StoreApi) {
        val categoryFilterCall = storeApi.getCategories(mLanguage)
        categoryFilterCall.enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, @Nullable response: Response<List<Category>>) {
                val categoryList = ArrayList<String>()
                categoryIds = ArrayList()
                categoryList.add(context.resources.getString(R.string.filter_by_category))
                if (response.body() != null) {
                    for (category in response.body()!!) {
                        categoryList.add(category.title)
                        categoryIds!!.add(category.id)
                    }
                }
                setupSpinner(categoryList, mCategoryFilter)


            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.d(TAG, "onCategoryCallFailure: " + t.message)
            }
        })

        val ageFilterCall = storeApi.ages
        ageFilterCall.enqueue(object : Callback<List<Age>> {
            override fun onResponse(call: Call<List<Age>>, response: Response<List<Age>>) {
                val ageList = ArrayList<String>()
                ageIds = ArrayList()
                ageList.add(context.resources.getString(R.string.filter_by_age_range))
                if (response.body() != null) {
                    for (age in response.body()!!) {
                        ageList.add(age.value)
                        ageIds!!.add(age.id)
                    }
                }
                setupSpinner(ageList, mAgeFilter)
            }

            override fun onFailure(call: Call<List<Age>>, t: Throwable) {
                Log.d(TAG, "onAgeCallFailure: " + t.message)
            }
        })

        val intelligenceFilterCall = storeApi.getIntelligence(mLanguage)
        intelligenceFilterCall.enqueue(object : Callback<List<Intelligence>> {
            override fun onResponse(call: Call<List<Intelligence>>, response: Response<List<Intelligence>>) {
                val intelligenceList = ArrayList<String>()
                intelligenceIds = ArrayList()
                intelligenceList.add(context.resources.getString(R.string.filter_by_intelligence))
                if (response.body() != null) {
                    for (intelligence in response.body()!!) {
                        intelligenceList.add(intelligence.title)
                        intelligenceIds!!.add(intelligence.id)
                    }
                }
                setupSpinner(intelligenceList, mIntelligenceFilter)

            }

            override fun onFailure(call: Call<List<Intelligence>>, t: Throwable) {
                Log.d(TAG, "onIntelligenceCallFailure: " + t.message)
            }
        })

        val numberOfPlayersFilterCall = storeApi.numberOfPlayers
        numberOfPlayersFilterCall.enqueue(object : Callback<List<NumberOfPlayers>> {
            override fun onResponse(call: Call<List<NumberOfPlayers>>, response: Response<List<NumberOfPlayers>>) {
                val numberOfPlayersList = ArrayList<String>()
                numberOfPlayersIds = ArrayList()
                numberOfPlayersList.add(context.resources.getString(R.string.filter_by_number_of_players))
                if (response.body() != null) {
                    for (numberOfPlayers in response.body()!!) {
                        numberOfPlayersList.add(numberOfPlayers.value)
                        numberOfPlayersIds!!.add(numberOfPlayers.id)
                    }
                }
                setupSpinner(numberOfPlayersList, mPlayersNumberFilter)
                loadingView!!.visibility = View.GONE
                mFilterButton!!.isEnabled = true
                setOnSpinnersClickListener()
                setFilterButtonOnClickListener()
            }

            override fun onFailure(call: Call<List<NumberOfPlayers>>, t: Throwable) {
                Log.d(TAG, "onNumberOfPlayersCallFailure: " + t.message)
            }
        })
    }


    private fun initViews() {
        mFilterButton = findViewById(R.id.btn_filter)
        mCategoryFilter = findViewById(R.id.category_filter_spinner)
        mAgeFilter = findViewById(R.id.age_filter_spinner)
        mGenderFilter = findViewById(R.id.gender_filter_spinner)
        mIntelligenceFilter = findViewById(R.id.intelligence_filter_spinner)
        mPlayersNumberFilter = findViewById(R.id.players_number_filter_spinner)
        minPriceEditText = findViewById(R.id.et_min_price)
        maxPriceEditText = findViewById(R.id.et_max_price)
        loadingView = findViewById(R.id.loading_view_filter_dialog)
    }


    private fun setupSpinner(strings: List<String>, spinner: Spinner?) {
        val spinnerAdapter = createSpinnerAdapter(strings)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = spinnerAdapter
    }

    private fun createSpinnerAdapter(strings: List<String>): ArrayAdapter<String> {
        return object : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
                strings) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?,
                                         parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0) {
                    tv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    tv.setTypeface(null, Typeface.BOLD)
                } else {
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }
        }
    }

    private fun setOnSpinnersClickListener() {
        mCategoryFilter!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val itemId = mCategoryFilter!!.selectedItemPosition
                if (itemId > 0) {
                    selectedFiltersTitles.add(parent.getItemAtPosition(position).toString())

                    categoryId = categoryIds!![itemId - 1]
                    selectedFilters.add(categoryId)
                    Log.d(TAG, "onItemSelected: categoryId= $categoryId")
                    Log.d(TAG, "onItemSelected: categoryIds= $categoryIds")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        mAgeFilter!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val itemId = mAgeFilter!!.selectedItemPosition
                if (itemId > 0) {
                    selectedFiltersTitles.add(parent.getItemAtPosition(position).toString())

                    ageId = ageIds!![itemId - 1]
                    selectedFilters.add(ageId)
                    Log.d(TAG, "onItemSelected: ageId= $ageId")
                    Log.d(TAG, "onItemSelected: ageIds= $ageIds")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        mIntelligenceFilter!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val itemId = parent.selectedItemPosition
                if (itemId > 0) {
                    selectedFiltersTitles.add(parent.getItemAtPosition(position).toString())

                    intelligenceId = intelligenceIds!![itemId - 1]
                    selectedFilters.add(intelligenceId)
                    Log.d(TAG, "onItemSelected: intelligenceId= $intelligenceId")
                    Log.d(TAG, "onItemSelected: intelligenceIds= $intelligenceIds")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        mGenderFilter!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val itemId = parent.selectedItemPosition
                if (itemId == 1) {
                    gender = User.Gender.Male.name
                    selectedFiltersTitles.add(gender)
                    selectedFilters.add(3)
                } else if (itemId == 2) {
                    gender = User.Gender.Female.name
                    selectedFiltersTitles.add(gender)
                    selectedFilters.add(3)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        mPlayersNumberFilter!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val itemId = mPlayersNumberFilter!!.selectedItemPosition
                if (itemId > 0) {
                    selectedFiltersTitles.add(mPlayersNumberFilter!!.getItemAtPosition(position).toString())

                    numberOfPlayersId = numberOfPlayersIds!![itemId - 1]
                    selectedFilters.add(numberOfPlayersId)
                    Log.d(TAG, "onItemSelected: numberOfPlayersId= $numberOfPlayersId")
                    Log.d(TAG, "onItemSelected: numberOfPlayersIds= $numberOfPlayersIds")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setFilterButtonOnClickListener() {
        mFilterButton!!.setOnClickListener {
            if (minPriceEditText!!.text.toString() == "") {
                minPrice = -1
            } else {
                minPrice = Integer.parseInt(minPriceEditText!!.text.toString())
                selectedFiltersTitles.add("Minimum price = $minPrice")
                selectedFilters.add(minPrice)
            }

            if (maxPriceEditText!!.text.toString() == "") {
                maxPrice = -1
            } else {
                maxPrice = Integer.parseInt(maxPriceEditText!!.text.toString())
                selectedFiltersTitles.add("Maximum price = $maxPrice")
                selectedFilters.add(maxPrice)
            }
            val filterResultsIntent = Intent(context, FilterResultsActivity::class.java)
            filterResultsIntent.putExtra("categoryId", categoryId)
            filterResultsIntent.putExtra("ageId", ageId)
            filterResultsIntent.putExtra("gender", gender)
            filterResultsIntent.putExtra("intelligenceId", intelligenceId)
            filterResultsIntent.putExtra("numberOfPlayersId", numberOfPlayersId)
            filterResultsIntent.putExtra("minPrice", minPrice)
            filterResultsIntent.putExtra("maxPrice", maxPrice)
            filterResultsIntent.putStringArrayListExtra("selectedFiltersTitles", selectedFiltersTitles)
            filterResultsIntent.putIntegerArrayListExtra("selectedFilters", selectedFilters)
            context.startActivity(filterResultsIntent)
        }
    }

    companion object {

        private const val TAG = "Filter Dialog"
    }


}
