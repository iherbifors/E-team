package test;
 
import java.awt.Point;
 
public abstract class Piece {
    final int DOWN = 0;
    final int LEFT = 1;
    final int RIGHT = 2;
    protected int r[];
    protected int c[];
    protected TetrisData data;
    protected Point center;
 
    public Piece(TetrisData data) {
        r = new int[4];
        c = new int[4];
        this.data = data;
        center = new Point(5, 0);
    }
 
    public abstract int getType();
    public abstract int roteType();
    public int getX() { return center.x; }
    public int getY() { return center.y; }
 
    public boolean copy() {
        boolean value = false;
        int x = getX();
        int y = getY();
        if (getMinY() + y <= 0) {
            value = true;
        }
        for (int i = 0; i < 4; i++) {
            data.setAt(y + r[i], x + c[i], getType());
        }
        return value;
    }
 
    public boolean isOverlap(int dir) {
        int x = getX();
        int y = getY();
        switch (dir) {
            case 0 :
                for (int i = 0; i < r.length; i++) {
                    if (data.getAt(y + r[i] + 1, x + c[i]) != 0) {
                        return true;
                    }
                }
                break;
            case 1 :
                for (int i = 0; i < r.length; i++) {
                    if (data.getAt(y + r[i], x + c[i] - 1) != 0) {
                        return true;
                    }
                }
                break;
            case 2 :
                for (int i = 0; i < r.length; i++) {
                    if (data.getAt(y + r[i], x + c[i] + 1) != 0) {
                        return true;
                    }
                }
                break;
        }
        return false;
    }
 
    public int getMinX() {
        int min = c[0];
        for (int i = 1; i < c.length; i++) {
            if (c[i] < min) {
                min = c[i];
            }
        }
        return min;
    }
 
    public int getMaxX() {
        int max = c[0];
        for (int i = 1; i < c.length; i++) {
            if (c[i] > max) {
                max = c[i];
            }
        }
        return max;
    }
 
    public int getMinY() {
        int min = r[0];
        for (int i = 1; i < r.length; i++) {
            if (r[i] < min) {
                min = r[i];
            }
        }
        return min;
    }
 
    public int getMaxY() {
        int max = r[0];
        for (int i = 1; i < r.length; i++) {
            if (r[i] > max) {
                max = r[i];
            }
        }
        return max;
    }
 
    public boolean moveDown() {
        if (center.y + getMaxY() + 1 < TetrisData.ROW) {
            if (isOverlap(DOWN) != true) {
                center.y++;
            } else {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }
 
    public void moveLeft() {
        if (center.x + getMinX() - 1 >= 0) {
            if (isOverlap(LEFT) != true) {
                center.x--;
            } else return;
        }
    }
 
    public void moveRight() {
        if (center.x + getMaxX() + 1 < TetrisData.COL) {
            if (isOverlap(RIGHT) != true) {
                center.x++;
            } else return;
        }
    }
 
    // 현재 좌표(center + r/c)가 보드 안에서 유효한 위치인지 검사한다.
    // 벽 밖으로 나가거나, 이미 쌓인 블록과 겹치면 false.
    public boolean canPlace() {
        int x = getX();
        int y = getY();
        for (int i = 0; i < 4; i++) {
            int nx = x + c[i];
            int ny = y + r[i];
 
            if (nx < 0 || nx >= TetrisData.COL) {
                return false; // 좌우 벽 밖
            }
            if (ny >= TetrisData.ROW) {
                return false; // 바닥 아래
            }
            if (ny >= 0 && data.getAt(ny, nx) != 0) {
                return false; // 이미 블록이 있는 칸과 겹침
            }
        }
        return true;
    }
 
    public void rotate() {
        int rc = roteType();
        if (rc <= 1) return; // O자처럼 회전 안 하는 블록
 
        // 1. 회전 전 좌표 백업
        int[] backupR = new int[4];
        int[] backupC = new int[4];
        for (int i = 0; i < 4; i++) {
            backupR[i] = r[i];
            backupC[i] = c[i];
        }
 
        // 2. 회전 실행 (rc == 2면 반시계 효과를 위해 3번 = 270도)
        if (rc == 2) {
            rotate4();
            rotate4();
            rotate4();
        } else {
            rotate4();
        }
 
        // 3. 유효성 검사 → 실패하면 백업으로 되돌림
        if (!canPlace()) {
            for (int i = 0; i < 4; i++) {
                r[i] = backupR[i];
                c[i] = backupC[i];
            }
        }
    }
 
    public void rotate4() {
        for (int i = 0; i < 4; i++) {
            int temp = c[i];
            c[i] = -r[i];
            r[i] = temp;
        }
    }
}
 