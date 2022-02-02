package com.livo.nuo.view.notifications

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.livo.nuo.R
import android.transition.Fade
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.widget.NestedScrollView
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
import com.livo.nuo.utility.MyAppSession
import com.livo.nuo.viewModel.ViewModelFactory
import com.livo.nuo.viewModel.products.ProductViewModel


class NotificationsActivity : AppCompatActivity() {

    lateinit var imgBack:ImageView
    lateinit var rvNotifications:RecyclerView
    lateinit var shimmerViewContainer:ShimmerFrameLayout

    private var currActivity : Activity = this
    private var productViewModel : ProductViewModel? = null
    lateinit var rlNoDataFound:RelativeLayout
    lateinit var nsMainScroll:NestedScrollView

    var currentPage=1
    var has_next=false
    var count=0

    private var notificationModelDemo = ArrayList<NotificationDataModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        imgBack=findViewById(R.id.imgBack)
        rvNotifications=findViewById(R.id.rvNotifications)
        shimmerViewContainer=findViewById(R.id.shimmerViewContainer)
        rlNoDataFound=findViewById(R.id.rlNoDataFound)
        nsMainScroll=findViewById(R.id.nsMainScroll)
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

                it.getMarkNotification()
            }
        }

        productViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                var jsonObject =  JsonObject()
                jsonObject.addProperty("page",currentPage.toString() )
                it.getAllNotification(jsonObject)
            }
        }

        nsMainScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                val view = nsMainScroll.getChildAt(nsMainScroll.getChildCount() - 1) as View

                val diff: Int = view.bottom - (nsMainScroll.getHeight() + nsMainScroll
                    .getScrollY())

                if (diff == 0) {

                    if (has_next) {
                        currentPage++
                        doApiCall()
                    }  else{}
                }
                Log.i("TAG", "Scroll DOWN")
            }
            if (scrollY < oldScrollY) {
                Log.i("TAG", "Scroll UP")
            }
            if (scrollY == 0) {
                Log.i("TAG", "TOP SCROLL")
            }
            if (scrollY == v.measuredHeight - v.getChildAt(0).measuredHeight) {
                Log.i("TAG", "BOTTOM SCROLL")
            }
        })




        observers()
       // setAdapter()
    }

    private fun observers() {

        productViewModel?.getMutableLiveDataAllNotification()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {


                rvNotifications.visibility=View.VISIBLE
                shimmerViewContainer.visibility=View.GONE
                shimmerViewContainer.stopShimmer()

                has_next=it.data.has_next

                if (currentPage==1)
                    notificationModelDemo.clear()

               notificationModelDemo.addAll(it.data.notifications_data)

                setAdapter()

               /* if (has_next) {
                    currentPage++
                    notificationModelDemo.addAll(it.data.notifications_data)
                }
                else{
                    if (count==0)
                    {
                        count=1
                        currentPage++
                        notificationModelDemo.addAll(it.data.notifications_data)
                    }
                }*/


            })

        productViewModel?.getMutableLiveMarkNotification()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {

                MyAppSession.notiIcon=false

            })

        productViewModel?.getErrorMutableLiveData()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {

            rvNotifications.visibility=View.GONE
            shimmerViewContainer.visibility=View.GONE
            shimmerViewContainer.stopShimmer()
            rlNoDataFound.visibility=View.VISIBLE

            AppUtils.showToast(currActivity!!,R.drawable.cross,it.message,R.color.error_red,R.color.white,R.color.white)
        })
    }

    fun setAdapter(){

        var layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,false)
        rvNotifications.layoutManager = layoutManager
        var adapter = NotificationsAdapter(currActivity,notificationModelDemo)
        rvNotifications.adapter = adapter
        rvNotifications.setHasFixedSize(true)

    }


    fun doApiCall(){
        productViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                var jsonObject =  JsonObject()
                jsonObject.addProperty("page",currentPage.toString() )
                it.getAllNotification(jsonObject)
            }
        }
    }

}