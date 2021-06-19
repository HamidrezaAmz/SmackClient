package ir.vasl.smacktester.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import ir.vasl.smacktester.R
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smackx.mam.MamManager
import org.jivesoftware.smackx.muc.MultiUserChat
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.parts.Resourcepart

class Tester2Activity : AppCompatActivity(), View.OnClickListener {

    private val LOG_TAG = "Tester2Activity-Debug"

    private var smackConnection: AbstractXMPPConnection? = null
    private lateinit var mucJid: EntityBareJid
    private var multiUserChatManager: MultiUserChatManager? = null
    private lateinit var mamManager: MamManager
    private var multiUserChat: MultiUserChat? = null
    private lateinit var nickName: Resourcepart
    var messageList = MutableLiveData<List<Message>>()
    private val tempMessageList = ArrayList<Message>()
    private var disposableMessages: Disposable? = null
    private lateinit var uid: String
    private var doMoreLoading: Boolean = false
    private lateinit var username: String
    private lateinit var groupName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tester2)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }
/*

    fun attemptLogin(userName: String, password: String): Single<AbstractXMPPConnection> {

        return Single.create<AbstractXMPPConnection> { source ->

            configBuilder = getBaseConfig(userName, password)<strong>
            val hostAddress: InetAddress = InetAddress.getByName(hostName)
            val configuration = configBuilder.setHostAddress(hostAddress).build()
            val connection = XMPPTCPConnection(configuration)

            connection.connect()
            if (connection.isConnected) {
                Log.v(LOG_TAG, "-> attemptLogin -> connected")
            } else {
                source.onError(Throwable("Unable to connect"))
            }

            connection.login()
            if (connection.isAuthenticated) {
                Log.v(LOG_TAG, "-> attemptLogin -> ${configuration.username} authenticated")
            } else {
                source.onError(Throwable("Unable to login"))
            }

            //send the available status that is "online" over the server
            connection.sendStanza(Presence(Presence.Type.available))
            this.smackConnection = connection

            val roster = Roster.getInstanceFor(connection)
            //accept_all means anyone can initiate chat and message
            roster.subscriptionMode = Roster.SubscriptionMode.accept_all

            source.onSuccess(connection)
        }
    }

    private fun initGroupChat() {
        initGroupChatRoom(userName)
        initMam()
    }

    private fun initGroupChatRoom(userName: String) {
        // xmppServiceGroupDomain is specific for group chat for eg ("muc_localhost")
        val xmppServiceGroupDomain: DomainBareJid = JidCreate.domainBareFrom(domainNameForGroupChat)
        //create muc jid which is room JID
        //groupName is the name of the group which we want to join
        mucJid = JidCreate.entityBareFrom(Localpart.from(groupName), xmppServiceGroupDomain)

        //userName can be your name by which you want to join the room
        //nickName is the name which will be shown on group chat
        nickName = Resourcepart.from(userName)

        // Get the MultiUserChatManager instance
        multiUserChatManager = MultiUserChatManager.getInstanceFor(smackConnection)
        // Get the multiuserchat instance
        multiUserChat = multiUserChatManager?.getMultiUserChat(mucJid)

        val mucEnterConfiguration = multiUserChat?.getEnterConfigurationBuilder(nickName)!!
            .requestNoHistory()
            .build()

        if (!multiUserChat!!.isJoined) {
            multiUserChat?.join(mucEnterConfiguration)
        }

        // For listening incoming message
        multiUserChat?.addMessageListener(incomingMessageListener)
        // incomingMessageListener is implemented below.
    }

    private fun initMam() {
        // check the connection object
        if (smackConnection != null) {
            //get the instance of MamManager
            mamManager = MamManager.getInstanceFor(multiUserChat)
            //enable it for fetching messages
            mamManager.enableMamForAllMessages()
            // Function for fetching messages
            disposableMessages = getObservableMessages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ listOfMessages ->
                    tempMessageList = listOfMessages
                }, { t ->
                    Log.e(LOG_TAG, "-> initMam -> onError ->", t)
                }, {
                    messageList.value = tempMessageList
                    tempList.clear()
                })
        }
    }

    // number_of_messages_to_fetch it's a limit of messages to be fetched eg. 20.
    private fun getObservableMessages(): Observable<List<Message>> {
        return Observable.create<List<Message>> { source ->
            try {
                val mamQuery = mamManager.queryMostRecentPage(mucJid, number_of_messages_to_fetch)
                if (mamQuery.messageCount == 0 || mamQuery.messageCount < number_of_messages_to_fetch) {
                    uid = ""
                    doMoreLoading = false
                } else {
                    uid = mamQuery.mamResultExtensions[0].id
                    doMoreLoading = true
                }
                source.onNext(mamQuery.messages)

            } catch (e: Exception) {
                if (smackConnection?.isConnected == false) {
                    source.onError(e)
                } else {
                    Log.e("ChatDetail", "Connection closed")
                }
            }
            source.onComplete()
        }
    }

    private val incomingMessageListener = object : MessageListener {
        override fun processMessage(message: Message?) {
            if (!TextUtils.isEmpty(message?.body)) {
                addIncomingMessageInRecycler(message!!)
            }
        }
    }
*/

}