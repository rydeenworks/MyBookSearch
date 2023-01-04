package com.rydeenworks.mybooksearch.usecase.customerservice

import android.content.Context
import android.content.Intent
import android.net.Uri

class ShowHelpPage(
   private val context: Context
) {
    fun handle()
    {
        val uri = Uri.parse("https://rydeenworks.hatenablog.com/entry/2019/09/12/214733")
        val i = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(i)
    }
}