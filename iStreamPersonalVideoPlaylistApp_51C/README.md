# iStream - Personal Video Playlist App

iStream is an Android app for saving and playing personal YouTube video playlists. Users can create an account, log in, paste a YouTube URL, play it inside the app, and save valid videos to their own playlist.

## Features

- User sign up and login
- Local session handling
- YouTube URL validation and embedded video playback
- Save videos to a personal playlist
- View saved playlist items
- Tap a saved video to return to the player
- Local persistence with Room database

## Tech Stack

- Java
- Android SDK
- AndroidX AppCompat, Fragment, Activity, Lifecycle, RecyclerView, ConstraintLayout
- Material Components
- Room database
- ViewBinding
- Gradle Kotlin DSL

## Project Structure

```text
iStreamPersonalVideoPlaylistApp_51C/
├── app/
│   └── src/main/
│       ├── java/com/example/istreampersonalvideoplaylistapp_51c/
│       │   ├── adapter/        # RecyclerView adapter for playlist items
│       │   ├── data/           # Room database and DAO classes
│       │   ├── model/          # Room entity models
│       │   ├── repository/     # Auth and playlist repositories
│       │   ├── ui/             # Login, signup, home, and playlist fragments
│       │   ├── utils/          # Session and YouTube URL helpers
│       │   ├── viewmodel/      # ViewModels for UI state
│       │   └── MainActivity.java
│       └── res/                # Layouts, drawables, strings, and themes
├── build.gradle.kts
├── settings.gradle.kts
└── gradle/
```

## Requirements

- Android Studio
- JDK 11 or later
- Android SDK with compile SDK 36 support
- Internet connection for YouTube playback

## Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/AkhileshwarVelijarla/AndroidProjects_Deakin.git
   ```

2. Open this project folder in Android Studio:

   ```text
   iStreamPersonalVideoPlaylistApp_51C
   ```

3. Let Android Studio sync Gradle dependencies.

4. Run the app on an emulator or Android device.

## Build From Terminal

From this project directory, run:

```bash
./gradlew assembleDebug
```

The debug APK will be generated under:

```text
app/build/outputs/apk/debug/
```

## App Flow

1. New users create an account from the signup screen.
2. Existing users log in with their username and password.
3. The home screen accepts YouTube URLs and plays valid videos in a WebView.
4. Users can save the current valid YouTube URL to their playlist.
5. The playlist screen lists saved videos for the logged-in user.
6. Logging out clears the local session and returns to the login screen.

## Notes

- User accounts and playlists are stored locally on the device using Room.
- The app accepts standard YouTube URLs, shortened `youtu.be` URLs, embed URLs, shorts URLs, and live URLs when they contain a valid YouTube video ID.
- The app declares the `INTERNET` permission for embedded YouTube playback.
