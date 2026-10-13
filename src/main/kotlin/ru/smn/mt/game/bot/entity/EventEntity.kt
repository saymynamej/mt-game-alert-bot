package ru.smn.mt.game.bot.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("events")
data class EventEntity(
    @Id
    @Column("id")
    val notifyId: Long? = null,
    @Column(value = "poll_message")
    val pollMessage: String,
    @Column("chat_id")
    val chatId: Long,
    @Column(value = "notify_date")
    val notifyDate: LocalDateTime,
    @Column(value = "force_send")
    var forceSend: Boolean,
) : Persistable<Long> {
    override var newEntity: Boolean = true

    override fun getId(): Long? = notifyId

    override fun isNew() = newEntity
}
