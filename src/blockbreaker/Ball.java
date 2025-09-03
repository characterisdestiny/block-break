package blockbreaker;

import java.awt.*;

public class Ball {
    private static final int SCREEN_WIDTH = 1280;

    private int x, y;
    private int diameter = 20;
    private int dx = 4, dy = -4;

    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSpeed(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getDx() { return dx; }
    public int getDy() { return dy; }

    public void move() {
        x += dx;
        y += dy;
    }

    public void reverseX() {
        dx = -dx;
    }

    public void reverseY() {
        dy = -dy;
    }

    public void checkWallCollision() {
        if (x <= 0 || x + diameter >= SCREEN_WIDTH) {
            reverseX();
        }
        if (y <= 0) {
            reverseY();
        }
    }

    public void checkPaddleCollision(Paddle paddle) {
        if (getRect().intersects(paddle.getRect())) {
            reverseY();
            y = paddle.getY() - diameter;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, diameter, diameter);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, diameter, diameter);
    }
}
