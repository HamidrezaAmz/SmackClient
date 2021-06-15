package ir.vasl.smacktester.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ir.vasl.smacktester.R
import kotlinx.android.synthetic.main.fragment_third.*
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class ThirdFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_click_me.setOnClickListener {
            setNewText("WE HAVE A CLICK")

            CoroutineScope(Dispatchers.IO).launch {
                fakeApiRequest2()
            }
        }
    }

    private fun fakeApiRequest2() {

        CoroutineScope(Dispatchers.IO).launch {
            val executionTime = measureTimeMillis {

                val result1: Deferred<String> = async {
                    println("debug: launching job1 : ${Thread.currentThread().name}")
                    getResult1FromApi()
                }

                val result2: Deferred<String> = async {
                    println("debug: launching job2 : ${Thread.currentThread().name}")
                    getResult2FromApi()
                }

                setTextOnMainThread("Got ${result1.await()}")
                setTextOnMainThread("Got ${result2.await()}")
            }
            println("debug: total elapsed time is $executionTime ms.")
        }

    }

    private suspend fun fakeApiRequest1() {
        val startTime = System.currentTimeMillis()
        val parentJob = CoroutineScope(Dispatchers.IO).launch {
            val job1 = launch {
                val time1 = measureTimeMillis {
                    println("debug: launching job1 in thread ${Thread.currentThread().name}")
                    val result1 = getResult1FromApi()
                    setTextOnMainThread("Got $result1")
                }
                println("debug: completed job1 in $time1 ms.")
            }

            val job2 = launch {
                val time2 = measureTimeMillis {
                    println("debug: launching job2 in thread ${Thread.currentThread().name}")
                    val result2 = getResult2FromApi()
                    setTextOnMainThread("Got $result2")
                }
                println("debug: completed job2 in $time2 ms.")
            }
        }
        parentJob.invokeOnCompletion {
            val elapsedTime = System.currentTimeMillis() - startTime
            println("estimated elaped time is $elapsedTime ms.")
        }
    }

    private fun setNewText(text: String) {
        val newText = textView_result.text.toString() + "\n$text"
        textView_result.text = newText
    }

    private suspend fun setTextOnMainThread(text: String) {
        withContext(Dispatchers.Main) {
            setNewText(text)
        }
    }

    private suspend fun getResult1FromApi(): String {
        delay(1000)
        return "RESULT #1"
    }

    private suspend fun getResult2FromApi(): String {
        delay(1700)
        return "RESULT #2"
    }

}