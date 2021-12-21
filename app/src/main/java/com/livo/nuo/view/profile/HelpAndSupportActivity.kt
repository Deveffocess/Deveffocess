package com.livo.nuo.view.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.jaeger.library.StatusBarUtil
import com.livo.nuo.R
import com.livo.nuo.utility.LocalizeActivity
import java.util.*

class HelpAndSupportActivity : LocalizeActivity() {

    lateinit var imgBack:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_and_support)

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