package com.livo.nuo.view.listing

import android.R.attr
import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.jaeger.library.StatusBarUtil
import com.livo.nuo.R
import com.livo.nuo.commonadapter.AdapterCommonViewPager
import com.livo.nuo.models.RegExp
import com.livo.nuo.netUtils.services.RequestHandler
import com.livo.nuo.utility.LocalizeActivity
import com.livo.nuo.utility.MyAppSession
import com.livo.nuo.view.listing.fragments.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject

import org.json.JSONArray
import android.graphics.BitmapFactory
import com.livo.nuo.lib.customcamera.Utils
import android.R.attr.bitmap
import android.os.*
import android.provider.MediaStore.Images

import android.R.attr.bitmap
import android.content.*
import android.graphics.Matrix

import android.os.Environment

import android.os.Build
import com.livo.nuo.utility.AppUtils
import java.io.*


class NewListingActivity : LocalizeActivity() {



    private var currActivity : Activity = this
    lateinit var viewPagerSubscription:ViewPager
    lateinit var imageFragment : Fragment
    lateinit var dropOffFragment : Fragment
    lateinit var publishFragment:Fragment
    
    lateinit var rlCenterLayout:RelativeLayout
    lateinit var rlCenterLayoutImage:RelativeLayout
    lateinit var rlCenterLayoutPickup:RelativeLayout
    lateinit var rlCenterLayoutDropOff:RelativeLayout
    lateinit var rlCenterLayoutPublish:RelativeLayout
    lateinit var tvNext:TextView
    lateinit var tvProductDetailImage:TextView
    lateinit var tvProductDetailPickup:TextView
    lateinit var tvProductDetailDropOff:TextView
    lateinit var tvProductDetailPublish:TextView

    var productTitle = ""
    var height = ""
    var depth = ""
    var width = ""
    var weight = ""
    var isTwoPeople = false

    lateinit var imagesDir:String

    var selectedDate = ""
    var pickupAddress = ""
    var addressNote = ""
    var mAddress: String? = ""
    var userState: String? = ""
    var userCity: String? = ""
    var userPostalCode: String? = ""
    var userCountry: String? = ""
    var userAddressline2: String? = ""
    val userAddressline1 = ""
    var addressBundle: Bundle? = null
    val addressdetails: List<*>? = null
    var mLatitude = 0.0
    var mLongitude = 0.0


    var dropSelectedDate = ""
    var dropAddress = ""
    var dropAddressNote = ""
    var dropmAddress: String? = ""
    var dropuserState: String? = ""
    var dropuserCity: String? = ""
    var dropuserPostalCode: String? = ""
    var dropuserCountry: String? = ""
    var dropuserAddressline2: String? = ""
    val dropuserAddressline1 = ""
    var dropaddressBundle: Bundle? = null
    val dropaddressdetails: List<*>? = null
    var dropmLatitude = 0.0
    var dropmLongitude = 0.0
    var routeDistance: String?=""

    lateinit var pref:SharedPreferences

    var slot1 = ""
    var slot2 = ""
    var slot3  = ""
    var forSlot = ""
    var slotNo = 0
    var from =0

    var extension: String = ""
    var mSelectedImagePath: String? = null

    var IMAGE_PICKER_SELECT_SLOT1 = 22
    var IMAGE_PICKER_SELECT_SLOT2 = 23
    var IMAGE_PICKER_SELECT_SLOT3 = 24


    lateinit var rlMain:LinearLayout
    lateinit var imgBack:ImageView
    lateinit var imgCross:ImageView


    var selectedImageModelPaths = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_listing)

        viewPagerSubscription=findViewById(R.id.viewPagerSubscription)

        rlCenterLayout=findViewById(R.id.rlCenterLayout)
        rlCenterLayoutImage=findViewById(R.id.rlCenterLayoutImage)
        rlCenterLayoutPickup=findViewById(R.id.rlCenterLayoutPickup)
        rlCenterLayoutDropOff=findViewById(R.id.rlCenterLayoutDropOff)
        rlCenterLayoutPublish=findViewById(R.id.rlCenterLayoutPublish)
        tvNext=findViewById(R.id.tvNext)
        tvProductDetailImage=findViewById(R.id.tvProductDetailImage)
        tvProductDetailPickup=findViewById(R.id.tvProductDetailPickup)
        tvProductDetailDropOff=findViewById(R.id.tvProductDetailDropOff)
        tvProductDetailPublish=findViewById(R.id.tvProductDetailPublish)
        imgCross=findViewById(R.id.imgCross)

        imgBack=findViewById(R.id.imgBack)

        initViews()

    }

    fun initViews(){

        imgBack.setOnClickListener({
            if(viewPagerSubscription.currentItem == 0){

                onBackPressed()
            }else if(viewPagerSubscription.currentItem == 1){

                viewPagerSubscription.setCurrentItem(0,true)

            }else if(viewPagerSubscription.currentItem == 2){
                viewPagerSubscription.setCurrentItem(1,true)

            }else if(viewPagerSubscription.currentItem == 3){
                viewPagerSubscription.setCurrentItem(2,true)

            }else if(viewPagerSubscription.currentItem == 4){
                viewPagerSubscription.setCurrentItem(3,true)

            }
        })

        tvNext.setOnClickListener({


            if(viewPagerSubscription.currentItem == 0) {
                if (validationInfo()) {
                    MyAppSession.productTitle = productTitle
                    MyAppSession.weight = weight
                    MyAppSession.width = width
                    MyAppSession.height = height
                    MyAppSession.depth = depth
                    MyAppSession.isTwoPeople = isTwoPeople
                }


                (publishFragment as PublishFragment).getTotal()

            }
            else if(viewPagerSubscription.currentItem == 1) {
                if (selectedImageModelPaths.size > 0) {
                    uploadImages()
                }
            }
            else if(viewPagerSubscription.currentItem == 2){
                if(validationPickupAddress()){
                    MyAppSession.pickup_fullAddress = mAddress!!
                    MyAppSession.pickup_lat = mLatitude.toString()
                    MyAppSession.pickUp_long = mLongitude.toString()
                    MyAppSession.pickupDate = selectedDate
                    MyAppSession.pickup_addressNote = addressNote

                }

            }
            else if(viewPagerSubscription.currentItem == 3){
                if(validationDropOff()){
                    MyAppSession.dropoff_fullAddress = dropmAddress!!
                    MyAppSession.dropoff_lat = dropmLatitude.toString()
                    MyAppSession.dropoff_long = dropmLongitude.toString()
                    MyAppSession.dropoffDate = dropSelectedDate
                    MyAppSession.dropoff_addressNote = dropAddressNote
                }

                getDistance()
                Log.e("latlan",mLatitude.toString()+","+mLongitude.toString()+" "+dropmLatitude.toString()+","+dropmLongitude.toString())
            }



            viewPagerSubscription.setCurrentItem(getItem(+1), true) //getItem(-1) for previous

        })
        imgCross.setOnClickListener({
            onBackPressed()
        })

        val adapter = AdapterCommonViewPager(currActivity,supportFragmentManager,
            FragmentStatePagerAdapter.POSITION_NONE)
        adapter.addFragment(AddInfoFragment.newInstance(),"")
        tvNext.isEnabled = false
        tvNext.background =currActivity.resources.getDrawable(R.drawable.grey_round_shape_more_less_corners)

        viewPagerSubscription.offscreenPageLimit = 5
        viewPagerSubscription.adapter = adapter

        Handler(Looper.getMainLooper()).postDelayed({
            setUpViewPager()
        }, 1000)


        viewPagerSubscription?.setOnTouchListener(object :View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                for (PAGE in 0..viewPagerSubscription.adapter!!.count){
                    if (viewPagerSubscription.currentItem==PAGE){
                        viewPagerSubscription.setCurrentItem(PAGE-1,false)
                        viewPagerSubscription.setCurrentItem(PAGE,false)
                    }}
                return true
            }

        })
    }

    private fun getItem(i: Int): Int {
        return viewPagerSubscription.getCurrentItem() + i
    }


    override fun onResume() {

        super.onResume()
    }

    fun setUpViewPager() {
        val adapter = AdapterCommonViewPager(currActivity,supportFragmentManager,
            FragmentStatePagerAdapter.POSITION_NONE)
        imageFragment = AddImagesFragment.newInstance()
        dropOffFragment = DropOffFragment.newInstance()
        publishFragment = PublishFragment.newInstance()
        adapter.addFragment(AddInfoFragment.newInstance(),"")
        adapter.addFragment(imageFragment,"")
        adapter.addFragment(PickupFragment.newInstance(),"")
        adapter.addFragment(dropOffFragment,"")
        adapter.addFragment(publishFragment,"")


        viewPagerSubscription.offscreenPageLimit = 5
        viewPagerSubscription.adapter = adapter

//        tabLayoutSubscription.setupWithViewPager(viewPagerSubscription)

//        for(i in 0 until tabLayoutSubscription.tabCount){
//            tabLayoutSubscription.getTabAt(i)!!.customView = getCustomView(i)
//            tabLayoutSubscription.getTabAt(0)!!.customView!!.rlMain.backgroundTintList =
//                currActivity!!.resources.getColorStateList(R.color.yellow)
//        }
//        viewPagerSubscription.beginFakeDrag()


        viewPagerSubscription.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
//                TabLayout.TabLayoutOnPageChangeListener(tabLayoutSubscription)

                if (position == 0) {
                    rlCenterLayout.visibility = View.VISIBLE
                    rlCenterLayoutImage.visibility = View.GONE
                    rlCenterLayoutPickup.visibility = View.GONE
                    rlCenterLayoutDropOff.visibility = View.GONE
                    rlCenterLayoutPublish.visibility = View.GONE
                    tvNext.visibility = View.VISIBLE
                    if(!productTitle.isEmpty())
                        showButtonNext()

                } else if (position == 1) {

                    rlCenterLayout.visibility = View.GONE
                    rlCenterLayoutImage.visibility = View.VISIBLE
                    rlCenterLayoutPickup.visibility = View.GONE
                    rlCenterLayoutDropOff.visibility = View.GONE
                    rlCenterLayoutPublish.visibility = View.GONE

                    /*val animation = AnimationUtils.loadAnimation(currActivity, R.anim.partial_slide)
                    tvProductDetailImage.startAnimation(animation)*/

                    if (selectedImageModelPaths.size>0)
                        showButtonNext()
                    else
                        tvNext.visibility = View.GONE

                } else if (position == 2) {
                    rlCenterLayout.visibility = View.GONE
                    rlCenterLayoutImage.visibility = View.GONE
                    rlCenterLayoutPickup.visibility = View.VISIBLE
                    rlCenterLayoutDropOff.visibility = View.GONE
                    rlCenterLayoutPublish.visibility = View.GONE

                    /*val animation = AnimationUtils.loadAnimation(currActivity, R.anim.partial_slide_pickup)
                    tvProductDetailPickup.startAnimation(animation)*/
                    tvNext.visibility = View.VISIBLE
                    pref = currActivity!!.getSharedPreferences("PickUp", Context.MODE_PRIVATE)
                    var dat = pref.getString("date", "")!!
                    if(dat.isEmpty())
                        hideButtonNext()
                    else
                        showButtonNext()

                } else if (position == 3) {
                    rlCenterLayout.visibility = View.GONE
                    rlCenterLayoutImage.visibility = View.GONE
                    rlCenterLayoutPickup.visibility = View.GONE
                    rlCenterLayoutDropOff.visibility = View.VISIBLE
                    rlCenterLayoutPublish.visibility = View.GONE

                   /* val animation = AnimationUtils.loadAnimation(currActivity, R.anim.partial_slide_dropoff)
                    tvProductDetailDropOff.startAnimation(animation)*/
                    tvNext.visibility = View.VISIBLE

                    pref = currActivity!!.getSharedPreferences("DropOff", Context.MODE_PRIVATE)
                    var dat = pref.getString("date", "")!!
                    if(dat.isEmpty())
                        hideButtonNext()
                    else
                        showButtonNext()

                } else if (position == 4) {
                    rlCenterLayout.visibility = View.GONE
                    rlCenterLayoutImage.visibility = View.GONE
                    rlCenterLayoutPickup.visibility = View.GONE
                    rlCenterLayoutDropOff.visibility = View.GONE
                    rlCenterLayoutPublish.visibility = View.VISIBLE
                   /* val animation = AnimationUtils.loadAnimation(currActivity, R.anim.partial_slide)
                    tvProductDetailPublish.startAnimation(animation)*/
                    tvNext.visibility = View.INVISIBLE
                    publishFragment.onResume()

                }

            }

            override fun onPageSelected(position: Int) {
                if(position == 0){

                    if(validationInfo()){
                        tvNext.isEnabled = true
                        tvNext.background = currActivity.resources.getDrawable(R.drawable.black_round_shape_more_less_corners)
                    }else{
                        tvNext.isEnabled = false
                        tvNext.background = currActivity.resources.getDrawable(R.drawable.grey_round_shape_more_less_corners)
                    }

                }
                /*else if( position == 1){
                    if(selectedImageModelPaths.size > 0){
                        tvNext.isEnabled = true
                        tvNext.background = currActivity.resources.getDrawable(R.drawable.black_round_shape_more_less_corners)
                    }else{
                        tvNext.isEnabled = false
                        tvNext.background = currActivity.resources.getDrawable(R.drawable.grey_round_shape_more_less_corners)
                    }
                }*/

            }
        })

    }


    fun validationInfo() : Boolean{

        var isValid = true

        if(RegExp.checkEmpty(productTitle)){
            isValid = false
        }
        if(RegExp._isValidName(productTitle)){
            isValid = false
        }
        if(RegExp.checkEmpty(height)){
            isValid = false
        }
        if(RegExp.checkEmpty(depth)){
            isValid = false
        }
        if(RegExp.checkEmpty(weight)){
            isValid = false
        }
        if(RegExp.checkEmpty(width)){
            isValid = false
        }
        if(height.equals("0")){
            isValid = false
        }
        if(depth.equals("0")){
            isValid = false
        }
        if(weight.equals("0")){
            isValid = false
        }
        if(width.equals("0")){
            isValid = false
        }

        return isValid
    }

    private fun validationPickupAddress() : Boolean{
        var isvalid = true
        if(RegExp.checkEmpty(selectedDate)){
            isvalid = false
        }

        if(RegExp.checkEmpty(addressNote)){
            isvalid = false
        }

        if(RegExp.checkEmpty(pickupAddress)){
            isvalid = false
        }

        return  isvalid
    }


    private fun validationDropOff() : Boolean{
        var isvalid = true
        if(RegExp.checkEmpty(dropSelectedDate)){
            isvalid = false
        }

        if(RegExp.checkEmpty(dropAddressNote)){
            isvalid = false
        }

        if(RegExp.checkEmpty(dropAddress)){
            isvalid = false
        }

        return  isvalid
    }

    fun getImage(i:Int){
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(pickPhoto, i)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICKER_SELECT_SLOT1) {
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
                                resolver.insert(Images.Media.EXTERNAL_CONTENT_URI, contentValues)
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

                       // Toast.makeText(applicationContext, lengthbmp.toString()+" "+(bitmapdata.size.toLong()/1024).toString(), Toast.LENGTH_SHORT).show()
                        (imageFragment as AddImagesFragment).galleryClick(0,imagesDir+"/img1.jpeg")
                        Log.e("file",imagesDir+"/img1.jpeg")
                        selectedImageModelPaths.add(0, imagesDir+"/img1.jpeg")
                        tvNext.visibility = View.VISIBLE

                    }
                        else if(lengthbmp<1024 && lengthbmp>160)
                    {

                        var filePath:String = data.data.toString()
                        (imageFragment as AddImagesFragment).galleryClick(0,filePath)

                        var ff:Uri= data.data!!
                        val picturePath = getRealPathFromURI(ff)

                        Log.e("file",picturePath)

                        selectedImageModelPaths.add(0,picturePath)
                        tvNext.visibility=View.VISIBLE

                        }
                    else if (lengthbmp<160){
                        AppUtils.showToast(currActivity!!,R.drawable.cross,"Select image upto 150 kb",R.color.error_red,R.color.white,R.color.white)

                    }
                }, 1000)
                    showButtonNext()
                }

            }
        }  else if (requestCode == IMAGE_PICKER_SELECT_SLOT2) {
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
                    ).toString() + File.separator + "Pics"+File.separator+"img2.jpg"

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
                                val imageUri =
                                    resolver.insert(Images.Media.EXTERNAL_CONTENT_URI, contentValues)
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

                           // Toast.makeText(applicationContext, lengthbmp.toString()+" "+(bitmapdata.size.toLong()/1024).toString(), Toast.LENGTH_SHORT).show()
                            (imageFragment as AddImagesFragment).galleryClick(1,imagesDir+"/img2.jpeg")
                            Log.e("file",imagesDir+"/img2.jpeg")
                            selectedImageModelPaths.add(1, imagesDir+"/img2.jpeg")
                            tvNext.visibility = View.VISIBLE

                        }
                        else if(lengthbmp<1024 && lengthbmp>160)
                        {

                            var filePath:String = data.data.toString()
                            (imageFragment as AddImagesFragment).galleryClick(1,filePath)

                            var ff:Uri= data.data!!
                            val picturePath = getRealPathFromURI(ff)

                            Log.e("file",picturePath)

                            selectedImageModelPaths.add(1,picturePath)
                            tvNext.visibility=View.VISIBLE

                        }
                        else if (lengthbmp<160){
                            AppUtils.showToast(currActivity!!,R.drawable.cross,"Select image upto 150 kb",R.color.error_red,R.color.white,R.color.white)

                        }
                    }, 1000)

                    showButtonNext()
                }

            }
        } else if (requestCode == IMAGE_PICKER_SELECT_SLOT3) {
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
                   // mSelectedImagePath = getRealPathFromURI(resultUri)
                    var filePath:String = data.data.toString()
                   // (imageFragment as AddImagesFragment).galleryClick(2,filePath)

                    tvNext.visibility = View.VISIBLE

                    var ff:Uri= data.data!!
                    val picturePath = getRealPathFromURI(ff)

                    imagesDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                    ).toString() + File.separator + "Pics"+File.separator+"img3.jpg"

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

                            //imgFirstImage.setImageBitmap(scaled)

                            var bos = ByteArrayOutputStream()
                            scaled.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                            val bitmapdata: ByteArray = bos.toByteArray()
                            val fos: OutputStream?

                            fos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                val resolver: ContentResolver = applicationContext.getContentResolver()
                                val contentValues = ContentValues()
                                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "img3")
                                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                                contentValues.put(
                                    MediaStore.MediaColumns.RELATIVE_PATH,
                                    "DCIM/Pics"
                                )
                                val imageUri =
                                    resolver.insert(Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                                resolver.openOutputStream(imageUri!!)
                            } else {
                                imagesDir = Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DCIM
                                ).toString() + File.separator + "Pics"
                                val file = File(imagesDir)
                                if (!file.exists()) {
                                    file.mkdir()
                                }
                                val image = File(imagesDir, "img3" + ".jpeg")
                                FileOutputStream(image)
                            }


                            scaled.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                            if (fos != null) {
                                fos.flush()
                            }
                            if (fos != null) {
                                fos.close()
                            }

                           // Toast.makeText(applicationContext, lengthbmp.toString()+" "+(bitmapdata.size.toLong()/1024).toString(), Toast.LENGTH_SHORT).show()
                            (imageFragment as AddImagesFragment).galleryClick(2,imagesDir+"/img3.jpeg")
                            Log.e("file",imagesDir+"/img3.jpeg")
                            selectedImageModelPaths.add(2, imagesDir+"/img3.jpeg")
                            tvNext.visibility = View.VISIBLE

                        }
                        else if(lengthbmp<1024 && lengthbmp>160)
                        {

                            var filePath:String = data.data.toString()
                            (imageFragment as AddImagesFragment).galleryClick(2,filePath)

                            var ff:Uri= data.data!!
                            val picturePath = getRealPathFromURI(ff)

                            Log.e("file",picturePath)

                            selectedImageModelPaths.add(2,picturePath)
                            tvNext.visibility=View.VISIBLE

                        }
                        else if (lengthbmp<160){
                            AppUtils.showToast(currActivity!!,R.drawable.cross,"Select image upto 150 kb",R.color.error_red,R.color.white,R.color.white)
                        }
                    }, 1000)
                    showButtonNext()
                }

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



    /* fun showNext() {
         if (viewPagerSubscription.currentItem == 1) {
             if (selectedImageModelPaths.size > 0) {
                 tvNext.isEnabled = true
                 tvNext.background =
                     currActivity.resources.getDrawable(R.drawable.black_round_shape_more_less_corners)
             } else {
                 tvNext.isEnabled = false
                 tvNext.background =
                     currActivity.resources.getDrawable(R.drawable.grey_round_shape_more_less_corners)
             }
         }
     }*/

    fun showButtonNext(){
        tvNext.isEnabled = true
        tvNext.background =currActivity.resources.getDrawable(R.drawable.black_round_shape_more_less_corners)
    }

    fun hideButtonNext(){
        tvNext.isEnabled = false
        tvNext.background =
            currActivity.resources.getDrawable(R.drawable.grey_round_shape_more_less_corners)
    }


    fun uploadImages(){

        val parts: Array<MultipartBody.Part?> =
            arrayOfNulls<MultipartBody.Part>(selectedImageModelPaths.size)
        for (i in 0 until selectedImageModelPaths.size) {
            if(selectedImageModelPaths[i] != ""){
//                val bitmap =
//                        MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.parse(selectedImageModelPaths[i]))

//                selectedImagePath = getRealPathFromURI(getImageUri(this, bitmap))
//                selectedImagePath = AppUtils.compressImage(selectedImagePath, this)
//                    mSelectedImagePath = resultUri!.toString()

                val mFile = File(Uri.parse(selectedImageModelPaths[i]).path)
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mFile)
                parts[i] =
                    MultipartBody.Part.createFormData("image[]", mFile.name, requestFile)
            }

            Log.e("io",parts[i].toString())
        }

        /*productViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                showProgressBar()
                it.uploadPhotoArray(parts)
            }
        }*/

    }


    fun getDistance() {


        var UPLOAD_URL="https://maps.googleapis.com/maps/api/distancematrix/json?origins=$mLatitude,$mLongitude&destinations=$dropmLatitude,$dropmLongitude&key="+resources.getString(R.string.api_key)

        Log.e("url",UPLOAD_URL)

        class UploadImage : AsyncTask<String, Void, String>() {
            var rh: RequestHandler = RequestHandler()

            override fun onPreExecute() {
                super.onPreExecute()
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)

                val jsonObject = JSONObject(result)
                val array: JSONArray = jsonObject.getJSONArray("rows")

                val routes = array.getJSONObject(0)
                val legs = routes.getJSONArray("elements")
                val steps = legs.getJSONObject(0)
                val distance = steps.getJSONObject("distance")
                var tance=distance.getString("text")
                var a=tance.split(" ")
                routeDistance=a[0]
                Log.e("res", routeDistance.toString())

            }

            override fun doInBackground(vararg params: String?): String {

                var bitmap: String? = params[0]
                var data = HashMap<String, String>()
                var result = rh.sendPostRequest(UPLOAD_URL, data)
                return result
            }
        }

        var ui = UploadImage()
        ui.execute(String())
    }


     override fun setStatusBar() {

        val mColor = resources.getColor(R.color.colorPrimary)
        StatusBarUtil.setLightMode(currActivity)

    }
}