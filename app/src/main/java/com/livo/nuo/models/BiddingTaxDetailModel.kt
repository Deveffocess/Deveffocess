package com.livo.nuo.models

import androidx.annotation.Nullable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BiddingTaxDetailModel: Serializable {

    @SerializedName("total")
    @Expose
    var total = ""

    @SerializedName("commission")
    @Expose
    var commission = ""

    @SerializedName("payout_amount")
    @Expose
    var payout_amount = ""

}