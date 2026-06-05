package test;

import javax.swing.*;
import java.awt.*;

public class MyTetris extends JFrame {
    public MyTetris() {
        setTitle("테트리스 게임 - 사진 밝히기 프로젝트");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        TetrisCanvas canvas = new TetrisCanvas();
        getContentPane().add(canvas, BorderLayout.CENTER);
        
        JMenuBar mb = new JMenuBar();
        JMenu m = new JMenu("게임");
        JMenuItem item = new JMenuItem("종료");
        item.addActionListener(e -> System.exit(0));
        m.add(item);
        mb.add(m);
        setJMenuBar(mb);
        
        setSize(500, 600); // 우측 UI 스코어보드를 보이기 위해 가로폭을 늘렸습니다.
        setLocationRelativeTo(null); 
        setVisible(true); 
        canvas.start(); 
    }

    public static void main(String[] args) {
        new MyTetris();
    }
}