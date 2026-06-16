package com.example.lavalamp.core;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * Represents a single animated blob in the lava lamp widget.
 * Stores position, velocity, radius, color, and animation properties.
 */
public class Blob {
    private Point2D position;
    private Point2D velocity;
    private double radius;
    private Color color;
    private double targetRadius;
    private Color targetColor;
    private double wobblePhase;
    private double wobbleSpeed;

    /**
     * Create a blob at the given position.
     *
     * @param x            initial x position
     * @param y            initial y position
     * @param vx           initial x velocity
     * @param vy           initial y velocity
     * @param radius       initial radius
     * @param color        initial color
     */
    public Blob(double x, double y, double vx, double vy, double radius, Color color) {
        this.position = new Point2D(x, y);
        this.velocity = new Point2D(vx, vy);
        this.radius = radius;
        this.targetRadius = radius;
        this.color = color;
        this.targetColor = color;
        this.wobblePhase = Math.random() * Math.PI * 2;
        this.wobbleSpeed = 2.0 + Math.random() * 1.0; // 2-3 rad/s
    }

    /**
     * Update blob position and animation state.
     *
     * @param dt delta time in seconds
     * @param width canvas width (for wrapping)
     * @param height canvas height (for wrapping)
     */
    public void update(double dt, double width, double height) {
        // Update position
        double newX = position.getX() + velocity.getX() * dt;
        double newY = position.getY() + velocity.getY() * dt;

        // Wrap around edges
        if (newX < -radius) newX += width + 2 * radius;
        if (newX > width + radius) newX -= width + 2 * radius;
        if (newY < -radius) newY += height + 2 * radius;
        if (newY > height + radius) newY -= height + 2 * radius;

        position = new Point2D(newX, newY);

        // Update wobble phase for radius animation
        wobblePhase += wobbleSpeed * dt;

        // Smoothly interpolate radius toward target
        double wobble = Math.sin(wobblePhase) * 0.1 * radius; // ±10% radius variation
        double animatedRadius = targetRadius * (1.0 + wobble * 0.5);
        radius = radius * 0.95 + animatedRadius * 0.05; // Smooth lerp

        // Smoothly interpolate color toward target
        color = interpolateColor(color, targetColor, 0.05);
    }

    /**
     * Apply an acceleration force to this blob.
     *
     * @param accelX x component of acceleration
     * @param accelY y component of acceleration
     */
    public void applyForce(double accelX, double accelY) {
        velocity = velocity.add(accelX, accelY);
    }

    /**
     * Apply damping to velocity (friction).
     *
     * @param damping friction coefficient (0.0 to 1.0)
     */
    public void applyDamping(double damping) {
        velocity = velocity.multiply(1.0 - damping);
    }

    /**
     * Linearly interpolate between two colors.
     */
    private Color interpolateColor(Color from, Color to, double t) {
        return Color.color(
            from.getRed() * (1 - t) + to.getRed() * t,
            from.getGreen() * (1 - t) + to.getGreen() * t,
            from.getBlue() * (1 - t) + to.getBlue() * t,
            from.getOpacity() * (1 - t) + to.getOpacity() * t
        );
    }

    // Getters and setters
    public Point2D getPosition() { return position; }
    public void setPosition(Point2D pos) { this.position = pos; }

    public Point2D getVelocity() { return velocity; }
    public void setVelocity(Point2D vel) { this.velocity = vel; }

    public double getRadius() { return radius; }
    public void setRadius(double r) { this.radius = r; }

    public double getTargetRadius() { return targetRadius; }
    public void setTargetRadius(double r) { this.targetRadius = r; }

    public Color getColor() { return color; }
    public void setColor(Color c) { this.color = c; }

    public Color getTargetColor() { return targetColor; }
    public void setTargetColor(Color c) { this.targetColor = c; }

    public double getX() { return position.getX(); }
    public double getY() { return position.getY(); }
}
