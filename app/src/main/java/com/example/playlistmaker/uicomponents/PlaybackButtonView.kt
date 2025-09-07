package com.example.playlistmaker.uicomponents

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R
import kotlin.math.min

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var imageBitmap: Bitmap?
    private var pauseButtonImage: Bitmap?
    private var playButtonImage: Bitmap?
    var setButtonClickListener: (() -> Unit)? = null
    private var imageRect = RectF(0f, 0f, 0f, 0f)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                pauseButtonImage =
                    getDrawable(R.styleable.PlaybackButtonView_pausedButton)?.toBitmap()
                playButtonImage =
                    getDrawable(R.styleable.PlaybackButtonView_playButton)?.toBitmap()
                imageBitmap = playButtonImage
            } finally {
                recycle()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN-> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        setButtonClickListener?.invoke()
        return super.performClick()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        if (imageBitmap != null) {
            canvas.drawBitmap(imageBitmap!!, null, imageRect, null)
        }
    }

    fun changeButtonState(isPlaying: Boolean) {
        imageBitmap = if (isPlaying) pauseButtonImage else playButtonImage
        invalidate()
    }

}