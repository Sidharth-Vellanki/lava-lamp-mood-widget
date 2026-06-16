package com.example.lavalamp.input;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

/**
 * Tracks mouse cursor position and computes interaction strength for blobs.
 * Provides proximity-based attraction/repulsion effects.
 */
public class MouseTracker {
    private Point2D mousePosition = new Point2D(0, 0);
    private double interactionStrength = 1.0; // 0.0 to 2.0
    private double interactionRadius = 200.0; // pixels
    private InteractionMode mode = InteractionMode.ATTRACT;
    private boolean enabled = true;

    /**
     * Interaction mode for blob response to mouse proximity.
     */
    public enum InteractionMode {
        ATTRACT,    // Blobs move toward cursor
        REPEL,      // Blobs move away from cursor
        DISTORT     // Blobs deform toward cursor
    }

    /**
     * Update mouse position from a MouseEvent.
     *
     * @param event the mouse event
     */
    public void updateFromMouseEvent(MouseEvent event) {
        mousePosition = new Point2D(event.getX(), event.getY());
    }

    /**
     * Manually set the mouse position.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public void setMousePosition(double x, double y) {
        mousePosition = new Point2D(x, y);
    }

    /**
     * Calculate the interaction force applied to a blob at a given position.
     *
     * @param blobX blob x position
     * @param blobY blob y position
     * @return force vector [fx, fy]
     */
    public Point2D calculateInteractionForce(double blobX, double blobY) {
        if (!enabled) {
            return new Point2D(0, 0);
        }

        double dx = mousePosition.getX() - blobX;
        double dy = mousePosition.getY() - blobY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Only apply force if within interaction radius
        if (distance > interactionRadius || distance < 1.0) {
            return new Point2D(0, 0);
        }

        // Falloff: stronger at close range, weaker at edge
        double falloff = 1.0 - (distance / interactionRadius);
        double forceMagnitude = falloff * interactionStrength * 50.0; // Scale factor

        // Normalize direction
        double dirX = (distance > 0) ? dx / distance : 0;
        double dirY = (distance > 0) ? dy / distance : 0;

        if (mode == InteractionMode.REPEL) {
            dirX = -dirX;
            dirY = -dirY;
        }

        return new Point2D(dirX * forceMagnitude, dirY * forceMagnitude);
    }

    /**
     * Get the proximity strength (0.0 to 1.0) for a blob at a given position.
     *
     * @param blobX blob x position
     * @param blobY blob y position
     * @return proximity strength
     */
    public double getProximityStrength(double blobX, double blobY) {
        if (!enabled) {
            return 0.0;
        }

        double dx = mousePosition.getX() - blobX;
        double dy = mousePosition.getY() - blobY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > interactionRadius) {
            return 0.0;
        }

        return 1.0 - (distance / interactionRadius);
    }

    // Getters and setters
    public Point2D getMousePosition() { return mousePosition; }

    public double getInteractionStrength() { return interactionStrength; }
    public void setInteractionStrength(double strength) {
        this.interactionStrength = Math.max(0, Math.min(2.0, strength));
    }

    public double getInteractionRadius() { return interactionRadius; }
    public void setInteractionRadius(double radius) {
        this.interactionRadius = Math.max(10, radius);
    }

    public InteractionMode getMode() { return mode; }
    public void setMode(InteractionMode mode) { this.mode = mode; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
