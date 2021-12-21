package com.livo.nuo.viewModel.ongoingstate

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.livo.nuo.models.LoginModel
import com.livo.nuo.netUtils.response.ErrorResponse
import com.livo.nuo.repository.ongoingstate.OngoingRepository
import com.livo.nuo.utility.AppUtils
import com.livo.nuo.viewModel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.lang.Exception

class OngoingStateModel(application: Application) :  BaseViewModel(application){

    private var ongoingRepository : OngoingRepository? = null


    private var mutableLiveDataViewAllState : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataSenderApproveTrns : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataTrnsAcceptApproval : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataTrnsDeclineApproval : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataPlacebid : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataMakePayment : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataChangePaymentStatus : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataPickupDropoffListing : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataTrnsCompleteListing : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataSenderCompletesListing : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataDeleteBid : MutableLiveData<LoginModel> = MutableLiveData()

    init {
        ongoingRepository = OngoingRepository()
    }

    fun getMutableLiveDataViewAllState(): MutableLiveData<LoginModel> =
        mutableLiveDataViewAllState

    fun getMutableLiveDataSenderApproveTrns(): MutableLiveData<LoginModel> =
        mutableLiveDataSenderApproveTrns

    fun getMutableLiveDataTrnsAcceptApproval(): MutableLiveData<LoginModel> =
        mutableLiveDataTrnsAcceptApproval

    fun getMutableLiveDataTrnsDeclineApproval(): MutableLiveData<LoginModel> =
        mutableLiveDataTrnsDeclineApproval

    fun getMutableLiveDataPlacebid(): MutableLiveData<LoginModel> =
        mutableLiveDataPlacebid

    fun getMutableLiveDataMakePayment(): MutableLiveData<LoginModel> =
        mutableLiveDataMakePayment

    fun getMutableLiveDataChangePaymentStatus(): MutableLiveData<LoginModel> =
        mutableLiveDataChangePaymentStatus

    fun getMutableLiveDataPickupDropoffListing(): MutableLiveData<LoginModel> =
        mutableLiveDataPickupDropoffListing

    fun getMutableLiveDataTrnsCompleteListing(): MutableLiveData<LoginModel> =
        mutableLiveDataTrnsCompleteListing

    fun getMutableLiveDataSenderCompletesListing(): MutableLiveData<LoginModel> =
        mutableLiveDataSenderCompletesListing

    fun getMutableLiveDataDeleteBid(): MutableLiveData<LoginModel> =
        mutableLiveDataDeleteBid




    fun getDeleteBid(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = ongoingRepository?.getDeleteBid(jsonObject)
                    userIma?.let {
                        mutableLiveDataDeleteBid.postValue(it)
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


    fun getSenderCompletesListing(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = ongoingRepository?.getSenderCompletesListing(jsonObject)
                    userIma?.let {
                        mutableLiveDataSenderCompletesListing.postValue(it)
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


    fun getTrnsCompleteListing(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = ongoingRepository?.getTrnsCompleteListing(jsonObject)
                    userIma?.let {
                        mutableLiveDataTrnsCompleteListing.postValue(it)
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

    fun getMakePayment(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = ongoingRepository?.getMakePayment(jsonObject)
                    userIma?.let {
                        mutableLiveDataMakePayment.postValue(it)
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


    fun getChangePaymentStatus(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = ongoingRepository?.getChangePaymentStatus(jsonObject)
                    userIma?.let {
                        mutableLiveDataChangePaymentStatus.postValue(it)
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


    fun getViewAllState(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = ongoingRepository?.getViewAllState(jsonObject)
                    userIma?.let {
                        mutableLiveDataViewAllState.postValue(it)
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


    fun getSenderApproveTrns(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = ongoingRepository?.getSenderApproveTrns(jsonObject)
                    userIma?.let {
                        mutableLiveDataSenderApproveTrns.postValue(it)
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


    fun getTrnsAcceptApproval(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = ongoingRepository?.getTrnsAcceptApproval(jsonObject)
                    userIma?.let {
                        mutableLiveDataTrnsAcceptApproval.postValue(it)
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


    fun getTrnsDeclineApproval(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = ongoingRepository?.getTrnsDeclineApproval(jsonObject)
                    userIma?.let {
                        mutableLiveDataTrnsDeclineApproval.postValue(it)
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


    fun getChangeBidPrice(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = ongoingRepository?.getChangeBidPrice(jsonObject)
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



    fun pickupDropoffListing(offer_id: RequestBody, state: RequestBody, image : MultipartBody.Part) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = ongoingRepository?.pickupDropoffListing(offer_id,state,image)
                    userIma?.let {
                        mutableLiveDataPickupDropoffListing.postValue(it)
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


}