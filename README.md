# mazegame
Maze Game - Distributed Systems Assignment 1

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
