package com.livo.nuo.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.livo.nu.models.UserModel
import java.io.Serializable

class ProductModel : Serializable {


    @SerializedName("id")
    @Expose
    var id = 0
    @SerializedName("title")
    @Expose
    var title = ""
    @SerializedName("price")
    @Expose
    var price = ""

    @SerializedName("offer_id")
    @Expose
    var offer_id = 0

    @SerializedName("my_offer")
    @Expose
    var my_offer = ""

    @SerializedName("approved_transporter")
    @Expose
    var approved_transporter = 0

    @SerializedName("color_status")
    @Expose
    var color_status = ""
    @SerializedName("listing_image")
    @Expose
    var listing_image = ""

    @SerializedName("status")
    @Expose
    var status = ""

    @SerializedName("bidding_status")
    @Expose
    var bidding_status = MainLevelDetailsModel()

    @SerializedName("biddings")
    @Expose
    var biddings = 0

    @SerializedName("approved_bid_id")
    @Expose
    var approved_bid_id = 0

    @SerializedName("user_details")
    @Expose
    var user_details = UserModel()

    /*@SerializedName("height")
    @Expose
    var height = ""
    @SerializedName("width")
    @Expose
    var width = ""
    @SerializedName("depth")
    @Expose
    var depth = ""
    @SerializedName("weight")
    @Expose
    var weight = ""
    @SerializedName("more_people_needed")
    @Expose
    var more_people_needed = 0
    @SerializedName("parcel_status")
    @Expose
    var parcel_status = 0
    @SerializedName("transporter_rating")
    @Expose
    var transporter_rating = 0
    @SerializedName("sender_rating")
    @Expose
    var sender_rating = 0
    @SerializedName("created_by")
    @Expose
    var created_by = 0

    @SerializedName("pickup_date")
    @Expose
    var pickup_date = ""

    @SerializedName("drop_date")
    @Expose
    var drop_date = ""*/


   /* @SerializedName("images")
    @Expose
    var images = ArrayList<ImageModel>()

    @SerializedName("pickup_address")
    @Expose
    var pickup_address = AddressModel()

    @SerializedName("drop_address")
    @Expose
    var drop_address = AddressModel()

    @SerializedName("mine")
    @Expose
    var mine = false
    @SerializedName("share")
    @Expose
    var share = ""

    @SerializedName("applied")
    @Expose
    var applied = 0
    @SerializedName("transporter_applied")
    @Expose
    var transporter_applied = TransporterAppliedModel()
    @SerializedName("driver")
    @Expose
    var driver = DriverModel()
    @SerializedName("creator",alternate = ["createdBy"])
    @Expose
    var creator = UserModel()

    @SerializedName("transportation_state")
    @Expose
    var transportation_state = ""
    @SerializedName("transporter")
    @Expose
    var transporter = UserModel()
    @SerializedName("transporter_rate")
    @Expose
    var transporter_rate = false
    @SerializedName("sender_rate")
    @Expose
    var sender_rate = false

    @SerializedName("chat_id")
    @Expose
    var chat_id = 0
    @SerializedName("from_id")
    @Expose
    var from_id = 0
    @SerializedName("to_id")
    @Expose
    var to_id = 0
    @SerializedName("message")
    @Expose
    var message = ""
    @SerializedName("is_read")
    @Expose
    var is_read = 0
    @SerializedName("message_type")
    @Expose
    var message_type = 0
    @SerializedName("created_at")
    @Expose
    var created_at = ""
    @SerializedName("from_user")
    @Expose
    var from_user = UserModel()
    @SerializedName("to_user")
    @Expose
    var to_user = UserModel()*/

}