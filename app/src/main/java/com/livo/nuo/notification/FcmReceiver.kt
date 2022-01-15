package com.livo.nuo.notification

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmReceiver : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage.getNotification() != null) {

            var data=remoteMessage.data
            var message=remoteMessage.notification?.body
            var title= remoteMessage.notification?.title
            remoteMessage.notification?.let {
                Log.e("TAG", "Message Notification Body: ${it.body}")
            }

            var datancode = remoteMessage.data.get("nCode")
            var datarequest_id = remoteMessage.data.get("request_id")

            Log.e("ki",datancode.toString())

            if (datancode.equals("1")){
                if (datarequest_id != null) {

                    val intent = Intent("listing_ongoing")
                    intent.putExtra("id", datarequest_id)
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                }
            }

            if (datancode.equals("3")){
                if (datarequest_id != null) {

                    val intent = Intent("profile_fragment")
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                }
            }

            if (datancode.equals("6")){
                if (datarequest_id != null) {

                    val intent = Intent("dashboard_fragment")
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                }
            }
        }
    }
}