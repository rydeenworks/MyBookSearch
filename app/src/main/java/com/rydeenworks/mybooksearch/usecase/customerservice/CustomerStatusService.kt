package com.rydeenworks.mybooksearch.usecase.customerservice

import android.content.SharedPreferences

class CustomerStatusService(
    private val sharedPref: SharedPreferences,
    private val appReviewFlagKesStr: String,
) {
    fun saveAppReviewFlag() {
        val editor = sharedPref.edit()
        editor.putBoolean(appReviewFlagKesStr, true)
        editor.commit()
    }
    fun isAppReviewed(): Boolean {
        return sharedPref.getBoolean(appReviewFlagKesStr, false)
    }
}