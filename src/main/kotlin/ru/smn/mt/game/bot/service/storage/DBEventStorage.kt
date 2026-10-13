package ru.smn.mt.game.bot.service.storage

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.smn.mt.game.bot.dto.EventData
import ru.smn.mt.game.bot.mapper.toDomain
import ru.smn.mt.game.bot.mapper.toEntity
import ru.smn.mt.game.bot.repository.EventRepository

@Service
class DBEventStorage(
    private val eventRepository: EventRepository
) : EventStorage {

    override fun findAllByForceResend(): Set<EventData> {
        return eventRepository.findAllByForceSend(true)
            .map { it.toDomain() }
            .toSet()
    }

    override fun isExist(gameId: Long): Boolean {
        return eventRepository.existsById(gameId)
    }

    override fun save(eventData: EventData) {
        eventRepository.save(eventData.toEntity()).let {
            LOGGER.info("Сохранили уведомление: $it")
        }
    }

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(DBEventStorage::class.java)
    }
}