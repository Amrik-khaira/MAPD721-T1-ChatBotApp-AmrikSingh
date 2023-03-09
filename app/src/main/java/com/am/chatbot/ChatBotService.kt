package com.am.chatbot

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.RingtoneManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.util.*

class ChatBotService: Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?,
        flags: Int,
        startId: Int): Int {
        val filter = IntentFilter()
        filter.addAction("com.am.chatbot.chatBotnotification")
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver, filter)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver)
    }

    @SuppressLint("NotificationPermission")
    private fun onShowNotification(message: String) {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, "com.am.chatbot")
        createNotificationChannel()
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setAutoCancel(true)
        builder.priority = NotificationCompat.PRIORITY_HIGH
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE)
        builder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)

        builder.setContentTitle("Chat Bot")
        builder.setContentText(message)
        builder.setStyle(NotificationCompat.BigTextStyle().bigText(message))

        val notification: Notification = builder.build()
        val notificationManager: NotificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        // notificationManager.cancelAll()
        notificationManager.notify(Random().nextInt(), notification)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel("com.am.chatbot", "ChatBot",
            NotificationManager.IMPORTANCE_HIGH)
        channel.enableVibration(true)
        channel.enableLights(true)
        channel.lightColor = ContextCompat.getColor(this, R.color.purple_200)
        channel.importance = NotificationManager.IMPORTANCE_HIGH

        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setLegacyStreamType(AudioManager.STREAM_RING)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT).build()

        val actualDefaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this,
            RingtoneManager.TYPE_NOTIFICATION)
        if (actualDefaultRingtoneUri != null) {
            channel.setSound(actualDefaultRingtoneUri, audioAttributes)
        }
        Objects.requireNonNull(getSystemService(NotificationManager::class.java))
            .createNotificationChannel(channel)
    }

    private val notificationReceiver: BroadcastReceiver = object: BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context,
            intent: Intent) {
            if (intent.getStringExtra("callFor") == "stop") {
                stopSelf()
                val resultIntent = Intent()
                resultIntent.action = "com.am.chatbot.notification"
                resultIntent.putExtra("callFor", "ChatBot Stopped: 57")
                LocalBroadcastManager.getInstance(context).sendBroadcast(resultIntent)

                onShowNotification("ChatBot Stopped: 57")
            } else {
                val resultIntent = Intent()
                resultIntent.action = "com.am.chatbot.notification"
                resultIntent.putExtra("callFor", "Hello ${intent.getStringExtra("name")}!")
                LocalBroadcastManager.getInstance(context).sendBroadcast(resultIntent)

                onShowNotification("Hello ${intent.getStringExtra("name")}!")

                Thread.sleep(100)
                val resultIntent1 = Intent()
                resultIntent1.action = "com.am.chatbot.notification"
                resultIntent1.putExtra("callFor", "How are you?")
                LocalBroadcastManager.getInstance(context).sendBroadcast(resultIntent1)

                onShowNotification("How are you?")

                Thread.sleep(100)
                val resultIntent2 = Intent()
                resultIntent2.action = "com.am.chatbot.notification"
                resultIntent2.putExtra("callFor", "Good Bye ${intent.getStringExtra("name")}!")
                LocalBroadcastManager.getInstance(context).sendBroadcast(resultIntent2)

                onShowNotification("Good Bye ${intent.getStringExtra("name")}!")
            }
        }
    }
}