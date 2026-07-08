package ru.vtb.dtc.service

import org.springframework.stereotype.Service
import java.io.File

@Service
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
    override fun save(gameId: Long) {
        try {
            storageFile.appendText("$gameId\n")
        } catch (e: Exception) {
            System.err.println("Ошибка при записи в файл: ${e.message}")
        }
    }
}
