package ir.vasl.smacktester.repository.helper.smackBridges

import android.util.Log
import ir.vasl.smacktester.repository.PublicValues
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.MessageBuilder
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.impl.JidCreate

object SmackChatBridge : IncomingChatMessageListener, OutgoingChatMessageListener {

    private const val TAG = "SmackChatBridge"
    private var chatManager: ChatManager? = null

    private var smackChatBridgeIncomingChatMessageListener: IncomingChatMessageListener? = null
    private var smackChatBridgeOutgoingChatMessageListener: OutgoingChatMessageListener? = null

    override fun newIncomingMessage(
        from: EntityBareJid?,
        message: Message?,
        chat: Chat?
    ) {
        Log.i(TAG, "newIncomingMessage: ${chat.toString()}")
        smackChatBridgeIncomingChatMessageListener?.let {
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
        Log.i(TAG, "newOutgoingMessage: ${chat.toString()}")
        smackChatBridgeOutgoingChatMessageListener?.let {
            it.newOutgoingMessage(to, messageBuilder, chat)
        }
    }

    fun generateChatMessage(message: String): String {
        return message
    }

    fun generateChatMessage(message: String, stanzaId: String): Message {
        val newMessage = Message()
        newMessage.stanzaId = stanzaId
        newMessage.type = Message.Type.chat
        newMessage.body = message
        return newMessage
    }

    fun generateChatManager(connection: AbstractXMPPConnection): ChatManager? {
        if (chatManager == null) {
            chatManager = ChatManager.getInstanceFor(connection)
            chatManager?.addIncomingListener(this@SmackChatBridge)
            chatManager?.addOutgoingListener(this@SmackChatBridge)
        }

        return chatManager
    }

    fun getChatManager(): ChatManager? {
        return chatManager
    }

    fun sendChatMessage(chatManager: ChatManager?, message: String, target: String) {
        chatManager?.let {
            val jid = JidCreate.entityBareFrom(target)
            val chat: Chat = it.chatWith(jid)
            chat.send(message)
        }
    }

    fun sendChatMessage(chatManager: ChatManager?, message: Message, target: String) {
        chatManager?.let {
            // val jid = JidCreate.entityBareFrom(target)
            val jid = JidCreate.from(target + "@" + PublicValues.serverNameTest)
            val chat: Chat = it.chatWith(jid.asEntityBareJidIfPossible())
            chat.send(message)
        }
    }

    fun setIncomingChatMessageListener(incomingChatMessageListener: IncomingChatMessageListener) {
        this.smackChatBridgeIncomingChatMessageListener = incomingChatMessageListener
    }

    fun setOutgoingChatMessageListener(outgoingChatMessageListener: OutgoingChatMessageListener) {
        this.smackChatBridgeOutgoingChatMessageListener = outgoingChatMessageListener
    }

}