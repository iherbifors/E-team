package test;
 
public class Square extends Piece {
    public Square(TetrisData data) {
        super(data);
        c[0] = 0;  r[0] = 0;   // 중심(왼쪽 위)
        c[1] = 1;  r[1] = 0;   // 오른쪽 위
        c[2] = 0;  r[2] = 1;   // 왼쪽 아래
        c[3] = 1;  r[3] = 1;   // 오른쪽 아래
    }
 
    public int getType() {
        return 4;   // colors[4] = 노란색 계열
    }
 
    public int roteType() {
        return 1;   // 정사각형은 회전해도 모양이 같으므로 회전 안 함
    }
}