package com.example.lavalamp.render;

import com.example.lavalamp.core.Blob;
import com.example.lavalamp.services.ColorProvider;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import java.util.List;

/**
 * Renders blobs to a Canvas using radial gradients for smooth, glowing effects.
 * Handles canvas resizing, frame-by-frame drawing, and dynamic color support.
 */
public class CanvasRenderer implements Renderer {
    private Canvas canvas;
    private GraphicsContext gc;
    private Color backgroundColor;
    private double glowIntensity = 1.0;
    private ColorProvider colorProvider = null;

    /**
     * Create a canvas renderer for the given canvas.
     *
     * @param canvas the Canvas to render to
     */
    public CanvasRenderer(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.backgroundColor = Color.web("#0a0a0a"); // Dark background
    }

    /**
     * Create a canvas renderer with a color provider.
     *
     * @param canvas the Canvas to render to
     * @param colorProvider the color provider for dynamic colors
     */
    public CanvasRenderer(Canvas canvas, ColorProvider colorProvider) {
        this(canvas);
        this.colorProvider = colorProvider;
    }

    /**
     * Render a frame of blobs.
     *
     * @param blobs list of blobs to render
     */
    @Override
    public void renderFrame(List<Blob> blobs) {
        // Get current background color (from provider or default)
        Color bgColor = (colorProvider != null) ? colorProvider.getBackgroundColor() : backgroundColor;
        
        // Clear canvas with background color
        gc.setFill(bgColor);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw each blob
        for (Blob blob : blobs) {
            drawBlob(blob);
        }
    }

    /**
     * Draw a single blob with a radial gradient and glow effect.
     *
     * @param blob the blob to draw
     */
    private void drawBlob(Blob blob) {
        double x = blob.getX();
        double y = blob.getY();
        double radius = blob.getRadius();
        Color color = blob.getColor();

        // Draw outer glow (soft shadow pass)
        drawGlowPass(x, y, radius, color);

        // Draw main blob with radial gradient
        drawMainBlob(x, y, radius, color);
    }

    /**
     * Draw the glow pass (outer ring with transparency).
     */
    private void drawGlowPass(double x, double y, double radius, Color color) {
        double glowRadius = radius * 1.5;
        Color glowColor = Color.color(
            color.getRed(),
            color.getGreen(),
            color.getBlue(),
            0.1 * glowIntensity
        );

        gc.setFill(glowColor);
        gc.fillOval(x - glowRadius, y - glowRadius, glowRadius * 2, glowRadius * 2);
    }

    /**
     * Draw the main blob body with a radial gradient.
     */
    private void drawMainBlob(double x, double y, double radius, Color color) {
        // Create radial gradient from bright center to darker edges
        Stop[] stops = new Stop[]{
            new Stop(0.0, color.brighter()),
            new Stop(0.6, color),
            new Stop(1.0, Color.color(color.getRed(), color.getGreen(), color.getBlue(), 0.3))
        };

        RadialGradient gradient = new RadialGradient(
            0, 0, // focal point at center
            x, y, // gradient center
            radius, // radius
            false, // proportional
            null, // cycleMethod
            stops
        );

        gc.setFill(gradient);
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);

        // Optional: draw a subtle outline
        gc.setStroke(color);
        gc.setLineWidth(1.0);
        gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    /**
     * Resize the canvas (should be called when widget window resizes).
     *
     * @param width new canvas width
     * @param height new canvas height
     */
    @Override
    public void resize(double width, double height) {
        canvas.setWidth(width);
        canvas.setHeight(height);
    }

    /**
     * Set the background color.
     *
     * @param color the background color
     */
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    /**
     * Set the glow intensity factor.
     *
     * @param intensity glow intensity (0.0 to 2.0)
     */
    public void setGlowIntensity(double intensity) {
        this.glowIntensity = Math.max(0, Math.min(2.0, intensity));
    }

    public Canvas getCanvas() { return canvas; }
    public double getGlowIntensity() { return glowIntensity; }
    public Color getBackgroundColor() { return backgroundColor; }
    public ColorProvider getColorProvider() { return colorProvider; }
    public void setColorProvider(ColorProvider provider) { this.colorProvider = provider; }
}
