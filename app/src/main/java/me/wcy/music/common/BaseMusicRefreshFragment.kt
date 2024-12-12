package me.wcy.music.common

import com.kingja.loadsir.callback.Callback
import top.wangchenyan.common.ui.fragment.BaseRefreshFragment
import me.wcy.music.widget.loadsir.SoundWaveLoadingCallback


abstract class BaseMusicRefreshFragment<T> : BaseRefreshFragment<T>() {

    override fun getLoadingCallback(): Callback {
        return SoundWaveLoadingCallback()
    }

    override fun showLoadSirLoading() {
        loadService?.showCallback(SoundWaveLoadingCallback::class.java)
    }
}