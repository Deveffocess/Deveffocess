package com.livo.nuo.netUtils.services

import com.google.gson.GsonBuilder
import com.livo.nuo.manager.SessionManager
import com.livo.nuo.utility.MyAppSession
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class api {


        var retrofit: Retrofit? = null
        val BASE_URL ="https://api.livo.nu/api/"
            //"https://api.dev.livo.nu/api/"

        fun getRetrofitInstance(): Retrofit? {
            if (retrofit == null) {
                val okHttpClient = OkHttpClient().newBuilder()
                    .connectTimeout(6000, TimeUnit.SECONDS)
                    .readTimeout(6000, TimeUnit.SECONDS)
                    .writeTimeout(6000, TimeUnit.SECONDS)
                    .build()
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit
        }

    fun headers(): Map<String, String> {
        if(SessionManager.getLoginModel() != null){
            return mapOf(Pair(MyAppSession.Autherization, "Bearer "+ SessionManager.getLoginModel()?.token ?: ""))

        }else{
            return mapOf(Pair(MyAppSession.Autherization, ""))

        }
    }

    }