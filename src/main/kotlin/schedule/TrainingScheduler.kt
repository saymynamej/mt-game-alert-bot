package ru.vtb.dtc.schedule

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class TrainingScheduler {


    @Scheduled(cron = "0 0 12 * * SAT")
    fun notifyTrainings() {

    }
}