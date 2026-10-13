package ru.smn.mt.game.bot.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.smn.mt.game.bot.entity.EventEntity

@Repository
interface EventRepository : CrudRepository<EventEntity, Long> {
    fun findAllByForceSend(forceSend: Boolean): List<EventEntity>
}