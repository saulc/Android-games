package com.acme.games.math

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView

class MathHelper {

    private var img : ImageView? = null
    private var tex : TextView? = null

    private var width: Int = 0
    private var height:Int = 0

    private var listener : MathFragment

    private var centerX = 0.0
    private var centerY = 0.0
    private var zoom = 1.0

    constructor(Callback: MathFragment, iv: ImageView?, tv: TextView?, w: Int, h: Int){
        listener = Callback
        img = iv
        tex = tv
        width = w
        height = h

    }

    fun zoom(normX: Float, normY: Float) {
        val scale = 4.0 / (width * zoom)
        val bitmapX = normX * width
        val bitmapY = normY * height
        
        centerX += (bitmapX - width / 2.0) * scale
        centerY += (bitmapY - height / 2.0) * scale
        zoom *= 2.0
        drawGrid()
    }

    //scaling problems... switching to floating buttons.
    fun drawGrid() {
        val rc = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        
        Thread {
            doMath(rc)
            img?.post {
                img?.setImageBitmap(rc)
            }
        }.start()
    }


    fun doMath(bitmap: Bitmap){
        val max = 100
        val colors = IntArray(max)
        for (i in 0 until max) {
            colors[i] = Color.rgb((i * 5) % 255, (i * 10) % 255, (i * 15) % 255)
        }

        val scale = 4.0 / (width * zoom)
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val c_re = (x - width / 2.0) * scale + centerX
                val c_im = (y - height / 2.0) * scale + centerY
                var zx = 0.0
                var zy = 0.0
                var iteration = 0
                while (zx * zx + zy * zy < 4 && iteration < max) {
                    val xtemp = zx * zx - zy * zy + c_re
                    zy = 2 * zx * zy + c_im
                    zx = xtemp
                    iteration++
                }

                pixels[y * width + x] = if (iteration < max) colors[iteration] else Color.BLACK
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    }
}
