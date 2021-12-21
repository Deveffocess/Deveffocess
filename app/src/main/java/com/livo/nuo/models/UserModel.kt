package com.livo.nu.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class  UserModel : Serializable{

    @SerializedName("country_code")
    @Expose
    var country_code = ""
    @SerializedName("phone_number")
    @Expose
    var phone_number = ""
    @SerializedName("referral_code")
    @Expose
    var referral_code = ""
    @SerializedName("updated_at")
    @Expose
    var updated_at = ""
    @SerializedName("created_at")
    @Expose
    var created_at = ""
    @SerializedName("id")
    @Expose
    var id = 0
    @SerializedName("username")
    @Expose
    var username = ""
    @SerializedName("path")
    @Expose
    var path = ""
    @SerializedName("thumb_image")
    @Expose
    var thumb_image = ""
    @SerializedName("short_image")
    @Expose
    var short_image = ""
    @SerializedName("short_user_licence")
    @Expose
    var short_user_licence = ""
    @SerializedName("short_vehicle_image")
    @Expose
    var short_vehicle_image = ""
    @SerializedName("email")
    @Expose
    var email = ""
    @SerializedName("profile_image")
    @Expose
    var profile_image = ""
    @SerializedName("status_text")
    @Expose
    var status_text = ""
    @SerializedName("user_type")
    @Expose
    var user_type = 0
    @SerializedName("image")
    @Expose
    var image = ""
    @SerializedName("is_approved")
    @Expose
    var is_approved = -1
    @SerializedName("gender")
    @Expose
    var gender = 0
    @SerializedName("cpr")
    @Expose
    var cpr = ""
    @SerializedName("is_driver")
    @Expose
    var is_driver = ""
    @SerializedName("first_name")
    @Expose
    var first_name = ""
    @SerializedName("rating")
    @Expose
    var rating = ""
    @SerializedName("last_name")
    @Expose
    var last_name = ""
    @SerializedName("dob")
    @Expose
    var dob = ""
    @SerializedName("user_licence")
    @Expose
    var user_licence = ""
    @SerializedName("vehicle_image")
    @Expose
    var vehicle_image = ""
    @SerializedName("vehicle_type")
    @Expose
    var vehicle_type = ""
    @SerializedName("licence_plate_number")
    @Expose
    var licence_plate_number = ""
    @SerializedName("licence_number")
    @Expose
    var licence_number = ""
    @SerializedName("about")
    @Expose
    var about = ""
    @SerializedName("bank_registration_number")
    @Expose
    var bank_registration_number = ""
    @SerializedName("line1")
    @Expose
    var line1 = ""
    @SerializedName("line2")
    @Expose
    var line2 = ""
    @SerializedName("city")
    @Expose
    var city = ""
    @SerializedName("postal_code")
    @Expose
    var postal_code = ""
    @SerializedName("state")
    @Expose
    var state = ""
    @SerializedName("identity_document")
    @Expose
    var identity_document = ""
    @SerializedName("document_provider_identity_document")
    @Expose
    var document_provider_identity_document = ""
    @SerializedName("bank_account_number")
    @Expose
    var bank_account_number = ""

}