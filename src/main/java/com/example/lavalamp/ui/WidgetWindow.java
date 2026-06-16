package com.example.lavalamp.ui;

import com.example.lavalamp.core.AnimationManager;
import com.example.lavalamp.core.Blob;
import com.example.lavalamp.core.SimulationManager;
import com.example.lavalamp.input.MouseTracker;
import com.example.lavalamp.render.CanvasRenderer;
import com.example.lavalamp.services.ColorProvider;
import com.example.lavalamp.services.PreferencesService;
import com.example.lavalamp.services.TimeColorProvider;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Creates and manages a transparent, frameless JavaFX window for the widget.
 * Integrates the renderer, simulation manager, mouse tracker, and settings UI.
 * Supports dragging to reposition the window and mouse-based blob interaction.
 */
public class WidgetWindow {
    private Stage stage;
    private Scene scene;
    private Canvas canvas;
    private CanvasRenderer renderer;
    private SimulationManager simulationManager;
    private AnimationManager animationManager;
    private MouseTracker mouseTracker;
    private SettingsController settingsController;
    private ColorProvider colorProvider;
    private double windowWidth = 800;
    private double windowHeight = 600;
    private double dragStartX = 0;
    private double dragStartY = 0;
    private double stageStartX = 0;
    private double stageStartY = 0;
    private boolean draggingWidget = false;

    /**
     * Create the widget window with all necessary components.
     */
    public WidgetWindow() {
        initializeStage();
        initializeColorProvider();
        initializeScene();
        initializeRendering();
        initializeSimulation();
        initializeMouseTracker();
        initializeSettingsController();
        setupMouseHandling();
        connectAnimationLoop();
        loadPreferences();
    }

    /**
     * Initialize the color provider.
     */
    private void initializeColorProvider() {
        colorProvider = new TimeColorProvider();
    }

    /**
     * Initialize the stage (window).
     */
    private void initializeStage() {
        stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        
        // Load window dimensions from preferences
        windowWidth = PreferencesService.getWindowWidth();
        windowHeight = PreferencesService.getWindowHeight();
        stage.setWidth(windowWidth);
        stage.setHeight(windowHeight);
        stage.setX(PreferencesService.getWindowX());
        stage.setY(PreferencesService.getWindowY());
        
        stage.setTitle("Lava Lamp Mood Widget");
    }

    /**
     * Initialize the scene with a transparent background and canvas.
     */
    private void initializeScene() {
        canvas = new Canvas(windowWidth, windowHeight);
        
        // Create a BorderPane to hold canvas and optional settings panel
        BorderPane root = new BorderPane();
        root.setCenter(new StackPane(canvas));
        root.setStyle("-fx-background-color: transparent;");

        scene = new Scene(root, windowWidth, windowHeight);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        
        // Store reference to root for later settings panel insertion
        stage.setUserData(root);
    }

    /**
     * Initialize the rendering system.
     */
    private void initializeRendering() {
        renderer = new CanvasRenderer(canvas, colorProvider);
        renderer.setBackgroundColor(Color.web("#0a0a0a"));
        renderer.setGlowIntensity(1.2);
    }

    /**
     * Initialize the simulation system with initial blobs.
     */
    private void initializeSimulation() {
        int blobCount = PreferencesService.getBlobCount();
        simulationManager = new SimulationManager(blobCount, windowWidth, windowHeight);
        simulationManager.setColorProvider(colorProvider);
        simulationManager.setDamping(0.02);
        simulationManager.setGravity(20.0);
    }

    /**
     * Initialize the mouse tracker.
     */
    private void initializeMouseTracker() {
        mouseTracker = new MouseTracker();
        mouseTracker.setInteractionStrength(PreferencesService.getInteractionStrength());
        mouseTracker.setEnabled(PreferencesService.isMouseEnabled());
        
        String modeStr = PreferencesService.getInteractionMode();
        try {
            mouseTracker.setMode(MouseTracker.InteractionMode.valueOf(modeStr));
        } catch (IllegalArgumentException e) {
            mouseTracker.setMode(MouseTracker.InteractionMode.ATTRACT);
        }
    }

    /**
     * Initialize the settings controller UI.
     */
    private void initializeSettingsController() {
        settingsController = new SettingsController(simulationManager, renderer, mouseTracker);
        settingsController.setVisible(false); // Hidden by default
    }

    /**
     * Load preferences and apply to components.
     */
    private void loadPreferences() {
        boolean alwaysOnTop = PreferencesService.isAlwaysOnTop();
        stage.setAlwaysOnTop(alwaysOnTop);
        
        if (settingsController != null) {
            settingsController.setAlwaysOnTopSelected(alwaysOnTop);
        }
    }

    /**
     * Set up mouse event handling for dragging the window and tracking mouse position.
     */
    private void setupMouseHandling() {
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);
        canvas.setOnMouseMoved(this::handleMouseMoved);
    }

    /**
     * Handle mouse press events for starting a drag operation.
     */
    private void handleMousePressed(MouseEvent event) {
        dragStartX = event.getScreenX();
        dragStartY = event.getScreenY();
        stageStartX = stage.getX();
        stageStartY = stage.getY();
        draggingWidget = true;
        
        // Update mouse tracker position
        mouseTracker.updateFromMouseEvent(event);
    }

    /**
     * Handle mouse drag events for repositioning the window.
     */
    private void handleMouseDragged(MouseEvent event) {
        if (draggingWidget) {
            double deltaX = event.getScreenX() - dragStartX;
            double deltaY = event.getScreenY() - dragStartY;
            stage.setX(stageStartX + deltaX);
            stage.setY(stageStartY + deltaY);
        }
        
        // Update mouse tracker position for blob interaction
        mouseTracker.updateFromMouseEvent(event);
    }

    /**
     * Handle mouse release events.
     */
    private void handleMouseReleased(MouseEvent event) {
        draggingWidget = false;
        PreferencesService.setWindowX(stage.getX());
        PreferencesService.setWindowY(stage.getY());
    }

    /**
     * Handle mouse move events for continuous tracking.
     */
    private void handleMouseMoved(MouseEvent event) {
        mouseTracker.updateFromMouseEvent(event);
    }

    /**
     * Connect the animation loop to the simulation and rendering.
     * Apply mouse interaction forces to blobs each frame.
     */
    private void connectAnimationLoop() {
        animationManager = new AnimationManager(60.0);
        animationManager.setFrameUpdateListener(dt -> {
            // Apply mouse interaction forces to all blobs
            for (Blob blob : simulationManager.getBlobs()) {
                Point2D force = mouseTracker.calculateInteractionForce(blob.getX(), blob.getY());
                blob.applyForce(force.getX(), force.getY());
            }
            
            // Update simulation
            simulationManager.update(dt);

            // Render frame
            renderer.renderFrame(simulationManager.getBlobs());
        });
    }

    /**
     * Show the widget window and start the animation loop.
     */
    public void show() {
        stage.show();
        animationManager.start();
    }

    /**
     * Hide the widget and stop animation.
     */
    public void hide() {
        animationManager.stop();
        stage.hide();
    }

    /**
     * Close the widget (cleanup).
     */
    public void close() {
        // Save final preferences
        PreferencesService.setWindowX(stage.getX());
        PreferencesService.setWindowY(stage.getY());
        PreferencesService.setWindowWidth(stage.getWidth());
        PreferencesService.setWindowHeight(stage.getHeight());
        PreferencesService.setAlwaysOnTop(stage.isAlwaysOnTop());
        PreferencesService.flush();
        
        animationManager.stop();
        stage.close();
    }

    /**
     * Handle window resize events.
     */
    public void handleResize(double newWidth, double newHeight) {
        windowWidth = newWidth;
        windowHeight = newHeight;
        renderer.resize(newWidth, newHeight);
        simulationManager.setCanvasDimensions(newWidth, newHeight);
    }

    /**
     * Toggle settings panel visibility.
     */
    public void toggleSettingsPanel() {
        if (settingsController != null) {
            settingsController.setVisible(!settingsController.isVisible());
        }
    }

    /**
     * Set always-on-top and update preferences.
     */
    public void setAlwaysOnTop(boolean alwaysOnTop) {
        stage.setAlwaysOnTop(alwaysOnTop);
        PreferencesService.setAlwaysOnTop(alwaysOnTop);
    }

    // Getters
    public Stage getStage() { return stage; }
    public Scene getScene() { return scene; }
    public Canvas getCanvas() { return canvas; }
    public CanvasRenderer getRenderer() { return renderer; }
    public SimulationManager getSimulationManager() { return simulationManager; }
    public AnimationManager getAnimationManager() { return animationManager; }
    public MouseTracker getMouseTracker() { return mouseTracker; }
    public ColorProvider getColorProvider() { return colorProvider; }
    public SettingsController getSettingsController() { return settingsController; }
}
