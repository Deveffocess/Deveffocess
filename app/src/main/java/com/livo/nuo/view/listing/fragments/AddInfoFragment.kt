package com.livo.nuo.view.listing.fragments

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import com.livo.nuo.R
import com.livo.nuo.view.home.homefragments.MyListingFragment
import android.widget.TextView.BufferType

import android.text.style.ForegroundColorSpan

import android.text.SpannableString

import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.livo.nuo.utility.AndroidUtil
import android.text.Editable

import android.text.TextWatcher
import android.widget.TextView
import com.livo.nuo.view.listing.NewListingActivity


class AddInfoFragment : Fragment() {

    lateinit var customSwitch:Switch
    lateinit var etHeight:EditText
    lateinit var etDepth:EditText
    lateinit var etWidth:EditText
    lateinit var etWeight:EditText
    lateinit var etProductName:EditText
    lateinit var tvProductNameCount:TextView
    lateinit var rlProductHeight:RelativeLayout
    lateinit var rlProductDepth:RelativeLayout
    lateinit var rlProductWidth:RelativeLayout
    lateinit var rlProductWeight:RelativeLayout

    private var currActivity : Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root= inflater.inflate(R.layout.fragment_add_info, container, false)

        customSwitch=root.findViewById(R.id.customSwitch)
        etHeight=root.findViewById(R.id.etHeight)
        etDepth=root.findViewById(R.id.etDepth)
        etWidth=root.findViewById(R.id.etWidth)
        etWeight=root.findViewById(R.id.etWeight)
        tvProductNameCount=root.findViewById(R.id.tvProductNameCount)
        etProductName=root.findViewById(R.id.etProductName)
        rlProductHeight=root.findViewById(R.id.rlProductHeight)
        rlProductDepth=root.findViewById(R.id.rlProductDepth)
        rlProductWidth=root.findViewById(R.id.rlProductWidth)
        rlProductWeight=root.findViewById(R.id.rlProductWeight)

        initViews()

        return root
    }

    companion object {
        fun newInstance() : Fragment{
            val f = AddInfoFragment()
            return f
        }
    }

    fun initViews(){

        currActivity = requireActivity()

        customSwitch.setOnClickListener({
            if (customSwitch.isChecked==true)
            {
                customSwitch.trackDrawable=resources.getDrawable(R.drawable.track_green)
                customSwitch.thumbDrawable=resources.getDrawable(R.drawable.thumb_green)
                (currActivity as NewListingActivity).isTwoPeople = true
            }
            else {
                customSwitch.trackDrawable=resources.getDrawable(R.drawable.track_grey)
                customSwitch.thumbDrawable=resources.getDrawable(R.drawable.thumb_blue)
                (currActivity as NewListingActivity).isTwoPeople = false
            }
        })


        etHeight.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) { }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                (currActivity as NewListingActivity).height = s!!.toString()
                checkCon()
            }
        })

        etWeight.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) { }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                (currActivity as NewListingActivity).weight = s!!.toString()
                checkCon()
            }
        })

        etWidth.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) { }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                (currActivity as NewListingActivity).width = s!!.toString()
                checkCon()
            }
        })

        etDepth.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) { }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                (currActivity as NewListingActivity).depth = s!!.toString()
                checkCon()
            }
        })

        etProductName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) { }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                (currActivity as NewListingActivity).productTitle = s!!.toString()
                tvProductNameCount.text = ""+  (currActivity as NewListingActivity).productTitle.length+activity!!.resources.getString(R.string.count_characters)
                checkCon()
            }
        })


        rlProductHeight.setOnClickListener({ v->
            etHeight.requestFocus()
            val imm: InputMethodManager =
                currActivity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        })

        rlProductDepth.setOnClickListener({ v->
            etDepth.requestFocus()
            val imm: InputMethodManager =
                currActivity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        })

        rlProductWidth.setOnClickListener({ v->
            etWidth.requestFocus()
            val imm: InputMethodManager =
                currActivity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        })

        rlProductWeight.setOnClickListener({ v->
            etWeight.requestFocus()
            val imm: InputMethodManager =
                currActivity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        })

        checkCon()
        (currActivity as NewListingActivity).hideButtonNext()
    }

    override fun onResume() {
        checkCon()
        super.onResume()
    }

    fun checkCon(){
        if (etProductName.text.isEmpty())
        {(currActivity as NewListingActivity).hideButtonNext()}
        else{
           if (etHeight.text.isEmpty()){(currActivity as NewListingActivity).hideButtonNext()}
           else{
               if (etWeight.text.isEmpty()){(currActivity as NewListingActivity).hideButtonNext()}
               else{
                   if(etWidth.text.isEmpty()){(currActivity as NewListingActivity).hideButtonNext()}
                   else{
                       if(etDepth.text.isEmpty()){(currActivity as NewListingActivity).hideButtonNext()}
                       else{
                           (currActivity as NewListingActivity).showButtonNext()
                       }
                   }
               }
           }
        }
    }
}