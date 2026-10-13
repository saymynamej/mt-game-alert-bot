package ru.smn.mt.game.bot.service.notify

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.smn.mt.game.bot.config.BotProperties
import ru.smn.mt.game.bot.dto.EventData
import ru.smn.mt.game.bot.ext.buildTrainingDateString
import ru.smn.mt.game.bot.service.poll.PollService
import ru.smn.mt.game.bot.service.storage.EventStorage
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class TrainingsNotificationService(
    private val botProperties: BotProperties,
    private val eventStorage: EventStorage,
    private val pollService: PollService,
) : NotificationService {
    override fun notifyEvent() {
        val today = LocalDate.now().dayOfWeek
        botProperties.trainingsAsMap
            .filter { (_, trainingInfo) -> trainingInfo.enabled }
            .forEach { (trainingName, trainingInfo) ->
                val daysUntilTraining = (trainingInfo.day.value - today.value + 7) % 7

                if (daysUntilTraining == trainingInfo.notifyDaysBefore) {
                    val targetTrainingDate = LocalDate.now().plusDays(daysUntilTraining.toLong())
                    val datePart = targetTrainingDate.toEpochDay() shl 32
                    val namePart = trainingName.hashCode().toLong() and 0xFFFFFFFFL
                    val trainingId: Long = datePart or namePart

                    if (!eventStorage.isExist(trainingId)) {
                        val dateDescription = buildTrainingDateString(
                            trainingInfo.day,
                            trainingInfo.startTime,
                            trainingInfo.endTime
                        )

                        val pollMessage = "Тренировка: ${trainingInfo.place}. $dateDescription"

                        pollService.createPollAndPin(trainingInfo.chatId, pollMessage)

                        eventStorage.save(
                            EventData(
                                trainingId,
                                pollMessage,
                                trainingInfo.chatId.toLong(),
                                LocalDateTime.now()
                            )
                        )
                        LOGGER.info("Провели нотификацию о тренировке $trainingName на дату $targetTrainingDate")
                    } else {
                        LOGGER.info("Уже провели нотификацию о тренировке $trainingName на дату $targetTrainingDate")
                    }
                }
            }
    }

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(TrainingsNotificationService::class.java)
    }
}