package me.wcy.music.common

import com.kingja.loadsir.callback.Callback
import top.wangchenyan.common.ui.fragment.SimpleRefreshFragment
import me.wcy.music.widget.loadsir.SoundWaveLoadingCallback


abstract class SimpleMusicRefreshFragment<T> : SimpleRefreshFragment<T>() {

    override fun getLoadingCallback(): Callback {
        return SoundWaveLoadingCallback()
    }

    override fun showLoadSirLoading() {
        loadService?.showCallback(SoundWaveLoadingCallback::class.java)
    }
}