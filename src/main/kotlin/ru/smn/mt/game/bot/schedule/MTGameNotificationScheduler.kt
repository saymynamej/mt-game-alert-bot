package ru.smn.mt.game.bot.schedule

import jakarta.annotation.PostConstruct
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.smn.mt.game.bot.service.NotificationService

@Component
class MTGameNotificationScheduler(
    private val notificationService: NotificationService
) {

    @PostConstruct
    fun init() {
        notificationService.notifyGames()
    }

    @Scheduled(cron = "0 */5 * * * *")
    fun notifyGames() {
        notificationService.notifyGames()
    }
}