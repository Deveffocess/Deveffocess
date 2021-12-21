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

    @SerializedName("colors")
    @Expose
    var colors = ArrayList<ColorModel>()


    @SerializedName("recommended_price")
    @Expose
    var recommended_price = ArrayList<ExtraDataRecommendedPriceModel>()
}