package ir.vasl.smacktester.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ir.vasl.smacktester.R
import ir.vasl.smacktester.repository.`interface`.TimerInterface
import ir.vasl.smacktester.repository.helper.LogGeneratorHelper
import ir.vasl.smacktester.repository.helper.PrinterHelper
import ir.vasl.smacktester.repository.helper.TimerHelper
import ir.vasl.smacktester.repository.helper.smackBridges.SmackCoreBridge
import kotlinx.android.synthetic.main.activity_main.*
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.MessageBuilder
import org.jxmpp.jid.EntityBareJid

class MainActivity : AppCompatActivity(), View.OnClickListener, TimerInterface, ConnectionListener,
    IncomingChatMessageListener, OutgoingChatMessageListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        findViewById<View>(R.id.appCompatButton_connect).setOnClickListener(this)
        findViewById<View>(R.id.appCompatButton_send_message).setOnClickListener(this)
        findViewById<View>(R.id.appCompatButton_disconnect).setOnClickListener(this)

        TimerHelper.apply {
            timerInterface = this@MainActivity
            runTimer()
        }

        SmackCoreBridge.getInstance(this).setConnectionListener(this)
        SmackCoreBridge.getInstance(this).setIncomingChatMessageListener(this)
        SmackCoreBridge.getInstance(this).setOutgoingChatMessageListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.appCompatButton_connect -> {

                PrinterHelper.printLogIntoTextView(
                    findViewById<View>(R.id.textView_log) as TextView,
                    LogGeneratorHelper.generateLogMessage("Connect to XMPP Server")
                )

                SmackCoreBridge.getInstance(this).connect(
                    editText_username.text.toString(),
                    editText_password.text.toString()
                )

            }
            R.id.appCompatButton_send_message -> {

                PrinterHelper.printLogIntoTextView(
                    findViewById<View>(R.id.textView_log) as TextView,
                    LogGeneratorHelper.generateLogMessage("Send message to xmpp server")
                )

                SmackCoreBridge.getInstance(this).sendMessage(
                    editText_message.text.toString(),
                    editText_target_username.text.toString()
                )

            }
            R.id.appCompatButton_disconnect -> {

                PrinterHelper.printLogIntoTextView(
                    findViewById<View>(R.id.textView_log) as TextView,
                    LogGeneratorHelper.generateLogMessage("Disconnect from XMPP Server")
                )

                SmackCoreBridge.getInstance(this).disconnect()
            }
        }
    }

    override fun onTimerTick() {
        PrinterHelper.printLogIntoTextView(
            findViewById<View>(R.id.textView_log) as TextView,
            LogGeneratorHelper.generateLogMessage("IDLE")
        )
    }

    override fun connecting(connection: XMPPConnection?) {
        super.connecting(connection)
        PrinterHelper.printLogIntoTextView(
            findViewById<View>(R.id.textView_log) as TextView,
            LogGeneratorHelper.generateLogMessage("Connecting")
        )
    }

    override fun connected(connection: XMPPConnection?) {
        super.connected(connection)
        PrinterHelper.printLogIntoTextView(
            findViewById<View>(R.id.textView_log) as TextView,
            LogGeneratorHelper.generateLogMessage("Connected")
        )
    }

    override fun authenticated(connection: XMPPConnection?, resumed: Boolean) {
        super.authenticated(connection, resumed)
        PrinterHelper.printLogIntoTextView(
            findViewById<View>(R.id.textView_log) as TextView,
            LogGeneratorHelper.generateLogMessage("Authenticated")
        )
    }

    override fun connectionClosed() {
        super.connectionClosed()
        PrinterHelper.printLogIntoTextView(
            findViewById<View>(R.id.textView_log) as TextView,
            LogGeneratorHelper.generateLogMessage("ConnectionClosed")
        )
    }

    override fun connectionClosedOnError(e: java.lang.Exception?) {
        super.connectionClosedOnError(e)
        PrinterHelper.printLogIntoTextView(
            findViewById<View>(R.id.textView_log) as TextView,
            LogGeneratorHelper.generateLogMessage("ConnectionClosedOnError -> ${e.toString()}")
        )
    }

    override fun newIncomingMessage(from: EntityBareJid?, message: Message?, chat: Chat?) {

        val newIncomingMessage = "\n\n" +
                "    " + from?.domain.toString() +
                "\n" +
                "    " + message?.body.toString() +
                "\n" +
                "    " + chat?.xmppAddressOfChatPartner.toString() +
                "\n\n"

        PrinterHelper.printLogIntoTextView(
            findViewById<View>(R.id.textView_log) as TextView,
            LogGeneratorHelper.generateLogMessage("NewIncomingMessage -> $newIncomingMessage")
        )
    }

    override fun newOutgoingMessage(
        to: EntityBareJid?,
        messageBuilder: MessageBuilder?,
        chat: Chat?
    ) {

        val outGoingMessage = "\n\n" +
                "    " + to?.domain.toString() +
                "\n" +
                "    " + messageBuilder?.body.toString() +
                "\n" +
                "    " + chat?.xmppAddressOfChatPartner.toString() +
                "\n\n"

        PrinterHelper.printLogIntoTextView(
            findViewById<View>(R.id.textView_log) as TextView,
            LogGeneratorHelper.generateLogMessage("NewOutgoingMessage -> $outGoingMessage")
        )
    }
}