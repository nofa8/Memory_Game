package pt.ipleiria.estg.dei.ei.taes.memorygame.functional.api


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import pt.ipleiria.estg.dei.ei.taes.memorygame.R
import pt.ipleiria.estg.dei.ei.taes.memorygame.functional.NotificationsViewModel
import kotlin.random.Random

object NotificationHelper {
    private const val CHANNEL_ID = "default"
    lateinit var notificationView: NotificationsViewModel
    @SuppressLint("MissingPermission")
    fun showNotification(context: Context, title: String, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
        notificationView.addNotification(title, message)
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(Random.nextInt(), notificationBuilder.build())
    }
}
