package me.wcy.music.discover.playlist.detail.bean

import com.google.gson.annotations.SerializedName
import me.wcy.music.common.bean.SongData


data class SongListData(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("songs")
    val songs: List<SongData> = emptyList()
)
