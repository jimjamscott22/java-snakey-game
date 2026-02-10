import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int UNIT_SIZE = 20;
    private final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private final int DELAY = 100;
    
    private ArrayList<Integer> snakeX = new ArrayList<>();
    private ArrayList<Integer> snakeY = new ArrayList<>();
    private int bodyParts = 3;
    
    private int targetX;
    private int targetY;
    private String binaryNumber;
    
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private Random random;
    private int score = 0;

    public GamePanel() {
        random = new Random();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        snakeX.clear();
        snakeY.clear();
        bodyParts = 3;
        score = 0;
        direction = 'R';
        
        // Initialize snake in the middle
        for (int i = 0; i < bodyParts; i++) {
            snakeX.add(WIDTH / 2 - i * UNIT_SIZE);
            snakeY.add(HEIGHT / 2);
        }
        
        newBinaryTarget();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // Draw grid (optional)
            g.setColor(Color.DARK_GRAY);
            for (int i = 0; i < WIDTH / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, WIDTH, i * UNIT_SIZE);
            }
            
            // Draw binary number target
            g.setColor(Color.YELLOW);
            g.fillRect(targetX, targetY, UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString(binaryNumber, 
                targetX + (UNIT_SIZE - metrics.stringWidth(binaryNumber)) / 2,
                targetY + ((UNIT_SIZE - metrics.getHeight()) / 2) + metrics.getAscent());
            
            // Draw snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(new Color(45, 180, 45));
                }
                g.fillRect(snakeX.get(i), snakeY.get(i), UNIT_SIZE, UNIT_SIZE);
            }
            
            // Draw score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 10, 30);
            g.drawString("Binary Target: " + binaryNumber + " (" + Integer.parseInt(binaryNumber, 2) + ")", 10, 55);
        } else {
            gameOver(g);
        }
    }

    public void newBinaryTarget() {
        // Generate random binary number (4-8 bits)
        int numBits = random.nextInt(5) + 4; // 4 to 8 bits
        int decimal = random.nextInt((int) Math.pow(2, numBits));
        binaryNumber = Integer.toBinaryString(decimal);
        
        targetX = random.nextInt((int) (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        targetY = random.nextInt((int) (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        // Move body
        for (int i = bodyParts - 1; i > 0; i--) {
            snakeX.set(i, snakeX.get(i - 1));
            snakeY.set(i, snakeY.get(i - 1));
        }
        
        // Move head
        switch (direction) {
            case 'U':
                snakeY.set(0, snakeY.get(0) - UNIT_SIZE);
                break;
            case 'D':
                snakeY.set(0, snakeY.get(0) + UNIT_SIZE);
                break;
            case 'L':
                snakeX.set(0, snakeX.get(0) - UNIT_SIZE);
                break;
            case 'R':
                snakeX.set(0, snakeX.get(0) + UNIT_SIZE);
                break;
        }
    }

    public void checkTarget() {
        if ((snakeX.get(0) == targetX) && (snakeY.get(0) == targetY)) {
            bodyParts++;
            snakeX.add(snakeX.get(bodyParts - 2));
            snakeY.add(snakeY.get(bodyParts - 2));
            score++;
            newBinaryTarget();
        }
    }

    public void checkCollisions() {
        // Check if head collides with body
        for (int i = bodyParts - 1; i > 0; i--) {
            if ((snakeX.get(0).equals(snakeX.get(i))) && (snakeY.get(0).equals(snakeY.get(i)))) {
                running = false;
            }
        }
        
        // Check if head touches borders
        if (snakeX.get(0) < 0 || snakeX.get(0) >= WIDTH || 
            snakeY.get(0) < 0 || snakeY.get(0) >= HEIGHT) {
            running = false;
        }
        
        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // Display game over text
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - metrics1.stringWidth("Game Over")) / 2, HEIGHT / 2);
        
        // Display score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + score, (WIDTH - metrics2.stringWidth("Score: " + score)) / 2, HEIGHT / 2 + 50);
        
        // Display restart instruction
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press SPACE to restart", (WIDTH - metrics3.stringWidth("Press SPACE to restart")) / 2, HEIGHT / 2 + 100);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkTarget();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (!running) {
                        startGame();
                    }
                    break;
            }
        }
    }
}
