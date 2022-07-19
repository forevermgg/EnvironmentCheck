package com.mgg.environmentcheck

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class TestAdapter(var dataList: List<String>) : RecyclerView.Adapter<TestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        Timber.e("onCreateViewHolder")
        return TestViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_letter, parent, false))
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.onCreate()
        Timber.e("onBindViewHolder")
        holder.bind(position, dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onViewRecycled(holder: TestViewHolder) {
        holder.onDestroy()
    }
}