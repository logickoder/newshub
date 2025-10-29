package dev.logickoder.newshub.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dev.logickoder.newshub.BuildConfig
import dev.logickoder.newshub.app.domain.SingletonCompanion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppStore(
    private val context: Context,
) {
    suspend operator fun <T> set(key: Preferences.Key<T>, data: T?) {
        context.app.edit { preferences ->
            if (data == null) {
                preferences.remove(key)
            } else preferences[key] = data
        }
    }

    operator fun <T> get(key: Preferences.Key<T>): Flow<T?> {
        return context.app.data.map { preferences ->
            preferences[key]
        }
    }

    companion object : SingletonCompanion<AppStore, Context>() {
        private val Context.app: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(
            name = NAME
        )

        const val NAME = "${BuildConfig.APPLICATION_ID}.preferences_pb"

        override fun createInstance(dependency: Context) = AppStore(dependency.applicationContext)
    }
}