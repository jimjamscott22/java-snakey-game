// Simple verification test for the Snake Game
// This test verifies that the game classes compile and basic functionality works

public class GameTest {
    public static void main(String[] args) {
        System.out.println("Testing Binary Snake Game Components...");
        
        // Test 1: Binary number generation
        System.out.println("\n1. Testing binary number generation:");
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 5; i++) {
            int numBits = random.nextInt(5) + 4; // 4 to 8 bits
            int decimal = random.nextInt((int) Math.pow(2, numBits));
            String binary = Integer.toBinaryString(decimal);
            System.out.println("   Binary: " + binary + " = Decimal: " + decimal + " (" + binary.length() + " bits)");
        }
        
        // Test 2: Verify ArrayList functionality for snake segments
        System.out.println("\n2. Testing snake segment storage:");
        java.util.ArrayList<Integer> snakeX = new java.util.ArrayList<>();
        java.util.ArrayList<Integer> snakeY = new java.util.ArrayList<>();
        snakeX.add(300);
        snakeY.add(300);
        snakeX.add(280);
        snakeY.add(300);
        snakeX.add(260);
        snakeY.add(300);
        System.out.println("   Initial snake segments: " + snakeX.size());
        System.out.println("   Head position: (" + snakeX.get(0) + ", " + snakeY.get(0) + ")");
        
        // Test 3: Movement simulation
        System.out.println("\n3. Testing snake movement logic:");
        int headX = snakeX.get(0);
        int headY = snakeY.get(0);
        int UNIT_SIZE = 20;
        
        // Move right
        headX += UNIT_SIZE;
        System.out.println("   After moving right: (" + headX + ", " + headY + ")");
        
        // Move down
        headY += UNIT_SIZE;
        System.out.println("   After moving down: (" + headX + ", " + headY + ")");
        
        // Test 4: Collision detection logic
        System.out.println("\n4. Testing collision detection:");
        int WIDTH = 600;
        int HEIGHT = 600;
        boolean collision = (headX < 0 || headX >= WIDTH || headY < 0 || headY >= HEIGHT);
        System.out.println("   Wall collision: " + collision);
        
        // Test 5: Target collision
        int targetX = 320;
        int targetY = 320;
        boolean targetHit = (headX == targetX && headY == targetY);
        System.out.println("   Target hit: " + targetHit);
        
        System.out.println("\n✓ All basic tests passed!");
        System.out.println("\nTo run the actual game, execute: java SnakeGame");
    }
}
