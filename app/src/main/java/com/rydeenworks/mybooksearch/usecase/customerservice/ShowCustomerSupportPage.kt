package com.rydeenworks.mybooksearch.usecase.customerservice

import android.app.Activity
import android.content.Intent
import android.net.Uri

class ShowCustomerSupportPage {
    fun handle(activity: Activity)
    {
        val uri =
            Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSegGYDdqtw9gq8xZSb4yqORgFE5A4uQzR0RBrFDtLfsIDfs3g/viewform?usp=sf_link")
        val i = Intent(Intent.ACTION_VIEW, uri)
        activity.startActivity(i)
    }
}