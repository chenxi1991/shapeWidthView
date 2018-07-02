package com.example.chenxi1991.shapewidthview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import java.util.concurrent.atomic.AtomicBoolean

class ShapeColorStateView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    companion object {
        enum class CurrentState {
            SHOW_BOARD,
            SHOW_FILL
        }

        interface StateChangeCallBack {
            fun onStateChange(currentState: CurrentState, color: Int)
        }
    }

    private val mPaint = Paint()
    private val mEdgeColor = Color.parseColor("#C5C5C5")
    private var mBoardColor = Color.RED
    private var mFillColor = Color.BLUE
    private var mViewSize = 0f
    private var mCircleRadius = 0f

    private var mLeftTopX = 0f
    private var mLeftTopY = 0f
    private var mRightBottomX = 0f
    private var mRightBottomY = 0f

    private var mBoardX = 0f
    private var mBoardY = 0f
    private var mFillX = 0f
    private var mFillY = 0f
    private var inited = AtomicBoolean(false)

    private var animation: SwitchAnimation? = null

    private var mCurrentState = CurrentState.SHOW_BOARD

    var mStateChangeCallBack: StateChangeCallBack? = null


    init {
        mPaint.isAntiAlias = true
        animation = SwitchAnimation()
        animation?.duration = 300
        val t = context.obtainStyledAttributes(attrs, R.styleable.ShapeColorStateView)
        mBoardColor = t.getColor(R.styleable.ShapeColorStateView_ss_board_color, Color.BLACK)
        mFillColor = t.getColor(R.styleable.ShapeColorStateView_ss_fill_color, Color.TRANSPARENT)
        var mStateTemp = t.getInteger(R.styleable.ShapeColorStateView_ss_current_state, 0)
        when (mStateTemp) {
            0 -> mCurrentState = CurrentState.SHOW_BOARD
            1 -> mCurrentState = CurrentState.SHOW_FILL
        }
        t.recycle()


        setOnClickListener {
            mCurrentState = when (mCurrentState) {
                CurrentState.SHOW_BOARD -> CurrentState.SHOW_FILL
                CurrentState.SHOW_FILL -> CurrentState.SHOW_BOARD
            }
            mStateChangeCallBack?.onStateChange(mCurrentState, if (mCurrentState == CurrentState.SHOW_BOARD) mBoardColor else mFillColor)
            this.startAnimation(animation)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (!inited.get()) {
            mViewSize = if (width > height) height.toFloat() else width.toFloat()
            mCircleRadius = mViewSize * 0.6f / 2 - 1f

            mLeftTopX = mViewSize * 0.6f / 2 + 3f
            mLeftTopY = mLeftTopX

            mRightBottomX = mViewSize - mLeftTopX
            mRightBottomY = mViewSize - mLeftTopY
            when (mCurrentState) {
                CurrentState.SHOW_BOARD -> {
                    mBoardX = mLeftTopX
                    mBoardY = mLeftTopY
                    mFillX = mRightBottomX
                    mFillY = mRightBottomY
                }
                CurrentState.SHOW_FILL -> {
                    mFillX = mLeftTopX
                    mFillY = mLeftTopY
                    mBoardX = mRightBottomX
                    mBoardY = mRightBottomY
                }
            }
            inited.set(true)
        }

        when (mCurrentState) {
            CurrentState.SHOW_BOARD -> {
                drawFill(canvas, mFillX, mFillY, mCircleRadius)
                drawBoard(canvas, mBoardX, mBoardY, mCircleRadius)
            }
            CurrentState.SHOW_FILL -> {
                drawBoard(canvas, mBoardX, mBoardY, mCircleRadius)
                drawFill(canvas, mFillX, mFillY, mCircleRadius)
            }
        }

        drawLeftBottomArrow(canvas)
        drawRightTopArrow(canvas)
    }

    private fun drawBoard(canvas: Canvas?, x: Float, y: Float, r: Float) {
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 5f
        mPaint.color = mBoardColor
        canvas?.drawCircle(x, y, r - 3, mPaint)

        mPaint.strokeWidth = 1f
        mPaint.color = mEdgeColor
        canvas?.drawCircle(x, y, r, mPaint)
        canvas?.drawCircle(x, y, r - 5, mPaint)
    }

    private fun drawFill(canvas: Canvas?, x: Float, y: Float, r: Float) {
        mPaint.style = Paint.Style.FILL
        mPaint.color = mFillColor
        canvas?.drawCircle(x, y, r, mPaint)

        mPaint.style = Paint.Style.STROKE
        mPaint.color = mEdgeColor
        mPaint.strokeWidth = 1f
        canvas?.drawCircle(x, y, r, mPaint)
    }

    private fun drawLeftBottomArrow(canvas: Canvas?) {
        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.BLACK
        mPaint.strokeWidth = 1f
        val path = Path()
        path.moveTo(mViewSize * 0.35f, mViewSize * 0.85f)
        path.lineTo(mViewSize * 0.15f, mViewSize * 0.85f)
        path.lineTo(mViewSize * 0.15f, mViewSize * 0.65f)
        path.lineTo(mViewSize * 0.1f, mViewSize * 0.7f)
        path.moveTo(mViewSize * 0.15f, mViewSize * 0.65f)
        path.lineTo(mViewSize * 0.2f, mViewSize * 0.7f)
        canvas?.drawPath(path, mPaint)
    }

    private fun drawRightTopArrow(canvas: Canvas?) {
        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.BLACK
        mPaint.strokeWidth = 1f
        val path = Path()
        path.moveTo(mViewSize * 0.65f, mViewSize * 0.15f)
        path.lineTo(mViewSize * 0.85f, mViewSize * 0.15f)
        path.lineTo(mViewSize * 0.85f, mViewSize * 0.35f)
        path.lineTo(mViewSize * 0.8f, mViewSize * 0.3f)
        path.moveTo(mViewSize * 0.85f, mViewSize * 0.35f)
        path.lineTo(mViewSize * 0.9f, mViewSize * 0.3f)
        canvas?.drawPath(path, mPaint)
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

    inner class SwitchAnimation : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            super.applyTransformation(interpolatedTime, t)
            when (mCurrentState) {
                CurrentState.SHOW_BOARD -> {
                    mBoardX-=2
                    if (mBoardX < mLeftTopX) mBoardX = mLeftTopX
                    mBoardY-=2
                    if (mBoardY < mLeftTopY) mBoardY = mLeftTopY
                    mFillX+=2
                    if (mFillX > mRightBottomX) mFillX = mRightBottomX
                    mFillY+=2
                    if (mFillY > mRightBottomY) mFillY = mRightBottomY
                }
                CurrentState.SHOW_FILL -> {
                    mBoardX+=2
                    if (mBoardX > mRightBottomX) mBoardX = mRightBottomX
                    mBoardY+=2
                    if (mBoardY > mRightBottomY) mBoardY = mRightBottomY
                    mFillX-=2
                    if (mFillX < mLeftTopX) mFillX = mLeftTopX
                    mFillY-=2
                    if (mFillY < mLeftTopY) mFillY = mLeftTopY
                }
            }
            postInvalidate()
        }
    }
}