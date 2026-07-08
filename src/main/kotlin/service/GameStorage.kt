package ru.vtb.dtc.service

interface GameStorage {
    fun isExist(gameId: Long): Boolean
    fun save(gameId: Long)
}