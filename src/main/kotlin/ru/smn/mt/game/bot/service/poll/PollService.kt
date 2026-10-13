package ru.smn.mt.game.bot.service.poll

interface PollService {

    fun createPollAndPin(
        chatId: String,
        message: String
    )
}