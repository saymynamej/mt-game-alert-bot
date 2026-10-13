package ru.smn.mt.game.bot.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.smn.mt.game.bot.entity.NotifyEntity

@Repository
interface GameRepository : CrudRepository<NotifyEntity, Long>