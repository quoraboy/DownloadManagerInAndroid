package com.example.downloadmanager

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.downloadmanager.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    var str: String? = null
    private var mgr: DownloadManager? = null
    private var enqueue: Long? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mgr = getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
        registerReceiver(broadCastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        binding.dwnLoad.setOnClickListener {
            str = binding.etrUrl.text.toString()
//            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
            downloadURLdata(it)
        }
    }

    private fun downloadURLdata(view: View) {
        enqueue = mgr?.enqueue(
            DownloadManager.Request(Uri.parse(str))
                .setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI or
                            DownloadManager.Request.NETWORK_MOBILE
                )
                .setTitle("Demo")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    System.currentTimeMillis().toString()
                )
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadCastReceiver);
    }


    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            var action: String? = intent?.action
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                var downloadID: Long? = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
                var query: DownloadManager.Query = DownloadManager.Query()
                enqueue?.let { query.setFilterById(it) }
                val c: Cursor = mgr!!.query(query)
                if (c.moveToFirst()) {
                    val columnIndex = c
                        .getColumnIndex(DownloadManager.COLUMN_STATUS)
                    if (DownloadManager.STATUS_SUCCESSFUL == c
                            .getInt(columnIndex)
                    ) {
                        val uriString = c
                            .getString(
                                c
                                    .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                            )
                        binding.imageView1.setImageURI(Uri.parse(uriString))
                        Toast.makeText(applicationContext, "Download completes", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }
}
