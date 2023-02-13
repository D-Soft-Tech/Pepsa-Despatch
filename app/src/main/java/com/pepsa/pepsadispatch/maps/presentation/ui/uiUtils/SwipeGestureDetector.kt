package com.pepsa.pepsadispatch.maps.presentation.ui.uiUtils

import android.view.GestureDetector
import android.view.MotionEvent
import com.pepsa.pepsadispatch.shared.domain.interactors.SwipeGestureCallBacks
import kotlin.math.abs

const val SWIPE_THRESHOLD = 100
const val SWIPE_VELOCITY_THRESHOLD = 100

class AppSwipeGestureDetector(private val swipeGestureCallBacks: SwipeGestureCallBacks) :
    GestureDetector.SimpleOnGestureListener() {
    override fun onFling(
        downEvent: MotionEvent,
        moveEvent: MotionEvent,
        velocityX: Float,
        velocityY: Float,
    ): Boolean {
        val diffX = moveEvent.x.minus(downEvent.x)
        val diffY = moveEvent.y.minus(downEvent.y)

        return if (abs(diffX) > abs(diffY)) { // This is a left or right swipe
            if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    swipeGestureCallBacks.swipeRight()
                } else {
                    swipeGestureCallBacks.swipeLeft()
                }
                true
            } else {
                super.onFling(downEvent, moveEvent, velocityX, velocityY)
            }
        } else { // This is bottom or up swipe
            if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    swipeGestureCallBacks.swipeDown()
                } else {
                    swipeGestureCallBacks.swipeUp()
                }
                true
            } else {
                super.onFling(downEvent, moveEvent, velocityX, velocityY)
            }
        }
    }
}
