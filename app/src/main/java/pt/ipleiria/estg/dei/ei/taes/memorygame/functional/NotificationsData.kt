package pt.ipleiria.estg.dei.ei.taes.memorygame.functional

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import pt.ipleiria.estg.dei.ei.taes.memorygame.ui.screen.Notification

class NotificationsViewModel : ViewModel() {
    private val _notifications = mutableStateListOf(
        Notification("Compra Concluída", "Você comprou 500 moedas com sucesso.", "2024-11-28 10:45", false),
        Notification("Novo Recorde!", "Parabéns! Você acabou de bater o recorde.", "2024-11-28 09:30", true),
        Notification("Recompensa Recebida", "Você ganhou 100 moedas bônus.", "2024-11-28 08:15", false),
        Notification("Compra Concluída", "Você comprou 500 moedas com sucesso.", "2024-11-27 10:45", false),
        Notification("Novo Recorde!", "Parabéns! Você acabou de bater o recorde.", "2024-11-27 09:30", true),
        Notification("Recompensa Recebida", "Você ganhou 100 moedas bônus.", "2024-11-27 08:15", false),
        Notification("Compra Concluída", "Você comprou 500 moedas com sucesso.", "2024-11-26 10:45", false),
        Notification("Novo Recorde!", "Parabéns! Você acabou de bater o recorde.", "2024-11-26 09:30", true),
        Notification("Recompensa Recebida", "Você ganhou 100 moedas bônus.", "2024-11-26 08:15", false),
        Notification("Compra Concluída", "Você comprou 500 moedas com sucesso.", "2024-11-25 10:45", false),
        Notification("Novo Recorde!", "Parabéns! Você acabou de bater o recorde.", "2024-11-25 09:30", true),
        Notification("Recompensa Recebida", "Você ganhou 100 moedas bônus.", "2024-11-25 08:15", false),
        Notification("Compra Concluída", "Você comprou 500 moedas com sucesso.", "2024-11-24 10:45", false),
        Notification("Novo Recorde!", "Parabéns! Você acabou de bater o recorde.", "2024-10-22 09:30", true),
        Notification("Recompensa Recebida", "Você ganhou 100 moedas bônus.", "2024-1-31 08:15", false)
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
}
