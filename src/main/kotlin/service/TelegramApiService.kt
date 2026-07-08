package ru.vtb.dtc.service

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll
import org.telegram.telegrambots.meta.api.objects.polls.input.InputPollOption
import org.telegram.telegrambots.meta.generics.TelegramClient
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

interface TelegramService {
    fun createGamePollAndPin(
        chatId: String,
        teams: String,
        date: LocalDateTime
    )
}

@Service
class TelegramApiServiceImpl(
    private val telegramClient: TelegramClient
) : TelegramService {

    override fun createGamePollAndPin(
        chatId: String,
        teams: String,
        date: LocalDateTime
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
        date: LocalDateTime
    ): SendPoll? = SendPoll.builder()
        .chatId(chatId)
        .question("Игра. $teams. Дата: ${date.truncatedTo(ChronoUnit.MINUTES)}")
        .options(
            listOf(
                InputPollOption(yesOptions.random()),
                InputPollOption(noOptions.random()),
                InputPollOption(maybeOptions.random())
            )
        )
        .isAnonymous(false)
        .build()


    private companion object {
        private val yesOptions = setOf(
            "(+) 🏀 В стартовой пятерке!",
            "(+) 🔥 Готов оформлять трипл-дабл!",
            "(+) 🗑️ Поставлю сверху!",
            "(+) 👟 Кроссовки зашнурованы",
            "(+) 🎯 Буду поливать трешками!",
            "(+) ⚡  Заряжен на победу",
            "(+) 🦖 Готов доминировать в краске",
            "(+) 🏃‍♂️ Уже бегу на перехват",
            "(+) 🔥 Иду возить этих мешков!",
            "(+) 🗑️ Оформлю им постер через голову!",
            "(+) 👟 Готов уничтожать их защиту",
            "(+) 🎯 Насую им трешек за шиворот",
            "(+) 🏀 Заберу их щит и их гордость"
        )

        private val noOptions = setOf(
            "(-) 🩹 В списке травмированных",
            "(-) 🛋️ Couch-coach сегодня",
            "(-) ❌ Мимо кольца",
            "(-) 🛑 Блок-шот от работы",
            "(-) 🚪 Вне заявки на матч",
            "(-) 🛋️ Слился на диван",
            "(-) 🩹 Немощный, сижу в лазарете",
            "(-) 🤡 Я балласт, поиграйте без меня",
            "(-) 🧊 Трус не играет в баскетбол",
            "(-) 🚲 Буду катать вату дома"
        )

        val maybeOptions = setOf(
            "(+-) 🤷‍♂️ Мну сиськи, не определился",
            "(+-) ⏳ Включил режим терпилы",
            "(+-) 🔄 Ищу отмазки",
            "(+-) 🎲 Жду, пока мамуля разрешит",
            "(+-) ⚖️ Ни рыба ни мясо, хз",
            "(+-) 🐔 Врубил заднюю, но еще думаю",
            "(+-) ⏳ Придумываю сказку"
        )
    }
}