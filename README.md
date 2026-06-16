# Lava Lamp Mood Widget

A modern, frameless, transparent JavaFX desktop widget featuring smoothly animated lava-lamp-style blobs. The widget displays organic, morphing blob particles that drift across your screen with colors that transition based on time of day.

## Features

### Milestone 1: Baseline Rendering & Window ✓
- **Transparent, frameless window** – Modern desktop widget aesthetic
- **Smooth animation** – 60 FPS rendering loop with Canvas + AnimationTimer
- **Animated blobs** – ~10 blobs with gravity, damping, and smooth motion
- **Radial gradient rendering** – Glowing blob effects with soft edges
- **Drag to reposition** – Click and drag anywhere to move the widget
- **Performance optimized** – Single Canvas, minimal Scene Graph

### Milestone 2: Interaction & Controls ✓
- **Mouse proximity effects** – Blobs respond to cursor (attract/repel/distort modes)
- **Real-time settings UI** – Sliders for blob count, speed, glow, interaction strength
- **Persistent preferences** – Settings saved between sessions (Java Preferences API)
- **Always-on-top toggle** – Optional window layering
- **Mouse interaction modes** – Three modes: ATTRACT, REPEL, DISTORT
- **Smooth parameter changes** – Live updates without restarting

## Requirements

- **Java**: Zulu JDK 25 with JavaFX components installed
- **Maven**: 3.9.16 or later
- **OS**: Windows (or any OS with Zulu JDK 25 + JavaFX)

## Build & Run

### Build
```bash
mvn clean compile
```

### Run (Development)
```bash
mvn javafx:run
```

### Package (Optional)
```bash
mvn clean package
```

## Milestone Status

### Milestone 1: Complete ✓
**Classes**: MainApp, WidgetWindow, CanvasRenderer, AnimationManager, Blob, SimulationManager
- Transparent, frameless window with dragging support
- Canvas-based rendering at 60 FPS with radial gradients
- 10 animated blobs with gravity and damping physics
- Delta-time animation loop with AnimationTimer

### Milestone 2: Complete ✓
**Classes**: MouseTracker, SettingsController, PreferencesService
- Mouse proximity interaction (three modes: ATTRACT, REPEL, DISTORT)
- Real-time settings UI with sliders and toggles (blob count, speed, glow, interaction)
- Persistent preferences using java.util.prefs
- Always-on-top window support
- Window position and size persistence

## Project Structure

```
src/
├── main/
│   ├── java/com/example/lavalamp/
│   │   ├── MainApp.java                 # Entry point
│   │   ├── ui/
│   │   │   ├── WidgetWindow.java        # Transparent stage & scene + mouse/settings integration
│   │   │   └── SettingsController.java  # Settings UI (sliders, toggles) - M2
│   │   ├── core/
│   │   │   ├── Blob.java                # Blob data & movement
│   │   │   ├── SimulationManager.java   # Blob collection & physics
│   │   │   └── AnimationManager.java    # AnimationTimer loop
│   │   ├── render/
│   │   │   ├── Renderer.java            # Rendering interface
│   │   │   └── CanvasRenderer.java      # Canvas rendering with gradients
│   │   ├── input/
│   │   │   └── MouseTracker.java        # Mouse proximity tracking - M2
│   │   ├── services/
│   │   │   └── PreferencesService.java  # Preferences persistence - M2
│   │   ├── effects/                     # For M4: trails, glow, particles
│   │   ├── util/                        # Utility functions
│   │   └── [remaining packages for M3-M5]
│   └── resources/
│       ├── styles/styles.css
│       └── config/default.properties
└── pom.xml                               # Maven build configuration
```

## Next Milestones

- **Milestone 3**: Dynamic color & themes (PaletteManager, TimeColorProvider, ColorProvider)
- **Milestone 4**: Visual effects (VisualEffectsManager, Particle, GlowPass, trails)
- **Milestone 5**: Audio support & polish (AudioManager, final performance tuning)

## Implementation Notes

- **No FXML**: All UI created procedurally in Java code
- **Single Canvas**: Optimal performance with minimal Scene Graph overhead
- **Delta-time updates**: Framerate-independent smooth animation
- **Preferences**: All settings persisted to java.util.prefs between sessions
- **Mouse interaction**: Three modes (ATTRACT, REPEL, DISTORT) with configurable strength
- **Settings UI**: Real-time sliders and toggles for all parameters
- **Platform**: Developed for Windows with Zulu JDK 25 + JavaFX 25.0.1

## Development Setup

This project builds with Maven 3.9.16. Ensure you have Zulu JDK 25 installed with JavaFX components.

```bash
# Verify build
mvn clean compile

# Run development version
mvn javafx:run

# Create executable JAR
mvn clean package
```

## License

Internal project.

