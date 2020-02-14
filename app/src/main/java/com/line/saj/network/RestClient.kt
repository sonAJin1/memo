package com.line.saj.network

import com.line.saj.BuildConfig
import com.line.saj.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


//TODO: 지워 줄 것

open class RestClient {

    val BASE_URL = Constants.SERVER_URL
    private lateinit var serverApi: ServerAPI
    private val interceptor = HttpLoggingInterceptor()


    init {

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client =
            OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build()
        val retrofitBuilder = Retrofit.Builder().baseUrl(BASE_URL)

        if (BuildConfig.DEBUG) {
            retrofitBuilder.client(client)
        }
        retrofitBuilder.apply {
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        }
        val retrofit = retrofitBuilder.build()
        serverApi = retrofit.create(ServerAPI::class.java)


        //return serverApi
    }

    fun getInstance(): ServerAPI {
        return serverApi
    }


}