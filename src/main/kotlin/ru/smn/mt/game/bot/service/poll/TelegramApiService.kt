package ru.smn.mt.game.bot.service

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll
import org.telegram.telegrambots.meta.api.objects.polls.input.InputPollOption
import org.telegram.telegrambots.meta.generics.TelegramClient
import ru.smn.mt.game.bot.dto.PollOptions.maybeOptions
import ru.smn.mt.game.bot.dto.PollOptions.noOptions
import ru.smn.mt.game.bot.dto.PollOptions.yesOptions

@Service
class TelegramPollService(
    private val telegramClient: TelegramClient
) : PollService {

    override fun createPollAndPin(
        chatId: String,
        teams: String,
        date: String
    ) {
        buildPoll(chatId, teams, date)
            .let { poll -> telegramClient.execute(poll).messageId }
            .let { messageId -> buildPin(chatId, messageId) }
            .let { pinRequest -> telegramClient.execute(pinRequest) }
    }

    private fun buildPin(chatId: String, messageId: Int): PinChatMessage {
        return PinChatMessage.builder()
            .chatId(chatId)
            .messageId(messageId)
            .disableNotification(false)
            .build()
    }

    private fun buildPoll(
        chatId: String,
        teams: String,
        textDate: String
    ): SendPoll? = SendPoll.builder()
        .chatId(chatId)
        .question("Игра. $teams. Дата: $textDate")
        .options(
            listOf(
                InputPollOption(yesOptions.random()),
                InputPollOption(noOptions.random()),
                InputPollOption(maybeOptions.random())
            )
        )
        .isAnonymous(false)
        .build()
}