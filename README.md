<div align="left">
  <h1>Set Your Course</h1>
</div>

<div align="left">
  Android app for exercise using MLKit Pose Detection
</div>

### What is MLKit?

MLKit is a mobile-optimized machine learning package developed by Google to make it easier for mobile developers to apply machine learning techniques to their apps

In this app, __I used MLKit Sample Quickstart app for the pose detection__. I applied pose detection parts of the sample app to this app and used it to detect the user's exercise pose in real time

### Note

_While applying MLKit Pose Detection feature to this app_

- __To code compatibility__, I changed some code of sample app from Java to Kotlin
- __To add functionality and improve the UI__, I changed some code of sample app

### Documentation
[MLKit Pose Detection](https://developers.google.com/ml-kit/vision/pose-detection)

### MLKit Sample Quickstart app for the pose detection
[Github Repository](https://github.com/googlesamples/mlkit/tree/master/android/vision-quickstart)

##

### Project Type
+ Toy project

### Development Environment
+ Android Studio Electric eel
+ Kotlin 1.8.0

### Application Version
+ minSdkVersion 24
+ targetSdkVersion 33

### Libraries
+ __Jetpack__
  + ViewModel, LiveData, DataStore, CameraX
+ __Asynchronous programming__
  + Coroutine 
+ __Asynchronous programming__
  + Coroutine
+ __Image__
  + Glide
+ __ETC__
  + MLKit Pose Detection
  
##

### Functions

#### Note

When you run this app for the first time, in the pose detection screen, turn on `Run classification` through the settings button on the bottom bar (**your exercise pose cannot be detected unless you turn it on**)

### 1. Main Screen

- In this screen, you can set the exercise count and number of sets you are aiming for

### 2. Exercise Pose Detection Screen

- In this screen, your exercise pose is detected and counted
- When you have completed your target count, you move on to the next exercise screen or start the next set. Once you've completed all sets, you'll return to the main screen.
