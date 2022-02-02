package com.livo.nuo.view.message

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.livo.nuo.R
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.google.gson.JsonObject
import com.livo.nuo.utility.AndroidUtil
import com.livo.nuo.utility.AppUtils
import com.livo.nuo.view.message.fragments.ChatFragment
import com.livo.nuo.view.message.prefs.Prefs
import com.livo.nuo.view.message.services.NetworkReceiver
import com.livo.nuo.view.message.util.ParentActivityImpl
import com.livo.nuo.view.ongoing.ListingOngoingStateActivity
import com.livo.nuo.view.profile.ContactAdminActivity
import com.livo.nuo.viewModel.ViewModelFactory
import com.livo.nuo.viewModel.profile.ProfileViewModel
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNReconnectionPolicy
import org.json.JSONObject


class ChatActivity : ParentActivity() , ParentActivityImpl {

    // The BroadcastReceiver that tracks network connectivity changes.
    private var mNetworkReceiver = NetworkReceiver()

    private var currActivity : Activity? = this
    private var profileViewModel : ProfileViewModel? = null

    // tag::INIT-1.1[]
    private var mPubNub : PubNub? = null
    // end::INIT-1.1[]

    // end::INIT-1.1[]
    var channel = "098765"
    var completed=""
    var rceiver_uuid=""

    //Prefs.get().uuid()
    private var mChatFragment: ChatFragment? = null

    lateinit var mFragmentContainer: FrameLayout
    lateinit var mToolbar:Toolbar
    lateinit var imgBack:ImageView
    lateinit var tvRequestHelp:TextView

    lateinit var view2:View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mToolbar=findViewById(R.id.toolbar)
        mFragmentContainer=findViewById(R.id.container)
        imgBack=findViewById(R.id.imgBack)
        tvRequestHelp=findViewById(R.id.tvRequestHelp)
        view2=findViewById(R.id.view2)

        //Imp Set UUId
        //Prefs.get().clearAllData()
        //Prefs.get().uuid("forest-animal-33")

        channel=intent.getStringExtra("ch")!!
        completed=intent.getStringExtra("st")!!
        rceiver_uuid=intent.getStringExtra("ruuid")!!

        Log.e("uuid",rceiver_uuid)

        setSupportActionBar(mToolbar)
        initializePubNub()
        mChatFragment = ChatFragment.newInstance(channel,completed)
        addFragment(mChatFragment)
        initReceiver()

        initViews()
        observers()

    }

    private fun initReceiver() {
        // Registers BroadcastReceiver to track network connection changes.
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        mNetworkReceiver = NetworkReceiver()
        this.registerReceiver(mNetworkReceiver, filter)
    }

    fun initViews(){

        currActivity?.application?.let {
            profileViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(ProfileViewModel::class.java)
        }


        if (completed.isNotEmpty())
        {
            tvRequestHelp.visibility=View.GONE
            view2.visibility=View.VISIBLE
        }


        imgBack.setOnClickListener({
            finish()
        })

        tvRequestHelp.setOnClickListener({
            var i=Intent(applicationContext,ContactAdminActivity::class.java)
            startActivity(i)
            finish()
        })

    }

    private fun initializePubNub() {
        // tag::KEYS-2[]
        val pubKey = "pub-c-370f7ab9-d0ab-4404-ad08-06eaee1b9b09"
        val subKey = "sub-c-34ef23fc-2a85-11ec-b636-021bdbd01fcd"
        val pnConfiguration = PNConfiguration()
        pnConfiguration.publishKey = pubKey
        pnConfiguration.subscribeKey = subKey
        pnConfiguration.uuid = Prefs.get().uuid() // uuid is stored in SharedPreferences
        pnConfiguration.logVerbosity = PNLogVerbosity.BODY
        pnConfiguration.reconnectionPolicy = PNReconnectionPolicy.LINEAR
        pnConfiguration.maximumReconnectionRetries = 10
        mPubNub = PubNub(pnConfiguration)
        // end::INIT-1.2[]
    }

    // tag::INIT-3[]
    override fun getPubNub(): PubNub? {
        return mPubNub
    }
    // end::INIT-3[]

    override fun setTitle(title: String?) {
        mToolbar!!.title = title
    }

    override fun setSubtitle(subtitle: String?) {
        if (!TextUtils.isEmpty(subtitle)) {
            mToolbar!!.subtitle = subtitle
        }
    }

    override fun addFragment(fragment: Fragment?) {
        super.addFragment(fragment)
    }

    override fun enableBackButton(enable: Boolean) {
        supportActionBar!!.setDisplayHomeAsUpEnabled(enable)
        supportActionBar!!.setDisplayShowHomeEnabled(enable)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            backPress()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun provideLayoutResourceId(): Int {
        return R.layout.activity_chat
    }

    override fun backPress() {
        onBackPressed()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    fun networkAvailable(available: Boolean) {
        if (available) mChatFragment!!.onConnected()
    }

    // tag::INIT-4[]
    override fun onDestroy() {
        mPubNub!!.unsubscribeAll()
        mPubNub!!.forceDestroy()
        super.onDestroy()
        // tag::ignore[]
        if (mNetworkReceiver != null) {
            unregisterReceiver(mNetworkReceiver)
        }
        // end::ignore[]
    }

    fun sendChatNotification(msg:String){

        var json_data: JSONObject = JSONObject(msg)
        //Toast.makeText(applicationContext,json_data.getString("text"),Toast.LENGTH_SHORT).show()
        var message=json_data.getString("text")

        profileViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {

                var jsonObject = JsonObject()
                jsonObject.addProperty("message", message)
                jsonObject.addProperty("reciever_uuid", rceiver_uuid)
                jsonObject.addProperty("channel_id", channel)
                it.getSendChatNotification(jsonObject)
            }
        }
    }

    fun observers()
    {
        profileViewModel?.getMutableLiveDataViewSendChatNotification()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
                //Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT).show()
            })

        profileViewModel?.getErrorMutableLiveData()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {

            AppUtils.showToast(currActivity!!,R.drawable.cross,it.message,R.color.error_red,R.color.white,R.color.white)
        })
    }
}