# Game Design Documentation

## Visual Layout

The game window is 600x600 pixels with the following elements:

### Display Elements:
1. **Score Display** (top-left corner)
   - Shows current score
   - Shows current binary target and its decimal equivalent

2. **Grid** (optional, visible as dark gray lines)
   - 20x20 pixel unit grid
   - Helps visualize movement

3. **Snake**
   - Head: Bright green square
   - Body: Darker green squares
   - Starts with 3 segments
   - Grows by one segment each time a binary number is eaten

4. **Binary Number Targets**
   - Yellow squares with black text
   - Displays binary numbers (4-8 bits, e.g., "1011", "11001", "10101110")
   - Randomly positioned on the grid
   - New target appears after each successful eat

### Game Screen Layout:
```
+----------------------------------------------------------+
| Score: 5                                                  |
| Binary Target: 1011 (11)                                  |
|                                                           |
|   [Grid lines across the entire play area]               |
|                                                           |
|                      [1011]  <- Yellow target            |
|                                                           |
|                                                           |
|                  ■■■  <- Green snake (moving right)      |
|                                                           |
|                                                           |
+----------------------------------------------------------+
```

### Game Over Screen:
```
+----------------------------------------------------------+
|                                                           |
|                                                           |
|                     Game Over                             |
|                                                           |
|                      Score: 5                             |
|                                                           |
|                Press SPACE to restart                     |
|                                                           |
+----------------------------------------------------------+
```

## Color Scheme:
- Background: Black
- Snake Head: Bright Green (RGB: 0, 255, 0)
- Snake Body: Medium Green (RGB: 45, 180, 45)
- Binary Target: Yellow (RGB: 255, 255, 0)
- Binary Text: Black
- Score/Info Text: White
- Game Over Text: Red
- Grid Lines: Dark Gray

## Game Mechanics:

### Snake Movement:
- Moves continuously in the current direction
- Changes direction based on arrow key input
- Cannot reverse direction (e.g., can't go left if moving right)
- Speed: Updates every 100ms (DELAY constant)

### Binary Number Generation:
- Random length: 4-8 bits
- Random decimal value within the bit range
- Converted to binary string for display
- Examples: "1011" (11), "11001" (25), "10101110" (174)

### Collision Detection:
- Target collision: Snake head position matches target position
- Wall collision: Snake head goes outside game boundaries
- Self collision: Snake head touches any body segment

### Scoring:
- +1 point for each binary number eaten
- Snake length increases by 1 for each target
- Displayed in real-time at top of screen
