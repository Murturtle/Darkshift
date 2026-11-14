package com.reo.darkshift

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PixelFormat
import android.service.quicksettings.TileService
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent

/** Draws the full-screen dimming/tint overlay. It only needs to exist to be allowed to add one. */
class DarkshiftService : AccessibilityService(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var settings: DarkshiftSettings
    private var overlay: View? = null

    override fun onServiceConnected() {
        settings = DarkshiftSettings(this)
        settings.registerListener(this)
        isRunning = true
        refresh()
        requestTileRefresh()
    }

    override fun onSharedPreferenceChanged(prefs: SharedPreferences?, key: String?) = refresh()

    private fun refresh() {
        val config = settings.config
        if (!config.enabled) {
            removeOverlay()
            return
        }
        val view = overlay ?: addOverlay()
        view.setBackgroundColor(OverlayColor.argb(config))
    }

    private fun addOverlay(): View {
        val view = View(this)
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT,
        ).apply {
            layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
            fitInsetsTypes = 0
            title = "Darkshift"
        }
        getSystemService(WindowManager::class.java).addView(view, params)
        overlay = view
        return view
    }

    private fun removeOverlay() {
        val view = overlay ?: return
        overlay = null
        getSystemService(WindowManager::class.java).removeView(view)
    }

    private fun requestTileRefresh() =
        TileService.requestListeningState(this, ComponentName(this, DarkshiftTileService::class.java))

    override fun onUnbind(intent: Intent?): Boolean {
        shutDown()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        shutDown()
        super.onDestroy()
    }

    private fun shutDown() {
        if (!isRunning) return
        isRunning = false
        settings.unregisterListener(this)
        removeOverlay()
        requestTileRefresh()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) = Unit

    override fun onInterrupt() = Unit

    companion object {
        /** True while the user has the accessibility service switched on. */
        @Volatile
        var isRunning = false
            private set
    }
}
