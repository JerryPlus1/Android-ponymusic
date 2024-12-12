package me.wcy.music.widget.loadsir

import android.content.Context
import android.view.View
import com.kingja.loadsir.callback.Callback
import me.wcy.music.R


class SoundWaveLoadingCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.load_sir_loading_sound_wave
    }

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        return true
    }
}