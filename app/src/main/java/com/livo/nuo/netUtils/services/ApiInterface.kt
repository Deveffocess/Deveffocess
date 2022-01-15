package com.livo.nuo.netUtils.services

import com.google.gson.JsonObject
import com.livo.nuo.models.ExtraDataModel
import com.livo.nuo.models.ListingAllModel
import com.livo.nuo.models.LoginModel
import com.livo.nuo.models.ProductListModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

  @POST("send_otp")
  suspend fun getOtp(
          @Body jsonObject: JsonObject?
  ): LoginModel?

  @POST("create_session")
  suspend fun createSession(
    @Body jsonObject: JsonObject?
  ): LoginModel?

  @GET("extra_data")
  suspend fun extraData(
  ): ExtraDataModel?

  @POST("user_listings")
  suspend fun getUserListings(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): ProductListModel?

  @POST("listings")
  suspend fun getAllListings(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): ListingAllModel?

  @POST("single_listing")
  suspend fun getSingleListingData(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("delete_listing")
  suspend fun getDeleteProduct(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("place_bid")
  suspend fun getPlacebid(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("search")
  suspend fun getSearch(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("all_notifications")
  suspend fun getAllNotification(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("change_bid_price")
  suspend fun getChangeBidPrice(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?


  @POST("bidding_list")
  suspend fun getTransportersListForProduct(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("remove_bid")
  suspend fun getTransportersListRemoveBid(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("view_all_states")
  suspend fun getViewAllState(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("sender_approve_trns")
  suspend fun getSenderApproveTrns(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("trns_accept_approval")
  suspend fun getTrnsAcceptApproval(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("trns_decline_approval")
  suspend fun getTrnsDeclineApproval(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("make_payment")
  suspend fun getMakePayment(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("change_payment_status")
  suspend fun getChangePaymentStatus(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("trns_complete_listing")
  suspend fun getTrnsCompleteListing(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("add_channel")
  suspend fun getAddChannel(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("sender_completes_listing")
  suspend fun getSenderCompletesListing(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("delete_bid")
  suspend fun getDeleteBid(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?


  @GET("user_settings")
  suspend fun getUserSettings(
    @HeaderMap headers: Map<String, String>,
  ): LoginModel?

  @POST("logout")
  suspend fun getLogout(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @GET("view_own_profile")
  suspend fun getViewOwnProfile(
    @HeaderMap headers: Map<String, String>,
  ): LoginModel?

  @POST("change_num_send_otp")
  suspend fun getchangeNumSendOtp(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("change_lang")
  suspend fun getChangeLang(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?

  @POST("change_num_change_number")
  suspend fun getChangeNumChangeNumber(
    @HeaderMap headers: Map<String, String>,
    @Body jsonObject: JsonObject
  ): LoginModel?


  @GET("get_all_chats")
  suspend fun getAllChats(
    @HeaderMap headers: Map<String, String>,
  ): LoginModel?



  @Multipart
  @POST("create_listing")
  suspend fun createListing(
    @HeaderMap headers: Map<String, String>,
    @Part ("title") title:RequestBody,
    @Part ("height") height:RequestBody,
    @Part ("width") width:RequestBody,
    @Part ("depth") depth:RequestBody,
    @Part ("weight") weight:RequestBody,
    @Part ("price") price:RequestBody,
    @Part ("more_people_needed") more_people_needed:RequestBody,
    @Part ("pickup_date") pickup_date:RequestBody,
    @Part ("dropoff_date") dropoff_date:RequestBody,
    @Part ("pickup_longitude") pickup_longitude:RequestBody,
    @Part ("pickup_latitude") pickup_latitude:RequestBody,
    @Part ("pickup_address") pickup_address:RequestBody,
    @Part ("pickup_address_note") pickup_address_note:RequestBody,
    @Part ("dropoff_longitude") dropoff_longitude:RequestBody,
    @Part ("dropoff_latitude") dropoff_latitude:RequestBody,
    @Part ("dropoff_address") dropoff_address:RequestBody,
    @Part ("dropoff_address_note") dropoff_address_note:RequestBody,
    @Part ("distance") distance:RequestBody,
    @Part image1 :MultipartBody.Part,
    @Part image2 :MultipartBody.Part,
    @Part image3 :MultipartBody.Part,
  ): LoginModel?


  @Multipart
  @POST("pickup_dropoff_listing")
  suspend fun pickupDropoffListing(
    @HeaderMap headers: Map<String, String>,
    @Part ("offer_id") title:RequestBody,
    @Part ("state") height:RequestBody,
    @Part image :MultipartBody.Part
  ): LoginModel?

  @Multipart
  @POST("edit_own_profile")
  suspend fun setEditOwnProfile(
    @HeaderMap headers: Map<String, String>,
    @Part ("first_name") first_name:RequestBody,
    @Part ("last_name") last_name:RequestBody,
    @Part ("email") email:RequestBody,
    @Part ("dob") dob:RequestBody,
    @Part ("gender") gender:RequestBody,
    @Part ("user_type") user_type:RequestBody,
    @Part image :MultipartBody.Part
  ): LoginModel?

}