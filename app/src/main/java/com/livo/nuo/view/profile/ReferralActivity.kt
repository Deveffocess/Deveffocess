package com.livo.nuo.view.profile


import android.os.Bundle
import android.widget.ImageView
import com.jaeger.library.StatusBarUtil
import com.livo.nuo.R
import com.livo.nuo.utility.LocalizeActivity

class ReferralActivity : LocalizeActivity() {

    lateinit var imgBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_referral)

        imgBack=findViewById(R.id.imgBack)

        initViews()
    }

    fun initViews(){
        imgBack.setOnClickListener({
            finish()
        })
    }

    override fun setStatusBar() {
        val mColor = resources.getColor(R.color.white)
        StatusBarUtil.setColor(this, mColor,40)
    }

}