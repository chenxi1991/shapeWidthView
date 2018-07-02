package com.example.chenxi1991.shapewidthview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

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

    private var mCurrentState = CurrentState.SHOW_BOARD

    var mStateChangeCallBack: StateChangeCallBack? = null

    init {
        mPaint.isAntiAlias = true
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
            invalidate()
            mStateChangeCallBack?.onStateChange(mCurrentState, if (mCurrentState == CurrentState.SHOW_BOARD) mBoardColor else mFillColor)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        mViewSize = if (width > height) height.toFloat() else width.toFloat()
        mCircleRadius = mViewSize * 0.6f / 2 - 1f

        val mLeftTopX = mViewSize * 0.6f / 2 + 3f
        val mLeftTopY = mLeftTopX

        val mRightBottomX = mViewSize - mLeftTopX
        val mRightBottomY = mViewSize - mLeftTopY

        when (mCurrentState) {
            CurrentState.SHOW_BOARD -> {
                drawFill(canvas, mRightBottomX, mRightBottomY, mCircleRadius)
                drawBoard(canvas, mLeftTopX, mLeftTopY, mCircleRadius)
            }
            CurrentState.SHOW_FILL -> {
                drawBoard(canvas, mRightBottomX, mRightBottomY, mCircleRadius)
                drawFill(canvas, mLeftTopX, mLeftTopY, mCircleRadius)
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
}