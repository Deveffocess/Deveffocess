package com.livo.nuo.repository.ongoingstate

import com.google.gson.JsonObject
import com.livo.nuo.netUtils.services.ApiInterface
import com.livo.nuo.netUtils.services.IntegralService
import com.livo.nuo.repository.BaseRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class OngoingRepository : BaseRepository() {

    private var apiInterface : ApiInterface? = null
    private var service : IntegralService = IntegralService()

    init {
        apiInterface = service.createService(ApiInterface::class.java)
    }


    suspend fun getViewAllState(jsonObject: JsonObject) = apiInterface?.getViewAllState(service.headers(),jsonObject)

    suspend fun getSenderApproveTrns(jsonObject: JsonObject) = apiInterface?.getSenderApproveTrns(service.headers(),jsonObject)

    suspend fun getTrnsAcceptApproval(jsonObject: JsonObject) = apiInterface?.getTrnsAcceptApproval(service.headers(),jsonObject)

    suspend fun getTrnsDeclineApproval(jsonObject: JsonObject) = apiInterface?.getTrnsDeclineApproval(service.headers(),jsonObject)

    suspend fun getChangeBidPrice(jsonObject: JsonObject) = apiInterface?.getChangeBidPrice(service.headers(),jsonObject)

    suspend fun getMakePayment(jsonObject: JsonObject) = apiInterface?.getMakePayment(service.headers(),jsonObject)

    suspend fun getChangePaymentStatus(jsonObject: JsonObject) = apiInterface?.getChangePaymentStatus(service.headers(),jsonObject)

    suspend fun getTrnsCompleteListing(jsonObject: JsonObject) = apiInterface?.getTrnsCompleteListing(service.headers(),jsonObject)

    suspend fun getSenderCompletesListing(jsonObject: JsonObject) = apiInterface?.getSenderCompletesListing(service.headers(),jsonObject)

    suspend fun getDeleteBid(jsonObject: JsonObject) = apiInterface?.getDeleteBid(service.headers(),jsonObject)

    suspend fun pickupDropoffListing(offer_id: RequestBody, state: RequestBody,image : MultipartBody.Part) = apiInterface?.pickupDropoffListing(service.headers(),offer_id,state,image)
}