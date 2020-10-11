package ru.skillbranch.skillarticles.extensions

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

fun Context.dpToPx(dp: Int): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        this.resources.displayMetrics

    )
}

fun Context.dpToIntPx(dp: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        this.resources.displayMetrics
    ).toInt()
}

fun Context.attrValue(@AttrRes attr: Int, typedValue: TypedValue = TypedValue()) : Int {
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}