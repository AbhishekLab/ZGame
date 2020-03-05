package com.zgame.zgame.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.zgame.zgame.model.UserResponseModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ApiInterface {

    @FormUrlEncoded
    @POST("account/login")
    fun login(@Field("email") email: String, @Field("password") password: String, @Field("companyId") companyId: Int): Call<UserResponseModel>


    companion object Factory {
        private const val BASE_URL: String = "https://devirm.krahejacorp.co.in/api/"

        fun create(): ApiInterface {

            val client = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .addNetworkInterceptor(StethoInterceptor())


            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(client.build())
                    .build()
            return retrofit.create(ApiInterface::class.java)
        }

    }

    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response =
                chain.proceed(chain.request().newBuilder().addHeader("AuthorizeToken", "401b09eab3c013d4ca54922bb802bec8fd5318192b0a75f201d8b3727429090fb337591abd3e44453b954555b7a0812e1081c39b740293f765eae731f5a65ed1").build())
    }
}
