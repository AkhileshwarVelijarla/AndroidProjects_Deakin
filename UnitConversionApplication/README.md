# Unit Conversion Application

## Purpose

This project is a multi-category unit converter that lets a user choose a conversion domain, select source and target units, enter a value, and calculate the result. It was built as a straightforward UI-driven Android exercise with clear input-processing-output flow.

## Tech Stack

- Java
- Android XML layouts
- Standard Android widgets: `Spinner`, `EditText`, `Button`, `TextView`

## Project Structure

```text
UnitConversionApplication/
├── app/src/main/java/com/example/unitconversionapplication/
│   └── MainActivity.java
└── app/src/main/res/
    ├── layout/         # Main screen and spinner item layout
    ├── values/         # Strings, colors, categories
    └── drawable/       # Background assets and widget styling
```

## Data Flow

This app uses a direct UI-controller flow:

1. `MainActivity` binds all widgets in `onCreate()`.
2. The category spinner selection triggers `updateSpinners(category)`.
3. `updateSpinners()` loads the valid unit list for that category into the From and To spinners.
4. The user enters a numeric value and taps Convert.
5. `MainActivity` validates the input string and parses it to `double`.
6. Based on the selected category, the activity calls one conversion method:
   - `convertCurrency`
   - `convertFuel`
   - `convertVol`
   - `convertDist`
   - `convertTemp`
7. The returned value is formatted into the result text view.

## Why These Decisions Were Made

### Single-activity design

The app contains one self-contained task: convert a number from one unit to another. A single activity is the simplest structure for that workflow.

### Spinners for category and unit selection

Spinners keep the input space controlled. That reduces invalid combinations and avoids users typing unit names manually.

### Category-first interaction

The category is chosen before the source and target units so only relevant units are shown. This keeps the UI small and prevents cross-category conversion mistakes.

### Separate conversion methods

Each category has its own method. That decision favors readability over abstraction. For a coursework-scale project, explicit methods are easier to trace and debug than a more generic rule engine.

### Base-unit conversion strategy

Most conversion methods normalize to a base representation first, then convert to the target:

- currency normalizes through USD
- temperature normalizes through Celsius
- other pairs convert directly because they only have two units

This approach reduces repeated formulas and makes the logic easier to extend.

### Toast for invalid input

A toast is enough for this scope because there is only one primary validation rule: the user must enter a valid numeric value before conversion.

## Conversion Logic Notes

### Currency

Currency values are converted to USD first and then to the requested target currency. Fixed rates are used, which keeps the app deterministic and offline.

### Fuel, volume, and distance

These categories currently support one pair of units each, so the logic is intentionally simple and symmetric.

### Temperature

Temperature is handled separately because it requires offset-based formulas rather than pure multiplication.

## Main Components

### `MainActivity`

- Owns all UI references
- Reacts to spinner selection changes
- Validates input
- Runs conversion logic
- Updates the result label

### `activity_main.xml`

- Defines a single vertical flow from category selection to result
- Uses a bold visual style with a custom background and highlighted result area

### `arrays.xml`

- Supplies the top-level conversion categories through resources instead of hardcoding them directly in layout

## Known Tradeoffs

- Rates and formulas are hardcoded in `MainActivity`, which is simple but not scalable.
- Currency exchange values are fixed and will become outdated over time.
- There is no history, favorites, or offline storage because the app focuses only on immediate conversion.
- All business logic lives in the activity, which is acceptable for this size but not ideal for long-term growth.

## How To Run

1. Open `UnitConversionApplication` in Android Studio.
2. Sync Gradle.
3. Run on an emulator or physical device.

## Suggested Next Improvements

- Extract conversion rules into dedicated classes
- Add more units within each category
- Add result formatting controls for decimal precision
- Replace fixed currency rates with a service-backed source if live data is ever needed
