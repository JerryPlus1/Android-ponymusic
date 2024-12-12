package me.wcy.music.search

import top.wangchenyan.common.net.NetResult
import top.wangchenyan.common.net.gson.GsonConverterFactory
import top.wangchenyan.common.utils.GsonUtils
import me.wcy.music.net.HttpClient
import me.wcy.music.search.bean.SearchResultData
import me.wcy.music.storage.preference.ConfigPreferences
import retrofit2.Retrofit
import retrofit2.http.POST
import retrofit2.http.Query


interface SearchApi {

    
    @POST("cloudsearch")
    suspend fun search(
        @Query("type") type: Int,
        @Query("keywords") keywords: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): NetResult<SearchResultData>

    companion object {
        private val api: SearchApi by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(ConfigPreferences.apiDomain)
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.gson, true))
                .client(HttpClient.okHttpClient)
                .build()
            retrofit.create(SearchApi::class.java)
        }

        fun get(): SearchApi = api
    }
}