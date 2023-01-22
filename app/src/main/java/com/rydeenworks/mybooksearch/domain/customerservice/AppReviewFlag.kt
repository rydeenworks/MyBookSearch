package com.rydeenworks.mybooksearch.domain.customerservice

import android.content.Context
import android.content.SharedPreferences
import com.rydeenworks.mybooksearch.R

class AppReviewFlag(
//    private val sharedPref: SharedPreferences,
    context: Context,
) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.preference_history_file_key), Context.MODE_PRIVATE)

    private val appReviewFlagKesStr: String = context.getString(R.string.app_is_reviewd)
    fun saveAppReviewFlag() {
        val editor = sharedPref.edit()
        editor.putBoolean(appReviewFlagKesStr, true)
        editor.commit()
    }
    fun isAppReviewed(): Boolean {
        return sharedPref.getBoolean(appReviewFlagKesStr, false)
    }
}