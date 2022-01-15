package com.livo.nuo.view.home.homefragments

import android.app.Activity
import android.content.*
import android.graphics.Color
import android.os.Bundle
import android.transition.Fade
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.livo.nuo.R
import com.livo.nuo.databinding.BottomSheetDashboardFilterBinding
import com.livo.nuo.lib.scrollimage.CustPagerTransformer
import com.livo.nuo.manager.SessionManager
import com.livo.nuo.models.BannerModel
import com.livo.nuo.models.BannerModelDemo
import com.livo.nuo.models.ProductModel
import com.livo.nuo.recyclerview_refresh.RecyclerRefreshLayout
import com.livo.nuo.utility.AndroidUtil
import com.livo.nuo.utility.DensityUtil
import com.livo.nuo.utility.MaterialRefreshView
import com.livo.nuo.viewModel.ViewModelFactory
import com.livo.nuo.viewModel.products.ProductViewModel
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.nostra13.universalimageloader.core.download.BaseImageDownloader
import java.util.*
import kotlin.collections.ArrayList
import com.google.gson.JsonArray
import com.livo.nuo.view.home.homefragments.PaginationListener.Companion.PAGE_START
import com.livo.nuoo.view.home.adapter.MyListingAdapter
import android.widget.*
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.livo.nuo.utility.AppUtils
import android.util.DisplayMetrics
import com.livo.nuo.view.notifications.NotificationsActivity
import androidx.core.app.ActivityOptionsCompat

import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView

import com.livo.nuo.view.search.SearchActivity
import com.livo.nuo.view.listing.NewListingActivity
import android.view.MotionEvent

import android.view.View.OnTouchListener
import android.widget.Toast
import android.view.ViewTreeObserver.OnScrollChangedListener
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.livo.nuo.view.home.HomeActivity


class MyListingFragment : Fragment() {
    private var currActivity : Activity? = null
    private var bannerList = ArrayList<BannerModel>()
    private var productList = ArrayList<ProductModel>()
    private var productViewModel : ProductViewModel? = null
    var currentPage: Int = 1
    var totalPage = 0
    var layoutManager : LinearLayoutManager? = null
    lateinit var adapter : MyListingAdapter
    private var loading = false

    lateinit var shimmerViewbanner:ShimmerFrameLayout
    lateinit var shimmerViewContainer:ShimmerFrameLayout
    lateinit var rvListing:RecyclerView
    lateinit var viewpager1:ViewPager
    lateinit var refreshLayout:RecyclerRefreshLayout
    lateinit var tabsLayout:TabLayout
    lateinit var imgFilter:ImageView
    lateinit var rlNoDataFound:LinearLayout
    lateinit var rlBottomMain:RelativeLayout
    lateinit var imgNotification:ImageView
    lateinit var tvMakeListing:TextView
    lateinit var progressbar:ProgressBar

    lateinit var nsMainScroll:NestedScrollView
    lateinit var rlMM:RelativeLayout
    lateinit var rlSearch:RelativeLayout

    var userType=""
    var pageNumber=0
    var height=0
    var width=0
    var has_next=false
    var numberingpage=0

    var MyListingFragment="MyListingFragment"

    private var bottomSheetApplicationDialog: BottomSheetDialog?=null
    var filterarray=JsonArray()

    lateinit var pref: SharedPreferences

    private var bannerListDemo = ArrayList<BannerModelDemo>()
    var drawableArray = arrayOf<Int>(
        R.drawable.banner_default, R.drawable.banner_default,R.drawable.banner_default,
        R.drawable.banner_default
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var root= inflater.inflate(R.layout.fragment_my_listing, container, false)

        shimmerViewbanner=root.findViewById(R.id.shimmerViewbanner)
        shimmerViewContainer=root.findViewById(R.id.shimmerViewContainer)
        rvListing=root.findViewById(R.id.rvListing)
        viewpager1=root.findViewById(R.id.viewpager1)
        refreshLayout=root.findViewById(R.id.refreshLayout)
        tabsLayout=root.findViewById(R.id.tabsLayout)
        imgFilter=root.findViewById(R.id.imgFilter)
        rlNoDataFound=root.findViewById(R.id.rlNoDataFound)
        rlBottomMain=root.findViewById(R.id.rlBottomMain)
        imgNotification=root.findViewById(R.id.imgNotification)
        rlSearch=root.findViewById(R.id.rlSearch)
        tvMakeListing=root.findViewById(R.id.tvMakeListing)
        nsMainScroll=root.findViewById(R.id.nsMainScroll)
        rlMM=root.findViewById(R.id.rlMM)
        progressbar=root.findViewById(R.id.progressbar)
        rlNoDataFound.visibility = View.GONE

        initViews()

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
    private fun initViews(){
        currActivity = requireActivity()

        val displayMetrics = DisplayMetrics()
        currActivity!!.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
        if (width!=0) {
            viewpager1.getLayoutParams().height = height / 4
            viewpager1.getLayoutParams().width = width / 1
        }
        else{
            viewpager1.getLayoutParams().height = 350
        }


        activity?.application?.let {
            productViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(ProductViewModel::class.java)
        }

        val savedBanners = SessionManager.getExtraDataModel()
        bannerList.clear()
        //bannerList.addAll(savedBanners?.data!!.banners)

        bannerListDemo.clear()
        for (i in drawableArray.indices) {
            bannerListDemo?.add(
                BannerModelDemo(
                    drawableArray.get(i)
                )
            )
        }

        shimmerViewbanner.visibility = View.GONE
        viewpager1.visibility = View.VISIBLE
        shimmerViewbanner.stopShimmer()


        fillViewPager()
        initImageLoader()

     /*   nsMainScroll.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val view: View = nsMainScroll.getChildAt(nsMainScroll.getChildCount() - 1)
                val topDetector: Int = nsMainScroll.getScrollY()
                val bottomDetector: Int =
                    view.bottom - (nsMainScroll.getHeight() + nsMainScroll.getScrollY())
                if (bottomDetector == 0) {
                    Toast.makeText(
                        currActivity!!,
                        "Scroll View bottom reached",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else if (topDetector <= 0) {
                    Toast.makeText(currActivity!!, "Scroll View top reached", Toast.LENGTH_SHORT)
                        .show()
                }
                true
            } else false
        })*/


        nsMainScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                val view = nsMainScroll.getChildAt(nsMainScroll.getChildCount() - 1) as View

                val diff: Int = view.bottom - (nsMainScroll.getHeight() + nsMainScroll
                    .getScrollY())

                if (diff == 0) {

                    if (has_next) {
                        currentPage++
                        doApiCall()
                    }  else{}
                }
                Log.i("TAG", "Scroll DOWN")
            }
            if (scrollY < oldScrollY) {
                Log.i("TAG", "Scroll UP")
            }
            if (scrollY == 0) {
                Log.i("TAG", "TOP SCROLL")
            }
            if (scrollY == v.measuredHeight - v.getChildAt(0).measuredHeight) {
                Log.i("TAG", "BOTTOM SCROLL")
            }
        })

        refreshLayout.setRefreshStyle(RecyclerRefreshLayout.RefreshStyle.FLOAT)
        val layoutParams = ViewGroup.LayoutParams(
            DensityUtil.dip2px(activity, 40f).toInt(),
            DensityUtil.dip2px(activity, 40f).toInt()
        )
        refreshLayout.setRefreshView(MaterialRefreshView(activity), layoutParams)

        refreshLayout.setOnRefreshListener {
            //doApiCall()
            refreshLayout.setRefreshing(false)
        }

        shimmerViewContainer.visibility = View.VISIBLE
        rvListing.visibility = View.GONE
        shimmerViewContainer.startShimmer()

        imgFilter.setOnClickListener({
            openBottomPopup()
        })

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


      /*  rvListing.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastCompletelyVisibleItemPosition =layoutManager!!.findLastVisibleItemPosition()
                try {
                    if (dy > 10) {
                        Toast.makeText(currActivity,lastCompletelyVisibleItemPosition.toString(),Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                try {
                    if (lastCompletelyVisibleItemPosition == adapter.itemCount - 1) {
                        pageNumber++
                        Toast.makeText(currActivity,lastCompletelyVisibleItemPosition.toString(),Toast.LENGTH_SHORT).show()
                        //doApiCall()
                    }
                }
             catch (e: JSONException)
            {
                e.printStackTrace()
            }
          }
        })*/



        pref = currActivity!!.getSharedPreferences(MyListingFragment,0)
        if (pref.contains("sort_by"))
        {

            pref = currActivity!!.getSharedPreferences(MyListingFragment, Context.MODE_PRIVATE)
            var filter_pref =pref.getString("filter","")

            if(filter_pref?.contains("Published") == true)
                filterarray.add("Published")
            if (filter_pref?.contains("Expired")== true)
                filterarray.add("Expired")
            if (filter_pref?.contains("Completed")== true)
                filterarray.add("Completed")
            if (filter_pref?.contains("Suspended")== true)
                filterarray.add("Suspended")
            if (filter_pref?.contains("Ongoing")== true)
                filterarray.add("Ongoing")
        }
        else {
            val editor = pref.edit()
            editor.putString("page", 1.toString())
            editor.putString("sort_by", "title")
            editor.putString("sort_type", "asc")
            editor.putString("user_type", "sender")
            filterarray.add("Ongoing")
            editor.putString("filter",filterarray.toString())
            editor.putString("my_bids", "false")
            editor.commit()
        }

        tvMakeListing.setOnClickListener({
            var i=Intent(currActivity,NewListingActivity::class.java)
            startActivity(i)
        })


        currActivity?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(mMessageReceiver,
                IntentFilter("dashboard_fragment")
            )
        }

        observers()

    }

    var mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            doApiCall()
        }
    }


    override fun onResume() {
        super.onResume()
        numberingpage=1
        currentPage=1
        doApiCall()
    }

    fun doApiCall(){
        productViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {
//                showProgressBar()
//                it.getBanners()

                var sort_by=pref.getString("sort_by","")
                var sort_type=pref.getString("sort_type","")
                var my_bids=pref.getString("my_bids","")
                var user_type=pref.getString("user_type","")
                var filter_pref =pref.getString("filter","")

                var jsonObject =  JsonObject()
                jsonObject.addProperty("page", currentPage)
                jsonObject.addProperty("sort_by", sort_by)
                jsonObject.addProperty("sort_type", sort_type)
                jsonObject.addProperty("user_type", user_type)
                jsonObject.add("filter", filterarray)
                jsonObject.addProperty("my_bids", my_bids)

                Log.e("api",jsonObject.toString())

                it.getUserListings(jsonObject)

            }

        }

    }

    companion object {
        fun newInstance() : Fragment{
            val f = MyListingFragment()
            return f
        }
    }


    private fun fillViewPager() {
        viewpager1.setClipToPadding(false)
        viewpager1.setPageMargin(24)
        viewpager1.setPadding(40, 0, 40, 0)
        viewpager1.setOffscreenPageLimit(3)
        viewpager1.setPageTransformer(false, CustPagerTransformer(currActivity!!))
        viewpager1.adapter = object : PagerAdapter() {

            override fun isViewFromObject(p0: View, p1: Any): Boolean {
                return p0 == p1 as LinearLayout
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                //temporary
                val model = bannerListDemo[position]
               // val model = bannerList[position]

                val itemView = LayoutInflater.from(currActivity).inflate(R.layout.fragment_common,container,false)
                var img_slider=itemView.findViewById<ImageView>(R.id.img_slider)
                var shimmerImagex=itemView.findViewById<ShimmerFrameLayout>(R.id.shimmerImagex)
                img_slider.setOnClickListener{
                    openPopupToDiscard()
                }
                shimmerImagex.visibility = View.VISIBLE
//                holder.itemView.imgProductImagex.visibility = View.INVISIBLE
                shimmerImagex.startShimmer()
                //temprary
                img_slider.setImageResource(model.image)

               /* Glide.with(activity!!).addDefaultRequestListener(object : RequestListener<Any> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Any>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        shimmerImagex.visibility = View.VISIBLE
//                        holder.itemView.imgProductImagex.visibility = View.INVISIBLE
                        shimmerImagex.startShimmer()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Any?,
                        model: Any?,
                        target: Target<Any>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        shimmerImagex.visibility = View.GONE
//                        holder.itemView.imgProductImagex.visibility = View.VISIBLE
                        shimmerImagex.stopShimmer()
                        return false
                    }

                })
                    .load(model.url).placeholder(
                        currActivity!!.getDrawable(R.drawable.banner_default)).
                    error(currActivity!!.getDrawable(R.drawable.banner_default)).
                    into(img_slider)*/
//                Glide.with(currAtivity).load(model.images[0].thumb_image).into(holder.itemView.imgProductImagex)


                //AppUtils.loadImageFullImage(model.url, img_slider,R.drawable.banner_default)
                container.addView(itemView)
                return itemView
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as LinearLayout)
            }

            override fun getCount(): Int {
                //temporary
                return bannerListDemo.size
                //return bannerList.size
            }
        }

        viewpager1.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                updateIndicatorTv()
            }
            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        tabsLayout.setupWithViewPager(viewpager1,true)
        updateIndicatorTv()
    }

    private fun updateIndicatorTv() {
        val totalNum =  viewpager1.adapter!!.count
        val currentItem = viewpager1.currentItem + 1
    }

    private fun initImageLoader() {
        val config = ImageLoaderConfiguration.Builder(currActivity)
            .memoryCacheExtraOptions(480, 800)
            .threadPoolSize(3)
            .threadPriority(Thread.NORM_PRIORITY - 1)
            .tasksProcessingOrder(QueueProcessingType.FIFO)
            .denyCacheImageMultipleSizesInMemory()
            .memoryCache(LruMemoryCache(2 * 1024 * 1024))
            .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13) // default
            .discCacheSize(50 * 1024 * 1024)
            .discCacheFileCount(100)
            .discCacheFileNameGenerator(HashCodeFileNameGenerator())
            .imageDownloader(BaseImageDownloader(currActivity))
            .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
            .writeDebugLogs().build()

        val imageLoader = ImageLoader.getInstance()
        imageLoader.init(config)
    }

    private fun openPopupToDiscard(){


        /*bottomSheetApplicationDialog = BottomSheetDialog(currActivity!!)
        bottomSheetDiscardPopupBinding = inflate<BottomSheetBannerDetailBinding>(
            LayoutInflater.from(currActivity),
            R.layout.bottom_sheet_banner_detail, null, false)

        bottomSheetApplicationDialog?.setContentView(bottomSheetDiscardPopuproot)
        Objects.requireNonNull<Window>(bottomSheetApplicationDialog?.window)
            .setBackgroundDrawableResource(android.R.color.transparent)
//        setupFullHeight(bottomSheetApplicationDialog!!)


        bottomSheetApplicationDialog?.show()*/

    }



    private fun openBottomPopup(){

        bottomSheetApplicationDialog = BottomSheetDialog(currActivity!!)
        var bottomSheetDashboardFilterBinding = DataBindingUtil.inflate<BottomSheetDashboardFilterBinding>(
            LayoutInflater.from(currActivity),
            R.layout.bottom_sheet_dashboard_filter, null, false)

        bottomSheetApplicationDialog?.setContentView(bottomSheetDashboardFilterBinding!!.root)
        Objects.requireNonNull<Window>(bottomSheetApplicationDialog?.window)
            .setBackgroundDrawableResource(android.R.color.transparent)


        pref = currActivity!!.getSharedPreferences(MyListingFragment, Context.MODE_PRIVATE)
        var sort_by=pref.getString("sort_by","")
        var sort_type=pref.getString("sort_type","")
        var filter_pref =pref.getString("filter","")
        var user_type=pref.getString("user_type","")
        var my_bids=pref.getString("my_bids","")
        var editor = pref.edit()

        bottomSheetDashboardFilterBinding.tvResetAll.setOnClickListener({

            editor.putString("page", 1.toString())
            editor.putString("sort_by", "title")
            editor.putString("sort_type", "asc")

            filterarray.remove(JsonPrimitive("Published"))
            filterarray.remove(JsonPrimitive("Expired"))
            filterarray.remove(JsonPrimitive("Completed"))
            filterarray.remove(JsonPrimitive("Suspended"))
            filterarray.remove(JsonPrimitive("Ongoing"))

            filterarray.add("Ongoing")
            editor.putString("filter",filterarray.toString())
            editor.putString("my_bids", "false")
            editor.commit()

            Log.e("arr",filterarray.toString())

            bottomSheetDashboardFilterBinding.tvDataModified.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.ivDataModified.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_upward_24))
            bottomSheetDashboardFilterBinding.tvAlphabetically.setBackground(currActivity!!.getDrawable( R.drawable.blue_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.ivAlphabatically.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_upward_24))
            bottomSheetDashboardFilterBinding.ivAlphabatically.visibility=View.VISIBLE
            bottomSheetDashboardFilterBinding.ivDataModified.visibility=View.GONE

            bottomSheetDashboardFilterBinding.tvPublished.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.tvOngoing.setBackground(currActivity!!.getDrawable( R.drawable.blue_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.tvCompleted.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.tvSuspended.setBackground(currActivity!!.getDrawable(R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.tvCompleted.setBackground(currActivity!!.getDrawable(R.drawable.grey_round_shape_45_opacity))

            bottomSheetDashboardFilterBinding.tvShowonlyMyoffer.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.tvCompletedTransporter.setBackground(currActivity!!.getDrawable(R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.tvSuspendedTransporter.setBackground(currActivity!!.getDrawable(R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.tvOngoingTranspoter.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))

            doApiCall()
        })

        if(filter_pref?.contains("Published") == true) {
            bottomSheetDashboardFilterBinding.tvPublished.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
        }
        if (filter_pref?.contains("Expired")== true) {
            bottomSheetDashboardFilterBinding.tvExpired.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
        }
        if (filter_pref?.contains("Completed")== true) {
            bottomSheetDashboardFilterBinding.tvCompletedTransporter.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.tvCompleted.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
        }
        if (filter_pref?.contains("Suspended")== true){
            bottomSheetDashboardFilterBinding.tvSuspendedTransporter.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.tvSuspended.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
        }
        if (filter_pref?.contains("Ongoing")== true) {
            bottomSheetDashboardFilterBinding.tvOngoingTranspoter.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.tvOngoing.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
        }


        if(userType.equals("sender"))
        {
            bottomSheetDashboardFilterBinding.cvUsertypeSelection.visibility=View.GONE
            bottomSheetDashboardFilterBinding.tvSender.setTextColor(Color.BLACK)
            bottomSheetDashboardFilterBinding.tvSender.setBackground(currActivity!!.getDrawable(R.drawable.white_round_shape_5))
            bottomSheetDashboardFilterBinding.tvTranspoter.setTextColor(Color.parseColor("#888888"))
            bottomSheetDashboardFilterBinding.tvTranspoter.setBackground(currActivity!!.getDrawable( R.drawable.black_20_round_shape_5))

            bottomSheetDashboardFilterBinding.rlFirstRow2.visibility=View.VISIBLE
            bottomSheetDashboardFilterBinding.rlFirstRow3.visibility=View.GONE

            bottomSheetDashboardFilterBinding.lltransporterrow1.visibility=View.GONE
            bottomSheetDashboardFilterBinding.llsenderrow1.visibility=View.VISIBLE
        }
        else{

            if(user_type.equals("sender"))
            {
                bottomSheetDashboardFilterBinding.cvUsertypeSelection.visibility=View.VISIBLE

                bottomSheetDashboardFilterBinding.tvSender.setTextColor(Color.BLACK)
                bottomSheetDashboardFilterBinding.tvSender.setBackground(currActivity!!.getDrawable(R.drawable.white_round_shape_5))
                bottomSheetDashboardFilterBinding.tvTranspoter.setTextColor(Color.parseColor("#888888"))
                bottomSheetDashboardFilterBinding.tvTranspoter.setBackground(currActivity!!.getDrawable( R.drawable.black_20_round_shape_5))

                bottomSheetDashboardFilterBinding.rlFirstRow2.visibility=View.VISIBLE
                bottomSheetDashboardFilterBinding.rlFirstRow3.visibility=View.GONE

                bottomSheetDashboardFilterBinding.lltransporterrow1.visibility=View.GONE
                bottomSheetDashboardFilterBinding.llsenderrow1.visibility=View.VISIBLE
            }
            else{
                bottomSheetDashboardFilterBinding.cvUsertypeSelection.visibility=View.VISIBLE

                bottomSheetDashboardFilterBinding.tvTranspoter.setTextColor(Color.BLACK)
                bottomSheetDashboardFilterBinding.tvTranspoter.setBackground(currActivity!!.getDrawable(R.drawable.white_round_shape_5))
                bottomSheetDashboardFilterBinding.tvSender.setTextColor(Color.parseColor("#888888"))
                bottomSheetDashboardFilterBinding.tvSender.setBackground(currActivity!!.getDrawable( R.drawable.black_20_round_shape_5))

                bottomSheetDashboardFilterBinding.rlFirstRow2.visibility=View.GONE
                bottomSheetDashboardFilterBinding.rlFirstRow3.visibility=View.VISIBLE

                bottomSheetDashboardFilterBinding.lltransporterrow1.visibility=View.VISIBLE
                bottomSheetDashboardFilterBinding.llsenderrow1.visibility=View.GONE
            }

        }

        if (my_bids.equals("true"))
        {
            bottomSheetDashboardFilterBinding.tvPublished.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.tvOngoing.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.tvCompleted.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.tvShowonlyMyoffer.setBackground(currActivity!!.getDrawable( R.drawable.blue_round_shape_45_opacity))
        }


        if (sort_by.equals("date"))
        {
            bottomSheetDashboardFilterBinding.ivAlphabatically.visibility=View.GONE
            bottomSheetDashboardFilterBinding.ivDataModified.visibility=View.VISIBLE
           if (sort_type.equals("latest"))
           {
               bottomSheetDashboardFilterBinding.tvDataModified.setBackground(currActivity!!.getDrawable( R.drawable.blue_round_shape_45_opacity))
               bottomSheetDashboardFilterBinding.ivDataModified.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_upward_24))
           }
           else
           {
               bottomSheetDashboardFilterBinding.tvDataModified.setBackground(currActivity!!.getDrawable( R.drawable.blue_round_shape_45_opacity))
               bottomSheetDashboardFilterBinding.ivDataModified.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_downward_24))
           }
        }
        else if (sort_by.equals("title")) {
            bottomSheetDashboardFilterBinding.ivAlphabatically.visibility=View.VISIBLE
            bottomSheetDashboardFilterBinding.ivDataModified.visibility=View.GONE
            if (sort_type.equals("asc")) {
                bottomSheetDashboardFilterBinding.tvAlphabetically.setBackground(currActivity!!.getDrawable( R.drawable.blue_round_shape_45_opacity))
                bottomSheetDashboardFilterBinding.ivAlphabatically.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_upward_24))
            }
            else{
                bottomSheetDashboardFilterBinding.tvAlphabetically.setBackground(currActivity!!.getDrawable( R.drawable.blue_round_shape_45_opacity))
                bottomSheetDashboardFilterBinding.ivAlphabatically.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_downward_24))
            }
        }


        bottomSheetDashboardFilterBinding.tvDataModified.setOnClickListener({
            sort_type=pref.getString("sort_type","")
            bottomSheetDashboardFilterBinding.tvDataModified.setBackground(currActivity!!.getDrawable( R.drawable.blue_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.tvAlphabetically.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.ivAlphabatically.visibility=View.GONE
            bottomSheetDashboardFilterBinding.ivDataModified.visibility=View.VISIBLE

            if (sort_type.equals("latest"))
            {
                editor.putString("sort_by", "date")
                editor.putString("sort_type","oldest")
                editor.commit()

                bottomSheetDashboardFilterBinding.tvDataModified.setBackground(currActivity!!.getDrawable( R.drawable.blue_round_shape_45_opacity))
                bottomSheetDashboardFilterBinding.ivDataModified.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_downward_24))
            }
            else{
                editor.putString("sort_by", "date")
                editor.putString("sort_type","latest")
                editor.commit()
                bottomSheetDashboardFilterBinding.tvDataModified.setBackground(currActivity!!.getDrawable( R.drawable.blue_round_shape_45_opacity))
                bottomSheetDashboardFilterBinding.ivDataModified.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_upward_24))
            }

            doApiCall()

        })
        bottomSheetDashboardFilterBinding.tvAlphabetically.setOnClickListener({
            sort_type=pref.getString("sort_type","")
            bottomSheetDashboardFilterBinding.tvAlphabetically.setBackground(currActivity!!.getDrawable( R.drawable.blue_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.tvDataModified.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.ivAlphabatically.visibility=View.VISIBLE
            bottomSheetDashboardFilterBinding.ivDataModified.visibility=View.GONE

            if (sort_type.equals("asc"))
            {
                editor.putString("sort_by", "title")
                editor.putString("sort_type","desc")
                editor.commit()
                bottomSheetDashboardFilterBinding.tvAlphabetically.setBackground(currActivity!!.getDrawable( R.drawable.blue_round_shape_45_opacity))
                bottomSheetDashboardFilterBinding.ivAlphabatically.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_downward_24))
            }
            else{
                editor.putString("sort_by", "title")
                editor.putString("sort_type","asc")
                editor.commit()

                bottomSheetDashboardFilterBinding.tvAlphabetically.setBackground(currActivity!!.getDrawable( R.drawable.blue_round_shape_45_opacity))
                bottomSheetDashboardFilterBinding.ivAlphabatically.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_upward_24))
            }

            doApiCall()
            //Log.e("pref",sort_type.toString())
        })

        bottomSheetDashboardFilterBinding.tvPublished.setOnClickListener({
            bottomSheetDashboardFilterBinding.tvShowonlyMyoffer.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))

            var filter_pref =pref.getString("filter","")

            if(filter_pref?.contains("Published")==true) {
                filterarray.remove(JsonPrimitive("Published"))
                bottomSheetDashboardFilterBinding.tvPublished.setBackground(currActivity!!.getDrawable(R.drawable.grey_round_shape_45_opacity))

            }
            else{
                filterarray.add("Published")
                bottomSheetDashboardFilterBinding.tvPublished.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            }
            editor.putString("filter",filterarray.toString())
            editor.putString("my_bids", "false")
            editor.commit()
            Log.e("arr",filterarray.toString())

            doApiCall()

        })
        bottomSheetDashboardFilterBinding.tvOngoing.setOnClickListener({
            bottomSheetDashboardFilterBinding.tvShowonlyMyoffer.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))

            var filter_pref =pref.getString("filter","")

            if(filter_pref?.contains("Ongoing")==true) {
                filterarray.remove(JsonPrimitive("Ongoing"))
                bottomSheetDashboardFilterBinding.tvOngoing.setBackground(currActivity!!.getDrawable(R.drawable.grey_round_shape_45_opacity))
            }
            else{
                filterarray.add("Ongoing")
                bottomSheetDashboardFilterBinding.tvOngoing.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            }

            editor.putString("filter",filterarray.toString())
            editor.putString("my_bids", "false")
            editor.commit()
            Log.e("arr",filterarray.toString())

            doApiCall()
        })

        bottomSheetDashboardFilterBinding.tvOngoingTranspoter.setOnClickListener({
            bottomSheetDashboardFilterBinding.tvShowonlyMyoffer.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))

            var filter_pref =pref.getString("filter","")

            if(filter_pref?.contains("Ongoing")==true) {
                filterarray.remove(JsonPrimitive("Ongoing"))
                bottomSheetDashboardFilterBinding.tvOngoingTranspoter.setBackground(currActivity!!.getDrawable(R.drawable.grey_round_shape_45_opacity))
            }
            else{
                filterarray.add("Ongoing")
                bottomSheetDashboardFilterBinding.tvOngoingTranspoter.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            }

            editor.putString("filter",filterarray.toString())
            editor.putString("my_bids", "false")
            editor.commit()
            Log.e("arr",filterarray.toString())

            doApiCall()
        })


        bottomSheetDashboardFilterBinding.tvCompleted.setOnClickListener({
            bottomSheetDashboardFilterBinding.tvShowonlyMyoffer.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))

            var filter_pref =pref.getString("filter","")

            if(filter_pref?.contains("Completed")==true) {
                filterarray.remove(JsonPrimitive("Completed"))
                bottomSheetDashboardFilterBinding.tvCompleted.setBackground(currActivity!!.getDrawable(R.drawable.grey_round_shape_45_opacity))
            }
            else{
                filterarray.add("Completed")
                bottomSheetDashboardFilterBinding.tvCompleted.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            }
            editor.putString("filter",filterarray.toString())
            editor.putString("my_bids", "false")
            editor.commit()
            Log.e("arr",filterarray.toString())

            doApiCall()
        })
        bottomSheetDashboardFilterBinding.tvCompletedTransporter.setOnClickListener({
            bottomSheetDashboardFilterBinding.tvShowonlyMyoffer.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))

            var filter_pref =pref.getString("filter","")

            if(filter_pref?.contains("Completed")==true) {
                filterarray.remove(JsonPrimitive("Completed"))
                bottomSheetDashboardFilterBinding.tvCompletedTransporter.setBackground(currActivity!!.getDrawable(R.drawable.grey_round_shape_45_opacity))
            }
            else{
                filterarray.add("Completed")
                bottomSheetDashboardFilterBinding.tvCompletedTransporter.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            }
            editor.putString("filter",filterarray.toString())
            editor.putString("my_bids", "false")
            editor.commit()
            Log.e("arr",filterarray.toString())

            doApiCall()
        })


        bottomSheetDashboardFilterBinding.tvSuspended.setOnClickListener({

            var filter_pref =pref.getString("filter","")

            if(filter_pref?.contains("Suspended")==true) {
                filterarray.remove(JsonPrimitive("Suspended"))
                bottomSheetDashboardFilterBinding.tvSuspended.setBackground(currActivity!!.getDrawable(R.drawable.grey_round_shape_45_opacity))
            }
            else{
                filterarray.add("Suspended")
                bottomSheetDashboardFilterBinding.tvSuspended.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            }

            editor.putString("filter",filterarray.toString())
            editor.commit()
            Log.e("arr",filterarray.toString())

            doApiCall()
        })
        bottomSheetDashboardFilterBinding.tvSuspendedTransporter.setOnClickListener({
            bottomSheetDashboardFilterBinding.tvShowonlyMyoffer.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))

            var filter_pref =pref.getString("filter","")

            if(filter_pref?.contains("Suspended")==true) {
                filterarray.remove(JsonPrimitive("Suspended"))
                bottomSheetDashboardFilterBinding.tvSuspendedTransporter.setBackground(currActivity!!.getDrawable(R.drawable.grey_round_shape_45_opacity))
            }
            else{
                filterarray.add("Suspended")
                bottomSheetDashboardFilterBinding.tvSuspendedTransporter.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            }

            editor.putString("filter",filterarray.toString())
            editor.putString("my_bids", "false")
            editor.commit()
            Log.e("arr",filterarray.toString())

            doApiCall()
        })


        bottomSheetDashboardFilterBinding.tvExpired.setOnClickListener({

            var filter_pref =pref.getString("filter","")

           if(filter_pref?.contains("Expired")==true) {
                filterarray.remove(JsonPrimitive("Expired"))
                bottomSheetDashboardFilterBinding.tvExpired.setBackground(currActivity!!.getDrawable(R.drawable.grey_round_shape_45_opacity))
            }
            else{
                filterarray.add("Expired")
                bottomSheetDashboardFilterBinding.tvExpired.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            }
            editor.putString("filter",filterarray.toString())
            editor.commit()
            Log.e("arr",filterarray.toString())

            doApiCall()
        })

        bottomSheetDashboardFilterBinding.tvSender.setOnClickListener({
            bottomSheetDashboardFilterBinding.tvSender.setTextColor(Color.BLACK)
            bottomSheetDashboardFilterBinding.tvSender.setBackground(currActivity!!.getDrawable(R.drawable.white_round_shape_5))
            bottomSheetDashboardFilterBinding.tvTranspoter.setTextColor(Color.parseColor("#888888"))
            bottomSheetDashboardFilterBinding.tvTranspoter.setBackground(currActivity!!.getDrawable( R.drawable.black_20_round_shape_5))

            bottomSheetDashboardFilterBinding.rlFirstRow2.visibility=View.VISIBLE
            bottomSheetDashboardFilterBinding.rlFirstRow3.visibility=View.GONE

            bottomSheetDashboardFilterBinding.lltransporterrow1.visibility=View.GONE
            bottomSheetDashboardFilterBinding.llsenderrow1.visibility=View.VISIBLE

            var filter_pref =pref.getString("filter","")

            if(filter_pref?.contains("Published") == true) {
                bottomSheetDashboardFilterBinding.tvPublished.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            }
            if (filter_pref?.contains("Expired")== true) {
                bottomSheetDashboardFilterBinding.tvExpired.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            }
            if (filter_pref?.contains("Completed")== true) {
                bottomSheetDashboardFilterBinding.tvCompleted.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            }
            if (filter_pref?.contains("Suspended")== true){
                bottomSheetDashboardFilterBinding.tvSuspended.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            }
            if (filter_pref?.contains("Ongoing")== true) {
                bottomSheetDashboardFilterBinding.tvOngoing.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            }



            editor.putString("user_type","sender")
            editor.putString("my_bids", "false")
            editor.commit()

            doApiCall()
        })
        bottomSheetDashboardFilterBinding.tvTranspoter.setOnClickListener({

            bottomSheetDashboardFilterBinding.tvTranspoter.setTextColor(Color.BLACK)
            bottomSheetDashboardFilterBinding.tvTranspoter.setBackground(currActivity!!.getDrawable(R.drawable.white_round_shape_5))
            bottomSheetDashboardFilterBinding.tvSender.setTextColor(Color.parseColor("#888888"))
            bottomSheetDashboardFilterBinding.tvSender.setBackground(currActivity!!.getDrawable( R.drawable.black_20_round_shape_5))

            bottomSheetDashboardFilterBinding.rlFirstRow2.visibility=View.GONE
            bottomSheetDashboardFilterBinding.rlFirstRow3.visibility=View.VISIBLE

            bottomSheetDashboardFilterBinding.lltransporterrow1.visibility=View.VISIBLE
            bottomSheetDashboardFilterBinding.llsenderrow1.visibility=View.GONE

            var filter_pref =pref.getString("filter","")
            var my_bids=pref.getString("my_bids","")

            if(my_bids.equals("true"))
                bottomSheetDashboardFilterBinding.tvShowonlyMyoffer.setBackground(currActivity!!.getDrawable( R.drawable.blue_round_shape_45_opacity))
            else
                bottomSheetDashboardFilterBinding.tvShowonlyMyoffer.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))


            if (filter_pref?.contains("Completed")== true) {
                bottomSheetDashboardFilterBinding.tvCompletedTransporter.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            }
            if (filter_pref?.contains("Suspended")== true){
                bottomSheetDashboardFilterBinding.tvSuspendedTransporter.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            }
            if (filter_pref?.contains("Ongoing")== true) {
                bottomSheetDashboardFilterBinding.tvOngoingTranspoter.setBackground(currActivity!!.getDrawable(R.drawable.blue_round_shape_45_opacity))
            }



            if(filter_pref?.contains("Published") == true)
                filterarray.remove(JsonPrimitive("Published"))
            if (filter_pref?.contains("Expired")== true)
                filterarray.remove(JsonPrimitive("Expired"))

            editor.putString("filter",filterarray.toString())
            editor.putString("user_type","transporter")
            editor.commit()

            doApiCall()
        })

        bottomSheetDashboardFilterBinding.tvShowonlyMyoffer.setOnClickListener({
            bottomSheetDashboardFilterBinding.tvOngoingTranspoter.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.tvCompletedTransporter.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))
            bottomSheetDashboardFilterBinding.tvSuspendedTransporter.setBackground(currActivity!!.getDrawable( R.drawable.grey_round_shape_45_opacity))

            bottomSheetDashboardFilterBinding.tvShowonlyMyoffer.setBackground(currActivity!!.getDrawable( R.drawable.blue_round_shape_45_opacity))

            filterarray=JsonArray()
            editor.putString("filter",filterarray.toString())
            editor.putString("my_bids", "true")
            editor.commit()

            doApiCall()
        })

       /* if (userType.equals("transporter")){
            bottomSheetDashboardFilterBinding.cvUsertypeSelection.visibility=View.VISIBLE
            bottomSheetDashboardFilterBinding.tvSender.setTextColor(Color.BLACK)
            bottomSheetDashboardFilterBinding.tvSender.setBackground(currActivity!!.getDrawable(R.drawable.white_round_shape_5))
            bottomSheetDashboardFilterBinding.tvTranspoter.setTextColor(Color.parseColor("#888888"))
            bottomSheetDashboardFilterBinding.tvTranspoter.setBackground(currActivity!!.getDrawable( R.drawable.black_20_round_shape_5))

            bottomSheetDashboardFilterBinding.rlFirstRow2.visibility=View.VISIBLE
            bottomSheetDashboardFilterBinding.rlFirstRow3.visibility=View.GONE
        }
        else{
            bottomSheetDashboardFilterBinding.cvUsertypeSelection.visibility=View.GONE

        }*/

        bottomSheetApplicationDialog?.show()

    }

    private fun setAdapter(){
        rvListing.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(currActivity, LinearLayoutManager.VERTICAL,false)
        rvListing.layoutManager = layoutManager
        if (userType.equals("sender"))
        adapter = MyListingAdapter(currActivity!!,productList,1,userType,userType)
        else{
            var user_type=pref.getString("user_type","")
            adapter = MyListingAdapter(currActivity!!,productList,1,userType,user_type.toString())
        }
        rvListing.adapter = adapter

        if (numberingpage==1)
        {
            adapter.notifyDataSetChanged()
            numberingpage=0
        }
        rvListing.addOnScrollListener(recyclerScrollListener)

    }

    private fun observers() {

        productViewModel?.getMutableLiveDataProductsData()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
                shimmerViewContainer.visibility = View.GONE
                shimmerViewContainer.stopShimmer()
                rvListing.visibility = View.VISIBLE

                has_next=it.data.has_next
                if(has_next)
                progressbar.visibility=View.VISIBLE
                else
                    progressbar.visibility=View.GONE
                Log.e("ha",has_next.toString())
               // currentPage = it.data.current_page
                totalPage = it.data.current_page

                if (currentPage == 1) {
                    productList.clear()
                    productList.addAll(it.data.list_data)
                    rvListing.smoothScrollToPosition(0)
                } else {
                    productList.addAll(it.data.list_data)
                }

                if (productList.size > 0) {
                    rlNoDataFound.visibility = View.GONE
                    rvListing.visibility = View.VISIBLE
                    rlBottomMain.background =
                        currActivity!!.resources.getDrawable(R.drawable.grey_square_shape)
                } else {
                    rvListing.visibility = View.GONE
                    rlNoDataFound.visibility = View.VISIBLE
                    rlBottomMain.background =
                        currActivity!!.resources.getDrawable(R.drawable.white_round_shape_bottom_square)

                }

                (currActivity as HomeActivity).profile_Check(it.data.profile_fulfil)

              userType=it.data.user_type

                setAdapter()
                adapter.notifyDataSetChanged()
            })

        productViewModel?.getErrorMutableLiveData()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
            //hideProgressBar()
            AppUtils.showToast(currActivity!!,R.drawable.cross,it.message,R.color.error_red,R.color.white,R.color.white)
        })
    }

    private val recyclerScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) { //check for scroll down
                    val visibleItemCount: Int = layoutManager!!.getChildCount()
                    val totalItemCount: Int = layoutManager!!.getItemCount()
                    val pastVisiblesItems: Int = layoutManager!!.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            // Do pagination.. i.e. fetch new data
                            if (currentPage + 1 <= totalPage) {
                                currentPage++
                                pageNumber = currentPage
                                productViewModel?.let {
                                    if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                                        var sort_by=pref.getString("sort_by","")
                                        var sort_type=pref.getString("sort_type","")
                                        var filter_pref =pref.getString("filter","")

                                        val jsonObject = JsonObject()
                                        jsonObject.addProperty("page",currentPage)
                                        jsonObject.addProperty("sort_by",sort_by)
                                        jsonObject.addProperty("sort_type",sort_type)
                                        jsonObject.addProperty("user_type","sender")
                                        jsonObject.addProperty("my_bids","false")

                                        jsonObject.add("filter",filterarray)
                                        it.getUserListings(jsonObject)
                                    }
                                }
                            }
                            loading = true
                        }
                    }
                }
            }

        }


}