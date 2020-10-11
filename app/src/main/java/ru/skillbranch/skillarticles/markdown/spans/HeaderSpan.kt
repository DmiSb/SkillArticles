package ru.skillbranch.skillarticles.markdown.spans

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.Spanned
import android.text.TextPaint
import android.text.style.LeadingMarginSpan
import android.text.style.LineHeightSpan
import android.text.style.MetricAffectingSpan
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.annotation.Px
import androidx.annotation.VisibleForTesting

class HeaderSpan constructor(
    @IntRange(from = 1, to = 6)
    private val level: Int,
    @ColorInt
    private val textColor: Int,
    @ColorInt
    private val dividerColor: Int,
    @Px
    private val marginTop: Float,
    @Px
    private val marginBottom: Float
) : MetricAffectingSpan(), LineHeightSpan, LeadingMarginSpan {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val linePadding = 0.4f

    private var originAscent = 0

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val sizes = mapOf(
        1 to 2f,
        2 to 1.5f,
        3 to 1.25f,
        4 to 1f,
        5 to 0.875f,
        6 to 0.85f
    )

    override fun chooseHeight(
        text: CharSequence?,
        start: Int,
        end: Int,
        spanstartv: Int,
        lineHeight: Int,
        fontMetrics: Paint.FontMetricsInt?
    ) {
        fontMetrics ?: return

        text as Spanned
        val spanStart = text.getSpanStart(this)
        val spanEnd = text.getSpanEnd(this)

        if (spanStart == start) {
            originAscent = fontMetrics.ascent
            fontMetrics.ascent = (fontMetrics.ascent - marginTop).toInt()
        } else {
            fontMetrics.ascent = originAscent
        }

        // line break +1 character
        if (spanEnd == end.dec()) {
            val originHeight = fontMetrics.descent - originAscent
            fontMetrics.descent = (originHeight * linePadding + marginBottom).toInt()
        }

        fontMetrics.top = fontMetrics.ascent
        fontMetrics.bottom = fontMetrics.descent
    }

    override fun updateMeasureState(paint: TextPaint) {
        with(paint) {
            textSize *= sizes.getOrElse(level) { 1f }
            isFakeBoldText = true
        }
    }

    override fun updateDrawState(textPaint: TextPaint) {
        with(textPaint) {
            textSize *= sizes.getOrElse(level) { 1f }
            isFakeBoldText = true
            color = textColor
        }
    }

    override fun getLeadingMargin(first: Boolean): Int {
        return 0
    }

    override fun drawLeadingMargin(
        canvas: Canvas,
        paint: Paint,
        currentMarginLocation: Int,
        paragraphDirection: Int,
        lineTop: Int,
        lineBaseline: Int,
        lineBottom: Int,
        text: CharSequence?,
        lineStart: Int,
        lineEnd: Int,
        isFirstLine: Boolean,
        layout: Layout?
    ) {
        if ((level == 1 || level == 2) && (text as Spanned).getSpanEnd(this) == lineEnd) {
            paint.forLine {
                val lineHeight = (paint.descent() - paint.ascent()) * sizes.getOrElse(level) { 1f }
                val lineOffset = lineBaseline + lineHeight * linePadding
                canvas.drawLine(0f, lineOffset, canvas.width.toFloat(), lineOffset, paint)
            }
        }
    }

    private inline fun Paint.forLine(block: () -> Unit) {
        val oldColor = color
        val oldStyle = style
        val oldWidth = strokeWidth

        color = dividerColor
        style = Paint.Style.STROKE
        strokeWidth = 0f

        block()

        color = oldColor
        style = oldStyle
        strokeWidth = oldWidth
    }
}