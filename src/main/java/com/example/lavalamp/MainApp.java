package com.example.lavalamp;

import com.example.lavalamp.ui.WidgetWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main JavaFX Application entry point for the Lava Lamp Mood Widget.
 * Initializes the widget window and manages the application lifecycle.
 */
public class MainApp extends Application {
    private WidgetWindow widgetWindow;

    @Override
    public void start(Stage primaryStage) {
        // Create and show the widget window
        widgetWindow = new WidgetWindow();
        widgetWindow.show();
    }

    @Override
    public void stop() {
        // Clean up resources
        if (widgetWindow != null) {
            widgetWindow.close();
        }
    }

    /**
     * Launch the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
