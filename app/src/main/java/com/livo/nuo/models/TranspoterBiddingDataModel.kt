package com.livo.nuo.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TranspoterBiddingDataModel : Serializable {

    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("bid_amount")
    @Expose
    var bid_amount = ""

    @SerializedName("transporter")
    @Expose
    var transporter = TranspoterOngoingDetailModel()
}