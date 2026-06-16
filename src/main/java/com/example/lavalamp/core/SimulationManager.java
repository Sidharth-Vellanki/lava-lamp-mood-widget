package com.example.lavalamp.core;

import com.example.lavalamp.services.ColorProvider;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages blob simulation, updating positions, velocities, and forces.
 * Maintains the active blob collection and provides render state.
 */
public class SimulationManager {
    private List<Blob> blobs;
    private double canvasWidth = 800;
    private double canvasHeight = 600;
    private double damping = 0.02; // Air resistance
    private double gravity = 20.0; // Slight downward drift
    private ColorProvider colorProvider = null;

    /**
     * Create a simulation manager with optional initial blob count.
     *
     * @param initialBlobCount number of blobs to create
     * @param width canvas width
     * @param height canvas height
     */
    public SimulationManager(int initialBlobCount, double width, double height) {
        this.blobs = new ArrayList<>();
        this.canvasWidth = width;
        this.canvasHeight = height;
        createBlobs(initialBlobCount);
    }

    /**
     * Create initial blobs with random properties.
     * Uses colors from ColorProvider if available, otherwise uses default palette.
     *
     * @param count number of blobs to create
     */
    private void createBlobs(int count) {
        Color[] palette;
        
        if (colorProvider != null) {
            palette = colorProvider.getPaletteColors();
        } else {
            palette = new Color[]{
                Color.web("#FF69B4"),
                Color.web("#00CED1"),
                Color.web("#FFD700"),
                Color.web("#FF6347"),
                Color.web("#9370DB")
            };
        }

        for (int i = 0; i < count; i++) {
            double x = Math.random() * canvasWidth;
            double y = Math.random() * canvasHeight;
            double vx = (Math.random() - 0.5) * 100; // -50 to 50 pixels/sec
            double vy = (Math.random() - 0.5) * 80 - 10; // Slight downward bias
            double radius = 30 + Math.random() * 40; // 30 to 70 pixels
            Color color = palette[(int) (Math.random() * palette.length)];

            blobs.add(new Blob(x, y, vx, vy, radius, color));
        }
    }

    /**
     * Update all blobs for the given delta time.
     *
     * @param dt delta time in seconds
     */
    public void update(double dt) {
        for (Blob blob : blobs) {
            // Apply gravity
            blob.applyForce(0, gravity);

            // Apply damping
            blob.applyDamping(damping);

            // Update position and animation
            blob.update(dt, canvasWidth, canvasHeight);
        }
    }

    /**
     * Add a blob to the simulation.
     *
     * @param blob the blob to add
     */
    public void addBlob(Blob blob) {
        blobs.add(blob);
    }

    /**
     * Remove a blob from the simulation.
     *
     * @param blob the blob to remove
     */
    public void removeBlob(Blob blob) {
        blobs.remove(blob);
    }

    /**
     * Clear all blobs.
     */
    public void clearBlobs() {
        blobs.clear();
    }

    /**
     * Set the number of active blobs (resizes the list).
     *
     * @param count desired blob count
     */
    public void setBlobCount(int count) {
        if (count < blobs.size()) {
            blobs.subList(count, blobs.size()).clear();
        } else if (count > blobs.size()) {
            int toAdd = count - blobs.size();
            createBlobs(toAdd);
        }
    }

    /**
     * Get immutable list of blobs for rendering.
     *
     * @return list of blobs
     */
    public List<Blob> getBlobs() {
        return new ArrayList<>(blobs);
    }

    /**
     * Get current blob count.
     *
     * @return number of blobs
     */
    public int getBlobCount() {
        return blobs.size();
    }

    /**
     * Set canvas dimensions (for wrapping).
     *
     * @param width canvas width
     * @param height canvas height
     */
    public void setCanvasDimensions(double width, double height) {
        this.canvasWidth = width;
        this.canvasHeight = height;
    }

    /**
     * Set damping (friction) coefficient.
     *
     * @param damping value from 0.0 to 1.0
     */
    public void setDamping(double damping) {
        this.damping = Math.max(0, Math.min(1.0, damping));
    }

    /**
     * Set gravity effect.
     *
     * @param gravity gravitational acceleration
     */
    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public double getDamping() { return damping; }
    public double getGravity() { return gravity; }
    public ColorProvider getColorProvider() { return colorProvider; }
    
    /**
     * Set the color provider for dynamic blob colors.
     *
     * @param provider color provider
     */
    public void setColorProvider(ColorProvider provider) {
        this.colorProvider = provider;
    }
}
