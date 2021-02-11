package ru.skillbranch.skillarticles.ui.custom.markdown

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.text.Spannable
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.core.graphics.withTranslation
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.extensions.attrValue
import ru.skillbranch.skillarticles.extensions.dpToIntPx

@SuppressLint("ViewConstructor", "AppCompatCustomView")
class MarkdownTextView constructor(
    context: Context,
    fontSize: Float,
    mockHelper: SearchBgHelper?=null
): TextView(context,null,0),IMarkdownView {

    override var fontSize: Float=fontSize
        //get() = fontSize
        set(value) {
            textSize=value
            field=value
        }

    override val spannableContent: Spannable
        get() = text as Spannable

    val color=context.attrValue(R.attr.colorOnBackground)
    private  val focusRect= Rect()

    private var searchBgHelper=SearchBgHelper(context=context) {top,bottom->
        focusRect.set(0,top-context.dpToIntPx(56),width,bottom+context.dpToIntPx(56))

        requestRectangleOnScreen(focusRect,false)
    }

    init {
        searchBgHelper=mockHelper ?: SearchBgHelper(context) {top,bottom->
            focusRect.set(0,top-context.dpToIntPx(56),width,bottom+context.dpToIntPx(56))

            requestRectangleOnScreen(focusRect,false)
        }

        //setBackgroundColor(Color.GREEN)
        setTextColor(color)
        textSize=fontSize
        movementMethod=LinkMovementMethod.getInstance()
    }

    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)
        if (layout!=null && text is Spanned) {
            canvas.withTranslation (totalPaddingLeft.toFloat(),totalPaddingTop.toFloat()) {
                searchBgHelper.draw(canvas=canvas,text=text as Spanned,layout = layout)
            }
        }
        super.onDraw(canvas)
    }
}