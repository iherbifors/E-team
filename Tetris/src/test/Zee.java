package test;
 
public class Zee extends Piece {
    public Zee(TetrisData data) {
        super(data);
        c[0] = 0;  r[0] = 0;   // 중심
        c[1] = -1; r[1] = 0;   // 왼쪽 위
        c[2] = 0;  r[2] = 1;   // 아래
        c[3] = 1;  r[3] = 1;   // 오른쪽 아래
    }
 
    public int getType() {
        return 6;   // colors[6] = 주황 계열
    }
 
    public int roteType() {
        return 2;   // Z자도 2가지 모양만 (가로/세로)
    }
}
 