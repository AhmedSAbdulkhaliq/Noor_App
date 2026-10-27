package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuranAudioService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private val binder = LocalBinder()
    private var mediaPlayer: MediaPlayer? = null

    inner class LocalBinder : Binder() {
        fun getService(): QuranAudioService = this@QuranAudioService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    enum class PlaybackState {
        IDLE, BUFFERING, PLAYING, PAUSED, STOPPED, ERROR
    }

    private val _playbackState = MutableStateFlow(PlaybackState.IDLE)
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val _currentSurahName = MutableStateFlow("")
    val currentSurahName: StateFlow<String> = _currentSurahName.asStateFlow()

    private val _currentAyahNumber = MutableStateFlow(1)
    val currentAyahNumber: StateFlow<Int> = _currentAyahNumber.asStateFlow()

    private val _reciterName = MutableStateFlow("")
    val reciterName: StateFlow<String> = _reciterName.asStateFlow()

    private var playlistUrls: List<String> = emptyList()
    private var currentPlaylistIndex: Int = 0

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    fun playAyahSequence(
        urls: List<String>,
        startIndex: Int = 0,
        surahName: String,
        reciter: String,
        startAyahNumber: Int = 1
    ) {
        playlistUrls = urls
        currentPlaylistIndex = startIndex
        _currentSurahName.value = surahName
        _reciterName.value = reciter
        _currentAyahNumber.value = startAyahNumber + startIndex
        playCurrentIndex()
    }

    private fun playCurrentIndex() {
        if (currentPlaylistIndex !in playlistUrls.indices) {
            stopPlayback()
            return
        }

        try {
            _playbackState.value = PlaybackState.BUFFERING
            val url = playlistUrls[currentPlaylistIndex]

            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                setOnPreparedListener(this@QuranAudioService)
                setOnCompletionListener(this@QuranAudioService)
                setOnErrorListener(this@QuranAudioService)
                prepareAsync()
            }

            startForeground(NOTIFICATION_ID, buildNotification(isPlaying = true))
        } catch (e: Exception) {
            e.printStackTrace()
            _playbackState.value = PlaybackState.ERROR
        }
    }

    fun togglePlayPause() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
                _playbackState.value = PlaybackState.PAUSED
                updateNotification(isPlaying = false)
            } else {
                player.start()
                _playbackState.value = PlaybackState.PLAYING
                updateNotification(isPlaying = true)
            }
        }
    }

    fun playNext() {
        if (currentPlaylistIndex + 1 < playlistUrls.size) {
            currentPlaylistIndex++
            _currentAyahNumber.value = _currentAyahNumber.value + 1
            playCurrentIndex()
        }
    }

    fun playPrevious() {
        if (currentPlaylistIndex > 0) {
            currentPlaylistIndex--
            _currentAyahNumber.value = (_currentAyahNumber.value - 1).coerceAtLeast(1)
            playCurrentIndex()
        }
    }

    fun stopPlayback() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        _playbackState.value = PlaybackState.STOPPED
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mp?.start()
        _playbackState.value = PlaybackState.PLAYING
        updateNotification(isPlaying = true)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        playNext()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        _playbackState.value = PlaybackState.ERROR
        return true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_TOGGLE -> togglePlayPause()
            ACTION_NEXT -> playNext()
            ACTION_PREVIOUS -> playPrevious()
            ACTION_STOP -> stopPlayback()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        stopPlayback()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "تلاوة القرآن الكريم",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "تشغيل الصوت للتلاوة في الخلفية"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(isPlaying: Boolean): Notification {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            this, 0, openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val toggleIntent = Intent(this, QuranAudioService::class.java).apply { action = ACTION_TOGGLE }
        val togglePendingIntent = PendingIntent.getService(
            this, 1, toggleIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, QuranAudioService::class.java).apply { action = ACTION_STOP }
        val stopPendingIntent = PendingIntent.getService(
            this, 2, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("${_currentSurahName.value} - آية ${_currentAyahNumber.value}")
            .setContentText(_reciterName.value.ifEmpty { "تلاوة القرآن الكريم" })
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(openAppPendingIntent)
            .setOngoing(isPlaying)
            .addAction(
                if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play,
                if (isPlaying) "إيقاف مؤقت" else "تشغيل",
                togglePendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "إغلاق",
                stopPendingIntent
            )
            .build()
    }

    private fun updateNotification(isPlaying: Boolean) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, buildNotification(isPlaying))
    }

    companion object {
        const val CHANNEL_ID = "quran_audio_playback_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_TOGGLE = "com.example.roohalislam.TOGGLE"
        const val ACTION_NEXT = "com.example.roohalislam.NEXT"
        const val ACTION_PREVIOUS = "com.example.roohalislam.PREVIOUS"
        const val ACTION_STOP = "com.example.roohalislam.STOP"
    }
}
