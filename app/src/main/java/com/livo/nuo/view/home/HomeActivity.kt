package com.livo.nuo.view.home

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.jaeger.library.StatusBarUtil
import com.livo.nuo.R
import com.livo.nuo.commonadapter.AdapterCommonViewPager
import com.livo.nuo.utility.AppUtils
import com.livo.nuo.utility.CheckPermission
import com.livo.nuo.utility.LocalizeActivity
import com.livo.nuo.view.home.homefragments.ListingFragment
import com.livo.nuo.view.home.homefragments.MessageFragment
import com.livo.nuo.view.home.homefragments.MyListingFragment
import com.livo.nuo.view.home.homefragments.ProfileFragment
import com.livo.nuo.view.listing.NewListingActivity

class HomeActivity : LocalizeActivity(), View.OnClickListener {

    companion object {
        lateinit var fa: HomeActivity
    }

    private lateinit var dialog: Dialog

    private var currActivity : Activity = this
    private var isFifth = false
    private var from = ""

    lateinit var vv:ViewPager
    lateinit var imgHome:ImageView
    lateinit var imgListing:ImageView
    lateinit var imgMessages:ImageView
    lateinit var imgProfile:ImageView
    lateinit var tvHome:TextView
    lateinit var tvListing:TextView
    lateinit var tvMessages:TextView
    lateinit var tvProfile:TextView
    lateinit var rlMain:RelativeLayout
    lateinit var rlHome:LinearLayout
    lateinit var rlListing:LinearLayout
    lateinit var rlCreateListing:RelativeLayout
    lateinit var rlMessages:LinearLayout
    lateinit var rlProfile:LinearLayout
    lateinit var imgCreateListing:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        fa=this

        vv=findViewById(R.id.vv)
        imgHome=findViewById(R.id.imgHome)
        imgListing=findViewById(R.id.imgListing)
        imgMessages=findViewById(R.id.imgMessages)
        imgProfile=findViewById(R.id.imgProfile)
        tvHome=findViewById(R.id.tvHome)
        tvListing=findViewById(R.id.tvListing)
        tvMessages=findViewById(R.id.tvMessages)
        tvProfile=findViewById(R.id.tvProfile)
        rlMain=findViewById(R.id.rlMain)
        rlHome=findViewById(R.id.rlHome)
        rlListing=findViewById(R.id.rlListing)
        rlCreateListing=findViewById(R.id.rlCreateListing)
        rlMessages=findViewById(R.id.rlMessages)
        rlProfile=findViewById(R.id.rlProfile)
        imgCreateListing=findViewById(R.id.imgCreateListing)

        initViews()
    }

    private fun initViews(){

        if(intent.getStringExtra("from") != null){
            from = intent.getStringExtra("from")!!
        }

        imgCreateListing.setOnClickListener({
            showProgressBar()
            var i=Intent(applicationContext,NewListingActivity::class.java)
            startActivity(i)
            hideProgressBar()
        })

        setUpViewPager()
        setListner()
        requestPermission()
    }

    private fun requestPermission(){
        if(CheckPermission.checkCameraPermission(currActivity)){
//            AppUtils.showToast(currActivity,R.drawable.check,"permission given",R.color.error_red,R.color.white,R.color.white)
        }else{
            CheckPermission.requestCameraPermission(currActivity,123)
        }

    }

    private fun setUpViewPager(){
        val adapter = AdapterCommonViewPager(currActivity,supportFragmentManager, FragmentStatePagerAdapter.POSITION_NONE)
        adapter.addFragment(MyListingFragment.newInstance(),"")
        adapter.addFragment(ListingFragment.newInstance(),"")
//        adapter.addFragment(CreateListingFragment.newInstance(),"")
        adapter.addFragment(MessageFragment.newInstance(),"")
        adapter.addFragment(ProfileFragment.newInstance(),"")
       vv.adapter = adapter




        if(from.equals("chat")){
            isFifth = false
            vv.setCurrentItem(1,true)
            imgHome.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
           imgListing.setColorFilter(ContextCompat.getColor(currActivity, R.color.colorPrimary));
           imgMessages.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
           imgProfile.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
           tvHome.visibility = View.GONE
           tvListing.visibility = View.VISIBLE
           tvMessages.visibility = View.GONE
           tvProfile.visibility = View.GONE

            val animation1 = AnimationUtils.loadAnimation(currActivity,R.anim.push_up_in)
           tvListing.animation = animation1
           imgListing.animation = animation1
           rlMain.setBackgroundColor(resources.getColor(R.color.white))

        }else{
           vv.setCurrentItem(0,true)

           imgHome.setColorFilter(ContextCompat.getColor(currActivity, R.color.colorPrimary));
           imgListing.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
           imgMessages.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
           imgProfile.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
           tvHome.visibility = View.VISIBLE
           tvListing.visibility = View.GONE
           tvMessages.visibility = View.GONE
           tvProfile.visibility = View.GONE

            val animation1 = AnimationUtils.loadAnimation(currActivity,R.anim.push_up_in)
           tvHome.animation = animation1
           imgHome.animation = animation1
           rlMain.setBackgroundColor(resources.getColor(R.color.livo_bg_color))


        }

       vv.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(i: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(i: Int) {
                if (i == 0) {
                    isFifth = false
                   imgHome.setColorFilter(ContextCompat.getColor(currActivity, R.color.colorPrimary));
                   imgListing.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
                   imgMessages.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
                   imgProfile.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
                   tvHome.visibility = View.VISIBLE
                   tvListing.visibility = View.GONE
                   tvMessages.visibility = View.GONE
                   tvProfile.visibility = View.GONE

                    val animation1 = AnimationUtils.loadAnimation(applicationContext,R.anim.push_up_in)
                    val animation2 = AnimationUtils.loadAnimation(applicationContext,R.anim.push_up_down)
                   tvHome.animation = animation1
                   imgHome.animation = animation1
                   imgListing.animation = animation2
                   rlMain.setBackgroundColor(resources.getColor(R.color.livo_bg_color))


                } else if (i == 1) {
                    isFifth = false
                   imgHome.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
                   imgListing.setColorFilter(ContextCompat.getColor(currActivity, R.color.colorPrimary));
                   imgMessages.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
                   imgProfile.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
                   tvHome.visibility = View.GONE
                   tvListing.visibility = View.VISIBLE
                   tvMessages.visibility = View.GONE
                   tvProfile.visibility = View.GONE

                    val animation1 = AnimationUtils.loadAnimation(applicationContext,R.anim.push_up_in)
                    val animation2 = AnimationUtils.loadAnimation(applicationContext,R.anim.push_up_down)

                   tvListing.animation = animation1
                   imgListing.animation = animation1
                   imgHome.animation = animation2
                   rlMain.setBackgroundColor(resources.getColor(R.color.white))

//                   svBottom.setBackgroundColor(currActivity.resources.getColor(R.color.white))


                } else if (i == 2) {
//                    isFifth = false
//                   imgHome.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
//                   imgListing.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
//                   imgMessages.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
//                   imgProfile.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
//                   tvHome.visibility = View.GONE
//                   tvListing.visibility = View.GONE
//                   tvMessages.visibility = View.GONE
//                   tvProfile.visibility = View.GONE

                    isFifth = false
                   imgHome.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
                   imgListing.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
                   imgMessages.setColorFilter(ContextCompat.getColor(currActivity, R.color.colorPrimary));
                   imgProfile.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
                   tvHome.visibility = View.GONE
                   tvListing.visibility = View.GONE
                   tvMessages.visibility = View.VISIBLE
                   tvProfile.visibility = View.GONE

                    val animation1 = AnimationUtils.loadAnimation(applicationContext,R.anim.push_up_in)
                   tvMessages.animation = animation1
                   imgMessages.animation = animation1
                   rlMain.setBackgroundColor(resources.getColor(R.color.white))

//                   svBottom.setBackgroundColor(currActivity.resources.getColor(R.color.white))



                } else if (i == 3) {

                    isFifth = true
                   imgHome.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
                   imgListing.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
                   imgMessages.setColorFilter(ContextCompat.getColor(currActivity, R.color.livo_disable_grey));
                   imgProfile.setColorFilter(ContextCompat.getColor(currActivity, R.color.colorPrimary));
                   tvHome.visibility = View.GONE
                   tvListing.visibility = View.GONE
                   tvMessages.visibility = View.GONE
                   tvProfile.visibility = View.VISIBLE

                    val animation1 = AnimationUtils.loadAnimation(applicationContext,R.anim.push_up_in)
                   tvProfile.animation = animation1
                   imgProfile.animation = animation1
                   rlMain.setBackgroundColor(resources.getColor(R.color.white))

//                   svBottom.setBackgroundColor(currActivity.resources.getColor(R.color.white))


                }else if(i == 4){

                }
            }
        })
    }

    fun showProgressBar(){
        dialog =  AppUtils.showProgress(this)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog)
    }


    private fun setListner(){

       rlHome.setOnClickListener(this)
       rlListing.setOnClickListener(this)
       rlCreateListing.setOnClickListener(this)
       rlMessages.setOnClickListener(this)
       rlProfile.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.rlHome ->{
                isFifth = false
                vv.setCurrentItem(0,true)
                rlMain.setBackgroundColor(resources.getColor(R.color.livo_bg_color))

            }
            R.id.rlListing ->{
                isFifth = false
                vv.setCurrentItem(1,true)
                rlMain.setBackgroundColor(resources.getColor(R.color.white))

            }
            R.id.rlCreateListing ->{
                isFifth = false
                //NewListingActivity.open(currActivity)
//                binding!!.vv.setCurrentItem(2,true)
            }
            R.id.rlMessages ->{
                isFifth = false
               vv.setCurrentItem(2,true)
                rlMain.setBackgroundColor(resources.getColor(R.color.white))

            }
            R.id.rlProfile ->{
                isFifth = true
                vv.setCurrentItem(3,true)
               rlMain.setBackgroundColor(resources.getColor(R.color.white))

            }
        }
    }

    protected override fun setStatusBar() {

        val mColor = resources.getColor(R.color.colorPrimary)
        StatusBarUtil.setLightMode(currActivity)
    }

    fun loadAgain(){
        setUpViewPager()
        setListner()
    }
}