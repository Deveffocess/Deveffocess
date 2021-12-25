package com.livo.nuo.view.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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
import com.livo.nuo.databinding.BottomSheetOtpCodeProfileBinding
import com.livo.nuo.databinding.BottomSheetlanguagecodeBinding
import com.livo.nuo.lib.roundImageView.RoundedImageView
import com.livo.nuo.manager.SessionManager
import com.livo.nuo.utility.AndroidUtil
import com.livo.nuo.utility.AppUtils
import com.livo.nuo.utility.LocalizeActivity
import com.livo.nuo.view.Splash_Screen
import com.livo.nuo.view.home.HomeActivity.Companion.fa
import com.livo.nuo.viewModel.ViewModelFactory
import com.livo.nuo.viewModel.profile.ProfileViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.w3c.dom.Text
import java.io.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ProfileSettingActivity : LocalizeActivity() {

    private var currActivity : Activity? = this

    lateinit var tvFemale : TextView
    lateinit var tvMale : TextView
    lateinit var tvOther : TextView
    lateinit var tvBusiness : TextView
    lateinit var tvIndividual : TextView
    lateinit var imgBack : ImageView
    lateinit var imgUser:ImageView
    lateinit var etFirstName:EditText
    lateinit var etLastName:EditText
    lateinit var etEmail:EditText
    lateinit var etDate:EditText
    lateinit var tvDialCode:TextView
    lateinit var etPhone:EditText
    lateinit var tvSubmit:TextView
    lateinit var tvImage:ImageView
    lateinit var tvChange:TextView
    lateinit var tvTimer:TextView
    lateinit var shimmerViewContainer:ShimmerFrameLayout
    lateinit var rlMainLayout:RelativeLayout


    var firstname=""
    var lastname=""
    var email=""
    var dob=""
    var phone=""
    var maingender=""
    var usertype=""
    var dialcode=""
    lateinit var imagesDir:String
    var extension=""
    var imagepath=""
    var otpcode=""
    var getotpcode=""
    var timeout=""
    var timerc=0
    var calandercounter=0


    lateinit var etNum1:EditText
    lateinit var etNum2:EditText
    lateinit var etNum3:EditText
    lateinit var etNum4:EditText

    lateinit var tvShimmerImage:ShimmerFrameLayout
    var bottomsheetlanguagecode : BottomSheetDialog? = null
    private var profileViewModel : ProfileViewModel? = null
    private lateinit var dialog: Dialog

    lateinit var myCalendar:Calendar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_setting)

        tvFemale = findViewById(R.id.tvFemale)
        tvMale = findViewById(R.id.tvMale)
        tvOther = findViewById(R.id.tvOther)
        tvBusiness = findViewById(R.id.tvBusiness)
        tvIndividual = findViewById(R.id.tvIndividual)
        imgBack = findViewById(R.id.imgBack)
        imgUser=findViewById(R.id.imgUser)

        etFirstName=findViewById(R.id.etFirstName)
        etLastName=findViewById(R.id.etLastName)
        etEmail=findViewById(R.id.etEmail)
        etDate=findViewById(R.id.etDate)
        tvDialCode=findViewById(R.id.tvDialCode)
        etPhone=findViewById(R.id.etPhone)
        tvSubmit=findViewById(R.id.tvSubmit)
        tvImage=findViewById(R.id.tvImage)
        tvChange=findViewById(R.id.tvChange)

        shimmerViewContainer=findViewById(R.id.shimmerViewContainer)
        rlMainLayout=findViewById(R.id.rlMainLayout)

        tvShimmerImage=findViewById(R.id.tvShimmerImage)

        rlMainLayout.visibility=View.GONE
        shimmerViewContainer.visibility=View.VISIBLE
        shimmerViewContainer.startShimmer()


        initViews()
    }

    fun initViews(){
        currActivity?.application?.let {
            profileViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(ProfileViewModel::class.java)
        }


        profileViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {
                it.getViewOwnProfile()
            }
        }



        myCalendar = Calendar.getInstance()
        var date =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth


                updateLabel()
            }



        tvFemale.setOnClickListener({
            maingender="Female"
            tvFemale.background = applicationContext.resources.getDrawable(R.drawable.black_round_shape)
            tvFemale.setTextColor(applicationContext.resources.getColor(R.color.white))

            tvMale.background =applicationContext.resources.getDrawable(R.drawable.white_fill_black_10_border)
            tvMale.setTextColor(applicationContext.resources.getColor(R.color.livo_black_45_opacity))

            tvOther.background =applicationContext.resources.getDrawable(R.drawable.white_fill_black_10_border)
            tvOther.setTextColor(applicationContext.resources.getColor(R.color.livo_black_45_opacity))

        })

        tvMale.setOnClickListener({

            maingender="Male"
            tvMale.background = applicationContext.resources.getDrawable(R.drawable.black_round_shape)
            tvMale.setTextColor(applicationContext.resources.getColor(R.color.white))

            tvFemale.background =applicationContext.resources.getDrawable(R.drawable.white_fill_black_10_border)
            tvFemale.setTextColor(applicationContext.resources.getColor(R.color.livo_black_45_opacity))

            tvOther.background =applicationContext.resources.getDrawable(R.drawable.white_fill_black_10_border)
            tvOther.setTextColor(applicationContext.resources.getColor(R.color.livo_black_45_opacity))


        })
        tvOther.setOnClickListener({

            maingender="Other"
            tvOther.background = applicationContext.resources.getDrawable(R.drawable.black_round_shape)
            tvOther.setTextColor(applicationContext.resources.getColor(R.color.white))

            tvFemale.background =applicationContext.resources.getDrawable(R.drawable.white_fill_black_10_border)
            tvFemale.setTextColor(applicationContext.resources.getColor(R.color.livo_black_45_opacity))

            tvMale.background =applicationContext.resources.getDrawable(R.drawable.white_fill_black_10_border)
            tvMale.setTextColor(applicationContext.resources.getColor(R.color.livo_black_45_opacity))

        })

        tvBusiness.setOnClickListener({
            usertype="Company"
            tvBusiness.background = applicationContext.resources.getDrawable(R.drawable.black_round_shape)
            tvBusiness.setTextColor(applicationContext.resources.getColor(R.color.white))

            tvIndividual.background =applicationContext.resources.getDrawable(R.drawable.white_fill_black_10_border)
            tvIndividual.setTextColor(applicationContext.resources.getColor(R.color.livo_black_45_opacity))

        })

        tvIndividual.setOnClickListener({
            usertype="Individual"
            tvIndividual.background =applicationContext.resources.getDrawable(R.drawable.black_round_shape)
            tvIndividual.setTextColor(applicationContext.resources.getColor(R.color.white))

            tvBusiness.background = applicationContext.resources.getDrawable(R.drawable.white_fill_black_10_border)
            tvBusiness.setTextColor(applicationContext.resources.getColor(R.color.livo_black_45_opacity))
        })

        tvSubmit.setOnClickListener({

            firstname=etFirstName.text.toString()
            lastname=etLastName.text.toString()
            email=etEmail.text.toString()

            Log.e("da",maingender+" "+usertype+" "+imagepath+" "+dob+" "+email)

            showProgressBar()

            profileViewModel?.let {
                if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                    var  fname = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),firstname)
                    var  lname = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),lastname)
                    var  nemail = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),email)
                    var  ndob = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),dob)
                    var  ngender = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),maingender)
                    var  nusertype = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),usertype)

                    var image1: MultipartBody.Part? =null
                    if (imagepath.isEmpty())
                    {
                        image1 = MultipartBody.Part.createFormData("profile_image", "")
                    }
                    else{
                        val mFile = File(imagepath)
                        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mFile)
                        image1 = MultipartBody.Part.createFormData("profile_image", mFile.name, requestFile)
                    }

                    it.setEditOwnProfile(fname,lname,nemail,ndob,ngender,nusertype,image1)
                }
            }

        })

        tvChange.setOnClickListener({

            phone=etPhone.text.toString()
            showProgressBar()
            profileViewModel?.let {
                if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {
                    var jsonObject =  JsonObject();
                    jsonObject.addProperty("country_code", dialcode)
                    jsonObject.addProperty("phone_number", phone)
                    it.getchangeNumSendOtp(jsonObject)
                }
            }

          //  openBottomPopup()
        })

        imgBack.setOnClickListener({
            onBackPressed()
        })

        imgUser.setOnClickListener({
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(pickPhoto, 1)
        })

        etDate.setOnClickListener({
            var dat:DatePickerDialog
            if (calandercounter==0) {
                myCalendar.add(Calendar.YEAR, -10) //Goes 10 Year Back in time ^^
            calandercounter=1
            }

            dat=DatePickerDialog(
                currActivity!!, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )

            var upperLimit = myCalendar.timeInMillis
            dat.datePicker.setMaxDate(upperLimit)
            dat.show()
        })


        observers()
    }

    private fun updateLabel() {
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        dob=sdf.format(myCalendar.getTime())

        val myFormat1 = "dd/MM/yyyy" //In which you need put here
        val sdf1 = SimpleDateFormat(myFormat1, Locale.US)
        etDate.setText(sdf1.format(myCalendar.getTime()))
    }


    private fun observers() {

        profileViewModel?.getMutableLiveDataViewOwnProfile()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {

                rlMainLayout.visibility=View.VISIBLE
                shimmerViewContainer.visibility=View.GONE
                shimmerViewContainer.stopShimmer()

                var dat=it.data
                etFirstName.setText(dat.first_name)
                etLastName.setText(dat.last_name)
                etEmail.setText(dat.email)
                etDate.setText(dat.dob)
                etPhone.setText(dat.phone_number)
                tvDialCode.text=dat.country_code

                firstname=dat.first_name
                lastname=dat.last_name
                email=dat.email
                dob=dat.dob
                phone=dat.phone_number
                dialcode=dat.country_code

                val inputFormat = SimpleDateFormat("yyyy-MM-dd")
                val outputFormat = SimpleDateFormat("dd/MM/yyyy")

                var date: Date? = null
                var str: String? = null

                try {
                    date = inputFormat.parse(dat.dob)
                    str = outputFormat.format(date)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                etDate.setText(str)


                Glide.with(currActivity!!).addDefaultRequestListener(object : RequestListener<Any> {
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
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        tvShimmerImage.visibility = View.GONE
                        imgUser.visibility = View.VISIBLE
                        tvShimmerImage.stopShimmer()
                        return false
                    }
                })
                    .load(it.data.profile_image).placeholder(currActivity!!.getDrawable(R.drawable.grey_round_shape))
                    .error(currActivity!!.getDrawable(R.drawable.grey_round_shape)).into(imgUser)

                var user_type=dat.user_type
                if (user_type.equals("Individual"))
                {
                    usertype="Individual"
                    tvIndividual.background =applicationContext.resources.getDrawable(R.drawable.black_round_shape)
                    tvIndividual.setTextColor(applicationContext.resources.getColor(R.color.white))

                    tvBusiness.background = applicationContext.resources.getDrawable(R.drawable.white_fill_black_10_border)
                    tvBusiness.setTextColor(applicationContext.resources.getColor(R.color.livo_black_45_opacity))
                }
                else{
                    usertype="Company"
                    tvBusiness.background = applicationContext.resources.getDrawable(R.drawable.black_round_shape)
                    tvBusiness.setTextColor(applicationContext.resources.getColor(R.color.white))

                    tvIndividual.background =applicationContext.resources.getDrawable(R.drawable.white_fill_black_10_border)
                    tvIndividual.setTextColor(applicationContext.resources.getColor(R.color.livo_black_45_opacity))
                }

                var gender=dat.gender

                if (gender.equals("Male")){
                    maingender="Male"
                    tvMale.background = applicationContext.resources.getDrawable(R.drawable.black_round_shape)
                    tvMale.setTextColor(applicationContext.resources.getColor(R.color.white))

                    tvFemale.background =applicationContext.resources.getDrawable(R.drawable.white_fill_black_10_border)
                    tvFemale.setTextColor(applicationContext.resources.getColor(R.color.livo_black_45_opacity))

                    tvOther.background =applicationContext.resources.getDrawable(R.drawable.white_fill_black_10_border)
                    tvOther.setTextColor(applicationContext.resources.getColor(R.color.livo_black_45_opacity))
                }
                else if(gender.equals("Female")){
                    maingender="Female"
                    tvFemale.background = applicationContext.resources.getDrawable(R.drawable.black_round_shape)
                    tvFemale.setTextColor(applicationContext.resources.getColor(R.color.white))

                    tvMale.background =applicationContext.resources.getDrawable(R.drawable.white_fill_black_10_border)
                    tvMale.setTextColor(applicationContext.resources.getColor(R.color.livo_black_45_opacity))

                    tvOther.background =applicationContext.resources.getDrawable(R.drawable.white_fill_black_10_border)
                    tvOther.setTextColor(applicationContext.resources.getColor(R.color.livo_black_45_opacity))
                }
                else{
                    maingender="Other"
                    tvOther.background = applicationContext.resources.getDrawable(R.drawable.black_round_shape)
                    tvOther.setTextColor(applicationContext.resources.getColor(R.color.white))

                    tvFemale.background =applicationContext.resources.getDrawable(R.drawable.white_fill_black_10_border)
                    tvFemale.setTextColor(applicationContext.resources.getColor(R.color.livo_black_45_opacity))

                    tvMale.background =applicationContext.resources.getDrawable(R.drawable.white_fill_black_10_border)
                    tvMale.setTextColor(applicationContext.resources.getColor(R.color.livo_black_45_opacity))
                }


            })

        profileViewModel?.getMutableLiveDataViewChangeNumSendOtp()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
               hideProgressBar()
                timeout=""
                getotpcode=it.data.otp
                timeout=it.data.timeout

                var countDownInterval:Long = 1000
                var millisInFuture: Long = (1000 * timeout.toLong())
                timer(millisInFuture, countDownInterval).cancel()
                timer(millisInFuture, countDownInterval).start()

                openBottomPopup()
            })

        profileViewModel?.getMutableLiveDataViewChangeNumChangeNumber()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
               hideProgressBar()

                SessionManager.clear()
                var i=Intent(currActivity, Splash_Screen::class.java)
                startActivity(i)
                finish()
                fa.finish()

            })

        profileViewModel?.getMutableLiveDataViewEditOwnProfile()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
               hideProgressBar()
                finish()
              // AppUtils.showToast(currActivity!!,R.drawable.arrow_right,it.message,R.color.livo_green,R.color.white,R.color.white)

            })

        profileViewModel?.getErrorMutableLiveData()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
            hideProgressBar()
            AppUtils.showToast(currActivity!!,R.drawable.cross,it.message,R.color.error_red,R.color.white,R.color.white)
        })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

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
                    ).toString() + File.separator + "Pics"+File.separator+"img1.jpg"

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
                            val imageUri =
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

                            tvShimmerImage.visibility = View.GONE
                            imgUser.visibility = View.VISIBLE
                            tvShimmerImage.stopShimmer()

                            Glide.with(currActivity!!).load(imagesDir+"/img1.jpeg")
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true).into(imgUser)

                            FileOutputStream(image)
                        }


                        scaled.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                        if (fos != null) {
                            fos.flush()
                        }
                        if (fos != null) {
                            fos.close()
                        }


                        imagepath= imagesDir+"/img1.jpeg"

                    }
                        else if(lengthbmp<1024 && lengthbmp>160)
                    {

                        var filePath:String = data.data.toString()

                        var ff:Uri= data.data!!
                        val picturePath = getRealPathFromURI(ff)

                        Log.e("file",picturePath)

                        imagepath=picturePath

                        tvShimmerImage.visibility = View.GONE
                        imgUser.visibility = View.VISIBLE
                        tvShimmerImage.stopShimmer()

                        Glide.with(currActivity!!).load(filePath)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).into(imgUser)

                        }
                    else if (lengthbmp<160){
                        AppUtils.showToast(currActivity!!,R.drawable.cross,"Select image upto 150 kb",R.color.error_red,R.color.white,R.color.white)

                    }
                }, 1000)
                }

            }
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


    private fun openBottomPopup(){

        try{
        bottomsheetlanguagecode?.dismiss()
        }
        catch (e:Exception){}

        bottomsheetlanguagecode = BottomSheetDialog(currActivity!!)
        var bottomSheetLanguageDialogBinding =
            DataBindingUtil.inflate<BottomSheetOtpCodeProfileBinding>(
                LayoutInflater.from(currActivity),
                R.layout.bottom_sheet_otp_code_profile,null,false
            )

        bottomsheetlanguagecode?.setContentView(bottomSheetLanguageDialogBinding!!.root)
        Objects.requireNonNull<Window>(bottomsheetlanguagecode?.window)
            .setBackgroundDrawableResource(android.R.color.transparent)

        etNum1= bottomsheetlanguagecode!!.findViewById(R.id.etNum1)!!
        etNum2= bottomsheetlanguagecode!!.findViewById(R.id.etNum2)!!
        etNum3= bottomsheetlanguagecode!!.findViewById(R.id.etNum3)!!
        etNum4= bottomsheetlanguagecode!!.findViewById(R.id.etNum4)!!
        tvTimer=bottomsheetlanguagecode!!.findViewById(R.id.tvTimer)!!



        etNum1.addTextChangedListener(GenericTextWatcher(etNum1, etNum2))
        etNum2.addTextChangedListener(GenericTextWatcher(etNum2, etNum3))
        etNum3.addTextChangedListener(GenericTextWatcher(etNum3, etNum4))
        etNum4.addTextChangedListener(GenericTextWatcher(etNum4, null))

//GenericKeyEvent here works for deleting the element and to switch back to previous EditText
//first parameter is the current EditText and second parameter is previous EditText
        etNum1.setOnKeyListener(GenericKeyEvent(etNum1, null))
        etNum2.setOnKeyListener(GenericKeyEvent(etNum2, etNum1))
        etNum3.setOnKeyListener(GenericKeyEvent(etNum3, etNum2))
        etNum4.setOnKeyListener(GenericKeyEvent(etNum4,etNum3))

        var llUpdateNumber=bottomsheetlanguagecode!!.findViewById<LinearLayout>(R.id.llUpdateNumber)
        var llCancel=bottomsheetlanguagecode!!.findViewById<LinearLayout>(R.id.llCancel)

        llUpdateNumber?.setOnClickListener({
            otpcode=etNum1.text.toString()+etNum2.text.toString()+etNum3.text.toString()+etNum4.text.toString()
            if (otpcode.length==4)
            {
                    showProgressBar()
                    profileViewModel?.let {
                        if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {
                            var jsonObject =  JsonObject()
                            jsonObject.addProperty("country_code", dialcode)
                            jsonObject.addProperty("phone_number", phone)
                            jsonObject.addProperty("otp", otpcode)
                            it.getChangeNumChangeNumber(jsonObject)
                        }
                    }

            }
        })

        tvTimer?.setOnClickListener({
            timeout=""
            phone=etPhone.text.toString()
            showProgressBar()
            profileViewModel?.let {
                if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {
                    var jsonObject =  JsonObject()
                    jsonObject.addProperty("country_code", dialcode)
                    jsonObject.addProperty("phone_number", phone)
                    it.getchangeNumSendOtp(jsonObject)
                }
            }
        })

        llCancel?.setOnClickListener({
            bottomsheetlanguagecode!!.dismiss()
        })



        bottomsheetlanguagecode?.show()
        bottomsheetlanguagecode?.setCancelable(false)

    }



    class GenericKeyEvent internal constructor(private val currentView: EditText, private val previousView: EditText?) : View.OnKeyListener{
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if(event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.etNum1 && currentView.text.isEmpty()) {
                //If current is empty then previous EditText's number will also be deleted
                previousView!!.text = null
                previousView.requestFocus()
                return true
            }
            return false
        }


    }

    class GenericTextWatcher internal constructor(private val currentView: View, private val nextView: View?) :
        TextWatcher {
        override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
            val text = editable.toString()
            when (currentView.id) {
                R.id.etNum1 -> if (text.length == 1) {
                     Handler().postDelayed({
                         nextView!!.requestFocus()
                         },200)
                    }
                R.id.etNum2 -> if (text.length == 1) {
                    Handler().postDelayed({
                        nextView!!.requestFocus()
                    },200)
                }
                R.id.etNum3 -> if (text.length == 1) {
                    Handler().postDelayed({
                        nextView!!.requestFocus()
                    },200)
                }
                //You can use EditText4 same as above to hide the keyboard
            }
        }

        override fun beforeTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) { // TODO Auto-generated method stub
        }

        override fun onTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) { // TODO Auto-generated method stub
        }

    }


    fun showProgressBar(){
        dialog =  AppUtils.showProgress(this)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog)
    }


    private fun timer(millisInFuture:Long,countDownInterval:Long): CountDownTimer {
        return object: CountDownTimer(millisInFuture,countDownInterval){
            override fun onTick(millisUntilFinished: Long){
                //val timeRemaining = timeString(millisUntilFinished)
                tvTimer.visibility=View.VISIBLE
                tvTimer.isEnabled=false
                tvTimer.text=applicationContext.resources.getString(R.string.resend_otp_in) + millisUntilFinished/1000+" "+ applicationContext.resources.getString(R.string.seconds)
            }
            override fun onFinish() {
                //rlotp.visibility=View.GONE
                if (timerc==0) {
                    tvTimer.isEnabled=true
                    tvTimer.text=applicationContext.resources.getString(R.string.resend_otp)
                    tvTimer.visibility = View.VISIBLE
                    etPhone.isEnabled=true
                }
            }
        }
    }


    override fun setStatusBar() {
        val mColor = resources.getColor(R.color.white)
        StatusBarUtil.setColor(this, mColor,40)
    }


}