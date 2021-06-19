package ir.vasl.smacktester.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ir.vasl.smacktester.R
import kotlinx.android.synthetic.main.fragment_sixth.*
import kotlinx.coroutines.*

class SixthFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sixth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_click_me.setOnClickListener {

        }

        temp()
    }

    val handler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("debug: Exception throw one of child -> $exception")
    }

    fun temp() {

        val parentJob = CoroutineScope(Dispatchers.IO).launch(handler) {

            // -- Job A --
            val job1 = launch {
                val resultA = getResult(number = 1)
                println("debug: We got result A -> $resultA")
            }
            job1.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("debug: Error getting resultA $throwable ")
                }
            }

            // -- Job B --
            val job2 = launch {
                val resultB = getResult(number = 2)
                println("debug: We got result B -> $resultB")
            }
            job2.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("debug: Error getting resultB $throwable ")
                }
            }

            // -- Job C --
            val job3 = launch {
                val resultC = getResult(number = 3)
                println("debug: We got result C -> $resultC")
            }
            job3.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("debug: Error getting resultC $throwable ")
                }
            }
        }
        parentJob.invokeOnCompletion { throwable ->
            if (throwable != null) {
                println("Parent job failed -> $throwable ")
            } else {
                println("debug: Parent job SUCCESS")
            }
        }
    }

    suspend fun getResult(number: Int): Int {
        delay(number * 500L)
        if (number == 2) {
            throw CancellationException("Error getting result for number $number")
        }
        return number * 2
    }

    suspend fun getResultOld(number: Int): Int {
        delay(number * 500L)
        if (number == 2) {
            throw Exception("Error getting result")
        }
        return number * 2
    }
}
