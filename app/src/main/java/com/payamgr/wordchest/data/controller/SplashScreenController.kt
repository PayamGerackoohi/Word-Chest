package com.payamgr.wordchest.data.controller

/**
 * It controls how long the splash screen is kept and its timing data.
 * @property delay The animation start delay in ms
 * @property duration The animation duration in ms
 * @property totalTime The total animation time in ms (delay and duration).
 * @property keep Whether the splashscreen should be kept or not.
 */
interface SplashScreenController {
    val delay: Long
    val duration: Long
    val totalTime: Long
    val keep: Boolean

    /**
     * It hides the splashscreen.
     */
    fun hide()
}
