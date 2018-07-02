package com.example.chenxi1991.shapewidthview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        big.setOnClickListener {
            swv.updateSize(swv.getSize() + 1f)
        }

        small.setOnClickListener {
            swv.updateSize(swv.getSize() - 1f)
        }

        scs.mStateChangeCallBack = object : ShapeColorStateView.Companion.StateChangeCallBack {
            override fun onStateChange(currentState: ShapeColorStateView.Companion.CurrentState, color: Int) {
                Toast.makeText(this@MainActivity, color.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
