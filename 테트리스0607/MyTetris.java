package test;

import javax.swing.*;
import java.awt.*;

public class MyTetris extends JFrame {
    private TetrisCanvas canvas;

    public MyTetris() {
        setTitle("아트리스 - 점수로 완성하는 미술 테트리스");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ArtStage selectedStage = selectStage();

        canvas = new TetrisCanvas(selectedStage);
        getContentPane().add(canvas, BorderLayout.CENTER);

        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu("게임");

        JMenuItem restartItem = new JMenuItem("처음부터 다시");
        restartItem.addActionListener(e -> restartGame());

        JMenuItem changeStageItem = new JMenuItem("스테이지 변경");
        changeStageItem.addActionListener(e -> changeStage());

        JMenuItem descriptionItem = new JMenuItem("작품 설명 보기");
        descriptionItem.addActionListener(e -> canvas.showDescription());

        JMenuItem exitItem = new JMenuItem("종료");
        exitItem.addActionListener(e -> System.exit(0));

        menu.add(restartItem);
        menu.add(changeStageItem);
        menu.add(descriptionItem);
        menu.add(exitItem);

        mb.add(menu);
        setJMenuBar(mb);

        setSize(620, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        SwingUtilities.invokeLater(() -> {
            canvas.requestFocus();
            canvas.start();
        });
    }

    private ArtStage selectStage() {
        ArtStage[] stages = StageData.getStages();

        ArtStage selected = (ArtStage) JOptionPane.showInputDialog(
                null,
                "플레이할 스테이지를 선택하세요.",
                "스테이지 선택",
                JOptionPane.QUESTION_MESSAGE,
                null,
                stages,
                stages[0]
        );

        if (selected == null) {
            return stages[0];
        }

        return selected;
    }

    private void restartGame() {
        if (canvas != null) {
            canvas.stop();
            getContentPane().remove(canvas);
        }

        canvas = new TetrisCanvas(canvas.getStage());
        getContentPane().add(canvas, BorderLayout.CENTER);

        revalidate();
        repaint();

        SwingUtilities.invokeLater(() -> {
            canvas.requestFocus();
            canvas.start();
        });
    }

    private void changeStage() {
        if (canvas != null) {
            canvas.stop();
            getContentPane().remove(canvas);
        }

        ArtStage selectedStage = selectStage();

        canvas = new TetrisCanvas(selectedStage);
        getContentPane().add(canvas, BorderLayout.CENTER);

        revalidate();
        repaint();

        SwingUtilities.invokeLater(() -> {
            canvas.requestFocus();
            canvas.start();
        });
    }

    public static void main(String[] args) {
        new MyTetris();
    }
}