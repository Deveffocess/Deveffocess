package com.livo.nuo.view.home.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.card.MaterialCardView
import com.livo.nuo.R
import com.livo.nuo.models.ProductDataModel
import com.livo.nuo.view.product.ProductDetailActivity

class ListingAdapter (
    private var currAtivity: Activity,
    private var list: ArrayList<ProductDataModel>
) :
    RecyclerView.Adapter<ListingAdapter.ViewHolder>() {

    private val LOADING = 0
    private val ITEM = 1
    private var isLoadingAdded = false

    companion object {
        var pagenuomber = 0
        var lastPagenuomber = 0
    }


    private var isSelected = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_listing, parent, false)
        return ViewHolder(view)


    }



    override fun getItemViewType(position: Int): Int {
        return if (position == list.size - 1 && isLoadingAdded) LOADING else ITEM

//        return super.getItemViewType(position)

    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list.size
    }


    fun getItem(position: Int):  ProductDataModel{
        return list.get(position)
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    fun filterData(filterList : ArrayList<ProductDataModel>){
        list = filterList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        holder.tvTitlex.text=model.title
        holder.tvPricex.text=model.price.toString()+" Kr"


        Glide.with(currAtivity).addDefaultRequestListener(object : RequestListener<Any>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Any>?,
                isFirstResource: Boolean
            ): Boolean {
                holder.shimmerImagex.visibility = View.VISIBLE
//                        holder.itemView.imgProductImagex.visibility = View.INVISIBLE
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
                holder.imgshimmerImage.visibility = View.GONE
                holder.imgProductImagex.visibility = View.VISIBLE
//                        holder.itemView.imgProductImagex.visibility = View.VISIBLE
                holder.shimmerImagex.stopShimmer()
                return false
            }

        })
            .load(model.listing_image).placeholder(currAtivity.getDrawable(R.drawable.grey_round_shape)).
            error(currAtivity.getDrawable(R.drawable.grey_round_shape)).
            into(holder.imgProductImagex)

        holder.rlrowlisting.setOnClickListener({
            var i=Intent(currAtivity, ProductDetailActivity::class.java)
            i.putExtra("id",model.id.toString())
            currAtivity.startActivity(i)
            Log.e("id",model.id.toString())
        })
    }

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        var shimmerImagex: ShimmerFrameLayout
        var imgshimmerImage:ImageView
        var imgProductImagex:ImageView
        var tvPricex: TextView
        var tvTitlex:TextView
        var rlrowlisting:RelativeLayout

        init {
            shimmerImagex=ItemView.findViewById(R.id.shimmerImagex)
            imgshimmerImage=ItemView.findViewById(R.id.imgshimmerImage)
            tvPricex = ItemView.findViewById(R.id.tvPricex)
            imgProductImagex=ItemView.findViewById(R.id.imgProductImagex)
            tvTitlex=ItemView.findViewById(R.id.tvTitlex)
            rlrowlisting=ItemView.findViewById(R.id.rlrowlisting)

        }
    }


}