
# Noober 2.0 - Debugging Library for iOS & Android 🚀

Noober 2.0 is the second version of Noober, a debugging library designed specifically for iOS. This version is powered by **Kotlin Multiplatform Mobile (KMM)** and **Compose Multiplatform**, allowing you to use a single library for both ![](https://img.shields.io/badge/Android-black.svg?style=for-the-badge&logo=android) ![](https://img.shields.io/badge/iOS-black.svg?style=for-the-badge&logo=apple)

## Table of Contents

- [🚀 Noober 2.0 - Debugging Library for iOS & Android](#-noober-20---debugging-library-for-ios--android)
- [🔍 Features](#-features)
  - [Common](#common)
  - [Android](#android)
- [🖇️ Getting Started](#️-getting-started)
  - [Android](#android-1)
  - [iOS](#ios)
- [🚀 Initialization](#-initialization)
  - [Android](#android-2)
  - [iOS](#ios-1)
- [⚙️ Additional Setup](#️-additional-setup)
  - [Android](#android-3)
  - [iOS](#ios-2)
- [🛠️ Utilizing Additional Features](#️-utilizing-additional-features)
- [📽️ Demo](#️-demo)
- [🛠️ Built with](#️-built-with)
- [📜 License](#-license)


## 🔍 Features

**Common**
- 🌐 Network Request Tracking: Noober 2.0 provides detailed information about all network requests made from your app, including their responses. You can also share this information with others for debugging purposes.

- 📝 Custom Logs: Easily add custom logs to display important data during debugging, such as analytics events fired within your app.

- 🌐 Change Base URL: Easily switch between different base URLs for network calls during debugging, streamlining testing in various environments.

- 🔗 Share User Properties via Deep Link: Share user properties stored in shared preferences (Android) or UserDefaults (iOS) with your teammates via deep links. This is especially useful for debugging and switching accounts without going through the authentication process repeatedly.

- 📊 Show and Edit Saved User Properties: Noober allows you to view and edit saved user properties without the need for additional customization.

**Android**
- 📦 Support for Multiple Shared Preferences: Noober 2.0 supports multiple shared preferences and encrypted shared preferences simultaneously.

- 🪲 Auto-detect Crashes: Automatically detect crashes and display their stack traces in the Logs section. You can easily share crash details, including account information, via deep links.

- 🧹 Clean Release Artifact: Ensure a clean release artifact with no traces of Noober in your final APK.



## 🖇 Getting Started

**Android**

Step 1: Configure Maven Repository

To get started with Noober 2.0 on Android:

Add the Noober 2.0 dependency to your app-level build.gradle file:

```gradle
dependencies {
    // For release builds
    releaseImplementation ("io.github.abhi165:noober-no-op-android:${version}")

    // For debug builds
    debugImplementation ("io.github.abhi165:noober-android:${version}")
    
    // Other dependencies...
}
```
**iOS**

To integrate Noober 2.0 into your iOS project, follow these steps:

Step 1: Add the following to your `~/.netrc` file (create the file if it doesn't exist):

```
machine maven.pkg.github.com
  login [github username]
  password [your new personal access token]
  ```

Step 2: In Xcode, go to File > Add Package Dependency.

Step 3: Enter the repository URL for Noober 2.0.

That's it! Noober 2.0 will be added to your iOS project, allowing you to enjoy the benefits of cross-platform debugging.

    
## 🚀 Initialization

**Android**

In your Application class:

```kotlin
Noober.start(this)
```
Add the NooberInterceptor to your OkHttpClient:
```kotlin
val client = OkHttpClient.Builder()
    .addInterceptor(NooberInterceptor())
    .build()
```
**iOS**

In your AppDelegate:
```swift
Noober().start()
```


## ⚙️ Additional Setup

**Android**
- Notification Permission: Starting with Android 13, you may need to request the android.permission.POST_NOTIFICATIONS permission to display notifications when Noober tracks network activity.

**iOS**
- If you're using a 3rd-party library for network calls like Alamofire, add NoobProtocol to your URLSessionConfiguration:
```swift
let configuration: URLSessionConfiguration =  {
    let config = URLSessionConfiguration.default
    config.protocolClasses?.insert(Noober().getNoobProtocol()!, at: 0)
    return config
}()

```

- To handle deep links on iOS, follow these steps:

Step 1: Open Xcode and go to Project Settings -> Info.

Step 2: Add a URL Scheme with the value "noober."

Step 3: Call Noober().importAccountFromNoob(url: NSURL) from your SceneDelegate (or App Delegate if you don't have AppDelegate).

- To open Noober on iOS, you can use your own gesture and call Noober().toggle() or utilize Noober's UIWindow, which opens Noober when a shake gesture is detected. You can access the Noober window by calling Noober().getNoobWindow().

## 🛠️ Utilizing Additional Features
- To add logs, use:
```kotlin
Noober.log(tag: String, value: Any, isError: Boolean = false)

```

- To set user properties for sharing via deep link:
```kotlin
Noober.setUserProperties(prop: List<NoobUserProperties>))
```

`NoobUserProperties` is a data class containing two keys:

1. `key`: This is the identifier Noober uses to retrieve a specific data value (e.g., an authentication token) from shared preferences (Android) or UserDefaults (iOS).

2. `alternateKeyForCrossPlatform` (Optional): In cases where the same data has different key names on Android and iOS, this attribute helps Noober map the keys correctly when sharing and receiving data via deep links.

## 📽️ Demo


| Android  | iOS |
| ------------- | ------------- |
| <video src="https://github.com/ABHI165/Noober-2.0/assets/56068132/97068b95-1120-4b49-bd9b-5e0701cd0439">  | <video src="https://github.com/ABHI165/Noober-2.0/assets/56068132/3bd442fd-0dbe-4b5e-afa4-bfac3b2febf1">|


## 🛠️ Built with 

- [Kotlin](kotlinlang.org): Programming language
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html): For building multi-platform applications in the single codebase.
- [Jetpack/JetBrains Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/): For a shared UI between multi-platforms i.e. Android and iOS in this project.
- [Kotlinx Datetime](https://github.com/Kotlin/kotlinx-datetime): A multiplatform Kotlin library for working with date and time.
-  [PreCompose](https://github.com/Tlaster/PreCompose): Compose Multiplatform library for  Navigation
-  [KMMBridge](https://github.com/Tlaster/PreCompose): KMMBridge is a set of Gradle tooling that facilitates publishing and consuming pre-built KMM (Kotlin Multiplatform Mobile) Xcode Framework binaries.





## 📜 License

```

  Copyright 2023 ABHI165

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   ```
