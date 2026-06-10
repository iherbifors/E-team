package test;
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
// 분야 선택 화면: 배경(분야_선택.png) 위에서 왼쪽=서양, 오른쪽=동양 영역을 클릭한다.
public class SelectThemePanel extends JPanel {
    private Image background;
    private java.util.function.Consumer<Theme> onSelect;
 
    private Rectangle westArea;   // 왼쪽(서양)
    private Rectangle eastArea;   // 오른쪽(동양)
    private Theme hover = null;
 
    public SelectThemePanel(java.util.function.Consumer<Theme> onSelect) {
        this.onSelect = onSelect;
        background = loadImage("resources/images/select_bg.jpg");
        setLayout(null);
 
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateAreas();
                if (westArea.contains(e.getPoint())) {
                    onSelect.accept(Theme.WEST);
                } else if (eastArea.contains(e.getPoint())) {
                    onSelect.accept(Theme.EAST);
                }
            }
        });
 
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateAreas();
                Theme old = hover;
                if (westArea.contains(e.getPoint())) {
                    hover = Theme.WEST;
                } else if (eastArea.contains(e.getPoint())) {
                    hover = Theme.EAST;
                } else {
                    hover = null;
                }
                if (hover != old) repaint();
            }
        });
    }
 
    // 패널 크기에 맞춰 좌/우 클릭 영역을 계산 (배경 이미지가 좌우 2분할 구도)
    private void updateAreas() {
        int W = getWidth();
        int H = getHeight();
        westArea = new Rectangle(0, 0, W / 2, H);
        eastArea = new Rectangle(W / 2, 0, W / 2, H);
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
        updateAreas();
 
        if (background != null) {
            g2.drawImage(background, 0, 0, W, H, this);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, W, H);
        }
 
        // 마우스 올린 쪽을 살짝 밝게 강조
        if (hover == Theme.WEST) {
            g2.setColor(new Color(255, 255, 255, 40));
            g2.fillRect(westArea.x, westArea.y, westArea.width, westArea.height);
        } else if (hover == Theme.EAST) {
            g2.setColor(new Color(255, 255, 255, 40));
            g2.fillRect(eastArea.x, eastArea.y, eastArea.width, eastArea.height);
        }
    }
}