package com.livo.nuo.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ProductDataModel : Serializable {

    @SerializedName("current_page")
    @Expose
    var current_page = 0
    @SerializedName("total_pages")
    @Expose
    var total_pages = 0
    @SerializedName("per_page")
    @Expose
    var per_page = 0
    @SerializedName("total_records")
    @Expose
    var total_records = 0
    @SerializedName("user_type")
    @Expose
    var user_type = ""
    @SerializedName("has_next")
    @Expose
    var has_next = false
    @SerializedName("profile_fulfil")
    @Expose
    var profile_fulfil = false
    @SerializedName("list_data")
    @Expose
    var list_data = ArrayList<ProductModel>()


    //listings
    @SerializedName("id")
    @Expose
    var id = 0
    @SerializedName("title")
    @Expose
    var title = ""
    @SerializedName("price")
    @Expose
    var price = ""
    @SerializedName("pickup_date")
    @Expose
    var pickup_date = ""
    @SerializedName("created_at")
    @Expose
    var created_at = ""
    @SerializedName("user_distance")
    @Expose
    var user_distance = ""
    @SerializedName("listing_image")
    @Expose
    var listing_image = ""
    @SerializedName("days")
    @Expose
    var days = 0


}