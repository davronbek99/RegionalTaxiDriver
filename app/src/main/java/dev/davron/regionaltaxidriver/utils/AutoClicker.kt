package dev.davron.regionaltaxidriver.utils

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.accessibility.AccessibilityManager
import android.widget.FrameLayout
import java.lang.Math.abs

class AutoClickerFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        AutoClickerDetector.recordEvent(event)
        // If you return true in this method touches will be blocked
        return false
    }

}

object AutoClickerDetector {

    fun isAccessibilityServiceEnabled(context: Context): Boolean {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices =
            am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        return enabledServices.isNotEmpty()
    }

    var isDetectingSimilarGestures = false
        private set

    private data class Touch(
        val x: Float,
        val y: Float,
        val time: Long
    ) {

        fun isSimilar(other: Touch): Boolean {
            return abs(this.x - other.x) < 1.0f
                    && abs(this.y - other.y) < 1.0f
        }

    }

    private data class Gesture(
        val start: Touch,
        val end: Touch
    ) {

        val duration: Long = end.time - start.time

        fun isSimilar(other: Gesture): Boolean {
            return this.start.isSimilar(other.start)
                    && this.end.isSimilar(other.end)
                    && abs(this.duration - other.duration) < 50
        }

    }

    private var gestureStart: Touch? = null
    private val recentGestures = ArrayList<Gesture>()

    fun recordEvent(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                gestureStart = Touch(event.rawX, event.rawY, System.currentTimeMillis())
            }

            MotionEvent.ACTION_UP -> {
                gestureStart?.let { gestureStart ->
                    val gestureEnd = Touch(event.rawX, event.rawY, System.currentTimeMillis())
                    recentGestures.add(Gesture(gestureStart, gestureEnd))
                    trimGestureHistory()
                    checkSimilarGestures()
                }
                gestureStart = null
            }
        }
    }

    private const val HISTORY_SIZE = 20

    private fun trimGestureHistory() {
        while (recentGestures.size > HISTORY_SIZE) {
            recentGestures.removeAt(0)
        }
    }

    private fun checkSimilarGestures() {
        recentGestures.forEachIndexed { i, searchGesture ->
            var similarCount = 0
            recentGestures.forEachIndexed { j, compareGesture ->
                if (i != j && searchGesture.isSimilar(compareGesture)) {
                    similarCount++
                }
            }
            if (similarCount > 2) {
                // There is no way user can physically perform almost exactly the same gesture
                // 3 times amongst 20 last gestures
                isDetectingSimilarGestures = true
                return
            }
        }
        isDetectingSimilarGestures = false
    }

}
