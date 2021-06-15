package ir.vasl.smacktester.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ir.vasl.smacktester.R
import kotlinx.android.synthetic.main.fragment_fifth.*
import kotlinx.android.synthetic.main.fragment_third.button_click_me
import kotlinx.android.synthetic.main.fragment_third.textView_result
import kotlinx.coroutines.*

class FifthFragment : Fragment() {

    var countNumber = 0

    lateinit var job: Job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fifth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tempMethod2()

        button_click_me.setOnClickListener {
            textView_result.text = (countNumber++).toString()
        }

        button_cancel.setOnClickListener {
            if (::job.isInitialized && job.isActive) {
                println("debug: job isInitialized, going to cancel it -> $job ")
                job.cancel()
            }
        }
    }

    private suspend fun work(i: Int) {
        delay(5000)
        println("debug: work $i Done. ${Thread.currentThread().name}")
    }

    private fun tempMethod2() {
        val startTime = System.currentTimeMillis()
        println("debug: start parent job...")
        job = CoroutineScope(Dispatchers.IO).launch {
            GlobalScope.launch {
                work(1)
            }
            GlobalScope.launch {
                work(2)
            }
        }
        job.invokeOnCompletion { throwable ->
            if (throwable != null) {
                println("debug: job was canceled after ${System.currentTimeMillis() - startTime} ms.")
            } else {
                println("debug: job was completed in ${System.currentTimeMillis() - startTime} ms.")
            }
        }
    }

    private fun tempMethod() {
        CoroutineScope(Dispatchers.Main).launch { // parent job
            println("debug: current thread -> ${Thread.currentThread().name}")
            for (i in 1..100_000) {
                launch { // child job
                    doNetworkRequest(i)
                }
            }
        }
    }

    private suspend fun doNetworkRequest(networkCounter: Int) {
        println("debug: starting network request -> $networkCounter")
        delay(1000)
        println("debug: finished network request!")
    }

}