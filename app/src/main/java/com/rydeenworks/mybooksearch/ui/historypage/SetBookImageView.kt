package com.rydeenworks.mybooksearch.ui.historypage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import java.io.IOException
import java.io.InputStream
import java.net.URL

class SetBookImageView(
    private val imageView: ImageView
) : AsyncTask<String, Void, Bitmap>(){

    override fun doInBackground(vararg params: String): Bitmap? {
        val image: Bitmap
//        val defaultImage = BitmapFactory.decodeStream(resources.assets.open("image/image.png"))
        try {
            val imageUrl = URL(params[0])
            val imageIs: InputStream
            imageIs = imageUrl.openStream()
            image = BitmapFactory.decodeStream(imageIs)
            return image
        }  catch (e: IOException) {
            //画像が取れない時用
//            return defaultImage
            return null
        }
    }
    override fun onPostExecute(result: Bitmap) {
        if(result == null)
        {
            return
        }
        // 取得した画像をImageViewに設定します。
        imageView.setImageBitmap(result)
    }
}