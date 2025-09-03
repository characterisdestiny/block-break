package blockbreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;

    private Timer timer;
    private ArrayList<Ball> balls;
    private Paddle paddle;
    private ArrayList<Block> blocks;
    private ArrayList<Item> items;

    private boolean inGame = false;
    private boolean isGameOver = false;
    private boolean isGameClear = false;
    private int score = 0;

    private final int blockWidth = 100;
    private final int blockHeight = 30;

    private Image background;
    private JButton startButton, quitButton, backToMenuButton, restartButton;

    private Music bgm;

    public GamePanel() {
        setLayout(null);
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(10, this);
        balls = new ArrayList<>();
        paddle = new Paddle(565, 690);
        blocks = new ArrayList<>();
        items = new ArrayList<>();

        initBlocks();

        try {
            background = new ImageIcon(getClass().getResource("/images/background.png")).getImage();
        } catch (Exception e) {
            System.out.println("❌ 배경 이미지 로딩 실패");
        }

        bgm = new Music("introbackground.mp3", true);
        bgm.start();

        createButtons();
    }

    private void createButtons() {
        startButton = createButton("시작하기", 540, 580);
        quitButton = createButton("종료하기", 540, 640);
        backToMenuButton = createButton("처음 화면", 460, 400);
        restartButton = createButton("재시작", 630, 400);

        backToMenuButton.setVisible(false);
        restartButton.setVisible(false);

        startButton.addActionListener(e -> startGame());
        quitButton.addActionListener(e -> System.exit(0));
        restartButton.addActionListener(e -> startGame());
        backToMenuButton.addActionListener(e -> returnToMenu());
    }

    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 150, 50);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        button.setFocusPainted(false);
        add(button);
        return button;
    }

    private void returnToMenu() {
        isGameOver = false;
        isGameClear = false;
        inGame = false;

        if (bgm != null) bgm.close();
        bgm = new Music("introbackground.mp3", true);
        bgm.start();

        restartButton.setVisible(false);
        backToMenuButton.setVisible(false);
        startButton.setVisible(true);
        quitButton.setVisible(true);
        repaint();
    }

    private void initializeBall(int dx, int dy) {
        balls.clear();
        int startX = paddle.getX() + paddle.getWidth() / 2;
        int startY = paddle.getY() - 20;
        Ball ball = new Ball(startX, startY);
        ball.setSpeed(dx, dy);
        balls.add(ball);
    }

    private void startGame() {
        if (bgm != null) bgm.close();

        paddle = new Paddle(565, 690);
        score = 0;
        isGameOver = false;
        isGameClear = false;
        inGame = true;
        timer.start();
        initBlocks();
        balls.clear();
        items.clear();
        initializeBall(4, -4);

        startButton.setVisible(false);
        quitButton.setVisible(false);
        backToMenuButton.setVisible(false);
        restartButton.setVisible(false);

        requestFocusInWindow();
    }

    private void initBlocks() {
        blocks.clear();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                blocks.add(new Block(80 + i * (blockWidth + 10), 50 + j * (blockHeight + 10)));
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!inGame && !isGameOver && !isGameClear) {
            g.drawImage(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);
        } else if (inGame) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            paddle.draw(g);
            for (Ball ball : balls) ball.draw(g);
            for (Block b : blocks) b.draw(g);
            for (Item item : items) item.draw(g);

            g.setFont(new Font("맑은 고딕", Font.BOLD, 24));
            g.setColor(Color.WHITE);
            g.drawString("점수: " + score, 80, 40);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            g.setFont(new Font("맑은 고딕", Font.BOLD, 50));
            g.setColor(isGameOver ? Color.RED : Color.YELLOW);
            g.drawString(isGameOver ? "GAME OVER!" : "GAME CLEAR!", 460, 300);

            g.setFont(new Font("맑은 고딕", Font.BOLD, 30));
            g.setColor(Color.WHITE);
            g.drawString("점수: " + score, 550, 370);

            restartButton.setVisible(true);
            backToMenuButton.setVisible(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!inGame) return;

        for (Ball ball : balls) {
            ball.move();
            ball.checkWallCollision();
            if (ball.getRect().intersects(paddle.getRect())) {
                ball.checkPaddleCollision(paddle);
                Sound.play("sound/BlockBreaker_src_sounds_attack.wav"); // ✅ 패들과 충돌 효과음
            }
        }

        for (int i = blocks.size() - 1; i >= 0; i--) {
            Block block = blocks.get(i);
            for (Ball ball : balls) {
                if (ball.getRect().intersects(block.getRect())) {
                    ball.reverseY();
                    Sound.play("sound/BlockBreaker_src_sounds_break.wav"); // ✅ 벽돌 깨짐 효과음

                    if (Math.random() < 0.2) {
                        items.add(new Item(block.getX() + 35, block.getY()));
                    }

                    blocks.remove(i);
                    score += 100;
                    break;
                }
            }
        }

        for (Item item : items) item.move();

        for (int i = items.size() - 1; i >= 0; i--) {
            Item item = items.get(i);
            if (item.getRect().intersects(paddle.getRect())) {
                applyItemEffect(item);
                items.remove(i);
            } else if (item.getY() > SCREEN_HEIGHT) {
                items.remove(i);
            }
        }

        if (blocks.isEmpty()) {
            inGame = false;
            isGameClear = true;
            timer.stop();
        }

        boolean allBallsLost = balls.stream().allMatch(ball -> ball.getY() >= SCREEN_HEIGHT);
        if (allBallsLost) {
            inGame = false;
            isGameOver = true;
            timer.stop();
        }

        repaint();
    }

    private void applyItemEffect(Item item) {
        Sound.play("sound/item_sound.wav"); // ✅ 아이템 획득 효과음

        if (item.getType() == ItemType.SPEED_UP) {
            for (Ball ball : balls) {
                int dx = ball.getDx();
                int dy = ball.getDy();
                dx = dx > 0 ? Math.min(dx + 1, 10) : Math.max(dx - 1, -10);
                dy = dy > 0 ? Math.min(dy + 1, 10) : Math.max(dy - 1, -10);
                ball.setSpeed(dx, dy);
            }
        } else if (item.getType() == ItemType.ADD_BALL) {
            Ball base = null;
            for (Ball b : balls) {
                if (b.getY() < SCREEN_HEIGHT) {
                    base = b;
                    break;
                }
            }
            if (base != null) {
                Ball newBall = new Ball(base.getX(), base.getY());
                newBall.setSpeed(-base.getDx(), base.getDy());
                balls.add(newBall);
            }
        }
    }

    @Override public void keyPressed(KeyEvent e) {
        if (inGame) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) paddle.moveLeft();
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) paddle.moveRight();
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
