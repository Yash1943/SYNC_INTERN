import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame extends JFrame implements KeyListener {
    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 400;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 175;

    private final List<Integer> snakeX = new ArrayList<>();
    private final List<Integer> snakeY = new ArrayList<>();
    private int foodX;
    private int foodY;
    private Direction direction = Direction.RIGHT;
    private boolean isRunning = false;
    private Timer timer;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        addKeyListener(this);
        startGame();
    }

    private void startGame() {
        snakeX.clear();
        snakeY.clear();
        snakeX.add(SCREEN_WIDTH / 2);
        snakeY.add(SCREEN_HEIGHT / 2);
        generateFood();

        isRunning = true;
        timer = new Timer(DELAY, e -> gameLoop());
        timer.start();
    }

    private void gameLoop() {
        if (isRunning) {
            move();
            checkFoodCollision();
            checkBoundaryCollision();
            checkSelfCollision();
            repaint();
        }
    }

    private void move() {
        for (int i = snakeX.size() - 1; i > 0; i--) {
            snakeX.set(i, snakeX.get(i - 1));
            snakeY.set(i, snakeY.get(i - 1));
        }

        switch (direction) {
            case UP:
                snakeY.set(0, snakeY.get(0) - UNIT_SIZE);
                break;
            case DOWN:
                snakeY.set(0, snakeY.get(0) + UNIT_SIZE);
                break;
            case LEFT:
                snakeX.set(0, snakeX.get(0) - UNIT_SIZE);
                break;
            case RIGHT:
                snakeX.set(0, snakeX.get(0) + UNIT_SIZE);
                break;
        }
    }

    private void checkFoodCollision() {
        if (snakeX.get(0) == foodX && snakeY.get(0) == foodY) {
            snakeX.add(foodX);
            snakeY.add(foodY);
            generateFood();
        }
    }

    private void checkBoundaryCollision() {
        if (snakeX.get(0) < 0 || snakeX.get(0) >= SCREEN_WIDTH
                || snakeY.get(0) < 0 || snakeY.get(0) >= SCREEN_HEIGHT) {
            gameOver();
        }
    }

    private void checkSelfCollision() {
        for (int i = 1; i < snakeX.size(); i++) {
            if (snakeX.get(i) == snakeX.get(0) && snakeY.get(i) == snakeY.get(0)) {
                gameOver();
            }
        }
    }

    private void generateFood() {
        Random random = new Random();
        foodX = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        foodY = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    private void gameOver() {
        isRunning = false;
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over!", "Snake Game", JOptionPane.PLAIN_MESSAGE);
        startGame();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (isRunning) {
            // Draw snake
            for (int i = 0; i < snakeX.size(); i++) {
                g.setColor(Color.GREEN);
                g.fillRect(snakeX.get(i), snakeY.get(i), UNIT_SIZE, UNIT_SIZE);
            }

            // Draw food
            g.setColor(Color.RED);
            g.fillRect(foodX, foodY, UNIT_SIZE, UNIT_SIZE);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP && direction != Direction.DOWN) {
            direction = Direction.UP;
        } else if (key == KeyEvent.VK_DOWN && direction != Direction.UP) {
            direction = Direction.DOWN;
        } else if (key == KeyEvent.VK_LEFT && direction != Direction.RIGHT) {
            direction = Direction.LEFT;
        } else if (key == KeyEvent.VK_RIGHT && direction != Direction.LEFT) {
            direction = Direction.RIGHT;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}
