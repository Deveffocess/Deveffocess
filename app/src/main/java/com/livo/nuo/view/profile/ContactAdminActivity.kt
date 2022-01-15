package com.livo.nuo.view.profile

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.jaeger.library.StatusBarUtil
import com.livo.nuo.R
import com.livo.nuo.manager.SessionManager
import com.livo.nuo.models.ExtraDataModel
import com.livo.nuo.utility.LocalizeActivity

class ContactAdminActivity : LocalizeActivity() {

    lateinit var imgBack:ImageView
    lateinit var btnEmail:TextView
    lateinit var btnPhone:TextView
    lateinit var btnChatWithUs:TextView

    var extraDataModel=ExtraDataModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_admin)

        imgBack=findViewById(R.id.imgBack)
        btnEmail=findViewById(R.id.btnEmail)
        btnPhone=findViewById(R.id.btnPhone)
        btnChatWithUs=findViewById(R.id.btnChatWithUs)

        initViews()

    }

    fun initViews(){

        extraDataModel= SessionManager.getExtraDataModel()!!

        imgBack.setOnClickListener({
            onBackPressed()
        })

        btnPhone.setOnClickListener({
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${extraDataModel.data.phone}"))
            startActivity(intent)
        })

        btnEmail.setOnClickListener({
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(extraDataModel.data.email))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Livo")
            intent.putExtra(Intent.EXTRA_TEXT, "")
            startActivity(Intent.createChooser(intent, ""))
        })


        btnChatWithUs.setOnClickListener({
            var intent: Intent
            try {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(extraDataModel.data.facebook_url)) //"fb-messenger://$myapp"
            } catch (e: ActivityNotFoundException) {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(extraDataModel.data.facebook_url))
            }

            startActivity(intent)
        })

    }

    override fun setStatusBar() {
        val mColor = resources.getColor(R.color.white)
        StatusBarUtil.setColor(this, mColor,40)
    }

}