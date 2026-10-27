package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.UserPreferencesRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun readStringFromContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("روح الإسلام", appName)
    }

    @Test
    fun testUserPreferencesRepository() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = UserPreferencesRepository(context)
        assertNotNull(repo.qadaRecord.value)
        repo.incrementQada(0) // Fajr
        assertEquals(1, repo.qadaRecord.value.fajr)
    }
}
