package com.example.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R

class PrayerAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val prayerName = intent?.getStringExtra("PRAYER_NAME") ?: "الصلاة"
        val isDailyVerse = intent?.getBooleanExtra("IS_DAILY_VERSE", false) ?: false

        val channelId = if (isDailyVerse) "daily_verse_channel" else "prayer_azan_channel"
        val channelName = if (isDailyVerse) "آية وحديث اليوم" else "مواقيت الأذان والصلوات"

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "تنبيهات وتذكيرات روح الإسلام"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val launchIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = if (isDailyVerse) "آية اليوم المباركة" else "حان الآن موعد أذان $prayerName"
        val content = if (isDailyVerse) {
            "«ألا بذكر الله تطمئن القلوب» — تدبر آية اليوم وتزود بالخير."
        } else {
            "حي على الصلاة، حي على الفلاح — بارك الله في صلاتكم."
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify((System.currentTimeMillis() % 10000).toInt(), notification)
    }
}
