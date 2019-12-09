package e.t.gameworkshop;

import java.io.Serializable;

public class Space implements Serializable {

    private int tag;
    private int numObject;            // 물건 : 개수
    private boolean blocked;
    private int existRobotNum;      // 로봇 정보 확인 시 인덱싱

    public Space(int tag, int numObject, boolean blocked) {
        this.tag = tag;
        this.numObject = numObject;
        this.blocked = blocked;
        existRobotNum = -1;
    }

    public Space(int tag, int numObject, boolean blocked, int existRobotNum) {
        this.tag = tag;
        this.numObject = numObject;
        this.blocked = blocked;
        this.existRobotNum = existRobotNum;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getNumObject() {
        return numObject;
    }

    public void setNumObject(int numObject) {
        this.numObject = numObject;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public int getExistRobotNum() {
        return existRobotNum;
    }

    public void setExistRobotNum(int existRobotNum) {
        this.existRobotNum = existRobotNum;
    }
}
