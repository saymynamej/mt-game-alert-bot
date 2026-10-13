package ru.smn.mt.game.bot.service.notify

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.smn.mt.game.bot.service.poll.PollService
import ru.smn.mt.game.bot.service.storage.EventStorage

@Service
class ForceNotificationService(
    private val eventStorage: EventStorage,
    private val pollService: PollService,
) : NotificationService {

    override fun notifyEvent() {
        eventStorage.findAllByForceResend().forEach {
            pollService.createPollAndPin(
                it.chatId.toString(),
                it.pollMessage
            )
            eventStorage.save(
                it.copy(
                    forceSend = false,
                    saveNewEntity = false
                )
            )
            LOGGER.info("Проверил повторную нотификацию события $it")
        }
    }

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(ForceNotificationService::class.java)
    }
}