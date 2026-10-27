package com.example.ui.viewmodel

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AzkarRepository
import com.example.data.DailyContentProvider
import com.example.data.PrayerTimeCalculator
import com.example.data.QiblaCalculator
import com.example.data.QuranRepository
import com.example.data.UserPreferencesRepository
import com.example.model.Ayah
import com.example.model.CalculationMethod
import com.example.model.DailyHadith
import com.example.model.DailyVerse
import com.example.model.HijriDate
import com.example.model.LocationCity
import com.example.model.PrayerTimeItem
import com.example.model.QadaRecord
import com.example.model.Reciter
import com.example.model.ReligiousOccasion
import com.example.model.Surah
import com.example.model.ZikrCategory
import com.example.model.ZikrItem
import com.example.sensor.QiblaCompassSensorManager
import com.example.service.QuranAudioService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.abs

enum class AppTab(val titleAr: String) {
    HOME("الرئيسية"),
    QURAN("القرآن"),
    PRAYER("الصلوات"),
    QIBLA("القبلة"),
    AZKAR("الأذكار"),
    TASBEEH("السبحة"),
    CALENDAR("التقويم")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val userPrefs = UserPreferencesRepository(application)
    private val compassSensorManager = QiblaCompassSensorManager(application)

    // Active Tab & Navigation
    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    // Sub-screens
    private val _selectedSurah = MutableStateFlow<Surah?>(null)
    val selectedSurah: StateFlow<Surah?> = _selectedSurah.asStateFlow()

    private val _activeAyahs = MutableStateFlow<List<Ayah>>(emptyList())
    val activeAyahs: StateFlow<List<Ayah>> = _activeAyahs.asStateFlow()

    private val _selectedTafseerAyah = MutableStateFlow<Ayah?>(null)
    val selectedTafseerAyah: StateFlow<Ayah?> = _selectedTafseerAyah.asStateFlow()

    private val _selectedAzkarCategory = MutableStateFlow<ZikrCategory?>(null)
    val selectedAzkarCategory: StateFlow<ZikrCategory?> = _selectedAzkarCategory.asStateFlow()

    private val _activeAzkarList = MutableStateFlow<List<ZikrItem>>(emptyList())
    val activeAzkarList: StateFlow<List<ZikrItem>> = _activeAzkarList.asStateFlow()

    // Quran Search
    private val _quranSearchQuery = MutableStateFlow("")
    val quranSearchQuery: StateFlow<String> = _quranSearchQuery.asStateFlow()

    // Locations & Prayer Times
    private val _currentCity = MutableStateFlow(PrayerTimeCalculator.PRESET_CITIES[userPrefs.selectedCityIndex.value.coerceIn(0, PrayerTimeCalculator.PRESET_CITIES.lastIndex)])
    val currentCity: StateFlow<LocationCity> = _currentCity.asStateFlow()

    private val _prayerTimes = MutableStateFlow<List<PrayerTimeItem>>(emptyList())
    val prayerTimes: StateFlow<List<PrayerTimeItem>> = _prayerTimes.asStateFlow()

    private val _nextPrayerCountdown = MutableStateFlow("")
    val nextPrayerCountdown: StateFlow<String> = _nextPrayerCountdown.asStateFlow()

    private val _nextPrayerName = MutableStateFlow("")
    val nextPrayerName: StateFlow<String> = _nextPrayerName.asStateFlow()

    // Qibla
    private val _qiblaBearing = MutableStateFlow(0f)
    val qiblaBearing: StateFlow<Float> = _qiblaBearing.asStateFlow()

    private val _distanceToKaabaKm = MutableStateFlow(0)
    val distanceToKaabaKm: StateFlow<Int> = _distanceToKaabaKm.asStateFlow()

    val deviceAzimuth: StateFlow<Float> = compassSensorManager.azimuth

    private val _isFacingQibla = MutableStateFlow(false)
    val isFacingQibla: StateFlow<Boolean> = _isFacingQibla.asStateFlow()

    // Tasbeeh
    private val _tasbeehSessionCount = MutableStateFlow(0)
    val tasbeehSessionCount: StateFlow<Int> = _tasbeehSessionCount.asStateFlow()

    private val _tasbeehTarget = MutableStateFlow(33)
    val tasbeehTarget: StateFlow<Int> = _tasbeehTarget.asStateFlow()

    private val _tasbeehDhikrText = MutableStateFlow("سُبْحَانَ اللَّهِ")
    val tasbeehDhikrText: StateFlow<String> = _tasbeehDhikrText.asStateFlow()

    val tasbeehTotal: StateFlow<Int> = userPrefs.tasbeehTotal

    // Qada
    val qadaRecord: StateFlow<QadaRecord> = userPrefs.qadaRecord

    // Settings & Content
    val selectedMethod: StateFlow<CalculationMethod> = userPrefs.selectedMethod
    val selectedReciterId: StateFlow<String> = userPrefs.selectedReciterId
    val azanNotificationEnabled: StateFlow<Boolean> = userPrefs.azanNotificationEnabled

    val dailyVerse: DailyVerse = DailyContentProvider.getTodayVerse()
    val dailyHadith: DailyHadith = DailyContentProvider.getTodayHadith()
    val hijriDate: HijriDate = DailyContentProvider.getHijriDate()
    val occasions: List<ReligiousOccasion> = DailyContentProvider.getUpcomingOccasions()

    // Audio Service Connection
    private var audioService: QuranAudioService? = null
    private var isAudioServiceBound = false

    private val _audioPlaybackState = MutableStateFlow(QuranAudioService.PlaybackState.IDLE)
    val audioPlaybackState: StateFlow<QuranAudioService.PlaybackState> = _audioPlaybackState.asStateFlow()

    private val _currentPlayingAyah = MutableStateFlow(1)
    val currentPlayingAyah: StateFlow<Int> = _currentPlayingAyah.asStateFlow()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val localBinder = binder as? QuranAudioService.LocalBinder
            audioService = localBinder?.getService()
            isAudioServiceBound = true

            audioService?.let { service ->
                viewModelScope.launch {
                    service.playbackState.collect { state ->
                        _audioPlaybackState.value = state
                    }
                }
                viewModelScope.launch {
                    service.currentAyahNumber.collect { ayahNum ->
                        _currentPlayingAyah.value = ayahNum
                    }
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            audioService = null
            isAudioServiceBound = false
        }
    }

    init {
        updatePrayerTimes()
        recalculateQibla()
        startPeriodicClock()
        bindAudioService()
        compassSensorManager.startListening()

        // Monitor alignment between compass heading and Qibla angle
        viewModelScope.launch {
            deviceAzimuth.collect { heading ->
                val targetAngle = _qiblaBearing.value
                var diff = abs(heading - targetAngle)
                if (diff > 180f) diff = 360f - diff
                _isFacingQibla.value = diff <= 4f
            }
        }
    }

    private fun bindAudioService() {
        val intent = Intent(getApplication(), QuranAudioService::class.java)
        getApplication<Application>().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onCleared() {
        super.onCleared()
        compassSensorManager.stopListening()
        if (isAudioServiceBound) {
            getApplication<Application>().unbindService(serviceConnection)
            isAudioServiceBound = false
        }
    }

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun selectSurah(surah: Surah?) {
        _selectedSurah.value = surah
        if (surah != null) {
            val reciter = getCurrentReciter()
            _activeAyahs.value = QuranRepository.getAyahsForSurah(surah.id, reciter)
            userPrefs.saveLastRead(surah.id, 1)
        }
    }

    fun selectTafseerAyah(ayah: Ayah?) {
        _selectedTafseerAyah.value = ayah
    }

    fun selectAzkarCategory(category: ZikrCategory?) {
        _selectedAzkarCategory.value = category
        if (category != null) {
            _activeAzkarList.value = AzkarRepository.getAzkarByCategory(category.id)
        }
    }

    fun incrementZikrCounter(index: Int) {
        val list = _activeAzkarList.value.toMutableList()
        if (index in list.indices) {
            val item = list[index]
            if (item.currentCount < item.targetCount) {
                list[index] = item.copy(currentCount = item.currentCount + 1)
                _activeAzkarList.value = list
            }
        }
    }

    fun resetZikrItem(index: Int) {
        val list = _activeAzkarList.value.toMutableList()
        if (index in list.indices) {
            val item = list[index]
            list[index] = item.copy(currentCount = 0)
            _activeAzkarList.value = list
        }
    }

    fun setQuranSearchQuery(query: String) {
        _quranSearchQuery.value = query
    }

    // Audio Playback
    fun playSurahAudio(surah: Surah, startAyahIndex: Int = 0) {
        val reciter = getCurrentReciter()
        val ayahs = QuranRepository.getAyahsForSurah(surah.id, reciter)
        val urls = ayahs.map { it.audioUrl }

        val intent = Intent(getApplication(), QuranAudioService::class.java)
        getApplication<Application>().startService(intent)

        audioService?.playAyahSequence(
            urls = urls,
            startIndex = startAyahIndex,
            surahName = surah.nameAr,
            reciter = reciter.nameAr,
            startAyahNumber = ayahs.getOrNull(startAyahIndex)?.numberInSurah ?: 1
        )
    }

    fun toggleAudioPlayPause() {
        audioService?.togglePlayPause()
    }

    fun playNextAyah() {
        audioService?.playNext()
    }

    fun playPreviousAyah() {
        audioService?.playPrevious()
    }

    fun stopAudio() {
        audioService?.stopPlayback()
    }

    fun getCurrentReciter(): Reciter {
        val reciterId = userPrefs.selectedReciterId.value
        return QuranRepository.RECITERS.firstOrNull { it.id == reciterId }
            ?: QuranRepository.RECITERS[0]
    }

    fun setReciter(reciter: Reciter) {
        userPrefs.setReciterId(reciter.id)
        // Refresh active ayahs with new reciter URLs if reading
        _selectedSurah.value?.let { surah ->
            _activeAyahs.value = QuranRepository.getAyahsForSurah(surah.id, reciter)
        }
    }

    // City & Prayer Times
    fun selectCity(cityIndex: Int) {
        val validIndex = cityIndex.coerceIn(0, PrayerTimeCalculator.PRESET_CITIES.lastIndex)
        userPrefs.setCityIndex(validIndex)
        _currentCity.value = PrayerTimeCalculator.PRESET_CITIES[validIndex]
        updatePrayerTimes()
        recalculateQibla()
    }

    fun setCalculationMethod(method: CalculationMethod) {
        userPrefs.setCalculationMethod(method)
        updatePrayerTimes()
    }

    fun toggleAzanNotification() {
        userPrefs.toggleAzanNotification()
    }

    private fun updatePrayerTimes() {
        val city = _currentCity.value
        val times = PrayerTimeCalculator.calculateTimes(
            calendar = Calendar.getInstance(),
            lat = city.latitude,
            lng = city.longitude,
            timezoneHours = city.timezoneOffsetHours,
            method = userPrefs.selectedMethod.value
        )
        _prayerTimes.value = times

        val next = times.firstOrNull { it.isNext }
        if (next != null) {
            _nextPrayerName.value = next.prayer.arabicName
        }
    }

    private fun recalculateQibla() {
        val city = _currentCity.value
        _qiblaBearing.value = QiblaCalculator.calculateBearing(city.latitude, city.longitude)
        _distanceToKaabaKm.value = QiblaCalculator.calculateDistanceKm(city.latitude, city.longitude)
    }

    private fun startPeriodicClock() {
        viewModelScope.launch {
            while (true) {
                val cal = Calendar.getInstance()
                val currentMin = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
                val currentSec = cal.get(Calendar.SECOND)

                val next = _prayerTimes.value.firstOrNull { it.isNext }
                if (next != null) {
                    var diffMins = next.timeMinutesOfDay - currentMin
                    if (diffMins < 0) diffMins += 24 * 60
                    val totalSecs = (diffMins * 60) - currentSec
                    if (totalSecs > 0) {
                        val hours = totalSecs / 3600
                        val mins = (totalSecs % 3600) / 60
                        val secs = totalSecs % 60
                        _nextPrayerCountdown.value = String.format("%02d:%02d:%02d", hours, mins, secs)
                    } else {
                        updatePrayerTimes()
                    }
                }
                delay(1000)
            }
        }
    }

    // Tasbeeh
    fun tapTasbeeh() {
        val next = _tasbeehSessionCount.value + 1
        if (next >= _tasbeehTarget.value) {
            _tasbeehSessionCount.value = 0
        } else {
            _tasbeehSessionCount.value = next
        }
        userPrefs.addTasbeehCount(1)
    }

    fun resetTasbeehSession() {
        _tasbeehSessionCount.value = 0
    }

    fun resetAllTasbeeh() {
        _tasbeehSessionCount.value = 0
        userPrefs.resetTasbeeh()
    }

    fun setTasbeehTarget(target: Int) {
        _tasbeehTarget.value = target
        _tasbeehSessionCount.value = 0
    }

    fun setTasbeehDhikr(dhikr: String) {
        _tasbeehDhikrText.value = dhikr
        _tasbeehSessionCount.value = 0
    }

    // Qada
    fun incrementQada(prayerIndex: Int) {
        userPrefs.incrementQada(prayerIndex)
    }

    fun decrementQada(prayerIndex: Int) {
        userPrefs.decrementQada(prayerIndex)
    }
}
