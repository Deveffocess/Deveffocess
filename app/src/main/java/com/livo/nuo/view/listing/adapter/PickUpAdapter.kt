package com.livo.nuo.view.listing.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.livo.nuo.R
import com.livo.nuo.models.DateDayModel
import com.livo.nuo.view.product.ProductDetailActivity
import com.livo.nuo.view.listing.NewListingActivity
import java.util.*
import kotlin.collections.ArrayList

class PickUpAdapter (
    private var currAtivity: Activity,
    private var list: ArrayList<DateDayModel>,private var from:Int
) :
    RecyclerView.Adapter<PickUpAdapter.ViewHolder>() {


lateinit var pref:SharedPreferences
    var dat:String=""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_date, parent, false)
        return ViewHolder(view)

    }

    private var isSelected = 0
    override fun getItemViewType(position: Int): Int {
       return position
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list.size
    }


    fun getItem(position: Int): DateDayModel {
        return list.get(position)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        holder.tvDate.text=model.date

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, model.day.toInt())
        val days =arrayOf(currAtivity.resources.getString(R.string.sun), currAtivity.resources.getString(R.string.mon),
            currAtivity.resources.getString(R.string.tue), currAtivity.resources.getString(R.string.wed),
            currAtivity.resources.getString(R.string.thu), currAtivity.resources.getString(R.string.fri)
            , currAtivity.resources.getString(R.string.sat))

        holder.tvDay.text= days[model.day.toInt()]

        if (from==0) {
            pref = currAtivity.getSharedPreferences("PickUp", Context.MODE_PRIVATE)
             dat = pref.getString("date", "")!!
        }
        else{
            pref = currAtivity.getSharedPreferences("DropOff", Context.MODE_PRIVATE)
            dat = pref.getString("date", "")!!
        }

        if (days[model.day.toInt()].equals(currAtivity.resources.getString(R.string.sun)))
        {
            holder.rlMain.background = currAtivity.resources.getDrawable(R.drawable.grey_date_shape)
            holder.tvDay.setTextColor(currAtivity.resources.getColor(R.color.livo_black_30_opacity))
            holder.tvDate.setTextColor(currAtivity.resources.getColor(R.color.livo_black_30_opacity))
            holder.rlMain.isEnabled=false
        }
        else{
            if (from==0) {
                if (dat.equals(model.fullDate)) {
                    holder.rlMain.background =
                        currAtivity.resources.getDrawable(R.drawable.selected_date_shape)
                    holder.tvDay.setTextColor(currAtivity.resources.getColor(R.color.white))
                    holder.tvDate.setTextColor(currAtivity.resources.getColor(R.color.white))
                    holder.rlMain.isEnabled = true
                } else {
                    holder.rlMain.background =
                        currAtivity.resources.getDrawable(R.drawable.white_date_shape)
                    holder.tvDay.setTextColor(currAtivity.resources.getColor(R.color.livo_black_30_opacity))
                    holder.tvDate.setTextColor(currAtivity.resources.getColor(R.color.black))
                    holder.rlMain.isEnabled = true
                }
            }
            else{
                if (dat.equals(model.fullDate)) {
                    holder.rlMain.background = currAtivity.resources.getDrawable(R.drawable.red_date_shape)
                    holder.tvDay.setTextColor(currAtivity.resources.getColor(R.color.white))
                    holder.tvDate.setTextColor(currAtivity.resources.getColor(R.color.white))
                    holder.rlMain.isEnabled = true
                } else {
                    holder.rlMain.background =
                        currAtivity.resources.getDrawable(R.drawable.white_date_shape)
                    holder.tvDay.setTextColor(currAtivity.resources.getColor(R.color.livo_black_30_opacity))
                    holder.tvDate.setTextColor(currAtivity.resources.getColor(R.color.black))
                    holder.rlMain.isEnabled = true
                }
            }

        }

        holder.rlMain.setOnClickListener({

            (currAtivity as NewListingActivity).showButtonNext()

            if (isSelected != holder.getAdapterPosition()) {
                notifyItemChanged(isSelected)
                isSelected = holder.getAdapterPosition()
            }

            if(from == 0){ //pickup
                if(isSelected == position) {
                        holder.rlMain.background =currAtivity.resources.getDrawable(R.drawable.selected_date_shape)
                        holder.tvDay.setTextColor(currAtivity.resources.getColor(R.color.white))
                        holder.tvDate.setTextColor(currAtivity.resources.getColor(R.color.white))

                    val editor = pref.edit()
                    editor.putString("date", model.fullDate)
                    editor.commit()
                }
                else{
                        holder.rlMain.background = currAtivity.resources.getDrawable(R.drawable.white_date_shape)
                        holder.tvDay.setTextColor(currAtivity.resources.getColor(R.color.livo_black_30_opacity))
                        holder.tvDate.setTextColor(currAtivity.resources.getColor(R.color.black))
                }
            }else{//drop off
                if(isSelected == position) {
                    holder.rlMain.background =
                        currAtivity.resources.getDrawable(R.drawable.red_date_shape)
                    holder.tvDay.setTextColor(currAtivity.resources.getColor(R.color.white))
                    holder.tvDate.setTextColor(currAtivity.resources.getColor(R.color.white))

                    val editor = pref.edit()
                    editor.putString("date", model.fullDate)
                    editor.commit()
                }
                else{
                    holder.rlMain.background = currAtivity.resources.getDrawable(R.drawable.white_date_shape)
                    holder.tvDay.setTextColor(currAtivity.resources.getColor(R.color.livo_black_30_opacity))
                    holder.tvDate.setTextColor(currAtivity.resources.getColor(R.color.black))
                }
            }

        })



    }

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        var tvDay: TextView
        var tvDate: TextView
        var rlMain:RelativeLayout

        init {
            tvDay=ItemView.findViewById(R.id.tvDay)
            tvDate=ItemView.findViewById(R.id.tvDate)
            rlMain=ItemView.findViewById(R.id.rlMain)
        }
    }


}