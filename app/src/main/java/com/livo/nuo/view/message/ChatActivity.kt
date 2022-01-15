package com.livo.nuo.view.message

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
import androidx.appcompat.widget.Toolbar
import com.livo.nuo.R
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub

import androidx.fragment.app.Fragment
import com.livo.nuo.view.message.fragments.ChatFragment
import com.livo.nuo.view.message.prefs.Prefs
import com.livo.nuo.view.message.services.NetworkReceiver
import com.livo.nuo.view.message.util.ParentActivityImpl
import com.livo.nuo.view.ongoing.ListingOngoingStateActivity
import com.livo.nuo.view.profile.ContactAdminActivity
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNReconnectionPolicy


class ChatActivity : ParentActivity() , ParentActivityImpl {

    // The BroadcastReceiver that tracks network connectivity changes.
    private var mNetworkReceiver = NetworkReceiver()

    // tag::INIT-1.1[]
    private var mPubNub : PubNub? = null
    // end::INIT-1.1[]

    // end::INIT-1.1[]
    var channel = "098765"
    var completed=""

    //Prefs.get().uuid()
    private var mChatFragment: ChatFragment? = null

    lateinit var mFragmentContainer: FrameLayout
    lateinit var mToolbar:Toolbar
    lateinit var imgBack:ImageView
    lateinit var tvRequestHelp:TextView
    lateinit var tvViewListings:TextView

    lateinit var view2:View
    lateinit var view3:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mToolbar=findViewById(R.id.toolbar)
        mFragmentContainer=findViewById(R.id.container)
        imgBack=findViewById(R.id.imgBack)
        tvRequestHelp=findViewById(R.id.tvRequestHelp)
        tvViewListings=findViewById(R.id.tvViewListings)
        view2=findViewById(R.id.view2)
        view3=findViewById(R.id.view3)

        //Imp Set UUId
        //Prefs.get().clearAllData()
        //Prefs.get().uuid("forest-animal-33")

        channel=intent.getStringExtra("ch")!!
        completed=intent.getStringExtra("st")!!

        //Log.e("uuid",completed)

        setSupportActionBar(mToolbar)
        initializePubNub()
        mChatFragment = ChatFragment.newInstance(channel,completed)
        addFragment(mChatFragment)
        initReceiver()

        initViews()

    }

    private fun initReceiver() {
        // Registers BroadcastReceiver to track network connection changes.
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        mNetworkReceiver = NetworkReceiver()
        this.registerReceiver(mNetworkReceiver, filter)
    }

    fun initViews(){

        if (completed.isNotEmpty())
        {
            tvRequestHelp.visibility=View.GONE
            tvViewListings.visibility=View.GONE
            view2.visibility=View.VISIBLE
            view3.visibility=View.VISIBLE
        }

        imgBack.setOnClickListener({
            finish()
        })

        tvRequestHelp.setOnClickListener({
            var i=Intent(applicationContext,ContactAdminActivity::class.java)
            startActivity(i)
            finish()
        })
        tvViewListings.setOnClickListener({
           /* var i=Intent(applicationContext,ListingOngoingStateActivity::class.java)
            startActivity(i)
            finish()*/
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

}