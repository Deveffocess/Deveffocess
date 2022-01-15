package com.livo.nuo.view.ongoing.adapters

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.JsonObject
import com.livo.nuo.R
import com.livo.nuo.models.TranspoterBiddingDataModel
import com.livo.nuo.utility.AndroidUtil
import com.livo.nuo.utility.AppUtils
import com.livo.nuo.view.message.ChatActivity
import com.livo.nuo.view.ongoing.ListingOngoingStateActivity
import com.livo.nuo.view.ongoing.TransporterOffersActivity
import com.livo.nuo.viewModel.ViewModelFactory
import com.livo.nuo.viewModel.ongoingstate.OngoingStateModel
import kotlin.collections.ArrayList

class TransporterOfferAdapter(
    private var currAtivity: Activity,
    private var list: ArrayList<TranspoterBiddingDataModel>) :
    RecyclerView.Adapter<TransporterOfferAdapter.ViewHolder>() {

    var channel_id=""
    private var ongoingViewModel : OngoingStateModel? = null
    private lateinit var dialog: Dialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_transporter_offers, parent, false)
        return ViewHolder(view)

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list.size
    }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        holder.tvPrice.text=model.bid_amount.toString()+" KR"

        holder.tvTitle.text=model.transporter.first_name+" "+model.transporter.last_name

        Glide.with(currAtivity).addDefaultRequestListener(object : RequestListener<Any> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Any>?,
                isFirstResource: Boolean
            ): Boolean {
                holder.shimmerImage.visibility = View.VISIBLE
//                        holder.itemView.imgProductImagex.visibility = View.INVISIBLE
                holder.shimmerImage.startShimmer()
                return false
            }

            override fun onResourceReady(
                resource: Any?,
                model: Any?,
                target: Target<Any>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                holder.shimmerImage.visibility = View.GONE
//                        holder.itemView.imgProductImagex.visibility = View.VISIBLE
                holder.shimmerImage.stopShimmer()
                return false
            }

        })
            .load(model.transporter.profile_image).placeholder(currAtivity.getDrawable(R.drawable.grey_round_shape)).
            error(currAtivity.getDrawable(R.drawable.grey_round_shape)).
            into(holder.imgProductImage)

        holder.rlRemove.setOnClickListener({v->
            try {
                (v.context as TransporterOffersActivity).Callback(model.id)
            } catch (e: Exception) {
                // ignore
            }
        })

        holder.rlTransporter.setOnClickListener({
            val intent = Intent(currAtivity, ListingOngoingStateActivity::class.java)
            intent.putExtra("id",model.id)
            currAtivity.startActivity(intent)
            currAtivity.finish()
        })

        holder.rlMessage.setOnClickListener({
            channel_id=model.channel_id
            if(channel_id.isEmpty()) {

                channel_id=model.id.toString()
                showProgressBar()
                ongoingViewModel?.let {
                    if (currAtivity.let { ctx -> AndroidUtil.isInternetAvailable(ctx!!) } == true) {
                        var jsonObject =  JsonObject();
                        jsonObject.addProperty("offer_id", model.id)
                        it.getAddChannel(jsonObject)
                    }
                }

            }
            else{
                var i=Intent(currAtivity,ChatActivity::class.java)
                i.putExtra("ch",channel_id)
                i.putExtra("st","")
                currAtivity.startActivity(i)
            }
        })

        observers()
    }

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        var tvTitle: TextView
        var tvPrice:TextView
        var imgProductImage: ImageView
        var rlRemove: RelativeLayout
        var rlMessage:RelativeLayout
        var shimmerImage:ShimmerFrameLayout
        var rlTransporter:RelativeLayout

        init {
            tvTitle=ItemView.findViewById(R.id.tvTitle)
            tvPrice=ItemView.findViewById(R.id.tvPrice)
            imgProductImage=ItemView.findViewById(R.id.imgProductImage)
            rlRemove=ItemView.findViewById(R.id.rlRemove)
            shimmerImage=ItemView.findViewById(R.id.shimmerImage)
            rlTransporter=ItemView.findViewById(R.id.rlTransporter)
            rlMessage=ItemView.findViewById(R.id.rlMessage)

            currAtivity?.application?.let {
                ongoingViewModel = ViewModelProvider(
                    ViewModelStore(),
                    ViewModelFactory(it)
                ).get(OngoingStateModel::class.java)
            }

        }
    }

    fun observers(){
        ongoingViewModel?.getMutableLiveDataAddChannel()
            ?.observe(currAtivity as LifecycleOwner, androidx.lifecycle.Observer {

                hideProgressBar()
                channel_id= it.data.channel_id

                var i=Intent(currAtivity,ChatActivity::class.java)
                i.putExtra("ch",channel_id)
                i.putExtra("st","")
                currAtivity.startActivity(i)
            })

        ongoingViewModel?.getErrorMutableLiveData()?.observe(currAtivity as LifecycleOwner, androidx.lifecycle.Observer {
            hideProgressBar()
            AppUtils.showToast(currAtivity,R.drawable.cross,it.message,R.color.error_red,R.color.white,R.color.white)
        })
    }

    fun showProgressBar(){
        dialog =  AppUtils.showProgress(currAtivity)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog)
    }

}