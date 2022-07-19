package com.mgg.environmentcheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val characterList = mutableListOf<String>()
        val dictChars = mutableListOf<Char>().apply { "123456789zxcvbnmasdfghjklqwertyuiop".forEach { this.add(it) } }
        for (i in 0 until 50) {
            val randomStr = StringBuilder().apply { (1..((10..30).random())).onEach { append(dictChars.random()) } }
            characterList.add(randomStr.toString())
        }

        val mLetterAdapter = TestAdapter(characterList)
        val letterReView = findViewById<RecyclerView>(R.id.mRecyclerView)
        letterReView.adapter = mLetterAdapter
        letterReView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }
}