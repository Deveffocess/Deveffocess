package com.livo.nuo.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ExtraDataInternalModel : Serializable {

    @SerializedName("transporter_commission")
    @Expose
    var transporter_commission = ""

    @SerializedName("sender_commission")
    @Expose
    var sender_commission = ""

    @SerializedName("minimum_listing_price")
    @Expose
    var minimum_listing_price = ""

    @SerializedName("email")
    @Expose
    var email = ""

    @SerializedName("phone")
    @Expose
    var phone = ""

    @SerializedName("facebook_url")
    @Expose
    var facebook_url = ""

    @SerializedName("app_store_url")
    @Expose
    var app_store_url = ""

    @SerializedName("play_store_url")
    @Expose
    var play_store_url = ""

    @SerializedName("faq_url")
    @Expose
    var faq_url = FaqModels()

    @SerializedName("colors")
    @Expose
    var colors = ArrayList<ColorModel>()


    @SerializedName("recommended_price")
    @Expose
    var recommended_price = ArrayList<ExtraDataRecommendedPriceModel>()
}