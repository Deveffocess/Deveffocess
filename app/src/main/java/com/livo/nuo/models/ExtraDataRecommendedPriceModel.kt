package com.livo.nuo.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ExtraDataRecommendedPriceModel: Serializable {

    @SerializedName("label")
    @Expose
    var label = ""
    @SerializedName("price")
    @Expose
    var price = ""
    @SerializedName("price_code")
    @Expose
    var price_code = ""
    @SerializedName("size")
    @Expose
    var size = ""



}