package com.livo.nuo.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MainLevelDetailsModel : Serializable {

    @SerializedName("title")
    @Expose
    var title = ""

    @SerializedName("sub_level")
    @Expose
    var sub_level = 0

    @SerializedName("image")
    @Expose
    var image = ""

    @SerializedName("thumb")
    @Expose
    var thumb = ""

    @SerializedName("sub_title")
    @Expose
    var sub_title = ""

}