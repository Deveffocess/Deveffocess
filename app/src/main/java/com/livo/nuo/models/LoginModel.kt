package com.livo.nuo.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class  LoginModel : Serializable{

    @SerializedName("code")
    @Expose
    var code = 0
    @SerializedName("is_login")
    @Expose
    var is_login = 0
    @SerializedName("message")
    @Expose
    var message = ""
    @SerializedName("token")
    @Expose
    var token = ""

    @SerializedName("status")
    @Expose
    var status = false

    @SerializedName("data",alternate = ["data_detail"])
    @Expose
    var data = DataModel()

    /*@SerializedName("user",alternate = ["user_detail"])
    @Expose
    var user = UserModel()*/

}