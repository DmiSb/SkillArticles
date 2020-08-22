package ru.skillbranch.skillarticles.data.delegates

import ru.skillbranch.skillarticles.data.local.PrefManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
Реализуй делегат PrefDelegate<T>(private val defaultValue: T) : ReadWriteProperty<PrefManager, T?>
(ru.skillbranch.skillarticles.data.delegates.PrefDelegate) возвращающий значений примитивов (Boolean, String, Float, Int, Long)

Пример: var storedBoolean by PrefDelegate(false)
var storedString by PrefDelegate("")
var storedFloat by PrefDelegate(0f)
var storedInt by PrefDelegate(0)
var storedLong by PrefDelegate(0)
 */

class PrefDelegate<T>(private val defaultValue: T) : ReadWriteProperty<PrefManager, T?> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: PrefManager, property: KProperty<*>): T? {
        return with(thisRef.preferences) {
            when (defaultValue) {
                is Boolean -> getBoolean(property.name, defaultValue)  as T
                is String -> getString(property.name, defaultValue) as T
                is Float -> getFloat(property.name, defaultValue) as T
                is Int -> getInt(property.name, defaultValue) as T
                is Long -> getLong(property.name, defaultValue) as T
                else -> null
            }
        }
    }

    override fun setValue(thisRef: PrefManager, property: KProperty<*>, value: T?) {
        with(thisRef.preferences.edit()) {
            when (value) {
                is Boolean -> putBoolean(property.name, value)
                is String -> putString(property.name, value).apply()
                is Float -> putFloat(property.name, value)
                is Int -> putInt(property.name, value)
                is Long -> putLong(property.name, value)
            }
            apply()
        }
    }
}