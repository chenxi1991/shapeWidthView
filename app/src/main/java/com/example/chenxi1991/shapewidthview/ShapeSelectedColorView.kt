package com.example.chenxi1991.shapewidthview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ShapeSelectedColorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private val mPaint = Paint()
    private var mBoardColor = Color.BLACK
    private var mFillColor = Color.TRANSPARENT

    private val mBoardWidth = 3f

    init {
        mPaint.isAntiAlias = true
        val t = context.obtainStyledAttributes(attrs, R.styleable.ShapeSelectedColorView)
        mBoardColor = t.getColor(R.styleable.ShapeSelectedColorView_sc_board_color, Color.BLACK)
        mFillColor = t.getColor(R.styleable.ShapeSelectedColorView_sc_fill_color, Color.TRANSPARENT)
        t.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        mPaint.color = mFillColor
        mPaint.style = Paint.Style.FILL
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), mPaint)

        mPaint.color = mBoardColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mBoardWidth
        canvas?.drawRect(0f, 0f, height.toFloat(), width.toFloat(), mPaint)
    }

    fun setBoardColor(color: Int) {
        this.mBoardColor = color
        invalidate()
    }

    fun getBoardColor(): Int {
        return mBoardColor
    }

    fun setFillColor(color: Int) {
        this.mFillColor = color
        invalidate()
    }

    fun getFillColor(): Int {
        return mFillColor
    }
}