package com.livo.nuo.view.notifications

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.livo.nuo.R
import android.transition.Fade
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.JsonObject
import com.livo.nuo.models.NotificationDataModel
import com.livo.nuo.utility.AndroidUtil
import com.livo.nuo.utility.AppUtils
import com.livo.nuo.viewModel.ViewModelFactory
import com.livo.nuo.viewModel.products.ProductViewModel


class NotificationsActivity : AppCompatActivity() {

    lateinit var imgBack:ImageView
    lateinit var rvNotifications:RecyclerView
    lateinit var shimmerViewContainer:ShimmerFrameLayout

    private var currActivity : Activity = this
    private var productViewModel : ProductViewModel? = null


    private var notificationModelDemo = ArrayList<NotificationDataModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        imgBack=findViewById(R.id.imgBack)
        rvNotifications=findViewById(R.id.rvNotifications)
        shimmerViewContainer=findViewById(R.id.shimmerViewContainer)
        rvNotifications.visibility=View.GONE
        shimmerViewContainer.visibility=View.VISIBLE
        shimmerViewContainer.startShimmer()

        val fade = Fade()
        val decor: View = window.decorView
       // fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true)
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)
        window.setEnterTransition(fade)
        window.setExitTransition(fade)


        imgBack.setOnClickListener({
            onBackPressed()
        })

        initViews()
    }

    fun initViews(){


        currActivity?.application?.let {
            productViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(ProductViewModel::class.java)
        }


        productViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                var jsonObject =  JsonObject()
                jsonObject.addProperty("page",1 )
                it.getAllNotification(jsonObject)
            }
        }




        observers()

    }

    private fun observers() {

        productViewModel?.getMutableLiveDataAllNotification()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {


                rvNotifications.visibility=View.VISIBLE
                shimmerViewContainer.visibility=View.GONE
                shimmerViewContainer.stopShimmer()

                notificationModelDemo.clear()
                notificationModelDemo.addAll(it.data.notifications_data)

                var layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,false)
                rvNotifications.layoutManager = layoutManager
                var adapter = NotificationsAdapter(currActivity,notificationModelDemo)
                rvNotifications.adapter = adapter

            })

        productViewModel?.getErrorMutableLiveData()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
            AppUtils.showToast(currActivity!!,R.drawable.cross,it.message,R.color.error_red,R.color.white,R.color.white)
        })
    }

}