package ru.smn.mt.game.bot.service.storage

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.smn.mt.game.bot.dto.NotifyData
import ru.smn.mt.game.bot.mapper.toEntity
import ru.smn.mt.game.bot.repository.GameRepository

@Service
class DBGameStorage(
    private val gameRepository: GameRepository
) : GameStorage {

    override fun isExist(gameId: Long): Boolean {
        return gameRepository.existsById(gameId)
    }

    override fun save(notifyData: NotifyData) {
        gameRepository.save(notifyData.toEntity()).let {
            LOGGER.info("Сохранили уведомление: $it")
        }
    }

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(DBGameStorage::class.java)
    }
}