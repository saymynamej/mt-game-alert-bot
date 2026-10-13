package ru.smn.mt.game.bot.entity


import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable

interface Persistable<T: Any> : Persistable<T> {
    @get:Transient
    val newEntity: Boolean
}
