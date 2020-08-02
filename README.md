# Green-Dome
- An Android app capable of delivering accurate and relevant information for the Shia Ithna-Asheri community, through Salah Clocks, Tasbeeh counters and more!

### Motivation
- I built this app to help members of my community with any religious obligations and needs they have. I wanted to build an app that could work regardless of a user's internet capabilities to ensure people could perform Salah anywhere in the world, at any time

## Getting Started
- As on editing this document (07/20/2020), the app is currently still being developed and it is not on the Google Play Store yet.
- To run the code, several gradle implementations will need to be used, such as the Android Volley implementation and the Google Play Location Services 17.0.0 implementation
- The android manifest file must include Permissions for Fine Location, Coarse Locaion and Internet Access.

#### Installation
- I created and ran the program through the Android Studio Application and the Android Virtual Device alongside it.
- I added the required gradle implementations to the build.gradle file and similarly added the required permissions to the Android Manifest File
- The code for the grade builds will be in the `dependencies` section with code implented as such: `implementation 'required implementation`
- The android manifest file permissions code will be as such: `<uses-permission> android:name ="required Permission"/>`
- The app required several vector assets and XML resources, all of which I added through the Android Studio platform to the drawable folder of resources
- Background Images were added to the app via the drawble folde as well
- The Adhan MP3 was added to the raw folder in the resources folder as well
- The 3 XML files alongside the 3 Java Activity classes should all be ready to go and ready for use
- An API Key must be created for the TimeZoneDB API, which is possible by maing a free account

#### Test Run
- Run the Program on the Android Virtual Device of your choice and test its functionality
- If you prefer, build a debugging APK and copy it to a real android device and test it's functionality there

#### Deployment
- Right now, the program has not been deployed, however, the plan is to make it available on the Google Play Store for free

### Authors
- This App was developed by myself, [Ali Raza Zaidi](https://twitter.com/Ali_RZ02)

### License
- This software is licensed through the GNU General Public License v3.0

### Learning Process
- Although I was already quite familiar with development in Java, as noted through other projects such as the [Animal Crossing New Horizons Twitter Bot](https://github.com/AliRZ-02/ANCHorizonsBot), I learned much with respect to Java, Android Development and XML
- I became more familiar with android libraries and files, such as the OnCreate method, OnClick method and other methods of the like
- I learned how to design elegant and useful UI's for android apps in XML usinf code editors as well as design editors
- I learned the basics of how to use Android-specific functions, such as permission requests, manifest files and of the like
- I became more familiar with Java libraries such as the Calendar library
- I learned how to integrate IO with my java code through Buttons, Image Buttons and many more
- I learned how to use Intents in Java and how to provide Toast Messages
- I learned how to use the FusedLocationProviderClient to give accurate GPS coordinates and how to use the Media Player to play Audio Messages
- I learned how to ask for permission within my Java Files to access specific resources
- I became more familiar with the Math, String, and other Java libraries
- I learned how to use the Android Volley library, as well as a Singleton class for the request queue. I also learned to deal with asynchronous tasks
- I became more familiar with object-oriented programming
- I learned how to use UI elements such as the Date Picker Tool
- I learned how to create scrollable UI
- I learned how to use the accelerometer and magnetometer inside an adnroid device to create a compass

### Development
- The development of this software is ongoing


