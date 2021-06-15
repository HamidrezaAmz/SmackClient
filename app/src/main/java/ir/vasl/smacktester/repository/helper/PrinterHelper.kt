package ir.vasl.smacktester.repository.helper

import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrinterHelper {

    companion object {

        fun printLogIntoTextView(textView: TextView, log: String) {

            val oldLog: String = textView.text.toString()
            val newLog: String = log +
                    '\n' +
                    oldLog

            CoroutineScope(Dispatchers.Main).launch {
                textView.text = newLog
            }

        }
    }
}