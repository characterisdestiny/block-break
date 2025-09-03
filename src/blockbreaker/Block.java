package blockbreaker;

import java.awt.*;
import javax.swing.*;

public class Block {
    private int x, y;
    private final int width = 100;
    private final int height = 30;

    private static Image image;

    static {
        try {
            image = new ImageIcon(Block.class.getResource("/images/block.png")).getImage();
        } catch (Exception e) {
            System.out.println("이미지 로딩 실패");
        }
    }

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }



    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, width, height);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
        }
    }
}
