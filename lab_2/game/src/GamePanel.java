import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    static final int SCREEN_WIDTH = 400;
    static final int SCREEN_HEIGHT = 600;
    static final int ROAD_WIDTH = 300;
    static final int LANE_COUNT = 3;
    static final int LANE_WIDTH = ROAD_WIDTH / LANE_COUNT;
    static final int BORDER = (SCREEN_WIDTH - ROAD_WIDTH) / 2;

    Timer timer;
    Car player;
    ArrayList<Car> enemies;
    boolean running = false;
    int lives = 3;
    int score = 0;
    int roadOffset = 0;
    Random random = new Random();

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.darkGray);
        this.setFocusable(true);
        this.addKeyListener(this);
        startGame();
    }

    public void startGame() {
        player = new Car(SCREEN_WIDTH / 2 - 25, SCREEN_HEIGHT - 120, 50, 80, Color.cyan);
        enemies = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            addEnemy(-i * 200);
        }

        score = 0;
        lives = 3;
        running = true;
        timer = new Timer(15, this);
        timer.start();
    }

    private void addEnemy(int yOffset) {
        int lane = random.nextInt(LANE_COUNT);
        Color color = new Color(random.nextInt(200) + 30, random.nextInt(200) + 30, random.nextInt(200) + 30);
        enemies.add(new Car(BORDER + lane * LANE_WIDTH + 10, yOffset, 50, 80, color));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void drawRoad(Graphics g) {
        // Сірий асфальт
        g.setColor(Color.gray);
        g.fillRect(BORDER, 0, ROAD_WIDTH, SCREEN_HEIGHT);

        // Білий бордюр
        g.setColor(Color.white);
        g.fillRect(BORDER - 10, 0, 10, SCREEN_HEIGHT);
        g.fillRect(BORDER + ROAD_WIDTH, 0, 10, SCREEN_HEIGHT);

        // Центральні розділові смуги (рухаються)
        g.setColor(Color.white);
        for (int y = roadOffset; y < SCREEN_HEIGHT; y += 60) {
            for (int i = 1; i < LANE_COUNT; i++) {
                int lineX = BORDER + i * LANE_WIDTH;
                g.fillRect(lineX - 2, y, 4, 30);
            }
        }
    }

    public void draw(Graphics g) {
        if (running) {
            drawRoad(g);

            // Score + Lives
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Score: " + score, 10, 20);
            g.drawString("Lives: " + lives, SCREEN_WIDTH - 100, 20);

            // Cars
            player.draw(g);
            for (Car e : enemies) e.draw(g);
        } else {
            gameOverScreen(g);
        }
    }

    public void move() {
        player.move();
        roadOffset += 5;
        if (roadOffset >= 60) roadOffset = 0;

        for (int i = 0; i < enemies.size(); i++) {
            Car enemy = enemies.get(i);
            enemy.y += 6;

            // якщо ворог вийшов за екран — новий
            if (enemy.y > SCREEN_HEIGHT) {
                enemies.remove(i);
                addEnemy(-random.nextInt(200) - 100);
                score++;
            }

            // зіткнення
            if (enemy.getBounds().intersects(player.getBounds())) {
                lives--;
                enemies.remove(i);
                addEnemy(-200);

                if (lives <= 0) {
                    running = false;
                    timer.stop();
                }
            }
        }
    }

    public void gameOverScreen(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 2);

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Score: " + score, SCREEN_WIDTH / 2 - 30, SCREEN_HEIGHT / 2 + 40);
        g.drawString("Press R to Restart", SCREEN_WIDTH / 2 - 70, SCREEN_HEIGHT / 2 + 80);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (running) {
            switch (key) {
                case KeyEvent.VK_LEFT -> player.xVelocity = -6;
                case KeyEvent.VK_RIGHT -> player.xVelocity = 6;
            }
        } else if (key == KeyEvent.VK_R) {
            startGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            player.xVelocity = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}