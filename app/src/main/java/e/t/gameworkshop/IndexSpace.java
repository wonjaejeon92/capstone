package e.t.gameworkshop;

import java.io.Serializable;

// DrawMap에서 지형의 빠른 인덱싱으로 그리기 위한 클래스
public class IndexSpace implements Serializable {

    private int row;
    private int col;
    private Space space;

    public IndexSpace(int row, int col, Space space) {
        this.row = row;
        this.col = col;
        this.space = space;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
