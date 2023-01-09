package com.rydeenworks.mybooksearch.infrastructure.browser

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast

class OpenChromeBrowser(
    private val activity: Activity,
) {
    fun handle(uri: Uri)
    {
        try {
            val i = Intent()
            i.setPackage("com.android.chrome")
            i.action = Intent.ACTION_VIEW
            i.data = uri
            activity.startActivity(i)
        } catch (e: Exception) {
            val toast: Toast = Toast.makeText(activity, "ブラウザ起動に失敗しました", Toast.LENGTH_LONG)
            toast.show()
        }

    }
}