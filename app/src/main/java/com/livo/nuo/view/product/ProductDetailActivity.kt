package com.livo.nuo.view.product

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Transition
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import com.google.gson.JsonObject
import com.jaeger.library.StatusBarUtil
import com.livo.nuo.R
import com.livo.nuo.utility.LocalizeActivity
import java.util.*
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.livo.nuo.databinding.*
import com.livo.nuo.lib.mapcurves.Curve
import com.livo.nuo.lib.mapcurves.impl.CurveManager
import com.livo.nuo.lib.mapcurves.interfaces.OnCurveClickListener
import com.livo.nuo.lib.mapcurves.interfaces.OnCurveDrawnCallback
import com.livo.nuo.lib.scrollimage.CustPagerTransformer
import com.livo.nuo.manager.SessionManager
import com.livo.nuo.models.DataModel
import com.livo.nuo.models.ExtraDataModel
import com.livo.nuo.models.LoginModel
import com.livo.nuo.utility.AndroidUtil
import com.livo.nuo.utility.AppUtils
import com.livo.nuo.viewModel.ViewModelFactory
import com.livo.nuo.viewModel.products.ProductViewModel
import java.lang.Double


class ProductDetailActivity : LocalizeActivity() , OnMapReadyCallback, OnCurveDrawnCallback,
    OnCurveClickListener {

    private var currActivity : Activity = this
    private lateinit var dialog: Dialog
    private  var dialogReport: Dialog? = null
    private var reportBinding: DialogReportBinding? = null
    private var  feedback_id = 0

    private var thankyouPopupBinding: ThankyouPopupBinding? = null
    private var offerSentSuccessfullyPopupBinding: OfferSentSuccessfullyPopupBinding? = null
    private  var dialogThankyou: Dialog? = null
    private  var dialogOfferSent: Dialog? = null

    private var productViewModel : ProductViewModel? = null
    private var productDetail = DataModel()

    var extraDataModel= ExtraDataModel()

    lateinit var customSwitch:SwitchCompat
    lateinit var rlShare:RelativeLayout
    lateinit var rlReport:RelativeLayout
    lateinit var rlEdit:RelativeLayout
    lateinit var rlDelete:RelativeLayout
    lateinit var vpImages:ViewPager
    lateinit var tvProductTitle:TextView
    lateinit var tvHeightValue:TextView
    lateinit var tvWidthValue:TextView
    lateinit var tvDepthValue:TextView
    lateinit var tvWeightValue:TextView
    lateinit var tvPickupValue:TextView
    lateinit var tvDropOffValue:TextView
    lateinit var tvPickupDate:TextView
    lateinit var tvDropOffDate:TextView
    lateinit var imgBack:ImageView
    lateinit var tabsLayout: TabLayout
    lateinit var rlTransporter:RelativeLayout
    lateinit var imgProductImage:ImageView
    lateinit var tvTitle:TextView
    lateinit var tvSendOffer:TextView

    var listing_price=""

    lateinit var shimmerImagex:ShimmerFrameLayout
    lateinit var shimmerImagex1:ShimmerFrameLayout
    lateinit var rlTopMain:RelativeLayout
    lateinit var rlHWWDTitle:RelativeLayout
    lateinit var rlHWWDValue:RelativeLayout
    lateinit var tvTitlexqimg:View

    var bottomSheetDeleteConfirmation:BottomSheetDialog?=null
    var bottomSheetDeleteSuccessflly:BottomSheetDialog?=null
    var bottomSheetDeleteError:BottomSheetDialog?=null
    var bottomSheetSendOffer:BottomSheetDialog?=null


    private var curveManager: CurveManager? = null
    private var map: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null

    var height=0
    var width=0
    var message=""

    var id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        id=intent.getStringExtra("id")!!

        customSwitch=findViewById(R.id.customSwitch)
        rlShare=findViewById(R.id.rlShare)
        rlReport=findViewById(R.id.rlReport)
        rlEdit=findViewById(R.id.rlEdit)
        rlDelete=findViewById(R.id.rlDelete)
        vpImages=findViewById(R.id.vpImages)
        tvProductTitle=findViewById(R.id.tvProductTitle)
        tvHeightValue=findViewById(R.id.tvHeightValue)
        tvWidthValue=findViewById(R.id.tvWidthValue)
        tvDepthValue=findViewById(R.id.tvDepthValue)
        tvWeightValue=findViewById(R.id.tvWeightValue)
        tvPickupValue=findViewById(R.id.tvPickupValue)
        tvDropOffValue=findViewById(R.id.tvDropOffValue)
        tvPickupDate=findViewById(R.id.tvPickupDate)
        tvDropOffDate=findViewById(R.id.tvDropOffDate)
        imgBack=findViewById(R.id.imgBack)
        tabsLayout=findViewById(R.id.tabsLayout)
        rlTransporter=findViewById(R.id.rlTransporter)
        imgProductImage=findViewById(R.id.imgProductImage)
        tvTitle=findViewById(R.id.tvTitle)
        tvSendOffer=findViewById(R.id.tvSendOffer)



        shimmerImagex=findViewById(R.id.shimmerImagex)
        shimmerImagex1=findViewById(R.id.shimmerImagex1)
        rlTopMain=findViewById(R.id.rlTopMain)
        rlHWWDTitle=findViewById(R.id.rlHWWDTitle)
        rlHWWDValue=findViewById(R.id.rlHWWDValue)
        tvTitlexqimg=findViewById(R.id.tvTitlexqimg)


        initViews()

    }

    fun initViews(){

        mapFragment = supportFragmentManager.findFragmentById(R.id.fmap) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


        val displayMetrics = DisplayMetrics()
        currActivity!!.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
        if (width!=0) {

            vpImages.getLayoutParams().height = (height /2.2).toInt()
            vpImages.getLayoutParams().width = width / 1

            tvTitlexqimg.getLayoutParams().height = (height /2.2).toInt()
            tvTitlexqimg.getLayoutParams().width = width / 1
        }
        else{
            vpImages.getLayoutParams().height = 350
        }


        currActivity?.application?.let {
            productViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(ProductViewModel::class.java)
        }

        showProgressBar()

        productViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                var jsonObject =  JsonObject();
                jsonObject.addProperty("id", id)

                it.getSingleListingData(jsonObject)
            }
        }

        customSwitch.setOnClickListener({
            if (customSwitch.isChecked==true)
            {
                customSwitch.trackDrawable=resources.getDrawable(R.drawable.track_green)
                customSwitch.thumbDrawable=resources.getDrawable(R.drawable.thumb_green)
            }
            else {
                customSwitch.trackDrawable=resources.getDrawable(R.drawable.track_grey)
                customSwitch.thumbDrawable=resources.getDrawable(R.drawable.thumb_blue)
            }
        })

        rlShare.setOnClickListener({

        })

        rlReport.setOnClickListener({
            openReportDialog()
        })

        imgBack.setOnClickListener({
            onBackPressed()
        })

        rlDelete.setOnClickListener({
            openBottomPopupDeleteConfirmation()
        })

        tvSendOffer.setOnClickListener({
            openBottomPopupSendOffer()
        })


        shimmerImagex.visibility=View.VISIBLE
        shimmerImagex1.visibility=View.VISIBLE
        shimmerImagex.startShimmer()
        shimmerImagex1.startShimmer()


        observers()
    }

    fun showProgressBar(){
        dialog =  AppUtils.showProgress(this)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog)
    }

    private fun observers() {


        productViewModel?.getMutableLiveDataProductDetail()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
                productDetail = it.data
                Log.e("pde",productDetail.status)

                hideProgressBar()
                tvProductTitle.text=productDetail.title
                tvHeightValue.text=productDetail.dimensions.height
                tvWidthValue.text=productDetail.dimensions.width
                tvDepthValue.text=productDetail.dimensions.depth
                tvWeightValue.text=productDetail.dimensions.weight

                tvPickupValue.text=productDetail.pickup_address
                tvDropOffValue.text=productDetail.dropoff_address
                tvPickupDate.text=productDetail.pickup_date
                tvDropOffDate.text=productDetail.dropoff_date

                Log.e("loc",productDetail.pickup_latitude+","+productDetail.pickup_longitude+" "+productDetail.dropoff_latitude+","+productDetail.dropoff_longitude)

                if (productDetail.is_edit.equals("true"))
                {
                    rlShare.visibility=View.GONE

                    //rlEdit.visibility=View.VISIBLE
                    rlDelete.visibility=View.VISIBLE
                }
                else{
                    rlEdit.visibility=View.GONE
                    rlDelete.visibility=View.GONE

                    rlShare.visibility=View.VISIBLE
                }

                if (productDetail.is_bidding.equals("false"))
                {
                    rlTransporter.visibility=View.GONE
                }
                else{
                    Glide.with(currActivity)
                        .load(productDetail.user_details.profile_image).placeholder(currActivity.getDrawable(R.drawable.grey_round_shape)).
                        error(currActivity.getDrawable(R.drawable.grey_round_shape)).
                        into(imgProductImage)

                    tvTitle.text=productDetail.user_details.first_name+" "+ productDetail.user_details.last_name
                }


                if (productDetail.more_people_needed.equals("false"))
                    customSwitch.isChecked = false
                else {
                    customSwitch.trackDrawable=resources.getDrawable(R.drawable.track_green)
                    customSwitch.thumbDrawable=resources.getDrawable(R.drawable.thumb_green)
                    customSwitch.isChecked = true
                }

                customSwitch.isEnabled = false

                drawCurveLine()
                fillViewPager(productDetail.listing_images,productDetail.price)

                listing_price=productDetail.price

                shimmerImagex.visibility=View.GONE
                shimmerImagex1.visibility=View.GONE
                shimmerImagex.stopShimmer()
                shimmerImagex1.stopShimmer()

            })

         productViewModel?.getMutableLiveDataPlacebid()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
                var code=it.code
                message=it.message

                openOfferSentSuccessfully()
                hideProgressBar()
            })


        productViewModel?.getMutableLiveDataDeleteData()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
                var status= it.status
                var code=it.code
                message=it.message

                if (code==200)
                {
                    if (status==true)
                    {
                        openBottomPopupDeleteSuccessfully()
                    }
                    else
                    {
                        openBottomPopupDeleteError()
                    }
                }

                hideProgressBar()


            })

        productViewModel?.getErrorMutableLiveData()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
            hideProgressBar()
            bottomSheetSendOffer?.dismiss()
            AppUtils.showToast(currActivity!!,R.drawable.cross,it.message,R.color.error_red,R.color.white,R.color.white)
        })
    }


    private fun fillViewPager(imagesList : ArrayList<String>,price : String) {
        vpImages?.setPageTransformer(false, CustPagerTransformer(currActivity!!))
        vpImages.adapter = object : PagerAdapter() {

            override fun isViewFromObject(p0: View, p1: Any): Boolean {
                return p0 == p1 as RelativeLayout
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val model = imagesList[position]
                val itemView = LayoutInflater.from(currActivity).inflate(R.layout.fragment_product_images,container,false)
                var shimmerImage=itemView.findViewById<ShimmerFrameLayout>(R.id.shimmerImage)
                var imgProductImagex=itemView.findViewById<ImageView>(R.id.imgProductImagex)
                var tvPricex=itemView.findViewById<TextView>(R.id.tvPricex)

                shimmerImage.visibility = View.VISIBLE
//               binding!!imgProductImagex.visibility = View.INVISIBLE
                shimmerImage.startShimmer()


                Glide.with(currActivity).addDefaultRequestListener(object : RequestListener<Any> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Any>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        shimmerImage.visibility = View.VISIBLE
//                       binding!!imgProductImagex.visibility = View.INVISIBLE
                        shimmerImage.startShimmer()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Any?,
                        model: Any?,
                        target: Target<Any>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        shimmerImage.visibility = View.GONE
//                       binding!!imgProductImagex.visibility = View.VISIBLE
                        shimmerImage.stopShimmer()
                        return false
                    }

                })
                    .load(model).placeholder(currActivity.getDrawable(R.drawable.grey_round_shape)).
                    error(currActivity.getDrawable(R.drawable.grey_round_shape)).
                    into(imgProductImagex)

//                AppUtils.loadImageFullImage(model.path, itemView.imgProductImagex,R.drawable.banner_default)

                tvPricex.text = price+" KR"
                container.addView(itemView)
                return itemView
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as RelativeLayout)
            }

            override fun getCount(): Int {
                return imagesList.size
            }
        }

        vpImages.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
//                updateIndicatorTv()
            }
            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        tabsLayout.setupWithViewPager(vpImages,true)


    }



    private fun openReportDialog() {

        dialogReport = Dialog(currActivity)

        reportBinding = DataBindingUtil.inflate(
            LayoutInflater.from(currActivity),
            R.layout.dialog_report, null, true
        )

        dialogReport?.setContentView(reportBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialogReport?.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)
        val window: Window? = dialogReport?.window
        window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)


        dialogReport?.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation;
//       window.enterTransition = transition

        var fraudulentItem=0
        var spam=0
        var againstPolicy=0
        var suspiciousUser=0
        var notMatch=0

        reportBinding!!.tvFraudulentItem.setOnClickListener{
            feedback_id = 1

            if (fraudulentItem==0)   {
                fraudulentItem=1
            reportBinding!!.tvFraudulentItem.background = currActivity.resources.getDrawable(R.drawable.red_round_shap)
            }
            else {
                fraudulentItem=0
                reportBinding!!.tvFraudulentItem.background =
                    currActivity.resources.getDrawable(R.drawable.black_40_round_shape_5)
            }
        }
        reportBinding!!.tvSpam.setOnClickListener{
            feedback_id = 2

            if (spam==0)   {
                spam=1
                reportBinding!!.tvSpam.background = currActivity.resources.getDrawable(R.drawable.red_round_shap)
            }
            else {
                spam=0
                reportBinding!!.tvSpam.background =
                    currActivity.resources.getDrawable(R.drawable.black_40_round_shape_5)
            }

        }
        reportBinding!!.tvAgainstPolicy.setOnClickListener{
            feedback_id = 3

            if (againstPolicy==0)   {
                againstPolicy=1
                reportBinding!!.tvAgainstPolicy.background = currActivity.resources.getDrawable(R.drawable.red_round_shap)
            }
            else {
                againstPolicy=0
                reportBinding!!.tvAgainstPolicy.background =
                    currActivity.resources.getDrawable(R.drawable.black_40_round_shape_5)
            }
        }
        reportBinding!!.tvSuspiciousUser.setOnClickListener{
            feedback_id = 4

            if (suspiciousUser==0)   {
                suspiciousUser=1
                reportBinding!!.tvSuspiciousUser.background = currActivity.resources.getDrawable(R.drawable.red_round_shap)
            }
            else {
                suspiciousUser=0
                reportBinding!!.tvSuspiciousUser.background =
                    currActivity.resources.getDrawable(R.drawable.black_40_round_shape_5)
            }
        }
        reportBinding!!.tvNotMatch.setOnClickListener{
            feedback_id = 5

            if (notMatch==0)   {
                notMatch=1
                reportBinding!!.tvNotMatch.background = currActivity.resources.getDrawable(R.drawable.red_round_shap)
            }
            else {
                notMatch=0
                reportBinding!!.tvNotMatch.background =
                    currActivity.resources.getDrawable(R.drawable.black_40_round_shape_5)
            }
        }


        reportBinding!!.tvSubmit.setOnClickListener({
            openThankYouDialog()
        })

        reportBinding!!.rlMainInternalSecond.setOnClickListener{
            dialogReport?.dismiss()
        }
        reportBinding!!.tvLabel.setOnClickListener{
            dialogReport?.dismiss()
        }

        dialogReport?.setCancelable(false)
        dialogReport?.setCanceledOnTouchOutside(true)
        dialogReport?.show()
    }



     override fun setStatusBar() {

        val mColor = resources.getColor(R.color.colorPrimary)
        StatusBarUtil.setLightMode(currActivity)

    }


    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map?.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map?.uiSettings?.isScrollGesturesEnabled = false;
        map?.getUiSettings()?.setZoomGesturesEnabled(false);
        map?.getUiSettings()?.setScrollGesturesEnabledDuringRotateOrZoom(false);
        curveManager = CurveManager(map)
        curveManager?.setOnCurveDrawnCallback(this)
        curveManager?.setOnCurveClickListener(this)
    }

    private fun drawCurveLine() {
        val PATTERN_DASH_LENGTH_PX = 20
        val PATTERN_GAP_LENGTH_PX = 20
        val DOT: PatternItem = Dot()
        val DASH: PatternItem = Dash(PATTERN_DASH_LENGTH_PX.toFloat())
        val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())
        val PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH)


        val pickupLat = productDetail.pickup_latitude
        val sourceLatitude: String = productDetail.pickup_latitude
        val sourceLongitude: String = productDetail.pickup_longitude
        val destLatitude: String = productDetail.dropoff_latitude
        val destLongitude: String = productDetail.dropoff_longitude

        val alpha: String = "0.5".trim { it <= ' ' }
        if (sourceLatitude == "" || sourceLongitude == "" || destLatitude == "" || destLongitude == "" || alpha == "") {
            return
        }
        val initLatLng = LatLng(Double.valueOf(sourceLatitude), Double.valueOf(sourceLongitude))
        val finalLatLng = LatLng(Double.valueOf(destLatitude), Double.valueOf(destLongitude))


        val builder = LatLngBounds.Builder()
        builder.include(initLatLng)
        builder.include(finalLatLng)
        val bounds = builder.build()
        map!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 40))


        val line =   map!!.addPolyline(
            PolylineOptions()
            .add(initLatLng,
                finalLatLng)
            .width(4f).color(currActivity.resources.getColor(R.color.livo_blue)).geodesic(true))
        line.setPattern(PATTERN_POLYGON_ALPHA);

        map!!.addMarker(
            MarkerOptions().position(initLatLng).
        icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("location_blue", 40, 40))).anchor(0.5f, 1f))
        map!!.addMarker(
            MarkerOptions().position(finalLatLng)
            .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("location_red", 40, 40)))
            .anchor(0.5f, 1f))

//        if (curveManager != null) {
//            curveManager!!.drawCurveAsync(curveOptions)
//
//        }

    }


    fun resizeMapIcons(iconName: String?, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(
            resources,
            resources.getIdentifier(iconName, "drawable", packageName)
        )
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }

    override fun onCurveDrawn(curve: Curve?) {
        Toast.makeText(baseContext, "Curve drawn..!!!", Toast.LENGTH_SHORT).show()
    }

    override fun onCurveClick(curve: Curve?) {

    }



    private fun openBottomPopupDeleteConfirmation() {

        bottomSheetDeleteConfirmation = BottomSheetDialog(currActivity!!)
        var bottomSheetDashboardFilterBinding =
            DataBindingUtil.inflate<BottomSheetPeoductDeleteConfirmationBinding>(
                LayoutInflater.from(currActivity),
                R.layout.bottom_sheet_peoduct_delete_confirmation, null, false
            )

        bottomSheetDeleteConfirmation?.setContentView(bottomSheetDashboardFilterBinding!!.root)
        Objects.requireNonNull<Window>(bottomSheetDeleteConfirmation?.window)
            .setBackgroundDrawableResource(android.R.color.transparent)

        var llCancel=bottomSheetDeleteConfirmation!!.findViewById<LinearLayout>(R.id.llCancel)
        var llDeletenow=bottomSheetDeleteConfirmation!!.findViewById<LinearLayout>(R.id.llDeletenow)


        llDeletenow?.setOnClickListener({

           // openBottomPopupDeleteSuccessfully()

           showProgressBar()
            productViewModel?.let {
                if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                    var jsonObject =  JsonObject();
                    jsonObject.addProperty("list_id", id)
                    it.getDeleteProduct(jsonObject)

                }
            }
        })

        llCancel?.setOnClickListener({
            //openBottomPopupDeleteError()
            bottomSheetDeleteConfirmation?.dismiss()
        })

        bottomSheetDeleteConfirmation?.show()
    }


    private fun openBottomPopupDeleteSuccessfully() {

        bottomSheetDeleteSuccessflly = BottomSheetDialog(currActivity!!)
        var bottomSheetDashboardFilterBinding =
            DataBindingUtil.inflate<BottomSheetPeoductDeleteSuccessfullyBinding>(
                LayoutInflater.from(currActivity),
                R.layout.bottom_sheet_peoduct_delete_successfully, null, false
            )

        bottomSheetDeleteSuccessflly?.setContentView(bottomSheetDashboardFilterBinding!!.root)
        Objects.requireNonNull<Window>(bottomSheetDeleteSuccessflly?.window)
            .setBackgroundDrawableResource(android.R.color.transparent)

        var llClose=bottomSheetDeleteSuccessflly!!.findViewById<LinearLayout>(R.id.llClose)
        var tvMessage=bottomSheetDeleteSuccessflly!!.findViewById<TextView>(R.id.tvMessage)
        tvMessage?.text=message

        llClose?.setOnClickListener({

            bottomSheetDeleteSuccessflly?.dismiss()
            finish()
        })

        bottomSheetDeleteSuccessflly?.show()
    }

    private fun openBottomPopupDeleteError() {

        bottomSheetDeleteError = BottomSheetDialog(currActivity!!)
        var bottomSheetDashboardFilterBinding =
            DataBindingUtil.inflate<BottomSheetPeoductDeleteErrorBinding>(
                LayoutInflater.from(currActivity),
                R.layout.bottom_sheet_peoduct_delete_error, null, false
            )

        bottomSheetDeleteError?.setContentView(bottomSheetDashboardFilterBinding!!.root)
        Objects.requireNonNull<Window>(bottomSheetDeleteError?.window)
            .setBackgroundDrawableResource(android.R.color.transparent)

        var llClose=bottomSheetDeleteError!!.findViewById<LinearLayout>(R.id.llClose)
        var tvMessage=bottomSheetDeleteError!!.findViewById<TextView>(R.id.tvMessage)

        tvMessage?.text=message
        llClose?.setOnClickListener({
            bottomSheetDeleteError?.dismiss()
        })

        bottomSheetDeleteError?.show()
    }



    private fun openBottomPopupSendOffer() {

        bottomSheetSendOffer = BottomSheetDialog(currActivity!!)
        var bottomSheetSendoffer =
            DataBindingUtil.inflate<BottomDailogSheetSinglelistingSendofferBinding>(
                LayoutInflater.from(currActivity),
                R.layout.bottom_dailog_sheet_singlelisting_sendoffer, null, false
            )

        bottomSheetSendOffer?.setContentView(bottomSheetSendoffer!!.root)
        Objects.requireNonNull<Window>(bottomSheetSendOffer?.window)
            .setBackgroundDrawableResource(android.R.color.transparent)

        extraDataModel= SessionManager.getExtraDataModel()!!
        var transportfee=extraDataModel.data.transporter_commission

        var llAdjustprice=bottomSheetSendOffer!!.findViewById<LinearLayout>(R.id.llAdjustprice)
        var llSendoffer=bottomSheetSendOffer!!.findViewById<LinearLayout>(R.id.llSendoffer)
        var llPriceTag=bottomSheetSendOffer!!.findViewById<LinearLayout>(R.id.llPriceTag)
        var etAmount=bottomSheetSendOffer!!.findViewById<EditText>(R.id.etAmount)
        var tvLivofee=bottomSheetSendOffer!!.findViewById<TextView>(R.id.tvLivofee)
        var tvLivofeetitle=bottomSheetSendOffer!!.findViewById<TextView>(R.id.tvLivofeetitle)
        var tvTotalAmount=bottomSheetSendOffer!!.findViewById<TextView>(R.id.tvTotalAmount)
        var tvAdjustPrice=bottomSheetSendOffer!!.findViewById<TextView>(R.id.tvAdjustPrice)
        var tvSendoffer=bottomSheetSendOffer!!.findViewById<TextView>(R.id.tvSendoffer)
        var tvMsg=bottomSheetSendOffer!!.findViewById<TextView>(R.id.tvMsg)
        tvMsg?.visibility=View.GONE

        tvLivofeetitle?.text=resources.getString(R.string.livo_fee)+" ("+transportfee+"%)"

        etAmount?.setText(listing_price)
        var a=(etAmount?.text.toString()).toDouble()
        var b=a*(transportfee.toDouble()/100)
        tvLivofee?.text="-"+String.format("%.2f", b)+" kr"
        tvTotalAmount?.text=String.format("%.2f", a-b)+" kr"

        llAdjustprice?.setOnClickListener({

            if (tvAdjustPrice?.text.toString().equals(resources.getString(R.string.adjust_price)))
            {
                etAmount?.isEnabled=true
                llPriceTag?.setBackgroundDrawable(resources.getDrawable(R.drawable.grey_round_shape_45_opacity))

                etAmount?.setBackgroundColor(resources.getColor(R.color.transparant))
                etAmount?.requestFocus()
                val imm: InputMethodManager =
                    currActivity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
            else{
                bottomSheetSendOffer!!.dismiss()
            }

        })

        etAmount?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) { }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                tvAdjustPrice?.text=resources.getString(R.string.cancel)

                if (s.isEmpty())
                    etAmount.setText("0")
                else{
                    var a=(s.toString()).toDouble()
                    var b=a*(transportfee.toDouble()/100)
                    tvLivofee?.text="-"+String.format("%.2f", b)+" kr"
                    tvTotalAmount?.text=String.format("%.2f", a-b).toString()+" kr"
                }
            }
        })

        llSendoffer?.setOnClickListener({

            showProgressBar()
            productViewModel?.let {
                if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                    var jsonObject =  JsonObject()
                    jsonObject.addProperty("list_id", id)
                    jsonObject.addProperty("bidding_price", etAmount?.text.toString())

                    it.getPlacebid(jsonObject)
                }
            }

        })

        bottomSheetSendOffer?.show()
    }


    private fun openThankYouDialog() {

        dialogReport?.dismiss()

        dialogThankyou = Dialog(currActivity)
        thankyouPopupBinding = DataBindingUtil.inflate(
            LayoutInflater.from(currActivity),
            R.layout.thankyou_popup, null, true
        )

        dialogThankyou?.setContentView(thankyouPopupBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialogThankyou?.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)

        dialogThankyou?.getWindow()!!.getAttributes().windowAnimations = R.style.dialog_animation;

        thankyouPopupBinding?.rlMain?.setOnClickListener{
            dialogThankyou?.dismiss()
        }
        dialogThankyou?.setCanceledOnTouchOutside(true)
        dialogThankyou?.setCancelable(true)
        dialogThankyou?.show()
    }




    private fun openOfferSentSuccessfully() {

        bottomSheetSendOffer?.dismiss()

        dialogOfferSent = Dialog(currActivity)
        offerSentSuccessfullyPopupBinding = DataBindingUtil.inflate(
            LayoutInflater.from(currActivity),
            R.layout.offer_sent_successfully_popup, null, true
        )

        dialogOfferSent?.setContentView(offerSentSuccessfullyPopupBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialogOfferSent?.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)

        dialogOfferSent?.getWindow()!!.getAttributes().windowAnimations = R.style.dialog_animation;

        offerSentSuccessfullyPopupBinding?.rlMain?.setOnClickListener{
            dialogOfferSent?.dismiss()
        }
        dialogOfferSent?.setCanceledOnTouchOutside(true)
        dialogOfferSent?.setCancelable(true)
        dialogOfferSent?.show()
    }


}