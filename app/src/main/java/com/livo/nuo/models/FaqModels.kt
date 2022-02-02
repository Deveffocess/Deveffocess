package com.livo.nuo.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FaqModels: Serializable {
    @SerializedName("en")
    @Expose
    var en = ""

    @SerializedName("da")
    @Expose
    var da = ""

    @SerializedName("sv")
    @Expose
    var sv = ""
}