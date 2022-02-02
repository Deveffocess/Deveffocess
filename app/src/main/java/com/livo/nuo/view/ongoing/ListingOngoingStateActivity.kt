package com.livo.nuo.view.ongoing

import android.R.attr
import android.app.Activity
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.provider.MediaStore
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.jaeger.library.StatusBarUtil
import com.livo.nuo.R
import com.livo.nuo.databinding.*
import com.livo.nuo.lib.customcamera.*
import com.livo.nuo.lib.customcamera.options.Commons
import com.livo.nuo.lib.slidetoact.SlideToActView
import com.livo.nuo.lib.stepperview.VerticalStepperItemView
import com.livo.nuo.manager.SessionManager
import com.livo.nuo.models.ExtraDataModel
import com.livo.nuo.utility.AndroidUtil
import com.livo.nuo.utility.AppUtils
import com.livo.nuo.utility.CheckPermission
import com.livo.nuo.utility.LocalizeActivity
import com.livo.nuo.view.message.ChatActivity
import com.livo.nuo.view.profile.ContactAdminActivity
import com.livo.nuo.viewModel.ViewModelFactory
import com.livo.nuo.viewModel.ongoingstate.OngoingStateModel
import com.loopeer.shadow.ShadowView
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.lang.Exception
import java.util.*
import android.os.*
import android.graphics.Bitmap
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.widget.RelativeLayout
import android.R.attr.button
import android.view.*

import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import com.tooltip.Tooltip


class ListingOngoingStateActivity : LocalizeActivity(){



    private var listingPopupBinding: ListingSuccessPopupBinding? = null
    private  var dialogReferral: Dialog? = null
    private  var dialogPopImage: Dialog? = null
    var bottomSheetSendOffer:BottomSheetDialog?=null
    private  var dialogTakepic: Dialog? = null
    private var takepicPopupBinding: PopUpCameraViewBinding? = null
    private var popUpPickupDropoffImageBinding: PopUpPickupDropoffImageBinding? = null
    private val mSteppers = arrayOfNulls<VerticalStepperItemView>(4)

    private var currActivity : Activity = this

    lateinit var imgArrow : ImageView
    lateinit var imgBack:ImageView
    lateinit var btnApprove : TextView
    lateinit var tvNotIntrested : TextView
    lateinit var tvOpenMap : TextView
    lateinit var svOpenMap : ShadowView
    lateinit var tvTakePicture : TextView
    lateinit var svTakePicture : ShadowView
    lateinit var tvOpenMapDrop : TextView
    lateinit var svOpenMapDrop : ShadowView
    lateinit var tvTakePictureDrop : TextView
    lateinit var svTakePictureDrop : ShadowView
    lateinit var rlRatingHelp : RelativeLayout
    lateinit var rlHelp : RelativeLayout
    lateinit var viewFlipper : ViewFlipper

    lateinit var lltransporter_Approval : LinearLayout
    lateinit var lltransporter_Acceptdecline:LinearLayout
    lateinit var btntransporter_Approval : TextView
    lateinit var llMake_payment : LinearLayout
    lateinit var btnMakePayment : TextView
    lateinit var svMakePayment : ShadowView
    lateinit var slideView : SlideToActView
    lateinit var tvStatus: AppCompatTextView
    lateinit var imgDropoff:ImageView
    lateinit var tvStatusDrop:TextView
    lateinit var tvStatusComplete:TextView
    lateinit var tvStatusApprove:TextView
    lateinit var imgCall:ImageView

    lateinit var llMessageTransporter:LinearLayout
    lateinit var llOpenMapTakePicture:LinearLayout
    lateinit var llMessageTransporterdrop:LinearLayout
    lateinit var llOpenMapTakePicturedrop:LinearLayout

    lateinit var shimmerUserData:ShimmerFrameLayout
    lateinit var shimmerSteps:ShimmerFrameLayout
    lateinit var rltransporter:RelativeLayout
    lateinit var rlSteps:RelativeLayout

    lateinit var imgUser:ImageView
    lateinit var tvShimmerImage:ShimmerFrameLayout
    lateinit var imgProductImage:ImageView
    lateinit var tvTitle:TextView
    lateinit var tvProposedAmount : TextView
    lateinit var tvMessageTransporterdrop:TextView
    lateinit var tvMessageTransporter:TextView

    lateinit var imgProductImage1:ImageView
    lateinit var preview:CameraView
    var slot1=0
    var slot2=0
    val IMAGE_PICKUP_CODE_1=20
    val IMAGE_PICKUP_CODE_2=21
    var picturePath1=""
    var picturePath2=""
    lateinit var imagesDir:String
    var extension: String = ""
    lateinit var imgMessage:ImageView

    private val currentFlash = Values.FLASH_AUTO

    lateinit var imgPickup:ImageView

    private lateinit var dialog: Dialog


    var flashtor=0

    var photographer: Photographer? = null
    var photographerHelper: PhotographerHelper? = null

    private var isFlashAvailable = false
    private var mCameraManager: CameraManager? = null
    private var mCameraId: String? = null

    var amount=0.0
    var value=""
    var channel_id=""
    var accept=0

    var extraDataModel= ExtraDataModel()
    private var ongoingViewModel : OngoingStateModel? = null
    private var bottomSheetApplicationDialog: BottomSheetDialog?=null

    var id=0
    var is_transpoter="false"

    var pickup_latitude=""
    var pickup_longitude=""
    var dropoff_latitude=""
    var dropoff_longitude=""
    var phone=""
    var pickupimage_url:String=""
    var dropoffimage_url=""
    var pickupdate=""
    var dropoffdate=""
    var status=""

    lateinit var stepper_0 : VerticalStepperItemView
    lateinit var stepper_1 : VerticalStepperItemView
    lateinit var stepper_2 : VerticalStepperItemView
    lateinit var stepper_3 : VerticalStepperItemView

    //payment
    lateinit var paymentSheet: PaymentSheet
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    lateinit var paymentIntentClientSecret: String

    var addr=""
    var dropaddr=""
    var list_id=0
    var pubnub_uuid=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing_ongoing_state)

        imgArrow =findViewById(R.id.imgArrow)
        imgBack=findViewById(R.id.imgBack)
        btnApprove = findViewById(R.id.btnApprove)
        tvNotIntrested = findViewById(R.id.tvNotIntrested)
        btnMakePayment = findViewById(R.id.btnMakePayment)
        svMakePayment = findViewById(R.id.svMakePayment)
        llMake_payment = findViewById(R.id.llMake_payment)
        lltransporter_Approval = findViewById(R.id.lltransporter_Approval)
        lltransporter_Acceptdecline=findViewById(R.id.lltransporter_Acceptdecline)
        btntransporter_Approval = findViewById(R.id.btntransporter_Approval)
        tvOpenMap = findViewById(R.id.tvOpenMap)
        svOpenMap = findViewById(R.id.svOpenMap)
        tvTakePicture = findViewById(R.id.tvTakePicture)
        svTakePicture = findViewById(R.id.svTakePicture)
        tvOpenMapDrop = findViewById(R.id.tvOpenMapdrop)
        svOpenMapDrop = findViewById(R.id.svOpenMapdrop)
        tvTakePictureDrop = findViewById(R.id.tvTakePicturedrop)
        svTakePictureDrop = findViewById(R.id.svTakePicturedrop)
        imgPickup=findViewById(R.id.imgPickup)
        imgDropoff=findViewById(R.id.imgDropoff)
        tvStatus=findViewById(R.id.tvStatus)
        tvStatusDrop=findViewById(R.id.tvStatusDrop)
        tvStatusComplete=findViewById(R.id.tvStatusCompleted)
        tvStatusApprove=findViewById(R.id.tvStatusApprove)
        imgCall=findViewById(R.id.imgCall)
        imgMessage=findViewById(R.id.imgMessage)
        tvMessageTransporterdrop=findViewById(R.id.tvMessageTransporterdrop)
        tvMessageTransporter=findViewById(R.id.tvMessageTransporter)


        rlRatingHelp = findViewById(R.id.rlRatingHelp)
        rlHelp = findViewById(R.id.rlHelp)
        viewFlipper = findViewById(R.id.viewFlipper)
        slideView = findViewById(R.id.slideView)
        imgUser=findViewById(R.id.imgUser)
        tvShimmerImage=findViewById(R.id.shimmerImagex)
        imgProductImage=findViewById(R.id.imgProductImage)
        tvTitle=findViewById(R.id.tvTitle)
        tvProposedAmount=findViewById(R.id.tvProposedAmount)
        rltransporter=findViewById(R.id.rltransporter)
        shimmerUserData=findViewById(R.id.shimmerUserData)
        shimmerSteps=findViewById(R.id.shimmerSteps)
        rlSteps=findViewById(R.id.rlSteps)

        llMessageTransporter=findViewById(R.id.llMessageTransporter)
        llOpenMapTakePicture=findViewById(R.id.llOpenMapTakePicture)
        llMessageTransporterdrop=findViewById(R.id.llMessageTransporterdrop)
        llOpenMapTakePicturedrop=findViewById(R.id.llOpenMapTakePicturedrop)


        id =intent.getIntExtra("id",0) // id send for if condition


        stepper_0 = findViewById(R.id.stepper_0)
        stepper_1 = findViewById(R.id.stepper_1)
        stepper_2 = findViewById(R.id.stepper_2)
        stepper_3 = findViewById(R.id.stepper_3)

        mSteppers[0] = stepper_0
        mSteppers[1] = stepper_1
        mSteppers[2] = stepper_2
        mSteppers[3] = stepper_3
        VerticalStepperItemView.bindSteppers(*mSteppers)

        rltransporter.visibility=View.GONE
        rlSteps.visibility=View.GONE
        shimmerUserData.visibility=View.VISIBLE
        shimmerSteps.visibility=View.VISIBLE
        shimmerUserData.startShimmer()
        shimmerSteps.startShimmer()

        initViews()

    }


    fun initViews(){

        isFlashAvailable = currActivity!!.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)

        mCameraManager = currActivity!!.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            mCameraId = mCameraManager?.getCameraIdList()?.get(0)

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        currActivity?.application?.let {
            ongoingViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(OngoingStateModel::class.java)
        }

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)


        viewAllStates()

        extraDataModel= SessionManager.getExtraDataModel()!!

        observers()

        imgBack.setOnClickListener({
            finish()
        })

        val view = slideView as SlideToActView
        view.onSlideToActAnimationEventListener = object : SlideToActView.OnSlideToActAnimationEventListener{
            override fun onSlideCompleteAnimationStarted(view: SlideToActView, threshold: Float) {

            }

            override fun onSlideCompleteAnimationEnded(view: SlideToActView) {
                if (is_transpoter=="true") {

                    showProgressBar()
                    ongoingViewModel?.let {
                        if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {
                            var jsonObject =  JsonObject();
                            jsonObject.addProperty("offer_id", id)
                            it.getTrnsCompleteListing(jsonObject)
                        }
                    }
                }
                else {
                     showProgressBar()
                    ongoingViewModel?.let {
                        if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {
                            var jsonObject =  JsonObject();
                            jsonObject.addProperty("offer_id", id)
                            it.getSenderCompletesListing(jsonObject)
                        }
                    }
                }

            }

            override fun onSlideResetAnimationStarted(view: SlideToActView) {

            }

            override fun onSlideResetAnimationEnded(view: SlideToActView) {

            }
        }

        imgCall.setOnClickListener({
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            startActivity(intent)
        })

        rlHelp.setOnClickListener({

            var i=Intent(currActivity,ContactAdminActivity::class.java)
            startActivity(i)

        })

        tvMessageTransporterdrop.setOnClickListener({
            var i=Intent(currActivity,ChatActivity::class.java)
            i.putExtra("ch",channel_id)
            i.putExtra("st",status)
            i.putExtra("ruuid",pubnub_uuid)
            currActivity.startActivity(i)
        })

        tvMessageTransporter.setOnClickListener({
            var i=Intent(currActivity,ChatActivity::class.java)
            i.putExtra("ch",channel_id)
            i.putExtra("st",status)
            i.putExtra("ruuid",pubnub_uuid)
            currActivity.startActivity(i)
        })

        imgMessage.setOnClickListener({
            if(channel_id.isEmpty()) {

                showProgressBar()
                ongoingViewModel?.let {
                    if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {
                        var jsonObject =  JsonObject();
                        jsonObject.addProperty("offer_id", id)

                        it.getAddChannel(jsonObject)
                    }
                }
            }

            else{
                var i=Intent(currActivity,ChatActivity::class.java)
                i.putExtra("ch",channel_id)
                i.putExtra("st",status)
                i.putExtra("ruuid",pubnub_uuid)
                currActivity.startActivity(i)
            }

        })
        var cci=0
        tvStatus.setOnClickListener({v->
            if (!addr.isEmpty()) {
                if (cci==0) {

                    var tooltip = Tooltip.Builder(v)
                        .setText(addr)
                        .setTextColor(Color.WHITE)
                        .setGravity(Gravity.TOP)
                        .setCornerRadius(8f)
                        .setBackgroundColor(resources.getColor(R.color.livo_green))
                        .setDismissOnClick(true)
                        .setOnDismissListener({
                           cci=0
                        })
                        .show()

                    cci=1
                }
               }
           })

        var ccd=0
        tvStatusDrop.setOnClickListener({v->
            if (!dropaddr.isEmpty()) {
                if (ccd==0) {

                    var tooltip = Tooltip.Builder(v)
                        .setText(dropaddr)
                        .setTextColor(Color.WHITE)
                        .setGravity(Gravity.TOP)
                        .setCornerRadius(8f)
                        .setBackgroundColor(resources.getColor(R.color.livo_green))
                        .setDismissOnClick(true)
                        .setOnDismissListener({
                            ccd=0
                        })
                        .show()

                    ccd=1
                }
            }
        })

        tvNotIntrested.setOnClickListener({

            if(tvNotIntrested.text.equals(resources.getString(R.string.decline)))
            {
                ongoingViewModel?.let {
                    if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                        var jsonObject =  JsonObject();
                        jsonObject.addProperty("offer_id", id)

                        it.getTrnsDeclineApproval(jsonObject)
                    }
                }

                showProgressBar()
            }
            else if (tvNotIntrested.text.equals(resources.getString(R.string.not_interested)))
            {
                var i = Intent(currActivity, TransporterOffersActivity::class.java)
                i.putExtra("id", list_id)
                startActivity(i)
                finish()
            }
            else{

                ongoingViewModel?.let {

                    showProgressBar()

                    if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                        var jsonObject =  JsonObject();
                        jsonObject.addProperty("offer_id", id)
                        it.getDeleteBid(jsonObject)
                    }
                }


            }

        })


        currActivity?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(mMessageReceiver,
                IntentFilter("listing_ongoing"))
        }
    }


    var mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {

            val id = intent.getStringExtra("id")

            ongoingViewModel?.let {
                if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                    var jsonObject =  JsonObject();
                    jsonObject.addProperty("offer_id", id)

                    it.getViewAllState(jsonObject)
                }
            }

        }
    }




    private fun observers(){
        ongoingViewModel?.getMutableLiveDataViewAllState()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {

            rltransporter.visibility=View.VISIBLE
            rlSteps.visibility=View.VISIBLE
            shimmerUserData.visibility=View.GONE
            shimmerSteps.visibility=View.GONE
            shimmerUserData.stopShimmer()
            shimmerSteps.stopShimmer()

            pickup_latitude=it.data.pickup_latitude
            pickup_longitude=it.data.pickup_longitude
            dropoff_latitude=it.data.dropoff_latitude
            dropoff_longitude=it.data.dropoff_longitude
            channel_id=it.data.channel_id

            list_id=it.data.list_id
            pubnub_uuid=it.data.user_details.pubnub_uuid

            is_transpoter=it.data.is_transporter
            tvTitle.text=it.data.user_details.first_name+" "+it.data.user_details.last_name
            tvProposedAmount.text=resources.getString(R.string.offer_price)+" : "+it.data.price+"kr"
            value=it.data.price
            amount=it.data.price.toDouble()

            var current_level=it.data.current_level


            if (is_transpoter.equals("false")) {//sender

                if (current_level == 0) {
                    var default_state = it.data.main_levels.default
                    var sub_level = it.data.main_levels.default.sub_level

                    mSteppers[0]?.title = default_state.title
                    tvStatusApprove.text = default_state.sub_title

                    if (sub_level == 0) {
                        lltransporter_Acceptdecline.visibility = View.VISIBLE
                        lltransporter_Approval.visibility = View.GONE
                        llMake_payment.visibility = View.GONE
                    }
                    if (sub_level == 1) {
                        lltransporter_Acceptdecline.visibility = View.GONE
                        lltransporter_Approval.visibility = View.VISIBLE
                        llMake_payment.visibility = View.GONE
                    }
                    if (sub_level == 2) {
                        lltransporter_Acceptdecline.visibility = View.GONE
                        lltransporter_Approval.visibility = View.GONE
                        llMake_payment.visibility = View.VISIBLE
                    }
                    if (sub_level == 3) {
                        lltransporter_Acceptdecline.visibility = View.GONE
                        lltransporter_Approval.visibility = View.VISIBLE
                        btntransporter_Approval.text=resources.getString(R.string.waiting_payment_approval)
                        llMake_payment.visibility = View.GONE
                    }

                    val buttonLayoutParams: RelativeLayout.LayoutParams =
                        RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
                    buttonLayoutParams.setMargins(0, 0, 0, 30)
                    rlSteps.setLayoutParams(buttonLayoutParams)

                }

                else if(current_level==1)
                {
                    var default_state = it.data.main_levels.default
                    mSteppers[0]?.title = default_state.title
                    tvStatusApprove.text = default_state.sub_title

                    var pickup_state = it.data.main_levels.pickup
                    var sub_level = it.data.main_levels.pickup.sub_level

                    mSteppers[1]?.title = pickup_state.title
                    tvStatus.text = pickup_state.sub_title

                    if (sub_level==0)
                    {
                        llMessageTransporter.visibility=View.VISIBLE
                        llOpenMapTakePicture.visibility=View.GONE
                    }

                    lltransporter_Acceptdecline.visibility=View.GONE
                    lltransporter_Approval.visibility=View.GONE
                    llMake_payment.visibility=View.GONE

                    mSteppers[0]?.state = VerticalStepperItemView.STATE_SELECTED
                    mSteppers[1]?.state = VerticalStepperItemView.STATE_SELECTED
                    /*mSteppers[2]?.state = VerticalStepperItemView.STATE_NORMAL
                    mSteppers[3]?.state = VerticalStepperItemView.STATE_NORMAL*/
                    stepper_1.lineColor=resources.getColor(R.color.livo_black_5_opacity)

                    val buttonLayoutParams: RelativeLayout.LayoutParams =
                        RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
                    buttonLayoutParams.setMargins(0, 0, 0, 30)
                    rlSteps.setLayoutParams(buttonLayoutParams)
                }

                else if(current_level==2)
                {
                    var default_state = it.data.main_levels.default
                    mSteppers[0]?.title = default_state.title
                    tvStatusApprove.text = default_state.sub_title

                    var pickup_state = it.data.main_levels.pickup
                    mSteppers[1]?.title = pickup_state.title
                    tvStatus.text = pickup_state.sub_title

                    llMessageTransporter.visibility=View.GONE
                    llOpenMapTakePicture.visibility=View.GONE

                    Glide.with(currActivity)
                        .load(pickup_state.thumb).placeholder(currActivity.getDrawable(R.drawable.grey_round_shape))
                        .centerCrop()
                        .error(currActivity.getDrawable(R.drawable.grey_round_shape)).into(imgPickup)

                    pickupimage_url=pickup_state.image
                    pickupdate=pickup_state.sub_title

                    var dropoff_state = it.data.main_levels.dropoff
                    mSteppers[2]?.title = dropoff_state.title
                    mSteppers[2]?.summary = dropoff_state.sub_title

                    var sub_level = it.data.main_levels.dropoff.sub_level

                    if (sub_level==0)
                    {
                        llMessageTransporterdrop.visibility=View.VISIBLE
                        llOpenMapTakePicturedrop.visibility=View.GONE
                    }

                    lltransporter_Acceptdecline.visibility=View.GONE
                    lltransporter_Approval.visibility=View.GONE
                    llMake_payment.visibility=View.GONE

                    mSteppers[0]?.state = VerticalStepperItemView.STATE_SELECTED
                    mSteppers[1]?.state = VerticalStepperItemView.STATE_SELECTED
                    mSteppers[2]?.state = VerticalStepperItemView.STATE_SELECTED
                   // mSteppers[3]?.state = VerticalStepperItemView.STATE_NORMAL
                    stepper_2.lineColor=resources.getColor(R.color.livo_black_5_opacity)

                    val buttonLayoutParams: RelativeLayout.LayoutParams =
                        RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
                    buttonLayoutParams.setMargins(0, 0, 0, 30)
                    rlSteps.setLayoutParams(buttonLayoutParams)
                }
                else if(current_level==3)
                {
                    var default_state = it.data.main_levels.default
                    mSteppers[0]?.title = default_state.title
                    tvStatusApprove.text = default_state.sub_title

                    var pickup_state = it.data.main_levels.pickup
                    mSteppers[1]?.title = pickup_state.title
                    tvStatus.text=pickup_state.sub_title

                    var dropoff_state = it.data.main_levels.dropoff
                    mSteppers[2]?.title = dropoff_state.title
                    tvStatusDrop.text = dropoff_state.sub_title

                    var complete_state = it.data.main_levels.completed
                    mSteppers[3]?.title = complete_state.title
                    tvStatusComplete.text = complete_state.sub_title

                    llMessageTransporter.visibility=View.GONE
                    llOpenMapTakePicture.visibility=View.GONE
                    llMessageTransporterdrop.visibility=View.GONE
                    llOpenMapTakePicturedrop.visibility=View.GONE

                    Glide.with(currActivity)
                        .load(pickup_state.thumb).placeholder(currActivity.getDrawable(R.drawable.grey_round_shape))
                        .centerCrop()
                        .error(currActivity.getDrawable(R.drawable.grey_round_shape)).into(imgPickup)

                    Glide.with(currActivity)
                        .load(dropoff_state.thumb).placeholder(currActivity.getDrawable(R.drawable.grey_round_shape))
                        .centerCrop()
                        .error(currActivity.getDrawable(R.drawable.grey_round_shape)).into(imgDropoff)

                    pickupimage_url=pickup_state.image
                    dropoffimage_url=dropoff_state.image
                    pickupdate=pickup_state.sub_title
                    dropoffdate=dropoff_state.sub_title

                    var sub_level = it.data.main_levels.completed.sub_level

                    if (sub_level==0)
                    {
                        slideView.visibility = View.VISIBLE

                        val buttonLayoutParams: RelativeLayout.LayoutParams =
                            RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
                        buttonLayoutParams.setMargins(0, 0, 0, 280)
                        rlSteps.setLayoutParams(buttonLayoutParams)
                    }
                    else if(sub_level==1)
                    {
                        slideView.visibility = View.VISIBLE

                        val buttonLayoutParams: RelativeLayout.LayoutParams =
                            RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
                        buttonLayoutParams.setMargins(0, 0, 0, 280)
                        rlSteps.setLayoutParams(buttonLayoutParams)
                    }
                    else if(sub_level==2){
                        status="completed"
                        rlRatingHelp.visibility = View.VISIBLE
                        rlHelp.visibility=View.VISIBLE

                        val buttonLayoutParams: RelativeLayout.LayoutParams =
                            RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                            )
                        buttonLayoutParams.setMargins(0, 0, 0, 30)
                        rlSteps.setLayoutParams(buttonLayoutParams)
                    }

                    lltransporter_Acceptdecline.visibility=View.GONE
                    lltransporter_Approval.visibility=View.GONE
                    llMake_payment.visibility=View.GONE

                    mSteppers[0]?.state = VerticalStepperItemView.STATE_SELECTED
                    mSteppers[1]?.state = VerticalStepperItemView.STATE_SELECTED
                    mSteppers[2]?.state = VerticalStepperItemView.STATE_SELECTED
                    mSteppers[3]?.state = VerticalStepperItemView.STATE_SELECTED

                }
            }
            else{

                if (current_level == 0) {
                    var default_state = it.data.main_levels.default
                    var sub_level = it.data.main_levels.default.sub_level

                    mSteppers[0]?.title = default_state.title
                    tvStatusApprove.text = default_state.sub_title

                    if (sub_level == 0) {
                        lltransporter_Acceptdecline.visibility = View.VISIBLE
                        lltransporter_Approval.visibility = View.GONE

                        if (channel_id.isEmpty()){
                        imgCall.setColorFilter(getResources().getColor(R.color.black_20_opacity))
                        imgMessage.setColorFilter(getResources().getColor(R.color.black_20_opacity))
                        imgCall.isEnabled=false
                        imgMessage.isEnabled=false}

                        btnApprove.text=resources.getString(R.string.change_price)
                        tvNotIntrested.text=resources.getString(R.string.delete_offer)

                    }
                    if (sub_level == 1) {

                        imgCall.setColorFilter(getResources().getColor(R.color.error_red_delete))
                        imgMessage.setColorFilter(getResources().getColor(R.color.livo_green))

                        lltransporter_Acceptdecline.visibility = View.VISIBLE
                        lltransporter_Approval.visibility = View.GONE
                        imgCall.isEnabled=true
                        imgMessage.isEnabled=true


                        btnApprove.text = applicationContext.resources.getString(R.string.accept_sender)
                        tvNotIntrested.text = applicationContext.resources.getString(R.string.decline)

                    }
                    if (sub_level == 2) {
                        lltransporter_Acceptdecline.visibility = View.GONE
                        lltransporter_Approval.visibility = View.VISIBLE
                    }
                    if (sub_level == 3) {
                        lltransporter_Acceptdecline.visibility = View.GONE
                        lltransporter_Approval.visibility = View.VISIBLE
                    }

                    val buttonLayoutParams: RelativeLayout.LayoutParams =
                        RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
                    buttonLayoutParams.setMargins(0, 0, 0, 30)
                    rlSteps.setLayoutParams(buttonLayoutParams)
                }

                else if(current_level==1)
                {
                    var default_state = it.data.main_levels.default
                    mSteppers[0]?.title = default_state.title
                    tvStatusApprove.text = default_state.sub_title

                    var pickup_state = it.data.main_levels.pickup
                    var sub_level = it.data.main_levels.pickup.sub_level

                    mSteppers[1]?.title = pickup_state.title

                    addr=it.data.pickup_address

                    if (sub_level==0)
                    {
                        if (addr.length>35) {

                            tvStatus.text = addr.substring(0, 35) + "... view all"
                            var spannable = SpannableStringBuilder(tvStatus.text)
                            spannable.setSpan(
                                ForegroundColorSpan(resources.getColor(R.color.livo_blue)),
                                /* start index */ 39, /* end index */ 47,
                                Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                            tvStatus.text=spannable
                        }
                        else {
                            tvStatus.text = addr
                        }

                        llMessageTransporter.visibility=View.GONE
                        llOpenMapTakePicture.visibility=View.VISIBLE
                    }

                    lltransporter_Acceptdecline.visibility=View.GONE
                    lltransporter_Approval.visibility=View.GONE
                    llMake_payment.visibility=View.GONE

                    mSteppers[0]?.state = VerticalStepperItemView.STATE_SELECTED
                    mSteppers[1]?.state = VerticalStepperItemView.STATE_SELECTED
                    /*mSteppers[2]?.state = VerticalStepperItemView.STATE_NORMAL
                    mSteppers[3]?.state = VerticalStepperItemView.STATE_NORMAL*/
                    stepper_1.lineColor=resources.getColor(R.color.livo_black_5_opacity)

                    val buttonLayoutParams: RelativeLayout.LayoutParams =
                        RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
                    buttonLayoutParams.setMargins(0, 0, 0, 30)
                    rlSteps.setLayoutParams(buttonLayoutParams)

                }

                else if(current_level==2)
                {
                    var default_state = it.data.main_levels.default
                    mSteppers[0]?.title = default_state.title
                    tvStatusApprove.text = default_state.sub_title

                    var pickup_state = it.data.main_levels.pickup
                    mSteppers[1]?.title = pickup_state.title

                    llMessageTransporter.visibility=View.GONE
                    llOpenMapTakePicture.visibility=View.GONE

                    Glide.with(currActivity)
                        .load(pickup_state.thumb).placeholder(currActivity.getDrawable(R.drawable.grey_round_shape))
                        .centerCrop()
                        .error(currActivity.getDrawable(R.drawable.grey_round_shape)).into(imgPickup)

                    pickupimage_url=pickup_state.image
                    pickupdate=pickup_state.sub_title
                    tvStatus.text = pickup_state.sub_title
                    addr=""

                    var dropoff_state = it.data.main_levels.dropoff
                    mSteppers[2]?.title = dropoff_state.title
                    tvStatusDrop.text = dropoff_state.sub_title

                    var sub_level = it.data.main_levels.dropoff.sub_level

                    dropaddr=it.data.dropoff_address

                    if (sub_level==0)
                    {
                        if (dropaddr.length>35) {

                            tvStatusDrop.text = dropaddr.substring(0, 35) + "... view all"
                            var spannable = SpannableStringBuilder(tvStatusDrop.text)
                            spannable.setSpan(
                                ForegroundColorSpan(resources.getColor(R.color.livo_blue)),
                                /* start index */ 39, /* end index */ 47,
                                Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                            tvStatusDrop.text=spannable
                        }
                        else {
                            tvStatusDrop.text = dropaddr
                        }

                        llMessageTransporterdrop.visibility=View.GONE
                        llOpenMapTakePicturedrop.visibility=View.VISIBLE
                    }

                    lltransporter_Acceptdecline.visibility=View.GONE
                    lltransporter_Approval.visibility=View.GONE
                    llMake_payment.visibility=View.GONE

                    mSteppers[0]?.state = VerticalStepperItemView.STATE_SELECTED
                    mSteppers[1]?.state = VerticalStepperItemView.STATE_SELECTED
                    mSteppers[2]?.state = VerticalStepperItemView.STATE_SELECTED
                    // mSteppers[3]?.state = VerticalStepperItemView.STATE_NORMAL
                    stepper_2.lineColor=resources.getColor(R.color.livo_black_5_opacity)

                    val buttonLayoutParams: RelativeLayout.LayoutParams =
                        RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
                    buttonLayoutParams.setMargins(0, 0, 0, 30)
                    rlSteps.setLayoutParams(buttonLayoutParams)

                }

                else if(current_level==3)
                {
                    var default_state = it.data.main_levels.default
                    mSteppers[0]?.title = default_state.title
                    tvStatusApprove.text = default_state.sub_title

                    var pickup_state = it.data.main_levels.pickup
                    mSteppers[1]?.title = pickup_state.title
                    tvStatus.text=pickup_state.sub_title

                    var dropoff_state = it.data.main_levels.dropoff
                    mSteppers[2]?.title = dropoff_state.title
                    tvStatusDrop.text = dropoff_state.sub_title

                    dropaddr=""

                    llMessageTransporter.visibility=View.GONE
                    llOpenMapTakePicture.visibility=View.GONE
                    llMessageTransporterdrop.visibility=View.GONE
                    llOpenMapTakePicturedrop.visibility=View.GONE

                    Glide.with(currActivity)
                        .load(pickup_state.thumb).placeholder(currActivity.getDrawable(R.drawable.grey_round_shape))
                        .centerCrop()
                        .error(currActivity.getDrawable(R.drawable.grey_round_shape)).into(imgPickup)

                    Glide.with(currActivity)
                        .load(dropoff_state.thumb).placeholder(currActivity.getDrawable(R.drawable.grey_round_shape))
                        .centerCrop()
                        .error(currActivity.getDrawable(R.drawable.grey_round_shape)).into(imgDropoff)

                    pickupimage_url=pickup_state.image
                    dropoffimage_url=dropoff_state.image
                    pickupdate=pickup_state.sub_title
                    dropoffdate=dropoff_state.sub_title

                    var complete_state = it.data.main_levels.completed
                    mSteppers[3]?.title = complete_state.title
                    tvStatusComplete.text = complete_state.sub_title

                    var sub_level = it.data.main_levels.completed.sub_level

                    if (sub_level==0)
                    {
                        slideView.visibility = View.VISIBLE

                        val buttonLayoutParams: RelativeLayout.LayoutParams =
                            RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
                        buttonLayoutParams.setMargins(0, 0, 0, 280)
                        rlSteps.setLayoutParams(buttonLayoutParams)
                    }

                    else if (sub_level==1)
                    {
                        slideView.visibility = View.GONE

                        val buttonLayoutParams: RelativeLayout.LayoutParams =
                            RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
                        buttonLayoutParams.setMargins(0, 0, 0, 30)
                        rlSteps.setLayoutParams(buttonLayoutParams)
                    }
                    else if(sub_level==2) {
                        rlRatingHelp.visibility = View.VISIBLE
                        rlHelp.visibility = View.VISIBLE
                        slideView.visibility = View.GONE
                        status="completed"


                        val buttonLayoutParams: RelativeLayout.LayoutParams =
                            RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                            )
                        buttonLayoutParams.setMargins(0, 0, 0, 30)
                        rlSteps.setLayoutParams(buttonLayoutParams)
                    }

                    lltransporter_Acceptdecline.visibility=View.GONE
                    lltransporter_Approval.visibility=View.GONE
                    llMake_payment.visibility=View.GONE

                    mSteppers[0]?.state = VerticalStepperItemView.STATE_SELECTED
                    mSteppers[1]?.state = VerticalStepperItemView.STATE_SELECTED
                    mSteppers[2]?.state = VerticalStepperItemView.STATE_SELECTED
                    mSteppers[3]?.state = VerticalStepperItemView.STATE_SELECTED

                }

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



            Glide.with(currActivity)
                .load(it.data.user_details.profile_image).placeholder(currActivity.getDrawable(R.drawable.grey_round_shape))
                .error(currActivity.getDrawable(R.drawable.grey_round_shape)).into(imgProductImage)

            phone=it.data.user_details.country_code+it.data.user_details.phone_number


            oprations()

        })

          ongoingViewModel?.getMutableLiveDataSenderApproveTrns()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
              hideProgressBar()
              tvTakePicture.visibility = View.GONE
              svTakePicture.visibility = View.GONE
              tvTakePictureDrop.visibility = View.GONE
              svTakePictureDrop.visibility = View.GONE

              lltransporter_Approval.visibility = View.VISIBLE
              lltransporter_Acceptdecline.visibility=View.GONE

              if(channel_id.isEmpty()) {
                  accept=1
                  ongoingViewModel?.let {
                      if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {
                          var jsonObject =  JsonObject();
                          jsonObject.addProperty("offer_id", id)

                          it.getAddChannel(jsonObject)
                      }
                  }
              }

              viewAllStates()
          })

        ongoingViewModel?.getMutableLiveDataTrnsAcceptApproval()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
            hideProgressBar()

              lltransporter_Approval.visibility = View.GONE
              lltransporter_Acceptdecline.visibility=View.VISIBLE

            btnApprove.text=resources.getString(R.string.change_price)
            tvNotIntrested.text=resources.getString(R.string.delete_offer)

            viewAllStates()
          })

        ongoingViewModel?.getMutableLiveDataTrnsDeclineApproval()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
            hideProgressBar()

              lltransporter_Approval.visibility = View.VISIBLE
              lltransporter_Acceptdecline.visibility=View.GONE

            viewAllStates()
          })

        ongoingViewModel?.getMutableLiveDataTrnsCompleteListing()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
              hideProgressBar()
              showPopup()

              viewAllStates()
          })

        ongoingViewModel?.getMutableLiveDataSenderCompletesListing()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
            hideProgressBar()
              showPopup()

            viewAllStates()
          })

        ongoingViewModel?.getMutableLiveDataPlacebid()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
                var code=it.code
                bottomSheetSendOffer!!.dismiss()
                hideProgressBar()

                viewAllStates()
            })

         ongoingViewModel?.getMutableLiveDataMakePayment()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
                var code=it.code

                hideProgressBar()
               // openBottomPopup()

                paymentIntentClientSecret = it.data.payment_intent
                customerConfig = PaymentSheet.CustomerConfiguration(
                    it.data.customer,
                    it.data.ephemeral_key
                )
                val publishableKey = it.data.publishable_key
                PaymentConfiguration.init(this, publishableKey)

                presentPaymentSheet()

                //viewAllStates()
            })

        ongoingViewModel?.getMutableLiveDataDeleteBid()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {

                hideProgressBar()
                finish()
            })

        ongoingViewModel?.getMutableLiveDataChangePaymentStatus()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
                var code=it.code
                hideProgressBar()

                viewAllStates()
            })

        ongoingViewModel?.getMutableLiveDataPickupDropoffListing()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
                var code=it.code
                Log.e("ms",it.message)
                dialogTakepic?.dismiss()

                hideProgressBar()
                viewAllStates()
            })

         ongoingViewModel?.getMutableLiveDataAddChannel()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {

                channel_id=it.data.channel_id
                hideProgressBar()

                if(accept==1)
                {
                    accept=0
                }
                else{
                    Log.e("yhj",id.toString())
                var i=Intent(currActivity, ChatActivity::class.java)
                i.putExtra("ch",channel_id)
                i.putExtra("st",status)
                i.putExtra("ruuid",pubnub_uuid)
                currActivity.startActivity(i)
                }
            })

        ongoingViewModel?.getErrorMutableLiveData()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
            hideProgressBar()
            AppUtils.showToast(currActivity,R.drawable.cross,it.message,R.color.error_red,R.color.white,R.color.white)
        })
    }


    fun oprations(){

        imgPickup.setOnClickListener({
            if (pickupimage_url.isEmpty())
            {
            }
            else{
                showPopupImageView("1")
            }
        })

        imgDropoff.setOnClickListener({
            if (dropoffimage_url.isEmpty())
            {
            }
            else{
                showPopupImageView("2")
            }
        })


        if(is_transpoter == "true"){ //Transporter View

            btntransporter_Approval.text = applicationContext.resources.getString(R.string.waiting_sender_payment)


            llMake_payment.visibility = View.GONE

            btnApprove.setOnClickListener({

                if(btnApprove.text.equals(resources.getString(R.string.accept_sender)))
                {
                    ongoingViewModel?.let {
                        if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                            var jsonObject =  JsonObject();
                            jsonObject.addProperty("offer_id", id)

                            it.getTrnsAcceptApproval(jsonObject)
                        }
                    }

                    showProgressBar()
                }
                else{
                    openBottomPopupSendOffer()
                }


            })





            tvTakePicture.setOnClickListener({

                slot1=1
                slot2=0
                openTakePicDialog()


            })

            tvTakePictureDrop.setOnClickListener({

                slot2=1
                slot1=0
                openTakePicDialog()

            })


            tvOpenMap.setOnClickListener({

                val uri = "http://maps.google.com/maps?q=loc:$pickup_latitude,$pickup_longitude"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                currActivity.startActivity(intent)

            })

            tvOpenMapDrop.setOnClickListener({

                val uri = "http://maps.google.com/maps?q=loc:$dropoff_latitude,$dropoff_longitude"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                currActivity.startActivity(intent)

            })



        }else if(is_transpoter == "false"){ //Sender View

            //lltransporter_Approval.visibility = View.GONE

            btnApprove.setOnClickListener({


                ongoingViewModel?.let {
                    if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                        var jsonObject =  JsonObject();
                        jsonObject.addProperty("offer_id", id)
                        jsonObject.addProperty("offer_price", amount)
                        it.getSenderApproveTrns(jsonObject)
                    }
                }
                showProgressBar()
            })

            btntransporter_Approval.setOnClickListener({
               /* llMake_payment.visibility = View.VISIBLE
                lltransporter_Approval.visibility = View.GONE
                lltransporter_Acceptdecline.visibility=View.GONE*/
            })

            btnMakePayment.setOnClickListener({

                /*showProgressBar()

                ongoingViewModel?.let {
                    if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {
                        var jsonObject =  JsonObject();
                        jsonObject.addProperty("offer_id", id)
                        it.getMakePayment(jsonObject)
                    }
                }*/
                openBottomPopup()
            })


        }

    }




    private fun openTakePicDialog() {

        dialogTakepic = Dialog(currActivity)
        takepicPopupBinding = DataBindingUtil.inflate(
            LayoutInflater.from(currActivity),
            R.layout.pop_up_camera_view, null, true
        )

        dialogTakepic?.setContentView(takepicPopupBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialogTakepic?.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)

        dialogTakepic?.getWindow()!!.getAttributes().windowAnimations = R.style.dialog_animation;

        preview=dialogTakepic?.findViewById(R.id.preview)!!
        var imgCamera=dialogTakepic?.findViewById<ImageView>(R.id.imgCamera)
        imgProductImage1= dialogTakepic?.findViewById(R.id.imgProductImage)!!
        var imgFlash=dialogTakepic?.findViewById<ImageView>(R.id.imgFlash)
        var imgGallery=dialogTakepic?.findViewById<ImageView>(R.id.imgGallery)
        var tvClose=dialogTakepic?.findViewById<TextView>(R.id.tvClose)
        var tvUpload=dialogTakepic?.findViewById<TextView>(R.id.tvUpload)

        preview?.setFocusIndicatorDrawer(object : CanvasDrawer {
            private val SIZE = 300
            private val LINE_LENGTH = 50

            override fun initPaints(): Array<Paint>? {
                val focusPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                focusPaint.style = Paint.Style.STROKE
                focusPaint.strokeWidth = 3f
                focusPaint.color = Color.WHITE
                return arrayOf(focusPaint)
            }

            override fun draw(canvas: Canvas, point: Point, paints: Array<Paint?>?) {
                if (paints == null || paints.size == 0) return
                val left = point.x - SIZE / 2
                val top = point.y - SIZE / 2
                val right = point.x + SIZE / 2
                val bottom = point.y + SIZE / 2
                val paint = paints[0]
                canvas.drawLine(
                    left.toFloat(), (top + LINE_LENGTH).toFloat(), left.toFloat(), top.toFloat(),
                    paint!!
                )
                canvas.drawLine(
                    left.toFloat(), top.toFloat(), (left + LINE_LENGTH).toFloat(), top.toFloat(),
                    paint
                )
                canvas.drawLine(
                    (right - LINE_LENGTH).toFloat(), top.toFloat(), right.toFloat(), top.toFloat(),
                    paint
                )
                canvas.drawLine(
                    right.toFloat(), top.toFloat(), right.toFloat(), (top + LINE_LENGTH).toFloat(),
                    paint
                )
                canvas.drawLine(
                    right.toFloat(),
                    (bottom - LINE_LENGTH).toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    paint
                )
                canvas.drawLine(
                    right.toFloat(),
                    bottom.toFloat(),
                    (right - LINE_LENGTH).toFloat(),
                    bottom.toFloat(),
                    paint
                )
                canvas.drawLine(
                    (left + LINE_LENGTH).toFloat(),
                    bottom.toFloat(),
                    left.toFloat(),
                    bottom.toFloat(),
                    paint
                )
                canvas.drawLine(
                    left.toFloat(),
                    bottom.toFloat(),
                    left.toFloat(),
                    (bottom - LINE_LENGTH).toFloat(),
                    paint
                )
            }
        })

        photographer = PhotographerFactory.createPhotographerWithCamera2(currActivity, preview)
        photographerHelper = PhotographerHelper(photographer)
        photographerHelper!!.setFileDir(Commons.MEDIA_DIR)
        photographer!!.setOnEventListener(object : SimpleOnEventListener() {
            override fun onDeviceConfigured() {
                if (photographer!!.mode === Values.MODE_VIDEO) {
//                    actionButton.setImageResource(R.drawable.record)
//                    chooseSizeButton.setText(R.string.video_size)
//                    switchButton.setText(R.string.video_mode)
                } else {
//                    actionButton.setImageResource(R.drawable.ic_camera)
//                    chooseSizeButton.setText(R.string.image_size)
//                    switchButton.setText(R.string.image_mode)
                }
            }

            override fun onZoomChanged(zoom: Float) {
            }

            override fun onStartRecording() {}

            override fun onFinishRecording(filePath: String?) {
//                announcingNewFile(filePath)
            }

            override fun onShotFinished(filePath: String?) {
                 announcingNewFile(filePath!!)
            }
        })

        photographer!!.startPreview()

        imgFlash?.setOnClickListener({
            if (flashtor == 1) {
                flashtor=0
                imgFlash.setImageDrawable(currActivity!!.resources.getDrawable(R.drawable.flash_off))
            } else {
                flashtor=1
                imgFlash.setImageDrawable(currActivity!!.resources.getDrawable(R.drawable.flash_active))
            }
        })


        imgCamera?.setOnClickListener({

            if(CheckPermission.checkCameraPermission(currActivity!!)){

                if(flashtor==1)
                    photographer?.setFlash(Values.FLASH_TORCH)

                photographer!!.takePicture()
                photographer?.setFlash(currentFlash)


            }else{
                CheckPermission.requestCameraPermission(currActivity!!,123)
            }
        })

        imgGallery?.setOnClickListener({
            if (slot1==1) {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, IMAGE_PICKUP_CODE_1)
            }
            else if (slot2==1)
            {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, IMAGE_PICKUP_CODE_2)
            }
        })

        tvUpload?.setOnClickListener({
            if(slot1==1)
            {
                if (picturePath1.isEmpty())
                {
                   // Log.e("dd",slot1.toString()+" "+picturePath1)
                }
                else{

                    showProgressBar()

                    ongoingViewModel?.let {
                        if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                            var  id = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),id.toString())
                            var  picku = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),"pickup")
                            val mFile = File(picturePath1)
                            val requestFile =RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mFile)
                            var image1 = MultipartBody.Part.createFormData("image", mFile.name, requestFile)

                            it.pickupDropoffListing(id,picku,image1)
                        }
                    }
                }
            }
            else {
                if (picturePath2.isEmpty()) {
                } else {
                    showProgressBar()

                    ongoingViewModel?.let {
                        if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                            var id = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),id.toString())
                            var drop = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),"dropoff")
                            val mFile = File(picturePath2)
                            val requestFile =
                                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mFile)
                            var image1 =
                                MultipartBody.Part.createFormData("image", mFile.name, requestFile)

                            it.pickupDropoffListing(id, drop, image1)
                        }

                    }
                }
            }

        })

        tvClose?.setOnClickListener({
            slot1=0
            slot2=0
            picturePath1=""
            picturePath2=""
            dialogTakepic?.dismiss()
        })


        dialogTakepic?.setCanceledOnTouchOutside(false)
        dialogTakepic?.setCancelable(false)
        dialogTakepic?.show()

    }



    private fun announcingNewFile(filePath: String) {

        imgProductImage1.visibility=View.VISIBLE
        preview.visibility=View.GONE

        if(slot1==1) {

            try {

                var bitmapImage = BitmapFactory.decodeFile(filePath)

                val nh = (bitmapImage.height * (800.0 / bitmapImage.width)).toInt()
                var scaled = Bitmap.createScaledBitmap(bitmapImage, 800, nh, true)

                var hh=bitmapImage.height.toDouble()
                var ww=bitmapImage.width.toDouble()
                var tt:Double=(hh/ww)

                val w = scaled.width
                val h = scaled.height
                val mtx = Matrix()

                if(tt==1.0)
                    mtx.postRotate(90.0F)
                else if (tt>1.4)
                    mtx.postRotate(90.0F)
                else
                    mtx.postRotate(0.0F)

                scaled = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true)

                imgProductImage1.setImageBitmap(scaled)

                var bos = ByteArrayOutputStream()
                scaled.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                val bitmapdata: ByteArray = bos.toByteArray()
                val fos = FileOutputStream(filePath)
                fos.write(bitmapdata)
                fos.flush()
                fos.close()

            } catch (e: Exception) {

                Handler().postDelayed({
                    try {

                        var bitmapImage = BitmapFactory.decodeFile(filePath)

                        val nh = (bitmapImage.height * (900.0 / bitmapImage.width)).toInt()
                        var scaled = Bitmap.createScaledBitmap(bitmapImage, 900, nh, true)

                        val w = scaled.width
                        val h = scaled.height
                        val mtx = Matrix()
                        mtx.postRotate(90.00F)
                        scaled = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true)

                        imgProductImage1.setImageBitmap(scaled)

                        var bos = ByteArrayOutputStream()
                        scaled.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                        val bitmapdata: ByteArray = bos.toByteArray()
                        val fos = FileOutputStream(filePath)
                        fos.write(bitmapdata)
                        fos.flush()
                        fos.close()

                    } catch (e: Exception) {
                        Log.e("exe", e.toString())
                    }
                }, 1000)

            }

            picturePath1=filePath
        }

        else if(slot2==1){
            try {

                var bitmapImage = BitmapFactory.decodeFile(filePath)

                val nh = (bitmapImage.height * (800.0 / bitmapImage.width)).toInt()
                var scaled = Bitmap.createScaledBitmap(bitmapImage, 800, nh, true)

                var hh=bitmapImage.height.toDouble()
                var ww=bitmapImage.width.toDouble()
                var tt:Double=(hh/ww)

                val w = scaled.width
                val h = scaled.height
                val mtx = Matrix()

                if(tt==1.0)
                    mtx.postRotate(90.0F)
                else if (tt>1.4)
                    mtx.postRotate(90.0F)
                else
                    mtx.postRotate(0.0F)

                scaled = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true)

                imgProductImage1.setImageBitmap(scaled)

                var bos = ByteArrayOutputStream()
                scaled.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                val bitmapdata: ByteArray = bos.toByteArray()
                val fos = FileOutputStream(filePath)
                fos.write(bitmapdata)
                fos.flush()
                fos.close()

            } catch (e: Exception) {

                Handler().postDelayed({
                    try {

                        var bitmapImage = BitmapFactory.decodeFile(filePath)

                        val nh = (bitmapImage.height * (900.0 / bitmapImage.width)).toInt()
                        var scaled = Bitmap.createScaledBitmap(bitmapImage, 900, nh, true)

                        val w = scaled.width
                        val h = scaled.height
                        val mtx = Matrix()
                        mtx.postRotate(90.00F)
                        scaled = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true)

                        imgProductImage1.setImageBitmap(scaled)

                        var bos = ByteArrayOutputStream()
                        scaled.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                        val bitmapdata: ByteArray = bos.toByteArray()
                        val fos = FileOutputStream(filePath)
                        fos.write(bitmapdata)
                        fos.flush()
                        fos.close()

                    } catch (e: Exception) {
                        Log.e("exe", e.toString())
                    }
                }, 1000)

            }

            picturePath2=filePath
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICKUP_CODE_1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val resultUri = data.getParcelableExtra<Uri>("path")
                    if (resultUri.toString().contains("jpg") || resultUri.toString()
                            .contains("jpeg")
                    ) {
                        extension = ".jpg"
                    } else if (resultUri.toString().contains("png")) {
                        extension = ".png"
                    }

                    var ff: Uri = data.data!!
                    var picturePath = getRealPathFromURI(ff)

                    var filePath: String = data.data.toString()

                    imagesDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                    ).toString() + File.separator + "Pics"+ File.separator+"img1.jpg"

                    val mFile =
                        File(imagesDir)
                    val fdelete = mFile
                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                        }
                    }

                    val options = BitmapFactory.Options()
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888
                    var bitmapOrg = BitmapFactory.decodeStream(FileInputStream(picturePath), null, options)
                    Handler().postDelayed({
                        val stream = ByteArrayOutputStream()
                        bitmapOrg?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                        val imageInByte: ByteArray = stream.toByteArray()
                        val lengthbmp = imageInByte.size.toLong()/1024

                        var imageUri:Uri?=null
                        if (lengthbmp>(1024))
                        // mSelectedImagePath = getRealPathFromURI(resultUri)
                        {
                            val nh = (bitmapOrg!!.height * (900.0 / bitmapOrg!!.width)).toInt()
                            var scaled = Bitmap.createScaledBitmap(bitmapOrg!!, 900, nh, true)

                            val w = scaled.width
                            val h = scaled.height
                            val mtx = Matrix()
                            mtx.postRotate(0.00F)
                            scaled = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true)

                            var bos = ByteArrayOutputStream()
                            scaled.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                            val bitmapdata: ByteArray = bos.toByteArray()
                            val fos: OutputStream?

                            fos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                val resolver: ContentResolver = applicationContext.getContentResolver()
                                val contentValues = ContentValues()
                                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "img1")
                                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                                contentValues.put(
                                    MediaStore.MediaColumns.RELATIVE_PATH,
                                    "DCIM/Pics"
                                )
                                 imageUri =
                                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                                resolver.openOutputStream(imageUri!!)
                            } else {
                                imagesDir = Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DCIM
                                ).toString() + File.separator + "Pics"
                                val file = File(imagesDir)
                                if (!file.exists()) {
                                    file.mkdir()
                                }
                                val image = File(imagesDir, "img1" + ".jpeg")
                                FileOutputStream(image)
                            }


                            scaled.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                            if (fos != null) {
                                fos.flush()
                            }
                            if (fos != null) {
                                fos.close()
                            }

                            preview.visibility=View.GONE
                            imgProductImage1.visibility=View.VISIBLE

                            if (imageUri==null){
                                picturePath1= imagesDir+"/img1.jpeg"

                                Handler().postDelayed({

                                    Glide.with(currActivity!!).load(imageUri)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true).into(imgProductImage1)

                                },200)
                            }
                            else {
                                picturePath1 = getRealPathFromURI(imageUri)

                                Handler().postDelayed({

                                    Glide.with(currActivity!!).load(imageUri)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true).into(imgProductImage1)

                                },200)

                            }
                        }
                        else if(lengthbmp<1024 && lengthbmp>160)
                        {

                            var filePath:String = data.data.toString()

                            var ff:Uri= data.data!!
                            val picturePath = getRealPathFromURI(ff)

                            Log.e("file",slot1.toString())

                            picturePath1=picturePath

                            preview.visibility=View.GONE
                            imgProductImage1.visibility=View.VISIBLE

                            Glide.with(currActivity!!).load(filePath)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true).into(imgProductImage1)

                        }
                        else if (lengthbmp<160){
                            AppUtils.showToast(currActivity!!,R.drawable.cross,"Select image upto 150 kb",R.color.error_red,R.color.white,R.color.white)

                        }
                    }, 1000)
                }

            }
        }  else if (requestCode == IMAGE_PICKUP_CODE_2) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val resultUri = data.getParcelableExtra<Uri>("path")
                    if (resultUri.toString().contains("jpg") || resultUri.toString()
                            .contains("jpeg")
                    ) {
                        extension = ".jpg"
                    } else if (resultUri.toString().contains("png")) {
                        extension = ".png"
                    }
                    //mSelectedImagePath = getRealPathFromURI(resultUri)
                    var filePath:String = data.data.toString()


                    var ff:Uri= data.data!!
                    val picturePath = getRealPathFromURI(ff)

                    imagesDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                    ).toString() + File.separator + "Pics"+ File.separator+"img2.jpg"

                    val mFile =
                        File(imagesDir)
                    val fdelete = mFile
                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                        }
                    }

                    val options = BitmapFactory.Options()
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888
                    var bitmapOrg = BitmapFactory.decodeStream(FileInputStream(picturePath), null, options)
                    Handler().postDelayed({
                        val stream = ByteArrayOutputStream()
                        bitmapOrg?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                        val imageInByte: ByteArray = stream.toByteArray()
                        val lengthbmp = imageInByte.size.toLong()/1024

                        var imageUri :Uri?=null
                        if (lengthbmp>(1024))
                        // mSelectedImagePath = getRealPathFromURI(resultUri)
                        {
                            val nh = (bitmapOrg!!.height * (900.0 / bitmapOrg!!.width)).toInt()
                            var scaled = Bitmap.createScaledBitmap(bitmapOrg!!, 900, nh, true)

                            val w = scaled.width
                            val h = scaled.height
                            val mtx = Matrix()
                            mtx.postRotate(0.00F)
                            scaled = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true)

                            //imgFirstImage.setImageBitmap(scaled)

                            var bos = ByteArrayOutputStream()
                            scaled.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                            val bitmapdata: ByteArray = bos.toByteArray()
                            val fos: OutputStream?

                            fos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                val resolver: ContentResolver = applicationContext.getContentResolver()
                                val contentValues = ContentValues()
                                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "img2")
                                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                                contentValues.put(
                                    MediaStore.MediaColumns.RELATIVE_PATH,
                                    "DCIM/Pics"
                                )
                                 imageUri =
                                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                                resolver.openOutputStream(imageUri!!)
                            } else {
                                imagesDir = Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DCIM
                                ).toString() + File.separator + "Pics"
                                val file = File(imagesDir)
                                if (!file.exists()) {
                                    file.mkdir()
                                }
                                val image = File(imagesDir, "img2" + ".jpeg")
                                FileOutputStream(image)
                            }


                            scaled.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                            if (fos != null) {
                                fos.flush()
                            }
                            if (fos != null) {
                                fos.close()
                            }

                            preview.visibility=View.GONE
                            imgProductImage1.visibility=View.VISIBLE

                            if (imageUri==null){
                                picturePath2= imagesDir+"/img2.jpeg"

                                Handler().postDelayed({

                                    Glide.with(currActivity!!).load(picturePath2)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true).into(imgProductImage1)

                                },200)
                            }
                            else {
                                picturePath2 = getRealPathFromURI(imageUri)

                                Handler().postDelayed({

                                    Glide.with(currActivity!!).load(imageUri)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true).into(imgProductImage1)

                                },200)

                            }

                        }
                        else if(lengthbmp<1024 && lengthbmp>160)
                        {

                            var filePath:String = data.data.toString()

                            var ff:Uri= data.data!!
                            val picturePath = getRealPathFromURI(ff)

                            Log.e("file",picturePath)

                            picturePath2=picturePath

                            preview.visibility=View.GONE
                            imgProductImage1.visibility=View.VISIBLE

                            Glide.with(currActivity!!).load(filePath)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true).into(imgProductImage1)
                        }
                        else if (lengthbmp<160){
                            AppUtils.showToast(currActivity!!,R.drawable.cross,"Select image upto 150 kb",R.color.error_red,R.color.white,R.color.white)

                        }
                    }, 1000)

                }

            }

        }


    }




    private fun openSuccessPopup(){

        slideView.visibility = View.GONE

        dialogReferral = Dialog(this)

        listingPopupBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.listing_success_popup, null, true
        )

        dialogReferral?.setContentView(listingPopupBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialogReferral?.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)

        dialogReferral?.getWindow()!!.getAttributes().windowAnimations = R.style.animationdialog;

        listingPopupBinding?.tvLabel?.text = this.resources.getString(R.string.listing_completed_successfully)

        listingPopupBinding?.rlMain?.setOnClickListener{
            dialogReferral?.dismiss()
        }
        dialogReferral?.setCancelable(true)
        dialogReferral?.show()

    }


    private fun openBottomPopup(){

        bottomSheetApplicationDialog = BottomSheetDialog(currActivity!!)
        var bottomSheetDashboardFilterBinding = DataBindingUtil.inflate<BottomSheetListingOngoingStatePaymentDetailBinding>(
            LayoutInflater.from(currActivity),
            R.layout.bottom_sheet_listing_ongoing_state_payment_detail, null, false)

        bottomSheetApplicationDialog?.setContentView(bottomSheetDashboardFilterBinding!!.root)
        Objects.requireNonNull<Window>(bottomSheetApplicationDialog?.window)
            .setBackgroundDrawableResource(android.R.color.transparent)

        var btnCompleteTransaction=bottomSheetApplicationDialog!!.findViewById<TextView>(R.id.btnCompleteTransaction)
        var tvPrice=bottomSheetApplicationDialog!!.findViewById<TextView>(R.id.tvPrice)
        var tvAmount=bottomSheetApplicationDialog!!.findViewById<TextView>(R.id.tvAmount)
        var tvLivoFee=bottomSheetApplicationDialog!!.findViewById<TextView>(R.id.tvLivoFee)
        var tvTotalAmount=bottomSheetApplicationDialog!!.findViewById<TextView>(R.id.tvTotalAmount)


        var tax=extraDataModel.data.sender_commission
        var a = amount
        var b = a * (tax.toDouble() / 100)
        tvLivoFee?.text = String.format("%.2f", b) + " kr"
        tvAmount?.text = String.format("%.2f", a) + " kr"
        tvTotalAmount?.text = String.format("%.2f", a+b)+" kr"
        tvPrice?.text=String.format("%.2f", a)+" KR"

        btnCompleteTransaction?.setOnClickListener({

            showProgressBar()
            ongoingViewModel?.let {
                if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {
                    var jsonObject =  JsonObject();
                    jsonObject.addProperty("offer_id", id)
                    it.getMakePayment(jsonObject)
                }
            }

        })

        bottomSheetApplicationDialog?.show()

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

        tvAdjustPrice?.text=resources.getString(R.string.cancel)
        tvSendoffer?.text=resources.getString(R.string.update_offer)

        tvLivofeetitle?.text=resources.getString(R.string.livo_fee)+" ("+transportfee+"%)"

        etAmount?.setText(value)
        var a=(etAmount?.text.toString()).toDouble()
        var b=a*(transportfee.toDouble()/100)
        tvLivofee?.text="-"+String.format("%.2f", b)+" kr"
        tvTotalAmount?.text=String.format("%.2f", a-b)+" kr"

        llPriceTag?.setOnClickListener({

                etAmount?.isEnabled=true
                llPriceTag?.setBackgroundDrawable(resources.getDrawable(R.drawable.grey_round_shape_45_opacity))

                etAmount?.setBackgroundColor(resources.getColor(R.color.transparant))
                etAmount?.requestFocus()

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

                if (s.isEmpty()){

                    try {
                        tvLivofee?.text = String.format("%.2f", 0) + " kr"
                        tvTotalAmount?.text = String.format("%.2f", 0) + " kr"
                    }
                    catch (ex:Exception){
                        etAmount?.setText(" ")
                    }
                }

                else{
                    try {
                        var a = (s.toString()).toDouble()
                        var b = a * (transportfee.toDouble() / 100)
                        tvLivofee?.text = "-" + String.format("%.2f", b) + " kr"
                        tvTotalAmount?.text = String.format("%.2f", a - b).toString() + " kr"
                    }
                    catch (e:Exception){
                        tvLivofee?.text=0.toString()+" kr"
                        tvTotalAmount?.text=0.toString()+" kr"
                    }
                }
            }
        })

        llSendoffer?.setOnClickListener({

            showProgressBar()
            ongoingViewModel?.let {
                if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                    var jsonObject =  JsonObject()
                    jsonObject.addProperty("offer_id", id)
                    jsonObject.addProperty("offer_price", etAmount?.text.toString())

                    it.getChangeBidPrice(jsonObject)
                }
            }

        })

        tvAdjustPrice?.setOnClickListener({
            val imm: InputMethodManager =
                currActivity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            bottomSheetSendOffer?.dismiss()
        })

        bottomSheetSendOffer?.show()
    }


    fun showProgressBar(){
        dialog =  AppUtils.showProgress(this)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog)
    }

    fun getRealPathFromURI(uri: Uri?): String {
        var path = ""
        if (contentResolver != null) {
            val cursor =
                contentResolver.query(uri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    protected override fun setStatusBar() {

        val mColor = resources.getColor(R.color.colorPrimary)
        StatusBarUtil.setLightMode(this)

    }

    private fun viewAllStates(){
        ongoingViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                var jsonObject =  JsonObject();
                jsonObject.addProperty("offer_id", id)

                it.getViewAllState(jsonObject)
            }
        }
    }


    private fun showPopup() {
        dialogReferral = Dialog(currActivity!!)
        listingPopupBinding = DataBindingUtil.inflate(
            LayoutInflater.from(currActivity),
            R.layout.listing_success_popup, null, true
        )

        dialogReferral?.setContentView(listingPopupBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialogReferral?.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)
        //dialogReferral?.getWindow()!!.getAttributes().windowAnimations = R.style.animationdialog;

        val window: Window? = dialogReferral?.window
        window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        var tvLabel=dialogReferral?.findViewById<TextView>(R.id.tvLabel)
        tvLabel?.text=resources.getString(R.string.listing_completed_successfully)

        Handler().postDelayed({
            if(is_transpoter.equals("true"))
                dialogReferral?.dismiss()
            else{
                dialogReferral?.dismiss()
                finish()
            }
            },2000)

        dialogReferral?.setCancelable(true)
        dialogReferral?.show()
    }


    private fun showPopupImageView(value:String) {
        dialogPopImage = Dialog(currActivity!!)
        popUpPickupDropoffImageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(currActivity),
            R.layout.pop_up_pickup_dropoff_image, null, true
        )

        dialogPopImage?.setContentView(popUpPickupDropoffImageBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialogPopImage?.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)
        //dialogReferral?.getWindow()!!.getAttributes().windowAnimations = R.style.animationdialog;

        val window: Window? = dialogPopImage?.window
        window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        var tvName=dialogPopImage?.findViewById<TextView>(R.id.tvName)
        var tvDetail=dialogPopImage?.findViewById<TextView>(R.id.tvDetail)
        var imgProductImage=dialogPopImage?.findViewById<ImageView>(R.id.imgProductImage)
        var shimmerImage=dialogPopImage?.findViewById<ShimmerFrameLayout>(R.id.shimmerImage)

        if (value.equals("1"))
        {
            tvName?.text=resources.getString(R.string.listing_picked_up)
            tvDetail?.text=pickupdate

            if (imgProductImage != null) {
                Glide.with(currActivity).addDefaultRequestListener(object : RequestListener<Any> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Any>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        shimmerImage?.visibility = View.VISIBLE
                        imgProductImage.visibility = View.GONE
                        shimmerImage?.startShimmer()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Any?,
                        model: Any?,
                        target: Target<Any>?,
                        dataSource:DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        shimmerImage?.visibility = View.GONE
                        imgProductImage.visibility = View.VISIBLE
                        shimmerImage?.stopShimmer()
                        return false
                    }

                })
                    .load(pickupimage_url).placeholder(currActivity.getDrawable(R.drawable.grey_round_shape))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(currActivity.getDrawable(R.drawable.grey_round_shape)).into(imgProductImage)
            }
        }
        else{
            tvName?.text=resources.getString(R.string.listing_delivered)
            tvDetail?.text=dropoffdate

            if (imgProductImage != null) {


                Glide.with(currActivity).addDefaultRequestListener(object : RequestListener<Any> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Any>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        shimmerImage?.visibility = View.VISIBLE
                        imgProductImage.visibility = View.GONE
                        shimmerImage?.startShimmer()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Any?,
                        model: Any?,
                        target: Target<Any>?,
                        dataSource:DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        shimmerImage?.visibility = View.GONE
                        imgProductImage.visibility = View.VISIBLE
                        shimmerImage?.stopShimmer()
                        return false
                    }

                })
                    .load(dropoffimage_url).placeholder(currActivity.getDrawable(R.drawable.grey_round_shape))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(currActivity.getDrawable(R.drawable.grey_round_shape)).into(imgProductImage)


            }
        }

        dialogPopImage?.setCancelable(true)
        dialogPopImage?.show()
    }



    fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "Livo",
                customer = customerConfig,
                // Set `allowsDelayedPaymentMethods` to true if your business
                // can handle payment methods that complete payment after a delay, like SEPA Debit and Sofort.
                allowsDelayedPaymentMethods = true
            )
        )
    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        // implemented in the next steps

        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Log.e("msg","cancel")
            }
            is PaymentSheetResult.Failed -> {
                Log.e("Error:", "${paymentSheetResult.error}")
            }
            is PaymentSheetResult.Completed -> {

                showProgressBar()
                ongoingViewModel?.let {
                    if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                        var jsonObject =  JsonObject();
                        jsonObject.addProperty("offer_id", id)

                        it.getChangePaymentStatus(jsonObject)
                    }
                }

                bottomSheetApplicationDialog?.dismiss()

               // viewAllStates()

            }
        }
    }




}