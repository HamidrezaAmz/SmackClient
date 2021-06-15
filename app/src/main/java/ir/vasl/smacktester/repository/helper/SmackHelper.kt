package ir.vasl.smacktester.repository.helper

import android.util.Log
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.StanzaListener
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.filter.StanzaFilter
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.packet.PresenceBuilder
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.roster.RosterListener
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.Jid
import org.jxmpp.jid.impl.JidCreate

class SmackHelper {

    private val TAG = "SmackHelper"

    private fun getConfig(
        username: String,
        password: String,
        domain: String,
        host: String,
        port: Int
    ): XMPPTCPConnectionConfiguration? {
        // Create a connection to the jabber.org server on a specific port.
        return XMPPTCPConnectionConfiguration.builder()
            .setUsernameAndPassword(username, password)
            .setXmppDomain(domain)
            .setHost(host)
            .setPort(port)
            .build()
    }

    private fun connect(
        username: String,
        password: String,
        serviceName: String
    ) {
        // Create a connection and login to the example.org XMPP service.
        val conn1: AbstractXMPPConnection = XMPPTCPConnection(
            username,
            password,
            serviceName
        )
        conn1.connect().login()
    }

    private fun connect(config: XMPPTCPConnectionConfiguration) {
        val conn2: AbstractXMPPConnection = XMPPTCPConnection(config)
        conn2.connect().login()
    }

    private fun generatePresence(): Presence? {
        // Create a new presence. Pass in false to indicate we're unavailable._
        return PresenceBuilder.buildPresence()
            .setMode(Presence.Mode.available)
            // .setMode(Presence.Mode.away)
            .build()
    }

    private fun sentStanza(con: AbstractXMPPConnection, presence: Presence) {
        // Send the stanza (assume we have an XMPPConnection instance called "con").
        con.sendStanza(presence)
    }

    private fun stanzaListener(con: AbstractXMPPConnection) {

        /*
            con.addStanzaListener(
                { TODO("Not yet implemented") },
                { TODO("Not yet implemented") })
        */

        con.addStanzaListener(
            object : StanzaListener {
                override fun processStanza(packet: Stanza?) {
                    TODO("Not yet implemented")
                }
            }, object : StanzaFilter {
                override fun accept(stanza: Stanza?): Boolean {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun connectAndDisconnect(connection: AbstractXMPPConnection) {
        // Connect to the server
        connection.connect()

        // Log into the server
        connection.login()

        // Disconnect from the server
        connection.disconnect()
    }

    private fun sendMessage(connection: AbstractXMPPConnection) {
        val chatManager = ChatManager.getInstanceFor(connection)
        val entityBareJid: EntityBareJid = JidCreate.entityBareFrom("jsmith@jivesoftware.com")
        val chat: Chat = chatManager.chatWith(entityBareJid)

        // chat.send("hello word!")

        val message: Message = Message(entityBareJid, Message.Type.chat)
        chat.send(message)
    }

    private fun setChatListener(connection: AbstractXMPPConnection) {
        val chatManager = ChatManager.getInstanceFor(connection)
        chatManager.addIncomingListener(
            object : IncomingChatMessageListener {
                override fun newIncomingMessage(
                    from: EntityBareJid?,
                    message: Message?,
                    chat: Chat?
                ) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun getRoster(connection: AbstractXMPPConnection) {
        val roster = Roster.getInstanceFor(connection)
        val rosterEntries = roster.entries
        for (rosterEntry in rosterEntries) {
            Log.i(TAG, "getRoster: ${rosterEntry.toString()}")
        }
    }

    private fun roosterWithListener(connection: AbstractXMPPConnection) {
        val roster = Roster.getInstanceFor(connection)
        roster.addRosterListener(object : RosterListener {
            override fun entriesAdded(addresses: MutableCollection<Jid>?) {
                TODO("Not yet implemented")
            }

            override fun entriesUpdated(addresses: MutableCollection<Jid>?) {
                TODO("Not yet implemented")
            }

            override fun entriesDeleted(addresses: MutableCollection<Jid>?) {
                TODO("Not yet implemented")
            }

            override fun presenceChanged(presence: Presence?) {
                println("Presence changed: " + presence!!.from + " " + presence);
            }
        })
    }

}