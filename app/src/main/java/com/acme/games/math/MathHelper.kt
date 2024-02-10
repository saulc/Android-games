package com.acme.games.math

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.ImageView
import android.widget.TextView

class MathHelper {

    private var img : ImageView? = null
    private var tex : TextView? = null

    private var width: Int = 0
    private var height:Int = 0

    private var listener : MathFragment

    constructor(Callback: MathFragment, iv: ImageView?, tv: TextView?, w: Int, h: Int){
        listener = Callback
        img = iv
        tex = tv
        width = w
        height = h

    }

    fun getPaint(): Paint {
        var p = Paint()
        //  p.setColor(Color.TRANSPARENT);
        p.strokeWidth = 3.0f
        p.color = Color.GREEN
        p.textSize = 44f
        return p
    }

    //scaling problems... switching to floating buttons.
    fun drawGrid(): Bitmap {

        val rc = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val cc = Canvas(rc)
        val p = getPaint()

        //black background
        cc.drawARGB(0, 0, 0, 0)


        doMath(cc);
                p.color = Color.CYAN
        cc.drawPoint(width/2f, height/2f, p)


        img?.setImageBitmap(rc)
        return rc
    }


    fun doMath(canvas : Canvas){
        val max = 100
        val p = getPaint()

        val black = 0
        val colors = IntArray(max)
        for (i in 0 until max) {
//            colors[i] = Color.HSBtoRGB(i / 256f, 1, i / (i + 8f))

            colors[i] = Color.rgb(i / 256f, 1f, i / (i + 8f))
        }

        for (row in 0 until height) {
            for (col in 0 until width) {
                val c_re = (col - width / 2) * 4.0 / width
                val c_im = (row - height / 2) * 4.0 / width
                var x = 0.0
                var y = 0.0
                var r2: Double
                var iteration = 0
                val step = 2
                while (Math.pow(x, step.toDouble()) + Math.pow(
                        y,
                        step.toDouble()
                    ) < 4 && iteration < max
                ) {
                    //  double x_new = x*x-y*y+c_re;
                    val x_new = Math.pow(x, step.toDouble()) - Math.pow(y, step.toDouble()) + c_re
                    y = 2 * x * y + c_im
                    x = x_new
                    iteration++
                }

                if (iteration < max)  p.color = colors[iteration]
                else p.color = Color.BLACK
                    canvas.drawPoint(row+0f,col +0f,  p)

            }
        }
    }
}