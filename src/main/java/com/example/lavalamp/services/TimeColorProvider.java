package com.example.lavalamp.services;

import javafx.scene.paint.Color;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides time-of-day based color palettes with smooth transitions.
 * Automatically transitions between Dawn, Day, Sunset, and Night palettes.
 */
public class TimeColorProvider implements ColorProvider {
    private PaletteManager paletteManager;
    private boolean enabled = true;

    public TimeColorProvider() {
        this.paletteManager = new PaletteManager();
    }

    @Override
    public Color getBackgroundColor() {
        String palette = getCurrentPalette();
        return paletteManager.getPaletteBackground(palette);
    }

    @Override
    public Color getPrimaryColor() {
        String palette = getCurrentPalette();
        Color[] colors = paletteManager.getPaletteColors(palette);
        return colors.length > 0 ? colors[0] : Color.PINK;
    }

    @Override
    public Color getSecondaryColor() {
        String palette = getCurrentPalette();
        Color[] colors = paletteManager.getPaletteColors(palette);
        return colors.length > 1 ? colors[1] : Color.CYAN;
    }

    @Override
    public Color getTertiaryColor() {
        String palette = getCurrentPalette();
        Color[] colors = paletteManager.getPaletteColors(palette);
        return colors.length > 2 ? colors[2] : Color.GOLD;
    }

    @Override
    public Color[] getPaletteColors() {
        String palette = getCurrentPalette();
        return paletteManager.getPaletteColors(palette);
    }

    @Override
    public String getCurrentPalette() {
        if (!enabled) {
            return PreferencesService.getTheme();
        }

        // Determine palette based on current time
        LocalTime now = LocalTime.now();
        int hour = now.getHour();

        if (hour >= 5 && hour < 9) {
            return "dawn";
        } else if (hour >= 9 && hour < 17) {
            return "day";
        } else if (hour >= 17 && hour < 21) {
            return "sunset";
        } else {
            return "night";
        }
    }

    /**
     * Get the name of the current time period.
     *
     * @return time period name
     */
    public String getCurrentTimePeriod() {
        return getCurrentPalette();
    }

    /**
     * Set whether time-based transitions are enabled.
     *
     * @param enabled true to enable time-based transitions
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public PaletteManager getPaletteManager() {
        return paletteManager;
    }
}
