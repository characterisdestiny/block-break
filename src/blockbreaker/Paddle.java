package blockbreaker;

import java.awt.*;

public class Paddle {
    private static final int SCREEN_WIDTH = 1280;

    private int x, y;
    private final int width = 150;
    private final int height = 20;
    private final int speed = 100;

    public Paddle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveLeft() {
        x = Math.max(0, x - speed);
    }

    public void moveRight() {
        x = Math.min(SCREEN_WIDTH - width, x + speed);
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
}
