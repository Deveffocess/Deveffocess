package com.livo.nuo.view.home.homefragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.transition.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isInvisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.gson.JsonObject
import com.livo.nuo.R
import com.livo.nuo.databinding.BottomSheetlanguagecodeBinding
import com.livo.nuo.lib.roundImageView.RoundedImageView
import com.livo.nuo.manager.SessionManager
import com.livo.nuo.utility.AndroidUtil
import com.livo.nuo.utility.AppUtils
import com.livo.nuo.view.Splash_Screen
import com.livo.nuo.view.profile.*
import com.livo.nuo.viewModel.ViewModelFactory
import com.livo.nuo.viewModel.profile.ProfileViewModel
import java.util.*

class ProfileFragment : Fragment() {

    private var currActivity : Activity? = null
    lateinit var rlProfile : RelativeLayout
    lateinit var tvSubmit : TextView
    lateinit var tvApplicationSettings : TextView
    lateinit var imgUser:ImageView
    lateinit var tvUserName:TextView
    lateinit var tvUserAge:TextView
    lateinit var tvTransporterApplication:TextView
    lateinit var tvTransporterApplicationLabel:TextView
    lateinit var rllanguage:RelativeLayout
    lateinit var rlApplicationSettings:RelativeLayout
    lateinit var rlFAQ:RelativeLayout
    lateinit var rlHelpnSupport:RelativeLayout
    lateinit var rlRefer : RelativeLayout
    lateinit var rlLogout:RelativeLayout

    lateinit var tvShimmerImage:ShimmerFrameLayout
    lateinit var tvShimmerName:ShimmerFrameLayout
    lateinit var tvShimmerAge:ShimmerFrameLayout
    lateinit var shTransporterApplication:ShimmerFrameLayout
    lateinit var shTransporterApplicationLabel:ShimmerFrameLayout

    private var profileViewModel : ProfileViewModel? = null
    var bottomsheetlanguagecode : BottomSheetDialog? = null

    var en_width=0
    var en_height=0
    var dn_width=0
    var dn_height=0
    var sv_width=0
    var sv_height=0

    lateinit var tvFAQ : TextView
    var statustransport = 1.0 // transporter View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root= inflater.inflate(R.layout.fragment_profile, container, false)

        tvSubmit = root.findViewById(R.id.tvSubmit)
        rlProfile = root.findViewById(R.id.rlProfile)
        tvApplicationSettings = root.findViewById(R.id.tvApplicationSettings)
        tvFAQ = root.findViewById(R.id.tvFAQ)
        imgUser=root.findViewById(R.id.imgUser)
        tvUserName=root.findViewById(R.id.tvUserName)
        tvUserAge=root.findViewById(R.id.tvUserAge)
        tvTransporterApplication=root.findViewById(R.id.tvTransporterApplication)
        tvTransporterApplicationLabel=root.findViewById(R.id.tvTransporterApplicationLabel)
        tvShimmerImage=root.findViewById(R.id.tvShimmerImage)
        tvShimmerName=root.findViewById(R.id.tvShimmerName)
        tvShimmerAge=root.findViewById(R.id.tvShimmerAge)
        shTransporterApplication=root.findViewById(R.id.shTransporterApplication)
        shTransporterApplicationLabel=root.findViewById(R.id.shTransporterApplicationLabel)
        rllanguage=root.findViewById(R.id.rllanguage)
        rlApplicationSettings=root.findViewById(R.id.rlApplicationSettings)
        rlFAQ=root.findViewById(R.id.rlFAQ)
        rlHelpnSupport=root.findViewById(R.id.rlHelpnSupport)
        rlRefer = root.findViewById(R.id.rlRefer)
        rlLogout=root.findViewById(R.id.rlLogout)

        initViews()

        return root
    }

    companion object {
        fun newInstance() : Fragment {
            val f = ProfileFragment()
            return f
        }
    }


    fun initViews(){

        currActivity=requireActivity()

        currActivity?.application?.let {
            profileViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(ProfileViewModel::class.java)
        }


        profileViewModel?.let {
            if (currActivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {
                it.getUserSettings()
            }
        }

        tvShimmerName.startShimmer()
        tvShimmerAge.startShimmer()
        tvShimmerName.visibility=View.VISIBLE
        tvShimmerAge.visibility=View.VISIBLE
        tvUserName.visibility=View.GONE
        tvUserAge.visibility=View.GONE
        shTransporterApplication.startShimmer()
        shTransporterApplicationLabel.startShimmer()
        shTransporterApplication.visibility=View.VISIBLE
        shTransporterApplicationLabel.visibility=View.VISIBLE
        tvTransporterApplication.visibility=View.GONE
        tvTransporterApplicationLabel.visibility=View.GONE
        tvSubmit.visibility=View.GONE


        rllanguage.setOnClickListener({
            openBottomPopup()
        })

        rlProfile.setOnClickListener({
            var i=Intent(currActivity,ProfileSettingActivity::class.java)
            startActivity(i)
        })

        rlApplicationSettings.setOnClickListener({
            var i=Intent(currActivity,ApplicationSettingsActivity::class.java)
            startActivity(i)
        })

        rlFAQ.setOnClickListener({
            var i=Intent(currActivity,FAQActivity::class.java)
            startActivity(i)
        })

        rlHelpnSupport.setOnClickListener({
            var i=Intent(currActivity,HelpAndSupportActivity::class.java)
            startActivity(i)
        })

        rlRefer.setOnClickListener({
            /*var i=Intent(currActivity,ReferralActivity::class.java)
            startActivity(i)*/
        })

        rlLogout.setOnClickListener({
            SessionManager.clear()
            var i=Intent(currActivity,Splash_Screen::class.java)
            startActivity(i)
            currActivity!!.finish()
        })

        observers()
    }


    private fun observers() {

        profileViewModel?.getMutableLiveDataUserSettings()
            ?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
                tvUserName.text=it.data.first_name+" "+it.data.last_name
                tvUserAge.text=it.data.age+" "+resources.getString(R.string.years_old)

                tvShimmerName.stopShimmer()
                tvShimmerAge.stopShimmer()
                tvShimmerName.visibility=View.GONE
                tvShimmerAge.visibility=View.GONE
                tvUserName.visibility=View.VISIBLE
                tvUserAge.visibility=View.VISIBLE
                shTransporterApplication.stopShimmer()
                shTransporterApplicationLabel.stopShimmer()
                shTransporterApplication.visibility=View.GONE
                shTransporterApplicationLabel.visibility=View.GONE
                tvTransporterApplication.visibility=View.VISIBLE
                tvTransporterApplicationLabel.visibility=View.VISIBLE
                tvSubmit.visibility=View.VISIBLE

                Glide.with(currActivity!!).addDefaultRequestListener(object : RequestListener<Any> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Any>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        tvShimmerImage.visibility = View.VISIBLE
//                   imgUser.visibility = View.GONE
                        tvShimmerImage.startShimmer()
                        return false
                    }
                    override fun onResourceReady(
                        resource: Any?,
                        model: Any?,
                        target: Target<Any>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        tvShimmerImage.visibility = View.GONE
                        imgUser.visibility = View.VISIBLE
                        tvShimmerImage.stopShimmer()
                        return false
                    }
                })
                    .load(it.data.profile_image).placeholder(currActivity!!.getDrawable(R.drawable.grey_round_shape))
                    .error(currActivity!!.getDrawable(R.drawable.grey_round_shape)).into(imgUser)


                tvTransporterApplication.text=it.data.transporter_application.title
                tvTransporterApplicationLabel.text=it.data.transporter_application.subtitle
                tvSubmit.text=it.data.transporter_application.button_text
                var state=it.data.transporter_application.state
               /* if (state==0)
                {
                    tvSubmit.setTextColor(Color.BLACK)
                    tvSubmit.setBackground(currActivity!!.getDrawable(R.drawable.white_fill_with_black_border))
                }*/
                if (state==1)
                {
                    tvSubmit.setTextColor(Color.WHITE)
                    tvSubmit.setBackground(currActivity!!.getDrawable(R.drawable.black_20_round_shape_5))
                }
                else if (state==2)
                {
                    tvSubmit.setTextColor(Color.WHITE)
                    tvSubmit.setBackground(currActivity!!.getDrawable(R.drawable.black_20_round_shape_5))
                }
                else  if (state==3)
                {
                    tvSubmit.setTextColor(Color.BLACK)
                    tvSubmit.setBackground(currActivity!!.getDrawable(R.drawable.white_fill_with_black_border))
                }


            })

        profileViewModel?.getErrorMutableLiveData()?.observe(currActivity as LifecycleOwner, androidx.lifecycle.Observer {
            //hideProgressBar()
            AppUtils.showToast(currActivity!!,R.drawable.cross,it.message,R.color.error_red,R.color.white,R.color.white)
        })
    }


    private fun openBottomPopup(){

        bottomsheetlanguagecode = BottomSheetDialog(requireActivity())
        var bottomSheetLanguageDialogBinding =
            DataBindingUtil.inflate<BottomSheetlanguagecodeBinding>(
                LayoutInflater.from(requireActivity()),
                R.layout.bottom_sheetlanguagecode,null,false
            )

        bottomsheetlanguagecode?.setContentView(bottomSheetLanguageDialogBinding!!.root)
        Objects.requireNonNull<Window>(bottomsheetlanguagecode?.window)
            .setBackgroundDrawableResource(android.R.color.transparent)


        var imgDanishLang = bottomsheetlanguagecode!!.findViewById<RoundedImageView>(R.id.imgDanishLang)
        var imgEnglishLang = bottomsheetlanguagecode!!.findViewById<RoundedImageView>(R.id.imgEnglishLang)
        var imgDenmarkLang = bottomsheetlanguagecode!!.findViewById<RoundedImageView>(R.id.imgDenmarkLang)
        var llCancel = bottomsheetlanguagecode!!.findViewById<LinearLayout>(R.id.llCancel)
        var llConfirmChange = bottomsheetlanguagecode!!.findViewById<LinearLayout>(R.id.llConfirmChange)
       // var cvEnglish=bottomsheetlanguagecode!!.findViewById<MaterialCardView>(R.id.cvEnglish)

        var enval=0
        var dnval=0
        var svVal=0

        Handler().postDelayed({

            en_width= imgEnglishLang?.width!!
            en_height=imgEnglishLang?.height!!
            dn_width=imgDanishLang?.width!!
            dn_height=imgDanishLang?.height!!
            sv_width=imgDenmarkLang?.width!!
            sv_height=imgDenmarkLang?.height!!

        },500)


        imgDanishLang?.setOnClickListener({

            /*val animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in)
            imgDanishLang.startAnimation(animation)*/

            if (dnval==0) {
                var cw = (imgDanishLang?.getWidth() * 15) / 100
                var ch = (imgDanishLang?.getHeight() * 15) / 100
                var width = imgDanishLang?.getWidth() + cw // ((display.getWidth()*20)/100)
                var height = imgDanishLang?.getHeight() + ch // ((display.getHeight()*30)/100)
                var parms = LinearLayout.LayoutParams(width, height)
                imgDanishLang?.setLayoutParams(parms)

                var parms1 = LinearLayout.LayoutParams(en_width, en_height)
                imgEnglishLang?.setLayoutParams(parms1)

                var parms2 = LinearLayout.LayoutParams(sv_width, sv_height)
                imgDenmarkLang?.setLayoutParams(parms2)

                dnval=1
                enval=0
                svVal=0
            }

            imgDanishLang?.setBorderColor(resources.getColor(R.color.colorPrimary))
            imgDanishLang?.setBorderWidth(resources.getDimension(R.dimen._3sdp))
            imgEnglishLang?.setBorderWidth(resources.getDimension(R.dimen._0sdp))
            imgDenmarkLang?.setBorderWidth(resources.getDimension(R.dimen._0sdp))

            bottomSheetLanguageDialogBinding.tvDanishLang.setTextColor(requireActivity().resources.getColor(R.color.livo_heading_black))
            bottomSheetLanguageDialogBinding.tvDenmarkLang.setTextColor(requireActivity().resources.getColor(R.color.black_40_opacity))
            bottomSheetLanguageDialogBinding.tvEnglishLang.setTextColor(requireActivity().resources.getColor(R.color.black_40_opacity))
        })

        imgEnglishLang?.setOnClickListener({

            /*val animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.zoom_in)
            imgEnglishLang.startAnimation(animation)*/
            if (enval==0) {
                var cw = (imgEnglishLang?.getWidth() * 15) / 100
                var ch = (imgEnglishLang?.getHeight() * 15) / 100
                var width = imgEnglishLang?.getWidth() + cw // ((display.getWidth()*20)/100)
                var height = imgEnglishLang?.getHeight() + ch // ((display.getHeight()*30)/100)
                var parms = LinearLayout.LayoutParams(width, height)
                imgEnglishLang?.setLayoutParams(parms)

                var parms1 = LinearLayout.LayoutParams(dn_width, dn_height)
                imgDanishLang?.setLayoutParams(parms1)

                var parms2 = LinearLayout.LayoutParams(sv_width, sv_height)
                imgDenmarkLang?.setLayoutParams(parms2)

                dnval=0
                enval=1
                svVal=0
            }

            imgEnglishLang?.setBorderColor(resources.getColor(R.color.colorPrimary))
            imgEnglishLang?.setBorderWidth(resources.getDimension(R.dimen._3sdp))
            imgDanishLang?.setBorderWidth(resources.getDimension(R.dimen._0sdp))
            imgDenmarkLang?.setBorderWidth(resources.getDimension(R.dimen._0sdp))

            bottomSheetLanguageDialogBinding.tvDanishLang.setTextColor(requireActivity().resources.getColor(R.color.black_40_opacity))
            bottomSheetLanguageDialogBinding.tvDenmarkLang.setTextColor(requireActivity().resources.getColor(R.color.black_40_opacity))
            bottomSheetLanguageDialogBinding.tvEnglishLang.setTextColor(requireActivity().resources.getColor(R.color.livo_heading_black))
        })
        imgDenmarkLang?.setOnClickListener({

            /*val animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.zoom_in)
            imgDenmarkLang.startAnimation(animation)*/

            if(svVal==0) {
                var cw = (imgDenmarkLang?.getWidth() * 15) / 100
                var ch = (imgDenmarkLang?.getHeight() * 15) / 100
                var width = imgDenmarkLang?.getWidth() + cw // ((display.getWidth()*20)/100)
                var height = imgDenmarkLang?.getHeight() + ch // ((display.getHeight()*30)/100)
                var parms = LinearLayout.LayoutParams(width, height)
                imgDenmarkLang?.setLayoutParams(parms)

                var parms1 = LinearLayout.LayoutParams(dn_width, dn_height)
                imgDanishLang?.setLayoutParams(parms1)

                var parms2 = LinearLayout.LayoutParams(en_width, en_height)
                imgEnglishLang?.setLayoutParams(parms2)

                dnval=0
                enval=0
                svVal=1
            }

            imgDenmarkLang?.setBorderColor(resources.getColor(R.color.colorPrimary))
            imgDenmarkLang?.setBorderWidth(resources.getDimension(R.dimen._3sdp))
            imgDanishLang?.setBorderWidth(resources.getDimension(R.dimen._0sdp))
            imgEnglishLang?.setBorderWidth(resources.getDimension(R.dimen._0sdp))

            bottomSheetLanguageDialogBinding.tvDanishLang.setTextColor(requireActivity().resources.getColor(R.color.black_40_opacity))
            bottomSheetLanguageDialogBinding.tvDenmarkLang.setTextColor(requireActivity().resources.getColor(R.color.livo_heading_black))
            bottomSheetLanguageDialogBinding.tvEnglishLang.setTextColor(requireActivity().resources.getColor(R.color.black_40_opacity))
        })

        llConfirmChange?.setOnClickListener({

            bottomSheetLanguageDialogBinding.llCancel.setBackground(requireActivity()!!.resources.getDrawable(R.drawable.grey_round_shape_45_opacity))
            bottomSheetLanguageDialogBinding.llConfirmChange.setBackground(requireActivity().resources.getDrawable(R.drawable.blue_round_shape))

        })
        llCancel?.setOnClickListener({

            bottomSheetLanguageDialogBinding.llConfirmChange.setBackground(requireActivity().resources.getDrawable(R.drawable.blue_round_shape))
            bottomSheetLanguageDialogBinding.llCancel.setBackground(requireActivity().resources.getDrawable(R.drawable.grey_round_shape_45_opacity))

        })

        bottomsheetlanguagecode?.show()


    }




}