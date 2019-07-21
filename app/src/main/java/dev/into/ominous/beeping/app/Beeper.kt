package dev.into.ominous.beeping.app

import android.content.Context
import android.media.MediaPlayer

object Beeper {
    var mp: MediaPlayer = MediaPlayer()
    var detectShakes = false
    var foreground = false
}