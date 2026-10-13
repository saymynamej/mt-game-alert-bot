package ru.smn.mt.game.bot.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("notify")
data class NotifyEntity(
    @Id
    @Column("id")
    val notifyId: Long? = null,
    @Column(value = "away_team")
    val awayTeam: String,
    @Column(value = "home_team")
    val homeTeam: String,
    @Column(value = "notify_date")
    val notifyDate: LocalDateTime,
) : Persistable<Long> {
    override var newEntity: Boolean = true

    override fun getId(): Long? = notifyId

    override fun isNew() = newEntity
}
