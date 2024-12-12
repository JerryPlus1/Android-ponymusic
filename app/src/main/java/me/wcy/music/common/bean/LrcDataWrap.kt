package me.wcy.music.common.bean

import com.google.gson.annotations.SerializedName


data class LrcDataWrap(
    @SerializedName("code")
    val code: Int = -1,
    @SerializedName("lrc")
    val lrc: LrcData = LrcData()
)
