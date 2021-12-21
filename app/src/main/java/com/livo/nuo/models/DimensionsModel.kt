package com.livo.nuo.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DimensionsModel: Serializable {
    @SerializedName("depth")
    @Expose
    var depth = ""
    @SerializedName("width")
    @Expose
    var width = ""
    @SerializedName("height")
    @Expose
    var height = ""
    @SerializedName("weight")
    @Expose
    var weight = ""

}