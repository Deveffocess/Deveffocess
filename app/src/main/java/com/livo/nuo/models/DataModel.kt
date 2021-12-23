package com.livo.nuo.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.livo.nu.models.UserModel
import java.io.Serializable

class DataModel :Serializable{

    @SerializedName("phone_number")
    @Expose
    var phone_number = ""
    @SerializedName("country_code")
    @Expose
    var country_code = ""
    @SerializedName("otp")
    @Expose
    var otp = ""
    @SerializedName("timeout")
    @Expose
    var timeout = ""
    @SerializedName("language")
    @Expose
    var language = ""
    @SerializedName("is_exists")
    @Expose
    var is_exists = false



    //Single listing fields

    @SerializedName("id")
    @Expose
    var id = 0
    @SerializedName("title")
    @Expose
    var title = ""
    @SerializedName("more_people_needed")
    @Expose
    var more_people_needed = ""
    @SerializedName("price")
    @Expose
    var price = ""
    @SerializedName("pickup_date")
    @Expose
    var pickup_date = ""
    @SerializedName("dropoff_date")
    @Expose
    var dropoff_date = ""
    @SerializedName("pickup_longitude")
    @Expose
    var pickup_longitude = ""
    @SerializedName("pickup_latitude")
    @Expose
    var pickup_latitude = ""
    @SerializedName("pickup_address")
    @Expose
    var pickup_address = ""
    @SerializedName("dropoff_longitude")
    @Expose
    var dropoff_longitude = ""
    @SerializedName("dropoff_latitude")
    @Expose
    var dropoff_latitude = ""
    @SerializedName("dropoff_address")
    @Expose
    var dropoff_address = ""
    @SerializedName("distance")
    @Expose
    var distance = ""
    @SerializedName("status")
    @Expose
    var status = ""
    @SerializedName("created_by")
    @Expose
    var created_by = ""
    @SerializedName("is_edit")
    @Expose
    var is_edit = ""
    @SerializedName("is_bidding")
    @Expose
    var is_bidding = ""

    @SerializedName("has_next")
    @Expose
    var has_next = false


    @SerializedName("dimensions")
    @Expose
    var dimensions = DimensionsModel()

    @SerializedName("user_details")
    @Expose
    var user_details = UserModel()

    @SerializedName("listing_images")
    @Expose
    var listing_images = ArrayList<String>()

    @SerializedName("bidding_details")
    @Expose
    var bidding_details = BiddingDetailModel()



    //Ongoing listing fields
    @SerializedName("current_page")
    @Expose
    var current_page = 0

    @SerializedName("total_pages")
    @Expose
    var total_pages = 0

    @SerializedName("per_page")
    @Expose
    var per_page = 0

    @SerializedName("total_records")
    @Expose
    var total_records = 0

    @SerializedName("biddings_data")
    @Expose
    var biddings_data = ArrayList<TranspoterBiddingDataModel>()



    //Transpoter Ongoing

    @SerializedName("offer_id")
    @Expose
    var offer_id = 0

    @SerializedName("is_transporter")
    @Expose
    var is_transporter = ""

    @SerializedName("listing_image")
    @Expose
    var listing_image = ""

    @SerializedName("channel_id")
    @Expose
    var channel_id = ""

    @SerializedName("current_level")
    @Expose
    var current_level = 0

    @SerializedName("main_levels")
    @Expose
    var main_levels = MainLevelsModel()


    //Make payment
    @SerializedName("payment_intent")
    @Expose
    var payment_intent = ""

    @SerializedName("ephemeral_key")
    @Expose
    var ephemeral_key = ""

    @SerializedName("customer")
    @Expose
    var customer = ""

    @SerializedName("publishable_key")
    @Expose
    var publishable_key = ""

    //pickupDropoffListing

    @SerializedName("image")
    @Expose
    var image = ""

    @SerializedName("thumb_image")
    @Expose
    var thumb_image = ""

    @SerializedName("sub_title")
    @Expose
    var sub_title = ""


    //Profile
    @SerializedName("first_name")
    @Expose
    var first_name = ""

    @SerializedName("last_name")
    @Expose
    var last_name = ""

    @SerializedName("age")
    @Expose
    var age = ""

    @SerializedName("profile_image")
    @Expose
    var profile_image = ""

    @SerializedName("lang")
    @Expose
    var lang = ""

    @SerializedName("email")
    @Expose
    var email = ""

    @SerializedName("dob")
    @Expose
    var dob = ""

    @SerializedName("gender")
    @Expose
    var gender = ""

    @SerializedName("user_type")
    @Expose
    var user_type = ""

    @SerializedName("transporter_application")
    @Expose
    var transporter_application = ProfileTransporterModel()

    //search
    @SerializedName("my_listings_count")
    @Expose
    var my_listings_count = 0

    @SerializedName("my_biddings_count")
    @Expose
    var my_biddings_count = 0

    @SerializedName("others_listings_count")
    @Expose
    var others_listings_count = 0

    @SerializedName("my_listings")
    @Expose
    var my_listings = ArrayList<ProductModel>()

    @SerializedName("my_biddings")
    @Expose
    var my_biddings = ArrayList<ProductModel>()

    @SerializedName("others_listings")
    @Expose
    var others_listings = ArrayList<ProductDataModel>()
}
