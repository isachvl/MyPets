package com.example.mypets.CustomView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.mypets.R

class CustomButton(
    context: Context,
    attrs: AttributeSet?
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()

    // Цвета кнопки
    var boneColor = Color.rgb(210, 155, 90)
        set(value) {
            field = value
            invalidate()
        }

    var bonePressedColor = Color.rgb(150, 89, 40)
        set(value) {
            field = value
            invalidate()
        }

    var boneTextColor = Color.WHITE
        set(value) {
            field = value
            invalidate()
        }

    // Текст на косточке
    var text = context.getString(R.string.singIn)
        set(value) {
            field = value
            contentDescription = value
            invalidate()
        }

    init {
        isClickable = true
        isFocusable = true

        // Получаем настройки из XML
        val attributes = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomButton
        )

        text = attributes.getString(
            R.styleable.CustomButton_boneText
        ) ?: context.getString(R.string.singIn)

        boneColor = attributes.getColor(
            R.styleable.CustomButton_boneColor,
            boneColor
        )

        bonePressedColor = attributes.getColor(
            R.styleable.CustomButton_bonePressedColor,
            bonePressedColor
        )

        boneTextColor = attributes.getColor(
            R.styleable.CustomButton_boneTextColor,
            boneTextColor
        )

        attributes.recycle()

        // Рисуем контур косточки
        path.moveTo(44f, 22f)

        path.cubicTo(26f, 0f, 0f, 16f, 15f, 40f)
        path.cubicTo(-2f, 60f, 4f, 94f, 28f, 94f)
        path.cubicTo(45f, 94f, 45f, 84f, 65f, 80f)

        path.cubicTo(93f, 70f, 147f, 70f, 175f, 80f)

        path.cubicTo(195f, 84f, 199f, 94f, 217f, 94f)
        path.cubicTo(242f, 94f, 245f, 60f, 225f, 40f)
        path.cubicTo(240f, 16f, 214f, 0f, 196f, 22f)

        path.cubicTo(184f, 34f, 180f, 42f, 165f, 42f)
        path.cubicTo(135f, 46f, 105f, 46f, 75f, 42f)
        path.cubicTo(60f, 42f, 56f, 34f, 44f, 22f)

        path.close()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Масштабируем косточку под размер View
        val scale = minOf(
            width / 240f,
            height / 100f
        )

        canvas.save()

        canvas.translate(
            (width - 240f * scale) / 2f,
            (height - 120f * scale) / 2f
        )

        canvas.scale(scale, scale)

        // Закрашиваем косточку
        paint.style = Paint.Style.FILL
        paint.color = if (isPressed) {
            bonePressedColor
        } else {
            boneColor
        }

        canvas.drawPath(path, paint)

        // Рисуем обводку
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f
        paint.color = Color.rgb(110, 65, 30)

        canvas.drawPath(path, paint)

        // Рисуем текст
        paint.style = Paint.Style.FILL
        paint.color = boneTextColor
        paint.textSize = 23f
        paint.textAlign = Paint.Align.CENTER
        paint.isFakeBoldText = true

        val centerY = 61f -
                (paint.ascent() + paint.descent()) / 2f

        canvas.drawText(
            text,
            120f,
            centerY,
            paint
        )

        canvas.restore()
    }

    // Обработка нажатия
    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.actionMasked) {

            MotionEvent.ACTION_DOWN -> {
                isPressed = true
                invalidate()
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                isPressed = event.x in 0f..width.toFloat() &&
                        event.y in 0f..height.toFloat()
                invalidate()
                return true
            }

            MotionEvent.ACTION_UP -> {
                val inside = event.x in 0f..width.toFloat() &&
                        event.y in 0f..height.toFloat()

                isPressed = false
                invalidate()

                if (inside) {
                    performClick()
                }

                return true
            }

            MotionEvent.ACTION_CANCEL -> {
                isPressed = false
                invalidate()
                return true
            }
        }

        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
