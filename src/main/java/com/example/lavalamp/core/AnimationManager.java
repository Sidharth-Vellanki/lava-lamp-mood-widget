package com.example.lavalamp.core;

import javafx.animation.AnimationTimer;

/**
 * Manages the main animation loop using JavaFX AnimationTimer.
 * Provides delta-time calculations and notifies listeners each frame.
 */
public class AnimationManager {
    private AnimationTimer timer;
    private double targetFps = 60.0;
    private long lastFrameTime = 0;
    private double frameTimeTarget;
    private FrameUpdateListener listener;
    private boolean running = false;

    /**
     * Create an animation manager with a target FPS.
     *
     * @param targetFps target frames per second
     */
    public AnimationManager(double targetFps) {
        this.targetFps = targetFps;
        this.frameTimeTarget = 1.0 / targetFps * 1_000_000_000; // nanoseconds
        initializeTimer();
    }

    /**
     * Initialize the AnimationTimer.
     */
    private void initializeTimer() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastFrameTime == 0) {
                    lastFrameTime = now;
                    return;
                }

                long elapsedNs = now - lastFrameTime;
                double dt = elapsedNs / 1_000_000_000.0; // Convert to seconds

                // Cap delta time to prevent huge jumps (e.g., if window is dragged)
                dt = Math.min(dt, 0.05); // Max 50ms per frame

                if (listener != null) {
                    listener.onFrameUpdate(dt);
                }

                lastFrameTime = now;
            }
        };
    }

    /**
     * Start the animation loop.
     */
    public void start() {
        if (!running) {
            lastFrameTime = 0;
            timer.start();
            running = true;
        }
    }

    /**
     * Stop the animation loop.
     */
    public void stop() {
        if (running) {
            timer.stop();
            running = false;
            lastFrameTime = 0;
        }
    }

    /**
     * Set the target frames per second.
     *
     * @param fps target FPS
     */
    public void setTargetFps(double fps) {
        this.targetFps = fps;
        this.frameTimeTarget = 1.0 / fps * 1_000_000_000;
    }

    /**
     * Register a listener to be notified each frame.
     *
     * @param listener the listener callback
     */
    public void setFrameUpdateListener(FrameUpdateListener listener) {
        this.listener = listener;
    }

    public boolean isRunning() {
        return running;
    }

    /**
     * Listener interface for frame updates.
     */
    @FunctionalInterface
    public interface FrameUpdateListener {
        /**
         * Called each frame with the delta time.
         *
         * @param deltaTime time elapsed since last frame, in seconds
         */
        void onFrameUpdate(double deltaTime);
    }
}
