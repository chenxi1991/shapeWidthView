package com.example.chenxi1991.shapewidthview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        big.setOnClickListener {
            swv.updateSize(swv.getSize()+1f)
        }

        small.setOnClickListener {
            swv.updateSize(swv.getSize()-1f)
        }
    }
}
