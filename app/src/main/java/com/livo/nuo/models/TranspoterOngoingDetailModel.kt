package com.livo.nuo.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TranspoterOngoingDetailModel : Serializable {

    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("first_name")
    @Expose
    var first_name = ""

    @SerializedName("last_name")
    @Expose
    var last_name = ""

    @SerializedName("profile_image")
    @Expose
    var profile_image = ""
}