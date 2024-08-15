package com.example.logicgo.service

import com.intuit.sdp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

fun client(): SearchApi {
    val okClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            chain.proceed(
                request
                    .newBuilder()
                    .addHeader("Cache-Control", "no-cache")
                    .method(request.method, request.body)
                    .build()
            )
        }
        .addInterceptor(if (BuildConfig.DEBUG) HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY) else HttpLoggingInterceptor()
        )
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    return Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okClient)
        .build().create(SearchApi::class.java)
}

interface SearchApi {
    @GET("posts")
    suspend fun getPosts(): Response<Any>

//    @GET("images")
//    suspend fun search(
//        @Query("query") query: String,
//        @Query("offset") offset: Int,
//        @Query("limit") limit: Int = 60,
//        @Query("screen[width]") width: Int = w,
//        @Query("screen[height]") height: Int = h,
//        @Query("sort") sort: String = "rating",
//        @Query("types[]") types: String = "free,private",
//        @Query("lang") lang: String = "en",
//        @Query("cost_variant") costVariant: String = "android_cost_1",
//    ): Response<HomeRes>
}
