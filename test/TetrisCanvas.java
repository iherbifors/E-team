package test;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

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
    private ArtStage stage;

    private boolean clear = false;
    private boolean clearDialogShown = false;

    public TetrisCanvas(ArtStage stage) {
        this.stage = stage;
        data = new TetrisData();

        try {
            backgroundImage = new ImageIcon(stage.getImagePath()).getImage();
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

    public ArtStage getStage() {
        return stage;
    }

    public void start() {
        stop = false;
        clear = false;
        clearDialogShown = false;
        makeNew = true;

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
                        clear = false;
                    } else {
                        data.removeLines();

                        if (isTargetScoreReached()) {
                            clearGame();
                        } else {
                            makeNew = true;
                        }
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

    private boolean isTargetScoreReached() {
        return data.getScore() >= stage.getTargetScore();
    }

    private void clearGame() {
        clear = true;
        stop = true;
        current = null;

        repaint();

        if (!clearDialogShown) {
            clearDialogShown = true;
            SwingUtilities.invokeLater(() -> showClearDialog());
        }
    }

    private void showClearDialog() {
        String[] options = {"설명 보기", "확인"};

        int choice = JOptionPane.showOptionDialog(
                this,
                "스테이지 클리어!\n그림이 완성되었습니다.",
                "CLEAR",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            showDescription();
        }
    }

    public void showDescription() {
        String message =
                "테마: " + stage.getTheme() + "\n" +
                "작품명: " + stage.getTitle() + "\n" +
                "작가: " + stage.getArtist() + "\n\n" +
                "작품 설명:\n" + stage.getDescription() + "\n\n" +
                "출처: " + stage.getSource();

        JOptionPane.showMessageDialog(
                this,
                message,
                "작품 설명",
                JOptionPane.INFORMATION_MESSAGE
        );

        requestFocus();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        if (backgroundImage != null) {
            g2.drawImage(
                    backgroundImage,
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    this
            );
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        int currentScore = data.getScore();

        double percent = (double) currentScore / stage.getTargetScore();

        if (percent > 1.0) {
            percent = 1.0;
        }

        int alpha = (int) ((1.0 - percent) * 255);

        g2.setColor(new Color(0, 0, 0, alpha));
        g2.fillRect(0, 0, getWidth(), getHeight());

        drawBoardBackground(g2);
        drawFixedBlocks(g2);
        drawCurrentBlock(g2);
        drawScoreBoard(g2, currentScore, percent);

        if (clear) {
            drawClearMessage(g2);
        } else if (stop) {
            drawGameOverMessage(g2);
        }
    }

    private void drawBoardBackground(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRect(
                margin,
                margin,
                TetrisData.COL * w,
                TetrisData.ROW * w
        );

        g2.setColor(new Color(255, 255, 255, 80));
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(
                margin,
                margin,
                TetrisData.COL * w,
                TetrisData.ROW * w
        );
    }

    private void drawFixedBlocks(Graphics2D g2) {
        g2.setStroke(new BasicStroke(3));

        for (int i = 0; i < TetrisData.ROW; i++) {
            for (int k = 0; k < TetrisData.COL; k++) {
                int value = data.getAt(i, k);

                if (value == 0) {
                    g2.setColor(new Color(120, 120, 120, 80));

                    g2.drawRect(
                            margin + k * w,
                            margin + i * w,
                            w,
                            w
                    );
                } else {
                    g2.setColor(colors[value]);

                    g2.drawRoundRect(
                            margin + k * w,
                            margin + i * w,
                            w,
                            w,
                            8,
                            8
                    );
                }
            }
        }
    }

    private void drawCurrentBlock(Graphics2D g2) {
        if (current == null) {
            return;
        }

        int x = current.getX();
        int y = current.getY();

        g2.setStroke(new BasicStroke(4));
        g2.setColor(colors[current.getType()]);

        for (int i = 0; i < 4; i++) {
            g2.drawRoundRect(
                    margin + (x + current.c[i]) * w,
                    margin + (y + current.r[i]) * w,
                    w,
                    w,
                    8,
                    8
            );
        }
    }

    private void drawScoreBoard(Graphics2D g2, int currentScore, double percent) {
        int uiX = margin + TetrisData.COL * w + 20;

        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRoundRect(uiX - 10, 25, 300, 230, 15, 15);

        g2.setColor(Color.WHITE);

        g2.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        g2.drawString("SCORE BOARD", uiX, 50);

        g2.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        g2.drawString("테마 : " + stage.getTheme(), uiX, 85);
        g2.drawString("작품 : " + stage.getTitle(), uiX, 110);
        g2.drawString("점수 : " + currentScore, uiX, 140);
        g2.drawString("목표 : " + stage.getTargetScore(), uiX, 165);
        g2.drawString("완성도 : " + (int)(percent * 100) + "%", uiX, 190);

        g2.drawRect(uiX, 215, 120, 20);

        g2.fillRect(
                uiX,
                215,
                (int)(120 * percent),
                20
        );
    }

    private void drawClearMessage(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("맑은 고딕", Font.BOLD, 34));

        g2.drawString(
                "CLEAR!",
                margin + 70,
                getHeight() / 2
        );
    }

    private void drawGameOverMessage(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.setFont(new Font("맑은 고딕", Font.BOLD, 30));

        g2.drawString(
                "GAME OVER",
                margin + 20,
                getHeight() / 2
        );
    }

    @Override
    public void update(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        if (width <= 0 || height <= 0) {
            return;
        }

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
        if (current == null || stop) {
            return;
        }

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
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}