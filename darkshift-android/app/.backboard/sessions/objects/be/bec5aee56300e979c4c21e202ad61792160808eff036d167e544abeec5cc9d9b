package com.reo.darkshift

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OverlayColorTest {
    private fun alpha(argb: Int) = argb ushr 24
    private fun red(argb: Int) = (argb shr 16) and 0xFF
    private fun green(argb: Int) = (argb shr 8) and 0xFF
    private fun blue(argb: Int) = argb and 0xFF

    @Test
    fun greenIsPureGreen() {
        val argb = OverlayColor.argb(DarkshiftConfig(red = false))
        assertEquals(255, green(argb))
        assertEquals(0, red(argb))
        assertEquals(0, blue(argb))
    }

    @Test
    fun redIsPureRed() {
        val argb = OverlayColor.argb(DarkshiftConfig(red = true))
        assertEquals(255, red(argb))
        assertEquals(0, green(argb))
        assertEquals(0, blue(argb))
    }

    @Test
    fun intensityIsTheOverlayOpacity() {
        val argb = OverlayColor.argb(DarkshiftConfig(intensity = 0.5f))
        assertEquals(0.5f, alpha(argb) / 255f, 0.01f)
    }

    @Test
    fun intensityStartsAtTwentyTwoPercent() {
        assertEquals(0.22f, DarkshiftConfig().intensity, 1e-6f)
    }

    @Test
    fun higherIntensityIsStronger() {
        val weak = alpha(OverlayColor.argb(DarkshiftConfig(intensity = 0.2f)))
        val strong = alpha(OverlayColor.argb(DarkshiftConfig(intensity = 0.8f)))
        assertTrue(strong > weak)
    }

    @Test
    fun intensityStaysWithinTheSliderRangeAndNeverFullyOpaque() {
        val low = alpha(OverlayColor.argb(DarkshiftConfig(intensity = -1f)))
        val high = alpha(OverlayColor.argb(DarkshiftConfig(intensity = 2f)))
        assertEquals(OverlayColor.MIN_INTENSITY, low / 255f, 0.01f)
        assertEquals(OverlayColor.MAX_INTENSITY, high / 255f, 0.01f)
        assertTrue(high < 255)
    }
}
