package com.livo.nuo.viewModel.products

import com.livo.nuo.viewModel.base.BaseViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.livo.nu.models.*
import com.livo.nuo.models.ListingAllModel
import com.livo.nuo.models.LoginModel
import com.livo.nuo.models.ProductListModel
import com.livo.nuo.netUtils.response.ErrorResponse
import com.livo.nuo.repository.product.ProductRepository
import com.livo.nuo.utility.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.lang.Exception

 class ProductViewModel(application: Application) :  BaseViewModel(application){

    private var productRepository : ProductRepository? = null

    /*private var mutableLiveDataImageArray : MutableLiveData<ImageModel> = MutableLiveData()
    private var mutableLiveDataApplyCoupon : MutableLiveData<ImageModel> = MutableLiveData()
    private var mutableLiveDataAddProduct : MutableLiveData<ImageModel> = MutableLiveData()
    private var mutableLiveDataMonthDateList : MutableLiveData<DateListModel> = MutableLiveData()
    private var mutableLiveDataEstimatedPrice : MutableLiveData<EstimatedPriceModel> = MutableLiveData()
    private var mutableLiveDataGetPickUpAmount : MutableLiveData<DiscountModel> = MutableLiveData()
    private var mutableLiveDataBanners : MutableLiveData<BannerListModel> = MutableLiveData()*/
    private var mutableLiveDataOwnProducts : MutableLiveData<ProductListModel> = MutableLiveData()
    private var mutableLiveDataProductsData : MutableLiveData<ProductListModel> = MutableLiveData()
    private var mutableLiveDataAllListings : MutableLiveData<ListingAllModel> = MutableLiveData()
    private var mutableLiveDataProductOngoingStates : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataDeleteProduct : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataCreateListing : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataPlacebid : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveTranspoterListForProduct : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveTransportersListRemoveBid : MutableLiveData<LoginModel> = MutableLiveData()

     /* private var mutableLiveDataOwnProductDetail : MutableLiveData<ProductDetailObjectModel> = MutableLiveData()
      private var mutableLiveDataApproveTransporterAdmin : MutableLiveData<ImageModel> = MutableLiveData()
      private var mutableLiveDataApplyFromTransporter : MutableLiveData<ImageModel> = MutableLiveData()
      private var mutableLiveDataRemoveTransporter : MutableLiveData<ImageModel> = MutableLiveData()
      private var mutableLiveDataApproveTransporterSender : MutableLiveData<ImageModel> = MutableLiveData()
      private var mutableLiveDataReportProduct : MutableLiveData<ImageModel> = MutableLiveData()
      private var mutableLiveDataGetTransporterList : MutableLiveData<TransporterListModel> = MutableLiveData()

      private var mutableLiveDataPostOngoingStates : MutableLiveData<ImageModel> = MutableLiveData()
      private var mutableLiveDataPostRating : MutableLiveData<ImageModel> = MutableLiveData()*/



    init {
        productRepository = ProductRepository()
    }

    /*fun getMutableLiveDataImageArray(): MutableLiveData<ImageModel> =
            mutableLiveDataImageArray

    fun getMutableLiveDataReportProduct(): MutableLiveData<ImageModel> =
            mutableLiveDataReportProduct

    fun getMutableLiveDataPostRating(): MutableLiveData<ImageModel> =
            mutableLiveDataPostRating

    fun getMutableLiveDataPostOngoingStates(): MutableLiveData<ImageModel> =
            mutableLiveDataPostOngoingStates

    fun getMutableLiveDataBanners(): MutableLiveData<BannerListModel> =
        mutableLiveDataBanners*/

    fun getMutableLiveDataProductDetail(): MutableLiveData<LoginModel> =
            mutableLiveDataProductOngoingStates

    fun getMutableLiveDataOwnProducts(): MutableLiveData<ProductListModel> =
        mutableLiveDataOwnProducts

    fun getMutableLiveDataProductsData(): MutableLiveData<ProductListModel> =
        mutableLiveDataProductsData

    fun getMutableLiveDataAllListings(): MutableLiveData<ListingAllModel> =
        mutableLiveDataAllListings

     fun getMutableLiveDataDeleteData(): MutableLiveData<LoginModel> =
         mutableLiveDataDeleteProduct

    fun getMutableLiveDataCreateListing(): MutableLiveData<LoginModel> =
        mutableLiveDataCreateListing

     fun getMutableLiveDataPlacebid(): MutableLiveData<LoginModel> =
         mutableLiveDataPlacebid

     fun getMutableLiveTranspoterListForProduct(): MutableLiveData<LoginModel> =
         mutableLiveTranspoterListForProduct

     fun getMutableLiveDataTransportersListRemoveBid(): MutableLiveData<LoginModel> =
         mutableLiveTransportersListRemoveBid

   /*

    fun getMutableLiveDataApplyCoupon(): MutableLiveData<ImageModel> =
            mutableLiveDataApplyCoupon

    fun getMutableLiveDataApproveTransporterSender(): MutableLiveData<ImageModel> =
            mutableLiveDataApproveTransporterSender

    fun getMutableLiveDataRemoveTransporter(): MutableLiveData<ImageModel> =
            mutableLiveDataRemoveTransporter

    fun getMutableLiveDataAddProduct(): MutableLiveData<ImageModel> =
            mutableLiveDataAddProduct

    fun getMutableLiveDataGetTransporterList(): MutableLiveData<TransporterListModel> =
            mutableLiveDataGetTransporterList

    fun getMutableLiveDataApproveTransporterAdmin(): MutableLiveData<ImageModel> =
            mutableLiveDataApproveTransporterAdmin

    fun getMutableLiveDataApplyTransporter(): MutableLiveData<ImageModel> =
            mutableLiveDataApplyFromTransporter

    fun getMutableLiveDataMonthDateList(): MutableLiveData<DateListModel> =
        mutableLiveDataMonthDateList

    fun getMutableLiveDataEstimatedPrice(): MutableLiveData<EstimatedPriceModel> =
        mutableLiveDataEstimatedPrice
*/

    /*fun uploadPhotoArray(image : Array<MultipartBody.Part?>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.uploadPhotoes(image)
                    userImage?.let {
                        mutableLiveDataImageArray.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", "upload user image Exception : $e")

                }
            }
        }
    }

    fun getEstimatedPrice(weight : Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.getEstimatedPrice(weight)
                    userImage?.let {
                        mutableLiveDataEstimatedPrice.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " estimated price Exception : $e")

                }
            }
        }
    }
    fun applyCoupon(couponCode : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.applyCoupon(couponCode)
                    userImage?.let {
                        mutableLiveDataApplyCoupon.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " coupon code  Exception : $e")

                }
            }
        }
    }
    fun getPickupAmount(couponCode : String,amount : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.getPickupAmount(couponCode,amount)
                    userImage?.let {
                        mutableLiveDataGetPickUpAmount.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " coupon code  Exception : $e")

                }
            }
        }
    }
    fun getPickupAmountWithoutCoupon(amount : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.getPickupAmountWithoutCoupon(amount)
                    userImage?.let {
                        mutableLiveDataGetPickUpAmount.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " coupon code  Exception : $e")

                }
            }
        }
    }
    fun addProduct(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.addProduct(jsonObject)
                    userImage?.let {
                        mutableLiveDataAddProduct.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " add product Exception : $e")

                }
            }
        }
    }
    fun getBanners() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.getBanners()
                    userImage?.let {
                        mutableLiveDataBanners.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " banners Exception : $e")

                }
            }
        }
    }
  */


     fun getTransportersListForProduct(jsonObject: JsonObject) {
         viewModelScope.launch {
             withContext(Dispatchers.IO){
                 try {

                     val userIma  = productRepository?.getTransportersListForProduct(jsonObject)
                     userIma?.let {
                         mutableLiveTranspoterListForProduct.postValue(it)
                     }

                 }catch (httpException : HttpException){
                     try {
                         val errorResponse =
                             AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                         errorResponse?.let {
                             getErrorMutableLiveData().postValue(it)
                         }
                     }catch (e : Exception){
                         val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                             "Please try again later , our server is having some problem","")
                         errorResponse?.let {
                             getErrorMutableLiveData().postValue(it)
                         }
                     }

                 } catch (e : Exception){
                     val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                     errorResponse.let {
                         getErrorMutableLiveData().postValue(it)
                     }
                     Log.d("TAG", " get own products Exception : $e")

                 }
             }
         }
     }

     fun getTransportersListRemoveBid(jsonObject: JsonObject) {
         viewModelScope.launch {
             withContext(Dispatchers.IO){
                 try {

                     val userIma  = productRepository?.getTransportersListRemoveBid(jsonObject)
                     userIma?.let {
                         mutableLiveTransportersListRemoveBid.postValue(it)
                     }

                 }catch (httpException : HttpException){
                     try {
                         val errorResponse =
                             AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                         errorResponse?.let {
                             getErrorMutableLiveData().postValue(it)
                         }
                     }catch (e : Exception){
                         val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                             "Please try again later , our server is having some problem","")
                         errorResponse?.let {
                             getErrorMutableLiveData().postValue(it)
                         }
                     }

                 } catch (e : Exception){
                     val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                     errorResponse.let {
                         getErrorMutableLiveData().postValue(it)
                     }
                     Log.d("TAG", " get own products Exception : $e")

                 }
             }
         }
     }




    fun getUserListings(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = productRepository?.getUserListings(jsonObject)
                    userIma?.let {
                        mutableLiveDataProductsData.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " get own products Exception : $e")

                }
            }
        }
    }


     fun getPlacebid(jsonObject: JsonObject) {
         viewModelScope.launch {
             withContext(Dispatchers.IO){
                 try {

                     val userIma  = productRepository?.getPlacebid(jsonObject)
                     userIma?.let {
                         mutableLiveDataPlacebid.postValue(it)
                     }

                 }catch (httpException : HttpException){
                     try {
                         val errorResponse =
                             AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                         errorResponse?.let {
                             getErrorMutableLiveData().postValue(it)
                         }
                     }catch (e : Exception){
                         val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                             "Please try again later , our server is having some problem","")
                         errorResponse?.let {
                             getErrorMutableLiveData().postValue(it)
                         }
                     }

                 } catch (e : Exception){
                     val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                     errorResponse.let {
                         getErrorMutableLiveData().postValue(it)
                     }
                     Log.d("TAG", " get own products Exception : $e")

                 }
             }
         }
     }





    fun createListing(title: RequestBody, height: RequestBody, width: RequestBody, depth: RequestBody, weight: RequestBody, price: RequestBody,
                      more_people_needed: RequestBody, pickup_date: RequestBody, dropoff_date: RequestBody, pickup_latitude: RequestBody,
                      pickup_longitude: RequestBody, pickup_address: RequestBody, pickup_address_note: RequestBody, dropoff_latitude: RequestBody,
                      dropoff_longitude: RequestBody, dropoff_address: RequestBody, dropoff_address_note: RequestBody, distance: RequestBody,
                      image1 : MultipartBody.Part, image2 : MultipartBody.Part, image3 : MultipartBody.Part) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.createListing(title,height,width,depth,weight,price,more_people_needed,pickup_date,dropoff_date,
                        pickup_latitude,pickup_longitude,pickup_address,pickup_address_note,dropoff_latitude,dropoff_longitude,dropoff_address,
                        dropoff_address_note,distance,image1,image2,image3)
                    userImage?.let {
                        mutableLiveDataCreateListing.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " get own products Exception : $e")

                }
            }
        }
    }


    fun getAllListings(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.getAllListings(jsonObject)
                    userImage?.let {
                        mutableLiveDataAllListings.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " get all listings Exception : $e")

                }
            }
        }
    }


     fun getDeleteProduct(jsonObject: JsonObject) {
         viewModelScope.launch {
             withContext(Dispatchers.IO){
                 try {
                     val userImage  = productRepository?.getDeleteProduct(jsonObject)
                     userImage?.let {
                         mutableLiveDataDeleteProduct.postValue(it)
                     }

                 }catch (httpException : HttpException){
                     try {
                         val errorResponse =
                             AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                         errorResponse?.let {
                             getErrorMutableLiveData().postValue(it)
                         }
                     }catch (e : Exception){
                         val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                             "Please try again later , our server is having some problem","")
                         errorResponse?.let {
                             getErrorMutableLiveData().postValue(it)
                         }
                     }

                 } catch (e : Exception){
                     val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                     errorResponse.let {
                         getErrorMutableLiveData().postValue(it)
                     }
                     Log.d("TAG", " get all listings Exception : $e")

                 }
             }
         }
     }

     /*
    fun getSearchedAllProducts(search: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.getSearchedAllProducts(search)
                    userImage?.let {
                        mutableLiveDataAllProducts.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " get all products Exception : $e")

                }
            }
        }
    }
    fun getProductDetail(id : Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.getProductDetail(id)
                    userImage?.let {
                        mutableLiveDataOwnProductDetail.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " get  product detail Exception : $e")

                }
            }
        }
    }
    fun approve_transporter_admin(userId : Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.approve_transporter_admin(userId)
                    userImage?.let {
                        mutableLiveDataApproveTransporterAdmin.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " approve transporter  detail Exception : $e")

                }
            }
        }
    }
    fun applyFromTransporterOnProduct(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.applyFromTransporterOnProduct(jsonObject)
                    userImage?.let {
                        mutableLiveDataApplyFromTransporter.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " apply transporter  detail Exception : $e")
                }
            }
        }
    }
    fun editApplyFromTransporterOnProduct(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.editApplyFromTransporterOnProduct(jsonObject)
                    userImage?.let {
                        mutableLiveDataApplyFromTransporter.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " edit apply transporter  detail Exception : $e")
                }
            }
        }
    }

    fun getTransportersForProduct(productId : Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.getTransportersForProduct(productId)
                    userImage?.let {
                        mutableLiveDataGetTransporterList.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " get transporter list Exception : $e")

                }
            }
        }
    }
    fun removeTransporter(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.removeTransporter(jsonObject)
                    userImage?.let {
                        mutableLiveDataRemoveTransporter.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " remove transporter list Exception : $e")

                }
            }
        }
    }
    fun approveTransporterSender(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.approveTransporterSender(jsonObject)
                    userImage?.let {
                        mutableLiveDataApproveTransporterSender.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " approve transporter list Exception : $e")

                }
            }
        }
    }
    fun reportProduct(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.reportProduct(jsonObject)
                    userImage?.let {
                        mutableLiveDataReportProduct.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", " report product list Exception : $e")

                }
            }
        }
    }


    fun listingOngoingStates(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.listingOngoingStates(jsonObject)
                    userImage?.let {
                        mutableLiveDataPostOngoingStates.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", "listing ongoing states Exception : $e")

                }
            }
        }
    }
    fun postRating(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.postRating(jsonObject)
                    userImage?.let {
                        mutableLiveDataPostRating.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", "Post Rating Exception : $e")

                }
            }
        }
    }*/
    fun getSingleListingData(productId: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val userImage  = productRepository?.getSingleListingData(productId)
                    userImage?.let {
                        mutableLiveDataProductOngoingStates.postValue(it)
                    }

                }catch (httpException : HttpException){
                    try {
                        val errorResponse =
                                AppUtils.getErrorResponse(httpException.response()?.errorBody()?.string())
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }catch (e : Exception){
                        val errorResponse = ErrorResponse("",0,"Please try again later , our server is having some problem",
                                "Please try again later , our server is having some problem","")
                        errorResponse?.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", "listing ongoing states Exception : $e")

                }
            }
        }
    }
}
