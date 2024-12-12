package me.wcy.music.search.bean

import com.google.gson.annotations.SerializedName
import me.wcy.music.common.bean.PlaylistData
import me.wcy.music.common.bean.SongData


data class SearchResultData(
    @SerializedName("songs")
    val songs: List<SongData> = emptyList(),
    @SerializedName("songCount")
    val songCount: Int = 0,
    @SerializedName("playlists")
    val playlists: List<PlaylistData> = emptyList(),
    @SerializedName("playlistCount")
    val playlistCount: Int = 0,
)
