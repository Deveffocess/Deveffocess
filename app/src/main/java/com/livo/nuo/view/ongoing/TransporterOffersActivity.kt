package com.livo.nuo.view.ongoing

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.JsonObject
import com.jaeger.library.StatusBarUtil
import com.livo.nuo.R
import com.livo.nuo.models.TranspoterBiddingDataModel
import com.livo.nuo.utility.AndroidUtil
import com.livo.nuo.utility.AppUtils
import com.livo.nuo.utility.LocalizeActivity
import com.livo.nuo.view.ongoing.adapters.TransporterOfferAdapter
import com.livo.nuo.viewModel.ViewModelFactory
import com.livo.nuo.viewModel.products.ProductViewModel
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class TransporterOffersActivity : LocalizeActivity() {

    private var currActivity : Activity = this
    lateinit var rvTransporters:RecyclerView

    private var productViewModel : ProductViewModel? = null
    lateinit var adapter : TransporterOfferAdapter
    private lateinit var dialog: Dialog
    lateinit var imgBack:ImageView

    var has_next=false

    var currentpage=1

    private var transporterList =  ArrayList<TranspoterBiddingDataModel>()

    var id=0
    var image=""

    lateinit var imgUser:ImageView
    lateinit var tvShimmerImage:ShimmerFrameLayout
    lateinit var shimmerViewContainer:ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transporter_offers)

        imgUser=findViewById(R.id.imgUser)
        tvShimmerImage=findViewById(R.id.tvShimmerImage)
        rvTransporters=findViewById(R.id.rvTransporters)
        shimmerViewContainer=findViewById(R.id.shimmerViewContainer)
        imgBack=findViewById(R.id.imgBack)

        initViews()

    }

    fun  initViews(){

        id=intent.getIntExtra("id",0)

            shimmerViewContainer.visibility = View.VISIBLE
            rvTransporters.visibility = View.GONE
            shimmerViewContainer.startShimmer()

             currActivity?.application?.let {
                 productViewModel = ViewModelProvider(
                     ViewModelStore(),
                     ViewModelFactory(it)
                 ).get(ProductViewModel::class.java)
             }

             productViewModel?.let {
                 if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {
                     showProgressBar()
                     var jsonObject =  JsonObject();
                     jsonObject.addProperty("list_id", id)
                     jsonObject.addProperty("custom_page",currentpage)
                     it.getTransportersListForProduct(jsonObject)
                 }
             }


        imgBack.setOnClickListener({
            finish()
        })

        currActivity?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(mMessageReceiver,
                IntentFilter("dashboard_fragment")
            )
        }

        setAdapter()
        observers()

    }

    var mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {

            productViewModel?.let {
                if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                    var jsonObject =  JsonObject();
                    jsonObject.addProperty("list_id", id)
                    jsonObject.addProperty("custom_page",currentpage)
                    it.getTransportersListForProduct(jsonObject)
                }
            }

        }
    }



    private fun observers(){
        productViewModel?.getMutableLiveTranspoterListForProduct()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
            hideProgressBar()
            transporterList.clear()
            transporterList.addAll(it.data.biddings_data)
            has_next=it.data.has_next


            if(transporterList.size > 0){
               shimmerViewContainer.visibility = View.GONE
               //rlNoDataFound.visibility = View.GONE
               rvTransporters.visibility = View.VISIBLE
               shimmerViewContainer.stopShimmer()
            }else{
               shimmerViewContainer.visibility = View.GONE
              // rlNoDataFound.visibility = View.VISIBLE
               rvTransporters.visibility = View.GONE
               shimmerViewContainer.startShimmer()

            }


            Glide.with(currActivity).addDefaultRequestListener(object : RequestListener<Any> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Any>?,
                    isFirstResource: Boolean
                ): Boolean {
                    tvShimmerImage.visibility = View.VISIBLE
//                   imgUser.visibility = View.GONE
                    tvShimmerImage.startShimmer()
                    return false
                }

                override fun onResourceReady(
                    resource: Any?,
                    model: Any?,
                    target: Target<Any>?,
                    dataSource:DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    tvShimmerImage.visibility = View.GONE
                    imgUser.visibility = View.VISIBLE
                    tvShimmerImage.stopShimmer()
                    return false
                }

            })
                .load(it.data.listing_image).placeholder(currActivity.getDrawable(R.drawable.grey_round_shape))
                .error(currActivity.getDrawable(R.drawable.grey_round_shape)).into(imgUser)



            adapter.notifyDataSetChanged()

        })

          productViewModel?.getMutableLiveDataTransportersListRemoveBid()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
            hideProgressBar()

              productViewModel?.let {
                  if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {
                      showProgressBar()
                      var jsonObject =  JsonObject();
                      jsonObject.addProperty("list_id", id)
                      jsonObject.addProperty("custom_page",currentpage)
                      it.getTransportersListForProduct(jsonObject)
                  }
              }
           // AppUtils.showToast(this,R.drawable.check,it.message,R.color.success_green,R.color.white,R.color.white)

        })

        productViewModel?.getErrorMutableLiveData()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
            hideProgressBar()
            AppUtils.showToast(currActivity,R.drawable.cross,it.message,R.color.error_red,R.color.white,R.color.white)
        })
    }


    private fun setAdapter(){

        var layoutManager=LinearLayoutManager(currActivity,
            LinearLayoutManager.VERTICAL,false)
       rvTransporters.setHasFixedSize(true)
       rvTransporters.layoutManager = layoutManager
        adapter = TransporterOfferAdapter(currActivity,transporterList)
       rvTransporters.adapter = adapter

       rvTransporters.addOnScrollListener (
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) { //check for scroll down
                        var visibleItemCount: Int = layoutManager!!.getChildCount()
                        var totalItemCount: Int = layoutManager!!.getItemCount()
                        var pastVisiblesItems: Int = layoutManager!!.findFirstVisibleItemPosition()
                        if (pastVisiblesItems + visibleItemCount >= totalItemCount) {

                            if (has_next) {
                                currentpage++

                                productViewModel?.let {
                                    if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!)} == true) {
                                        showProgressBar()
                                        var jsonObject = JsonObject();
                                        jsonObject.addProperty("list_id", id)
                                        jsonObject.addProperty("custom_page", currentpage)
                                        it.getTransportersListForProduct(jsonObject)
                                    }
                                }
                            }

                        }
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    {Log.e("state","--")}
                }

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

    fun Callback(idi: Int) {

        productViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {
                showProgressBar()
                var jsonObject =  JsonObject();
                jsonObject.addProperty("bid_id", idi)

                it.getTransportersListRemoveBid(jsonObject)
            }
        }

    }

}