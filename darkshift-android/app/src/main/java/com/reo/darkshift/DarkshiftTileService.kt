package com.reo.darkshift

import android.app.PendingIntent
import android.content.Intent
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class DarkshiftTileService : TileService() {
    override fun onStartListening() = refresh()

    override fun onClick() {
        if (!DarkshiftService.isRunning) {
            // Nothing to toggle until the accessibility service is on; the app explains how.
            val intent = Intent(this, MainActivity::class.java)
            startActivityAndCollapse(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE))
            return
        }
        DarkshiftSettings(this).update { it.copy(enabled = !it.enabled) }
        refresh()
    }

    private fun refresh() {
        val tile = qsTile ?: return
        val on = DarkshiftService.isRunning && DarkshiftSettings(this).config.enabled
        tile.state = if (on) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.subtitle = getString(
            when {
                !DarkshiftService.isRunning -> R.string.tile_needs_service
                on -> R.string.tile_on
                else -> R.string.tile_off
            }
        )
        tile.updateTile()
    }
}
