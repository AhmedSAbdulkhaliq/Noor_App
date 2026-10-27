package com.example

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class RoohAlIslamApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initNotificationChannels()
    }

    private fun initNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)

            val audioChannel = NotificationChannel(
                "quran_audio_playback_channel",
                "تلاوة القرآن الكريم",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "تشغيل التلاوات الصوتية في الخلفية"
            }

            val prayerChannel = NotificationChannel(
                "prayer_azan_channel",
                "تنبيهات مواقيت الصلاة والأذان",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "تنبيهات مواعيد الصلوات الخمس والأذان"
                enableVibration(true)
            }

            val dailyChannel = NotificationChannel(
                "daily_verse_channel",
                "آية وحديث اليوم",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "الإشعارات اليومية للآيات والأحاديث النبوية"
            }

            manager.createNotificationChannel(audioChannel)
            manager.createNotificationChannel(prayerChannel)
            manager.createNotificationChannel(dailyChannel)
        }
    }
}
