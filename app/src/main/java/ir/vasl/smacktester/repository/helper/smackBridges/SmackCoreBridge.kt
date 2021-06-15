package ir.vasl.smacktester.repository.helper.smackBridges

import android.content.Context
import ir.vasl.smacktester.repository.PublicValues
import ir.vasl.smacktester.repository.helper.SingletonHelper
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.android.AndroidSmackInitializer
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.MessageBuilder
import org.jxmpp.jid.EntityBareJid

/**
 *
 * This class is a core class that handles all developer interactions with smack client
 * 1 - Connection handling
 * 2 - Messaging handling
 * 3 - Callback interfaces handling
 *
 */

class SmackCoreBridge private constructor(context: Context) : ConnectionListener,
    IncomingChatMessageListener,
    OutgoingChatMessageListener {

    private val smackConnectionBridge: SmackConnectionBridge by lazy {
        SmackConnectionBridge
    }
    private val smackChatBridge: SmackChatBridge by lazy {
        SmackChatBridge
    }

    private var smackCoreBridgeConnectionListener: ConnectionListener? = null
    private var smackCoreBridgeIncomingChatMessageListener: IncomingChatMessageListener? = null
    private var smackCoreBridgeOutgoingChatMessageListener: OutgoingChatMessageListener? = null

    companion object : SingletonHelper<SmackCoreBridge, Context>(::SmackCoreBridge)

    init {
        AndroidSmackInitializer.initialize(context)

        smackChatBridge.setIncomingChatMessageListener(this@SmackCoreBridge)
        smackChatBridge.setOutgoingChatMessageListener(this@SmackCoreBridge)
    }

    override fun connecting(connection: XMPPConnection?) {
        super.connecting(connection)
        smackCoreBridgeConnectionListener?.let { it.connecting(connection) }
    }

    override fun connected(connection: XMPPConnection?) {
        super.connected(connection)
        smackCoreBridgeConnectionListener?.let { it.connected(connection) }
    }

    override fun authenticated(connection: XMPPConnection?, resumed: Boolean) {
        super.authenticated(connection, resumed)
        smackCoreBridgeConnectionListener?.let { it.authenticated(connection, resumed) }
    }

    override fun connectionClosed() {
        super.connectionClosed()
        smackCoreBridgeConnectionListener?.let { it.connectionClosed() }
    }

    override fun connectionClosedOnError(e: Exception?) {
        super.connectionClosedOnError(e)
        smackCoreBridgeConnectionListener?.let { it.connectionClosedOnError(e) }
    }

    override fun newIncomingMessage(from: EntityBareJid?, message: Message?, chat: Chat?) {
        smackCoreBridgeIncomingChatMessageListener?.let {
            it.newIncomingMessage(
                from,
                message,
                chat
            )
        }
    }

    override fun newOutgoingMessage(
        to: EntityBareJid?,
        messageBuilder: MessageBuilder?,
        chat: Chat?
    ) {
        smackCoreBridgeOutgoingChatMessageListener?.let {
            it.newOutgoingMessage(
                to,
                messageBuilder,
                chat
            )
        }
    }

    fun connect(
        username: String = PublicValues.usernameTest,
        password: String = PublicValues.passwordTest
    ) {

        val config = smackConnectionBridge.generateConnectionConfig(
            username,
            password,
            PublicValues.serverNameTest,
            PublicValues.serverNameTest,
            PublicValues.portTest
        )

        smackConnectionBridge.generateConnectionInstance(config)
        smackConnectionBridge.setConnectionListener(this@SmackCoreBridge)
        smackConnectionBridge.connectAndLogin()
    }

    fun sendMessage(message: String, stanzaId: String) {
        val generatedMessage = smackChatBridge.generateChatMessage(message, stanzaId)
        val chatManager = smackChatBridge.generateChatManager(
            smackConnectionBridge.getConnectionInstance()
        )
        smackChatBridge.sendChatMessage(
            chatManager,
            generatedMessage,
            stanzaId
        )

    }

    fun disconnect() {
        smackConnectionBridge.disConnect()
    }

    fun getStatus() {}

    fun setConnectionListener(connectionListener: ConnectionListener) {
        this.smackCoreBridgeConnectionListener = connectionListener
    }

    fun setIncomingChatMessageListener(incomingChatMessageListener: IncomingChatMessageListener) {
        this.smackCoreBridgeIncomingChatMessageListener = incomingChatMessageListener
    }

    fun setOutgoingChatMessageListener(outgoingChatMessageListener: OutgoingChatMessageListener) {
        this.smackCoreBridgeOutgoingChatMessageListener = outgoingChatMessageListener
    }

}