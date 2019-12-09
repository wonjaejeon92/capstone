package e.t.gameworkshop;

import java.util.LinkedList;
import e.t.gameworkshop.Arrange.ArrangeObject;
import e.t.gameworkshop.Arrange.Arrange_ScrType;
import e.t.gameworkshop.Arrange.Worker;

public class ScriptExecute {

    private Worker worker;
    private Program program;
    public int indexExcute = 0;
    public int status_Obstacle;
    private LinkedList<Integer> stackStatemt;

    public ScriptExecute(Worker worker, Program program) {
        this.worker = worker;
        this.program = program;
        stackStatemt = new LinkedList<Integer>();
    }

    public int Find_NextCommand(Space[][] spaces) {
        // 명령 스크립트를 찾을 때까지 반복
        if (indexExcute == program.getScripts().size())
            indexExcute = 0;

        while (program.getScripts().get(indexExcute).getType() < 6)
        {
            Script s = program.getScripts().get(indexExcute);

            // 머리
            if (s.getType() > -3 && s.getType() <= 3) {
                // for문은 바로 push
                // 최초 실행 시 1회 소모하므로 n - 1
                if (s.getType() == ScrType.FOR) {
                    stackStatemt.add(s.getNum() - 1);
                    // 행동 소모 (조건 머리)
                    Update_RobotActionStatus();
                }
                // 조건문은 체크 후 push
                else if (s.isTrue(worker, spaces)) {
                    stackStatemt.add(indexExcute);
                    Update_RobotActionStatus();
                }
                // 거짓이면 꼬리롷 이동
                else {
                    // ELSE 문은 연산 비용 소모x
                    if (s.getType() == ScrType.ELSE)
                        stackStatemt.add(indexExcute);
                    else {
                        Update_RobotActionStatus();
                        indexExcute = s.getNum();
                    }
                }
            }

            // 꼬리
            else if (s.getType() <= -3)
            {
                int last_I = stackStatemt.size() - 1;
                switch (s.getType())
                {
                    case ScrType.IFTAIL:
                    case ScrType.ELSETAIL: {
                        if (s.getNum() == stackStatemt.get(stackStatemt.size() - 1))
                        {
                            ++indexExcute;
                            stackStatemt.remove(last_I);
                            Escape_IFconditions();
                            // Escape 에서 위치를 잡음
                            continue;
                        }
                        break;
                    }
                    case ScrType.FORTAIL: {
                        if (stackStatemt.get(last_I) > 0)
                        {
                            stackStatemt.set(last_I, stackStatemt.get(last_I) - 1);
                            indexExcute = s.getNum();
                        }
                        else stackStatemt.remove(last_I);
                        break;
                    }
                    case ScrType.WHILETAIL: {
                        if (program.getScripts().get(s.getNum()).isTrue(worker, spaces))
                            indexExcute = s.getNum();
                        else stackStatemt.remove(last_I);
                        break;
                    }
                }
            }

            // 프로그램 처음으로
            if (++indexExcute == program.getScripts().size())
                indexExcute = 0;
        }

        return indexExcute;
    }

    public void Escape_IFconditions() {
        while (indexExcute < program.getScripts().size())
        {
            int type = program.getScripts().get(indexExcute).getType();
            if (type == ScrType.ELSEIF || type == ScrType.ELSE)
            {
                indexExcute = program.getScripts().get(indexExcute).getNum();
                if (++indexExcute == program.getScripts().size())
                {
                    indexExcute = 0;
                    return;
                }
            }
            else return;
        }
        indexExcute = 0;
    }

    public void Script_MoveCoord() {
        switch (program.getScripts().get(indexExcute).getNum()) {
            case -1: {
                worker.setY(worker.getY() - 5 * worker.getSpeed());
                return;
            }
            case -2: {
                worker.setY(worker.getY() + 5 * worker.getSpeed());
                return;
            }
            case -3: {
                worker.setX(worker.getX() - 5 * worker.getSpeed());
                return;
            }
            case -4: {
                worker.setX(worker.getX() + 5 * worker.getSpeed());
                return;
            }
        }
    }

    public void Script_MoveSpaceIndex(int spaceRow, int spaceCol) {
        switch (program.getScripts().get(indexExcute).getNum()) {
            case MoveDir.UP: {
                if (worker.getRow() - 1 >= 0)
                    worker.setRow(worker.getRow() - 1);
                return;
            }
            case MoveDir.DOWN: {
                if (worker.getRow() + 1 < spaceRow)
                    worker.setRow(worker.getRow() + 1);
                return;
            }
            case MoveDir.LEFT: {
                if (worker.getCol() - 1 >= 0)
                    worker.setCol(worker.getCol() - 1);
                return;
            }
            case MoveDir.RIGHT: {
                if (worker.getCol() + 1 < spaceCol)
                    worker.setCol(worker.getCol() + 1);
                return;
            }
        }
    }

    public void Script_Pick(Space[][] Spaces, LinkedList<ArrangeObject> Objects)
    {
        int spare = worker.getStrength() - worker.getNumHoldObject();
        int row = worker.getRow();
        int col = worker.getCol();
        int numFloorObject = Spaces[row][col].getNumObject();

        if (numFloorObject > 0 && spare > 0)
        {
            // IndexSpace 인덱스 찾기
            int indexDraw = -1;
            for (int i = 0; i < Objects.size(); ++i)
            {
                if (Objects.get(i).getRow() == row && Objects.get(i).getCol() == col)
                {
                    indexDraw = i;
                    break;
                }
            }

            // 여유 >= 바닥 물건 개수
            if (spare >= numFloorObject)
            {
                worker.setNumHoldObject(worker.getNumHoldObject() + numFloorObject);
                Spaces[row][col].setNumObject(0);
                Objects.remove(indexDraw);
            }
            // 여유 < 바닥 물건 개수
            else
            {
                worker.setNumHoldObject(worker.getNumHoldObject() + spare);
                Spaces[row][col].setNumObject(Spaces[row][col].getNumObject() - spare);
                Objects.get(indexDraw).setNum(Objects.get(indexDraw).getNum() - spare);
            }
        }
    }

    public void Script_Put(Space[][] Spaces, LinkedList<ArrangeObject> Objects)
    {
        int row = worker.getRow();
        int col = worker.getCol();
        Space curSpace = Spaces[row][col];
        // 저장고에 위치했나
        if (curSpace.getTag() == SpaceType.SAVE)
        {
            // 물건을 들고 있나
            if (worker.getNumHoldObject() > 0)
            {
                SimulationActivity.setCurNumObject(SimulationActivity.getCurNumObject() + worker.getNumHoldObject());
                worker.setNumHoldObject(0);
            }
        }
        else
        {
            if (curSpace.getNumObject() == 0 && worker.getNumHoldObject() > 0)
                Objects.add(new ArrangeObject(row, col, worker.getNumHoldObject()));
            else
            {
                int size = SimulationActivity.arrangeDrawMap.getObjects().size();
                LinkedList<ArrangeObject> list = SimulationActivity.arrangeDrawMap.getObjects();
                for (int i = 0; i < size; ++i)
                {
                    if (row == list.get(i).getRow() && col == list.get(i).getCol())
                        list.get(i).setNum(list.get(i).getNum() + worker.getNumHoldObject());
                }
            }
            curSpace.setNumObject( curSpace.getNumObject() + worker.getNumHoldObject() );
            worker.setNumHoldObject(0);
        }
    }

    // 로봇이 수행할 때마다 행동량 증가 + 에너지 감소
    public void Update_RobotActionStatus()
    {
        switch (program.getScripts().get(indexExcute).getType())
        {
            case Arrange_ScrType.MOVE:
            case Arrange_ScrType.PICK:
            case Arrange_ScrType.PUT: {
                worker.setNumAct(worker.getNumAct() + 5);
                if (worker.getEnergy() - 3 < 0) worker.setEnergy(0);
                else worker.setEnergy(worker.getEnergy() - 3);
                return;
            }
            case Arrange_ScrType.IF:
            case Arrange_ScrType.ELSEIF:
            case Arrange_ScrType.ELSE:
            case Arrange_ScrType.FOR:
            case Arrange_ScrType.WHILE: {
                worker.setNumAct(worker.getNumAct() + 2);
                worker.setEnergy(worker.getEnergy() - 1);
                return;
            }
            default:
                return;
        }
    }

    public Worker getWorker() {
        return worker;
    }

    public Program getProgram() {
        return program;
    }

    public void setStatus_Obstacle(int status_Obstacle) {
        this.status_Obstacle = status_Obstacle;
    }
}
