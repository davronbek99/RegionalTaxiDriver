package dev.davron.regionaltaxi.utils.broadcastReciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log

class SmsBroadcastReciever : BroadcastReceiver() {

    private val SMS_RECIEVED_ACTION = "android.provider.Telephony.SMS_RECEIVED"

    override fun onReceive(p0: Context?, p1: Intent?) {

        if (p1 != null) {
            if (p1.action.toString() == SMS_RECIEVED_ACTION) {

                val bundle = p1.extras!!
                val pdus = bundle.get("pdus") as Array<*>
                val messages: Array<SmsMessage?> = arrayOfNulls(pdus.size)
                for (i in pdus.indices) {
                    messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                }
                if (messages.size > -1) {
                    Log.i(
                        "Message broadcast reciever",
                        "Message recieved: " + messages[0]?.messageBody
                    )
                }
            }
        }
    }
}