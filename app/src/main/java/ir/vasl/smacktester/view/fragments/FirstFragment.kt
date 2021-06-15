package ir.vasl.smacktester.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ir.vasl.smacktester.R
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.coroutines.*

class FirstFragment : Fragment(), View.OnClickListener {

    val TAG = "_FirstFragment"
    val RESULT_1 = "RESULT #1"
    val RESULT_2 = "RESULT #2"

    val JOB_TIMEOUT = 12900L

    val PROGRESS_MAX = 100
    val PROGRESS_START = 0
    val JOB_TIME = 4000 // ms
    lateinit var job: CompletableJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_navigate.setOnClickListener(this)
        button_fake_api.setOnClickListener(this)
        button_start_job_new.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_navigate -> {
                findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
            }
            R.id.button_fake_api -> {
                CoroutineScope(Dispatchers.IO).launch {
                    fakeApiRequest()
                }
            }
            R.id.button_start_job_new -> {
                if (::job.isInitialized.not()) {
                    initJob()
                }
                progressBar.startJobOrCancel(job)
            }
        }
    }

    private suspend fun fakeApiRequest() {
        withContext(Dispatchers.IO) {

            val job = withTimeoutOrNull(JOB_TIMEOUT) {

                val result1 = getResult1FromApi()
                Log.i(TAG, "debug : $result1")
                setTextToTextviewOnMainThread(result1)

                val result2 = getResult2FromApi()
                Log.i(TAG, "debug : $result2")
                setTextToTextviewOnMainThread(result2)

            }

            if (job == null) {
                val cancelingMessage = "Canceling the job | Job got more than $JOB_TIMEOUT ms"
                setTextToTextviewOnMainThread(cancelingMessage)
            }
        }

    }

    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(): String {
        logThread("getResult2FromApi")
        delay(1000)
        return RESULT_2
    }

    private suspend fun setTextToTextviewOnMainThread(input: String) {
        withContext(Dispatchers.Main) {
            val newText = textView_result.text.toString() + "\n$input"
            textView_result.text = newText
        }
    }

    private fun logThread(methodName: String) {
        Log.i(TAG, "debug: $methodName : ${Thread.currentThread().name}")
    }

    private fun initJob() {
        button_start_job_new.setText("START Job #1")
        textView_result.setText("")
        progressBar.progress = 0
        job = Job()
        job.invokeOnCompletion {
            it?.message.let {
                var msg = it
                if (msg.isNullOrBlank()) {
                    msg = "unknown cancellation error."
                }
                println("$job was canceled, reason: $msg")
                showToast(msg)
            }
        }
        progressBar.max = PROGRESS_MAX
        progressBar.progress = PROGRESS_START
    }

    fun ProgressBar.startJobOrCancel(job: Job) {
        if (this.progress > 0) {
            Log.d(TAG, "${job} is already active. Cancelling...")
            resetjob()
        } else {
            button_start_job_new.setText("Cancel Job #1")
            CoroutineScope(Dispatchers.IO + job).launch {
                Log.d(TAG, "coroutine ${this} is activated with job ${job}.")

                for (i in PROGRESS_START..PROGRESS_MAX) {
                    delay((JOB_TIME / PROGRESS_MAX).toLong())
                    this@startJobOrCancel.progress = i
                }
                updateJobCompleteTextView("Job is complete!")
            }
        }
    }

    private fun resetjob() {
        if (job.isActive || job.isCompleted) {
            job.cancel(CancellationException("Resetting job ;)"))
        }
        initJob()
    }

    private fun updateJobCompleteTextView(text: String) {
        GlobalScope.launch(Dispatchers.Main) {
            textView_header.text = text
        }
    }

    fun showToast(text: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}