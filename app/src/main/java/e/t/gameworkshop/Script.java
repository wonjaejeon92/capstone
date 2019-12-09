package e.t.gameworkshop;

import java.util.LinkedList;

public class Script {

    private String name;
    private boolean selected;
    public String inclusion;
    private int type;
    // 이동 : 방향, if : 종료 후 라인 위치, for : 반복 횟수, 나머지 : 0
    private int num;
    private LinkedList<Condition> conditions;

    public Script(String name, int type, int num) {
        this.name = name;
        this.inclusion = "";
        this.type = type;
        this.num = num;
        if (type == ScrType.IF || type == ScrType.WHILE)
            this.conditions = new LinkedList<Condition>();
    }

    public boolean isTrue(Robot robot, Space[][] spaces)
    {
        // 아무것도 없으면 false
        if (conditions.size() == 0)
            return false;

        int i = -1;
        boolean flag = true;  // 이전 조건 상태
        while (++i < conditions.size())
        {
            // true-and : 체크 후 이동
            // true-or : return true
            // true-end : return flag
            if (flag)
            {
                // &&
                if (conditions.get(i).getConnectType() == 1) {
                    if (!conditions.get(i).isTrueArrange(robot, spaces))
                        flag = false;
                }
                // ||
                else if (conditions.get(i).getConnectType() == 2)
                    return true;
            }
            // false-and : 다음 이동
            // false-or : 체크 후 이동
            // false-end : return flag
            else
            {
                // &&
                if (conditions.get(i).getConnectType() == 1)
                    continue;
                // ||
                else if (conditions.get(i).getConnectType() == 2)
                {
                    if (conditions.get(i).isTrueArrange(robot, spaces))
                        flag = true;
                }
            }
        }
        return flag;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public LinkedList<Condition> getConditions() {
        return conditions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}