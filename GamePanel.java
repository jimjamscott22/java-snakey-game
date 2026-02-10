import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    private final int[] SPEED_DELAYS = {160, 130, 100, 80, 60};
    private final int DEFAULT_SPEED_LEVEL = 3;
    private final int TARGET_PULSE_FRAMES = 10;
    
    private ArrayList<Integer> snakeX = new ArrayList<>();
    private ArrayList<Integer> snakeY = new ArrayList<>();
    private int bodyParts = 3;
    
    private int targetX;
    private int targetY;
    private String binaryNumber;
    private int targetDecimal;
    private int targetPulseFrames = 0;
    
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private Random random;
    private int score = 0;
    private int speedLevel = DEFAULT_SPEED_LEVEL;

    public GamePanel() {
        random = new Random();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        if (timer != null) {
            timer.stop();
        }

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
        timer = new Timer(getCurrentDelay(), this);
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
            
            drawTarget(g);
            
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
            g.drawString("Binary Target: " + binaryNumber + " (" + targetDecimal + ")", 10, 55);
            g.drawString("Speed: " + speedLevel + " (1-5)", 10, 80);
        } else {
            gameOver(g);
        }
    }

    public void newBinaryTarget() {
        // Generate random binary number (4-8 bits)
        int numBits = random.nextInt(5) + 4; // 4 to 8 bits
        targetDecimal = random.nextInt(1 << numBits);
        binaryNumber = String.format("%" + numBits + "s", Integer.toBinaryString(targetDecimal)).replace(' ', '0');

        do {
            targetX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
            targetY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        } while (isOnSnake(targetX, targetY));

        targetPulseFrames = TARGET_PULSE_FRAMES;
    }

    private boolean isOnSnake(int x, int y) {
        for (int i = 0; i < bodyParts; i++) {
            if (snakeX.get(i) == x && snakeY.get(i) == y) {
                return true;
            }
        }
        return false;
    }

    private void drawTarget(Graphics g) {
        // Keep the grid cell as the pickup and render readable text beside it.
        g.setColor(Color.YELLOW);
        g.fillRect(targetX, targetY, UNIT_SIZE, UNIT_SIZE);
        g.setColor(Color.BLACK);
        g.fillOval(targetX + 6, targetY + 6, 8, 8);

        String label = binaryNumber + " (" + targetDecimal + ")";
        g.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics metrics = getFontMetrics(g.getFont());
        int padding = 6;
        int labelWidth = metrics.stringWidth(label) + (padding * 2);
        int labelHeight = metrics.getHeight() + (padding * 2);

        int labelX = targetX + UNIT_SIZE + 6;
        int labelY = targetY - labelHeight - 4;

        if (labelX + labelWidth > WIDTH) {
            labelX = targetX - labelWidth - 6;
        }
        if (labelX < 0) {
            labelX = 0;
        }
        if (labelY < 0) {
            labelY = targetY + UNIT_SIZE + 4;
        }
        if (labelY + labelHeight > HEIGHT) {
            labelY = HEIGHT - labelHeight;
        }

        if (targetPulseFrames > 0) {
            Graphics2D g2 = (Graphics2D) g.create();
            float progress = (float) targetPulseFrames / TARGET_PULSE_FRAMES;
            int pulsePadding = 2 + (int) ((1.0f - progress) * 12.0f);
            float alpha = 0.08f + (0.30f * progress);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setColor(Color.YELLOW);
            g2.fillRoundRect(
                labelX - pulsePadding,
                labelY - pulsePadding,
                labelWidth + pulsePadding * 2,
                labelHeight + pulsePadding * 2,
                12,
                12
            );
            g2.dispose();
        }

        g.setColor(Color.YELLOW);
        g.fillRoundRect(labelX, labelY, labelWidth, labelHeight, 8, 8);
        g.setColor(Color.BLACK);
        g.drawRoundRect(labelX, labelY, labelWidth, labelHeight, 8, 8);
        g.drawString(label, labelX + padding, labelY + padding + metrics.getAscent());
    }

    private int getCurrentDelay() {
        return SPEED_DELAYS[speedLevel - 1];
    }

    private void setSpeedLevel(int newSpeedLevel) {
        if (newSpeedLevel < 1 || newSpeedLevel > 5) {
            return;
        }
        speedLevel = newSpeedLevel;
        if (timer != null) {
            int delay = getCurrentDelay();
            timer.setDelay(delay);
            timer.setInitialDelay(delay);
        }
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
        g.drawString("Press 1-5 to set speed (current: " + speedLevel + ")", (WIDTH - metrics3.stringWidth("Press 1-5 to set speed (current: " + speedLevel + ")")) / 2, HEIGHT / 2 + 130);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkTarget();
            checkCollisions();
        }
        if (targetPulseFrames > 0) {
            targetPulseFrames--;
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
                case KeyEvent.VK_1:
                case KeyEvent.VK_NUMPAD1:
                    setSpeedLevel(1);
                    break;
                case KeyEvent.VK_2:
                case KeyEvent.VK_NUMPAD2:
                    setSpeedLevel(2);
                    break;
                case KeyEvent.VK_3:
                case KeyEvent.VK_NUMPAD3:
                    setSpeedLevel(3);
                    break;
                case KeyEvent.VK_4:
                case KeyEvent.VK_NUMPAD4:
                    setSpeedLevel(4);
                    break;
                case KeyEvent.VK_5:
                case KeyEvent.VK_NUMPAD5:
                    setSpeedLevel(5);
                    break;
            }
        }
    }
}
