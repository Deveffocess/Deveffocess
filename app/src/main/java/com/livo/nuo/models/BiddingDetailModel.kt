package com.livo.nuo.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BiddingDetailModel: Serializable {

    @SerializedName("bidding_price")
    @Expose
    var bidding_price = ""

    @SerializedName("bidding_message")
    @Expose
    var bidding_message = ""


    @SerializedName("bidding_tax_details")
    @Expose
    var bidding_tax_details =BiddingTaxDetailModel()
}