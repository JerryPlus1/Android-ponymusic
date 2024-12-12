package me.wcy.music.discover.playlist.square.bean

import com.google.gson.annotations.SerializedName


data class PlaylistTagListData(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("tags")
    val tags: List<PlaylistTagData> = emptyList(),
)
