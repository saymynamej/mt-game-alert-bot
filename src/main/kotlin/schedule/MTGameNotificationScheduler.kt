package ru.vtb.dtc.schedule

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.vtb.dtc.service.NotificationService

@Component
class MTGameNotificationScheduler(
    @Qualifier("MTGameNotificationService")
    private val notificationService: NotificationService
) {

    @PostConstruct
    fun init() {
        notificationService.notify()
    }

    @Scheduled(cron = "0 */5 * * * *")
    fun notifyGames() {
        notificationService.notify()
    }
}