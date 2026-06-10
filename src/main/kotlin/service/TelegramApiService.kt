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
        teams: String,
        date: LocalDateTime
    ) {
        val poll = SendPoll.builder()
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
        try {
            telegramClient.execute(poll)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


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