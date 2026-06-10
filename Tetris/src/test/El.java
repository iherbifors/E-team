package test;

public class El extends Piece {
    public El(TetrisData data) {
        super(data);
        c[0] = 0;  r[0] = 0;   // 중심
        c[1] = -1; r[1] = 0;   // 왼쪽
        c[2] = 1;  r[2] = 0;   // 오른쪽
        c[3] = 1;  r[3] = 1;   // 오른쪽 아래 (L자 꺾임)
    }

    public int getType() {
        return 3;   // colors[3] = 하늘색 계열
    }

    public int roteType() {
        return 4;   // 4방향 회전
    }
}