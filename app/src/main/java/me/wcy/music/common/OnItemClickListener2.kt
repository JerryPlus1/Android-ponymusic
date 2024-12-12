package me.wcy.music.common


interface OnItemClickListener2<T> {
    fun onItemClick(item: T, position: Int)
    fun onMoreClick(item: T, position: Int)
}