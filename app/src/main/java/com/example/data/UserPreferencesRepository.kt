package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.CalculationMethod
import com.example.model.LocationCity
import com.example.model.QadaRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferencesRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("rooh_al_islam_prefs", Context.MODE_PRIVATE)

    private val _qadaRecord = MutableStateFlow(loadQada())
    val qadaRecord: StateFlow<QadaRecord> = _qadaRecord.asStateFlow()

    private val _tasbeehTotal = MutableStateFlow(prefs.getInt(KEY_TASBEEH_TOTAL, 0))
    val tasbeehTotal: StateFlow<Int> = _tasbeehTotal.asStateFlow()

    private val _lastReadSurah = MutableStateFlow(prefs.getInt(KEY_LAST_SURAH, 1))
    val lastReadSurah: StateFlow<Int> = _lastReadSurah.asStateFlow()

    private val _lastReadAyah = MutableStateFlow(prefs.getInt(KEY_LAST_AYAH, 1))
    val lastReadAyah: StateFlow<Int> = _lastReadAyah.asStateFlow()

    private val _selectedCityIndex = MutableStateFlow(prefs.getInt(KEY_CITY_INDEX, 0))
    val selectedCityIndex: StateFlow<Int> = _selectedCityIndex.asStateFlow()

    private val _selectedMethod = MutableStateFlow(
        CalculationMethod.valueOf(
            prefs.getString(KEY_CALC_METHOD, CalculationMethod.EGYPTIAN.name) ?: CalculationMethod.EGYPTIAN.name
        )
    )
    val selectedMethod: StateFlow<CalculationMethod> = _selectedMethod.asStateFlow()

    private val _selectedReciterId = MutableStateFlow(
        prefs.getString(KEY_RECITER_ID, "menshawy") ?: "menshawy"
    )
    val selectedReciterId: StateFlow<String> = _selectedReciterId.asStateFlow()

    private val _azanNotificationEnabled = MutableStateFlow(
        prefs.getBoolean(KEY_AZAN_NOTIFS, true)
    )
    val azanNotificationEnabled: StateFlow<Boolean> = _azanNotificationEnabled.asStateFlow()

    private val _dailyVerseNotificationEnabled = MutableStateFlow(
        prefs.getBoolean(KEY_DAILY_VERSE_NOTIFS, true)
    )
    val dailyVerseNotificationEnabled: StateFlow<Boolean> = _dailyVerseNotificationEnabled.asStateFlow()

    // Qada management
    private fun loadQada(): QadaRecord {
        return QadaRecord(
            fajr = prefs.getInt(KEY_QADA_FAJR, 0),
            dhuhr = prefs.getInt(KEY_QADA_DHUHR, 0),
            asr = prefs.getInt(KEY_QADA_ASR, 0),
            maghrib = prefs.getInt(KEY_QADA_MAGHRIB, 0),
            isha = prefs.getInt(KEY_QADA_ISHA, 0)
        )
    }

    fun updateQada(record: QadaRecord) {
        prefs.edit()
            .putInt(KEY_QADA_FAJR, record.fajr)
            .putInt(KEY_QADA_DHUHR, record.dhuhr)
            .putInt(KEY_QADA_ASR, record.asr)
            .putInt(KEY_QADA_MAGHRIB, record.maghrib)
            .putInt(KEY_QADA_ISHA, record.isha)
            .apply()
        _qadaRecord.value = record
    }

    fun incrementQada(prayerIndex: Int) {
        val cur = _qadaRecord.value
        val updated = when (prayerIndex) {
            0 -> cur.copy(fajr = cur.fajr + 1)
            1 -> cur.copy(dhuhr = cur.dhuhr + 1)
            2 -> cur.copy(asr = cur.asr + 1)
            3 -> cur.copy(maghrib = cur.maghrib + 1)
            4 -> cur.copy(isha = cur.isha + 1)
            else -> cur
        }
        updateQada(updated)
    }

    fun decrementQada(prayerIndex: Int) {
        val cur = _qadaRecord.value
        val updated = when (prayerIndex) {
            0 -> cur.copy(fajr = (cur.fajr - 1).coerceAtLeast(0))
            1 -> cur.copy(dhuhr = (cur.dhuhr - 1).coerceAtLeast(0))
            2 -> cur.copy(asr = (cur.asr - 1).coerceAtLeast(0))
            3 -> cur.copy(maghrib = (cur.maghrib - 1).coerceAtLeast(0))
            4 -> cur.copy(isha = (cur.isha - 1).coerceAtLeast(0))
            else -> cur
        }
        updateQada(updated)
    }

    // Tasbeeh
    fun addTasbeehCount(count: Int = 1) {
        val newTotal = _tasbeehTotal.value + count
        prefs.edit().putInt(KEY_TASBEEH_TOTAL, newTotal).apply()
        _tasbeehTotal.value = newTotal
    }

    fun resetTasbeeh() {
        prefs.edit().putInt(KEY_TASBEEH_TOTAL, 0).apply()
        _tasbeehTotal.value = 0
    }

    // Bookmarks & Last Read
    fun saveLastRead(surahId: Int, ayahNumber: Int) {
        prefs.edit()
            .putInt(KEY_LAST_SURAH, surahId)
            .putInt(KEY_LAST_AYAH, ayahNumber)
            .apply()
        _lastReadSurah.value = surahId
        _lastReadAyah.value = ayahNumber
    }

    // Location & Calculation method
    fun setCityIndex(index: Int) {
        prefs.edit().putInt(KEY_CITY_INDEX, index).apply()
        _selectedCityIndex.value = index
    }

    fun setCalculationMethod(method: CalculationMethod) {
        prefs.edit().putString(KEY_CALC_METHOD, method.name).apply()
        _selectedMethod.value = method
    }

    fun setReciterId(reciterId: String) {
        prefs.edit().putString(KEY_RECITER_ID, reciterId).apply()
        _selectedReciterId.value = reciterId
    }

    fun toggleAzanNotification() {
        val newVal = !_azanNotificationEnabled.value
        prefs.edit().putBoolean(KEY_AZAN_NOTIFS, newVal).apply()
        _azanNotificationEnabled.value = newVal
    }

    fun toggleDailyVerseNotification() {
        val newVal = !_dailyVerseNotificationEnabled.value
        prefs.edit().putBoolean(KEY_DAILY_VERSE_NOTIFS, newVal).apply()
        _dailyVerseNotificationEnabled.value = newVal
    }

    companion object {
        private const val KEY_TASBEEH_TOTAL = "tasbeeh_total"
        private const val KEY_LAST_SURAH = "last_surah"
        private const val KEY_LAST_AYAH = "last_ayah"
        private const val KEY_CITY_INDEX = "city_index"
        private const val KEY_CALC_METHOD = "calc_method"
        private const val KEY_RECITER_ID = "reciter_id"
        private const val KEY_AZAN_NOTIFS = "azan_notifs"
        private const val KEY_DAILY_VERSE_NOTIFS = "daily_verse_notifs"

        private const val KEY_QADA_FAJR = "qada_fajr"
        private const val KEY_QADA_DHUHR = "qada_dhuhr"
        private const val KEY_QADA_ASR = "qada_asr"
        private const val KEY_QADA_MAGHRIB = "qada_maghrib"
        private const val KEY_QADA_ISHA = "qada_isha"
    }
}
