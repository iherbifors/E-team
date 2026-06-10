package test;
 
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Desktop;
import java.net.URI; 
 
public class TetrisCanvas extends JPanel implements Runnable, KeyListener {
    protected Thread worker;
    protected Color colors[];
    protected int w;               // 한 칸 크기(px) - Theme에서 가져옴
    protected int interval = 1000;
    protected boolean makeNew = true;
    protected boolean stop = false;
    protected Piece current;
    protected TetrisData data;
 
    private Image artImage;        // 액자 안에서 점수에 따라 드러나는 완성 그림(east1.jpg 등)
    private Image backgroundImage; // 전시실 배경(벽+바닥)
    private Image frameImage;      // 가운데가 투명한 액자 PNG
    private ArtStage stage;
    private Theme theme;
 
    // 보드를 화면에 그릴 좌상단 위치(Theme에서 가져옴)
    private int boardX;
    private int boardY;
 
    private boolean clear = false;
    private boolean clearDialogShown = false;
 
    public TetrisCanvas(ArtStage stage) {
        this.stage = stage;
        this.theme = Theme.fromThemeName(stage.getTheme());
        this.w = theme.getCellSize();
        this.boardX = theme.getBoardX();
        this.boardY = theme.getBoardY();
        data = new TetrisData();
 
        artImage = loadImage(stage.getImagePath());
        backgroundImage = loadImage(theme.getBackgroundPath());
        frameImage = loadImage(theme.getFramePath());
 
        colors = new Color[8];
        colors[0] = new Color(120, 120, 120, 40);
        colors[1] = new Color(255, 80, 80, 130);
        colors[2] = new Color(80, 255, 80, 130);
        colors[3] = new Color(80, 220, 255, 130);
        colors[4] = new Color(255, 255, 80, 130);
        colors[5] = new Color(255, 80, 255, 130);
        colors[6] = new Color(255, 180, 80, 130);
        colors[7] = new Color(220, 220, 220, 130);
 
        setFocusable(true);
        addKeyListener(this);
    }
 
    private Image loadImage(String path) {
        try {
            return new ImageIcon(path).getImage();
        } catch (Exception e) {
            System.out.println("이미지를 불러오는데 실패했습니다: " + path);
            return null;
        }
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
 
    private void updateSpeed() {
        if (data.getScore() >= 600) {
            interval = 400;
        } else if (data.getScore() >= 400) {
            interval = 600;
        } else if (data.getScore() >= 200) {
            interval = 800;
        } else {
            interval = 1000;
        }
    }
 
    @Override
    public void run() {
        while (!stop) {
 
            if (makeNew) {
                Random r = new Random();
                int randomType = r.nextInt(7) + 1;
 
                switch (randomType) {
                    case 1:  current = new Bar(data);    break; // I자
                    case 2:  current = new Tee(data);    break; // T자
                    case 3:  current = new El(data);     break; // L자
                    case 4:  current = new Square(data); break; // O자
                    case 5:  current = new Ess(data);    break; // S자
                    case 6:  current = new Zee(data);    break; // Z자
                    case 7:  current = new Jay(data);    break; // J자
                    default: current = new Bar(data);
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
                        updateSpeed();
 
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
        String source = stage.getSource();

        String message =
                "테마: " + stage.getTheme() + "\n" +
                "작품명: " + stage.getTitle() + "\n" +
                "작가: " + stage.getArtist() + "\n\n" +
                "작품 설명:\n" + stage.getDescription() + "\n\n" +
                "출처: " + source;

        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(420, 250));

        JButton sourceButton = new JButton("출처 열기");

        sourceButton.addActionListener(e -> {
            try {
                String url = source;

                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://" + url;
                }

                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "출처 링크를 열 수 없습니다.");
            }
        });

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(sourceButton, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(
                this,
                panel,
                "작품 설명",
                JOptionPane.INFORMATION_MESSAGE
        );

        requestFocusInWindow();
    }
    
 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
        int boardW = TetrisData.COL * w; // 250
        int boardH = TetrisData.ROW * w; // 500
 
        // 1. 배경(전시실) 그리기 - 화면 전체
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
 
        int currentScore = data.getScore();
        double percent = (double) currentScore / stage.getTargetScore();
        if (percent > 1.0) percent = 1.0;
 
        // 2. 보드 영역: 게임 중엔 어두운 바닥만(블록 놓인 칸에만 그림), 클리어 시 전체 그림
        drawArtReveal(g2, boardX, boardY, boardW, boardH);
 
        // 3. 보드 격자 + 블록
        if (!clear) {
            drawFixedBlocks(g2, boardX, boardY);
            drawCurrentBlock(g2, boardX, boardY);
        }
 
        // 4. 액자를 보드 구멍에 맞춰 덮기(맨 위)
        drawFrame(g2, boardX, boardY, boardW, boardH);
 
        // 5. 점수판(오른쪽 빈 공간)
        drawScoreBoard(g2, currentScore, percent);
 
        // 6. 상태 메시지
        if (clear) {
            drawClearMessage(g2);
        } else if (stop) {
            drawGameOverMessage(g2);
        }
    }
 
    // 보드 영역 처리:
    //  - 게임 중: 그림을 미리 깔지 않는다. (블록이 놓인 칸에만 drawArtBlock이 그림 조각을 그림)
    //    바닥은 어둡게 칠해서 빈 칸과 채워진 칸이 구분되게 한다.
    //  - 클리어: 완성된 전체 그림을 보여준다.
    private void drawArtReveal(Graphics2D g2, int bx, int by, int bw, int bh) {
        if (clear) {
            // 클리어 시 전체 그림 공개
            if (artImage != null) {
                g2.drawImage(artImage, bx, by, bw, bh, this);
            } else {
                g2.setColor(Color.DARK_GRAY);
                g2.fillRect(bx, by, bw, bh);
            }
        } else {
            // 게임 중: 어두운 바닥만 (그림은 블록이 놓인 칸에만 나타남)
            g2.setColor(new Color(0, 0, 0, 170));
            g2.fillRect(bx, by, bw, bh);
        }
    }
 
    private void drawFrame(Graphics2D g2, int bx, int by, int bw, int bh) {
        if (frameImage == null) return;
 
        double holeWRatio = theme.getHoleWidthRatio();
        double holeHRatio = theme.getHoleHeightRatio();
 
        // 액자 구멍이 보드(bw x bh)와 일치하도록 액자 전체 크기를 역산
        int frameW = (int) (bw / holeWRatio);
        int frameH = (int) (bh / holeHRatio);
 
        // 액자 그리는 시작 위치 = 보드 위치 - (구멍 시작 비율 * 액자 전체 크기)
        int fx = (int) (bx - theme.getHoleX0() * frameW);
        int fy = (int) (by - theme.getHoleY0() * frameH);
 
        g2.drawImage(frameImage, fx, fy, frameW, frameH, this);
    }
 
    private void drawFixedBlocks(Graphics2D g2, int bx, int by) {
        g2.setStroke(new BasicStroke(2));
 
        for (int i = 0; i < TetrisData.ROW; i++) {
            for (int k = 0; k < TetrisData.COL; k++) {
                int value = data.getAt(i, k);
 
                int px = bx + k * w;
                int py = by + i * w;
 
                if (value == 0) {
                    g2.setColor(new Color(255, 255, 255, 35));
                    g2.drawRect(px, py, w, w);
                } else {
                    drawArtBlock(g2, k, i, px, py);
                    g2.setColor(colors[value]);
                    g2.drawRoundRect(px, py, w, w, 8, 8);
                }
            }
        }
    }
 
    private void drawCurrentBlock(Graphics2D g2, int bx, int by) {
        if (current == null) return;
 
        int x = current.getX();
        int y = current.getY();
 
        g2.setStroke(new BasicStroke(3));
 
        for (int i = 0; i < 4; i++) {
            int blockX = x + current.c[i];
            int blockY = y + current.r[i];
 
            if (blockY < 0) continue;
 
            int px = bx + blockX * w;
            int py = by + blockY * w;
 
            drawArtBlock(g2, blockX, blockY, px, py);
            g2.setColor(colors[current.getType()]);
            g2.drawRoundRect(px, py, w, w, 8, 8);
        }
    }
 
    // 블록이 놓인 칸에 완성 그림의 해당 조각을 그려서, 블록이 쌓일수록 그림이 채워지는 느낌을 준다.
    private void drawArtBlock(Graphics2D g2, int boardCol, int boardRow, int px, int py) {
        if (artImage == null) {
            g2.setColor(new Color(180, 180, 180, 120));
            g2.fillRect(px, py, w, w);
            return;
        }
        int imgW = artImage.getWidth(this);
        int imgH = artImage.getHeight(this);
        if (imgW <= 0 || imgH <= 0) {
            g2.setColor(new Color(180, 180, 180, 120));
            g2.fillRect(px, py, w, w);
            return;
        }
 
        int sx1 = boardCol * imgW / TetrisData.COL;
        int sy1 = boardRow * imgH / TetrisData.ROW;
        int sx2 = (boardCol + 1) * imgW / TetrisData.COL;
        int sy2 = (boardRow + 1) * imgH / TetrisData.ROW;
 
        g2.drawImage(artImage, px, py, px + w, py + w, sx1, sy1, sx2, sy2, this);
    }
 
    private void drawScoreBoard(Graphics2D g2, int currentScore, double percent) {
        int boardW = TetrisData.COL * w;
        int boardH = TetrisData.ROW * w;
 
        // 액자 오른쪽 끝 위치를 계산해서 그 너머 빈 공간에 점수판을 둔다.
        int frameW = (int) (boardW / theme.getHoleWidthRatio());
        int frameX = (int) (boardX - theme.getHoleX0() * frameW);
        int frameRight = frameX + frameW;
 
        int uiX = frameRight + 30;
        int uiY = 70;
 
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(uiX - 15, uiY - 25, 240, 215, 15, 15);
 
        g2.setColor(Color.WHITE);
 
        g2.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        g2.drawString("SCORE BOARD", uiX, uiY);
 
        g2.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        g2.drawString("테마 : " + stage.getTheme(), uiX, uiY + 35);
        g2.drawString("작품 : " + stage.getTitle(), uiX, uiY + 60);
        g2.drawString("점수 : " + currentScore, uiX, uiY + 90);
        g2.drawString("목표 : " + stage.getTargetScore(), uiX, uiY + 115);
        g2.drawString("완성도 : " + (int) (percent * 100) + "%", uiX, uiY + 140);
 
        g2.drawRect(uiX, uiY + 158, 195, 18);
        g2.fillRect(uiX, uiY + 158, (int) (195 * percent), 18);
    }
 
    private void drawClearMessage(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("맑은 고딕", Font.BOLD, 34));
        g2.drawString("CLEAR!", boardX + 60, boardY + (TetrisData.ROW * w) / 2);
    }
 
    private void drawGameOverMessage(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.setFont(new Font("맑은 고딕", Font.BOLD, 30));
        g2.drawString("GAME OVER", boardX + 30, boardY + (TetrisData.ROW * w) / 2);
    }
 
    @Override
    public void keyPressed(KeyEvent e) {
        if (current == null || stop) return;
 
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:  current.moveLeft();  break;
            case KeyEvent.VK_RIGHT: current.moveRight(); break;
            case KeyEvent.VK_UP:    current.rotate();    break;
            case KeyEvent.VK_DOWN:  current.moveDown();  break;
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
 