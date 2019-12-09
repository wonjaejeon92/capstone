package e.t.gameworkshop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import java.util.LinkedList;
import java.util.List;
import e.t.gameworkshop.Arrange.Arrange_ScrType;
import e.t.gameworkshop.Arrange.ArrangeDrawMap;
import e.t.gameworkshop.Arrange.ArrangeObject;
import e.t.gameworkshop.Arrange.Game_ArrangeActivity;
import e.t.gameworkshop.Arrange.Stage_ArrangeActivity;
import e.t.gameworkshop.Arrange.Worker;

public class SimulationActivity extends AppCompatActivity implements FragmentRobotInform.InterfaceRobotInform {

    Handler UIhandler;
    public static Context mContext;
    private static TextView textCost, textTime, textAct, textNumObject;
    public static Button BtnStart;
    public static ArrangeDrawMap arrangeDrawMap;
    private Space[][] spaces;
    private ScriptExecute[] scriptExecute;
    private Thread thread_game;
    private static boolean isStart = false;
    private static int totalObject, curNumObject;
    private int spaceRow, spaceCol, maxX, maxY;
    private AlertDialog dialog;
    private static ListView ListView_worker;
    private static Adapter_RobotInformList Adapter_workerInformList;
    private static List<Worker> List_worker;
    private Arrange_GameRecord gameRecord;
    RelativeLayout fragment;
    LinearLayout list_robot;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로 모드 고정
        setContentView(R.layout.activity_simulation);
        mContext = this;

        textCost = (TextView) findViewById(R.id.textCost);
        textTime = (TextView) findViewById(R.id.textTime);
        textAct = (TextView) findViewById(R.id.textAct);
        textNumObject = (TextView) findViewById(R.id.textNumObject);
        fragment = fragment = (RelativeLayout) findViewById(R.id.fragment);
        list_robot = (LinearLayout) findViewById(R.id.listrobot);

        // 데이터 받아 준비
        // 1 : Arrange
        UIhandler = new Handler();
        Intent intent = getIntent();
        int game = intent.getIntExtra("game", 1);
        int stagenumber = MapInfo.getStagenumber();
        if (game == 1) {
            spaceRow = Game_ArrangeActivity.getSpaces().length;
            spaceCol = Game_ArrangeActivity.getSpaces()[0].length;
            arrangeDrawMap = (ArrangeDrawMap) findViewById(R.id.painter2);
            Copy_MapArrange(Game_ArrangeActivity.getSpaces(), spaceRow, spaceCol);
            CopyRobot(Game_ArrangeActivity.getWorkers(), Game_ArrangeActivity.getPrograms());
            Copy_DrawArrange(stagenumber, spaceRow, spaceCol);
            maxX = spaceCol * arrangeDrawMap.getSPACE_LENGTH() + arrangeDrawMap.getSTART_GRID();
            maxY = spaceRow * arrangeDrawMap.getSPACE_LENGTH() + arrangeDrawMap.getSTART_GRID();

            // 게임 기록 초기화
            textNumObject.setText(String.valueOf(curNumObject) + " / " + String.valueOf(totalObject));
            curNumObject = 0;
            textAct.setText("0");
            textTime.setText("0");
        }


        // 시작 버튼
        BtnStart = (Button) findViewById(R.id.BtnStart);
        BtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isStart)
                {
                    isStart = true;
                    BtnStart.setText("시작");
                    BtnStart.setBackgroundResource(R.drawable.button_borderline_selected);
                    // 스레드 시작
                    CreateThread_ArrangeGame();
                    thread_game.start();
                }
                else
                {
                    Stop_Thread();
                    // 명령 한 번이 완전히 끝나야만 다시 시작 가능
                    BtnStart.setClickable(false);
                    Handler delayHandler = new Handler();
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            BtnStart.setClickable(true);
                        }
                    }, 1000);
                }
            }
        });

        // 로봇 정보 리스트 클릭
        ListView_worker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 화면 변화
                list_robot.setVisibility(View.INVISIBLE);
                fragment.setVisibility(View.VISIBLE);
                // 정보 보내주기
                FragmentRobotInform.setIndexRobot(i);
                FragmentRobotInform.setWorker(scriptExecute[i].getWorker());
                FragmentRobotInform.setProgram(scriptExecute[i].getProgram());
                // 표시
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new FragmentRobotInform());
                fragmentTransaction.commit();
            }
        });
    }

    public void Copy_MapArrange(Space[][] mapSpaces, int row, int col) {
        // 맵
        spaces = new Space[row][col];
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                Space s = mapSpaces[i][j];
                spaces[i][j] = new Space(s.getTag(), s.getNumObject(), s.isBlocked(), s.getExistRobotNum());
            }
        }
    }

    public void Copy_DrawArrange(int stagenumber, int row, int col) {
        ArrangeDrawMap map = Game_ArrangeActivity.getDrawmapArrange();
        // 지형 복사
        arrangeDrawMap.setRow(row);
        arrangeDrawMap.setCol(col);
        IndexSpace[] indexSpaces = new IndexSpace[map.getIndexSpaces().length];
        for (int i = 0; i < map.getIndexSpaces().length; ++i)
        {
            Space s = map.getIndexSpaces()[i].getSpace();

            Space temp = new Space(map.getIndexSpaces()[i].getSpace().getTag(),
                    map.getIndexSpaces()[i].getSpace().getNumObject(),
                    map.getIndexSpaces()[i].getSpace().isBlocked(),
                    map.getIndexSpaces()[i].getSpace().getExistRobotNum());

            indexSpaces[i] = new IndexSpace(map.getIndexSpaces()[i].getRow(),
                    map.getIndexSpaces()[i].getCol(), temp);
        }
        // 물건 복사
        totalObject = 0;
        LinkedList<ArrangeObject> arrangeObjects = new LinkedList<ArrangeObject>();
        for (int i = 0; i < map.getObjects().size(); ++i)
        {
            arrangeObjects.add(new ArrangeObject(map.getObjects().get(i).getRow(), map.getObjects().get(i).getCol(), map.getObjects().get(i).getNum()));
            totalObject += map.getObjects().get(i).getNum();
        }

        // 로봇 연결 리스트화
        LinkedList<Worker> listRobot = new LinkedList<Worker>();
        for (int i = 0; i < scriptExecute.length; ++i)
            listRobot.add(scriptExecute[i].getWorker());
        // 초기화
        arrangeDrawMap.setMapInfo(row, col, indexSpaces, arrangeObjects, listRobot);
    }

    public void CopyRobot(LinkedList<Worker> Listworkers, LinkedList<Program> Listprograms) {
        // 로봇, 프로그램 리소스 가져오기
        LinkedList<Worker> workers = Listworkers;
        LinkedList<Program> programs = Listprograms;

        // 게임 총 비용 기록
        gameRecord = new Arrange_GameRecord(workers, programs);
        textCost.setText(String.valueOf(gameRecord.getTotalCost()));

        // 명령문이 존재하는 명령문인가 (사용 가능 여부)
        for (int i = 0; i < programs.size(); ++i)
        {
            programs.get(i).Exist_Command();
            // 사용 가능한 프로그램 : 머리-꼬리 연결
            if (programs.get(i).isExist_Command())
                programs.get(i).Conenct_HeadTail();
        }

        // 로봇, 프로그램 복사
        scriptExecute = new ScriptExecute[workers.size()];
        for (int i = 0; i < scriptExecute.length; ++i)
        {
            Worker copiedWorker = GetCopyWorker(workers.get(i));
            // 프로그램 연결됨 + 명령문이 존재함 = 사용되는 로봇
            if (workers.get(i).getIndexProgram() != -1 && programs.get(workers.get(i).getIndexProgram()).isExist_Command())
            {
                scriptExecute[i] = new ScriptExecute(copiedWorker, programs.get(copiedWorker.getIndexProgram()));
                // 장애물에 걸려 있는 로봇?
                if (spaces[workers.get(i).getRow()][workers.get(i).getCol()].getTag() == SpaceType.OBSTACLE)
                    scriptExecute[i].setStatus_Obstacle(1);
                else
                    scriptExecute[i].setStatus_Obstacle(0);
            }
            else scriptExecute[i] = new ScriptExecute(copiedWorker, null);
        }

        // 로봇 정보 리스트 뷰
        ListView_worker = (ListView) findViewById(R.id.ListRobotInform);
        List_worker = new LinkedList<Worker>();
        for (int i = 0; i < scriptExecute.length; ++i)
            List_worker.add(scriptExecute[i].getWorker());
        Adapter_workerInformList = new Adapter_RobotInformList(getApplicationContext(), List_worker);
        ListView_worker.setAdapter(Adapter_workerInformList);
    }

    public Worker GetCopyWorker(Worker w)
    {
        Worker temp = new Worker(w.getName(), w.getEnergy(), w.getRow(), w.getCol(), w.getX(), w.getY(), w.getCost(), w.getStrength(), w.getSpeed());
        temp.setIndexProgram(w.getIndexProgram());
        return temp;
    }

    public int Check_SpaceStatus(int row, int col)
    {
        if (row < 0 || row >= spaceRow || col < 0 || col >= spaceCol)
            return -1;
        else if (spaces[row][col].getExistRobotNum() >= 0)
            return SpaceType.ROBOT;
        else if (spaces[row][col].getTag() == SpaceType.WALL)
            return SpaceType.WALL;
        else if (spaces[row][col].getTag() == SpaceType.OBSTACLE)
            return SpaceType.OBSTACLE;
        // 맵 안 + 로봇x + 벽x + 장애물x
        else return SpaceType.EMPTY;
    }

    public void Check_Move(int robotIdx)
    {
        Worker w = scriptExecute[robotIdx].getWorker();
        int direction = scriptExecute[robotIdx].getProgram().getScripts().get(scriptExecute[robotIdx].indexExcute).getNum();
        switch (direction) {
            // 상
            case MoveDir.UP: {
                for (int i = 1; i <= w.getSpeed(); ++i) {
                    int spaceStatus = Check_SpaceStatus(w.getRow() - i, w.getCol());
                    if (spaceStatus == -1 || spaceStatus == SpaceType.WALL || spaceStatus == SpaceType.OBSTACLE || spaceStatus == SpaceType.ROBOT) {
                        Move_Location(robotIdx, spaceStatus, w.getRow() - i, w.getCol());
                        return;
                    }
                }
                Move_Location(robotIdx, SpaceType.EMPTY, w.getRow() - w.getSpeed(), w.getCol());
                return;
            }
            // 하
            case MoveDir.DOWN: {
                for (int i = 1; i <= w.getSpeed(); ++i) {
                    int spaceStatus = Check_SpaceStatus(w.getRow() + i, w.getCol());
                    if (spaceStatus == -1 || spaceStatus == SpaceType.WALL || spaceStatus == SpaceType.OBSTACLE || spaceStatus == SpaceType.ROBOT) {
                        Move_Location(robotIdx, spaceStatus, w.getRow() + i, w.getCol());
                        return;
                    }
                }
                Move_Location(robotIdx, SpaceType.EMPTY, w.getRow() + w.getSpeed(), w.getCol());
                return;
            }
            // 좌
            case MoveDir.LEFT: {
                for (int i = 1; i <= w.getSpeed(); ++i) {
                    int spaceStatus = Check_SpaceStatus(w.getRow(), w.getCol() - i);
                    if (spaceStatus == -1 || spaceStatus == SpaceType.WALL || spaceStatus == SpaceType.OBSTACLE || spaceStatus == SpaceType.ROBOT) {
                        Move_Location(robotIdx, spaceStatus, w.getRow(), w.getCol() - i);
                        return;
                    }
                }
                Move_Location(robotIdx, SpaceType.EMPTY, w.getRow(), w.getCol() - w.getSpeed());
                return;
            }
            // 우
            case MoveDir.RIGHT: {
                for (int i = 1; i <= w.getSpeed(); ++i) {
                    int spaceStatus = Check_SpaceStatus(w.getRow(), w.getCol() + i);
                    if (spaceStatus == -1 || spaceStatus == SpaceType.WALL || spaceStatus == SpaceType.OBSTACLE || spaceStatus == SpaceType.ROBOT) {
                        Move_Location(robotIdx, spaceStatus, w.getRow(), w.getCol() + i);
                        return;
                    }
                }
                Move_Location(robotIdx, SpaceType.EMPTY, w.getRow(), w.getCol() + w.getSpeed());
                return;
            }
        }
    }

    public void Set_Robot_NewLocation(Worker w, int index, int row, int col)
    {
        spaces[w.getRow()][w.getCol()].setExistRobotNum(-1);
        if (row >= 0)
            w.setRow(row);
        else
            w.setCol(col);
        spaces[w.getRow()][w.getCol()].setExistRobotNum(index);
    }

    public void Move_Location(int index, int status, int collisionRow, int collisionCol)
    {
        Worker w = scriptExecute[index].getWorker();
        int direction = scriptExecute[index].getProgram().getScripts().get(scriptExecute[index].indexExcute).getNum();

        switch (direction)
        {
            // 상
            case MoveDir.UP: {
                if (status == -1) {
                    w.setY(arrangeDrawMap.getSTART_GRID());
                    Set_Robot_NewLocation(w, index, 0, -1);
                }
                else if (status == SpaceType.WALL) {
                    w.setY( arrangeDrawMap.getSTART_GRID() + ((collisionRow + 1) * arrangeDrawMap.getSPACE_LENGTH()) );
                    Set_Robot_NewLocation(w, index, collisionRow +1, -1);
                }
                else if (status == SpaceType.OBSTACLE) {
                    w.setY( arrangeDrawMap.getSTART_GRID() + collisionRow * arrangeDrawMap.getSPACE_LENGTH() );
                    scriptExecute[index].setStatus_Obstacle(1);
                    Set_Robot_NewLocation(w, index, collisionRow, -1);
                }
                else if (status == SpaceType.ROBOT) {
                    w.setY( arrangeDrawMap.getSTART_GRID() + ((collisionRow + 1) * arrangeDrawMap.getSPACE_LENGTH()) );
                    Set_Robot_NewLocation(w, index, collisionRow + 1, -1);
                    w.setEnergy(0); // 사망
                }
                else if (status == SpaceType.EMPTY)
                    Set_Robot_NewLocation(w, index, collisionRow, - 1);
                return;
            }
            // 하
            case MoveDir.DOWN: {
                if (status == -1) {
                    w.setY(maxY - arrangeDrawMap.getSPACE_LENGTH());
                    Set_Robot_NewLocation(w, index, spaceRow - 1, -1);
                }
                else if (status == SpaceType.WALL) {
                    w.setY( arrangeDrawMap.getSTART_GRID() + ((collisionRow - 1) * arrangeDrawMap.getSPACE_LENGTH()) );
                    Set_Robot_NewLocation(w, index, collisionRow - 1, -1);
                }
                else if (status == SpaceType.OBSTACLE) {
                    w.setY( arrangeDrawMap.getSTART_GRID() + collisionRow * arrangeDrawMap.getSPACE_LENGTH() );
                    scriptExecute[index].setStatus_Obstacle(1);
                    Set_Robot_NewLocation(w, index, collisionRow, -1);
                }
                else if (status == SpaceType.ROBOT) {
                    w.setY( arrangeDrawMap.getSTART_GRID() + ((collisionRow - 1) * arrangeDrawMap.getSPACE_LENGTH()) );
                    Set_Robot_NewLocation(w, index, collisionRow - 1, -1);
                    w.setEnergy(0); // 사망
                }
                else if (status == SpaceType.EMPTY)
                    Set_Robot_NewLocation(w, index, collisionRow, - 1);
                return;
            }
            // 좌
            case MoveDir.LEFT: {
                if (status == -1) {
                    w.setX(arrangeDrawMap.getSTART_GRID());
                    Set_Robot_NewLocation(w, index, -1, 0);
                }
                else if (status == SpaceType.WALL) {
                    w.setX( arrangeDrawMap.getSTART_GRID() + ((collisionCol + 1) * arrangeDrawMap.getSPACE_LENGTH()) );
                    Set_Robot_NewLocation(w, index, -1, collisionCol + 1);
                }
                else if (status == SpaceType.OBSTACLE) {
                    w.setX( arrangeDrawMap.getSTART_GRID() + collisionCol * arrangeDrawMap.getSPACE_LENGTH() );
                    scriptExecute[index].setStatus_Obstacle(1);
                    Set_Robot_NewLocation(w, index, -1, collisionCol);
                }
                else if (status == SpaceType.ROBOT) {
                    w.setX( arrangeDrawMap.getSTART_GRID() + ((collisionCol + 1) * arrangeDrawMap.getSPACE_LENGTH()) );
                    Set_Robot_NewLocation(w, index, -1, collisionCol + 1);
                    w.setEnergy(0); // 사망
                }
                else if (status == SpaceType.EMPTY)
                    Set_Robot_NewLocation(w, index, -1, collisionCol);
                return;
            }
            // 우
            case MoveDir.RIGHT: {
                if (status == -1) {
                    w.setX(maxX - arrangeDrawMap.getSPACE_LENGTH());
                    Set_Robot_NewLocation(w, index, -1, spaceCol - 1);
                }
                else if (status == SpaceType.WALL) {
                    w.setX( arrangeDrawMap.getSTART_GRID() + ((collisionCol - 1) * arrangeDrawMap.getSPACE_LENGTH()) );
                    Set_Robot_NewLocation(w, index, -1, collisionCol - 1);
                }
                else if (status == SpaceType.OBSTACLE) {
                    w.setX( arrangeDrawMap.getSTART_GRID() + collisionCol * arrangeDrawMap.getSPACE_LENGTH() );
                    scriptExecute[index].setStatus_Obstacle(1);
                    Set_Robot_NewLocation(w, index, -1, collisionCol);
                }
                else if (status == SpaceType.ROBOT) {
                    w.setX( arrangeDrawMap.getSTART_GRID() + ((collisionCol - 1) * arrangeDrawMap.getSPACE_LENGTH()) );
                    Set_Robot_NewLocation(w, index, -1, collisionCol - 1);
                    w.setEnergy(0); // 사망
                }
                else if (status == SpaceType.EMPTY)
                    Set_Robot_NewLocation(w, index, -1, collisionCol);
                return;
            }
        }
    }

    public boolean Usability_Robot(int indexRobot)
    {
        if (scriptExecute[indexRobot].getProgram() != null && scriptExecute[indexRobot].getWorker().getEnergy() > 0)
            return true;
        else return false;
    }

    public void Number_RobotAnimation(int index, int direction, int numMove)
    {
        switch (direction)
        {
            case MoveDir.UP:
                if ((numMove / 2) % 2 == 0) scriptExecute[index].getWorker().setNumberAnimation(1);
                else scriptExecute[index].getWorker().setNumberAnimation(2);
                return;
            case MoveDir.DOWN:
                if ((numMove / 2) % 2 == 0) scriptExecute[index].getWorker().setNumberAnimation(3);
                else scriptExecute[index].getWorker().setNumberAnimation(4);
                return;
            case MoveDir.LEFT:
                if ((numMove / 2) % 2 == 0) scriptExecute[index].getWorker().setNumberAnimation(5);
                else scriptExecute[index].getWorker().setNumberAnimation(6);
                return;
            case MoveDir.RIGHT:
                if ((numMove / 2) % 2 == 0) scriptExecute[index].getWorker().setNumberAnimation(7);
                else scriptExecute[index].getWorker().setNumberAnimation(8);
                return;
        }
    }

    public void CreateThread_ArrangeGame()
    {
        thread_game = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isStart)
                    {
                        // 게임 엔딩 여부
                        if (totalObject == curNumObject) {
                            GameEnd();
                            break;
                        }

                        // 1. 명령 탐색
                        for (int i = 0; i < scriptExecute.length; ++i)
                        {
                            if (Usability_Robot(i)) {
                                if (scriptExecute[i].status_Obstacle % 5 == 0)
                                    scriptExecute[i].Find_NextCommand(spaces);
                                else
                                    ++scriptExecute[i].status_Obstacle;
                            }
                        }
                        // 2. 명령 실행
                        for (int i = 0; i < 20; ++i)
                        {
                            for (int j = 0; j < scriptExecute.length; ++j)
                            {
                                if (Usability_Robot(j)) {
                                    // 선택된 스크립트 유형
                                    if (scriptExecute[j].status_Obstacle % 5 == 0)
                                    {
                                        Script s = scriptExecute[j].getProgram().getScripts().get(scriptExecute[j].indexExcute);
                                        switch (s.getType()) {
                                            case Arrange_ScrType.MOVE: {
                                                scriptExecute[j].Script_MoveCoord();
                                                Number_RobotAnimation(j, s.getNum(), i);
                                                if (i == 0)
                                                    scriptExecute[j].Update_RobotActionStatus();
                                                continue;
                                            }
                                            case Arrange_ScrType.PICK: {
                                                if (i == 0) {
                                                    scriptExecute[j].Script_Pick(spaces, arrangeDrawMap.getObjects());
                                                    scriptExecute[j].Update_RobotActionStatus();
                                                }
                                                continue;
                                            }
                                            case Arrange_ScrType.PUT: {
                                                if (i == 0) {
                                                    scriptExecute[j].Script_Put(spaces, arrangeDrawMap.getObjects());
                                                    scriptExecute[j].Update_RobotActionStatus();
                                                }
                                                continue;
                                            }
                                        }
                                    } else if (i == 0)
                                        scriptExecute[j].setStatus_Obstacle(scriptExecute[j].status_Obstacle + 1);
                                }
                            }
                            arrangeDrawMap.invalidate();
                            thread_game.sleep(50);
                        }

                        // 3. 상태 조정
                        for (int i = 0; i < scriptExecute.length; ++i)
                        {
                            if (Usability_Robot(i)) {
                                int type = scriptExecute[i].getProgram().getScripts().get(scriptExecute[i].indexExcute).getType();
                                if (scriptExecute[i].status_Obstacle % 5 == 0) {
                                    if (type == Arrange_ScrType.MOVE)
                                        Check_Move(i);
                                } else
                                    scriptExecute[i].setStatus_Obstacle(scriptExecute[i].status_Obstacle + 1);
                            }
                        }
                        arrangeDrawMap.invalidate();

                        // 4. 다음 명령으로 이동
                        for (int i = 0; i < scriptExecute.length; ++i)
                        {
                            if (Usability_Robot(i)) {
                                if (scriptExecute[i].status_Obstacle % 5 == 0)
                                    ++scriptExecute[i].indexExcute;
                                else
                                    scriptExecute[i].setStatus_Obstacle(scriptExecute[i].status_Obstacle + 1);
                            }
                        }

                        // 시간 경과
                        gameRecord.setTime(gameRecord.getTime() + 1);
                        gameRecord.setTotalAct(0);
                        for (int i = 0; i < scriptExecute.length; ++i)
                            gameRecord.setTotalAct(scriptExecute[i].getWorker().getNumAct());

                        // 5. 로봇 정보 업데이트 (UI 변경 스레드)
                        UIhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                //메인스레드가 처리한 작업의 번호
                                textTime.setText(String.valueOf(gameRecord.getTime()));
                                textAct.setText(String.valueOf(gameRecord.getTotalAct()));
                                textNumObject.setText(String.valueOf(curNumObject) + " / " + String.valueOf(totalObject));
                                Adapter_workerInformList.notifyDataSetChanged();
                            }
                        });

                        thread_game.sleep(100);
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void Stop_Thread()
    {
        isStart = false;
        BtnStart.setText("중지");
        BtnStart.setBackgroundResource(R.drawable.button_borderline2);
    }

    public void GameEnd()
    {
        // 게임 종료 소리 + bgm 해제
        GameEnd_Sound();
        if (!AudioService_GameBGM.mute)
        {
            Intent intent_audioGame = new Intent(getApplicationContext(), AudioService_GameBGM.class);
            stopService(intent_audioGame);
        }

        // 결과 출력
        Intent intent = new Intent(getApplicationContext(), Popup_GameEndActivity.class);
        intent.putExtra("cost", gameRecord.getTotalCost());
        intent.putExtra("time", gameRecord.getTime());
        intent.putExtra("act", gameRecord.getTotalAct());

        // 게임 모드에 따른 결과 처리 방법
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { }
        };

        // 게임 모드
        switch (MapInfo.getGameMode())
        {
            case MapInfo.SINGLE_MODE: {
                CurUserData.LevelUp(40);
                RecordRequest recordRequest = new RecordRequest(CurUserData.userID, GameExp.SINGLE, responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(SimulationActivity.this);
                requestQueue.add(recordRequest);
                intent.putExtra("result", 0);
                break;
            }
            case MapInfo.CHALLENGE_MODE:
            {
                if (MapInfo.getCompeteUser() != null)
                {
                    RecordRequest recordRequest = new RecordRequest(0, MapInfo.getCompeteUser(), CurUserData.userID, CurUserData.userLevel, CurUserData.userExp, CurUserData.userWIN, CurUserData.userLOSE, MapInfo.getStagenumber(), gameRecord.getTotalCost(), gameRecord.getTime(), gameRecord.getTotalAct(), responseListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(SimulationActivity.this);
                    requestQueue.add(recordRequest);
                }
                break;
            }
            case MapInfo.APPROVAL_MODE:
            {
                // 결과 비교
                int me = 0;
                int challenger = 0;
                if (gameRecord.getTotalCost() < Challenger_Record.getRecordCost()) ++me;
                else if (gameRecord.getTotalCost() > Challenger_Record.getRecordCost()) ++challenger;

                if (gameRecord.getTime() < Challenger_Record.getRecordTime()) ++me;
                else if (gameRecord.getTime() > Challenger_Record.getRecordTime()) ++challenger;

                if (gameRecord.getTotalAct() < Challenger_Record.getRecordAction()) ++me;
                else if (gameRecord.getTotalAct() > Challenger_Record.getRecordAction()) ++challenger;

                int flag;
                RequestQueue requestQueue = Volley.newRequestQueue(SimulationActivity.this);
                // 승패 및 경험치 차등 처리
                if (me > challenger)
                {
                    CurUserData.LevelUp(GameExp.WIN);
                    ++CurUserData.userWIN;
                    intent.putExtra("result", 1);
                    flag = 1;
                }
                else if (me < challenger)
                {
                    CurUserData.LevelUp(GameExp.LOSE);
                    ++CurUserData.userLOSE;
                    intent.putExtra("result", 2);
                    flag = 2;
                }
                else
                {
                    CurUserData.LevelUp(GameExp.DRAW);
                    intent.putExtra("result", 3);
                    flag = 3;
                }
                switch (flag)
                {
                    case 1: {
                        RecordRequest recordRequest = new RecordRequest(CurUserData.userID, GameExp.WIN, responseListener);
                        RecordRequest recordRequest2 = new RecordRequest(Challenger_Record.getSenderID(), GameExp.LOSE, responseListener);
                        RecordRequest recordRequest3 = new RecordRequest(1, 0, CurUserData.userID, responseListener);
                        RecordRequest recordRequest4 = new RecordRequest(0, 1, Challenger_Record.getSenderID(), responseListener);
                        requestQueue.add(recordRequest);
                        requestQueue.add(recordRequest2);
                        requestQueue.add(recordRequest3);
                        requestQueue.add(recordRequest4);
                        break;
                    }
                    case 2: {
                        RecordRequest recordRequest = new RecordRequest(CurUserData.userID, GameExp.LOSE, responseListener);
                        RecordRequest recordRequest2 = new RecordRequest(Challenger_Record.getSenderID(), GameExp.WIN, responseListener);
                        RecordRequest recordRequest3 = new RecordRequest(0, 1, CurUserData.userID, responseListener);
                        RecordRequest recordRequest4 = new RecordRequest(1, 0, Challenger_Record.getSenderID(), responseListener);
                        requestQueue.add(recordRequest);
                        requestQueue.add(recordRequest2);
                        requestQueue.add(recordRequest3);
                        requestQueue.add(recordRequest4);
                        break;
                    }
                    case 3: {
                        RecordRequest recordRequest = new RecordRequest(CurUserData.userID, GameExp.DRAW, responseListener);
                        RecordRequest recordRequest2 = new RecordRequest(Challenger_Record.getSenderID(), GameExp.DRAW, responseListener);
                        requestQueue.add(recordRequest);
                        requestQueue.add(recordRequest2);
                        break;
                    }
                }
                break;
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    public void GameExit()
    {
        // BGM 전환
        if (AudioService_MainBGM.mute == false && AudioService_GameBGM.mute == false) {
            Intent intent_audioMain = new Intent(getApplicationContext(), AudioService_MainBGM.class);
            startService(intent_audioMain);
        }
        Intent intent;
        if (MapInfo.getGameMode() == MapInfo.SINGLE_MODE)
            intent = new Intent(getApplicationContext(), Stage_ArrangeActivity.class);
        else
            intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
        // 맵 정보 삭제 -> 새로운 게임에서 받을 맵 정보 저장고 비우기
        MapInfo.getArrangeObject().clear();
        finish();
    }

    // 게임 도중 뒤로 가기 버튼 클릭
    @Override
    public void onBackPressed() {
        // 스레드 중지
        Stop_Thread();
        AlertDialog.Builder builder = new AlertDialog.Builder(SimulationActivity.this);
        builder.setTitle("게임종료");
        builder.setIcon(R.drawable.icon_warning);
        dialog = builder.setMessage("게임을 종료하시겠습니까?\n사용 중인 데이터는 저장되지 않습니다.")
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "시뮬레이션 종료", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                .setNegativeButton("취소", null)
                .create();
        dialog.show();
    }

    // 클릭 효과음
    public void GameEnd_Sound()
    {
        Intent intent_audio = new Intent(getApplicationContext(), AudioService_GameEnd.class);
        startService(intent_audio);
    }

    public static int getCurNumObject() {
        return curNumObject;
    }

    public static void setCurNumObject(int curNumObject) {
        SimulationActivity.curNumObject = curNumObject;
    }

    @Override
    public void TurnOff_Inform() {
        list_robot.setVisibility(View.VISIBLE);
        fragment.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onUserLeaveHint() {
        // 스레드 중지
        Stop_Thread();
        super.onUserLeaveHint();
    }
}
