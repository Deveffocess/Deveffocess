package com.livo.nuo.view.home.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.ShimmerFrameLayout
import com.livo.nuo.R
import com.livo.nuo.models.ChatMessageModel
import com.livo.nuo.view.message.ChatActivity


class MessageCurrentAdapter (
    private var currAtivity: Activity,
    private var list: ArrayList<ChatMessageModel>, var status:String
) :
    RecyclerView.Adapter<MessageCurrentAdapter.ViewHolder>() {

    private val LOADING = 0
    private val ITEM = 1
    private var isLoadingAdded = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_chat_message, parent, false)
        return ViewHolder(view)


    }



    override fun getItemViewType(position: Int): Int {
        return if (position == list.size - 1 && isLoadingAdded) LOADING else ITEM

//        return super.getItemViewType(position)

    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list.size
    }


    fun getItem(position: Int): ChatMessageModel {
        return list.get(position)
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        holder.tvUserName.text=model.user_data.first_name+" "+model.user_data.last_name
        holder.tvTime.text=model.channel_details.time

        if(model.channel_details.message.length > 30){
            holder.tvLastMessage.text = model.channel_details.message.substring(0,30)+"..."

        }else{
            holder.tvLastMessage.text = model.channel_details.message
        }


        Glide.with(currAtivity).addDefaultRequestListener(object : RequestListener<Any> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Any>?,
                isFirstResource: Boolean
            ): Boolean {
                holder.shimmerImagex.visibility = View.VISIBLE
                holder.shimmerImagex.startShimmer()
                return false
            }

            override fun onResourceReady(
                resource: Any?,
                model: Any?,
                target: Target<Any>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                holder.shimmerImagex.visibility = View.GONE
                holder.imgshimmerImagex.visibility = View.GONE
                holder.imgshimmerImagex.visibility = View.VISIBLE
                holder.shimmerImagex.stopShimmer()
                return false
            }

        })
            .load(model.listing_image).placeholder(currAtivity.getDrawable(R.drawable.grey_round_shape)).
            error(currAtivity.getDrawable(R.drawable.grey_round_shape)).
            into(holder.imgProductImage)


        Glide.with(currAtivity).addDefaultRequestListener(object : RequestListener<Any> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Any>?,
                isFirstResource: Boolean
            ): Boolean {
                holder.shimmerImagex1.visibility = View.VISIBLE
                holder.shimmerImagex1.startShimmer()
                return false
            }

            override fun onResourceReady(
                resource: Any?,
                model: Any?,
                target: Target<Any>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                holder.shimmerImagex1.visibility = View.GONE
                holder.imgshimmerImagex1.visibility = View.GONE
                holder.imgshimmerImagex1.visibility = View.VISIBLE
                holder.shimmerImagex1.stopShimmer()
                holder.imgProductImage.visibility=View.VISIBLE
                return false
            }

        })
            .load(model.user_data.profile_image).placeholder(currAtivity.getDrawable(R.drawable.grey_round_shape)).
            error(currAtivity.getDrawable(R.drawable.grey_round_shape)).
            into(holder.imgUserImage)

        holder.llMain.setOnClickListener({
            var i=Intent(currAtivity,ChatActivity::class.java)
            i.putExtra("ch",model.channel_id)
            i.putExtra("st",status)
            currAtivity.startActivity(i)
        })



    }

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        var imgProductImage: ImageView
        var imgUserImage:ImageView
        var tvUserName: TextView
        var tvTime: TextView
        var tvLastMessage: TextView

        var llMain:LinearLayout
        var shimmerImagex:ShimmerFrameLayout
        var imgshimmerImagex:ImageView
        var shimmerImagex1:ShimmerFrameLayout
        var imgshimmerImagex1:ImageView

        init {

            tvUserName = ItemView.findViewById(R.id.tvUserName)
            imgProductImage=ItemView.findViewById(R.id.imgProductImage)
            imgUserImage=ItemView.findViewById(R.id.imgUserImage)
            tvTime=ItemView.findViewById(R.id.tvTime)
            tvLastMessage=ItemView.findViewById(R.id.tvLastMessage)
            llMain=ItemView.findViewById(R.id.llMain)

            shimmerImagex=ItemView.findViewById(R.id.shimmerImagex)
            imgshimmerImagex=ItemView.findViewById(R.id.imgshimmerImagex)

            shimmerImagex1=ItemView.findViewById(R.id.shimmerImagex1)
            imgshimmerImagex1=ItemView.findViewById(R.id.imgshimmerImagex1)

        }
    }


}