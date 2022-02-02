package com.livo.nuo.view.prelogin

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.transition.ChangeBounds
import android.transition.Transition
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.livo.nuo.DummyData
import com.livo.nuo.R
import com.livo.nuo.models.CountryCodeModel
import com.livo.nuo.view.prelogin.adapter.AdapterLanguageOptions
import com.livo.nuo.view.prelogin.adapter.CountryCodeAdapter
import com.loopeer.shadow.ShadowView
import java.util.*
import com.livo.nuo.models.LoginModel
import com.livo.nuo.netUtils.services.ApiInterface
import com.livo.nuo.netUtils.services.api
import com.livo.nuo.viewModel.prelogin.LoginViewModel
import com.livo.nuo.viewModel.ViewModelFactory
import retrofit2.*
import java.lang.Exception
import kotlin.collections.ArrayList

import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import com.jaeger.library.StatusBarUtil
import com.livo.nuo.databinding.BottomSheetlanguagecodeBinding
import com.livo.nuo.lib.roundImageView.RoundedImageView
import com.livo.nuo.manager.SessionManager
import com.livo.nuo.netUtils.services.fpNetworkHelper
import com.livo.nuo.utility.*
import com.livo.nuo.view.home.HomeActivity
import com.livo.nuo.view.message.prefs.Prefs
import org.json.JSONArray
import org.json.JSONObject

class Login_Activity : LocalizeActivity(){

    lateinit var tvEnterPhone:TextView
    lateinit var tvEnterOTP:TextView

//    lateinit var vv:VideoView
    lateinit var tvDialCode:TextView
    lateinit var rlSelectLanguage:RelativeLayout
    lateinit var etPhone:EditText
    lateinit var tvSelectLanguage:TextView
    lateinit var tvLogin:TextView
    lateinit var svLoginRegister:ShadowView

    var getotpcode:String=""
    lateinit var rlotp:RelativeLayout
    lateinit var etOTP:EditText
    lateinit var tvResendOtp:TextView

    var countryCode="+45"
    var timerc=0

    private lateinit var dialog: Dialog
    lateinit var countryCodeAdapter:CountryCodeAdapter
    private var countryCodeList = ArrayList<CountryCodeModel>()

    private lateinit var dialogReferral:Dialog
    lateinit var languageOptions : AdapterLanguageOptions
     var languageModel=ArrayList<CountryCodeModel>()
    lateinit var tvApply:TextView

    var bottomsheetlanguagecode : BottomSheetDialog? = null
    var en_width=0
    var en_height=0
    var dn_width=0
    var dn_height=0
    var sv_width=0
    var sv_height=0


    private var loginViewModel : LoginViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        vv=findViewById(R.id.vv1)
        tvDialCode=findViewById(R.id.tvDialCode)
        rlSelectLanguage=findViewById(R.id.rlSelectLanguage)
        etPhone=findViewById(R.id.etPhone)
        tvSelectLanguage=findViewById(R.id.tvSelectLanguage)
        tvLogin=findViewById(R.id.tvLogin)
        svLoginRegister=findViewById(R.id.svLoginRegister)
        etOTP=findViewById(R.id.etOTP)
        rlotp=findViewById(R.id.rlOtp)
        tvResendOtp=findViewById(R.id.tvResendOtp)
        tvEnterPhone=findViewById(R.id.tvEnterPhone)
        tvEnterOTP=findViewById(R.id.tvEnterOTP)

        tvDialCode.setOnClickListener({
            openFullWidthPopup()
        })

        rlSelectLanguage.setOnClickListener({
           // openChooseLanguagePopup()

            openBottomPopup()
        })

        init()
    }

    private fun init(){

        countryCodeList = DummyData.getCountryList(this)
        languageModel=DummyData.getLanguages(this)
        //getWindow().sharedElementEnterTransition = enterTransition()
        //supportActionBar?.hide()

        this?.application?.let {
            loginViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(LoginViewModel::class.java)
        }

       /* loginViewModel?.let {
            if (this.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                it.extraData()
            }
        }*/


        tvLogin.isEnabled = false
        tvLogin.background =
            this@Login_Activity.resources.getDrawable(R.drawable.button_grey_ripple_effect)
        svLoginRegister.shadowColor =
            this@Login_Activity.resources.getColor(R.color.grey_100)

        var refreshedToken = ""


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("tag", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            refreshedToken = task.result
            PrefManager.getInstance(applicationContext).savePreference(MyAppSession.FCM_TOKEN, refreshedToken)

        })

        Log.e("token",refreshedToken)

       /* val path = "android.resource://" + packageName + "/" + R.raw.app_bg
        vv.setVideoURI(Uri.parse(path))
        vv.start()*/
        setListner()
        reloadLanguage()

        observers()
    }

    private fun enterTransition(): Transition? {
        val bounds = ChangeBounds()
        bounds.setDuration(1000)
        return bounds
    }

    private fun openChooseLanguagePopup(){
        dialogReferral = Dialog(this@Login_Activity as Context)

        val customLayout = layoutInflater.inflate(R.layout.dialog_language, null)
        dialogReferral.setContentView(customLayout!!)
        Objects.requireNonNull<Window>(dialogReferral.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)
        val window = dialogReferral?.window!!
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        tvApply=customLayout.findViewById(R.id.tvApply)

        val lang = MyAppPreferences.getInstance(this).getlanguage()
        var langCode = lang

        var rvLanguage=customLayout.findViewById<RecyclerView>(R.id.rvLanguage)
        var close=customLayout.findViewById<ImageView>(R.id.close)

        rvLanguage.setHasFixedSize(true)
        rvLanguage.layoutManager = GridLayoutManager(this,3)
        languageOptions = AdapterLanguageOptions(this,languageModel)

        rvLanguage.adapter = languageOptions
        languageOptions.notifyDataSetChanged()

        close.setOnClickListener{
            dialogReferral?.dismiss()
        }
        dialogReferral?.show()
    }

    private fun reloadLanguage(){
        val langCode= MyAppPreferences.getInstance(this).getlanguage()
        when (langCode) {
            "en" -> tvSelectLanguage.text = getString(R.string.englishTitle)
            "da" -> tvSelectLanguage.text = getString(R.string.denish)
            "sv" -> tvSelectLanguage.text = getString(R.string.swedish)
        }


    }


    private fun openFullWidthPopup(){

        dialog = Dialog(this@Login_Activity as Context)
        val customLayout = layoutInflater.inflate(R.layout.dialog_country_code_selection, null)
        dialog.setContentView(customLayout!!)
        Objects.requireNonNull<Window>(dialog.getWindow())
            .setBackgroundDrawableResource(android.R.color.transparent)
        val window: Window? = dialog.window
        window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)

         var img_cross=customLayout.findViewById<ImageView>(R.id.imgCross)
        var rlCountyCode=customLayout.findViewById<RecyclerView>(R.id.rlCountryCode)
        var etSponseeLabel=customLayout.findViewById<EditText>(R.id.etSponseeLabel)


        // recycler view setup with adapter
        rlCountyCode.setHasFixedSize(true)
        rlCountyCode.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false )

        countryCodeAdapter = CountryCodeAdapter(this, countryCodeList)
        rlCountyCode.adapter = countryCodeAdapter
        countryCodeAdapter.notifyDataSetChanged()


        etSponseeLabel.addTextChangedListener(object :
            TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val handler = Handler().postDelayed({
                    filterCategories(p0.toString())
                }, 100)

            }
        })

        img_cross.setOnClickListener({
            dialog.dismiss()
        })

        dialog.show()
    }

    fun filterCategories(name: String){
        val filteredCategories = ArrayList<CountryCodeModel>()
        for(search in countryCodeList){
            val searchData = search.country_name
            if(searchData.toLowerCase().contains(name.toLowerCase())){
                filteredCategories.add(search)
            }
        }
        countryCodeAdapter.filterData(filteredCategories)
    }

    private fun setListner(){
//        vv.setOnCompletionListener {
       //     vv.start()
       // }

            etPhone.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    var phone = p0!!.toString()

                    if (countryCode == "+45") {
                        if (phone.length == 8) {
                            tvLogin.isEnabled = true
                            tvLogin.background =
                                this@Login_Activity.resources.getDrawable(R.drawable.button_red_rippel_effect)
                            svLoginRegister.shadowColor =
                                this@Login_Activity.resources.getColor(R.color.livo_light_red)
                        } else {
                            tvLogin.isEnabled = false
                            tvLogin.background =
                                this@Login_Activity.resources.getDrawable(R.drawable.button_grey_ripple_effect)
                            svLoginRegister.shadowColor =
                                this@Login_Activity.resources.getColor(R.color.grey_100)
                        }

                    }
                    else if ( countryCode == "+46") {
                        if (phone.length == 9) {
                            tvLogin.isEnabled = true
                            tvLogin.background =
                                this@Login_Activity.resources.getDrawable(R.drawable.button_red_rippel_effect)
                            svLoginRegister.shadowColor =
                                this@Login_Activity.resources.getColor(R.color.livo_light_red)
                        } else {
                            tvLogin.isEnabled = false
                            tvLogin.background =
                                this@Login_Activity.resources.getDrawable(R.drawable.button_grey_ripple_effect)
                            svLoginRegister.shadowColor =
                                this@Login_Activity.resources.getColor(R.color.grey_100)
                        }

                    }
                    else if (countryCode == "+91") {
                        if (phone.length == 10) {
                            tvLogin.isEnabled = true
                            tvLogin.background =
                                this@Login_Activity.resources.getDrawable(R.drawable.button_red_rippel_effect)
                            svLoginRegister.shadowColor =
                                this@Login_Activity.resources.getColor(R.color.livo_light_red)
                        } else {
                            tvLogin.isEnabled = false
                            tvLogin.background =
                                this@Login_Activity.resources.getDrawable(R.drawable.button_grey_ripple_effect)
                            svLoginRegister.shadowColor =
                                this@Login_Activity.resources.getColor(R.color.grey_100)
                        }
                    }

                }
            })


        tvLogin.setOnClickListener({

            var android_id = Settings.Secure.getString(this.contentResolver,
                Settings.Secure.ANDROID_ID)

            var ty=tvLogin.text
            if (ty.equals(resources.getString(R.string.login)) || ty.equals(resources.getString(R.string.register)))
            {
                    var phone = etPhone.text.toString()
                    val langCode = MyAppPreferences.getInstance(this).getlanguage()

                if (etOTP.text.toString().isEmpty())
                {}
                else {
                    loginViewModel?.let {
                        if (this.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                            showProgressBar()
                            val device_token = PrefManager.getInstance(this@Login_Activity).getPreference(MyAppSession.FCM_TOKEN,"abc123")

                            val jsonObject = JsonObject()
                            jsonObject.addProperty("country_code", countryCode)
                            jsonObject.addProperty("phone_number", phone)
                            jsonObject.addProperty("lang", langCode)
                            jsonObject.addProperty("otp", etOTP.text.toString())
                            jsonObject.addProperty("os","android")
                            jsonObject.addProperty("device_token", device_token)
                            jsonObject.addProperty("device_id", android_id)
                            it.createSession(jsonObject)
                        }
                    }
                }
            }

            else {
                var phone = etPhone.text.toString()
                val langCode = MyAppPreferences.getInstance(this).getlanguage()
                rlSelectLanguage.isEnabled=false
                    loginViewModel?.let {
                        if (this.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                            showProgressBar()
                            // firstTime = true
                            //val device_token = PrefManager.getInstance(this).getPreference(MyAppSession.FCM_TOKEN,"")
                            val jsonObject = JsonObject()
                            jsonObject.addProperty("country_code", countryCode)
                            jsonObject.addProperty("phone_number", phone)
                            jsonObject.addProperty("lang", langCode)
                            it.getOtp(jsonObject)
                            // startSMSListener()
                        }
                    }


            }
        })

        tvResendOtp.setOnClickListener({
            var phone = etPhone.text.toString()
            val langCode = MyAppPreferences.getInstance(this).getlanguage()

            loginViewModel?.let {
                if (this.let { ctx -> AndroidUtil.isInternetAvailable(ctx) }) {
                    showProgressBar()
                    // firstTime = true
                    //val device_token = PrefManager.getInstance(this).getPreference(MyAppSession.FCM_TOKEN,"")
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("country_code", countryCode)
                    jsonObject.addProperty("phone_number", phone)
                    jsonObject.addProperty("lang", langCode)
                    it.getOtp(jsonObject)
                    // startSMSListener()
                }
            }
        })
    }

    fun setSelectedCountryCode(model: CountryCodeModel) {
        tvDialCode.text=model.country_code
        countryCode=model.country_code
        //etPhone.setText("")
        var phone:String=etPhone.text.toString()

        if (countryCode=="+45" ) {

            if (phone.length==8)
            {
                tvLogin.isEnabled = true
                tvLogin.background = this.resources.getDrawable(R.drawable.button_red_rippel_effect)
                svLoginRegister.shadowColor = this.resources.getColor(R.color.livo_light_red)
            } else {
                tvLogin.isEnabled = false
                tvLogin.background =
                    this.resources.getDrawable(R.drawable.button_grey_ripple_effect)
                svLoginRegister.shadowColor = this.resources.getColor(R.color.grey_100)
            }

        }
        else if (countryCode=="+46") {

            if (phone.length == 9) {
                tvLogin.isEnabled = true
                tvLogin.background = this.resources.getDrawable(R.drawable.button_red_rippel_effect)
                svLoginRegister.shadowColor = this.resources.getColor(R.color.livo_light_red)
            } else {
                tvLogin.isEnabled = false
                tvLogin.background =
                    this.resources.getDrawable(R.drawable.button_grey_ripple_effect)
                svLoginRegister.shadowColor = this.resources.getColor(R.color.grey_100)
            }

        }
        else if(countryCode == "+91"){
            if(phone.length == 10){
                tvLogin.isEnabled = true
                tvLogin.background = this.resources.getDrawable(R.drawable.button_red_rippel_effect)
                svLoginRegister.shadowColor = this.resources.getColor(R.color.livo_light_red)
            }else{
                tvLogin.isEnabled = false
                tvLogin.background = this.resources.getDrawable(R.drawable.button_grey_ripple_effect)
                svLoginRegister.shadowColor = this.resources.getColor(R.color.grey_100)
            }
        }

        dialog.dismiss()
    }

    fun click(model: CountryCodeModel) {

        tvApply.setOnClickListener{
            if(!model.language_code.equals(MyAppPreferences.getInstance(this).getlanguage())){
                MyAppPreferences.getInstance(this).save_selected_lang(model.language_code)
               // MyAppPreferences.getInstance(this).saveLanguageId(langId)
              //  etPhone.setText("")
                recreate()
            }
            dialogReferral?.dismiss()
        }
    }

    fun showProgressBar(){
        dialog =  AppUtils.showProgress(this)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog)
    }

     fun observers(){

        loginViewModel?.getMutableLiveDataOtp()?.observe(this, Observer {

           // AppUtils.showToast(this,R.drawable.check,it.message,R.color.success_green,R.color.white,R.color.white)
            hideProgressBar()
            if (it.data.is_exists)
            tvLogin.text=getString(R.string.login)
            else if (!it.data.is_exists)
                tvLogin.text=getString(R.string.register)

            rlotp.visibility=View.VISIBLE


            timerc=0
            var oo=it.data.timeout
            val countDownInterval:Long = 1000
            val millisInFuture: Long = (1000 * oo.toLong())
            timer(millisInFuture, countDownInterval).cancel()
            timer(millisInFuture, countDownInterval).start()
        })

         loginViewModel?.getmutableLiveDataCreateSession()?.observe(this, Observer {
             hideProgressBar()

             timerc=1
             if(SessionManager.getLoginModel()!= null){
             }
             Prefs.get().clearAllData()
             Prefs.get().uuid(it.data.pubnub_uuid)

             var i=Intent(applicationContext,HomeActivity::class.java)
             startActivity(i)
             finish()

            // AppUtils.showToast(this,R.drawable.check,it.message+tt,R.color.success_green,R.color.white,R.color.white)

         })

         loginViewModel?.getMutableLiveDataExtraDataModel()?.observe(this, Observer {
             Log.e("che",it.data.colors.toString())
         })

         loginViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
             hideProgressBar()
             //dialogReferral?.dismiss()

             AppUtils.showToast(this,R.drawable.cross,it.message,R.color.error_red,R.color.white,R.color.white)


         })
     }

    private fun timer(millisInFuture:Long,countDownInterval:Long): CountDownTimer {
        return object: CountDownTimer(millisInFuture,countDownInterval){
            override fun onTick(millisUntilFinished: Long){
                //val timeRemaining = timeString(millisUntilFinished)
              tvResendOtp.visibility=View.VISIBLE
                tvResendOtp.isEnabled=false
                etPhone.isEnabled=false
                tvResendOtp.text=applicationContext.resources.getString(R.string.resend_otp_in) + millisUntilFinished/1000+" "+ applicationContext.resources.getString(R.string.seconds)
            }
            override fun onFinish() {
                //rlotp.visibility=View.GONE
                if (timerc==0) {
                    tvResendOtp.isEnabled=true
                    tvResendOtp.text=applicationContext.resources.getString(R.string.resend_otp)
                    tvResendOtp.visibility = View.VISIBLE
                    etPhone.isEnabled=true
                }
            }
        }
    }

    override fun onResume() {
        //vv.start()
        super.onResume()
    }

    protected override fun setStatusBar() {
        val mColor = resources.getColor(R.color.white)
        StatusBarUtil.setColor(this, mColor,40)
    }


    private fun openBottomPopup(){

        bottomsheetlanguagecode = BottomSheetDialog(this)
        var bottomSheetLanguageDialogBinding =
            DataBindingUtil.inflate<BottomSheetlanguagecodeBinding>(
                LayoutInflater.from(this),
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
        var lang = MyAppPreferences.getInstance(applicationContext).getlanguage()

        Handler().postDelayed({

            en_width= imgEnglishLang?.width!!
            en_height=imgEnglishLang?.height!!
            dn_width=imgDanishLang?.width!!
            dn_height=imgDanishLang?.height!!
            sv_width=imgDenmarkLang?.width!!
            sv_height=imgDenmarkLang?.height!!

            if (lang.equals("en"))
            {
                var cw = (imgEnglishLang?.getWidth() * 15) / 100
                var ch = (imgEnglishLang?.getHeight() * 15) / 100
                var width = imgEnglishLang?.getWidth() + cw // ((display.getWidth()*20)/100)
                var height = imgEnglishLang?.getHeight() + ch // ((display.getHeight()*30)/100)
                var parms = LinearLayout.LayoutParams(width, height)
                imgEnglishLang?.setLayoutParams(parms)

                imgEnglishLang?.setBorderColor(resources.getColor(R.color.colorPrimary))
                imgEnglishLang?.setBorderWidth(resources.getDimension(R.dimen._3sdp))

                bottomSheetLanguageDialogBinding.tvEnglishLang.setTextColor(resources.getColor(R.color.livo_heading_black))
            }
            else if(lang.equals("da"))
            {
                var cw = (imgDanishLang?.getWidth() * 15) / 100
                var ch = (imgDanishLang?.getHeight() * 15) / 100
                var width = imgDanishLang?.getWidth() + cw // ((display.getWidth()*20)/100)
                var height = imgDanishLang?.getHeight() + ch // ((display.getHeight()*30)/100)
                var parms = LinearLayout.LayoutParams(width, height)
                imgDanishLang?.setLayoutParams(parms)

                imgDanishLang?.setBorderColor(resources.getColor(R.color.colorPrimary))
                imgDanishLang?.setBorderWidth(resources.getDimension(R.dimen._3sdp))

                bottomSheetLanguageDialogBinding.tvDanishLang.setTextColor(resources.getColor(R.color.livo_heading_black))
            }
            else if(lang.equals("sv")){
                var cw = (imgDenmarkLang?.getWidth() * 15) / 100
                var ch = (imgDenmarkLang?.getHeight() * 15) / 100
                var width = imgDenmarkLang?.getWidth() + cw // ((display.getWidth()*20)/100)
                var height = imgDenmarkLang?.getHeight() + ch // ((display.getHeight()*30)/100)
                var parms = LinearLayout.LayoutParams(width, height)
                imgDenmarkLang?.setLayoutParams(parms)

                imgDenmarkLang?.setBorderColor(resources.getColor(R.color.colorPrimary))
                imgDenmarkLang?.setBorderWidth(resources.getDimension(R.dimen._3sdp))

                bottomSheetLanguageDialogBinding.tvDenmarkLang.setTextColor(resources.getColor(R.color.livo_heading_black))
            }

        },500)



        imgDanishLang?.setOnClickListener({

            lang="da"

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

            bottomSheetLanguageDialogBinding.tvDanishLang.setTextColor(resources.getColor(R.color.livo_heading_black))
            bottomSheetLanguageDialogBinding.tvDenmarkLang.setTextColor(resources.getColor(R.color.black_40_opacity))
            bottomSheetLanguageDialogBinding.tvEnglishLang.setTextColor(resources.getColor(R.color.black_40_opacity))
        })

        imgEnglishLang?.setOnClickListener({
            lang="en"
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

            bottomSheetLanguageDialogBinding.tvDanishLang.setTextColor(resources.getColor(R.color.black_40_opacity))
            bottomSheetLanguageDialogBinding.tvDenmarkLang.setTextColor(resources.getColor(R.color.black_40_opacity))
            bottomSheetLanguageDialogBinding.tvEnglishLang.setTextColor(resources.getColor(R.color.livo_heading_black))
        })
        imgDenmarkLang?.setOnClickListener({
            lang="sv"
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

            bottomSheetLanguageDialogBinding.tvDanishLang.setTextColor(resources.getColor(R.color.black_40_opacity))
            bottomSheetLanguageDialogBinding.tvDenmarkLang.setTextColor(resources.getColor(R.color.livo_heading_black))
            bottomSheetLanguageDialogBinding.tvEnglishLang.setTextColor(resources.getColor(R.color.black_40_opacity))
        })

        llConfirmChange?.setOnClickListener({

            MyAppPreferences.getInstance(this).save_selected_lang(lang)
            recreate()
            bottomsheetlanguagecode?.dismiss()

        })
        llCancel?.setOnClickListener({
            bottomsheetlanguagecode?.dismiss()
        })

        bottomsheetlanguagecode?.show()


    }

}


