package com.livo.nuo.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.jaeger.library.StatusBarUtil
import com.livo.nuo.R
import com.livo.nuo.manager.SessionManager
import com.livo.nuo.utility.AndroidUtil
import com.livo.nuo.utility.AppUtils
import com.livo.nuo.utility.LocalizeActivity
import com.livo.nuo.view.home.HomeActivity
import com.livo.nuo.view.prelogin.Login_Activity
import com.livo.nuo.view.product.ProductDetailActivity
import com.livo.nuo.viewModel.ViewModelFactory
import com.livo.nuo.viewModel.prelogin.LoginViewModel


class Splash_Screen : LocalizeActivity() {


    lateinit var imgLogo:ImageView
    var ListingFragment="ListingFragment"
    lateinit var pref: SharedPreferences
    private var loginViewModel : LoginViewModel? = null

    var link_url=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

         link_url= intent.data.toString()

        imgLogo=findViewById(R.id.imgLogo)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)



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

            this?.application?.let {
                loginViewModel = ViewModelProvider(
                    ViewModelStore(),
                    ViewModelFactory(it)
                ).get(LoginViewModel::class.java)
            }

            loginViewModel?.let {
                if (this.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                    it.extraData()
                }
            }

            observers()


        }, 1000)
    }


    fun observers()
    {
          loginViewModel?.getMutableLiveDataExtraDataModel()?.observe(this, Observer {
             Log.e("che",it.data.colors.toString())


              if (SessionManager.getLoginModel() != null) {

                  if (link_url.equals("null")) {

                      var i = Intent(applicationContext, HomeActivity::class.java)
                      startActivity(i)
                      finish()
                  }
                  else{
                      val s: String = link_url
                      val p = s.split("=").toTypedArray()
                      val e = " " + p[1]

                      val dat = Base64.decode(e, Base64.DEFAULT)
                      val mm = String(dat)

                      var i=Intent(applicationContext, ProductDetailActivity::class.java)
                      i.putExtra("id",mm)
                      startActivity(i)
                      finish()
                  }
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

         })

         loginViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
             //hideProgressBar()
             //dialogReferral?.dismiss()

             AppUtils.showToast(this,R.drawable.cross,it.message,R.color.error_red,R.color.white,R.color.white)


         })
    }


    override fun setStatusBar() {
        val mColor = resources.getColor(R.color.white)
        StatusBarUtil.setColor(this, mColor,40)
    }

}