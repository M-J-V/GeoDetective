# GeoDetective
GeoDetective Android Application

// Insert Image Here

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
  
  
# User Scenarios

// Attach link to the User Manual (needs to be added to the repo)

## Players

+ Create an Account

+ View Account Details

+ Edit Account Details

+ Playing Quests

+ Requesting Creator Permission

## Creators

+ Creating Quests

+ Editing Quests
