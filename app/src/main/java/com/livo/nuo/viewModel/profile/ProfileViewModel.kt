package com.livo.nuo.viewModel.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.livo.nuo.models.LoginModel
import com.livo.nuo.models.ProductListModel
import com.livo.nuo.netUtils.response.ErrorResponse
import com.livo.nuo.repository.profile.ProfileRepository
import com.livo.nuo.utility.AppUtils
import com.livo.nuo.viewModel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.lang.Exception

class ProfileViewModel(application: Application) :  BaseViewModel(application) {

    private var profileRepository : ProfileRepository? = null

    private var mutableLiveDataUserSettings : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataViewOwnProfile : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataViewChangeNumSendOtp : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataViewChangeNumChangeNumber : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataViewEditOwnProfile : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataViewChangeLang : MutableLiveData<LoginModel> = MutableLiveData()


    init {
        profileRepository = ProfileRepository()
    }

    fun getMutableLiveDataUserSettings(): MutableLiveData<LoginModel> =
        mutableLiveDataUserSettings

    fun getMutableLiveDataViewOwnProfile(): MutableLiveData<LoginModel> =
        mutableLiveDataViewOwnProfile

    fun getMutableLiveDataViewChangeNumSendOtp(): MutableLiveData<LoginModel> =
        mutableLiveDataViewChangeNumSendOtp

    fun getMutableLiveDataViewChangeNumChangeNumber(): MutableLiveData<LoginModel> =
        mutableLiveDataViewChangeNumChangeNumber

    fun getMutableLiveDataViewEditOwnProfile(): MutableLiveData<LoginModel> =
        mutableLiveDataViewEditOwnProfile

    fun getMutableLiveDataViewChangeLang(): MutableLiveData<LoginModel> =
        mutableLiveDataViewChangeLang


    fun getUserSettings() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = profileRepository?.getUserSettings()
                    userIma?.let {
                        mutableLiveDataUserSettings.postValue(it)
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



    fun getViewOwnProfile() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = profileRepository?.getViewOwnProfile()
                    userIma?.let {
                        mutableLiveDataViewOwnProfile.postValue(it)
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



    fun getchangeNumSendOtp(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = profileRepository?.getchangeNumSendOtp(jsonObject)
                    userIma?.let {
                        mutableLiveDataViewChangeNumSendOtp.postValue(it)
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

    fun getChangeLang(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = profileRepository?.getChangeLang(jsonObject)
                    userIma?.let {
                        mutableLiveDataViewChangeLang.postValue(it)
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


    fun getChangeNumChangeNumber(jsonObject: JsonObject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = profileRepository?.getChangeNumChangeNumber(jsonObject)
                    userIma?.let {
                        mutableLiveDataViewChangeNumChangeNumber.postValue(it)
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


    fun setEditOwnProfile(first_name: RequestBody, last_name: RequestBody, email: RequestBody
                          , dob: RequestBody, gender: RequestBody, user_type: RequestBody
                          , image : MultipartBody.Part) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {

                    val userIma  = profileRepository?.setEditOwnProfile(first_name,last_name,email,dob,gender,user_type,image)
                    userIma?.let {
                        mutableLiveDataViewEditOwnProfile.postValue(it)
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