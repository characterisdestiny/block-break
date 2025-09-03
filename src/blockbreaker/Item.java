package blockbreaker;

import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Item {
    private int x, y;
    private final int size = 30;
    private final int dy = 2;
    private final ItemType type;
    private final Rectangle rect;

    private static Image imageSpeed = loadImage("/images/item_speed.png");
    private static Image imageBall = loadImage("/images/item_ball.png");

    public Item(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = randomType();
        this.rect = new Rectangle(x, y, size, size);
    }

    private static Image loadImage(String path) {
        java.net.URL url = Item.class.getResource(path);
        return (url != null) ? new ImageIcon(url).getImage() : null;
    }

    private ItemType randomType() {
        return new Random().nextBoolean() ? ItemType.SPEED_UP : ItemType.ADD_BALL;
    }

    public void move() {
        y += dy;
        rect.setLocation(x, y);
    }

    public void draw(Graphics g) {
        Image img = (type == ItemType.SPEED_UP) ? imageSpeed : imageBall;

        if (img != null) {
            g.drawImage(img, x, y, size, size, null);
        } else {
            g.setColor(type == ItemType.SPEED_UP ? Color.MAGENTA : Color.CYAN);
            g.fillOval(x, y, size, size);
        }
    }

    public Rectangle getRect() {
        return rect;
    }

    public int getY() {
        return y;
    }

    public ItemType getType() {
        return type;
    }
}
