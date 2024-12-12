package me.wcy.music.discover.banner

import com.google.gson.annotations.SerializedName


data class BannerListData(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("banners")
    val banners: List<BannerData> = emptyList(),
)