package com.rydeenworks.mybooksearch.ui.historypage.listview

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import com.rydeenworks.mybooksearch.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

class SetBookImageView(
    private val imageView: ImageView
){

    fun handle(url: String)
    {
        imageView.visibility = View.INVISIBLE
        GlobalScope.launch {
            try {
                val imageUrl = URL(url)
                val imageIs = imageUrl.openStream()
                val image = BitmapFactory.decodeStream(imageIs)
                withContext(Dispatchers.Main) {
                    if(image == null)
                    {
                        imageView.setImageResource(R.drawable.noimage128x128)
                    } else {
                        imageView.setImageBitmap(image)
                    }
                    imageView.visibility = View.VISIBLE
                }
            }  catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    imageView.setImageResource(R.drawable.noimage128x128)
                }
            }
        }
    }
}