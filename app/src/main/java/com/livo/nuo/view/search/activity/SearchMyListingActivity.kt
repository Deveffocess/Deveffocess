package com.livo.nuo.view.search.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialContainerTransform
import com.jaeger.library.StatusBarUtil
import com.livo.nuo.R
import com.livo.nuo.models.ProductModel
import com.livo.nuo.utility.LocalizeActivity
import com.livo.nuo.view.search.adapter.SearchMyListingAdapter

class SearchMyListingActivity : LocalizeActivity() {

    private var currActivity : Activity = this

    var productListMyListing = ArrayList<ProductModel>()

    lateinit var etSearch:EditText

    var search_text=""

    lateinit var rvMyListings:RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_my_listing)

        MaterialContainerTransform()

        productListMyListing.clear()
        productListMyListing= intent.getSerializableExtra("mylist") as ArrayList<ProductModel>
        search_text=intent.getStringExtra("search")!!

        etSearch=findViewById(R.id.etSearch)


        initViews()

    }

    fun initViews(){

        etSearch.setText(search_text)

    }

    override fun setStatusBar() {
        val mColor = resources.getColor(R.color.colorPrimary)
        StatusBarUtil.setLightMode(currActivity)
    }
}