package ru.smn.mt.game.bot.ext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

fun buildTrainingDateString(day: DayOfWeek, startTime: LocalTime, endTime: LocalTime): String {
    val trainingDate = LocalDate.now().with(TemporalAdjusters.nextOrSame(day))

    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale("ru"))
    val formattedDate = trainingDate.format(dateFormatter)
        .replaceFirstChar { it.uppercase() }

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val start = startTime.format(timeFormatter)
    val end = endTime.format(timeFormatter)

    return "Дата: $formattedDate ($start - $end)"
}