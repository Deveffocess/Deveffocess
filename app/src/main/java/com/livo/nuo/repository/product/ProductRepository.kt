package com.livo.nuo.repository.product

import com.google.gson.JsonObject
import com.livo.nuo.netUtils.services.ApiInterface
import com.livo.nuo.netUtils.services.IntegralService
import com.livo.nuo.repository.BaseRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject

class ProductRepository : BaseRepository() {

    private var apiInterface : ApiInterface? = null
    private var service : IntegralService = IntegralService()

    init {
        apiInterface = service.createService(ApiInterface::class.java)
    }

   /* suspend  fun uploadPhotoes(image : Array<MultipartBody.Part?>)  =
            apiInterface?.uploadPhotoes(service.headers(),image)

    suspend fun getMonthDatesList() = apiInterface?.getMonthDatesList(service.headers())
    suspend fun getEstimatedPrice(weight : Int) = apiInterface?.getEstimatedPrice(service.headers(),weight)

    suspend fun applyCoupon(couponCode : String) = apiInterface?.applyCoupon(service.headers(),couponCode)

    suspend fun getPickupAmount(couponCode : String,amount : String) = apiInterface?.getPickupAmount(service.headers(),couponCode,amount)

    suspend fun getPickupAmountWithoutCoupon(amount : String) = apiInterface?.getPickupAmountWithoutCoupon(service.headers(),amount)

    suspend fun addProduct(jsonObject: JsonObject) = apiInterface?.addProduct(service.headers(),jsonObject)

    suspend fun getBanners() = apiInterface?.getBanners(service.headers())

    suspend fun getOwnProducts() = apiInterface?.getOwnProducts(service.headers())
*/
    suspend fun getUserListings(jsonObject: JsonObject) = apiInterface?.getUserListings(service.headers(),jsonObject)

    suspend fun getTransportersListForProduct(jsonObject: JsonObject) = apiInterface?.getTransportersListForProduct(service.headers(),jsonObject)
    suspend fun getTransportersListRemoveBid(jsonObject: JsonObject) = apiInterface?.getTransportersListRemoveBid(service.headers(),jsonObject)


    suspend fun getPlacebid(jsonObject: JsonObject) = apiInterface?.getPlacebid(service.headers(),jsonObject)

    suspend fun getAllListings(jsonObject: JsonObject) = apiInterface?.getAllListings(service.headers(),jsonObject)
    suspend fun getDeleteProduct(jsonObject: JsonObject) = apiInterface?.getDeleteProduct(service.headers(),jsonObject)
    suspend fun getSingleListingData(jsonObject: JsonObject) = apiInterface?.getSingleListingData(service.headers(),jsonObject)

    suspend fun createListing(title:RequestBody,height:RequestBody,width:RequestBody,depth:RequestBody,weight:RequestBody,price:RequestBody,
                              more_people_needed:RequestBody,pickup_date:RequestBody,dropoff_date:RequestBody,pickup_latitude:RequestBody,
                              pickup_longitude:RequestBody,pickup_address:RequestBody,pickup_address_note:RequestBody,dropoff_latitude:RequestBody,
                              dropoff_longitude:RequestBody,dropoff_address:RequestBody,dropoff_address_note:RequestBody,distance:RequestBody,
                              image1 :MultipartBody.Part,image2 :MultipartBody.Part,image3 :MultipartBody.Part) =
        apiInterface?.createListing(service.headers(),title,height,width,depth,weight,price,more_people_needed,pickup_date,dropoff_date,
                pickup_latitude,pickup_longitude,pickup_address,pickup_address_note,dropoff_latitude,dropoff_longitude,dropoff_address,
            dropoff_address_note,distance,image1,image2,image3)

    /*
    suspend fun getSearchedAllProducts(search : String) = apiInterface?.getSearchedAllProducts(service.headers(),search)



    suspend fun approve_transporter_admin(userId : Int) = apiInterface?.approve_transporter_admin(service.headers(),userId)

    suspend fun applyFromTransporterOnProduct(jsonObject: JsonObject) = apiInterface?.applyFromTransporterOnProduct(service.headers(),jsonObject)

    suspend fun editApplyFromTransporterOnProduct(jsonObject: JsonObject) = apiInterface?.editApplyFromTransporterOnProduct(service.headers(),jsonObject)

    suspend fun getTransportersForProduct(productId : Int) = apiInterface?.getTransportersForProduct(service.headers(),productId)

    suspend fun removeTransporter(jsonObject: JsonObject) = apiInterface?.removeTransporter(service.headers(),jsonObject)

    suspend fun approveTransporterSender(jsonObject: JsonObject) = apiInterface?.approveTransporterSender(service.headers(),jsonObject)

    suspend fun reportProduct(jsonObject: JsonObject) = apiInterface?.reportProduct(service.headers(),jsonObject)

    suspend fun listingOngoingStates(jsonObject: JsonObject) = apiInterface?.listingOngoingStates(service.headers(),jsonObject)

    suspend fun postRating(jsonObject: JsonObject) = apiInterface?.postRating(service.headers(),jsonObject)

    suspend fun getOngoingListingStates(productId : Int) = apiInterface?.getOngoingListingStates(service.headers(),productId)
*/
}