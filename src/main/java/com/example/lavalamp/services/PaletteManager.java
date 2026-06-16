package com.example.lavalamp.services;

import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages color palettes for the lava lamp widget.
 * Includes built-in palettes (Dawn, Day, Sunset, Night) and supports custom palettes.
 */
public class PaletteManager {
    private static class Palette {
        String name;
        Color background;
        Color[] colors;

        Palette(String name, Color background, Color... colors) {
            this.name = name;
            this.background = background;
            this.colors = colors;
        }
    }

    private Map<String, Palette> palettes = new HashMap<>();
    private String currentPalette = "day";

    public PaletteManager() {
        initializeBuiltInPalettes();
    }

    /**
     * Initialize the built-in color palettes.
     */
    private void initializeBuiltInPalettes() {
        // Dawn palette: soft oranges and pinks
        addPalette("dawn",
            Color.web("#0a0505"),  // dark background
            Color.web("#FF9A56"),  // soft orange
            Color.web("#FFB8A3"),  // peach
            Color.web("#FFD4A3"),  // warm gold
            Color.web("#FF8C42")   // burnt orange
        );

        // Day palette: bright blues and cyans
        addPalette("day",
            Color.web("#0a0a0a"),  // near black background
            Color.web("#00CED1"),  // cyan
            Color.web("#1E90FF"),  // dodger blue
            Color.web("#87CEEB"),  // sky blue
            Color.web("#00B8E6")   // bright cyan
        );

        // Sunset palette: vibrant oranges, purples, reds
        addPalette("sunset",
            Color.web("#0a0505"),  // dark background
            Color.web("#FF6B35"),  // bright orange
            Color.web("#D62828"),  // deep red
            Color.web("#A23B72"),  // purple
            Color.web("#F18F01")   // orange
        );

        // Night palette: deep purples and indigos
        addPalette("night",
            Color.web("#050510"),  // very dark purple background
            Color.web("#7851A9"),  // purple
            Color.web("#4B0082"),  // indigo
            Color.web("#2E1A47"),  // deep purple
            Color.web("#6A4C93")   // lighter purple
        );

        // Load saved theme from preferences
        currentPalette = PreferencesService.getTheme();
        if (!palettes.containsKey(currentPalette)) {
            currentPalette = "day";
        }
    }

    /**
     * Add a custom palette.
     *
     * @param name palette name
     * @param background background color
     * @param colors blob colors
     */
    public void addPalette(String name, Color background, Color... colors) {
        if (colors.length == 0) {
            throw new IllegalArgumentException("Palette must have at least one color");
        }
        palettes.put(name.toLowerCase(), new Palette(name, background, colors));
    }

    /**
     * Get the background color for a palette.
     *
     * @param paletteName palette name
     * @return background color
     */
    public Color getPaletteBackground(String paletteName) {
        Palette p = palettes.get(paletteName.toLowerCase());
        return p != null ? p.background : Color.BLACK;
    }

    /**
     * Get all colors for a palette.
     *
     * @param paletteName palette name
     * @return array of colors
     */
    public Color[] getPaletteColors(String paletteName) {
        Palette p = palettes.get(paletteName.toLowerCase());
        if (p != null) {
            Color[] copy = new Color[p.colors.length];
            System.arraycopy(p.colors, 0, copy, 0, p.colors.length);
            return copy;
        }
        return new Color[]{Color.PINK, Color.CYAN, Color.GOLD};
    }

    /**
     * Get a specific color from a palette by index.
     *
     * @param paletteName palette name
     * @param colorIndex color index
     * @return color at index, or first color if out of bounds
     */
    public Color getPaletteColor(String paletteName, int colorIndex) {
        Color[] colors = getPaletteColors(paletteName);
        if (colorIndex >= 0 && colorIndex < colors.length) {
            return colors[colorIndex];
        }
        return colors[0];
    }

    /**
     * Get the current palette name.
     *
     * @return current palette name
     */
    public String getCurrentPalette() {
        return currentPalette;
    }

    /**
     * Set the current palette.
     *
     * @param paletteName palette name
     */
    public void setCurrentPalette(String paletteName) {
        String lower = paletteName.toLowerCase();
        if (palettes.containsKey(lower)) {
            currentPalette = lower;
            PreferencesService.setTheme(lower);
        }
    }

    /**
     * Get all available palette names.
     *
     * @return array of palette names
     */
    public String[] getAvailablePalettes() {
        return palettes.keySet().stream()
            .sorted()
            .toArray(String[]::new);
    }

    /**
     * Check if a palette exists.
     *
     * @param paletteName palette name
     * @return true if exists
     */
    public boolean hasPalette(String paletteName) {
        return palettes.containsKey(paletteName.toLowerCase());
    }
}
