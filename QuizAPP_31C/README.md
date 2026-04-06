# QuizAPP_31C

## Purpose

This project is a lightweight arithmetic quiz app. It focuses on predictable UI state management, simple game progression, and immediate feedback while staying within a single-screen Android app structure.

## Tech Stack

- Java
- Android XML layouts
- DataBinding
- LiveData and ViewModel
- SharedPreferences for theme persistence

## Project Structure

```text
QuizAPP_31C/
├── app/src/main/java/com/example/quizapp_31c/
│   ├── model/          # UI state and answer option models
│   ├── util/           # Binding adapters, event wrapper, theme preference
│   ├── viewmodel/      # Quiz state machine and business logic
│   └── MainActivity.java
└── app/src/main/res/   # Single-screen layout, colors, strings, themes
```

## Data Flow

The app is built around a unidirectional UI-state flow:

1. `MainActivity` creates `QuizViewModel` and binds it to the layout.
2. The XML layout reads `viewModel.uiState` and renders one of three sections:
   - welcome
   - quiz
   - result
3. User actions such as start, option select, submit, next, retake, and finish call ViewModel methods directly through DataBinding.
4. `QuizViewModel` updates its internal quiz state and republishes a new `QuizUiState`.
5. Binding adapters convert state into visual behavior such as `VISIBLE/GONE`, feedback colors, and answer button styling.
6. Transient messages and finish requests are emitted through `Event<T>` wrappers and consumed once by `MainActivity`.

## Why These Decisions Were Made

### Single activity, single screen

This app is intentionally compact. A single activity keeps navigation overhead out of the way and makes the quiz loop easier to follow.

### ViewModel-owned state machine

`QuizViewModel` owns:

- current screen
- player name
- score
- question index
- generated operands and operator
- correct answer
- selected answer
- submission status

This keeps the game logic out of the activity and prevents UI widgets from becoming the source of truth.

### Immutable `QuizUiState`

The UI is rendered from a single state object rather than many disconnected widget updates. That makes the screen easier to reason about because every render is based on one consistent snapshot.

### DataBinding for user actions and display

DataBinding reduces repetitive wiring code in the activity. It also fits this project well because most interactions are straightforward button taps and display updates.

### Custom binding adapters

`BindingAdapters` were introduced for three repeated UI concerns:

- show/hide sections
- apply feedback text colors
- style answer buttons based on correctness and selection state

This keeps UI transformation logic out of `MainActivity`.

### Event wrapper for one-off actions

Snackbars and finish requests should not replay after rotation or rebinding. The `Event<T>` wrapper protects those actions from being handled twice.

### SharedPreferences for dark mode

Theme selection is user preference rather than quiz state, so it is stored with `SharedPreferences` through `ThemePreference`.

## Quiz Logic Flow

1. The user enters a name and taps Start.
2. `onStartQuiz()` validates the name and resets quiz counters.
3. `prepareQuestion()` generates operands, operator, correct answer, and four answer options.
4. The user selects one option.
5. `onSubmitAnswer()` locks the question and evaluates correctness.
6. The UI reveals feedback and enables the next step.
7. `onNextQuestion()` either loads the next question or switches to the result screen.
8. The user can retake the quiz or finish the activity.

## Question Generation Decisions

### Random arithmetic categories

Addition, subtraction, and multiplication are enough to create variety without introducing extra UI complexity.

### Preventing awkward subtraction questions

For subtraction, operands are swapped when needed so the result stays non-negative. That makes the quiz friendlier and avoids distracting edge cases for this scope.

### Distractor generation

Wrong answers are generated near the correct answer rather than completely random. This makes guesses less trivial and keeps options believable.

## Main Components

### `MainActivity`

- Applies persisted theme preference before UI setup
- Binds the ViewModel to the layout
- Observes snackbar and finish events

### `QuizViewModel`

- Implements the quiz state machine
- Generates questions and options
- Computes progress and feedback text
- Publishes a full `QuizUiState`

### `QuizUiState`

- Represents the full UI snapshot
- Decouples rendering from business logic internals

### `ThemePreference`

- Reads and writes dark mode preference
- Keeps preference logic out of the activity

## Known Tradeoffs

- Questions are generated in memory only, so there is no score history or persistence.
- The game rules are fixed at five questions and three operation types.
- Random generation is simple and does not balance difficulty over time.
- There is no separation into repository/domain layers because the app has no external data source.

## How To Run

1. Open `QuizAPP_31C` in Android Studio.
2. Sync Gradle.
3. Run on an emulator or physical device.

## Suggested Next Improvements

- Add division and difficulty levels
- Persist recent scores
- Add unit tests for question generation and scoring rules
- Add accessibility refinements for large text and talkback
