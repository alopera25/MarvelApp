package com.marvelapp.data

import com.marvelapp.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import java.math.BigInteger
import java.security.MessageDigest

object CharactersClient {

    private val okHttpClient = okhttp3.OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        })
        .addInterceptor(::apiKeyAsQuery)
        .build()


    private val json = Json {
        ignoreUnknownKeys = true
    }

    val instance = Retrofit.Builder()
        .baseUrl("https://gateway.marvel.com")
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create<CharactersService>()

}

private fun apiKeyAsQuery(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()
    val originalHttpUrl = originalRequest.url

    val ts = System.currentTimeMillis().toString()
    val publicKey = BuildConfig.MARVEL_API_KEY
    val privateKey = BuildConfig.MARVEL_PRIVATE_API_KEY
    val hash = (ts + privateKey + publicKey).md5()

    val url = originalHttpUrl.newBuilder()
        .addQueryParameter("ts", ts)
        .addQueryParameter("apikey", publicKey)
        .addQueryParameter("hash", hash)
        .build()

    val requestBuilder = originalRequest.newBuilder()
        .url(url)

    val request = requestBuilder.build()
    return chain.proceed(request)
}
fun String.md5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return BigInteger(1, bytes).toString(16).padStart(32, '0')
}
