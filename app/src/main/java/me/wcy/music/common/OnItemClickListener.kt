package me.wcy.music.common


interface OnItemClickListener<T> {
    fun onItemClick(item: T, position: Int)
}