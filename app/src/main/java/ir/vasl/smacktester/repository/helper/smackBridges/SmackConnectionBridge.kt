package ir.vasl.smacktester.repository.helper.smackBridges

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration

object SmackConnectionBridge : ConnectionListener {

    private const val TAG = "SmackConnectionBridge"

    private lateinit var connection: AbstractXMPPConnection

    private var currConnectionListener: ConnectionListener? = null

    override fun connecting(connection: XMPPConnection?) {
        super.connecting(connection)
        Log.i(TAG, "connecting: ")
        currConnectionListener?.let { it.connecting(connection) }
    }

    override fun connected(connection: XMPPConnection?) {
        super.connected(connection)
        Log.i(TAG, "connected: ")
        currConnectionListener?.let { it.connected(connection) }
    }

    override fun authenticated(connection: XMPPConnection?, resumed: Boolean) {
        super.authenticated(connection, resumed)
        Log.i(TAG, "authenticated: ")
        currConnectionListener?.let { it.authenticated(connection, resumed) }
    }

    override fun connectionClosed() {
        super.connectionClosed()
        Log.i(TAG, "connectionClosed: ")
        currConnectionListener?.let { it.connectionClosed() }
    }

    override fun connectionClosedOnError(e: Exception?) {
        super.connectionClosedOnError(e)
        Log.i(TAG, "connectionClosedOnError: ")
        currConnectionListener?.let { it.connectionClosedOnError(e) }
    }

    fun generateConnectionConfig(
        username: String,
        password: String,
        domain: String,
        host: String,
        port: Int
    ): XMPPTCPConnectionConfiguration {
        return XMPPTCPConnectionConfiguration.builder()
            .setUsernameAndPassword(username, password)
            .setXmppDomain(domain)
            .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
            .setHost(host)
            .setPort(port)
            .enableDefaultDebugger()
            .setCompressionEnabled(true)
            .build()
    }

    fun generateConnectionInstance(config: XMPPTCPConnectionConfiguration): AbstractXMPPConnection {
        config.let { xmppConfig ->
            connection = XMPPTCPConnection(xmppConfig)
            connection.addConnectionListener(this@SmackConnectionBridge)
            return connection
        }
    }

    fun getConnectionInstance(): AbstractXMPPConnection {
        return connection
    }

    fun connect() {
        CoroutineScope(Dispatchers.IO).launch {
            connection.connect()
        }
    }

    fun login() {
        CoroutineScope(Dispatchers.IO).launch {
            connection.login()
        }
    }

    fun connectAndLogin() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                connection.connect().login()
            }
        }
    }

    fun disConnect() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                connection.disconnect()
            }
        }
    }

    fun setConnectionListener(connectionListener: ConnectionListener) {
        this.currConnectionListener = connectionListener
    }

}