package me.wcy.music.common.bean

import com.google.gson.annotations.SerializedName


data class OriginSongSimpleData(
    @SerializedName("songId")
    val songId: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("artists")
    val artists: List<ArtistData> = listOf(),
    @SerializedName("albumMeta")
    val albumMeta: AlbumData = AlbumData()
)