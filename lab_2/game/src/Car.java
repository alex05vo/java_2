import java.awt.*;

public class Car {
    int x, y, width, height;
    int xVelocity = 0;
    Color color;

    public Car(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void move() {
        x += xVelocity;
        if (x < GamePanel.BORDER + 5) x = GamePanel.BORDER + 5;
        if (x + width > GamePanel.BORDER + GamePanel.ROAD_WIDTH - 5)
            x = GamePanel.BORDER + GamePanel.ROAD_WIDTH - width - 5;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRoundRect(x, y, width, height, 10, 10);

        // фари
        g.setColor(Color.yellow);
        g.fillRect(x + 10, y + 5, 10, 5);
        g.fillRect(x + width - 20, y + 5, 10, 5);

        // колеса
        g.setColor(Color.black);
        g.fillRect(x + 5, y + 10, 10, 10);
        g.fillRect(x + width - 15, y + 10, 10, 10);
        g.fillRect(x + 5, y + height - 20, 10, 10);
        g.fillRect(x + width - 15, y + height - 20, 10, 10);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}