package com.livo.nuo.view.home.homefragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.JsonObject
import com.livo.nuo.R
import com.livo.nuo.models.ChatMessageModel
import com.livo.nuo.models.ProductDataModel
import com.livo.nuo.utility.AndroidUtil
import com.livo.nuo.utility.AppUtils
import com.livo.nuo.utility.MyAppSession
import com.livo.nuo.view.home.adapter.ListingAdapter
import com.livo.nuo.view.home.adapter.MessageCurrentAdapter
import com.livo.nuo.view.message.prefs.Prefs
import com.livo.nuo.view.notifications.NotificationsActivity
import com.livo.nuo.view.search.SearchActivity
import com.livo.nuo.viewModel.ViewModelFactory
import com.livo.nuo.viewModel.products.ProductViewModel
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.callbacks.PNCallback
import com.pubnub.api.models.consumer.objects_api.channel.PNGetAllChannelsMetadataResult
import com.pubnub.api.models.consumer.objects_api.membership.PNGetMembershipsResult
import com.pubnub.api.models.consumer.PNStatus

import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadata
import org.jetbrains.annotations.NotNull
import java.util.ArrayList


class MessageFragment : Fragment() {

    lateinit var imgNotification:ImageView
    private var productViewModel : ProductViewModel? = null
    private var currActivity : Activity? = null
    lateinit var rvCurrentMessageList:RecyclerView
    lateinit var rlSearch:RelativeLayout
    lateinit var rvCompletedMessageList:RecyclerView
    lateinit var llMainLayout:LinearLayout
    lateinit var llDataNotFound:LinearLayout
    lateinit var imgNotificationDot:ImageView
    lateinit var shimmerViewContainer:ShimmerFrameLayout

    private var chatmessageList = ArrayList<ChatMessageModel>()
    private var chatmessagecompletedList = ArrayList<ChatMessageModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root=inflater.inflate(R.layout.fragment_message, container, false)

        imgNotification=root.findViewById(R.id.imgNotification)
        rvCurrentMessageList=root.findViewById(R.id.rvCurrentMessageList)
        rvCompletedMessageList=root.findViewById(R.id.rvCompletedMessageList)
        rlSearch=root.findViewById(R.id.rlSearch)
        llMainLayout=root.findViewById(R.id.llMainLayout)
        llDataNotFound=root.findViewById(R.id.llDataNotFound)
        shimmerViewContainer=root.findViewById(R.id.shimmerViewContainer)
        imgNotificationDot=root.findViewById(R.id.imgNotificationDot)

        initViews()
        return root
    }

    companion object {
        fun newInstance() : Fragment {
            val f = MessageFragment()
            return f
        }
    }

    fun initViews(){

        currActivity = requireActivity()

        currActivity?.application?.let {
            productViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(ProductViewModel::class.java)
        }

        shimmerViewContainer.visibility=View.VISIBLE
        shimmerViewContainer.startShimmer()
        llMainLayout.visibility=View.GONE


        imgNotification.setOnClickListener({
            val fade = Fade()
            val decor: View = currActivity!!.window.getDecorView()

            //fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true)
            fade.excludeTarget(android.R.id.statusBarBackground, true)
            fade.excludeTarget(android.R.id.navigationBarBackground, true)

            currActivity!!.window.setEnterTransition(fade)
            currActivity!!.window.setExitTransition(fade)

            val intent = Intent(currActivity!!, NotificationsActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                currActivity!!, imgNotification, ViewCompat.getTransitionName(imgNotification)!!
            )
            startActivity(intent, options.toBundle())
        })

        rlSearch.setOnClickListener({
            val intent = Intent(currActivity!!, SearchActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                currActivity!!, rlSearch, ViewCompat.getTransitionName(rlSearch)!!
            )
            startActivity(intent, options.toBundle())
        })

        observers()

    }

    override fun onResume() {

        productViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {
                it.getAllChats()
            }
        }

        if(MyAppSession.notiIcon)
        {
            imgNotificationDot.visibility=View.VISIBLE
        }
        else{
            imgNotificationDot.visibility=View.GONE
        }

        super.onResume()
    }

    private fun observers() {

        productViewModel?.getMutableLiveDataAllChat()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {

                shimmerViewContainer.visibility=View.GONE
                shimmerViewContainer.stopShimmer()
                llDataNotFound.visibility=View.GONE
                llMainLayout.visibility=View.VISIBLE

                chatmessageList.clear()
                chatmessageList.addAll(it.data.current)
                chatmessagecompletedList.clear()
                chatmessagecompletedList.addAll(it.data.completed)

                    var layoutManager : GridLayoutManager?
                rvCurrentMessageList.setHasFixedSize(true)
                    layoutManager= GridLayoutManager(currActivity, 1)
                rvCurrentMessageList.layoutManager = layoutManager
                    var adapter = MessageCurrentAdapter(currActivity!!,chatmessageList,"")
                rvCurrentMessageList.adapter = adapter

                rvCompletedMessageList.setHasFixedSize(true)
                var layoutManager1= GridLayoutManager(currActivity, 1)
                rvCompletedMessageList.layoutManager = layoutManager1
                var adapter1 = MessageCurrentAdapter(currActivity!!,chatmessagecompletedList,"completed")
                rvCompletedMessageList.adapter = adapter1


            })

        productViewModel?.getErrorMutableLiveData()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
            //hideProgressBar()

            shimmerViewContainer.visibility=View.GONE
            shimmerViewContainer.stopShimmer()
            llDataNotFound.visibility=View.VISIBLE
            llMainLayout.visibility=View.GONE

           // AppUtils.showToast(currActivity!!,R.drawable.cross,it.message,R.color.error_red,R.color.white,R.color.white)
        })
    }

}