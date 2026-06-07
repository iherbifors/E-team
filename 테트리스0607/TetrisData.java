package test;

public class TetrisData {
    public static final int ROW = 20;
    public static final int COL = 10;
    private int data[][]; 
    private int score; // 지운 라인 수 대신 누적 점수 사용

    public TetrisData() {
        data = new int[ROW][COL];
        score = 0;
    }

    public int getAt(int x, int y) {
        if (x < 0 || x >= ROW || y < 0 || y >= COL) {
            return 0;
        }
        return data[x][y];
    }

    public void setAt(int x, int y, int v) {
        data[x][y] = v;
    }

    public int getScore() {
        return score;
    }

    public synchronized void removeLines() {
        NEXT:
        for (int i = ROW - 1; i >= 0; i--) {
            boolean done = true;
            for (int k = 0; k < COL; k++) {
                if (data[i][k] == 0) {
                    done = false;
                    continue NEXT;
                }
            }
            if (done) {
                score += 100; // 한 줄 제거할 때마다 100점 획득
                for (int x = i; x > 0; x--) {
                    for (int y = 0; y < COL; y++) {
                        data[x][y] = data[x - 1][y];
                    }
                }
                if (i != 0) {
                    for (int y = 0; y < COL; y++) {
                        data[0][y] = 0;
                    }
                }
                i++; 
            }
        }
    }

    public void clear() {
        for (int i = 0; i < ROW; i++) {
            for (int k = 0; k < COL; k++) {
                data[i][k] = 0;
            }
        }
        score = 0;
    }
}