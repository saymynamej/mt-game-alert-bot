package ru.vtb.dtc.service.storage

import org.springframework.stereotype.Service
import ru.vtb.dtc.service.GameStorage
import java.io.File

@Service
class FileGameStorage : GameStorage {
    private val storageFile = File(if (File("/data").exists()) "/data/notified_games.txt" else "notified_games.txt")
        .apply {
            if (!exists()) {
                createNewFile()
            }
        }

    override fun isExist(id: String): Boolean {
        return storageFile.useLines { lines ->
            lines.any { it.trim() == id }
        }
    }

    @Synchronized
    override fun save(id: String) {
        try {
            storageFile.appendText("$id\n")
        } catch (e: Exception) {
            System.err.println("Ошибка при записи в файл: ${e.message}")
        }
    }

    override fun isExist(id: Long): Boolean {
        return this.isExist(id.toString())
    }

    override fun save(id: Long) {
        return this.save(id.toString())
    }
}
