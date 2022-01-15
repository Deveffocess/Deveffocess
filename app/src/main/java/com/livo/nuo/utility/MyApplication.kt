package com.livo.nuo.utility


import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.livo.nuo.manager.SharedPreferenceFactory
import com.livo.nuo.manager.SharedPreferenceManager
import com.livo.nuo.view.message.prefs.Prefs
import java.security.MessageDigest


class MyApplication : Application() , LifecycleObserver {

    override fun onCreate() {
        super.onCreate()

        val appSignatureHelper = AppSignatureHelper(this)
        appSignatureHelper.appSignatures
//        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//ADDED
        instance = this
        setAppContext(instance)
        printHashKey(instance)
        initializeSharedPreference()

        Prefs.initialize(this)
    }





    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onAppBackgrounded() {
        Log.d("MyApp", "App in background")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onAppForegrounded() {
        Log.d("MyApp", "App in foreground")
    }

    fun initializeSharedPreference() {
        val sharedPreferenceManager = SharedPreferenceManager(getSharedPreferences(SharedPreferenceManager.SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE))
        SharedPreferenceFactory.setSharedPreferenceManager(sharedPreferenceManager)
    }


    companion object {
        private lateinit var instance: MyApplication
        private lateinit var mAppContext: Context

        fun getInstance(): MyApplication {
            return instance
        }

        fun getAppContext(): Context {
            return mAppContext
        }

        fun setAppContext(mAppContext: Context) {
            Companion.mAppContext = mAppContext
        }

        fun isActivityVisible()  : Boolean{
            return activityVisible
        }

         fun activityResumed() {
            activityVisible = true
        }

         fun  activityPaused() {
            activityVisible = false
        }

        private var activityVisible = false

        @JvmStatic
        @Synchronized
        fun get(): MyApplication {
            if (instance == null) {
                instance = MyApplication()
            }
            return instance
        }
    }
    }

     fun printHashKey(context: Context) {
          try {
                val info = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
                for (signature in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    val hashKey = String(Base64.encode(md.digest(), 0))
                    System.out.println("hashkey$hashKey")
                    Log.i("AppLog", "key:$hashKey=")
                }
          } catch (e: Exception) {
                Log.e("AppLog", "error:", e)
          }


}


