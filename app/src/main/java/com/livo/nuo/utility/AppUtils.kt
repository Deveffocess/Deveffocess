package com.livo.nuo.utility


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.Html
import android.text.format.DateFormat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.livo.nuo.R
import com.livo.nuo.netUtils.response.ErrorResponse
import org.json.JSONObject
import java.io.File
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AppUtils {



    companion object {




        /*----------------------------------DATE TIME FORMATS ------------------------------------ */
        // var mongoDateTimeFormat = SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH)
        //var mongoDateTimeFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        var serverDateTimeFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH)
        var mongoDateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        var serverDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        var serverTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        var displayDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        var displayDateTimeFormat = SimpleDateFormat("MMM-dd-yy hh:mm a", Locale.ENGLISH)
        var displayTimeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        var deliveryDate = SimpleDateFormat("dd,MMM yyyy", Locale.ENGLISH)
        var displayDateAndTime = SimpleDateFormat("dd,MMM yyyy | hh:mm a", Locale.ENGLISH)
        var transcationDate = SimpleDateFormat("MMM dd, yyyy")
        var time = SimpleDateFormat("hh:mm a")


        /*----------------------------------DATE TIME  METHODS------------------------------------ */
        init {
            serverDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            serverDateTimeFormat.timeZone = TimeZone.getTimeZone("UTC")
            displayDateTimeFormat.timeZone = TimeZone.getTimeZone("UTC")
            displayTimeFormat.timeZone = TimeZone.getTimeZone("UTC")
            deliveryDate.timeZone = TimeZone.getTimeZone("UTC")
            displayDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            serverTimeFormat.timeZone = TimeZone.getTimeZone("UTC")
            mongoDateTimeFormat.timeZone = TimeZone.getTimeZone("UTC")
            displayDateAndTime.timeZone = TimeZone.getTimeZone("UTC")
        }

        fun convert(dateStr: String, original: SimpleDateFormat, convert: SimpleDateFormat): String {
            var displayDate = dateStr
            try {
                val date = original.parse(dateStr)
                displayDate = convert.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                return displayDate
            }
        }


        fun getDateByDate(dateStr: String): String {
            var displayDate = dateStr
            try {
                val date = serverDateFormat.parse(dateStr)
                displayDate = displayDateFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                return displayDate
            }
        }

        fun getDateTime(dateStr: String): String {
            var displayDate = dateStr
            try {
                val date = serverDateTimeFormat.parse(dateStr)
                displayDate = displayDateTimeFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                return displayDate
            }
        }
        fun getTime(dateStr: String): String {
            var displayDate = dateStr
            try {
                val date = serverDateTimeFormat.parse(dateStr)
                displayDate = displayDateTimeFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                return displayDate
            }
        }

        fun getTimeOnly(dateStr: String): String {
            var displayDate = dateStr
            try {
                val date = serverDateTimeFormat.parse(dateStr)
                displayDate = time.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                return displayDate
            }
        }

        fun getDate(dateStr: String): String {
            var displayDate = dateStr
            try {
                val date = serverDateTimeFormat.parse(dateStr)
                displayDate = displayDateFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                return displayDate
            }
        }

        fun getTimeFromTime(timeStr: String): String {
            var displayDate = timeStr
            try {
                val date = serverTimeFormat.parse(timeStr)
                displayDate = displayTimeFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                return displayDate
            }
        }

        fun getServerTime(timeStr: String): String {
            var displayDate = timeStr
            try {
                val date = displayTimeFormat.parse(timeStr)
                displayDate = serverTimeFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                return displayDate
            }
        }

        fun getUTCTime(timeStr: String): String {
            mongoDateTimeFormat.timeZone = TimeZone.getTimeZone("UTC")
            try {
                val myDate = mongoDateTimeFormat.parse(timeStr)
                displayDateTimeFormat.timeZone = TimeZone.getDefault()
                return displayDateTimeFormat.format(myDate)

            } catch (e: ParseException) {
                e.printStackTrace()
                return timeStr
            }
        }
        fun getUTCTimeOnly(timeStr: String): String {
            mongoDateTimeFormat.timeZone = TimeZone.getTimeZone("UTC")
            try {
                val myDate = mongoDateTimeFormat.parse(timeStr)
                time.timeZone = TimeZone.getDefault()
                return time.format(myDate)

            } catch (e: ParseException) {
                e.printStackTrace()
                return timeStr
            }
        }

        public fun convertDateFormat(dateTimeString: String?, originalFormat: SimpleDateFormat, targetFormat: SimpleDateFormat): String {
            if (dateTimeString == null || dateTimeString.equals("null", ignoreCase = true))
                return ""

            var targetDateString = dateTimeString
            try {
                val originalDate = originalFormat.parse(dateTimeString)
                val originalDateString = originalFormat.format(originalDate)
                val targetDate = originalFormat.parse(originalDateString)
                targetDateString = targetFormat.format(targetDate)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                return targetDateString!!
            }
        }


       public fun hideKeyboard(activity: Activity?) {
            if (activity != null && activity.currentFocus != null && activity.currentFocus!!.windowToken != null) {
                val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                try {
                    inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
                } catch (ignored: NullPointerException) {

                }

            }
        }

        fun getErrorResponse(jsonString: String?): ErrorResponse? {
            return fromJson(jsonString, ErrorResponse::class.java) as ErrorResponse?
        }


        fun fromJson(jsonString: String?, type: Type): Any? {
            return Gson().fromJson(jsonString, type)
        }


        fun jsonFromModel(model: Any): JSONObject {
            if (model is String) {
                return JSONObject(Gson().toJson(JSONObject(model)))
            }
            return JSONObject(Gson().toJson(model))
        }

        /*fun refreshToken(tokenRefresh: OnTokenRefresh, mContext: Context) {
            if (!MyAppSession.isTokenRefresh) {
                MyAppSession.isTokenRefresh = true;
                val call = APIClient.getClient().getRefreshToken(MyAppSession.locale)
                APIClient.getResponse(call, mContext as Activity, null, true, object : RetrofitResponse {
                    override fun onResponse(data: String?) {
                        if (data != null) {
                            var jsonResponse = JSONObject(data)
                            var jsonData = jsonResponse.getJSONObject(Keys.KEY_DATA);
                            var token = jsonData.optString("token");
                            MyAppSession.userToken = token;
                            MyAppPreferences.getInstance(mContext).saveInPreference("Token", token)
                            tokenRefresh.onTokenUpdate()
                        } else {
                            *//*   val home_intent = Intent(mContext, LoginActivity::class.java)
                               home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                               mContext.startActivity(home_intent)
   *//*
                        }
                    }
                })
            }

        }*/


        private var mInstance: AppUtils? = null

        val instance: AppUtils
            get() {
                if (mInstance == null) {
                    mInstance = AppUtils()
                }
                return mInstance!!
            }

        fun loadImageCrop(url: String, imageView: ImageView, placeHolder: Int, targetHeight: Int, targetWidth: Int) {
            //        System.out.println("IMAGE URL IS= " + url);
            if (!url.isEmpty() && !url.equals("null", ignoreCase = true)) {
                Glide.with(MyApplication.getInstance().applicationContext).load(url)
                    .apply(RequestOptions().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).override(targetWidth, targetHeight).placeholder(placeHolder))
                    .into(imageView)
            } else {
                Glide.with(MyApplication.getInstance().applicationContext).load(placeHolder)
                    .apply(RequestOptions().centerCrop().override(targetWidth, targetHeight)).into(imageView)
            }
        }

        fun loadImageCrop(url: String, imageView: ImageView, placeHolder: Int) {
            //        System.out.println("IMAGE URL IS= " + url);
            if (!url.isEmpty() && !url.equals("null", ignoreCase = true)) {
                Glide.with(MyApplication.getInstance().applicationContext).load(url)
                    .apply(RequestOptions().centerCrop()
                        .placeholder(placeHolder))
                    .into(imageView)
            } else {
                Glide.with(MyApplication.getInstance().applicationContext).load(placeHolder)
                    .apply(RequestOptions().centerCrop()).into(imageView)
            }
        }

        fun loadImageInside(url: String?, imageView: ImageView, placeHolder: Int, targetHeight: Int, targetWidth: Int) {
            //        System.out.println("IMAGE URL IS= " + url);
            if (url != null && !url.isEmpty() && !url.equals("null", ignoreCase = true)) {
                Glide.with(MyApplication.getInstance().applicationContext).load(url)
                    .apply(RequestOptions().centerInside()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).override(targetWidth, targetHeight).placeholder(placeHolder))
                    .into(imageView)
//                Picasso.with(MyApplication.getInstance()).load(url).placeholder(placeHolder).into(imageView)

            } else {
                Glide.with(MyApplication.getInstance().applicationContext).load(placeHolder)
                    .apply(RequestOptions().centerInside().override(targetWidth, targetHeight)).into(imageView)
            }
        }

        fun loadImageFullImage(url: String?, imageView: ImageView, placeHolder: Int) {
            //        System.out.println("IMAGE URL IS= " + url);
            if (url != null && !url.isEmpty() && !url.equals("null", ignoreCase = true)) {
                Glide.with(MyApplication.getInstance().applicationContext).load(url)
                    .apply(RequestOptions().centerInside()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).placeholder(placeHolder))
                    .into(imageView)
//                Picasso.with(MyApplication.getInstance()).load(url).placeholder(placeHolder).into(imageView)

            } else {
                Glide.with(MyApplication.getInstance().applicationContext).load(placeHolder)
                    .apply(RequestOptions().centerInside()).into(imageView)
            }
        }

        fun loadImageResourceCenterCrop(resourceId: Int, imageView: ImageView, targetHeight: Int, targetWidth: Int) {
            Glide.with(MyApplication.getInstance().applicationContext).load(resourceId)
                .apply(RequestOptions().centerCrop().override(targetWidth, targetHeight)).into(imageView)
        }

        fun loadImageResource(resourceId: Int, imageView: ImageView) {
            Glide.with(MyApplication.getInstance().applicationContext).load(resourceId)
                .apply(RequestOptions().centerInside()).into(imageView)
        }



        fun setText(textView: AppCompatTextView, text: String?) {
            if (text != null && !text.equals("null", ignoreCase = true)) {
                textView.text = text
            } else {
                textView.text = "N/A"
            }
        }

        fun setText(textView: AppCompatTextView, text: String?, defaultText: String) {
            if (text != null && !text.isEmpty() && !text.equals("null", ignoreCase = true)) {
                textView.text = text
            } else {
                textView.text = defaultText
            }
        }

        fun setText(textView: AppCompatEditText, text: String?) {
            if (text != null && !text.equals("null", ignoreCase = true)) {
                textView.setText(text)
            } else {
                textView.setText("")
            }
        }

        fun setText(textView: AppCompatEditText, text: String?, defaultText: String) {
            if (text != null && !text.equals("null", ignoreCase = true)) {
                textView.setText(text)
            } else {
                textView.setText(defaultText)
            }
        }

        fun setText(textView: AppCompatEditText, text: Double?) {
            if (text != null) {
                textView.setText(text.toString())
            } else {
                textView.setText("")
            }
        }


        fun setTexts(textView: AppCompatTextView, vararg texts: String) {
            var text = ""
            for (test in texts) {
                if (!test.equals("null", ignoreCase = true))
                    text += test
                else
                    text = ""
            }

            text = text.replace(", ,".toRegex(), ",")
            textView.text = Html.fromHtml(text.trim { it <= ' ' })
        }

        fun showToast(activity: Context, image : Int?, message: String, backgroundColor: Int,textColor : Int,imagecolor : Int) {
            if (activity != null) {

               val layoutInflater = LayoutInflater.from(activity).inflate(R.layout.row_toast,null)

                var img=layoutInflater.findViewById<ImageView>(R.id.img)
                var tvMessage=layoutInflater.findViewById<TextView>(R.id.tvMessage)
                var rlMain=layoutInflater.findViewById<RelativeLayout>(R.id.rlMain)

                if(image!= null){
                   img.setImageResource(image)
                }

                tvMessage.text = message

                tvMessage.setTextColor(activity.resources.getColor(textColor))
                rlMain.backgroundTintList = activity.resources.getColorStateList(backgroundColor);
                img.setColorFilter(ContextCompat.getColor(activity,imagecolor));



                val toast = Toast(activity)

                toast.view = layoutInflater
                toast.duration = Toast.LENGTH_SHORT

                toast.show()
            }
        }
        fun showCenterToast(activity: Context, image : Int?, message: String, backgroundColor: Int,textColor : Int,imagecolor : Int) {
            if (activity != null) {

               val layoutInflater = LayoutInflater.from(activity).inflate(R.layout.row_toast,null)
                var img=layoutInflater.findViewById<ImageView>(R.id.img)
                var tvMessage=layoutInflater.findViewById<TextView>(R.id.tvMessage)
                var rlMain=layoutInflater.findViewById<RelativeLayout>(R.id.rlMain)
                if(image!= null){
                   img.setImageResource(image)
                }

                tvMessage.text = message

                tvMessage.setTextColor(activity.resources.getColor(textColor))
                rlMain.backgroundTintList = activity.resources.getColorStateList(backgroundColor);
                img.setColorFilter(ContextCompat.getColor(activity,imagecolor));



                val toast = Toast(activity)

                toast.view = layoutInflater
                toast.duration = Toast.LENGTH_SHORT
                toast.setGravity(Gravity.CENTER,0,0)
                toast.show()
            }
        }


        fun getText(textView: TextView?): String {
            return textView?.text?.toString()?.trim { it <= ' ' } ?: ""
        }

        fun checkNull(text: String?): String {
            return if (text != null && !text.equals("null", ignoreCase = true)) {
                text
            } else {
                ""
            }
        }


        fun getText(textView: EditText?): String {
            return textView?.text?.toString()?.trim { it <= ' ' } ?: ""

        }

        private var dialog: Dialog? = null

        private fun dialogInternet(activity: Activity, fragment: Fragment?, requestCode: Int) {
            if (dialog != null && dialog!!.isShowing)
                dialog!!.dismiss()

            val ad = AlertDialog.Builder(activity)
            ad.setTitle(activity.getString(R.string.noConnection))
            ad.setMessage(activity.getString(R.string.turnOnInternet))
            //        ad.setCancelable(false);
            ad.setNegativeButton(activity.getString(R.string.mobileData)) { _, _ ->
                val i = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
                if (fragment == null) {
                    activity.startActivityForResult(i, requestCode)
                } else {
                    fragment.startActivityForResult(i, requestCode)
                }
            }
            ad.setPositiveButton(activity.getString(R.string.wifi)) { dialog, which ->
                val i = Intent(Settings.ACTION_WIFI_SETTINGS)
                if (fragment == null) {
                    activity.startActivityForResult(i, requestCode)
                } else {
                    fragment.startActivityForResult(i, requestCode)
                }
            }
            dialog = ad.show()
        }


//        fun showProgress(activity: Activity): Dialog {
//            val overlayDialog = Dialog(activity, android.R.style.Theme_Panel)
//            overlayDialog.setContentView(R.layout.dialog_progress)
//            overlayDialog.setCanceledOnTouchOutside(false)
//            if (!activity.isFinishing) {
//                overlayDialog.show()
//            }
//            return overlayDialog
//        }

        fun isOnline(mContext: Context): Boolean {
            val connectivity =
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connectivity.allNetworkInfo
            for (i in info.indices)
                if (info[i].state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            return false
        }

        fun showProgress(activity: Activity): Dialog {
            val overlayDialog = Dialog(activity, android.R.style.Theme_Panel)
            overlayDialog.setContentView(R.layout.dialog_progress)
            overlayDialog.setCanceledOnTouchOutside(false)
            var imgLoader=overlayDialog.findViewById<ImageView>(R.id.imgLoader)
            Glide.with(activity).load(R.drawable.loader).into(imgLoader);
            if (!activity.isFinishing) {
                overlayDialog.show()
            }
            return overlayDialog
        }


        fun hideProgress(overlayDialog: Dialog) {
            if (overlayDialog.isShowing) {
                overlayDialog.dismiss()
            }
        }

        fun printLogs(tag: String, value: String) {
            Log.d(tag, value)
        }

        fun getOutPutMediaFile(context: Context): File? {
            val mediaStorageDir = File(
                context.getExternalFilesDir(
                    Environment.DIRECTORY_PICTURES
                ), context.getString(R.string.app_name)
            )
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdir()) {
                    return null
                }
            }
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            return File(
                mediaStorageDir.path + File.separator +
                        "IMG_" + timeStamp + ".jpg"
            )
        }


        private fun getRealPathFromURI(
            contentURI: String,
            activity: Activity
        ): String? {
            val contentUri = Uri.parse(contentURI)
            val cursor =
                activity.contentResolver.query(contentUri, null, null, null, null)
            return if (cursor == null) {
                contentUri.path
            } else {
                cursor.moveToFirst()
                val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                cursor.getString(index)
            }
        }

        fun getFilename(): String? {
            val file = File(
                Environment.getExternalStorageDirectory().path,
                "MyFolder/Images"
            )
            if (!file.exists()) {
                file.mkdirs()
            }
            return file.absolutePath + "/" + System.currentTimeMillis() + ".jpg"
        }

        fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int,
            reqHeight: Int
        ): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1
            if (height > reqHeight || width > reqWidth) {
                val heightRatio =
                    Math.round(height.toFloat() / reqHeight.toFloat())
                val widthRatio =
                    Math.round(width.toFloat() / reqWidth.toFloat())
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }
            val totalPixels = width * height.toFloat()
            val totalReqPixelsCap = reqWidth * reqHeight * 2.toFloat()
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++
            }
            return inSampleSize
        }

        fun formatDate(date : String) : String{
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat = SimpleDateFormat("dd MMM yyyy")
            val inputDateStr = date
            val date = inputFormat.parse(inputDateStr)
            val outputDateStr = outputFormat.format(date)
            return outputDateStr
        }

        fun getDateDigit(date : String) : String{
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat = SimpleDateFormat("dd")
            val inputDateStr = date
            val date = inputFormat.parse(inputDateStr)
            val outputDateStr = outputFormat.format(date)
            return outputDateStr
        }

        fun getDay(date : String) : String{
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat = SimpleDateFormat("MMM")
            val inputDateStr = date
            val date = inputFormat.parse(inputDateStr)
            val outputDateStr = outputFormat.format(date)
            return outputDateStr
        }

        fun formatDate2(date : String) : String{
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat = SimpleDateFormat("dd-MM-yyyy")
            val inputDateStr = date
            val date = inputFormat.parse(inputDateStr)
            val outputDateStr = outputFormat.format(date)
            return outputDateStr
        }
        fun formatDate3(date : String) : String{
            val inputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val outputFormat = SimpleDateFormat("dd MMM yy")
            val inputDateStr = date
            val date = inputFormat.parse(inputDateStr)
            val outputDateStr = outputFormat.format(date)
            return outputDateStr
        }

         fun getAge(year: Int, month: Int, day: Int): String? {
            val dob = Calendar.getInstance()
            val today = Calendar.getInstance()
            dob[year, month] = day
            var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
            if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
                age--
            }
            val ageInt = age
            return ageInt.toString()
        }

        fun sendOnlineStatus(status : Int){

        }


        fun changeYYYYmmDDtoHHmm(timestamp:String):String{
            val calendar = Calendar.getInstance(Locale.ENGLISH)
            calendar.timeInMillis = timestamp.toLong() * 1000L
            val date = DateFormat.format("hh:mm a",calendar).toString()
            return date
        }
        fun convertTimeStamp(timestamp:String):String{
            val calendar = Calendar.getInstance(Locale.ENGLISH)
            calendar.timeInMillis = timestamp.toLong() * 1000L
            val date = DateFormat.format("yyyy-MM-dd",calendar).toString()
            return date
        }

        fun getCurrentDate() : String{
            val c = Calendar.getInstance().time
            println("Current time => $c")

            val df =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = df.format(c)
            return formattedDate
        }

        fun convertTimeStampToFormat(timestamp:String):String{
            val calendar = Calendar.getInstance(Locale.ENGLISH)
            calendar.timeInMillis = timestamp.toLong() * 1000L
            val date = DateFormat.format("hh:mm a",calendar).toString()
            return date
        }

    }





}
