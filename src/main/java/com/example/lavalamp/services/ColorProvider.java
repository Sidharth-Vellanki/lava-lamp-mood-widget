package com.example.lavalamp.services;

import javafx.scene.paint.Color;

/**
 * Interface for color provision to the widget.
 * Implementations can provide static colors or dynamic time-based colors.
 */
public interface ColorProvider {
    /**
     * Get the background color for the current state.
     *
     * @return background color
     */
    Color getBackgroundColor();

    /**
     * Get a primary blob color for the current state.
     *
     * @return primary color
     */
    Color getPrimaryColor();

    /**
     * Get a secondary blob color for the current state.
     *
     * @return secondary color
     */
    Color getSecondaryColor();

    /**
     * Get a tertiary blob color for the current state.
     *
     * @return tertiary color
     */
    Color getTertiaryColor();

    /**
     * Get an array of all available colors for the current palette.
     *
     * @return array of colors
     */
    Color[] getPaletteColors();

    /**
     * Get the current palette name.
     *
     * @return palette name
     */
    String getCurrentPalette();
}
