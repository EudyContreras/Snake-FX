# SnakeFX
A Snake Game made with JavaFX.

![capture](https://github.com/EudyContreras/SnakeFX/blob/master/Capture.PNG)

I made this game to try out JavaFX and as a personal challenge at the end of my first year in computer science in 2016. It became a big project that helped me learn a bunch. Feels a little late to put this project here but better late than never. It is not completed but it is enough. 

## Game Features

* 2 player mode where players can play against eachother.
* Classic mode 
* Snake AI player using a combination of complex path finding techinques and adaptable gameplay. 
* Snake speed boost
* Muliple apples to collect
* Damage zones
* Simple particle system
* Simple wind simulation
* Simple physics and collision
* Multiple maps
* Other features I dont remember...

## Technical Fetures

* Resisable game window
* Show path finding graph
* Debug mode
* Other features I dont remember

## Working Modes

At the moment only 2 modes are working through the game menu which are classic mode and multiplayer mode.

## Settings

Inside the `GameSettings.java` you will find all the game settings. You are welcome to play around. Beware that changing some settings may break the game.
There are a few settings of intrest that I personally recommend that you play around with.

* `SHOW_ASTAR_GRAPH`
* `DEBUG_MODE`
* `ALLOW_AI_CONTROL` to play against the AI
* A bunch of others...

## Controls

Player one is controlled using `A,W,S,D` for direction and `Left Shift` for boost
Player two is controlled using `LEFT, UPP, BOTTOM, RIGHT` for direction and `Right CTRL` for boost

In order to resize the window hold down `Left CTRL` and use mouse scrolling
In order to toggle fullscreen press `Left CTRL` and `Enter`
Toggle the Hud using `Left CTRL` and `H`

## Requirements

Java 8 which includes JavaFX

## Showcase videos
* [Gameplay Demo](https://www.youtube.com/watch?v=GdUSoCoz2_o)
* [AI Demo](https://www.youtube.com/watch?v=vg2k24SuX5k)
