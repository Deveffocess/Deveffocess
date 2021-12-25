package com.livo.nuo.repository.profile

import com.google.gson.JsonObject
import com.livo.nuo.netUtils.services.ApiInterface
import com.livo.nuo.netUtils.services.IntegralService
import com.livo.nuo.repository.BaseRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileRepository : BaseRepository() {

    private var apiInterface : ApiInterface? = null
    private var service : IntegralService = IntegralService()

    init {
        apiInterface = service.createService(ApiInterface::class.java)
    }


    suspend fun getUserSettings() = apiInterface?.getUserSettings(service.headers())

    suspend fun getViewOwnProfile() = apiInterface?.getViewOwnProfile(service.headers())

    suspend fun getchangeNumSendOtp(jsonObject: JsonObject) = apiInterface?.getchangeNumSendOtp(service.headers(),jsonObject)

    suspend fun getChangeLang(jsonObject: JsonObject) = apiInterface?.getChangeLang(service.headers(),jsonObject)

    suspend fun getChangeNumChangeNumber(jsonObject: JsonObject) = apiInterface?.getChangeNumChangeNumber(service.headers(),jsonObject)

    suspend fun setEditOwnProfile(first_name: RequestBody, last_name: RequestBody,email: RequestBody
                                  ,dob: RequestBody,gender: RequestBody,user_type: RequestBody
                                  ,image : MultipartBody.Part) =
        apiInterface?.setEditOwnProfile(service.headers(),first_name,last_name,email,dob,gender,user_type,image)
}