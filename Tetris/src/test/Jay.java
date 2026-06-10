package test;
 
public class Jay extends Piece {
    public Jay(TetrisData data) {
        super(data);
        c[0] = 0;  r[0] = 0;   // 중심
        c[1] = -1; r[1] = 0;   // 왼쪽
        c[2] = 1;  r[2] = 0;   // 오른쪽
        c[3] = -1; r[3] = 1;   // 왼쪽 아래 (J자 꺾임 - El과 좌우 반대)
    }
 
    public int getType() {
        return 7;   // colors[7] = 흰색 계열
    }
 
    public int roteType() {
        return 4;   // 4방향 회전
    }
}