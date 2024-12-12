package me.wcy.music.common.bean

import com.google.gson.annotations.SerializedName


data class ArtistData(
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("tns")
    val tns: List<Any> = listOf(),
    @SerializedName("alias")
    val alias: List<Any> = listOf()
)