package ru.smn.mt.game.bot.schedule

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.smn.mt.game.bot.service.notify.NotificationService

@Component
class MTGameNotificationScheduler(
    private val notificationServices: List<NotificationService>
) {

    @PostConstruct
    fun init() {
        notificationServices.forEach {
            try {
                it.notifyEvent()
            } catch (e: Exception) {
                LOGGER.error("Exception while notifying game notifications", e)
            }
        }
    }

    @Scheduled(cron = "0 */5 * * * *")
    fun notifyGames() {
        notificationServices.forEach {
            try {
                it.notifyEvent()
            } catch (e: Exception) {
                LOGGER.error("Exception while notifying game notifications", e)
            }
        }
    }

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(MTGameNotificationScheduler::class.java)
    }
}