package test;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.ImageIcon;

public class TetrisCanvas extends Canvas implements Runnable, KeyListener {
    protected Thread worker;
    protected Color colors[];
    protected int w = 25;
    protected int margin = 20;
    protected int interval = 1000;
    protected boolean makeNew = true;
    protected boolean stop = false;
    protected Piece current;
    protected TetrisData data;

    private Image backgroundImage;
    private final int MAX_SCORE = 1000;

    public TetrisCanvas() {
        data = new TetrisData();

        String imagePath = "background.jpg";

        try {
            backgroundImage = new ImageIcon(imagePath).getImage();
        } catch (Exception e) {
            System.out.println("이미지를 불러오는데 실패했습니다.");
        }

        colors = new Color[8];

        colors[0] = new Color(120, 120, 120, 40);

        colors[1] = new Color(255, 80, 80, 130);
        colors[2] = new Color(80, 255, 80, 130);
        colors[3] = new Color(80, 220, 255, 130);
        colors[4] = new Color(255, 255, 80, 130);
        colors[5] = new Color(255, 80, 255, 130);
        colors[6] = new Color(255, 180, 80, 130);
        colors[7] = new Color(220, 220, 220, 130);

        addKeyListener(this);
    }

    public void start() {
        worker = new Thread(this);
        worker.start();
    }

    public void stop() {
        stop = true;
        current = null;
    }

    @Override
    public void run() {
        while (!stop) {

            if (makeNew) {
                Random r = new Random();
                int randomType = r.nextInt(3) + 1;

                switch (randomType) {
                    case 1:
                        current = new Bar(data);
                        break;
                    case 2:
                        current = new Tee(data);
                        break;
                    case 3:
                        current = new El(data);
                        break;
                    default:
                        current = new Bar(data);
                }

                makeNew = false;
            }

            if (current != null) {

                if (current.moveDown()) {

                    boolean gameOver = current.copy();

                    if (gameOver) {
                        System.out.println("Game Over!");
                        stop = true;
                    } else {
                        data.removeLines();
                        makeNew = true;
                    }
                }
            }

            repaint();

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        // 배경 이미지
        if (backgroundImage != null) {
            g2.drawImage(
                    backgroundImage,
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    this);
        }

        // 점수에 따라 밝기 증가
        int currentScore = data.getScore();

        double percent = (double) currentScore / MAX_SCORE;

        if (percent > 1.0)
            percent = 1.0;

        int alpha = (int) ((1.0 - percent) * 255);

        g2.setColor(new Color(0, 0, 0, alpha));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // 게임판 배경
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRect(
                margin,
                margin,
                TetrisData.COL * w,
                TetrisData.ROW * w);

        // 테두리 두께
        g2.setStroke(new BasicStroke(3));

        // 고정된 블록
        for (int i = 0; i < TetrisData.ROW; i++) {

            for (int k = 0; k < TetrisData.COL; k++) {

                int value = data.getAt(i, k);

                if (value == 0) {

                    g2.setColor(new Color(120, 120, 120, 80));

                    g2.drawRect(
                            margin + k * w,
                            margin + i * w,
                            w,
                            w);

                } else {

                    g2.setColor(colors[value]);

                    g2.drawRoundRect(
                            margin + k * w,
                            margin + i * w,
                            w,
                            w,
                            8,
                            8);
                }
            }
        }

        // 현재 떨어지는 블록
        if (current != null) {

            int x = current.getX();
            int y = current.getY();

            g2.setStroke(new BasicStroke(4));

            for (int i = 0; i < 4; i++) {

                g2.setColor(colors[current.getType()]);

                g2.drawRoundRect(
                        margin + (x + current.c[i]) * w,
                        margin + (y + current.r[i]) * w,
                        w,
                        w,
                        8,
                        8);
            }
        }

        // 우측 점수판
        int uiX = margin + TetrisData.COL * w + 20;

        g2.setColor(Color.WHITE);

        g2.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        g2.drawString("SCORE BOARD", uiX, 50);

        g2.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        g2.drawString("점수 : " + currentScore, uiX, 90);
        g2.drawString("밝기 : " + (int)(percent * 100) + "%", uiX, 120);

        // 진행도 바
        g2.drawRect(uiX, 150, 120, 20);

        g2.fillRect(
                uiX,
                150,
                (int)(120 * percent),
                20);

        // 게임 오버
        if (stop) {

            g2.setColor(Color.RED);

            g2.setFont(new Font("맑은 고딕", Font.BOLD, 30));

            g2.drawString(
                    "GAME OVER",
                    margin + 20,
                    getHeight() / 2);
        }
    }

    @Override
    public void update(Graphics g) {

        int width = getWidth();
        int height = getHeight();

        if (width <= 0 || height <= 0)
            return;

        Image buffer = createImage(width, height);

        if (buffer != null) {

            Graphics bg = buffer.getGraphics();

            paint(bg);

            g.drawImage(buffer, 0, 0, this);

        } else {
            paint(g);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (current == null || stop)
            return;

        switch (e.getKeyCode()) {

            case KeyEvent.VK_LEFT:
                current.moveLeft();
                break;

            case KeyEvent.VK_RIGHT:
                current.moveRight();
                break;

            case KeyEvent.VK_UP:
                current.rotate();
                break;

            case KeyEvent.VK_DOWN:
                current.moveDown();
                break;
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}