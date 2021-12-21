package com.livo.nuo.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ColorModel : Serializable {

    @SerializedName("id")
    @Expose
    var id = 0
    @SerializedName("label")
    @Expose
    var label = ""
    @SerializedName("color_light")
    @Expose
    var color_light = ""
    @SerializedName("color_dark")
    @Expose
    var color_dark = ""
}