package e.t.gameworkshop.Arrange;

import java.io.Serializable;

public class ArrangeObject implements Serializable {

    private int row;
    private int col;
    private int num;

    public ArrangeObject(int row, int col, int num) {
        this.row = row;
        this.col = col;
        this.num = num;
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
