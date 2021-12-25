package  com.livo.nuo.utility

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.view.MotionEvent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.jaeger.library.StatusBarUtil
import com.livo.nuo.R
import java.util.*


open class LocalizeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
//        Fabric.with(this, Crashlytics())
        super.onCreate(savedInstanceState)
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        applicationContext.cacheDir.deleteRecursively()
       // FirebaseMessaging.getInstance().setAutoInitEnabled(true);

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = Color.TRANSPARENT
//
//        }

        setStatusBar()
        isNetworkAvailable()
        setLocale()
    }

    protected open fun setStatusBar() {
        StatusBarUtil.setColor(this, resources.getColor(R.color.colorPrimary))
    }


    protected fun setLocale() {
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        MyAppSession.deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
//        PrefManager.getInstance(applicationContext).savePreference(MyAppSession.FCM_TOKEN, MyAppSession.deviceId)


        val savedLanguage = MyAppPreferences.getInstance(this).getlanguage()


        if (savedLanguage != null) {
            when (savedLanguage) {
                "en" -> {
                    conf.setLocale(Locale("en"))
                    MyAppSession.locale = "en"
                    MyAppSession.isLocaleEnglish = true
                }
                "da" -> {
                    conf.setLocale(Locale("da"))
                    MyAppSession.locale = "da"
                    MyAppSession.isLocaleEnglish = false
                }
                "sv" -> {
                    conf.setLocale(Locale("sv"))
                    MyAppSession.locale = "sv"
                    MyAppSession.isLocaleEnglish = false
                }
                else -> {
                    conf.setLocale(Locale("en"))
                    MyAppSession.locale = "en"
                    MyAppSession.isLocaleEnglish = true
                }
            }
            res.updateConfiguration(conf, dm)
        }


    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        val v = currentFocus

        if (v != null &&
                (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) &&
                v is EditText &&
                !v.javaClass.name.startsWith("android.webkit.")) {
            val scrcoords = IntArray(2)
            v.getLocationOnScreen(scrcoords)
            val x = ev.rawX + v.left - scrcoords[0]
            val y = ev.rawY + v.top - scrcoords[1]

            if (x < v.left || x > v.right || y < v.top || y > v.bottom)
                AppUtils.hideKeyboard(this)
        }
        return super.dispatchTouchEvent(ev)
    }

     fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
         if(activeNetworkInfo != null && activeNetworkInfo.isConnected){
//             AppUtils.showToast(this, R.drawable.netrowk_on,getString(R.string.you_are_back_online),
//                 R.color.success_green,R.color.white,R.color.white)
         }else{
             AppUtils.showToast(this, R.drawable.netrowk_off,getString(R.string.you_are_offline_please_check_your_internet_connection),
                 R.color.black_border_color, R.color.white,R.color.white)
         }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    override fun onBackPressed() {
        super.onBackPressed()
//        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left)
    }

//    fun changeBottomNavigationBarColor( color : Int){
//        window.navigationBarColor = resources.getColor(color)
//
//    }
}