package me.wcy.music.common.dialog.songmenu

import android.view.View


interface MenuItem {
    val name: String
    fun onClick(view: View)
}

data class SimpleMenuItem(
    override val name: String,
    val onClick: (View) -> Unit = {}
) : MenuItem {
    override fun onClick(view: View) {
        onClick.invoke(view)
    }
}
