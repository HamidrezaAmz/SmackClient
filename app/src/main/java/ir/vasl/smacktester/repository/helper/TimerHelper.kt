package ir.vasl.smacktester.repository.helper

import ir.vasl.smacktester.repository.`interface`.TimerInterface
import java.util.*

class TimerHelper {

    companion object {

        lateinit var timerInterface: TimerInterface

        fun runTimer() {
            Timer().scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    timerInterface.let {
                        timerInterface.onTimerTick()
                    }
                }
            }, 0, 4000)
        }
    }
}