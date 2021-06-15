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

class ForthFragment : Fragment() {

    val RESULT1 = "RESULT #1"
    val RESULT2 = "RESULT #2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_click_me.setOnClickListener {
            button_click_me.text = "WE HAVE A CLICK"
            fakeApiRequest3()
        }
    }

    private fun fakeApiRequest3() {

        val parentJob = CoroutineScope(Dispatchers.IO).launch {

            val elapsedTime = measureTimeMillis {

                val result1 = async {
                    println("debug: launching job1 : ${Thread.currentThread().name}")
                    getResult1FromApi()
                }.await()

                val result2 = async {
                    println("debug: launching job2 : ${Thread.currentThread().name}")
                    try {
                        getResult2FromApi("wwwwresult1")
                    } catch (e: CancellationException) {
                        e.message
                    }
                }.await()

                println("debug: we got result #2 -> $result2")
            }
            println("debug: total elapsedTime is -> $elapsedTime ms.")
        }

        parentJob.invokeOnCompletion {
            println("debug: job parent $parentJob completed")
        }
    }

    /*private fun fakeApiRequest2() {

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
    }*/

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
        return RESULT1
    }

    private suspend fun getResult2FromApi(result1: String): String {
        delay(1700)
        if (result1 == RESULT1)
            return RESULT2
        else
            throw CancellationException("result #1 was incorrect...")
    }

}