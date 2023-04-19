# GeoDetective
GeoDetective Android Application

![logo](https://github.com/M-J-V/GeoDetective/blob/master/GeoDetective_Logo.png)

This application was developed by group 23 for the purpose of the course DBL App Development at the Tecnical University of Eindhvoen

## What is Geodetective?

Geodetective is an android application that takes inspiration from GeoCaching, where users aim to find a location in their environment. Geodetective works by means of "quests" that include an image of the location the player must find, as well as at least a Title and Description (quests may also include optional hints).
Users can take one of two roles:

+ **Players** - Every new user on the application is a player when they first create an account. These users are able to play quests and view/edit their own profile but are unable to create or edit quests.
+ **Creators** - These are users that have manually been given permission to create and edit quests from the app itself that other users are then allowed to access. (Creators have access to all the smae features as players)

Our primary vision as a use-case for Geodetective would be to use it as a fun and engaging way to help new university students to discover interesting and 'out-of-the-way' spots around campus. Here the Creators would be students that are given responsiblity by the university to create and maintain quests for the new students to play.

## Database uaage

To make the magic of GeoDetective happen, the app makes use of Firebase to store and handle the required data.
Firstly a Firestore Database is used to handle data about the:
  + User accounts (with hashed passwords for security)
  + Playable Quests
  + Attempts that users have made to complete quests (it is possible to either fail or pass a quest)
  + Requests by Players to be granted Creator permission (these are handled manually by the admins of GeoDetective)
  
Secondly Firebase Storage is used to store all the images that are used by the Quests that can be played. A seperate database is used for this as images cannot be stored in a Firestore Database and Firebase Storage is better suited for information management of these larger files.

## Code Structure

The application is written using Java and is split into 6 main packages each with their own purpose:

+ **Activities**
Here we the classes that create and manage each of the different screens that the user can interact with.

+ **Connectivity**
The application requires that the user has access to the internet as well as GPS location on their device at all times when using the app. Here we find classes that check for such connectivity and react appropriately once such connection is lost.

+ **Game Components**
Each 'game' or 'quest' requires more than simply the quest information itself, here we find classes that work together to allow the user to play or create a quest. These involve 
  + "image Input" class to handle obtaining an image for a new quest, either through the phone camera or gallery
  + "Location" class that obtains the users current GPS location and deals with all related computations such as calculating distance
  + "Quest" class that allows us to use all of the quest data in the form of an ADT
  + "Timer" class that is visible once a user starts to play a quest, allowing them to track how long it takes them to find a location.
  
+ **GUI List Adapters**
These are custom adapter classs that the application makes use of so that we can make use of lists to display to the user available quests as well as their attepmt history.

+ **Helpers**
Such classes are used for utility functions such as ensuring that passwords meet the applications minimum security requirements and that passwords are also hashed before being stored in the databse.

+ **Singletons**
These include singleton classes that are useful in handling objects that should not have more than one instance at a time, such as
  + Active (logged in) User
  + Active Quest (either being played or edited)
  + Connection to the Firestore Database.
  
## Running the Application

The app can either be ran on a physical device by compiling and buiding the .apk file within Android studio, or it can be ran on the emulator within Android Studio. (Note: As a team we have on occasion found that the emulator can be too slow and gives issues when communicating with the database, this issue was typically fixed by instead using emulator version 31.2.9)
  
# User Scenarios

The ![User Manual](https://github.com/M-J-V/GeoDetective/blob/master/User_Manual.pdf) can be consulted to better understand specific actions in the User Scenarios.

## Players
Here we discuss the User Scenario for a brand new player. The brand new player in this scenario is for example a new student interested in discovering the TU/e campus.

### Creating an Account

The student is brand new to the TU/e and looking to discover some interesting spots on campus to study or take a break, so they install the application through the .apk file provided to them. Upon opening the app the student will first see the login screen. To create an account they press the "Register" button on the bottom right and are taken to the "Create Account" page. They now enter a unique Username and their chosen password (once more to confirm) to create an account. Once their account is created they are taken back to the first login screen. (The password the student creates must meet the following specification: Is of length at least 4, contains a letter, contains a number, contains a special character. The student is notified if one criterium is missing)

### Logging in

Once the student has created an account and is on the "Log In" page, they can enter their Username and Password into the appropriate fields to log in to the main screen of the application.

### Viewing Account Details

From the Main Menu page, the student can press the button on the bottom right of the screen to be taken to their profile page. Here they are able to:
  + See their Username 
  + See if they have been given Creator permission
  + Set Camera and Phone Gallery permissions
  + Go to their Quest History Screen
  + Go to the Edit Profile Screen
  + Request Creator permission
  + Delete their account

### Editing Account Details

Upon pressing "Edit Profile" button on the profile page, the student is taken to the edit profile page, where it is split into two. One section of the page can be used to change the student's Username (this must still be unique), while the other section allows the student to change their password by asking them for their old and new password choice. (The new password must also comply with the password specifications mentioned previously)

### Playing Quests

Once back on the Main page, the student can press the "Go on a Quest" button to play a quest. They are then presented by a ScrollView list of all quests that are available to be played, by showing the Quest image, title and creator. Once a quest is selected they are taken to the appropriate quest overview page where the quest description is also visible. (The student is also asked to provide GPS permission to be able to play)

Now the student can press "Start Quest" to begin the timer and search for the location, once they believe they have found the location, the student presses "Finish Quest". An appropriate message appears informing the student if they have succeeded or failed. If the quest is failed the student has the choice to abort the quest and give up or keep trying. 

If the quest is too difficult for the student, they may get a hint by pressing the "Hint" button if the creator of the quest added one.

### Viwing Quest History

Once the student has played a number of quests, they may wish to see how they have done. By going back to the profile page and pressing "History" they are presented with a list of all their attempts so far, the time the attempt was made, and whether they failed or succeeded in their attempt. (Note that even when 'finishing', failing, but continuing a quest, this counts as a failed attempt)

### Requesting Creator Permission

After playing with the application and discovering all about campus, the student has now moved onto their second year and wish to become an intro-parent, helping now new students also discover the campus. They can on the app from the profile page press "Request Creator Permission", where a request will be sent to the admins of Geodetective through the database that the student has made the request. If it is granted, this will be reflected on their profile page and will now be able to also create quests.

## Creators

### Creating Quests

The student, now excited to share their own discoveries with new students presses "Create a Quest" on the Main page. They choose an image for the quest by pressing "Choose Image" and can either use their camera or phone gallery to upload a photo of the location to be found. (Appropriate permissions are asked if they have not been given yet). However to create the quest the creator must be at the location to be found, since the coordinates are obatined using the devices real time GPS location.

Next they provide the quest with a unique quest title, and a non-empty description. If they decide to they can also add a hint to the quest. These are all inputted into the appropriate text fields. Once the quest is ready to be created, the student presses "Submit Quest" and the quest is uploaded to the database and is ready to be played.

### Editing Quests

If at some point the student decides to edit some detial about the quest, they must first find it in the quest list by pressing "Go on a quest" in the main menu and choose it from the list. Once on the quest overview, the "edit" button in the top right corner of the screen can be pressed and will take the sudent to the edit quest details page. Here they are allowed to change any aspect of the quest, including the image, title and description. These must still include a unique title and non-empty description. (Note however that the location of the quest cannot be changed after it is created, if this is necessary it is recommended to create a new quest). The quest can also be deleted from here.
