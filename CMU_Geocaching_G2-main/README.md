# LEI - Licenciatura Engenharia Informática

## Computação Móvel e Ubíqua

### Relatório Milestone 1

---

## Introduction

This report’s purpose is to document the process of planning and developing the project for the CMU course unit.  
This document accounts only for the first milestone and all that has been done within it.

---

## Theme and Scope

As a theme, the group has chosen to develop a Geocaching app. Users will be able to create an account, after which they can choose a cache to look for. Once a cache is found (user is close enough to the location), a trivia question will be presented. If the user answers correctly, they will be awarded more points for their achievement. The points can later be used to create caches. Users will also be able to access a history of all the caches they have found thus far.

---

## Functional Requirements

### User Management

#### Category: User Management

- **ID**: FRUM-01  
- **Name**: Login and Registration  
- **Description**: Users must be able to register in the application, upon which their data will be securely kept in the database. If a user is registered, they will then be able to log into their account, allowing them to use the platform.  
- **Priority**: 1 - Essential  
- **State**: Proposed  
- **Restrictions**: N/A  

#### Category: User Management

- **ID**: FRUM-02  
- **Name**: Account Changes  
- **Description**: Registered users will be able to update their accounts’ information whenever they deem fit.  
- **Priority**: 1 - Essential  
- **State**: Proposed  
- **Restrictions**: N/A  

### GeoCaching

#### Category: GeoCaching

- **ID**: FRGC-01  
- **Name**: Searching for a Cache  
- **Description**: Users will be able to search for cache locations within a map.  
- **Priority**: 1 - Essential  
- **State**: Proposed  
- **Restrictions**: N/A  

- **ID**: FRGC-01.1  
- **Name**: Filtering cache locations  
- **Description**: When searching for a cache, users can filter locations by radius in regards to their real-time location, and also the difficulty of the geocaching challenge.  
- **Priority**: 1 - Essential  
- **State**: Proposed  
- **Restrictions**: N/A  

#### Category: GeoCaching

- **ID**: FRGC-02  
- **Name**: Geocaching Activity  
- **Description**: Users will be able to start a geocaching activity, and consult the map on their way, checking the elapsed time.  
- **Priority**: 1 - Essential  
- **State**: Proposed  
- **Restrictions**: N/A  

#### Category: GeoCaching

- **ID**: FRGC-03  
- **Name**: Stopping Geocaching Activity  
- **Description**: Once a geocaching activity is started, a user can end it, which will result in no points earned.  
- **Priority**: 1 - Essential  
- **State**: Proposed  
- **Restrictions**: N/A  

#### Category: GeoCaching

- **ID**: FRGC-04  
- **Name**: Find Cache  
- **Description**: Once a user is close enough to a cache location, it will be marked as found.  
- **Priority**: 1 - Essential  
- **State**: Proposed  
- **Restrictions**: N/A  

- **ID**: FRGC-04.1  
- **Name**: Answer Trivia  
- **Description**: If a user finds a cache, they will need to answer a trivia question in order to collect the full amount of points.  
- **Priority**: 1 - Essential  
- **State**: Proposed  
- **Restrictions**: N/A  

#### Category: GeoCaching

- **ID**: FRGC-05  
- **Name**: Create Cache  
- **Description**: Once a user has gained enough points, they will be able to create a cache location of their own for other users to find.  
- **Priority**: 1 - Essential  
- **State**: Proposed  
- **Restrictions**: N/A  

- **ID**: FRGC-05.1  
- **Name**: Upload image  
- **Description**: Creating a cache involves the user uploading an image of the spot, to make it easier for other players to recognize the spot.  
- **Priority**: 1 - Essential  
- **State**: Proposed  
- **Restrictions**: N/A  

#### Category: GeoCaching

- **ID**: FRGC-06  
- **Name**: Answer Trivia Question  
- **Description**: Users will be able to answer trivia questions upon finding their cache, for a chance at earning better rewards.  
- **Priority**: 1 - Essential  
- **State**: Proposed  
- **Restrictions**: N/A  

---

## Features Implemented

- **Layouts**: The layouts and previews for these have been implemented as per the designs, albeit with some missing features, and placeholder items. The layouts are responsive within measure, according to the device orientation and size.
- **Navigation**: Navigation has been fully implemented, as well as the drawer for easy access to certain pages.  
- **Use of Intents**: The app currently makes use of android intents by permitting the user to open google maps centered on the cache location.
- **Local Database**: A database has been created with room, containing the following entities:
  - **User**: Which holds all information of the user currently logged in, to allow for basic functionality 
  - **Cache**: Which is meant to hold information related to the current cache being “hunted” by the user, as an attempt to maintain functionality if the user lacks internet access during the caching activity
  - **Trivia Question**: Which holds information related to the trivia questions users will have to answer  to earn the maximum amount of points. This has been placed in the local DB for a similar reason to the cache entity. The trivia questions will be sourced from https://the-trivia-api.com/docs/v2/ , as the group found it was the most appropriate choice for an external API to use in the project, together with Retrofit.


- **Android Features**: 
  As a way to incorporate android features into the application, the group settled on using the android gps and location system in order to track the user’s location in the background as they search for the cache.
  Additionally, we will, in the future, implement a way for the theme to change to dark automatically, according to the correct lighting conditions.


---

## Implementation Details

### Database Structure

- **User - Cache Relationship**: 
  - Many-to-many relationship (a user can find many caches; a cache can be found by many users).  
  - One-to-many relationship (a user can create many caches).
  - Additionally, there is a one-to-many relationship seen as a user can create many caches.
  To solve this many-to-many relationship, the approach taken goes as follows:
    - The UserCacheCrossRef data class effectively joins the entities in one table containing only the primary keys, and one common attribute, which is the date that a cache was found by a user.
    - The UserWithCaches class associates the caches found by a user to the user entity, so that these can be retrieved easily.
    - Similarly, the CacheWithUsers class associates the users that found a cache to the cache entity, so that these can be retrieved easily.


### DAOs and Repositories

- **DAOs**: Queries for User, Cache, Trivia Questions, and UserCacheCrossRef.
  - The Cache, User and Trivia Questions DAOs are similar, containing the necessary access queries to interact with the data.
  - The only DAO that has some more variety is the  UserCacheCrossRefDao, as it interacts with both the User and Cache entities.
- **Repositories**: Abstract layer over DAOs.
  - The repositories are simply a layer of abstraction over the DAOs, containing similar content. 

 

### ViewModels

The purpose of view models is to take the raw data from the model, and make it possible to display on the UI. Once that change is made, it notifies the UI so that it can show the content on the screen.

From the View, when there is some kind of UI action (event), like a click, the UI (View) will send this information to the ViewModel, so that it can react to it. For example, when a user clicks “Save” on something they have input, and that needs to be persisted, the View Model will send that information to the Model for it to be saved.

The view models developed thus far are relative to the User and Cache entities, with possibility of expanding in the future, as the group has had some trouble understanding how to implement this component of the MVVM architecture. Their purpose is to hold the state of user’s and cache’s state, managing changes coming from both the model and from the UI. 

For this, along with the view models, classes to hold events were created, these hold the list of events that will trigger changes in models, facilitating communication with the UI.

### Location Tracking

The location tracking feature serves as a way to track the user’s progress over finding their geocache, running in the foreground until the user reaches the destination.

### Notes: For screenshots and references to code and design, please refer to the PDF version of this report. Repository link: https://github.com/degtyareva16/CMU_Geocaching_G2.git
