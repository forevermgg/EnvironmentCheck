package com.mgg.environmentcheck

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

// https://cloud.tencent.com/developer/article/1915880
// https://stackoverflow.com/questions/61364874/view-models-for-recyclerview-items
class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // private val lifecycleRegistry: LifecycleRegistry by lazy { LifecycleRegistry(this) }

    init {
        itemView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            // View onDetached 的时候回调 onDestroy()
            override fun onViewDetachedFromWindow(v: View?) {
                // Timber.e("onViewDetachedFromWindow")
            }

            // View onAttached 的时候回调 onCreate()
            override fun onViewAttachedToWindow(v: View?) {
                // Timber.e("onViewAttachedToWindow")
            }
        })
    }

    fun bind(position: Int, msg: String) {
        /*viewModel.item.observe(this) {
            Timber.e("LifecycleViewHolderViewModel$it")
            itemView.findViewById<TextView>(R.id.tvContent).text = it
        }*/
        itemView.findViewById<TestView>(R.id.mTestView).findViewById<TextView>(R.id.tvContent).text =
            "position:$position : $msg"
        Timber.e("bind position:$position : $msg")
    }

    fun onCreate() {
        Timber.e("Lifecycle.State.CREATED")
        (itemView as? ViewGroup)?.children?.forEach {
            (it as? TestView)?.onCreate()
        }
    }

    fun onDestroy() {
        Timber.e("Lifecycle.State.DESTROYED")
        (itemView as? ViewGroup)?.children?.forEach {
            (it as? TestView)?.onDestroy()
        }
    }
}