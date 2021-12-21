package com.livo.nuo.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MainLevelsModel : Serializable {

    @SerializedName("default")
    @Expose
    var default = MainLevelDetailsModel()

    @SerializedName("pickup")
    @Expose
    var pickup = MainLevelDetailsModel()

    @SerializedName("dropoff")
    @Expose
    var dropoff = MainLevelDetailsModel()

    @SerializedName("completed")
    @Expose
    var completed = MainLevelDetailsModel()

}