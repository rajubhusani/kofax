package com.fab.kofax.common

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import com.fab.kofax.R
import com.fab.kofax.utils.ConstValues
import com.kofax.kmc.ken.engines.data.BoundingRect
import com.kofax.kmc.ken.engines.data.Image
import com.kofax.kmc.ken.engines.data.QuickAnalysisFeedback
import com.kofax.kmc.kut.utilities.error.KmcException
import java.util.*

object CommonUtils {
    @JvmStatic
    fun formatAnalysisMessage(ctx: Context, imageQuickAnalysisFeedBack: QuickAnalysisFeedback?, isLowContrastEnabled: Boolean): String {
        var txt = ctx.getString(R.string.THE_IMAGE_IS) + " "
        val feedbackList: ArrayList<String>
        if (imageQuickAnalysisFeedBack != null) {
            feedbackList = ArrayList()
            if (imageQuickAnalysisFeedBack.isBlurry) {
                feedbackList.add(ctx.getString(R.string.BLURRED))
            }
            if (imageQuickAnalysisFeedBack.isOversaturated) {
                feedbackList.add(ctx.getString(R.string.OVER_SATURATED))
            }
            if (imageQuickAnalysisFeedBack.isUndersaturated) {
                feedbackList.add(ctx.getString(R.string.UNDER_SATURATED))
            }
            if (imageQuickAnalysisFeedBack.isShadowed) {
                feedbackList.add(ctx.getString(R.string.SHADOWED))
            }
            if (imageQuickAnalysisFeedBack.isGlareDetected) {
                feedbackList.add(ctx.getString(R.string.GLARE_DETECTED))
            }
            if (imageQuickAnalysisFeedBack.isMissingBorders) {
                feedbackList.add(ctx.getString(R.string.MISSING_BORDERS))
            }
            if (imageQuickAnalysisFeedBack.isOverlySkewed) {
                feedbackList.add(ctx.getString(R.string.OVERLY_SKEWED))
            }
            if (isLowContrastEnabled && imageQuickAnalysisFeedBack.isLowContrastBackground) {
                feedbackList.add(ctx.getString(R.string.LOW_CONTRAST_BACKGROUND_DETECTED))
            }
            if (feedbackList.size > 0) {
                if (feedbackList.size == 1) {
                    txt += feedbackList[0]
                } else {
                    for (i in feedbackList.indices) {
                        txt += if (i > 0 && i == feedbackList.size - 1) {
                            " " + ctx.getString(R.string.and) + " " + feedbackList[i]
                        } else if (i == 0) {
                            feedbackList[i]
                        } else {
                            ctx.getString(R.string.commo) + " " + feedbackList[i]
                        }
                    }
                }
            }
        }
        if (!txt.contentEquals(ctx.getString(R.string.THE_IMAGE_IS) + " ")) {
            txt += "."
        }
        return txt
    }

    fun loadBoundingRectArray(width: Int, height: Int): Array<BoundingRect?>? {
        // Log.e("TEST", "Enter :: loadBoundingRectArray  " +
        // ConstValues.mAppObject.mPositionCoordinates.length);
        if (null == ConstValues.mAppObject!!.mPositionCoordinates) {
            return null
        }
        val count: Int? = ConstValues.mAppObject!!.mPositionCoordinates?.size
        // allocate rect with number of rectangles to be displayed
        val bRectCollection = arrayOfNulls<BoundingRect>(count!!)

        // populate rect array
        var rowCount = 0

        // calculate image to device scaling factor
        // float scaleFactor =
        // getBitmapScalingFactor(mDisplayedImage.getImageBitmap());
        while (rowCount < count) {
            if (ConstValues.mAppObject!!.mPositionCoordinates!![rowCount][0] == -1) {
                break
            }
            val scalingLeft = ConstValues.mAppObject!!.mPositionCoordinates!![rowCount][0]
            val scalingTop = ConstValues.mAppObject!!.mPositionCoordinates!![rowCount][1]
            var scalingWidth = ConstValues.mAppObject!!.mPositionCoordinates!![rowCount][2]
            var scalingHeight = ConstValues.mAppObject!!.mPositionCoordinates!![rowCount][3]
            Log.e("TEST", "ConstValues.mAppObject.mPositionCoordinates[ :: "
                    + rowCount + "] "
                    + ConstValues.mAppObject!!.mPositionCoordinates!![rowCount][0])
            Log.e("TEST", "ConstValues.mAppObject.mPositionCoordinates[ :: "
                    + rowCount + "] "
                    + ConstValues.mAppObject!!.mPositionCoordinates!![rowCount][1])
            Log.e("TEST", "ConstValues.mAppObject.mPositionCoordinates[ :: "
                    + rowCount + "] "
                    + ConstValues.mAppObject!!.mPositionCoordinates!![rowCount][2])
            Log.e("TEST", "ConstValues.mAppObject.mPositionCoordinates[ :: "
                    + rowCount + "] "
                    + ConstValues.mAppObject!!.mPositionCoordinates!![rowCount][3])
            Log.e("TEST",
                    "=============================================================================")
            if (scalingLeft >= 0 && scalingLeft <= width
                    && scalingTop >= 0 && scalingTop <= height) {
                if (scalingLeft + scalingWidth > width) {
                    scalingWidth = width - scalingLeft
                }
                if (scalingTop + scalingHeight > height) {
                    scalingHeight = height - scalingTop
                }
            }

            // create new BoundingRectangle object
            val tempRect = BoundingRect(scalingLeft, scalingTop,
                    scalingLeft + scalingWidth, scalingTop + scalingHeight)
            if (!isDuplicateRectangle(bRectCollection, tempRect)) {
                bRectCollection[rowCount] = BoundingRect(scalingLeft,
                        scalingTop, scalingLeft + scalingWidth,  // right
                        scalingTop + scalingHeight) // bottom
                Log.e("TEST", "bRect[rowCount] LEFT "
                        + bRectCollection[rowCount]!!.rectLeft)
                Log.e("TEST", "bRect[rowCount] TOP "
                        + bRectCollection[rowCount]!!.rectTop)
                Log.e("TEST", "bRect[rowCount] RIGHT "
                        + bRectCollection[rowCount]!!.rectRight)
                Log.e("TEST", "bRect[rowCount] BOTTOM "
                        + bRectCollection[rowCount]!!.rectBottom)
                Log.e("TEST",
                        "=============================================================================")
            }
            rowCount++
        }
        return bRectCollection
    }

    private fun isDuplicateRectangle(
            boundingRectCollection: Array<BoundingRect?>, boundingRect: BoundingRect?): Boolean {
        var duplicate = false
        for (bRect in boundingRectCollection) {
            if (null != bRect && null != boundingRect) {
                if (bRect.rectLeft == boundingRect.rectLeft && bRect.rectRight == boundingRect.rectRight && bRect.rectTop == boundingRect.rectTop && bRect.rectBottom == boundingRect
                                .rectBottom) {
                    duplicate = true
                    break
                }
            }
        }
        return duplicate
    }

    fun unbindDrawables(ctx: Context?, view: View?) {
        if (view != null) {
            if (view.background != null) {
                view.background.callback = null
                view.setBackgroundResource(0)
            }
            if (view is ImageView) {
                view.setImageBitmap(null)
            } else if (view is ViewGroup) {
                val viewGroup = view
                for (i in 0 until viewGroup.childCount) {
                    unbindDrawables(ctx, viewGroup.getChildAt(i))
                }
                if (view !is AdapterView<*>) {
                    viewGroup.removeAllViews()
                }
            }
        }
    }

    /**
     * Clear the bitmap and file buffer in the Image object.
     *
     * @param image
     */
    @JvmStatic
    fun cleanupImageObj(image: Image?) {
        if (null != image) {
            try {
                image.imageClearBitmap()
                image.imageClearFileBuffer()
            } catch (ke: KmcException) {
                ke.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}