package com.livo.nuo.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.jaeger.library.StatusBarUtil
import com.livo.nuo.R
import com.livo.nuo.manager.SessionManager
import com.livo.nuo.utility.LocalizeActivity
import com.livo.nuo.view.home.HomeActivity
import com.livo.nuo.view.prelogin.Login_Activity

class Splash_Screen : LocalizeActivity() {


    lateinit var imgLogo:ImageView
    var ListingFragment="ListingFragment"
    lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        imgLogo=findViewById(R.id.imgLogo)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        Handler().postDelayed({

           pref = getSharedPreferences(ListingFragment, Context.MODE_PRIVATE)
           val editor = pref.edit()
           editor.clear()
           editor.apply()

            pref = getSharedPreferences("PickUp", Context.MODE_PRIVATE)
            val editor1 = pref.edit()
            editor1.clear()
            editor1.apply()

            pref = getSharedPreferences("DropOff", Context.MODE_PRIVATE)
            val editor2 = pref.edit()
            editor2.clear()
            editor2.apply()


           if (SessionManager.getLoginModel() != null) {
               var i=Intent(applicationContext,HomeActivity::class.java)
               startActivity(i)
               finish()
           } else {
               var i=Intent(applicationContext,Login_Activity::class.java)
               // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
               var options= ViewCompat.getTransitionName(imgLogo)?.let {
                   ActivityOptionsCompat.makeSceneTransitionAnimation(this@Splash_Screen,imgLogo,
                       it
                   )
               }
               startActivity(i,options?.toBundle())
               //overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left)
               finish()
           }



        }, 3000)
    }

    override fun setStatusBar() {
        val mColor = resources.getColor(R.color.white)
        StatusBarUtil.setColor(this, mColor,40)
    }

}