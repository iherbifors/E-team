package test;
 
public class Ess extends Piece {
    public Ess(TetrisData data) {
        super(data);
        c[0] = 0;  r[0] = 0;   // 중심
        c[1] = -1; r[1] = 1;   // 왼쪽 아래
        c[2] = 0;  r[2] = 1;   // 아래
        c[3] = 1;  r[3] = 0;   // 오른쪽 위
    }
 
    public int getType() {
        return 5;   // colors[5] = 분홍/보라 계열
    }
 
    public int roteType() {
        return 2;   // S자는 2가지 모양만 (가로/세로)
    }
}
 