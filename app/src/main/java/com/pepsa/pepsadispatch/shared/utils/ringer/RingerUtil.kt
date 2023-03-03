package com.pepsa.pepsadispatch.shared.utils.ringer

import android.content.Context
import android.media.MediaPlayer
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class RingerUtil @Inject constructor(@ApplicationContext context: Context) {
    private val ringTone: MediaPlayer? = try {
        MediaPlayer.create(context, R.raw.ring_tone_one)?.apply {
            isLooping = true
        }
    } catch (e: Exception) {
        null
    }

    fun startRinging() {
        try {
            ringTone?.start()
        } catch (e: Exception) {
            Timber.d("${DeliveryOrdersConstants.TAG_RING_TONE_ERROR_LOGGER}%s", e.localizedMessage)
        }
    }

    fun stopRinging() {
        try {
            ringTone?.stop()
        } catch (e: Exception) {
            Timber.d("${DeliveryOrdersConstants.TAG_RING_TONE_ERROR_LOGGER}%s", e.localizedMessage)
        }
    }
}
