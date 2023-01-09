package com.rydeenworks.mybooksearch.usecase.customerservice

import android.content.Context
import android.content.SharedPreferences
import com.rydeenworks.mybooksearch.R

class AppReviewFlag(
    private val sharedPref: SharedPreferences,
    context: Context,
) {
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