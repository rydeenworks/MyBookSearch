package com.rydeenworks.mybooksearch.ui.customerservice

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import com.rydeenworks.mybooksearch.domain.customerservice.AppReviewFlag
import com.rydeenworks.mybooksearch.usecase.customerservice.ShowCustomerSupportPage
import com.rydeenworks.mybooksearch.usecase.customerservice.ShowGooglePlayPage

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
                ShowGooglePlayPage().handle(activity)
            }
            .setNeutralButton("その他要望") { dialog: DialogInterface?, which: Int ->
                ShowCustomerSupportPage().handle(activity)
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