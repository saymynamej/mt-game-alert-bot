package ru.vtb.dtc.service

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll
import org.telegram.telegrambots.meta.api.objects.polls.input.InputPollOption
import org.telegram.telegrambots.meta.generics.TelegramClient
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class TelegramApiService(
    private val telegramClient: TelegramClient
) {

    fun createGamePoll(
        chatId: String,
        opponent: String,
        date: LocalDateTime
    ) {
        val poll = SendPoll.builder()
            .chatId(chatId)
            .question("Игра против команды: $opponent. Дата: ${date.truncatedTo(ChronoUnit.MINUTES)}")
            .options(
                listOf(
                    InputPollOption("+"),
                    InputPollOption("-"),
                    InputPollOption("+-")
                )
            )
            .isAnonymous(false)
            .build()
        try {
            telegramClient.execute(poll)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}