package ir.vasl.smacktester.repository.helper

abstract class LogGeneratorHelper {

    companion object {

        private var logLineCount: Int = 0

        fun generateLogMessage(message: String): String {
            return generateLogLineCount() +
                    " " +
                    message
        }

        private fun generateLogLineCount(): String {
            logLineCount += 1
            return String.format("%04d", logLineCount)
        }

    }


}