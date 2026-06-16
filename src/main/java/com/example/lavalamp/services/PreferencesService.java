package com.example.lavalamp.services;

import java.util.prefs.Preferences;

/**
 * Manages persistence of user preferences using Java Preferences API.
 * Stores settings like blob count, always-on-top, window position, etc.
 */
public class PreferencesService {
    private static final Preferences prefs = Preferences.userNodeForPackage(PreferencesService.class);

    // Preference keys
    private static final String KEY_BLOB_COUNT = "blob.count";
    private static final String KEY_ANIMATION_SPEED = "animation.speed";
    private static final String KEY_INTERACTION_STRENGTH = "interaction.strength";
    private static final String KEY_GLOW_INTENSITY = "glow.intensity";
    private static final String KEY_ALWAYS_ON_TOP = "window.alwaysOnTop";
    private static final String KEY_WINDOW_X = "window.x";
    private static final String KEY_WINDOW_Y = "window.y";
    private static final String KEY_WINDOW_WIDTH = "window.width";
    private static final String KEY_WINDOW_HEIGHT = "window.height";
    private static final String KEY_THEME = "theme.current";
    private static final String KEY_INTERACTION_MODE = "interaction.mode";
    private static final String KEY_MOUSE_ENABLED = "mouse.enabled";

    /**
     * Get blob count setting.
     *
     * @return blob count (default 10)
     */
    public static int getBlobCount() {
        return prefs.getInt(KEY_BLOB_COUNT, 10);
    }

    /**
     * Set blob count setting.
     *
     * @param count blob count
     */
    public static void setBlobCount(int count) {
        prefs.putInt(KEY_BLOB_COUNT, Math.max(1, Math.min(100, count)));
    }

    /**
     * Get animation speed multiplier.
     *
     * @return speed multiplier (default 1.0)
     */
    public static double getAnimationSpeed() {
        return prefs.getDouble(KEY_ANIMATION_SPEED, 1.0);
    }

    /**
     * Set animation speed multiplier.
     *
     * @param speed speed multiplier
     */
    public static void setAnimationSpeed(double speed) {
        prefs.putDouble(KEY_ANIMATION_SPEED, Math.max(0.1, Math.min(3.0, speed)));
    }

    /**
     * Get interaction strength setting.
     *
     * @return interaction strength (default 1.0)
     */
    public static double getInteractionStrength() {
        return prefs.getDouble(KEY_INTERACTION_STRENGTH, 1.0);
    }

    /**
     * Set interaction strength setting.
     *
     * @param strength interaction strength
     */
    public static void setInteractionStrength(double strength) {
        prefs.putDouble(KEY_INTERACTION_STRENGTH, Math.max(0, Math.min(2.0, strength)));
    }

    /**
     * Get glow intensity setting.
     *
     * @return glow intensity (default 1.2)
     */
    public static double getGlowIntensity() {
        return prefs.getDouble(KEY_GLOW_INTENSITY, 1.2);
    }

    /**
     * Set glow intensity setting.
     *
     * @param intensity glow intensity
     */
    public static void setGlowIntensity(double intensity) {
        prefs.putDouble(KEY_GLOW_INTENSITY, Math.max(0, Math.min(2.0, intensity)));
    }

    /**
     * Get always-on-top setting.
     *
     * @return true if always on top (default false)
     */
    public static boolean isAlwaysOnTop() {
        return prefs.getBoolean(KEY_ALWAYS_ON_TOP, false);
    }

    /**
     * Set always-on-top setting.
     *
     * @param alwaysOnTop always on top flag
     */
    public static void setAlwaysOnTop(boolean alwaysOnTop) {
        prefs.putBoolean(KEY_ALWAYS_ON_TOP, alwaysOnTop);
    }

    /**
     * Get last window X position.
     *
     * @return x position (default 100)
     */
    public static double getWindowX() {
        return prefs.getDouble(KEY_WINDOW_X, 100);
    }

    /**
     * Set window X position.
     *
     * @param x x position
     */
    public static void setWindowX(double x) {
        prefs.putDouble(KEY_WINDOW_X, x);
    }

    /**
     * Get last window Y position.
     *
     * @return y position (default 100)
     */
    public static double getWindowY() {
        return prefs.getDouble(KEY_WINDOW_Y, 100);
    }

    /**
     * Set window Y position.
     *
     * @param y y position
     */
    public static void setWindowY(double y) {
        prefs.putDouble(KEY_WINDOW_Y, y);
    }

    /**
     * Get last window width.
     *
     * @return width (default 800)
     */
    public static double getWindowWidth() {
        return prefs.getDouble(KEY_WINDOW_WIDTH, 800);
    }

    /**
     * Set window width.
     *
     * @param width width
     */
    public static void setWindowWidth(double width) {
        prefs.putDouble(KEY_WINDOW_WIDTH, Math.max(400, width));
    }

    /**
     * Get last window height.
     *
     * @return height (default 600)
     */
    public static double getWindowHeight() {
        return prefs.getDouble(KEY_WINDOW_HEIGHT, 600);
    }

    /**
     * Set window height.
     *
     * @param height height
     */
    public static void setWindowHeight(double height) {
        prefs.putDouble(KEY_WINDOW_HEIGHT, Math.max(300, height));
    }

    /**
     * Get current theme/palette name.
     *
     * @return theme name (default "day")
     */
    public static String getTheme() {
        return prefs.get(KEY_THEME, "day");
    }

    /**
     * Set current theme/palette name.
     *
     * @param theme theme name
     */
    public static void setTheme(String theme) {
        prefs.put(KEY_THEME, theme);
    }

    /**
     * Get interaction mode.
     *
     * @return interaction mode (default "ATTRACT")
     */
    public static String getInteractionMode() {
        return prefs.get(KEY_INTERACTION_MODE, "ATTRACT");
    }

    /**
     * Set interaction mode.
     *
     * @param mode interaction mode
     */
    public static void setInteractionMode(String mode) {
        prefs.put(KEY_INTERACTION_MODE, mode);
    }

    /**
     * Get mouse interaction enabled setting.
     *
     * @return true if enabled (default true)
     */
    public static boolean isMouseEnabled() {
        return prefs.getBoolean(KEY_MOUSE_ENABLED, true);
    }

    /**
     * Set mouse interaction enabled setting.
     *
     * @param enabled enabled flag
     */
    public static void setMouseEnabled(boolean enabled) {
        prefs.putBoolean(KEY_MOUSE_ENABLED, enabled);
    }

    /**
     * Clear all preferences (for reset).
     */
    public static void clearAll() {
        try {
            prefs.clear();
        } catch (Exception e) {
            System.err.println("Failed to clear preferences: " + e.getMessage());
        }
    }

    /**
     * Flush all changes to persistent storage.
     */
    public static void flush() {
        try {
            prefs.flush();
        } catch (Exception e) {
            System.err.println("Failed to flush preferences: " + e.getMessage());
        }
    }
}
