package com.rydeenworks.mybooksearch.ui.customerservice

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import com.rydeenworks.mybooksearch.R
import com.rydeenworks.mybooksearch.usecase.customerservice.AppReviewFlag

class ReviewDialog (
    private val activity: Activity
        ){

    private val appReviewFlag: AppReviewFlag = AppReviewFlag(activity)

    fun showDialog() {
        AlertDialog.Builder(activity)
            .setTitle("レビューにご協力お願いします")
            .setMessage("いつもご利用ありがとうございます。よろしければ励ましのレビューをお寄せください")
            .setPositiveButton("レビューする") { dialog: DialogInterface?, which: Int ->
                appReviewFlag.saveAppReviewFlag()
                val uri =
                    Uri.parse("https://play.google.com/store/apps/details?id=com.rydeenworks.mybooksearch")
                val i = Intent(Intent.ACTION_VIEW, uri)
                activity.startActivity(i)
            }
            .setNeutralButton("その他要望") { dialog: DialogInterface?, which: Int ->
                val uri =
                    Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSegGYDdqtw9gq8xZSb4yqORgFE5A4uQzR0RBrFDtLfsIDfs3g/viewform?usp=sf_link")
                val i = Intent(Intent.ACTION_VIEW, uri)
                activity.startActivity(i)
            }
            .setNegativeButton("また今度") { dialog: DialogInterface?, which: Int -> }
            .show()
    }

    fun showDialog(bookNum: Int)
    {
        if( appReviewFlag.isAppReviewed())
        {
            return
        }
        if (bookNum < 0 )
        {
            return
        }
        // 4冊検索するごとに表示する
        if(bookNum % 4 != 0) {
            return
        }
        showDialog()
    }
}