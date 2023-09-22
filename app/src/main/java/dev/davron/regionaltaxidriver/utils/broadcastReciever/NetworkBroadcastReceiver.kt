package dev.davron.regionaltaxidriver.utils.broadcastReciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import dev.davron.regionaltaxidriver.utils.NetworkState

class NetworkBroadcastReceiver(private val networkState: NetworkState) : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p0?.let { isOnline(it) }!!) {
            networkState.isOnline(true)
        } else {
            networkState.isOnline(false)
        }
    }

    private fun isOnline(context: Context): Boolean {
        val connectionManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activityNetworkInfo = connectionManager.activeNetworkInfo

        return activityNetworkInfo != null && activityNetworkInfo.isConnected
    }
}