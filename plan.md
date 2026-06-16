Project Overview
**Project Name**: Lava Lamp Mood Widget
**Goal**: Build a modern, frameless, transparent JavaFX desktop widget that shows smoothly animated lava-lamp-style blobs and particles.

Core experience:
- Continuous, organic blob/particle motion that morphs and merges.
- Colors evolve with time-of-day and respond subtly to mouse proximity.
- Real-time controls for blob count, speed, palettes, transparency, glow, and interaction.
- Optional ambient, computer-generated audio tones tied to palette or motion.

Platform & Tooling Notes
- Target platform: Windows (note: development machine has Zulu JDK 25 with JavaFX components installed).
- Build tool: Maven 3.9.16 (assume local JDK with JavaFX — no need to bundle JavaFX runtime in pom).
- UI approach: Procedural JavaFX only (do NOT use FXML).
- Rendering: JavaFX `Canvas` + `AnimationTimer` for a single high-performance render loop.

Architecture Overview (Modular)
**Presentation**: `MainApp`, `WidgetWindow`, `SettingsController` — window, stage, system integration, settings UI.
**Rendering**: `Renderer` (interface), `CanvasRenderer` — single Canvas, layered drawing, glow & blending.
**Simulation**: `AnimationManager`, `SimulationManager`, `Blob`, `Particle` — update loop and state.
**Effects**: `VisualEffectsManager` — trails, soft blur, additive glow passes.
**Color & Themes**: `PaletteManager`, `TimeColorProvider`, `ColorProvider` — palettes, interpolation.
**Input & Interaction**: `MouseTracker`, drag helpers — proximity, attraction/repulsion, widget drag-to-move.
**Settings & Persistence**: `SettingsController`, `PreferencesService` — real-time controls, `java.util.prefs` storage.
**Audio**: `AudioManager` — procedural tone/ambience generator using `javax.sound.sampled`.

Class Responsibilities (summary)
- `MainApp`: JavaFX `Application` entry; create `WidgetWindow` and lifecycle hooks.
- `WidgetWindow`: Frameless transparent `Stage` and `Scene`; attaches `CanvasRenderer`; supports dragging and optional always-on-top.
- `CanvasRenderer`: Owns a single `Canvas`; handles resize, double-buffering patterns, draws blobs and particles; provides hooks for glow passes.
- `AnimationManager`: Wraps `AnimationTimer`; computes delta time, target FPS throttling, notifies `SimulationManager` and `CanvasRenderer` each frame.
- `SimulationManager`: Maintains `Blob` and `Particle` collections; integrates movement, collisions, merging rules, and emits renderables.
- `Blob`: Position, velocity, radius, color, soft-edge mask parameters, merging/morph state.
- `Particle`: Lightweight decorative element for trails and micro-movement.
- `VisualEffectsManager`: Adds trails, screen-space blur or bloom approximations (render to intermediate canvas, composite back).
- `ColorProvider` / `TimeColorProvider`: Return active colors for blobs and background based on system time; smooth interpolation between palettes.
- `PaletteManager`: Manage named palettes (Dawn, Day, Sunset, Night) and custom user palettes.
- `MouseTracker`: Track cursor positions, compute proximity strength per blob, support interaction modes.
- `SettingsController`: Provide a compact settings UI panel rendered procedurally (sliders, toggles) or a small window; changes take effect immediately.
- `PreferencesService`: Persist and restore user settings.
- `AudioManager`: Procedural tones and subtle ambient sounds; togglable, low CPU profile.

Rendering & Performance Strategy
- Single `Canvas` approach; minimal Scene Graph nodes.
- Use `AnimationTimer` with delta-time; optionally cap FPS for low-power mode.
- Avoid per-frame object allocations in hot path; reuse arrays and objects.
- Use separable passes for glow: draw blobs to an offscreen canvas at reduced resolution, blur, and composite.
- Provide `quality` settings (High / Balanced / Low) to change particle counts, blur size, and update frequency.

User Interaction & Window Behavior
- Frameless, transparent Stage with `StageStyle.TRANSPARENT` and Scene fill transparent.
- Drag to reposition: click-and-drag anywhere (or use a small draggable handle) via `setOnMousePressed`/`setOnMouseDragged`.
- Optional `Always-on-Top` toggle persisted to preferences.
- Mouse proximity influences blobs with configurable interaction strength and mode (attract/repel/distort).

Sound
- `AudioManager` produces procedural tones (sine/triangle with slow LFOs) synchronized to palette or blob energy.
- Use `javax.sound.sampled` to write PCM to a SourceDataLine; keep audio optional and off by default.

Project Layout (directory framework to create)
src/main/java/com/example/lavalamp/
- MainApp.java
- ui/
    - WidgetWindow.java
    - SettingsController.java
- render/
    - Renderer.java
    - CanvasRenderer.java
    - OffscreenBuffer.java
- core/
    - AnimationManager.java
    - SimulationManager.java
    - Blob.java
    - Particle.java
- effects/
    - VisualEffectsManager.java
    - GlowPass.java
- services/
    - PaletteManager.java
    - TimeColorProvider.java
    - ColorProvider.java
    - PreferencesService.java
    - AudioManager.java
- input/
    - MouseTracker.java
- util/
    - FXUtils.java
    - MathUtils.java
src/main/resources/
- styles/styles.css
- config/default.properties

Maven & Run Configuration
- Build: Maven 3.9.16 (developer note: local Zulu JDK 25 includes JavaFX; no runtime download required).
- Recommended plugin rule (add to `pom.xml`): use `org.openjfx:javafx-maven-plugin` with `javafx:run` for development. Example (to add to pom):

Add a `plugin` entry under `build/plugins` in the pom to enable `mvn javafx:run` during development. Configure the plugin with your main class and module settings if you use modules. Since Zulu 25 with JavaFX is installed on the system, the plugin can simply invoke the local Java to run the app without bundling JavaFX runtime.

Milestones (include platform/tool specifics)
- Milestone 1 — Baseline rendering & window (Maven, Windows, Zulu v25)
    - Create: `MainApp`, `WidgetWindow`, `CanvasRenderer`, `AnimationManager`, `Blob`, `SimulationManager`.
    - Goals: Transparent frameless stage, draggable window, stable AnimationTimer loop, render ~10 blobs, `mvn clean javafx:run` works using local JDK.

- Milestone 2 — Interaction & controls
    - Create: `MouseTracker`, `SettingsController`, `PreferencesService`.
    - Goals: Mouse proximity effects, drag-to-move, always-on-top toggle, persist settings.

- Milestone 3 — Color & themes
    - Create: `PaletteManager`, `TimeColorProvider`, `ColorProvider`.
    - Goals: Smooth time-of-day transitions (Dawn/Day/Sunset/Night) and custom palettes.

- Milestone 4 — Visual effects
    - Create: `VisualEffectsManager`, `Particle`, `GlowPass`.
    - Goals: Trails, soft glow, quality presets.

Deliverables for review (per milestone)
- Compilable Maven project that runs with `mvn clean javafx:run` on Windows with Zulu JDK 25.
- Source files for all classes listed in the project layout, implemented incrementally per milestone.
- A small README with run instructions and platform notes (Zulu 25, Maven 3.9.16).

Implementation Constraints & Notes
- Do not use FXML: create all UI and controls procedurally in code.
- Keep rendering hot-path allocations minimal.
- Provide toggles for `quality` (affects particle counts and blur radius) and `power-saver` (caps FPS or reduces updates when idle).
- The plan includes sound support but audio must be optional and low CPU.
- Persist `alwaysOnTop`, last position, and last-used palette in `PreferencesService`.

Next steps for review
- Confirm the plan and milestone priorities.
- After approval, I will implement Milestone 1 and open a PR with the initial runnable code and `pom.xml` plugin entry.

Notes
- Maven: `3.9.16` (developer machine has this installed).
- JDK: Zulu `25` with JavaFX components — no additional JavaFX runtime dependencies required for development on that machine.

-- End of plan --

