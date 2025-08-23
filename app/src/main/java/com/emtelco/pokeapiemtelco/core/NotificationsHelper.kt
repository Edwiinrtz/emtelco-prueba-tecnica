package com.emtelco.pokeapiemtelco.core;

import android.app.NotificationManager
import android.content.Context;
import androidx.core.app.NotificationCompat
import com.emtelco.pokeapiemtelco.MyHiltApp
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class NotificationsHelper@Inject constructor(@ApplicationContext private val context: Context){

    fun sendNotification(title:String, message:String){

        val localTime = System.currentTimeMillis();
        val notificationManager = context.getSystemService(NotificationManager::class.java)

        val notification = NotificationCompat.Builder(context, MyHiltApp.CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        notificationManager.notify(localTime.hashCode(), notification)
    }

}
