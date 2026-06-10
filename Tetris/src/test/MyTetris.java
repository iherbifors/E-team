package test;
 
import javax.swing.*;
import java.awt.*;
 
// 전체 화면 흐름을 CardLayout으로 전환한다:
//   START -> SELECT_THEME -> SELECT_STAGE -> GAME
public class MyTetris extends JFrame {
    private CardLayout cardLayout;
    private JPanel root;
 
    private StartPanel startPanel;
    private SelectThemePanel selectThemePanel;
 
    private TetrisCanvas canvas;          // 현재 게임 캔버스
    private JPanel gamePanel;             // 캔버스를 담는 컨테이너
    private ArtStage currentStage;        // 현재 진행 중인 스테이지
 
    private static final String START = "START";
    private static final String SELECT_THEME = "SELECT_THEME";
    private static final String SELECT_STAGE = "SELECT_STAGE";
    private static final String GAME = "GAME";
 
    public MyTetris() {
        setTitle("아트리스 - 점수로 완성하는 미술 테트리스");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        cardLayout = new CardLayout();
        root = new JPanel(cardLayout);
 
        // 1) 시작 화면
        startPanel = new StartPanel(() -> showSelectTheme());
        root.add(startPanel, START);
 
        // 2) 분야 선택 화면
        selectThemePanel = new SelectThemePanel(theme -> showSelectStage(theme));
        root.add(selectThemePanel, SELECT_THEME);
 
        // 3) 게임 화면 컨테이너(스테이지 선택 시마다 캔버스를 새로 끼움)
        gamePanel = new JPanel(new BorderLayout());
        root.add(gamePanel, GAME);
 
        getContentPane().add(root);
 
        buildMenu();
 
        setSize(760, 760);
        setLocationRelativeTo(null);
        setVisible(true);
 
        // 시작 화면 페이드인
        cardLayout.show(root, START);
        SwingUtilities.invokeLater(() -> startPanel.startFade());
    }
 
    private void buildMenu() {
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu("게임");
 
        JMenuItem restartItem = new JMenuItem("처음부터 다시");
        restartItem.addActionListener(e -> restartGame());
 
        JMenuItem changeStageItem = new JMenuItem("스테이지 변경");
        changeStageItem.addActionListener(e -> showSelectTheme());
 
        JMenuItem descriptionItem = new JMenuItem("작품 설명 보기");
        descriptionItem.addActionListener(e -> {
            if (canvas != null) canvas.showDescription();
        });
 
        JMenuItem homeItem = new JMenuItem("처음 화면으로");
        homeItem.addActionListener(e -> showStart());
 
        JMenuItem exitItem = new JMenuItem("종료");
        exitItem.addActionListener(e -> System.exit(0));
 
        menu.add(restartItem);
        menu.add(changeStageItem);
        menu.add(descriptionItem);
        menu.add(homeItem);
        menu.add(exitItem);
        mb.add(menu);
        setJMenuBar(mb);
    }
 
    // ---- 화면 전환 ----
 
    private void showStart() {
        stopCurrentGame();
        cardLayout.show(root, START);
        SwingUtilities.invokeLater(() -> startPanel.startFade());
    }
 
    private void showSelectTheme() {
        stopCurrentGame();
        // 분야 선택은 매번 새로 만들 필요 없음(상태 없음)
        cardLayout.show(root, SELECT_THEME);
    }
 
    private SelectStagePanel selectStagePanel; // 현재 스테이지 선택 패널
 
    private void showSelectStage(Theme theme) {
        // 분야가 정해질 때마다 해당 분야 스테이지 카드 화면을 새로 만든다.
        if (selectStagePanel != null) {
            root.remove(selectStagePanel);
        }
        selectStagePanel = new SelectStagePanel(theme, stage -> startGame(stage));
        root.add(selectStagePanel, SELECT_STAGE);
        cardLayout.show(root, SELECT_STAGE);
        root.revalidate();
        root.repaint();
    }
 
    private void startGame(ArtStage stage) {
        this.currentStage = stage;
        stopCurrentGame();
 
        gamePanel.removeAll();
        canvas = new TetrisCanvas(stage);
        gamePanel.add(canvas, BorderLayout.CENTER);
        gamePanel.revalidate();
        gamePanel.repaint();
 
        cardLayout.show(root, GAME);
        SwingUtilities.invokeLater(() -> {
            canvas.requestFocus();
            canvas.start();
        });
    }
 
    private void restartGame() {
        if (currentStage != null) {
            startGame(currentStage);
        }
    }
 
    private void stopCurrentGame() {
        if (canvas != null) {
            canvas.stop();
        }
    }
 
    public static void main(String[] args) {
        new MyTetris();
    }
}