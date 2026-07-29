# English Mastery Android Wrapper

This Android app removes the need to manually run Simple HTTP Server.

## First launch
1. Install the APK.
2. Tap **Choose English Mastery folder**.
3. Select the extracted course folder containing `index.html`.

The folder permission is saved. Future launches open English Mastery directly as one Android app. The internal loopback server starts and stops automatically inside the app.

## Included native support
- Audio and PDF streaming with byte-range requests.
- Persistent course-folder access through Android's folder picker.
- Microphone permission for speaking recordings.
- JSON backup export and restore through Android's file picker.
- Local-only operation; no external server or internet connection is required.

The public repository contains only wrapper source code. It does not contain the private books or audio files.
