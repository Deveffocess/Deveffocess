package com.livo.nuo.view.search

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialContainerTransform
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.jaeger.library.StatusBarUtil
import com.livo.nuo.R
import com.livo.nuo.utility.AndroidUtil
import com.livo.nuo.utility.AppUtils
import com.livo.nuo.utility.LocalizeActivity
import com.livo.nuo.viewModel.ViewModelFactory
import com.livo.nuo.viewModel.products.ProductViewModel
import android.view.inputmethod.EditorInfo
import android.widget.*

import android.widget.TextView.OnEditorActionListener
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.livo.nuo.models.ProductDataModel
import com.livo.nuo.models.ProductModel
import com.livo.nuo.view.search.activity.SearchMyListingActivity
import com.livo.nuo.view.search.adapter.SearchMyListingAdapter
import com.livo.nuo.view.search.adapter.SearchOtherListingAdapter
import com.livo.nuoo.view.home.adapter.MyListingAdapter


class SearchActivity : LocalizeActivity() {

    private var currActivity : Activity = this
    private var productViewModel : ProductViewModel? = null
    private var productListMyListing = ArrayList<ProductModel>()
    private var productListMyOffer = ArrayList<ProductModel>()
    private var productListOtherListing = ArrayList<ProductDataModel>()

    lateinit var imgBack:ImageView
    lateinit var rvMyListings:RecyclerView
    lateinit var rvMyOffer:RecyclerView
    lateinit var rvOtherListing:RecyclerView
    lateinit var etSearch:EditText
    lateinit var tvShowAllMyListing:TextView
    lateinit var tvShowAllMyOffers:TextView
    lateinit var tvShowAllOtherListing:TextView

    lateinit var tvMylisting:TextView
    lateinit var tvMyOffer:TextView
    lateinit var tvOtherListing:TextView
    lateinit var rlSearch:RelativeLayout
    lateinit var dialog:Dialog

    lateinit var llMyListings:LinearLayout
    lateinit var llMyOffers:LinearLayout
    lateinit var llOtherListings:LinearLayout

    lateinit var rlNoDataFound:RelativeLayout

    lateinit var shimmerViewContainer:ShimmerFrameLayout
    lateinit var llmainLayout:LinearLayout

    var search_text=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

         MaterialContainerTransform()

        imgBack=findViewById(R.id.imgBack)
        rvMyListings=findViewById(R.id.rvMyListings)
        rvMyOffer=findViewById(R.id.rvMyOffer)
        etSearch=findViewById(R.id.etSearch)
        tvShowAllMyOffers=findViewById(R.id.tvShowAllMyOffers)
        tvShowAllMyListing=findViewById(R.id.tvShowAllMyListing)
        tvShowAllOtherListing=findViewById(R.id.tvShowAllOtherListing)
        tvMylisting=findViewById(R.id.tvMylisting)
        tvMyOffer=findViewById(R.id.tvMyOffer)
        tvOtherListing=findViewById(R.id.tvOtherListing)
        rvOtherListing=findViewById(R.id.rvOtherListing)
        llMyListings=findViewById(R.id.llMyListings)
        llMyOffers=findViewById(R.id.llMyOffers)
        llOtherListings=findViewById(R.id.llOtherListings)
        rlSearch=findViewById(R.id.rlSearch)
        shimmerViewContainer=findViewById(R.id.shimmerViewContainer)
        llmainLayout=findViewById(R.id.llmainLayout)

        rlNoDataFound=findViewById(R.id.rlNoDataFound)

        llMyListings.visibility=View.GONE
        llMyOffers.visibility=View.GONE
        llOtherListings.visibility=View.GONE

        initViews()
    }

    fun initViews(){


        currActivity?.application?.let {
            productViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(ProductViewModel::class.java)
        }


        imgBack.setOnClickListener({
            onBackPressed()
        })


        etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                search_text=etSearch.text.toString()

                shimmerViewContainer.visibility=View.VISIBLE
                shimmerViewContainer.startShimmer()
                llmainLayout.visibility=View.GONE

                showProgressBar()
                hideProgressBar()
                productViewModel?.let {
                    if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                        var jsonObject =  JsonObject();
                        jsonObject.addProperty("search_text",search_text )

                        it.getSearch(jsonObject)
                    }
                }

                return@OnEditorActionListener true
            }
            false
        })

        tvShowAllMyListing.setOnClickListener({
            var i=Intent(currActivity,SearchMyListingActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                currActivity!!, rlSearch, ViewCompat.getTransitionName(rlSearch)!!
            )
            i.putExtra("mylist",productListMyListing)
            i.putExtra("search",search_text)
            i.putExtra("type",resources.getString(R.string.my_listing))
            startActivity(i,options.toBundle())
        })

        tvShowAllMyOffers.setOnClickListener({
            var i=Intent(currActivity,SearchMyListingActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                currActivity!!, rlSearch, ViewCompat.getTransitionName(rlSearch)!!
            )
            i.putExtra("mylist",productListMyOffer)
            i.putExtra("search",search_text)
            i.putExtra("type",resources.getString(R.string.my_offers))
            startActivity(i,options.toBundle())
        })

        tvShowAllOtherListing.setOnClickListener({
            var i=Intent(currActivity,SearchMyListingActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                currActivity!!, rlSearch, ViewCompat.getTransitionName(rlSearch)!!
            )
            i.putExtra("mylist",productListOtherListing)
            i.putExtra("search",search_text)
            i.putExtra("type",resources.getString(R.string.other_listings))
            startActivity(i,options.toBundle())
        })


        observers()

    }


    private fun observers() {

        productViewModel?.getMutableLiveDataSearch()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
                hideProgressBar()

                shimmerViewContainer.visibility=View.GONE
                shimmerViewContainer.stopShimmer()
                llmainLayout.visibility=View.VISIBLE

                if (it.data.my_listings_count==0)
                    llMyListings.visibility=View.GONE
                else
                    llMyListings.visibility=View.VISIBLE

                if (it.data.my_biddings_count==0)
                    llMyOffers.visibility=View.GONE
                else
                    llMyOffers.visibility=View.VISIBLE

                if (it.data.others_listings_count==0)
                    llOtherListings.visibility=View.GONE
                else
                    llOtherListings.visibility=View.VISIBLE

                tvMylisting.text=resources.getString(R.string.my_listing)+"  ("+it.data.my_listings_count.toString()+")"
                tvMyOffer.text=resources.getString(R.string.my_offers)+"  ("+it.data.my_biddings_count.toString()+")"
                tvOtherListing.text=resources.getString(R.string.other_listings)+"  ("+it.data.others_listings_count.toString()+")"

                productListMyListing.clear()
                productListMyListing.addAll(it.data.my_listings)

                rvMyListings.setHasFixedSize(true)
                var layoutManager = LinearLayoutManager(currActivity, LinearLayoutManager.VERTICAL,false)
                rvMyListings.layoutManager = layoutManager
                var adapter = SearchMyListingAdapter(currActivity!!,productListMyListing,"sender","sender")
                rvMyListings.adapter = adapter
                adapter.notifyDataSetChanged()

                productListMyOffer.clear()
                productListMyOffer.addAll(it.data.my_biddings)

                rvMyOffer.setHasFixedSize(true)
                var layoutManager1 = LinearLayoutManager(currActivity, LinearLayoutManager.VERTICAL,false)
                rvMyOffer.layoutManager = layoutManager1
                var adapter1 = SearchMyListingAdapter(currActivity!!,productListMyOffer,"transporter","transporter")
                rvMyOffer.adapter = adapter1
                adapter1.notifyDataSetChanged()

                productListOtherListing.clear()
                productListOtherListing.addAll(it.data.others_listings)

                rvOtherListing.setHasFixedSize(true)
                var layoutManager2 = GridLayoutManager(currActivity, 2)
                rvOtherListing.layoutManager = layoutManager2
                var adapter2 = SearchOtherListingAdapter(currActivity!!,productListOtherListing)
                rvOtherListing.adapter = adapter2
                adapter2.notifyDataSetChanged()

                rlNoDataFound.visibility=View.GONE

            })

        productViewModel?.getErrorMutableLiveData()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
            hideProgressBar()

            rlNoDataFound.visibility=View.VISIBLE
            shimmerViewContainer.visibility=View.GONE
            shimmerViewContainer.stopShimmer()

            AppUtils.showToast(currActivity!!,R.drawable.cross,it.message,R.color.error_red,R.color.white,R.color.white)
        })
    }


    fun showProgressBar(){
        dialog =  AppUtils.showProgress(this)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog)
    }

    override fun setStatusBar() {

        val mColor = resources.getColor(R.color.colorPrimary)
        StatusBarUtil.setLightMode(currActivity)

    }
}