package com.livo.nuo.view.search.activity

import android.R.attr
import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialContainerTransform
import com.jaeger.library.StatusBarUtil
import com.livo.nuo.R
import com.livo.nuo.models.ProductDataModel
import com.livo.nuo.models.ProductModel
import com.livo.nuo.utility.LocalizeActivity
import com.livo.nuo.view.search.activity.adapter.SearchMyListingsAdapter
import com.livo.nuo.view.search.adapter.SearchMyListingAdapter
import com.livo.nuo.view.search.adapter.SearchOtherListingAdapter
import android.R.attr.right

import android.R.attr.left

import android.widget.LinearLayout





class SearchMyListingActivity : LocalizeActivity() {

    private var currActivity : Activity = this

    var productListMyListing = ArrayList<ProductModel>()
    var productListOtherListing = ArrayList<ProductDataModel>()

    lateinit var etSearch:EditText
    lateinit var imgBack:ImageView
    lateinit var tvMylisting:TextView
    lateinit var tvShowlessMyListing:TextView

    var search_text=""

    lateinit var rvMyListings:RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_my_listing)

        MaterialContainerTransform()

        rvMyListings=findViewById(R.id.rvMyListings)
        imgBack=findViewById(R.id.imgBack)
        tvMylisting=findViewById(R.id.tvMylisting)
        tvShowlessMyListing=findViewById(R.id.tvShowlessMyListing)
        etSearch=findViewById(R.id.etSearch)



        search_text=intent.getStringExtra("search")!!
        tvMylisting.text=intent.getStringExtra("type")!!



        if (tvMylisting.text.equals(resources.getString(R.string.my_listing)))
        {
            productListMyListing.clear()
            productListMyListing= intent.getSerializableExtra("mylist") as ArrayList<ProductModel>
            listMyListing()
        }

        else if (tvMylisting.text.equals(resources.getString(R.string.my_offers)))
        {
            productListMyListing.clear()
            productListMyListing= intent.getSerializableExtra("mylist") as ArrayList<ProductModel>
            listMyOffer()
        }

        else if (tvMylisting.text.equals(resources.getString(R.string.other_listings)))
        {
            productListOtherListing.clear()
            productListOtherListing= intent.getSerializableExtra("mylist") as ArrayList<ProductDataModel>
            listOtherListing()
        }




        initViews()

    }

    fun initViews(){

        etSearch.setText(search_text)


        imgBack.setOnClickListener({
           onBackPressed()
        })

        tvShowlessMyListing.setOnClickListener({
            onBackPressed()
        })



    }

    fun listMyListing(){
        rvMyListings.setHasFixedSize(true)
        var layoutManager = LinearLayoutManager(currActivity, LinearLayoutManager.VERTICAL,false)
        rvMyListings.layoutManager = layoutManager
        var adapter = SearchMyListingsAdapter(currActivity!!,productListMyListing,"sender","sender")
        rvMyListings.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun listMyOffer(){
        rvMyListings.setHasFixedSize(true)
        var layoutManager = LinearLayoutManager(currActivity, LinearLayoutManager.VERTICAL,false)
        rvMyListings.layoutManager = layoutManager
        var adapter = SearchMyListingsAdapter(currActivity!!,productListMyListing,"transporter","transporter")
        rvMyListings.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun listOtherListing(){

        rvMyListings.setHasFixedSize(true)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(20, 20, 20, 0)
        rvMyListings.setLayoutParams(lp)
        var layoutManager2 = GridLayoutManager(currActivity, 2)
        rvMyListings.layoutManager = layoutManager2
        var adapter2 = SearchOtherListingAdapter(currActivity!!,productListOtherListing)
        rvMyListings.adapter = adapter2
        adapter2.notifyDataSetChanged()

    }

    override fun setStatusBar() {
        val mColor = resources.getColor(R.color.colorPrimary)
        StatusBarUtil.setLightMode(currActivity)
    }
}