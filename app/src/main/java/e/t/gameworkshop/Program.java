package e.t.gameworkshop;

import java.util.LinkedList;

public class Program {

    private String name;
    private LinkedList<Integer> Num_RobotUse;   // 소속 로봇
    private LinkedList<Script> scripts;
    private boolean Exist_Command = false;

    public Program(String name) {
        this.name = name;
        Num_RobotUse = new LinkedList<Integer>();
        scripts = new LinkedList<Script>();
    }

    public void Conenct_HeadTail() {
        LinkedList<Integer> headList = new LinkedList<Integer>();

        if (Num_RobotUse.size() == 0 || scripts.size() == 0)
            return;
        else {
            int j = 0;
            while (j < scripts.size())
            {
                int type = scripts.get(j).getType();
                // 머리 push (for 머리는 반복 횟수)
                if (type >= -2 && type <= 3)
                    headList.add(j);
                // 꼬리
                else if (type < -2)
                {
                    // 머리 꼬리 서로 인덱스 저장 후 pop
                    // for 문은 꼬리->머리만 연결. 머리는 반복 횟수 저장용
                    if (type != ScrType.FORTAIL)
                        scripts.get(headList.get(headList.size() - 1)).setNum(j);
                    scripts.get(j).setNum(headList.get(headList.size() - 1));
                    headList.remove(headList.size() - 1);
                }
                ++j;
            }
        }
    }

    public void Exist_Command()
    {
        for (int i = 0; i < scripts.size(); ++i)
        {
            if (scripts.get(i).getType() >= 8)
            {
                Exist_Command = true;
                return;
            }
        }
        Exist_Command = false;
    }

    public String getName() {
        return name;
    }

    public LinkedList<Integer> getNum_RobotUse() {
        return Num_RobotUse;
    }

    public LinkedList<Script> getScripts() {
        return scripts;
    }

    public boolean isExist_Command() {
        return Exist_Command;
    }
}
