package com.mgg.checkenv.monkey

import android.app.ActivityManager


object CheckMonkey {
    /**
     * Check if the normal method of "isUserAMonkey"
     * returns a quick win of who the user is.
     *
     * @return `true` if the user is a monkey
     * or `false` if not.
     */
    fun isUserAMonkey(): Boolean {
        return ActivityManager.isUserAMonkey()
    }
}
