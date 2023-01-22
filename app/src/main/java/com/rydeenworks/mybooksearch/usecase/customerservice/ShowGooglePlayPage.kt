package com.rydeenworks.mybooksearch.usecase.customerservice

import android.app.Activity
import android.content.Intent
import android.net.Uri

class ShowGooglePlayPage {

    fun handle(activity: Activity)
    {
        val uri =
            Uri.parse("https://play.google.com/store/apps/details?id=com.rydeenworks.mybooksearch")
        val i = Intent(Intent.ACTION_VIEW, uri)
        activity.startActivity(i)
    }
}