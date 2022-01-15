package com.livo.nuo.viewModel.prelogin

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.livo.nuo.manager.SessionManager
import com.livo.nuo.models.ExtraDataModel
import com.livo.nuo.models.LoginModel
import com.livo.nuo.netUtils.response.ErrorResponse
import com.livo.nuo.viewModel.base.BaseViewModel
import com.livo.nuo.repository.login.LoginRepository
import com.livo.nuo.utility.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.Exception

 class LoginViewModel(application: Application) : BaseViewModel(application){

    private var loginRepository : LoginRepository? = null
    private var mutableLiveDataLogin : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataCreateSession:MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataOtp : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataSignUp : MutableLiveData<LoginModel> = MutableLiveData()
    private var mutableLiveDataExtraDataModel:MutableLiveData<ExtraDataModel> = MutableLiveData()


    init {
        loginRepository = LoginRepository()
    }


    fun getmutableLiveDataCreateSession(): MutableLiveData<LoginModel> =
        mutableLiveDataCreateSession

    fun getMutableLiveDataLogin(): MutableLiveData<LoginModel> =
            mutableLiveDataLogin

    fun getMutableLiveDataExtraDataModel(): MutableLiveData<ExtraDataModel> =
        mutableLiveDataExtraDataModel

    fun getMutableLiveDataOtp() : MutableLiveData<LoginModel> =
        mutableLiveDataOtp


    fun getMutableLiveDataSignUp() : MutableLiveData<LoginModel> =
            mutableLiveDataSignUp



    fun getOtp(jsonObject: JsonObject) {
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                try {
                    val otpModel  = loginRepository?.getOtp(jsonObject)
                    otpModel?.let {
                        mutableLiveDataOtp.postValue(it)
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
                }catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", "OTP Exception : $e")
                }
            }
        }
    }


    fun createSession(jsonObject: JsonObject) {
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                try {
                    val login  = loginRepository?.createSession(jsonObject)
                    login?.let {
                        mutableLiveDataCreateSession.postValue(it)
                        SessionManager.setLoginModel(login)
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
                }catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", "OTP Exception : $e")
                }
            }
        }
    }


    fun extraData() {

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val extraDataModel  = loginRepository?.extraData()
                    extraDataModel?.let {
                        mutableLiveDataExtraDataModel.postValue(it)
                        SessionManager.setExtraDataModel(extraDataModel)
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
                        errorResponse.let {
                            getErrorMutableLiveData().postValue(it)
                        }
                    }

                } catch (e : Exception){
                    val errorResponse = ErrorResponse("",0,e.message,e.message!!,"")
                    errorResponse.let {
                        getErrorMutableLiveData().postValue(it)
                    }
                    Log.d("TAG", "extra Data Exception : $e")

                }
            }
        }
    }

}
