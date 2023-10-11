package dev.davron.regionaltaxidriver.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log


class SmsBroadcastReceiver() : BroadcastReceiver() {


    interface OnSmsReceived {
        fun onSmsCodeReceived(code: String)
    }

    private val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
    private val TAG = "SMSBroadcastReceiver"


    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action === SMS_RECEIVED) {
            Log.d("##########", "onReceive: sms recieved")
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle["pdus"] as Array<Any>?
                val messages: Array<SmsMessage?> = arrayOfNulls<SmsMessage>(pdus!!.size)
                for (i in pdus.indices) {
                    messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                }
                if (messages.size > -1) {
                    Log.i("##########3", "Message recieved: " + messages[0]!!.messageBody)
                }
            }
        }
    }


}