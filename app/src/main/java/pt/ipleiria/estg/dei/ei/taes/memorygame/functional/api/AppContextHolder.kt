package pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api


import android.content.Context

object AppContextHolder {
    lateinit var appContext: Context
        public set

    fun initialize(context: Context) {
        appContext = context.applicationContext
    }
}
