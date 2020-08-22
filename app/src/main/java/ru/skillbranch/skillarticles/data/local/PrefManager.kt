package ru.skillbranch.skillarticles.data.local

import android.content.Context
import androidx.preference.PreferenceManager

/**
Реализуй в классе PrefManager(context:Context) (ru.skillbranch.skillarticles.data.local.PrefManager)
свойство val preferences : SharedPreferences проинициализированое экземпляром SharedPreferences приложения.
И метод fun clearAll() - очищающий все сохраненные значения SharedPreferences приложения.
Использовать PrefManager из androidx (import androidx.preference.PreferenceManager)
 */
class PrefManager(context: Context) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun clearAll() {
        preferences.edit().clear().apply()
    }
}