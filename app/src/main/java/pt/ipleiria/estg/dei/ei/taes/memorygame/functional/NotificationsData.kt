package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Notification(
    val title: String,
    val message: String,
    val timestamp: String,
    var isRead: Boolean = false
)



class NotificationsViewModel : ViewModel() {
    private val _notifications = mutableStateListOf(
        Notification("Session Started!", "", getCurrentFormattedTime(), false)
    )

    val notifications: SnapshotStateList<Notification> = _notifications

    fun markNotificationAsRead(index: Int) {
        _notifications[index] = _notifications[index].copy(isRead = true)
    }

    fun markAllNotificationsAsRead() {
        for (i in _notifications.indices) {
            if (!_notifications[i].isRead) {
                _notifications[i] = _notifications[i].copy(isRead = true)
            }
        }
    }
    fun clearAllNotifications(){
        _notifications.clear()
        _notifications.add(Notification("Session Started!", "", getCurrentFormattedTime(), false))
    }

    fun addNotification(title: String, message: String) {
        val timestamp = getCurrentFormattedTime()
        _notifications.add(0,Notification(title, message, timestamp, false))
    }

    fun getCurrentFormattedTime(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return LocalDateTime.now().format(formatter)
    }
}
