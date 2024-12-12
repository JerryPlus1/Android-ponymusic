package me.wcy.music.consts


object PreferenceName {
    val ACCOUNT = "account".assemble()
    val CONFIG = "config".assemble()
    val SEARCH = "search".assemble()

    private fun String.assemble(): String {
        return "music_$this"
    }
}