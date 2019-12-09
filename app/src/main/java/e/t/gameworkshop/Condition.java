package e.t.gameworkshop;

public class Condition {

    private int conLeft;
    private int conRight;
    private int compare;
    private int connectType;

    public Condition(int conLeft, int compare, int conRight, int connectType) {
        this.conLeft = conLeft;
        this.conRight = conRight;
        this.compare = compare;
        this.connectType = connectType;
    }

    public boolean isTrueArrange(Robot robot, Space[][] spaces) {
        int row = robot.getRow();
        int col = robot.getCol();
        // and 1 or 2
        switch (conLeft) {
            case 0: {
                if (GetCompare_Arrange(spaces, compare, row, col)) return true;
                else return false;
            }
            case 1: {
                if (GetCompare_Arrange(spaces, compare, row - 1, col)) return true;
                else return false;
            }
            case 2: {
                if (GetCompare_Arrange(spaces, compare, row + 1, col)) return true;
                else return false;
            }
            case 3: {
                if (GetCompare_Arrange(spaces, compare, row, col - 1)) return true;
                else return false;
            }
            case 4: {
                if (GetCompare_Arrange(spaces, compare, row, col + 1)) return true;
                else return false;
            }
        }
        return false;
    }

    public boolean GetCompare_Arrange(Space[][] spaces, int Compare, int row, int col) {
        boolean flag;
        if (conRight == SpaceType.ROBOT)
        {
            // 맵 밖을 참조
            if (IsOutOfIndex(spaces, row, col))
                flag = false;
            else if (spaces[row][col].getExistRobotNum() != -1)
                flag = true;
            else flag = false;
        }

        else if (conRight == SpaceType.OBJECT)
        {
            if (IsOutOfIndex(spaces, row, col))
                flag = false;
            else if (spaces[row][col].getNumObject() > 0)
                flag = true;
            else flag = false;
        }

        else
        {
            if (IsOutOfIndex(spaces, row, col))
            {
                // 벽인 경우는 참
                if (conRight == SpaceType.WALL) flag = true;
                else flag = false;
            }
            else if (spaces[row][col].getTag() == conRight)
                flag = true;
            else flag = false;
        }

        // == 0 != 1
        if (Compare == 0)
            return flag;
        else return !flag;
    }

    public boolean IsOutOfIndex(Space[][] spaces, int row, int col)
    {
        if (row < 0 || row == spaces.length || col < 0 || col == spaces[0].length)
            return true;
        else return false;
    }

    public int getConLeft() {
        return conLeft;
    }

    public void setConLeft(int conLeft) {
        this.conLeft = conLeft;
    }

    public int getConRight() {
        return conRight;
    }

    public void setConRight(int conRight) {
        this.conRight = conRight;
    }

    public int getCompare() {
        return compare;
    }

    public void setCompare(int compare) {
        this.compare = compare;
    }

    public int getConnectType() {
        return connectType;
    }

    public void setConnectType(int connectType) {
        this.connectType = connectType;
    }
}