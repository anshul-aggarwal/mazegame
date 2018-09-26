# mazegame
Maze Game - Distributed Systems Assignment 1

## Commit on Thur, 27 Sep, 02:50

Added handling of player crashes. Tested by crashing players > 2 (Didn't crash Primary and backup). 

Refactored the project structure. Read [Project Structure Overview](#Project-Structure-Overview)

## Commit on Wed, 26 Sep, 19:26

Added pinging. Handles player addition, no handling yet for player crash as the relevant functions have not been added. Ping topology handled, pinging executes in a separate thread. Added dummy code for movement.

## Project Structure Overview

### Division in Player Class
Functions have been divided into two categories
1. Normal player functions
2. Functions needed when this player becomes a server

### Utils
All the major logic has been moved to various Utils. This will keep the Player class light.

#### Player Registration Util 
Contains all the code related to player registration and deregistration. 
To be called when 
- registering a new player
- deregistering pinged player with no response
- quiting game

#### Player Action Util
Contains all the code related to player actions 
- move
- refresh

#### Server Request Handler Util
Contains all the code needed by server to process various requests.
