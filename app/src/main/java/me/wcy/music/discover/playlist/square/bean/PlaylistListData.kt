package me.wcy.music.discover.playlist.square.bean

import com.google.gson.annotations.SerializedName
import me.wcy.music.common.bean.PlaylistData


data class PlaylistListData(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("playlists", alternate = ["playlist", "recommend", "list"])
    val playlists: List<PlaylistData> = emptyList(),
)
