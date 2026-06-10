package test;
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
// 첫 화면: 미술관 배경 위에 "시작하기" 글자가 서서히 떠오르고, 클릭하면 onStart 실행.
public class StartPanel extends JPanel {
    private Image background;
    private float titleAlpha = 0f;     // 0 -> 1 페이드인
    private Timer fadeTimer;
    private Runnable onStart;
 
    public StartPanel(Runnable onStart) {
        this.onStart = onStart;
        background = loadImage("resources/images/start_bg.jpg");
 
        setLayout(null);
        setBackground(Color.BLACK);
 
        // 글자 페이드인 애니메이션
        fadeTimer = new Timer(40, e -> {
            titleAlpha += 0.03f;
            if (titleAlpha >= 1f) {
                titleAlpha = 1f;
                fadeTimer.stop();
            }
            repaint();
        });
 
        // 클릭하면 시작
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onStart != null) {
                    onStart.run();
                }
            }
        });
    }
 
    // 패널이 화면에 보일 때 페이드인 시작
    public void startFade() {
        titleAlpha = 0f;
        fadeTimer.start();
    }
 
    private Image loadImage(String path) {
        try {
            Image img = new ImageIcon(path).getImage();
            if (img == null || img.getWidth(null) <= 0) {
                System.out.println("이미지 로드 실패(파일 없음/경로 오류): " + path);
                System.out.println("  현재 작업 폴더: " + new java.io.File(".").getAbsolutePath());
                System.out.println("  -> 이 폴더 기준으로 위 경로에 파일이 있어야 합니다.");
            }
            return img;
        } catch (Exception e) {
            System.out.println("이미지 로드 예외: " + path);
            return null;
        }
    }
 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
 
        int W = getWidth();
        int H = getHeight();
 
        // 1. 배경
        if (background != null && background.getWidth(null) > 0) {
            g2.drawImage(background, 0, 0, W, H, this);
        } else {
            // 배경 로드 실패 시 어두운 그라데이션 느낌으로 대체
            g2.setColor(new Color(30, 25, 20));
            g2.fillRect(0, 0, W, H);
        }
 
        // 2. 가독성을 위해 살짝 어둡게 덮기 (글자가 떠오를수록 더 진해짐)
        int veil = (int) (titleAlpha * 80);
        g2.setColor(new Color(0, 0, 0, veil));
        g2.fillRect(0, 0, W, H);
 
        // 3. "시작하기" 글자 (페이드인)
        int alpha = (int) (titleAlpha * 255);
        if (alpha > 255) alpha = 255;
        if (alpha < 0) alpha = 0;
 
        String title = "시작하기";
        Font titleFont = new Font("맑은 고딕", Font.BOLD, 64);
        g2.setFont(titleFont);
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(title);
        int tx = (W - tw) / 2;
        int ty = H / 2;
 
        // 글자 그림자
        g2.setColor(new Color(0, 0, 0, (int) (alpha * 0.6)));
        g2.drawString(title, tx + 3, ty + 3);
        // 글자 본체 (금색 느낌)
        g2.setColor(new Color(245, 222, 160, alpha));
        g2.drawString(title, tx, ty);
 
        // 4. 클릭 안내 (글자가 다 떠오른 뒤 표시)
        if (titleAlpha >= 1f) {
            g2.setColor(new Color(255, 255, 255, 200));
            g2.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
            String guide = "화면을 클릭하세요";
            int gw = g2.getFontMetrics().stringWidth(guide);
            g2.drawString(guide, (W - gw) / 2, ty + 70);
        }
    }
}