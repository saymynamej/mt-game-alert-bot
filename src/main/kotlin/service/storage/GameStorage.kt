package ru.vtb.dtc.service.storage

interface GameStorage {
    fun isExist(id: String): Boolean
    fun save(id: String)

    fun isExist(id: Long): Boolean
    fun save(id: Long)
}