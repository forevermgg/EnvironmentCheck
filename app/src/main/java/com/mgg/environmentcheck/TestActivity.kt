package com.mgg.environmentcheck

import android.content.pm.ApplicationInfo
import android.content.pm.ApplicationInfoHidden
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.rikka.tools.refine.Refine
import timber.log.Timber

class TestActivity : ComponentActivity() {
    private val FLAG_HIDDEN = 1 shl 27
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

        isAppHidden(this.application.applicationInfo)
        Timber.e("primaryCpuAbi:${Refine.unsafeCast<ApplicationInfoHidden>(applicationInfo).primaryCpuAbi}")
    }

    private fun isAppHidden(ai: ApplicationInfo): Boolean {
        return try {
            val flags: Int = Refine.unsafeCast<ApplicationInfoHidden>(ai).privateFlags
            flags or ApplicationInfoHidden.PRIVATE_FLAG_HIDDEN == flags
        } catch (e: Throwable) {
            ai.flags or FLAG_HIDDEN == ai.flags
        }
    }
}