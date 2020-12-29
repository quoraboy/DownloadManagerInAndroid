package com.example.downloadmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class Broadcast: BroadcastReceiver() {
    var broadcastListener:BroadcastListener?=null
    override fun onReceive(context: Context?, intent: Intent?) {
        broadcastListener?.onComplete()
    }
    fun setListener(broadcastListener: BroadcastListener)
    {
        this.broadcastListener=broadcastListener
    }
}