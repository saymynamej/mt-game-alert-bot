package ru.smn.mt.game.bot.service.storage

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.stereotype.Service
import ru.smn.mt.game.bot.dto.NotifyData
import java.io.File

@Service
@ConditionalOnMissingBean(value = [DBGameStorage::class])
class FileGameStorage : GameStorage {
    private val storageFile = File(if (File("/data").exists()) "/data/notified_games.txt" else "notified_games.txt")
        .apply {
            if (!exists()) {
                createNewFile()
            }
        }

    override fun isExist(gameId: Long): Boolean {
        return storageFile.useLines { lines -> lines.any { it.trim() == gameId.toString() } }
    }

    @Synchronized
    override fun save(notifyData: NotifyData) {
        try {
            storageFile.appendText("${notifyData.id}\n")
        } catch (e: Exception) {
            System.err.println("Ошибка при записи в файл: ${e.message}")
        }
    }
}
