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

### Development
- The development of this software is ongoing

### Acknowledgements
- I used many different sources to help build this software, from the [Android Java Documentation](https://developer.android.com/docs) to the [Oracle Java Documentation](https://docs.oracle.com/javase/7/docs/api/overview-summary.html)
- I used [Unsplash](https://unsplash.com/) to get images for the Background of my app; Thanks to [David Billings](https://unsplash.com/@dav_billings), [Raul Cacho Oses](https://unsplash.com/@raulcachophoto) and [Zosia Korcz](https://unsplash.com/@calanthe) for their photos
- Thanks to [AbhiAndroid](https://abhiandroid.com/ui/imagebutton) for their tutorial for image buttons
- Thanks to [Coding In Flow](https://www.youtube.com/watch?v=C_Ka7cKwXW0) for their video on Playing Sounds through the MediaPlayer Class as well as their [Volley Tutorial](https://codinginflow.com/tutorials/android/volley/part-1-simple-get-request)
- Thanks to [Rahul Chauhan](https://www.youtube.com/watch?v=ezpOmH50Xdg) for their video on adding a background image in Java
- Thanks to [Stevdza-San](https://www.youtube.com/watch?v=nlPtfncjOWA) for their video on Custom Button Designs in Android
- Thanks to [HTML Color Codes](https://htmlcolorcodes.com/) and [Flat UI Colors](https://flatuicolors.com/palette/us) for their HEX Color codes which I used in making the colour scheme for the App
- Thanks to [Stelian Matei](https://stackoverflow.com/questions/24880388/cannot-lower-case-button-text-in-android-studio/34237846) for their answer in this Stack Overflow thread regarding text All Caps
- Thanks to [Ankit Bansal](https://stackoverflow.com/questions/30051269/android-lollipop-transparent-statusbar) for their answer in this Stack Overflow thread regarding the Translucent Status Bar
- Thanks to [Geykel](https://stackoverflow.com/questions/3402787/how-to-have-a-transparent-imagebutton-android) for their answer in this Stack OVerflow thread regarding a transparent background for an Image Button
- Thanks to [Mark Allison](https://constraintlayout.com/layouts/linearlayout.html) for their work on explaining how to achieve weighted effects in a Constraint Layout
- Thanks to [Shad Sluiter](https://www.youtube.com/user/shadsluiter) for their Youtube Series on finding GPS Locations which I used to find the User's coordinates as well as their [REST API series](https://youtu.be/mn3t4IlH3HM)
- Thanks to [PuguaSoft](https://stackoverflow.com/questions/11399491/get-timezone-offset-value-from-timezone-without-timezone-name) for their response in this Stack Overflow thread regarding a query for a User's Timezone 
- Thanks to [Sealroto](https://stackoverflow.com/questions/43098150/android-how-to-make-a-scrollable-constraintlayout?rq=1) for their answer in this Stack Overflow thread regarding scrollable layouts
- Thanks to [Tutlane](https://www.tutlane.com/tutorial/android/android-datepicker-with-examples) for their tutorial and code examples for the Android Date Picker Tool
- Thanks to [MarsAtomic](https://stackoverflow.com/questions/26269193/how-to-call-a-method-function-from-another-class/26269334) for their answer in this Stack Overflow thread regarding calling methods from other classes
- Thanks to [pRaNaY](https://stackoverflow.com/questions/42189004/android-studio-textview-showing-on-preview-but-not-emulator-device) for their answer in this thread, allowing me to navigate issues in development
- Thanks to [Meenal](https://stackoverflow.com/questions/26071442/android-activity-context-is-null/43633502) for their response in this thread which allowed me to navigate issues in development
- Thanks to [SaravInfern & Bunny](https://stackoverflow.com/questions/48827503/android-datepicker-spinner-text-color-white-on-some-devices/48827570) for their responses which allowed me to change the colour of the Date Picker Tool
- External APIS: [TimeZoneDB](https://timezonedb.com/) & [Nominatim](https://nominatim.org/release-docs/develop/api/Search/)


