package com.livo.nuo.view.home.homefragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.livo.nuo.R
import com.livo.nuo.databinding.BottomSheetListingFilterBinding
import com.livo.nuo.models.ProductDataModel
import com.livo.nuo.utility.AndroidUtil
import com.livo.nuo.utility.AppUtils
import com.livo.nuo.view.home.adapter.ListingAdapter
import com.livo.nuo.view.home.search.SearchActivity
import com.livo.nuo.view.notifications.NotificationsActivity
import com.livo.nuo.viewModel.ViewModelFactory
import com.livo.nuo.viewModel.products.ProductViewModel
import java.util.*


class ListingFragment : Fragment() {

    private var currActivity : Activity? = null

    lateinit var imgFilter:ImageView
    private var productList = ArrayList<ProductDataModel>()
    private var productViewModel : ProductViewModel? = null
    lateinit var adapter : ListingAdapter
    lateinit var rlSearch:RelativeLayout

    private var bottomSheetDialog: BottomSheetDialog?=null
    lateinit var rvListing:RecyclerView

    lateinit var imgNotification:ImageView

    var ListingFragment="ListingFragment"
    lateinit var pref: SharedPreferences

    var highestprice=0
    var nearestlocation=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root= inflater.inflate(R.layout.fragment_listing, container, false)

        imgFilter=root.findViewById(R.id.imgFilter)
        rvListing=root.findViewById(R.id.rvListing)
        rlSearch=root.findViewById(R.id.rlSearch)
        imgNotification=root.findViewById(R.id.imgNotification)


        imgFilter.setOnClickListener({
            openBottomPopup()
        })
        initViews()

        return root
    }


    fun initViews(){
        currActivity = requireActivity()

        currActivity?.application?.let {
            productViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(ProductViewModel::class.java)
        }



        imgNotification.setOnClickListener({
            val fade = Fade()
            val decor: View = currActivity!!.window.getDecorView()

            //fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true)
            fade.excludeTarget(android.R.id.statusBarBackground, true)
            fade.excludeTarget(android.R.id.navigationBarBackground, true)

            currActivity!!.window.setEnterTransition(fade)
            currActivity!!.window.setExitTransition(fade)

            val intent = Intent(currActivity!!, NotificationsActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                currActivity!!, imgNotification, ViewCompat.getTransitionName(imgNotification)!!
            )
            startActivity(intent, options.toBundle())
        })

        rlSearch.setOnClickListener({
            val intent = Intent(currActivity!!, SearchActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                currActivity!!, rlSearch, ViewCompat.getTransitionName(rlSearch)!!
            )
            startActivity(intent, options.toBundle())
        })

        productViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                var jsonObject =  JsonObject();
                jsonObject.addProperty("longitude", "19.1527096")
                jsonObject.addProperty("latitude", "72.8611884")

                it.getAllListings(jsonObject)
            }
        }

        setAdapter()
        observers()

    }

    override fun onResume() {

        productViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                var jsonObject =  JsonObject();
                jsonObject.addProperty("longitude", "19.1527096")
                jsonObject.addProperty("latitude", "72.8611884")

                it.getAllListings(jsonObject)
            }
        }

        setAdapter()
        observers()

        super.onResume()
    }


    private fun setAdapter(){
        var layoutManager : GridLayoutManager?
        rvListing.setHasFixedSize(true)
        layoutManager=GridLayoutManager(currActivity, 2)
        rvListing.layoutManager = layoutManager
        adapter = ListingAdapter(currActivity!!,productList)
        rvListing.adapter = adapter

        rvListing.addOnScrollListener (
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) { //check for scroll down
                        var visibleItemCount: Int = layoutManager!!.getChildCount()
                        var totalItemCount: Int = layoutManager!!.getItemCount()
                        var pastVisiblesItems: Int = layoutManager!!.findFirstVisibleItemPosition()
                        if (pastVisiblesItems + visibleItemCount >= totalItemCount) {
                            Log.e("end","--")
                        }
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    {Log.e("state","--")}
                }

            })

    }


    companion object {
        fun newInstance() : Fragment {
            val f = ListingFragment()
            return f
        }
    }


    private fun observers() {

        productViewModel?.getMutableLiveDataAllListings()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {

                productList.clear()
                productList.addAll(it.data)
                adapter.notifyDataSetChanged()

            })

        productViewModel?.getErrorMutableLiveData()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
            //hideProgressBar()
            AppUtils.showToast(currActivity!!,R.drawable.cross,it.message,R.color.error_red,R.color.white,R.color.white)
        })
    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun openBottomPopup() {

        bottomSheetDialog = BottomSheetDialog(currActivity!!)
        var bottomSheetDashboardFilterBinding =
            DataBindingUtil.inflate<BottomSheetListingFilterBinding>(
                LayoutInflater.from(currActivity),
                R.layout.bottom_sheet_listing_filter, null, false
            )

        bottomSheetDialog?.setContentView(bottomSheetDashboardFilterBinding!!.root)
        Objects.requireNonNull<Window>(bottomSheetDialog?.window)
            .setBackgroundDrawableResource(android.R.color.transparent)

        pref = currActivity!!.getSharedPreferences(ListingFragment, Context.MODE_PRIVATE)
        val editor = pref.edit()

        var llNearestPickup=bottomSheetDialog!!.findViewById<LinearLayout>(R.id.llNearestPickup)
        var llHighestPrice=bottomSheetDialog!!.findViewById<LinearLayout>(R.id.llHighestPrice)
        var llLatest=bottomSheetDialog!!.findViewById<LinearLayout>(R.id.llLatest)
        var llOldest=bottomSheetDialog!!.findViewById<LinearLayout>(R.id.llOldest)

        var loc=pref.getString("loc","")
        var hpri=pref.getString("hpri","")
        var type=pref.getString("type","")

        if (loc.equals("yes"))
        {
            bottomSheetDashboardFilterBinding.llNearestPickup.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
        }

        if (hpri.equals("yes"))
        {
            bottomSheetDashboardFilterBinding.llHighestPrice.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
        }

        if (type.equals("oldest"))
        {
            bottomSheetDashboardFilterBinding.llLatest.setBackground(currActivity!!.getDrawable(R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.llOldest.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
        }
        else if (type.equals("latest")){
            bottomSheetDashboardFilterBinding.llOldest.setBackground(currActivity!!.getDrawable(R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.llLatest.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
        }


        llOldest?.setOnClickListener({

            editor.putString("type", "oldest")
            editor.commit()

            if (productList != null) {
                productList.sortBy{ it.pickup_date }
            }
            adapter.filterData(productList)
            rvListing.smoothScrollToPosition(0)

            bottomSheetDashboardFilterBinding.llLatest.setBackground(currActivity!!.getDrawable(R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.llOldest.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
        })

        llLatest?.setOnClickListener({

            editor.putString("type", "latest")
            editor.commit()

            if (productList != null) {
                productList.sortByDescending{ it.pickup_date }
            }
            adapter.filterData(productList)
            rvListing.smoothScrollToPosition(0)

            bottomSheetDashboardFilterBinding.llOldest.setBackground(currActivity!!.getDrawable(R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.llLatest.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
        })

        llNearestPickup?.setOnClickListener({

            if (nearestlocation==0) {

                editor.putString("loc", "yes")
                editor.commit()

                nearestlocation=1
                //sorting
                if (productList != null) {
                    productList.sortBy { it.user_distance }
                }
                adapter.filterData(productList)
                rvListing.smoothScrollToPosition(0)

                bottomSheetDashboardFilterBinding.llNearestPickup.setBackground(
                    currActivity!!.getDrawable(
                        R.drawable.blue_round_shape_45_opacity
                    )
                )

            }
            else{
                editor.putString("loc", "")
                editor.commit()

                nearestlocation=0
                bottomSheetDashboardFilterBinding.llNearestPickup.setBackground(
                    currActivity!!.getDrawable(
                        R.drawable.grey_round_shape_45_opacity
                    )
                )
            }
        })
        llHighestPrice?.setOnClickListener({

            if (highestprice==0) {

                editor.putString("hpri", "yes")
                editor.commit()

                highestprice=1
                if (productList != null) {
                    productList.sortByDescending { it.price }
                }
                adapter.filterData(productList)
                rvListing.smoothScrollToPosition(0)

                bottomSheetDashboardFilterBinding.llHighestPrice.setBackground(
                    currActivity!!.getDrawable(
                        R.drawable.blue_round_shape_45_opacity
                    )
                )
            }

            else{
                highestprice=0

                editor.putString("hpri", "")
                editor.commit()

                bottomSheetDashboardFilterBinding.llHighestPrice.setBackground(
                    currActivity!!.getDrawable(
                        R.drawable.grey_round_shape_45_opacity
                    )
                )
            }
            })



        bottomSheetDialog?.show()
    }
}