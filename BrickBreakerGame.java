import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class BrickBreakerGame extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private int paddleX = 250;
    private int ballX = 300, ballY = 350, ballSpeedX = 2, ballSpeedY = -2;
    private boolean ballInMotion = false;
    private ArrayList<Rectangle> bricks;
    private int brickWidth = 50, brickHeight = 20;
    private int score = 0;
    private boolean gameRunning = true;

    public BrickBreakerGame() {
        bricks = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 5; j++) {
                bricks.add(new Rectangle(i * brickWidth + 50, j * brickHeight + 50, brickWidth, brickHeight));
            }
        }

        timer = new Timer(10, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw paddle
        g.setColor(Color.blue);
        g.fillRect(paddleX, 550, 100, 10);

        // Draw ball
        g.setColor(Color.red);
        g.fillOval(ballX, ballY, 20, 20);

        // Draw bricks
        for (Rectangle brick : bricks) {
            g.setColor(Color.green);
            g.fillRect(brick.x, brick.y, brickWidth, brickHeight);
        }

        // Draw score
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 30);

        if (!gameRunning) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", 200, 300);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameRunning) {
            if (ballInMotion) {
                ballX += ballSpeedX;
                ballY += ballSpeedY;

                // Ball collisions with walls
                if (ballX <= 0 || ballX >= 680) {
                    ballSpeedX = -ballSpeedX;
                }
                if (ballY <= 0) {
                    ballSpeedY = -ballSpeedY;
                }

                // Ball hits the paddle
                if (ballY >= 540 && ballX >= paddleX && ballX <= paddleX + 100) {
                    ballSpeedY = -ballSpeedY;
                }

                // Ball misses the paddle (game over)
                if (ballY >= 570) {
                    gameRunning = false;
                }

                // Ball collisions with bricks
                for (int i = 0; i < bricks.size(); i++) {
                    if (bricks.get(i).intersects(new Rectangle(ballX, ballY, 20, 20))) {
                        bricks.remove(i);
                        ballSpeedY = -ballSpeedY;
                        score += 10;
                        break;
                    }
                }

                if (bricks.isEmpty()) {
                    gameRunning = false;
                }
            }

            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT && paddleX > 0) {
            paddleX -= 20;
        }

        if (key == KeyEvent.VK_RIGHT && paddleX < 600) {
            paddleX += 20;
        }

        if (key == KeyEvent.VK_SPACE) {
            if (!ballInMotion) {
                ballInMotion = true;
                Random rand = new Random();
                int initialDirection = rand.nextInt(2);
                if (initialDirection == 0) {
                    ballSpeedX = -ballSpeedX;
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Brick Breaker");
        BrickBreakerGame game = new BrickBreakerGame();
        frame.add(game);
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
