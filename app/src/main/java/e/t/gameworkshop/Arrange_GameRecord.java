package e.t.gameworkshop;

import java.util.LinkedList;

import e.t.gameworkshop.Arrange.Arrange_ScrType;
import e.t.gameworkshop.Arrange.Worker;

public class Arrange_GameRecord {

    private int Time = 0;
    private int TotalAct = 0;
    private int TotalCost = 0;

    public Arrange_GameRecord(LinkedList<Worker> workers, LinkedList<Program> programs)
    {
        // 로봇 비용
        for (int i = 0; i < workers.size(); ++i)
            TotalCost += workers.get(i).getCost();

        // 프로그램 비용
        for (int i = 0; i < programs.size(); ++i)
        {
            TotalCost += 50;

            // 스크립트 비용
            for (int j = 0; j < programs.get(i).getScripts().size(); ++j)
            {
                int type = programs.get(i).getScripts().get(j).getType();
                switch (type)
                {
                    case Arrange_ScrType.MOVE:
                    case Arrange_ScrType.PICK:
                    case Arrange_ScrType.PUT: {
                        TotalCost += 20;
                        break;
                    }
                    case Arrange_ScrType.IF:
                    case Arrange_ScrType.ELSEIF:
                    case Arrange_ScrType.ELSE:
                    case Arrange_ScrType.FOR:
                    case Arrange_ScrType.WHILE: {
                        TotalCost += 10;

                        // 조건식 비용
                        if (type != Arrange_ScrType.FOR)
                        {
                            int ConditionSize = programs.get(i).getScripts().get(j).getConditions().size();
                            for (int k = 0; k < ConditionSize; ++k)
                                TotalCost += 5;
                        }
                        break;
                    }
                    default:
                        continue;
                }
            }
        }
    }

    public int getTotalCost() {
        return TotalCost;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int time) {
        Time = time;
    }

    public int getTotalAct() {
        return TotalAct;
    }

    public void setTotalAct(int totalAct) {
        TotalAct = totalAct;
    }

    public void setTotalCost(int totalCost) {
        TotalCost = totalCost;
    }
}
