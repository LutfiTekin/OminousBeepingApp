package dev.into.ominous.beeping.app

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast

infix fun String.logWith(string: String){
    if (BuildConfig.DEBUG) Log.d(string,this)
}

infix fun Int.shortToastWith(context: Context){
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(context,this, Toast.LENGTH_SHORT).show()
    }
}

infix fun Int.longToastWith(context: Context){
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(context,this, Toast.LENGTH_LONG).show()
    }
}