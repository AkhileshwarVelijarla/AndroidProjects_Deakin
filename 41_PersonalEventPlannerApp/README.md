# Personal Event Planner App

## Purpose

This project is a CRUD-based Android app for creating, editing, viewing, and deleting personal events. It was built to demonstrate a small but complete mobile workflow with persistent storage, navigation, validation, and reactive UI updates.

## Tech Stack

- Java
- Android XML layouts
- Android Jetpack Navigation
- ViewBinding and DataBinding
- LiveData and ViewModel
- Room database

## Project Structure

```text
41_PersonalEventPlannerApp/
├── app/src/main/java/com/example/a41_personaleventplannerapp/
│   ├── data/            # Room entity, DAO, database, repository
│   ├── ui/              # Fragments and RecyclerView adapter
│   ├── util/            # Validation, formatting, one-time event wrapper
│   ├── viewmodel/       # Screen state and business logic
│   └── MainActivity.java
└── app/src/main/res/    # Layouts, navigation graph, colors, strings
```

## Data Flow

The app follows a simple MVVM flow:

1. `MainActivity` hosts the `NavHostFragment` and bottom navigation.
2. `EventListFragment` observes `EventViewModel.getAllEvents()`.
3. `EventViewModel` reads and writes through `EventRepository`.
4. `EventRepository` delegates persistence work to Room through `EventDao`.
5. Room emits updated `LiveData<List<EventEntity>>`.
6. The fragment receives the new list and passes it to `EventAdapter`.
7. The adapter renders cards and dispatches edit/delete actions back to the fragment.

For the form flow:

1. `AddEditEventFragment` asks the shared `EventViewModel` to `prepareForm(eventId)`.
2. The ViewModel either resets the form for create mode or loads an event from Room for edit mode.
3. Form fields are exposed as `MutableLiveData` values and bound to the layout.
4. On save, the ViewModel validates title and date/time.
5. If validation passes, the ViewModel creates an `EventEntity` and calls repository `insert()` or `update()`.
6. The ViewModel emits a one-time completion event so the fragment can navigate back to the list.

## Why These Decisions Were Made

### Shared `EventViewModel`

A single activity-scoped ViewModel was used so both fragments can share state without passing large objects through navigation arguments. Only the `eventId` is passed between screens; everything else is loaded from the source of truth.

### Room for persistence

Room was chosen instead of keeping events only in memory because the app needs to survive process death and app restarts. It also gives a clear separation between UI logic and storage logic.

### Repository layer

The repository is small, but it keeps database access out of the ViewModel and leaves one place to change if the storage mechanism changes later.

### `LiveData` for UI updates

The list screen updates automatically when Room emits changes. This avoids manual refresh logic after insert, update, or delete.

### One-time event wrapper

Snackbars and navigation are not persistent screen state. They are transient actions. `OneTimeEvent` prevents the same message or navigation event from replaying after configuration changes.

### Validation in the ViewModel

Validation is handled in `EventViewModel` through `EventValidator` so business rules stay out of the fragment. The fragment is kept focused on rendering and input widgets.

### Single-threaded Room executor

Database writes are pushed to `EventDatabase.databaseWriteExecutor` to avoid blocking the main thread and to keep write ordering predictable.

## Main Components

### `MainActivity`

- Sets up view binding
- Hosts the navigation graph
- Connects bottom navigation to the `NavController`

### `EventListFragment`

- Observes the event list
- Shows empty-state visibility
- Handles edit and delete actions
- Displays snackbar messages from the ViewModel

### `AddEditEventFragment`

- Loads create or edit mode based on `eventId`
- Opens date and time pickers
- Triggers save and clear actions
- Navigates back after a successful save

### `EventViewModel`

- Holds the screen state for form fields
- Validates input
- Coordinates repository calls
- Publishes transient UI events

### `EventAdapter`

- Uses `ListAdapter` and `DiffUtil` so list updates stay efficient and easier to reason about
- Maps an `EventEntity` to card UI text

## Validation Rules

- Title must not be blank
- Date and time must be selected
- Past dates are rejected

These checks exist to keep the stored dataset meaningful and to prevent obvious invalid records.

## Known Tradeoffs

- Category and location are optional free-text fields, which is flexible but can lead to inconsistent labels.
- The app uses a single database table and simple UI state, which is good for this scope but not enough for recurring events, reminders, or sync.
- Date/time formatting is local and simple; there is no timezone handling strategy beyond device defaults.

## How To Run

1. Open `41_PersonalEventPlannerApp` in Android Studio.
2. Sync Gradle.
3. Run on an emulator or physical Android device.

## Suggested Next Improvements

- Add category presets with validation
- Add search and sort on the event list
- Add migration support for future schema changes
- Add repository and ViewModel unit tests beyond validator coverage
