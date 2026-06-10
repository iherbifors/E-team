package test;
 
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
 
// 스테이지 선택 화면: 선택한 분야의 스테이지 4개를 카드로 보여주고, 클릭하면 onSelect 실행.
public class SelectStagePanel extends JPanel {
    private Image background;
    private Theme theme;
    private List<ArtStage> stages = new ArrayList<>();
    private java.util.function.Consumer<ArtStage> onSelect;
 
    private List<Rectangle> cardAreas = new ArrayList<>();
    private int hoverIndex = -1;
 
    public SelectStagePanel(Theme theme, java.util.function.Consumer<ArtStage> onSelect) {
        this.theme = theme;
        this.onSelect = onSelect;
        background = loadImage(theme.getBackgroundPath());
        setLayout(null);
 
        // 해당 분야 스테이지만 추리기
        for (ArtStage s : StageData.getStages()) {
            if (s.getTheme().equals(theme.getThemeName())) {
                stages.add(s);
            }
        }
 
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layoutCards();
                for (int i = 0; i < cardAreas.size(); i++) {
                    if (cardAreas.get(i).contains(e.getPoint())) {
                        onSelect.accept(stages.get(i));
                        return;
                    }
                }
            }
        });
 
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                layoutCards();
                int old = hoverIndex;
                hoverIndex = -1;
                for (int i = 0; i < cardAreas.size(); i++) {
                    if (cardAreas.get(i).contains(e.getPoint())) {
                        hoverIndex = i;
                        break;
                    }
                }
                if (hoverIndex != old) repaint();
            }
        });
    }
 
    // 카드 4개를 가로로 배치
    private void layoutCards() {
        cardAreas.clear();
        int W = getWidth();
        int H = getHeight();
        int n = stages.size();
        if (n == 0) return;
 
        int cardW = 130;
        int cardH = 180;
        int gap = 30;
        int totalW = n * cardW + (n - 1) * gap;
        int startX = (W - totalW) / 2;
        int y = (H - cardH) / 2;
 
        for (int i = 0; i < n; i++) {
            int x = startX + i * (cardW + gap);
            cardAreas.add(new Rectangle(x, y, cardW, cardH));
        }
    }
 
    private Image loadImage(String path) {
        try {
            return new ImageIcon(path).getImage();
        } catch (Exception e) {
            System.out.println("이미지 로드 실패: " + path);
            return null;
        }
    }
 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
        int W = getWidth();
        int H = getHeight();
        layoutCards();
 
        // 배경
        if (background != null) {
            g2.drawImage(background, 0, 0, W, H, this);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, W, H);
        }
        // 살짝 어둡게 덮어 카드 가독성 확보
        g2.setColor(new Color(0, 0, 0, 90));
        g2.fillRect(0, 0, W, H);
 
        // 제목
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        String title = theme.getThemeName() + " - 스테이지 선택";
        int tw = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (W - tw) / 2, 80);
 
        // 카드
        for (int i = 0; i < stages.size(); i++) {
            ArtStage s = stages.get(i);
            Rectangle r = cardAreas.get(i);
 
            boolean hover = (i == hoverIndex);
 
            // 카드 배경
            g2.setColor(hover ? new Color(255, 240, 200, 230) : new Color(240, 235, 220, 200));
            g2.fillRoundRect(r.x, r.y, r.width, r.height, 12, 12);
 
            // 액자 느낌 테두리
            g2.setColor(new Color(150, 110, 40));
            g2.setStroke(new BasicStroke(hover ? 5 : 3));
            g2.drawRoundRect(r.x, r.y, r.width, r.height, 12, 12);
 
            // 미리보기 그림(완성 이미지 축소)
            Image art = loadImage(s.getImagePath());
            if (art != null) {
                g2.drawImage(art, r.x + 12, r.y + 12, r.width - 24, r.height - 70, this);
            }
 
            // 작품명 + 목표 점수
            g2.setColor(new Color(60, 40, 10));
            g2.setFont(new Font("맑은 고딕", Font.BOLD, 14));
            String name = s.getTitle();
            int nw = g2.getFontMetrics().stringWidth(name);
            g2.drawString(name, r.x + (r.width - nw) / 2, r.y + r.height - 36);
 
            g2.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
            String goal = "목표 " + s.getTargetScore() + "점";
            int gw = g2.getFontMetrics().stringWidth(goal);
            g2.drawString(goal, r.x + (r.width - gw) / 2, r.y + r.height - 16);
        }
    }
}