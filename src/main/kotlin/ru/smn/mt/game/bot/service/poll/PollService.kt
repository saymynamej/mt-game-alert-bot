package ru.smn.mt.game.bot.service

interface PollService {

    fun createPollAndPin(
        chatId: String,
        teams: String,
        date: String
    )
}