package me.wcy.music.mine

import me.wcy.music.discover.playlist.square.bean.PlaylistListData
import me.wcy.music.mine.collect.song.bean.CollectSongResult
import me.wcy.music.net.HttpClient
import me.wcy.music.service.likesong.bean.LikeSongListData
import me.wcy.music.storage.preference.ConfigPreferences
import retrofit2.Retrofit
import retrofit2.http.POST
import retrofit2.http.Query
import top.wangchenyan.common.net.NetResult
import top.wangchenyan.common.net.gson.GsonConverterFactory
import top.wangchenyan.common.utils.GsonUtils
import top.wangchenyan.common.utils.ServerTime


interface MineApi {

    @POST("user/playlist")
    suspend fun getUserPlaylist(
        @Query("uid") uid: Long,
        @Query("limit") limit: Int = 1000,
        @Query("timestamp") timestamp: Long = ServerTime.currentTimeMillis()
    ): PlaylistListData

    
    @POST("playlist/subscribe")
    suspend fun collectPlaylist(
        @Query("id") id: Long,
        @Query("t") t: Int,
        @Query("timestamp") timestamp: Long = ServerTime.currentTimeMillis()
    ): NetResult<Any>

    
    @POST("playlist/tracks")
    suspend fun collectSong(
        @Query("pid") pid: Long,
        @Query("tracks") tracks: String,
        @Query("op") op: String = "add",
        @Query("timestamp") timestamp: Long = ServerTime.currentTimeMillis()
    ): CollectSongResult

    
    @POST("like")
    suspend fun likeSong(
        @Query("id") id: Long,
        @Query("like") like: Boolean = true,
        @Query("timestamp") timestamp: Long = ServerTime.currentTimeMillis()
    ): NetResult<Any>

    
    @POST("likelist")
    suspend fun getMyLikeSongList(
        @Query("uid") uid: Long,
        @Query("timestamp") timestamp: Long = ServerTime.currentTimeMillis()
    ): LikeSongListData

    companion object {
        private val api: MineApi by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(ConfigPreferences.apiDomain)
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.gson, true))
                .client(HttpClient.okHttpClient)
                .build()
            retrofit.create(MineApi::class.java)
        }

        fun get(): MineApi = api
    }
}