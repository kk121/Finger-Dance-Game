# Finger-Dance-Game
# How to Run
- Clone or download this repository
- Import project in Android Studio
- Run the app

# Design/Architecture of the game
- TileView is a Custom View:
  Each Tile contains Player information,row and column numbers in Game board. Tile overrides onTouchEvent() method and sends events to registered listener for TouchUp and TouchDown event along with it's reference and player.
- Player class:
  Player class contains player informations like: name,score,currently selected row and column along with previously selected tileView.
- GameBoard is a GridLayout:
  GridLayout is a 2-D grid of TileView.
- Game Flow:
  - On start of the game ask user for number of Rows or Column (N)
  - Create N*N number of tiles and add it to GridLayout
  - Start with player 1. highlight one tile, which is not selected yet,randomly for Player 1.
  - If he touch the highlighted Tile then randomly highlight next tile for player 2. If Player 1 touches tile other than highlighted then he lose.
  - Game gets over if Player touches the screen with more fingers than maximum touch allowed for that device for one player.
