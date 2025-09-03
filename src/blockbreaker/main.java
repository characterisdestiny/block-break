package blockbreaker;

import javax.swing.*;

public class main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("벽돌깨기 게임 🎮");
        frame.setSize(1280, 760);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GamePanel panel = new GamePanel();
        frame.add(panel);

        frame.setVisible(true);
    }
}