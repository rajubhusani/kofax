package com.fab.kofax.uicontrols

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.kofax.kmc.ken.engines.data.BoundingTetragon

class PageDetectionOverlay : View {
    private var paint: Paint? = null
    private var lastUpdate: Long = 0
    var bound: BoundingTetragon? = null
    var previewWidth = 0
    var previewHeight = 0

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        paint = Paint()
        paint!!.strokeWidth = 5f
        paint!!.color = -0xff0100
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        parent.bringChildToFront(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val curTime = System.currentTimeMillis()
        if (curTime - lastUpdate > 500) return
        val curDelta = curTime - lastUpdate
        var alpha = 1f
        if (curDelta > 250) {
            // If last update was more than 250ms ago
            // scale alpha value
            // curDelta will be at least 250 (by 'if' statement)
            // curDelta will grow from there (as long as lastUpdate doesn't
            // grow)
            // at 250ms, alpha == 1
            // at >250ms, alpha drops
            // when 500 - curDelta would be 0, the previous if statement
            // returns; no drawing
            alpha = (500 - curDelta) / 250f
        }
        if (bound == null) return
        paint!!.color = (alpha * 255).toInt() shl 24 or Color.RED
        canvas.save()
        canvas.scale(width.toFloat() / previewWidth, height.toFloat() / previewHeight)
        val p0x = bound!!.bottomLeft.x.toFloat()
        val p0y = bound!!.bottomLeft.y.toFloat()
        val p1x = bound!!.topLeft.x.toFloat()
        val p1y = bound!!.topLeft.y.toFloat()
        val p2x = bound!!.topRight.x.toFloat()
        val p2y = bound!!.topRight.y.toFloat()
        val p3x = bound!!.bottomRight.x.toFloat()
        val p3y = bound!!.bottomRight.y.toFloat()
        canvas.drawLine(p0x, p0y, p1x, p1y, paint!!)
        canvas.drawLine(p1x, p1y, p2x, p2y, paint!!)
        canvas.drawLine(p2x, p2y, p3x, p3y, paint!!)
        canvas.drawLine(p3x, p3y, p0x, p0y, paint!!)
        canvas.restore()
    }

    fun update() {
        lastUpdate = System.currentTimeMillis()
    }
}