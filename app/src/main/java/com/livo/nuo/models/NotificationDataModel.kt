package com.livo.nuo.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class NotificationDataModel : Serializable {

    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("text")
    @Expose
    var text = ""

    @SerializedName("sub_text")
    @Expose
    var sub_text = ArrayList<String>()

    @SerializedName("image")
    @Expose
    var image = ""

    @SerializedName("nCode")
    @Expose
    var nCode = 0

    @SerializedName("request_id")
    @Expose
    var request_id = 0

    @SerializedName("is_read")
    @Expose
    var is_read = false

    @SerializedName("date")
    @Expose
    var date = ""

    @SerializedName("time")
    @Expose
    var time = ""


}