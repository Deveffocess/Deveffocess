package com.livo.nuo.view.listing.fragments

import android.Manifest
import android.R.attr
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.Camera
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.livo.nuo.R
import com.livo.nuo.databinding.DiscardPopupBinding
import com.livo.nuo.lib.customcamera.*
import com.livo.nuo.lib.customcamera.options.Commons
import com.livo.nuo.lib.roundImageView.RoundedImageView
import com.livo.nuo.utility.CheckPermission
import com.livo.nuo.utility.ImagePickerActivity
import com.livo.nuo.view.listing.NewListingActivity
import java.util.*
import android.graphics.Bitmap
import java.lang.Exception
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.decodeFile
import android.os.Handler
import android.util.Log
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import android.R.attr.angle
import android.media.ExifInterface
import java.util.Collections.rotate












class AddImagesFragment : Fragment() {

    private var currActivity: Activity? = null

    private val currentFlash = Values.FLASH_AUTO

    lateinit var imgGallery:ImageView
    lateinit var imgFirstImage:RoundedImageView
    lateinit var rlImage1:RelativeLayout
    lateinit var rlImage2:RelativeLayout
    lateinit var imgProductImage:ImageView
    lateinit var imgSecondImage:RoundedImageView
    lateinit var rlImage3:RelativeLayout
    lateinit var imgThirdImage: RoundedImageView
    lateinit var imgCamera:ImageView
    lateinit var preview:CameraView
    lateinit var imgFlash:ImageView

    var flashtor=0

    var photographer: Photographer? = null
    var photographerHelper: PhotographerHelper? = null

    private var isFlashAvailable = false;
    private var mCameraManager: CameraManager? = null
    private var mCameraId: String? = null


    private var bottomSheetApplicationDialog: BottomSheetDialog?=null
    private var bottomSheetDiscardPopupBinding: DiscardPopupBinding?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root= inflater.inflate(R.layout.fragment_add_images, container, false)

        imgGallery=root.findViewById(R.id.imgGallery)
        imgFirstImage=root.findViewById(R.id.imgFirstImage)
        rlImage1=root.findViewById(R.id.rlImage1)
        rlImage2=root.findViewById(R.id.rlImage2)
        imgProductImage=root.findViewById(R.id.imgProductImage)
        imgSecondImage=root.findViewById(R.id.imgSecondImage)
        rlImage3=root.findViewById(R.id.rlImage3)
        imgThirdImage=root.findViewById(R.id.imgThirdImage)
        imgCamera=root.findViewById(R.id.imgCamera)
        preview=root.findViewById(R.id.preview)
        imgFlash=root.findViewById(R.id.imgFlash)

        initViews()
        return root
    }

    companion object {
        fun newInstance() : Fragment{
            val f = AddImagesFragment()
            return f
        }
    }


    override fun onResume() {
        super.onResume()
        photographer!!.startPreview()
        enterFullscreen()

    }

    override fun onPause() {
        finishRecordingIfNeeded()
        photographer?.stopPreview()
        super.onPause()
    }

    fun initViews(){

        currActivity = requireActivity()


        isFlashAvailable = currActivity!!.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)

        mCameraManager = currActivity!!.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            mCameraId = mCameraManager?.getCameraIdList()?.get(0)


        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }



        rlImage1.background = currActivity!!.resources.getDrawable(R.drawable.red_image_shape)
        rlImage3.background =
            currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
        rlImage2.background =
            currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)


        imgFlash.setOnClickListener({

            if (flashtor == 1) {
                flashtor=0
                imgFlash.setImageDrawable(currActivity!!.resources.getDrawable(R.drawable.flash_off))
            } else {
                flashtor=1
                imgFlash.setImageDrawable(currActivity!!.resources.getDrawable(R.drawable.flash_active))
            }

        })


        imgGallery.setOnClickListener({

           if((currActivity as NewListingActivity).slot1 == ""){
                (currActivity as NewListingActivity).forSlot = "slot1"
                onProfileImageClick(1,(currActivity as NewListingActivity).IMAGE_PICKER_SELECT_SLOT1)//1 for gallery
            }else if((currActivity as NewListingActivity).slot2 == ""){
                (currActivity as NewListingActivity).forSlot = "slot2"
                onProfileImageClick(1,(currActivity as NewListingActivity).IMAGE_PICKER_SELECT_SLOT2)
            }else if((currActivity as NewListingActivity).slot3 ==""){
                (currActivity as NewListingActivity).forSlot = "slot3"
                onProfileImageClick(1,(currActivity as NewListingActivity).IMAGE_PICKER_SELECT_SLOT3)
            }

        })

            imgCamera.setOnClickListener({

                val flash = photographer?.getFlash()

                if((currActivity as NewListingActivity).slot1 == ""){
                    (currActivity as NewListingActivity).slotNo = 1
                }else if((currActivity as NewListingActivity).slot2 == ""){
                    (currActivity as NewListingActivity).slotNo = 2
                }else if((currActivity as NewListingActivity).slot3 ==""){
                    (currActivity as NewListingActivity).slotNo = 3
                }
                //(currActivity as NewListingActivity).slotNo = 0



                if(CheckPermission.checkCameraPermission(currActivity!!)){

                    if(flashtor==1)
                        photographer?.setFlash(Values.FLASH_TORCH)

                    photographer!!.takePicture()
                    photographer?.setFlash(currentFlash)


                }else{
                    CheckPermission.requestCameraPermission(currActivity!!,123)
                }
            })


            preview.setFocusIndicatorDrawer(object : CanvasDrawer {
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
//                zoomValueTextView.setText(String.format(Locale.getDefault(), "%.1fX", zoom))
                }

                override fun onStartRecording() {
//                switchButton.setVisibility(View.INVISIBLE)
//                flipButton.setVisibility(View.INVISIBLE)
//                actionButton.setEnabled(true)
//                actionButton.setImageResource(R.drawable.stop)
//                statusTextView.setVisibility(View.VISIBLE)
                }

                override fun onFinishRecording(filePath: String?) {
//                announcingNewFile(filePath)
                }

                override fun onShotFinished(filePath: String?) {
                    announcingNewFile(filePath!!)
                }


            })


        rlImage1.setOnClickListener({
            (currActivity as NewListingActivity).forSlot = ""
            (currActivity as NewListingActivity).slotNo = 1
            rlImage1.background =
                currActivity!!.resources.getDrawable(R.drawable.red_image_shape)
            rlImage2.background =
                currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
            rlImage3.background =
                currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)

            imgFirstImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgFirstImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.white))
            imgSecondImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgSecondImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.white))
            imgThirdImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgThirdImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.white))

            imgProductImage.visibility = View.GONE
            preview.visibility = View.VISIBLE
            imgCamera.isEnabled = true
        })

        rlImage2.setOnClickListener({
            (currActivity as NewListingActivity).forSlot = ""
            (currActivity as NewListingActivity).slotNo = 2
            rlImage1.background =
                currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
            rlImage2.background =
                currActivity!!.resources.getDrawable(R.drawable.red_image_shape)
            rlImage3.background =
                currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)

            imgFirstImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgFirstImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.white))
            imgSecondImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgSecondImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.white))
            imgThirdImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgThirdImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.white))

            imgProductImage.visibility = View.GONE
            preview.visibility = View.VISIBLE
            imgCamera.isEnabled = true
        })

        rlImage3.setOnClickListener({
            (currActivity as NewListingActivity).forSlot = ""
            (currActivity as NewListingActivity).slotNo = 3
            rlImage1.background =
                currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
            rlImage2.background =
                currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
            rlImage3.background =
                currActivity!!.resources.getDrawable(R.drawable.red_image_shape)

            imgFirstImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgFirstImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.white))
            imgSecondImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgSecondImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.white))
            imgThirdImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgThirdImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.white))

            imgProductImage.visibility = View.GONE
            preview.visibility = View.VISIBLE
            imgCamera.isEnabled = true
        })

        imgFirstImage.setOnClickListener({
            (currActivity as NewListingActivity).forSlot = ""
            (currActivity as NewListingActivity).slotNo = 1
            imgFirstImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgFirstImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.colorPrimaryDark))
            imgSecondImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgSecondImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.white))
            imgThirdImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgThirdImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.white))

            rlImage1.background =
                currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
            rlImage2.background =
                currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
            rlImage3.background =
                currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)

            var img=(currActivity as NewListingActivity).selectedImageModelPaths[0]
            if (img.isEmpty()){
                imgProductImage.visibility = View.GONE
                preview.visibility = View.VISIBLE
                imgCamera.isEnabled = true
            }
            else {
                imgProductImage.visibility = View.VISIBLE
                Glide.with(currActivity!!).load(img).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(imgProductImage)
                preview.visibility = View.GONE
                imgCamera.isEnabled = false
            }

            openPopupToDiscard((currActivity as NewListingActivity).slotNo)
        })

        imgSecondImage.setOnClickListener({
            (currActivity as NewListingActivity).forSlot = ""
            (currActivity as NewListingActivity).slotNo = 2
            imgFirstImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgFirstImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.white))
            imgSecondImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgSecondImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.colorPrimaryDark))
            imgThirdImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgThirdImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.white))


            rlImage1.background =
                currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
            rlImage2.background =
                currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
            rlImage3.background =
                currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)

            var img=(currActivity as NewListingActivity).selectedImageModelPaths[1]
            if (img.isEmpty()){
                imgProductImage.visibility = View.GONE
                preview.visibility = View.VISIBLE
                imgCamera.isEnabled = true
            }
            else {
                imgProductImage.visibility = View.VISIBLE
                Glide.with(currActivity!!).load(img).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(imgProductImage)
                preview.visibility = View.GONE
                imgCamera.isEnabled = false
            }

            openPopupToDiscard((currActivity as NewListingActivity).slotNo)
        })

        imgThirdImage.setOnClickListener({
            (currActivity as NewListingActivity).forSlot = ""
            (currActivity as NewListingActivity).slotNo = 3
            imgFirstImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgFirstImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.white))
            imgSecondImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgSecondImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.white))
            imgThirdImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
            imgThirdImage.setBorderColor(
                currActivity!!.resources.getColor(R.color.colorPrimaryDark))


            rlImage1.background =
                currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
            rlImage2.background =
                currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
            rlImage3.background =
                currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)

            var img=(currActivity as NewListingActivity).selectedImageModelPaths[2]
            if (img.isEmpty()){
                imgProductImage.visibility = View.GONE
                preview.visibility = View.VISIBLE
                imgCamera.isEnabled = true
            }
            else {
                imgProductImage.visibility = View.VISIBLE
                Glide.with(currActivity!!).load(img).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(imgProductImage)
                preview.visibility = View.GONE
                imgCamera.isEnabled = false
            }

            openPopupToDiscard((currActivity as NewListingActivity).slotNo)
        })

    }


    private fun announcingNewFile(filePath: String) {
        imgFirstImage.visibility = View.VISIBLE
        rlImage1.visibility = View.GONE


        if((currActivity as NewListingActivity).slot1 == "" || (currActivity as NewListingActivity).slotNo == 1){
            (currActivity as NewListingActivity).forSlot = "slot1"
            (currActivity as NewListingActivity).slot1 = filePath

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

                Log.e("imap",tt.toString()+" "+hh.toString()+" "+ww.toString())

                scaled = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true)

                imgFirstImage.setImageBitmap(scaled)

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

                        imgFirstImage.setImageBitmap(scaled)

                        var bos = ByteArrayOutputStream()
                        scaled.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                        val bitmapdata: ByteArray = bos.toByteArray()
                        val fos = FileOutputStream(filePath)
                        fos.write(bitmapdata)
                        fos.flush()
                        fos.close()

                    } catch (e: Exception) {
                        Log.e("exe",e.toString())
                    }
                }, 1000)

            }


           // Glide.with(currActivity!!).load(filePath).into(imgFirstImage)

            (currActivity as NewListingActivity).tvNext.visibility=View.VISIBLE

            if((currActivity as NewListingActivity).slot2.isEmpty()){
                rlImage2.background = currActivity!!.resources.getDrawable(R.drawable.red_image_shape)
                rlImage1.background =
                    currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
                rlImage3.background =
                    currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
                imgFirstImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgFirstImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))
                imgSecondImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgSecondImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))
                imgThirdImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgThirdImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))

            }else if((currActivity as NewListingActivity).slot3.isEmpty()){
                rlImage3.background = currActivity!!.resources.getDrawable(R.drawable.red_image_shape)
                rlImage1.background =
                    currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
                rlImage2.background =
                    currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
                imgFirstImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgFirstImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))
                imgSecondImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgSecondImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))
                imgThirdImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgThirdImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))
            }

            if((currActivity as NewListingActivity).forSlot.isEmpty()){ //set
                (currActivity as NewListingActivity).selectedImageModelPaths.set(0,filePath)
            }else{
                (currActivity as NewListingActivity).selectedImageModelPaths.add(0,filePath)
            }

            //(currActivity as NewListingActivity).showNext()

        }else if((currActivity as NewListingActivity).slot2 == ""|| (currActivity as NewListingActivity).slotNo == 2){
            (currActivity as NewListingActivity).forSlot = "slot2"
            (currActivity as NewListingActivity).slot2 = filePath

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

                imgSecondImage.setImageBitmap(scaled)

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

                        imgSecondImage.setImageBitmap(scaled)

                        var bos = ByteArrayOutputStream()
                        scaled.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                        val bitmapdata: ByteArray = bos.toByteArray()
                        val fos = FileOutputStream(filePath)
                        fos.write(bitmapdata)
                        fos.flush()
                        fos.close()

                    } catch (e: Exception) {
                        Log.e("exe",e.toString())
                    }
                }, 1000)

            }

           // Glide.with(currActivity!!).load(filePath).into(imgSecondImage)

            (currActivity as NewListingActivity).tvNext.visibility=View.VISIBLE

            imgSecondImage.visibility = View.VISIBLE
            rlImage2.visibility = View.GONE

            if((currActivity as NewListingActivity).slot3.isEmpty()){
                rlImage3.background = currActivity!!.resources.getDrawable(R.drawable.red_image_shape)
                rlImage1.background =
                    currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
                rlImage2.background =
                    currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
                imgFirstImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgFirstImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))
                imgSecondImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgSecondImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))
                imgThirdImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgThirdImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))

            }else if((currActivity as NewListingActivity).slot1.isEmpty()){
                rlImage1.background = currActivity!!.resources.getDrawable(R.drawable.red_image_shape)
                rlImage3.background =
                    currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
                rlImage2.background =
                    currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
                imgFirstImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgFirstImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))
                imgSecondImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgSecondImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))
                imgThirdImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgThirdImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))
            }
            if((currActivity as NewListingActivity).forSlot.isEmpty()){ //set
                (currActivity as NewListingActivity).selectedImageModelPaths.set(1,filePath)
            }else{
                (currActivity as NewListingActivity).selectedImageModelPaths.add(1,filePath)
            }

           // (currActivity as NewListingActivity).showNext()


        }else if((currActivity as NewListingActivity).slot3 =="" || (currActivity as NewListingActivity).slotNo == 3){
            (currActivity as NewListingActivity).forSlot = "slot3"
            (currActivity as NewListingActivity).slot3 = filePath

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

                imgThirdImage.setImageBitmap(scaled)

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

                        imgThirdImage.setImageBitmap(scaled)

                        var bos = ByteArrayOutputStream()
                        scaled.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                        val bitmapdata: ByteArray = bos.toByteArray()
                        val fos = FileOutputStream(filePath)
                        fos.write(bitmapdata)
                        fos.flush()
                        fos.close()

                    } catch (e: Exception) {
                        Log.e("exe",e.toString())
                    }
                }, 1000)

            }

            //Glide.with(currActivity!!).load(filePath).into(imgThirdImage)

            (currActivity as NewListingActivity).tvNext.visibility=View.VISIBLE

            imgThirdImage.visibility = View.VISIBLE
            rlImage3.visibility = View.GONE
            if((currActivity as NewListingActivity).slot2.isEmpty()){
                rlImage2.background = currActivity!!.resources.getDrawable(R.drawable.red_image_shape)
                rlImage1.background =
                    currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
                rlImage3.background =
                    currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
                imgFirstImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgFirstImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))
                imgSecondImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgSecondImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))
                imgThirdImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgThirdImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))

            }else if((currActivity as NewListingActivity).slot1.isEmpty()){
                rlImage1.background = currActivity!!.resources.getDrawable(R.drawable.red_image_shape)
                rlImage3.background =
                    currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
                rlImage2.background =
                    currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
                imgFirstImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgFirstImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))
                imgSecondImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgSecondImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))
                imgThirdImage.setBorderWidth(currActivity!!.resources.getDimension(R.dimen._2sdp))
                imgThirdImage.setBorderColor(
                    currActivity!!.resources.getColor(R.color.white))
            }


            if((currActivity as NewListingActivity).forSlot.isEmpty()){ //set
                (currActivity as NewListingActivity).selectedImageModelPaths.set(2,filePath)
            }else{
                (currActivity as NewListingActivity).selectedImageModelPaths.add(2,filePath)
            }

        }
//        Toast.makeText(currActivity, "File: $filePath", Toast.LENGTH_SHORT).show()
        Utils.addMediaToGallery(currActivity, filePath)
    }
    
    


    fun onProfileImageClick(from : Int,intentFor : Int) { //camera or gallery
        Dexter.withActivity(currActivity!!)
            .withPermissions(
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        if(from == 0){
                            photographer?.takePicture()
                        }else if(from == 1){
                            launchGalleryIntent(intentFor)
                        }else if(from == 2){
                            showImagePickerOptions(intentFor)
                        }
                    }
                    if (report.isAnyPermissionPermanentlyDenied()) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }
    private fun launchGalleryIntent( intentFor : Int) {

        (currActivity as NewListingActivity).getImage(intentFor)

    }


    private fun showImagePickerOptions(intentFor : Int) {
        ImagePickerActivity.showImagePickerOptions(currActivity!!, object : ImagePickerActivity.PickerOptionListener {
            override fun onTakeCameraSelected() {
                launchCameraIntent(intentFor)
                //dispatchTakePictureIntent();
            }

            override fun onChooseGallerySelected() {
                launchGalleryIntent(intentFor)
            }
        })
    }

    private fun launchCameraIntent(intentFor : Int) {
        val intent = Intent(currActivity!!, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )

// setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

// setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)
        startActivityForResult(intent, intentFor)
    }

    private fun showSettingsDialog() {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(currActivity!!)
        builder.setTitle(getString(R.string.dialog_permission_title))
        builder.setMessage(getString(R.string.dialog_permission_message))
        builder.setPositiveButton(getString(R.string.go_to_settings), { dialog, which ->
            dialog.cancel()
            openSettings()
        })
        builder.setNegativeButton(
            getString(android.R.string.cancel),
            { dialog, which -> dialog.cancel() })
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri =
            Uri.fromParts("package", currActivity!!.getPackageName(), null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    fun galleryClick(from : Int,resultUri : String){
        if(from == 0){
            imgFirstImage.visibility = View.VISIBLE
            rlImage1.visibility = View.GONE

            if((currActivity as NewListingActivity).slot2.isEmpty()){
               rlImage2.background = currActivity!!.resources.getDrawable(R.drawable.red_image_shape)
            }
            (currActivity as NewListingActivity).slot1 = resultUri

            Glide.with(currActivity!!).load(resultUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imgFirstImage)

            Glide.with(currActivity!!).load(resultUri).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imgProductImage)

        }else if(from == 1){
            imgSecondImage.visibility = View.VISIBLE
            rlImage2.visibility = View.GONE

            if((currActivity as NewListingActivity).slot3.isEmpty()){
                rlImage3.background = currActivity!!.resources.getDrawable(R.drawable.red_image_shape)
            }

            (currActivity as NewListingActivity).slot2 = resultUri
            Glide.with(currActivity!!).load(resultUri).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imgSecondImage)
            Glide.with(currActivity!!).load(resultUri).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imgProductImage)
        }else if(from == 2){
            imgThirdImage.visibility = View.VISIBLE
            rlImage3.visibility = View.GONE
            (currActivity as NewListingActivity).slot3 = resultUri
            Glide.with(currActivity!!).load(resultUri).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imgThirdImage)
            Glide.with(currActivity!!).load(resultUri).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imgProductImage)
        }

    }

    private fun enterFullscreen() {
//        val decorView = window.decorView
//        decorView.setBackgroundColor(Color.BLACK)
//        val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
//        decorView.systemUiVisibility = uiOptions
    }

    private fun finishRecordingIfNeeded() {
//        if (isRecordingVideo) {
//            isRecordingVideo = false
//            photographer!!.finishRecording()
//            statusTextView.setVisibility(View.INVISIBLE)
//            switchButton.setVisibility(View.VISIBLE)
//            flipButton.setVisibility(View.VISIBLE)
//            actionButton.setEnabled(true)
//            actionButton.setImageResource(R.drawable.record)
//        }
    }





    private fun openPopupToDiscard(slot : Int){


        bottomSheetApplicationDialog = BottomSheetDialog(currActivity!!)
        bottomSheetDiscardPopupBinding = DataBindingUtil.inflate<DiscardPopupBinding>(
            LayoutInflater.from(currActivity),
            R.layout.discard_popup, null, false)

        bottomSheetApplicationDialog?.setContentView(bottomSheetDiscardPopupBinding!!.root)
        Objects.requireNonNull<Window>(bottomSheetApplicationDialog?.window)
            .setBackgroundDrawableResource(android.R.color.transparent)
//        setupFullHeight(bottomSheetApplicationDialog!!)


        bottomSheetDiscardPopupBinding?.rlMain?.setOnClickListener{
            bottomSheetApplicationDialog?.dismiss()
        }
        bottomSheetDiscardPopupBinding?.tvCancel?.setOnClickListener{
            (currActivity as NewListingActivity).slotNo = 0
            bottomSheetApplicationDialog?.dismiss()
        }
        bottomSheetDiscardPopupBinding?.tvOkay?.setOnClickListener{
            if(slot == 1){
                (currActivity as NewListingActivity).slot1 = ""
                rlImage1.visibility = View.VISIBLE
                imgFirstImage.visibility = View.GONE
                rlImage1.background = currActivity!!.resources.getDrawable(R.drawable.red_image_shape)
                rlImage2.background = currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
                rlImage3.background = currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)

                (currActivity as NewListingActivity).selectedImageModelPaths.set(0,"")
                imgProductImage.visibility=View.GONE
                preview.visibility=View.VISIBLE
                imgCamera.isEnabled=true

            }else if(slot == 2){
                (currActivity as NewListingActivity).slot2 = ""
                rlImage2.visibility = View.VISIBLE
                imgSecondImage.visibility = View.GONE
                rlImage2.background = currActivity!!.resources.getDrawable(R.drawable.red_image_shape)
                rlImage1.background = currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
                rlImage3.background = currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)

                (currActivity as NewListingActivity).selectedImageModelPaths.set(1,"")

                imgProductImage.visibility=View.GONE
                preview.visibility=View.VISIBLE
                imgCamera.isEnabled=true
            }else if(slot == 3 ){
                (currActivity as NewListingActivity).slot3 = ""
                rlImage3.visibility = View.VISIBLE
                imgThirdImage.visibility = View.GONE
                rlImage3.background = currActivity!!.resources.getDrawable(R.drawable.red_image_shape)
                rlImage2.background = currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
                rlImage1.background = currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)

                (currActivity as NewListingActivity).selectedImageModelPaths.set(2,"")

                imgProductImage.visibility=View.GONE
                preview.visibility=View.VISIBLE
                imgCamera.isEnabled=true
            }
            bottomSheetApplicationDialog?.dismiss()
        }

        bottomSheetApplicationDialog?.show()

//        dialogDiscard = Dialog(currActivity!!)
//        discardPopupBinding = DataBindingUtil.inflate(
//            LayoutInflater.from(currActivity!!),
//            R.layout.discard_popup, null, true
//        )
//
//        dialogDiscard?.setContentView(discardPopupgetRoot())
//        Objects.requireNonNull<Window>(dialogDiscard?.getWindow())
//            .setBackgroundDrawableResource(android.R.color.transparent)
//
//        discardPopupBinding?.rlMain?.setOnClickListener{
//            dialogDiscard?.dismiss()
//        }
//        discardPopupBinding?.tvCancel?.setOnClickListener{
//            (currActivity as NewListingActivity).slotNo = 0
//            dialogDiscard?.dismiss()
//        }
//        discardPopupBinding?.tvOkay?.setOnClickListener{
//            if(slot == 1){
//                (currActivity as NewListingActivity).slot1 = ""
//                rlImage1.visibility = View.VISIBLE
//                imgFirstImage.visibility = View.GONE
//                rlImage1.background = currActivity!!.resources.getDrawable(R.drawable.red_image_shape)
//                rlImage2.background = currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
//                rlImage3.background = currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
//
//                (currActivity as NewListingActivity).selectedImageModelPaths.set(0,"")
//            }else if(slot == 2){
//                (currActivity as NewListingActivity).slot2 = ""
//                rlImage2.visibility = View.VISIBLE
//                imgSecondImage.visibility = View.GONE
//                rlImage2.background = currActivity!!.resources.getDrawable(R.drawable.red_image_shape)
//                rlImage1.background = currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
//                rlImage3.background = currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
//
//                (currActivity as NewListingActivity).selectedImageModelPaths.set(1,"")
//            }else if(slot == 3 ){
//                (currActivity as NewListingActivity).slot3 = ""
//                rlImage3.visibility = View.VISIBLE
//                imgThirdImage.visibility = View.GONE
//                rlImage3.background = currActivity!!.resources.getDrawable(R.drawable.red_image_shape)
//                rlImage2.background = currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
//                rlImage1.background = currActivity!!.resources.getDrawable(R.drawable.grey_image_shape)
//
//                (currActivity as NewListingActivity).selectedImageModelPaths.set(2,"")
//            }
//            dialogDiscard?.dismiss()
//        }
//        dialogDiscard?.setCanceledOnTouchOutside(false)
//        dialogDiscard?.setCancelable(false)
//        dialogDiscard?.show()
    }


    fun calculateSampleSize(srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int): Int {
        val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
        val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()
        return if (srcAspect > dstAspect) {
            srcWidth / dstWidth
        } else {
            srcHeight / dstHeight
        }
    }

}