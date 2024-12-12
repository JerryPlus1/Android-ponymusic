package me.wcy.music.discover.recommend.song.bean

import com.google.gson.annotations.SerializedName
import me.wcy.music.common.bean.SongData


data class RecommendSongListData(
    @SerializedName("dailySongs")
    val dailySongs: List<SongData> = emptyList()
)
