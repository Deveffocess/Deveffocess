package com.livo.nuo.view.listing.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.facebook.shimmer.Shimmer
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.livo.nuo.R
import com.livo.nuo.databinding.ListingDataUploadingPopupBinding
import com.livo.nuo.databinding.ListingSuccessPopupBinding
import com.livo.nuo.lib.loadingloader.DotProgressBar
import com.livo.nuo.lib.slidetoact.SlideToActView
import com.livo.nuo.manager.SessionManager
import com.livo.nuo.models.ExtraDataModel
import com.livo.nuo.models.ExtraDataRecommendedPriceModel
import com.livo.nuo.utility.AndroidUtil
import com.livo.nuo.utility.AppUtils
import com.livo.nuo.utility.MyAppSession
import com.livo.nuo.view.home.HomeActivity
import com.livo.nuo.view.listing.NewListingActivity
import com.livo.nuo.viewModel.ViewModelFactory
import com.livo.nuo.viewModel.products.ProductViewModel
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*





class PublishFragment : Fragment() {

    lateinit var llEnterPrice:LinearLayout
    lateinit var etEnterPrice:EditText
    lateinit var tvLivofee:TextView
    lateinit var tvLivefeevalue:TextView
    lateinit var tvLivoamountvalue:TextView
    lateinit var tvAmountPayable:TextView
    lateinit var tvAmt:TextView

    private var currActivity : Activity? = null
    lateinit var slideView: SlideToActView

    var size1=""
    var size2=""
    var size3=""
    var size4=""
    var size5=""
    var size6=""

    var price1=""
    var price2=""
    var price3=""
    var price4=""
    var price5=""
    var price6=""

    var extraDataModel=ExtraDataModel()
    private var productViewModel : ProductViewModel? = null
    var priceArray=ArrayList<ExtraDataRecommendedPriceModel>()

    var transportfee:String=""
    var final_tot=0.0

    lateinit var pref:SharedPreferences

    private var listingPopupBinding: ListingSuccessPopupBinding? = null
    private  var dialogReferral: Dialog? = null
    private var dialog: Dialog? = null
    private var listingUploadPopupBinding: ListingDataUploadingPopupBinding? = null
    private  var dialogUpload: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root= inflater.inflate(R.layout.fragment_publish, container, false)

        llEnterPrice=root.findViewById(R.id.llEnterPrice)
        etEnterPrice=root.findViewById(R.id.etEnterPrice)
        slideView=root.findViewById(R.id.slideView)
        tvLivofee=root.findViewById(R.id.tvLivofee)
        tvLivefeevalue=root.findViewById(R.id.tvLivefeevalue)
        tvLivoamountvalue=root.findViewById(R.id.tvLivoamountvalue)
        tvAmountPayable=root.findViewById(R.id.tvAmountPayable)
        tvAmt=root.findViewById(R.id.tvAmt)

        initViews()

        return root
    }

    companion object {
        fun newInstance() : Fragment{
            val f = PublishFragment()
            return f
        }
    }

    fun initViews(){

        currActivity = requireActivity()


        currActivity?.application?.let {
            productViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(ProductViewModel::class.java)
        }


        extraDataModel= SessionManager.getExtraDataModel()!!
        transportfee=extraDataModel.data.sender_commission

        priceArray=(extraDataModel.data.recommended_price)

        tvLivofee.text=resources.getString(R.string.livo_fee)+ " ("+transportfee+"%)"

        var a=0.0

        etEnterPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) { }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.equals("")) {
                    etEnterPrice.setText("")
                    tvLivefeevalue.text=String.format("%.2f", 0)+" kr"
                    tvAmountPayable.text=(0).toString()+" kr"
                    tvLivoamountvalue.text=s.toString()+" kr"
                }
                else {
                  try {
                      a = (s.toString()).toDouble()
                      var b = a * (transportfee.toDouble() / 100)
                      tvLivefeevalue.text = String.format("%.2f", b) + " kr"
                      tvAmountPayable.text = (a + b).toString() + " kr"
                      tvLivoamountvalue.text = s.toString() + " kr"
                      final_tot=a+b
                  }
                  catch (e:Exception){
                      tvLivefeevalue.text=0.toString()+" kr"
                      tvAmountPayable.text=0.toString()+" kr"
                      tvLivoamountvalue.text=0.toString()+" kr"
                  }
                }
            }
        })

        priceArray.sortBy { it.size }

        size1=priceArray[0].size
        size2=priceArray[1].size
        size3=priceArray[2].size
        size4=priceArray[3].size
        size5=priceArray[4].size
        size6=priceArray[5].size

        price1=priceArray[0].price
        price2=priceArray[1].price
        price3=priceArray[2].price
        price4=priceArray[3].price
        price5=priceArray[4].price
        price6=priceArray[5].price

        Log.e("pri",size1+" "+size2+" "+size3+" "+size4+" "+size5+" "+size6+","+price1+" "+price2+" "+price3+" "+price4+" "+price5+" "+price6)

        llEnterPrice.setOnClickListener({
            etEnterPrice.requestFocus()
            val imm: InputMethodManager =
                currActivity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        })



        slideView.onSlideToActAnimationEventListener = object : SlideToActView.OnSlideToActAnimationEventListener{
            override fun onSlideCompleteAnimationStarted(view: SlideToActView, threshold: Float) {
                slideView.innerColor=resources.getColor(R.color.livo_green)
            }

            override fun onSlideCompleteAnimationEnded(view: SlideToActView) {

                slideView.resetSlider()
                slideView.innerColor=resources.getColor(R.color.black)
                slideView.visibility = View.GONE

                 showUploadPopup()

                 Handler().postDelayed({

                     var  rTitle = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),(currActivity as NewListingActivity).productTitle)

                     var  rHeight = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),(currActivity as NewListingActivity).height)
                     var  rweight = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),(currActivity as NewListingActivity).weight)
                     var  rwidth = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),(currActivity as NewListingActivity).width)
                     var  rdepth = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),(currActivity as NewListingActivity).depth)
                     var  rprice = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),final_tot.toString())
                     var  rmorePeople = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),(currActivity as NewListingActivity).isTwoPeople.toString())

                     pref = currActivity!!.getSharedPreferences("PickUp", Context.MODE_PRIVATE)
                     var dat = pref.getString("date", "")!!

                     var  rpickup_date = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),dat)

                     pref = currActivity!!.getSharedPreferences("DropOff", Context.MODE_PRIVATE)
                     var dat1 = pref.getString("date", "")!!

                     var  rdropoff_date = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),dat1)

                     var mLongitude=String.format("%.7f", (currActivity as NewListingActivity).mLongitude)
                     var mLatitude=String.format("%.7f",(currActivity as NewListingActivity).mLatitude)

                     var  rmLongitude = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),mLongitude)
                     var  rmLatitude = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),mLatitude)
                     var  rpickupAddress = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),(currActivity as NewListingActivity).userCity.toString()+","+(currActivity as NewListingActivity).userState.toString())
                     var  raddressNote = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),(currActivity as NewListingActivity).addressNote)

                     var dropmLongitude=String.format("%.7f", (currActivity as NewListingActivity).dropmLongitude)
                     var dropmLatitude=String.format("%.7f",(currActivity as NewListingActivity).dropmLatitude)

                     var  rdropmLongitude = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),dropmLongitude)
                     var  rdropmLatitude = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),dropmLatitude)
                     var  rdropAddress = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),(currActivity as NewListingActivity).dropuserCity.toString()+","+(currActivity as NewListingActivity).dropuserState.toString())
                     var  rdropAddressNote = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),(currActivity as NewListingActivity).dropAddressNote)

                     var  rrouteDistance = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),(currActivity as NewListingActivity).routeDistance.toString())

                     var image1: MultipartBody.Part? =null
                     var image2: MultipartBody.Part? =null
                     var image3: MultipartBody.Part? =null
                     val parts: Array<MultipartBody.Part?> =
                         arrayOfNulls<MultipartBody.Part>((currActivity as NewListingActivity).selectedImageModelPaths.size)
                     for (i in 0 until (currActivity as NewListingActivity).selectedImageModelPaths.size) {
                         if((currActivity as NewListingActivity).selectedImageModelPaths[i] != ""){

                             val mFile = File(Uri.parse((currActivity as NewListingActivity).selectedImageModelPaths[i]).path)
                             val requestFile =RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mFile)
                             parts[i] =MultipartBody.Part.createFormData("images[$i]", mFile.name, requestFile)

                             if (i==0)
                                 image1 = MultipartBody.Part.createFormData("images[0]", mFile.name, requestFile)
                             if (i==1)
                                 image2 = MultipartBody.Part.createFormData("images[1]", mFile.name, requestFile)
                             if (i==2)
                                 image3 = MultipartBody.Part.createFormData("images[2]", mFile.name, requestFile)

                             Log.e("pf",image1.toString()+" "+image2.toString()+ mFile.name )
                         }
                     }

                     if (image1 ==null)
                         image1 = MultipartBody.Part.createFormData("images[0]", "")
                     if (image2 ==null)
                         image2 = MultipartBody.Part.createFormData("images[1]", "")
                     if (image3==null)
                         image3 = MultipartBody.Part.createFormData("images[2]", "")


                     Log.e("data","title:"+(currActivity as NewListingActivity).productTitle+" Height:"+(currActivity as NewListingActivity).height+" weight:"+
                             (currActivity as NewListingActivity).weight+" width:"+(currActivity as NewListingActivity).width+" depth"+(currActivity as NewListingActivity).depth+" price:"
                     +etEnterPrice.text.toString()+" more_people_needed"+(currActivity as NewListingActivity).isTwoPeople.toString()+" pickup_date:"+dat+" dropoff_date:"+dat1+" pickup_latitude:"+(currActivity as NewListingActivity).mLatitude.toString()+" pickup_longitude:"+(currActivity as NewListingActivity).mLongitude.toString()+" pickup_address:"+
                             (currActivity as NewListingActivity).userCity.toString()+","+(currActivity as NewListingActivity).userState.toString()+" pickup_address_note:"+(currActivity as NewListingActivity).addressNote+" dropoff_latitude:"+(currActivity as NewListingActivity).dropmLatitude.toString()+" dropoff_longitude:"+(currActivity as NewListingActivity).dropmLongitude.toString()+" dropoff_address:"+
                             (currActivity as NewListingActivity).dropuserCity.toString()+","+(currActivity as NewListingActivity).dropuserState.toString()+" dropoff_address_note:"+(currActivity as NewListingActivity).dropAddressNote+" distance:"+(currActivity as NewListingActivity).routeDistance.toString())

                     productViewModel?.let {
                         if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                             it.createListing(rTitle,rHeight,rwidth,rdepth,rweight,rprice,rmorePeople,rpickup_date,rdropoff_date,
                                 rmLongitude,rmLatitude,rpickupAddress,raddressNote,rdropmLongitude,rdropmLatitude, rdropAddress,
                                 rdropAddressNote ,rrouteDistance,image1,image2,image3)

                         }
                     }
                   /*  val fdelete = File(file_dj_path)
                     if (fdelete.exists()) {
                         if (fdelete.delete()) {
                             System.out.println("file Deleted :$file_dj_path")
                         } else {
                             System.out.println("file not Deleted :$file_dj_path")
                         }
                     }*/


                 }, 100)

            }

            override fun onSlideResetAnimationStarted(view: SlideToActView) {
            }

            override fun onSlideResetAnimationEnded(view: SlideToActView) {
            }
        }



        observers()

    }


    override fun onResume() {
        slideView.visibility = View.VISIBLE

        super.onResume()
    }


    private fun observers() {

        productViewModel?.getMutableLiveDataCreateListing()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
                Log.e("result",it.message)

                dialogUpload?.dismiss()
                showPopup()
            })

        productViewModel?.getErrorMutableLiveData()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
            //hideProgressBar()
            dialogUpload?.dismiss()
            AppUtils.showToast(currActivity!!,R.drawable.cross,it.message+it.code,R.color.error_red,R.color.white,R.color.white)
        })
    }


    fun getTotal(){
        var width=(currActivity as NewListingActivity).width
        var height=(currActivity as NewListingActivity).height
        var depth=(currActivity as NewListingActivity).depth

        var tot:Double=width.toDouble()+height.toDouble()+depth.toDouble()


        if (tot<=size1.toDouble()) {
            tvAmt.text = price1
            etEnterPrice.setText(price1)
        }
        else if (tot>size1.toDouble() && tot<=size2.toDouble()){
            tvAmt.text=price2
            etEnterPrice.setText(price2)
        }
        else if (tot>size2.toDouble() && tot<=size3.toDouble()){
            tvAmt.text=price3
            etEnterPrice.setText(price3)
        }
        else if (tot>size3.toDouble() && tot<=size4.toDouble()){
            etEnterPrice.setText(price4)
            tvAmt.text=price4
        }
        else if (tot>size4.toDouble() && tot<=size5.toDouble()){
            etEnterPrice.setText(price5)
            tvAmt.text=price5
        }
        else if (tot>size5.toDouble() && tot<=size6.toDouble()){
            tvAmt.text=price6
            etEnterPrice.setText(price6)
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

        Handler().postDelayed({

            pref = currActivity!!.getSharedPreferences("PickUp", Context.MODE_PRIVATE)
            val editor1 = pref.edit()
            editor1.clear()
            editor1.apply()

            pref = currActivity!!.getSharedPreferences("DropOff", Context.MODE_PRIVATE)
            val editor2 = pref.edit()
            editor2.clear()
            editor2.apply()

            for (i in 0 until (currActivity as NewListingActivity).selectedImageModelPaths.size) {
                if ((currActivity as NewListingActivity).selectedImageModelPaths[i] != "") {

                    val mFile =
                        File(Uri.parse((currActivity as NewListingActivity).selectedImageModelPaths[i]).path)
                    val fdelete = mFile
                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                        }
                    }
                }
            }

           currActivity!!.finish()

        },5000)

        dialogReferral?.setCancelable(false)
        dialogReferral?.show()
    }


    private fun showUploadPopup() {
        dialogUpload = Dialog(currActivity!!)
        listingUploadPopupBinding = DataBindingUtil.inflate(
            LayoutInflater.from(currActivity),
            R.layout.listing_data_uploading_popup, null, true
        )

        dialogUpload?.setContentView(listingUploadPopupBinding!!.getRoot())
        Objects.requireNonNull<Window>(dialogUpload?.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)
        //dialogUpload?.getWindow()!!.getAttributes().windowAnimations = R.style.animationdialog;

        val window: Window? = dialogUpload?.window
        window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        var frame_layout=dialogUpload!!.findViewById<FrameLayout>(R.id.frame)

        val dotProgressBar = DotProgressBar.Builder()
            .setMargin(7)
            .setAnimationDuration(1800)
            .setMaxScale(2.3f)
            .setMinScale(1.5f)
            .setNumberOfDots(4)
            .setdotRadius(4)
            .build(currActivity!!)
        frame_layout.addView(dotProgressBar)
        dotProgressBar.startAnimation()

        dialogUpload?.setCancelable(false)
        dialogUpload?.show()
    }


}