# java-snakey-game
A simple graphical game in Java where the user controls a snake trying to eat numbers. But, the numbers are sequences of binary which the snake has to hunt down, while slithering around on the screen.

## Features
- Classic snake gameplay with a twist - hunt down binary numbers!
- Binary numbers are displayed on yellow targets (showing both binary and decimal representations)
- Snake grows each time it eats a binary number
- Score tracking
- Game over detection (collision with walls or self)
- Restart functionality

## How to Play
1. **Compile the game:**
   ```bash
   javac SnakeGame.java GamePanel.java
   ```

2. **Run the game:**
   ```bash
   java SnakeGame
   ```

3. **Controls:**
   - Arrow Keys: Control snake direction (Up, Down, Left, Right)
   - Space: Restart game after game over

## Game Rules
- Use arrow keys to move the snake around the screen
- The snake will continuously move in the current direction
- Eat the yellow squares containing binary numbers to score points
- Each binary number eaten makes the snake grow longer
- The game displays both the binary representation and its decimal value
- Avoid hitting the walls or the snake's own body
- Try to get the highest score possible!

## Requirements
- Java Development Kit (JDK) 8 or higher
- Java Runtime Environment (JRE) with GUI support
