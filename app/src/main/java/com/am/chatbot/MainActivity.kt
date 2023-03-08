package com.am.chatbot

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity: AppCompatActivity() {
    private lateinit var adapter: ChatAdapter
    private lateinit var rvData: RecyclerView
    private lateinit var tvGenerate: TextView
    private lateinit var tvStop: TextView
    private val studentId: String = "301296257"
    private val dataList: ArrayList<ChatModel> = ArrayList()
    private var isServiceStop: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        setAdapter()
    }

    private fun initUI() {
        rvData = findViewById(R.id.rv_data)
        tvGenerate = findViewById(R.id.tv_generate)
        tvStop = findViewById(R.id.tv_stop)

        tvGenerate.setOnClickListener {
            if(isServiceStop){
                startService(Intent(this, ChatBotService::class.java))
                isServiceStop = false
            }
            val intent = Intent()
            intent.action = "com.am.chatbot.chatBotnotification"
            intent.putExtra("callFor", "generate")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }

        tvStop.setOnClickListener {
            isServiceStop = true
            val intent = Intent()
            intent.action = "com.am.chatbot.chatBotnotification"
            intent.putExtra("callFor", "stop")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    private fun setAdapter() {
        adapter = ChatAdapter()
        rvData.layoutManager = LinearLayoutManager(this)
        rvData.adapter = adapter
    }

    override fun onResume() {
        val filter = IntentFilter()
        filter.addAction("com.am.chatbot.notification")
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(notificationReceiver, filter)

        startService(Intent(this, ChatBotService::class.java))
        super.onResume()
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver)
        super.onPause()
    }

    private val notificationReceiver: BroadcastReceiver = object: BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context,
            intent: Intent) {
            runOnUiThread {
                setData(intent.getStringExtra("callFor")!!)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setData(message: String){
        dataList.add(ChatModel(message, false, System.currentTimeMillis()))
        adapter.submitList(dataList)
        adapter.notifyDataSetChanged()
    }
}