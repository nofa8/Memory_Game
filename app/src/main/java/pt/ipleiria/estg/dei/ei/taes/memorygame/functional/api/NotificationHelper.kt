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
    private const val GROUP_KEY = "notification_group"
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

        // Add to the ViewModel's notification list
        notificationView.addNotification(title, message)

        // Create a unique notification ID
        val notificationId = Random.nextInt()

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setGroup(GROUP_KEY)

        // Show the notification
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationId, notificationBuilder.build())

        // Update the summary notification
        showSummaryNotification(context)
    }

    @SuppressLint("MissingPermission")
    fun showSummaryNotification(context: Context) {
        // Count unread notifications in ViewModel
        val unreadCount = notificationView.notifications.count { !it.isRead }

        // Create a summary notification for grouped notifications
        val summaryNotification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Memory Game Notifications")
            .setContentText("You have $unreadCount new notifications")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setGroup(GROUP_KEY)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(0, summaryNotification)
    }
}