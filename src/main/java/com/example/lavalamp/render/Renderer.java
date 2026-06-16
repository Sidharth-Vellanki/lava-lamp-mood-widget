package com.example.lavalamp.render;

import com.example.lavalamp.core.Blob;
import java.util.List;

/**
 * Interface for rendering blobs to a display surface.
 */
public interface Renderer {
    /**
     * Render a frame with the given list of blobs.
     *
     * @param blobs list of blobs to render
     */
    void renderFrame(List<Blob> blobs);

    /**
     * Resize the renderer to new dimensions.
     *
     * @param width new width
     * @param height new height
     */
    void resize(double width, double height);
}
