package test;
 
// 분야(동양/서양)별로 게임 배경, 액자 이미지, 액자 안쪽 구멍 비율을 묶어서 관리한다.
// 액자가 추가되거나 비율이 바뀌면 여기 값만 고치면 된다.
public enum Theme {
 
    // 액자 안쪽 구멍 비율(holeX0, holeX1, holeY0, holeY1)은
    // 액자 PNG 전체 크기 대비 투명 구멍이 차지하는 위치 비율이다. (이미지 분석으로 측정한 값)
    WEST(
            "서양미술",
            "resources/images/bg_west.jpg",
            "resources/images/frame_west.png",
            0.251, 0.749, 0.174, 0.742,
            19, 115, 141
    ),
    EAST(
            "동양미술",
            "resources/images/bg_east.jpg",
            "resources/images/frame_east.png",
            0.301, 0.696, 0.088, 0.855,
            18, 156, 166
    );
 
    private final String themeName;
    private final String backgroundPath;
    private final String framePath;
    private final double holeX0;
    private final double holeX1;
    private final double holeY0;
    private final double holeY1;
    private final int cellSize;    // 한 칸 크기(px)
    private final int boardX;      // 보드 좌상단 x
    private final int boardY;      // 보드 좌상단 y
 
    Theme(String themeName, String backgroundPath, String framePath,
          double holeX0, double holeX1, double holeY0, double holeY1,
          int cellSize, int boardX, int boardY) {
        this.themeName = themeName;
        this.backgroundPath = backgroundPath;
        this.framePath = framePath;
        this.holeX0 = holeX0;
        this.holeX1 = holeX1;
        this.holeY0 = holeY0;
        this.holeY1 = holeY1;
        this.cellSize = cellSize;
        this.boardX = boardX;
        this.boardY = boardY;
    }
 
    public String getThemeName() {
        return themeName;
    }
 
    public String getBackgroundPath() {
        return backgroundPath;
    }
 
    public String getFramePath() {
        return framePath;
    }
 
    public double getHoleX0() { return holeX0; }
    public double getHoleX1() { return holeX1; }
    public double getHoleY0() { return holeY0; }
    public double getHoleY1() { return holeY1; }
 
    public int getCellSize() { return cellSize; }
    public int getBoardX() { return boardX; }
    public int getBoardY() { return boardY; }
 
    // 구멍의 가로 비율 (전체 대비 폭)
    public double getHoleWidthRatio() {
        return holeX1 - holeX0;
    }
 
    // 구멍의 세로 비율 (전체 대비 높이)
    public double getHoleHeightRatio() {
        return holeY1 - holeY0;
    }
 
    // 스테이지의 테마 문자열("서양미술"/"동양미술")로 Theme을 찾는다.
    // 일치하는 게 없으면 기본값으로 WEST를 반환한다.
    public static Theme fromThemeName(String name) {
        for (Theme t : values()) {
            if (t.themeName.equals(name)) {
                return t;
            }
        }
        return WEST;
    }
}