package com.livo.nuo.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ProfileTransporterModel: Serializable {

    @SerializedName("state")
    @Expose
    var state = 0
    @SerializedName("title")
    @Expose
    var title = ""
    @SerializedName("subtitle")
    @Expose
    var subtitle = ""
    @SerializedName("button_text")
    @Expose
    var button_text = ""
}