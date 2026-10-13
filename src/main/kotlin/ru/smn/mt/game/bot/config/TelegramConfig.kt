package ru.smn.mt.game.bot.config

import okhttp3.OkHttpClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.meta.TelegramUrl
import org.telegram.telegrambots.meta.api.objects.message.Message
import org.telegram.telegrambots.meta.generics.TelegramClient
import java.lang.reflect.Proxy

@Configuration
class TelegramConfig {

    @Bean
    @ConditionalOnProperty("mock", prefix = "bot", havingValue = "false", matchIfMissing = false)
    fun telegramClient(gameAlertBotProperties: GameAlertBotProperties): TelegramClient {
        val okHttpClient = OkHttpClient.Builder().build()
        return OkHttpTelegramClient(
            okHttpClient,
            gameAlertBotProperties.token,
            TelegramUrl.builder()
                .schema(gameAlertBotProperties.schema)
                .host(gameAlertBotProperties.host)
                .port(gameAlertBotProperties.port.toInt())
                .build()
        )
    }

    @Bean
    @ConditionalOnMissingBean(TelegramClient::class)
    fun mockTelegramClient(): TelegramClient {
        return Proxy.newProxyInstance(
            TelegramClient::class.java.classLoader,
            arrayOf(TelegramClient::class.java)
        ) { _, method, args ->
            val firstArg = args?.firstOrNull()

            println("🤖 [TG MOCK] Вызван метод API: ${method.name} -> $firstArg")

            if (firstArg != null) {
                val className = firstArg.javaClass.simpleName

                if (className.contains("deleteWebhook", ignoreCase = true) ||
                    className.contains("pinChatMessage", ignoreCase = true) ||
                    className.contains("unpinChatMessage", ignoreCase = true) ||
                    className.contains("deleteMessage", ignoreCase = true)
                ) {
                    return@newProxyInstance true
                }
            }

            val returnType = method.returnType
            if (returnType == Boolean::class.javaObjectType || returnType == Boolean::class.java) {
                return@newProxyInstance true
            }
            if (returnType == List::class.java || returnType == MutableList::class.java) {
                return@newProxyInstance mutableListOf<Any>()
            }

            return@newProxyInstance Message().apply { messageId = 999 }
        } as TelegramClient
    }
}