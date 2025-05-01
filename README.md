# Android Music Player

## Description

This Android application is a simple yet functional music player that allows users to play audio files stored on their device. The app features a single-page interface with a recycler view to display the list of available audio files and basic playback controls.

You can download a pre-released version of the app on your Android Device right here -> [Serv_Player_v0.1.apk](https://github.com/keleviss/androidMusicPlayer/releases/tag/Serv_Player_v0.1)

## Features

- Access and read audio files from the device's storage
- Display audio files in a scrollable list
- Play, pause, skip to next, and go to previous audio tracks
- Seek through the current audio track using a seekbar
- Display current playback time and total duration of the track
- Notification with playback controls when the app is in the background

## Documentation

The app is built using Java and follows a service-based architecture for background playback. Here's some of the main documentation, activities and methods that i used to create this.

### 1. MainActivity (MainActivity.java)

- Manages the user interface and handles user interactions
- Implements `OnClickListener` for playback control buttons
- Uses a `RecyclerView` to display the list of songs
- Handles runtime permissions for accessing external storage
- Communicates with `MediaPlayerService` using Intents
- Updates UI elements (seekbar, time displays) in real-time

Key methods:
- `onCreate()`: Initializes UI components and sets up listeners
- `loadAudioFiles()`: Queries the device's MediaStore to retrieve audio files
- `onSongClick()`: Handles song selection from the list
- `playAudio()`, `pauseAudio()`, `nextSong()`, `prevSong()`: Control playback

### 2. MediaPlayerService (MediaPlayerService.java)

- Extends `Service` class to enable background audio playback
- Implements various `MediaPlayer` listener interfaces for comprehensive playback control
- Manages audio focus to handle interruptions (e.g., calls, other apps playing audio)
- Creates and manages a foreground notification for ongoing playback

Key methods:
- `onStartCommand()`: Handles incoming intents to control playback
- `prepareMedia()`: Sets up the MediaPlayer with the selected audio file
- `playMedia()`, `pauseMedia()`, `resumeMedia()`: Control playback state
- `createNotification()`: Creates and updates the foreground notification

### 3. SongListAdapter (SongListAdapter.java)

- Extends `RecyclerView.Adapter` to populate the list of songs
- Uses a custom `ViewHolder` to efficiently recycle list items

Key methods:
- `onCreateViewHolder()`: Inflates the layout for each list item
- `onBindViewHolder()`: Binds song data to the ViewHolder
- `formatDuration()`: Converts milliseconds to a readable time format

### 4. MyMediaPlayer (MyMediaPlayer.java)

- Singleton wrapper around Android's `MediaPlayer` class
- Maintains static variables for tracking playback state across the app

Key features:
- `getInstance()`: Returns the singleton instance of MediaPlayer
- `currentIndex`: Keeps track of the currently playing song
- `isPaused` and `isStopped`: Boolean flags for playback state

### 5. Song (Song.java)

- Model class representing an audio file
- Implements `Serializable` for easy passing between components

Attributes:
- `path`: File path of the audio file
- `title`: Title of the song
- `duration`: Duration of the song in milliseconds

## Architecture and Data Flow

1. `MainActivity` loads audio files from the device and displays them using `SongListAdapter`.
2. When a song is selected or a control button is pressed, `MainActivity` sends an Intent to `MediaPlayerService`.
3. `MediaPlayerService` uses the singleton `MyMediaPlayer` instance to control playback.
4. `MediaPlayerService` updates its notification and sends broadcast intents back to `MainActivity` to update the UI.

## Key Android Concepts Used

- Services for background playback
- ContentResolver and MediaStore for accessing audio files
- RecyclerView for efficient list display
- Runtime Permissions for accessing external storage
- Foreground Services and Notifications for ongoing playback indication

## Getting Started

### Prerequisites

- Android Studio
- Android SDK (minimum SDK version: [specify your minimum SDK version])
- A physical Android device or emulator for testing

### Installation

1. Clone this repository:
   ```
   git clone https://github.com/keleviss/androidMusicPlayer.git
   ```
2. Open the project in Android Studio.
3. Build and run the app on your device or emulator.

## Usage

1. When you first launch the app, it will request permission to access your device's file storage.
2. Once permission is granted, the app will scan for and display available audio files.
3. Tap on any song in the list to start playback.
4. Use the controls at the bottom of the screen to play, pause, skip to the next track, or go to the previous track.
5. Use the seekbar to navigate within the current track.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
# musicapp
# musicapp
