package com.livo.nuo.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.livo.nu.models.UserModel
import java.io.Serializable

class ChatMessageModel: Serializable
{
    @SerializedName("list_id")
    @Expose
    var list_id = 0

    @SerializedName("status")
    @Expose
    var status = ""

    @SerializedName("channel_id")
    @Expose
    var channel_id = ""

    @SerializedName("listing_image")
    @Expose
    var listing_image = ""

    @SerializedName("user_data")
    @Expose
    var user_data = UserModel()

    @SerializedName("channel_details")
    @Expose
    var channel_details = UserModel()

}