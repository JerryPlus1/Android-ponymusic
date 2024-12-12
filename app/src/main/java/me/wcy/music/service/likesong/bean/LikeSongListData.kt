package me.wcy.music.service.likesong.bean

import com.google.gson.annotations.SerializedName


data class LikeSongListData(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("ids")
    val ids: Set<Long> = emptySet()
)
