package com.example.downloadmanager

import android.app.DownloadManager
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.downloadmanager.databinding.ActivityMainBinding


class MainActivity :BroadcastListener, AppCompatActivity() {
    var str:String?=null
    private var mgr:DownloadManager?=null

    private lateinit var binding: ActivityMainBinding

    private val broadcast by lazy{
        Broadcast()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_main)
       mgr= getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
        registerReceiver(broadcast, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        binding.dwnLoad.setOnClickListener {
            str=binding.etrUrl.text.toString()
//            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
            downloadURLdata(it)
        }
    }

    private fun downloadURLdata(view: View) {
        mgr?.enqueue(DownloadManager.Request(Uri.parse(str))
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or
                        DownloadManager.Request.NETWORK_MOBILE)
                .setTitle("Demo")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                        System.currentTimeMillis().toString()))

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcast);
    }

    override fun onComplete() {
        Toast.makeText(this, "done", Toast.LENGTH_LONG).show();
    }

}
