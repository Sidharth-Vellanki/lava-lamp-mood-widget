package com.example.lavalamp.ui;

import com.example.lavalamp.core.SimulationManager;
import com.example.lavalamp.input.MouseTracker;
import com.example.lavalamp.render.CanvasRenderer;
import com.example.lavalamp.services.PreferencesService;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Settings UI controller for real-time parameter adjustment.
 * Provides sliders and toggles for blob count, animation speed, glow, interaction, etc.
 * Settings update immediately and are persisted to preferences.
 */
public class SettingsController {
    private VBox settingsPanel;
    private SimulationManager simulationManager;
    private CanvasRenderer renderer;
    private MouseTracker mouseTracker;

    private Slider blobCountSlider;
    private Slider animationSpeedSlider;
    private Slider interactionStrengthSlider;
    private Slider glowIntensitySlider;
    private CheckBox alwaysOnTopCheckBox;
    private CheckBox mouseEnabledCheckBox;
    private ComboBox<String> interactionModeCombo;

    /**
     * Create a settings controller for the given components.
     *
     * @param simulationManager simulation manager to control
     * @param renderer canvas renderer to control
     * @param mouseTracker mouse tracker to control
     */
    public SettingsController(SimulationManager simulationManager, CanvasRenderer renderer, MouseTracker mouseTracker) {
        this.simulationManager = simulationManager;
        this.renderer = renderer;
        this.mouseTracker = mouseTracker;
        buildSettingsPanel();
        loadSettings();
    }

    /**
     * Build the settings panel UI procedurally.
     */
    private void buildSettingsPanel() {
        settingsPanel = new VBox(8);
        settingsPanel.setPadding(new Insets(12));
        settingsPanel.setStyle("-fx-border-color: #333333; -fx-border-width: 1; -fx-background-color: #1a1a1a;");

        // Blob Count Slider
        settingsPanel.getChildren().add(new Label("Blob Count:"));
        blobCountSlider = new Slider(1, 50, 10);
        blobCountSlider.setShowTickLabels(true);
        blobCountSlider.setShowTickMarks(true);
        blobCountSlider.setMajorTickUnit(10);
        blobCountSlider.setMinorTickCount(4);
        blobCountSlider.setSnapToTicks(true);
        blobCountSlider.setPrefWidth(200);
        blobCountSlider.valueProperty().addListener((obs, old, newVal) -> {
            simulationManager.setBlobCount((int) newVal.doubleValue());
            PreferencesService.setBlobCount((int) newVal.doubleValue());
        });
        settingsPanel.getChildren().add(blobCountSlider);

        // Animation Speed Slider
        settingsPanel.getChildren().add(new Label("Animation Speed:"));
        animationSpeedSlider = new Slider(0.1, 3.0, 1.0);
        animationSpeedSlider.setShowTickLabels(true);
        animationSpeedSlider.setShowTickMarks(true);
        animationSpeedSlider.setMajorTickUnit(0.5);
        animationSpeedSlider.setMinorTickCount(4);
        animationSpeedSlider.setPrefWidth(200);
        animationSpeedSlider.valueProperty().addListener((obs, old, newVal) -> {
            // Animation speed affects gravity and damping
            double speed = newVal.doubleValue();
            simulationManager.setGravity(20.0 * speed);
            PreferencesService.setAnimationSpeed(speed);
        });
        settingsPanel.getChildren().add(animationSpeedSlider);

        // Interaction Strength Slider
        settingsPanel.getChildren().add(new Label("Interaction Strength:"));
        interactionStrengthSlider = new Slider(0, 2.0, 1.0);
        interactionStrengthSlider.setShowTickLabels(true);
        interactionStrengthSlider.setShowTickMarks(true);
        interactionStrengthSlider.setMajorTickUnit(0.5);
        interactionStrengthSlider.setMinorTickCount(4);
        interactionStrengthSlider.setPrefWidth(200);
        interactionStrengthSlider.valueProperty().addListener((obs, old, newVal) -> {
            mouseTracker.setInteractionStrength(newVal.doubleValue());
            PreferencesService.setInteractionStrength(newVal.doubleValue());
        });
        settingsPanel.getChildren().add(interactionStrengthSlider);

        // Glow Intensity Slider
        settingsPanel.getChildren().add(new Label("Glow Intensity:"));
        glowIntensitySlider = new Slider(0, 2.0, 1.2);
        glowIntensitySlider.setShowTickLabels(true);
        glowIntensitySlider.setShowTickMarks(true);
        glowIntensitySlider.setMajorTickUnit(0.5);
        glowIntensitySlider.setMinorTickCount(4);
        glowIntensitySlider.setPrefWidth(200);
        glowIntensitySlider.valueProperty().addListener((obs, old, newVal) -> {
            renderer.setGlowIntensity(newVal.doubleValue());
            PreferencesService.setGlowIntensity(newVal.doubleValue());
        });
        settingsPanel.getChildren().add(glowIntensitySlider);

        // Interaction Mode ComboBox
        settingsPanel.getChildren().add(new Label("Interaction Mode:"));
        interactionModeCombo = new ComboBox<>();
        interactionModeCombo.getItems().addAll("ATTRACT", "REPEL", "DISTORT");
        interactionModeCombo.setValue("ATTRACT");
        interactionModeCombo.setOnAction(event -> {
            String mode = interactionModeCombo.getValue();
            try {
                mouseTracker.setMode(MouseTracker.InteractionMode.valueOf(mode));
                PreferencesService.setInteractionMode(mode);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid interaction mode: " + mode);
            }
        });
        settingsPanel.getChildren().add(interactionModeCombo);

        // Mouse Enabled Checkbox
        mouseEnabledCheckBox = new CheckBox("Enable Mouse Interaction");
        mouseEnabledCheckBox.setSelected(true);
        mouseEnabledCheckBox.selectedProperty().addListener((obs, old, newVal) -> {
            mouseTracker.setEnabled(newVal);
            PreferencesService.setMouseEnabled(newVal);
        });
        settingsPanel.getChildren().add(mouseEnabledCheckBox);

        // Always On Top Checkbox
        alwaysOnTopCheckBox = new CheckBox("Always On Top");
        alwaysOnTopCheckBox.setSelected(false);
        alwaysOnTopCheckBox.selectedProperty().addListener((obs, old, newVal) -> {
            PreferencesService.setAlwaysOnTop(newVal);
            // This will be applied by WidgetWindow
        });
        settingsPanel.getChildren().add(alwaysOnTopCheckBox);

        // Apply dark styling to controls
        applyDarkStyling();
    }

    /**
     * Load settings from preferences and update controls.
     */
    private void loadSettings() {
        blobCountSlider.setValue(PreferencesService.getBlobCount());
        animationSpeedSlider.setValue(PreferencesService.getAnimationSpeed());
        interactionStrengthSlider.setValue(PreferencesService.getInteractionStrength());
        glowIntensitySlider.setValue(PreferencesService.getGlowIntensity());
        alwaysOnTopCheckBox.setSelected(PreferencesService.isAlwaysOnTop());
        mouseEnabledCheckBox.setSelected(PreferencesService.isMouseEnabled());

        String mode = PreferencesService.getInteractionMode();
        interactionModeCombo.setValue(mode);
    }

    /**
     * Apply dark theme styling to all controls.
     */
    private void applyDarkStyling() {
        String darkStyle = "-fx-text-fill: #ffffff; -fx-control-inner-background: #2a2a2a;";
        settingsPanel.setStyle(settingsPanel.getStyle() + darkStyle);
    }

    /**
     * Get the settings panel for adding to a container.
     *
     * @return the VBox settings panel
     */
    public VBox getSettingsPanel() {
        return settingsPanel;
    }

    /**
     * Show or hide the settings panel (toggle visibility).
     *
     * @param visible true to show, false to hide
     */
    public void setVisible(boolean visible) {
        settingsPanel.setVisible(visible);
        settingsPanel.setManaged(visible);
    }

    /**
     * Check if settings panel is visible.
     *
     * @return true if visible
     */
    public boolean isVisible() {
        return settingsPanel.isVisible();
    }

    /**
     * Get the always-on-top checkbox state.
     *
     * @return true if checked
     */
    public boolean isAlwaysOnTopSelected() {
        return alwaysOnTopCheckBox.isSelected();
    }

    /**
     * Set the always-on-top checkbox without triggering listener.
     *
     * @param selected true to check
     */
    public void setAlwaysOnTopSelected(boolean selected) {
        alwaysOnTopCheckBox.setSelected(selected);
    }
}
