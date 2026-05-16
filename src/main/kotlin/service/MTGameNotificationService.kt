package ru.vtb.dtc.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class MTGameNotificationService(
    @param:Value($$"${bot.token}")
    private val botToken: String,
    private val telegramApiService: TelegramApiService
) : SpringLongPollingBot, LongPollingUpdateConsumer {
    override fun getBotToken() = botToken

    override fun getUpdatesConsumer(): LongPollingUpdateConsumer = this

    override fun consume(updates: List<Update>) {
        println("${updates[0].message.chatId}")
    }
}