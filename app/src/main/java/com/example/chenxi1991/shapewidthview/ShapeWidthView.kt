package com.example.chenxi1991.shapewidthview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ShapeWidthView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private val mPaint = Paint()

    //最大半径（onDraw的时候获取）
    private var mMaxFillRadius = 0f
    //最小半径
    private val mMinFillRadius = 5f
    //计算获得的填充圆半径
    private var mRadius = 0f

    //最大值
    private var mMaxWidthSize = 100f
    //最小值
    private var mMinWidthSize = 0f
    //当前值
    private var mCurrentSize = 50f

    //边框颜色
    private var mBoardColor = Color.GRAY

    //填充颜色
    private var mFillColor = Color.BLACK

    init {
        mPaint.isAntiAlias = true
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeWidthView)
        mMaxWidthSize = typeArray.getFloat(R.styleable.ShapeWidthView_sw_max_value, 100f)
        mMinWidthSize = typeArray.getFloat(R.styleable.ShapeWidthView_sw_min_value, 0f)
        mCurrentSize = typeArray.getFloat(R.styleable.ShapeWidthView_sw_current_value, 50f)
        mBoardColor = typeArray.getColor(R.styleable.ShapeWidthView_sw_board_color, Color.GRAY)
        mFillColor = typeArray.getColor(R.styleable.ShapeWidthView_sw_fill_color, Color.BLACK)
        if (mMinWidthSize >= mMaxWidthSize || mCurrentSize < mMinWidthSize || mCurrentSize > mMaxWidthSize)
            throw IllegalArgumentException()
        typeArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        mPaint.color = mBoardColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 1f
        val mStrokeRadius = (if (height > width) width / 2 else height / 2) - 2
        canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), mStrokeRadius.toFloat(), mPaint)

        mPaint.style = Paint.Style.FILL
        mPaint.color = mFillColor
        mMaxFillRadius = (mStrokeRadius - 1).toFloat()
        mRadius = ((mCurrentSize - mMinWidthSize) / (mMaxWidthSize - mMinWidthSize)) * (mMaxFillRadius - mMinFillRadius) + mMinFillRadius
        canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), mRadius, mPaint)
    }

    fun getSize(): Float {
        return mCurrentSize
    }

    fun updateSize(size: Float) {
        if (size < mMinWidthSize || size > mMaxWidthSize)
            return
        mCurrentSize = size
        invalidate()
    }
}