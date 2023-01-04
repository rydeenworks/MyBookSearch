package com.rydeenworks.mybooksearch.ui.historypage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.URL

class SetBookImageView(
    private val imageView: ImageView
){

    fun handle(url: String)
    {
        GlobalScope.launch {
            val image: Bitmap
//        val defaultImage = BitmapFactory.decodeStream(resources.assets.open("image/image.png"))
            try {
                val imageUrl = URL(url)
                val imageIs: InputStream = imageUrl.openStream()
                image = BitmapFactory.decodeStream(imageIs)
                withContext(Dispatchers.Main) {
                    imageView.setImageBitmap(image)
                }
            }  catch (e: IOException) {
                //画像が取れない時用
            }
        }
    }
}