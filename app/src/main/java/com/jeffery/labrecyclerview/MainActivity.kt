package com.jeffery.labrecyclerview

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val data: ArrayList<String> = ArrayList()
        val context: Context = this
        val adapter: LabRecyclerViewAdapter<String>
                = object : LabRecyclerViewAdapter<String>(context, data, 1, 2) {
            override fun setConvertView(holder: LabRecyclerViewViewHolder, item: String, position: Int) {

            }
        }
    }
}
