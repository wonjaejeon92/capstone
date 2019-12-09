package e.t.gameworkshop.Arrange;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.LinkedList;
import e.t.gameworkshop.Challenger_Record;
import e.t.gameworkshop.GameExp;
import e.t.gameworkshop.MapInfo;
import e.t.gameworkshop.RecordRequest;
import e.t.gameworkshop.SimulationActivity;
import e.t.gameworkshop.AudioService_GameBGM;
import e.t.gameworkshop.AudioService_MainBGM;
import e.t.gameworkshop.IndexSpace;
import e.t.gameworkshop.Popup_SettingActivity;
import e.t.gameworkshop.PrevIndexRobot;
import e.t.gameworkshop.Program;
import e.t.gameworkshop.R;
import e.t.gameworkshop.Robot;
import e.t.gameworkshop.ScriptManager;
import e.t.gameworkshop.Space;

public class Game_ArrangeActivity extends AppCompatActivity implements FragmentRobot.Data_FragToActivity, FragmentProgramming.InterfaceProgram,
FragmentScript.InterfaceScript{

    private static Space[][] spaces;
    public static ArrangeDrawMap drawmapArrange;
    private PrevIndexRobot prevIndexRobot;
    private boolean selRobot = false, selProgram = false, selConnect = false;
    private int indexRobotAdd;
    private static LinkedList<Worker> workers;
    private static LinkedList<Program> programs;
    private TextView textRobot, textProgram, textConnect, textSimulation, textWall, textSave, textObstacle;
    private Button BtnSettings, BtnSimulation, BtnfragRobot, BtnfragProgram, BtnfragConnect;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로 모드 고정
        setContentView(R.layout.activity_game__arrange);

        BtnSettings = (Button) findViewById(R.id.BtnSettings);
        BtnSimulation = (Button) findViewById(R.id.BtnSimulation);
        BtnfragRobot = (Button) findViewById(R.id.fragRobot);
        BtnfragProgram = (Button) findViewById(R.id.fragProgram);
        BtnfragConnect = (Button) findViewById(R.id.fragConnect);
        final RelativeLayout fragment = (RelativeLayout) findViewById(R.id.fragment);
        final LinearLayout notice = (LinearLayout) findViewById(R.id.notice);
        prevIndexRobot = new PrevIndexRobot();
        Description();

        // 맵 전체 데이터 저장
        int row = MapInfo.getRow();
        int col = MapInfo.getCol();
        spaces = new Space[row][col];
        for (int i = 0; i < row; ++i)
            for (int j = 0; j < col; ++j)
                spaces[i][j] = new Space(0, 0, false);
         // 지형
        for (int i = 0; i < MapInfo.getIndexSpaces().length; ++i)
        {
            IndexSpace s = MapInfo.getIndexSpaces()[i];
            spaces[s.getRow()][s.getCol()].setTag(s.getSpace().getTag());
            spaces[s.getRow()][s.getCol()].setBlocked(s.getSpace().isBlocked());
        }
        // 물건
        ArrayList<ArrangeObject> list = MapInfo.getArrangeObject();
        for (int i = 0; i < list.size(); ++i)
            spaces[list.get(i).getRow()][list.get(i).getCol()].setNumObject(list.get(i).getNum());

        // 로봇, 프로그램 리스트
        workers = new LinkedList<Worker>();
        programs = new LinkedList<Program>();

        // 맵 디자인 규모 설정
        drawmapArrange = (ArrangeDrawMap) findViewById(R.id.painter);
        drawmapArrange.setMapInfo(MapInfo.getStagenumber(), row, col, MapInfo.getIndexSpaces(), MapInfo.getArrangeObject(), workers);

        // Service 소리 켜기
        if (AudioService_GameBGM.mute == false) {
            Intent intent_audio = new Intent(getApplicationContext(), AudioService_GameBGM.class);
            startService(intent_audio);
            // Service 소리 켜기
            Intent intent_audio2 = new Intent(getApplicationContext(), AudioService_MainBGM.class);
            stopService(intent_audio2);
        }


        BtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Game_ArrangeActivity.this, Popup_SettingActivity.class);
                intent.putExtra("activity", 2);     // Game BGM 번호
                Game_ArrangeActivity.this.startActivity(intent);
            }
        });

        BtnfragRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selRobot == false) {
                    selRobot = true;
                    selProgram = false;        // 다른 프래그먼트를 클릭했을 경우 이전 프래그먼트는 종료됨
                    selConnect = false;
                    fragment.setVisibility(View.VISIBLE);
                    notice.setVisibility(View.INVISIBLE);
                    BtnfragRobot.setBackgroundResource(R.drawable.button_borderline_selected);
                    BtnfragProgram.setBackgroundResource(R.drawable.button_borderline);
                    BtnfragConnect.setBackgroundResource(R.drawable.button_borderline);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, new FragmentRobot());
                    fragmentTransaction.commit();
                } else {
                    selRobot = false;
                    BtnfragRobot.setBackgroundResource(R.drawable.button_borderline);
                    fragment.setVisibility(View.INVISIBLE);
                    notice.setVisibility(View.VISIBLE);
                }
            }
        });

        BtnfragProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selProgram == false) {
                    selRobot = false;
                    selProgram = true;
                    selConnect = false;
                    fragment.setVisibility(View.VISIBLE);
                    notice.setVisibility(View.INVISIBLE);
                    BtnfragRobot.setBackgroundResource(R.drawable.button_borderline);
                    BtnfragProgram.setBackgroundResource(R.drawable.button_borderline_selected);
                    BtnfragConnect.setBackgroundResource(R.drawable.button_borderline);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, new FragmentProgramming());
                    fragmentTransaction.commit();
                } else {
                    selProgram = false;
                    BtnfragProgram.setBackgroundResource(R.drawable.button_borderline);
                    fragment.setVisibility(View.INVISIBLE);
                    notice.setVisibility(View.VISIBLE);
                }
            }
        });

        BtnfragConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selConnect == false) {
                    selRobot = false;
                    selProgram = false;
                    selConnect = true;
                    fragment.setVisibility(View.VISIBLE);
                    notice.setVisibility(View.INVISIBLE);
                    BtnfragRobot.setBackgroundResource(R.drawable.button_borderline);
                    BtnfragProgram.setBackgroundResource(R.drawable.button_borderline);
                    BtnfragConnect.setBackgroundResource(R.drawable.button_borderline_selected);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, new FragmentConnect());
                    fragmentTransaction.commit();
                } else {
                    selConnect = false;
                    BtnfragConnect.setBackgroundResource(R.drawable.button_borderline);
                    fragment.setVisibility(View.INVISIBLE);
                    notice.setVisibility(View.VISIBLE);
                }
            }
        });

        BtnSimulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Game_ArrangeActivity.this, SimulationActivity.class);
                intent.putExtra("game", 1);
                Game_ArrangeActivity.this.startActivity(intent);
            }
        });
    }

    public void Description()
    {
        textRobot = (TextView) findViewById(R.id.textDescRobot);
        textProgram = (TextView) findViewById(R.id.textDescProgram);
        textConnect = (TextView) findViewById(R.id.textDescConnect);
        textSimulation = (TextView) findViewById(R.id.textDescSimulation);
        textWall = (TextView) findViewById(R.id.textDescWall);
        textSave = (TextView) findViewById(R.id.textDescSave);
        textObstacle = (TextView) findViewById(R.id.textDescObstacle);

        String s = "로봇 생성\n다양한 성능의 로봇을 선택하여 맵에 배치하세요.";
        SpannableStringBuilder ssb = new SpannableStringBuilder("로봇 생성\n다양한 성능의 로봇을 선택하여 맵에 배치하세요.");
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#3DB7CC")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textRobot.setText(ssb);

        s = "프로그램 생성\n로봇을 작동시키는 프로그램을 생성하세요.";
        ssb = new SpannableStringBuilder(s);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#3DB7CC")), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textProgram.setText(ssb);

        s = "로봇-프로그램 연결\n생성한 로봇에 프로그램을 연결하세요.";
        ssb = new SpannableStringBuilder(s);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#3DB7CC")), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textConnect.setText(ssb);

        s = "모의 실험\n설계가 완료되었으면 실험을 통해 결과를 확인하세요.\n원하지 않은 결과였다면 다시 설계할 수 있습니다.";
        ssb = new SpannableStringBuilder(s);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#3DB7CC")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textSimulation.setText(ssb);

        s = "벽\n벽 공간에는 이동할 수 없습니다.\n이동 시 이전 공간으로 되돌아옵니다.";
        ssb = new SpannableStringBuilder(s);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#3DB7CC")), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textWall.setText(ssb);

        s = "저장고\n로봇으로 물건을 집어 저장고 위치에 내려 놓으면 물건이 저장됩니다.";
        ssb = new SpannableStringBuilder(s);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#3DB7CC")), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textSave.setText(ssb);

        s = "장애물\n장애물에 도착한 로봇은 명령을 1초 지연 후 수행합니다.\n행동량 및 전력에 영향이 없습니다.";
        ssb = new SpannableStringBuilder(s);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#3DB7CC")), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textObstacle.setText(ssb);
    }

    // 게임 도중 뒤로 가기 버튼 클릭
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Game_ArrangeActivity.this);
        builder.setTitle("게임종료");
        builder.setIcon(R.drawable.icon_warning);
        dialog = builder.setMessage("게임을 종료하시겠습니까?\n사용 중인 데이터는 저장되지 않으며\n대전 도중 퇴장일 경우 대전이 취소됩니다.")
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Service 소리 종료
                                if (AudioService_MainBGM.mute == false && AudioService_GameBGM.mute == false) {
                                    Intent intent_audio = new Intent(getApplicationContext(), AudioService_MainBGM.class);
                                    startService(intent_audio);
                                }
                                Intent intent_audio = new Intent(getApplicationContext(), AudioService_GameBGM.class);
                                stopService(intent_audio);

                                // 대전 상태일 시 챌린저 싱글 경험치 획득
                                if (MapInfo.getGameMode() == MapInfo.APPROVAL_MODE)
                                {
                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) { }
                                    };
                                    RecordRequest recordRequest = new RecordRequest(Challenger_Record.getSenderID(), GameExp.SINGLE, responseListener);
                                    RequestQueue requestQueue = Volley.newRequestQueue(Game_ArrangeActivity.this);
                                    requestQueue.add(recordRequest);
                                }

                                Toast.makeText(getApplicationContext(), "게임이 종료되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Game_ArrangeActivity.this, Stage_ArrangeActivity.class);
                                Game_ArrangeActivity.this.startActivity(intent);
                                // 맵 정보 삭제 -> 새로운 게임에서 받을 맵 정보 저장고 비우기
                                MapInfo.getArrangeObject().clear();
                                finish();
                            }
                        })
                .setNegativeButton("취소", null)
                .create();
        dialog.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_UP: {
                int x = (int) event.getX();
                int y = (int) event.getY();
                // 맵의 어떤 칸을 클릭한 좌표의 인덱스
                int start = drawmapArrange.getSTART_GRID();
                int len = drawmapArrange.getSPACE_LENGTH();
                int TouchRow = (y - start) / len;
                int TouchCol = (x - start) / len;

                // 맵 칸을 클릭했는가
                int spaceRobot;         // 칸에 로봇이 존재?
                boolean spaceBlock;     // 막혀있는 칸?
                if (TouchRow >= 0 && TouchCol >= 0 && TouchRow <= drawmapArrange.getRow() - 1 && TouchCol <= drawmapArrange.getCol() - 1) {
                    spaceRobot = spaces[TouchRow][TouchCol].getExistRobotNum();
                    spaceBlock = spaces[TouchRow][TouchCol].isBlocked();
                } else
                    return true;

                // 로봇 생성 버튼 ON
                if (FragmentRobot.getSelectStatus() >= 0) {
                    // 맵 안의 좌표에 존재하고 공간이 있음
                    if (!spaceBlock) {
                        // 다른 기능(이동 혹은 삭제) 비활성화
                        drawmapArrange.setClickedRobot(false);
                        // 로봇 리스트에 로봇 추가
                        AddRobot(FragmentRobot.getWorkerList().get(indexRobotAdd), TouchRow, TouchCol);

                        // 추가된 로봇이 위치한 인덱스의 xy 좌표
                        workers.get(workers.size() - 1).setX(start + len * TouchCol);
                        workers.get(workers.size() - 1).setY(start + len * TouchRow);
                        Update_SpaceStatus(1, TouchRow, TouchCol);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "맵 안의 빈 공간에만 위치할 수 있습니다.\n취소하려면 로봇을 한 번 더 클릭하세요.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

                // 로봇 생성 버튼 OFF
                else
                {
                    // 로봇 위치 이동 모드-------------------------------------------------------------------------------------------------------
                    if (spaceRobot >= 0 && drawmapArrange.isClickedRobot() == false && FragmentRobot.getSelectStatus() == -2) {
                        // 선택 표시 활성화 및 자리 비우기
                        drawmapArrange.setClickedRobot(true);

                        // 선택 표시(노란색으로 색칠) 좌표 설정
                        drawmapArrange.clickRow = workers.get(spaceRobot).getRow();
                        drawmapArrange.clickCol = workers.get(spaceRobot).getCol();

                        // 선택된 로봇 정보 저장 (빠른 접근)
                        prevIndexRobot.setRow(TouchRow);
                        prevIndexRobot.setCol(TouchCol);
                        prevIndexRobot.setIndex(spaces[TouchRow][TouchCol].getExistRobotNum());
                    }
                    // 이동할 로봇이 선택되어 있음
                    else if (drawmapArrange.isClickedRobot() == true && FragmentRobot.getSelectStatus() == -2)
                    {
                        drawmapArrange.setClickedRobot(false);

                        // 이동할 수 있는 공간
                        if (!spaceBlock) {
                            int i = prevIndexRobot.getIndex();
                            int r = prevIndexRobot.getRow();
                            int c = prevIndexRobot.getCol();
                            // 이전 자리 비움
                            spaces[r][c].setExistRobotNum(-1);
                            spaces[r][c].setBlocked(false);
                            // 새 자리 저장
                            spaces[TouchRow][TouchCol].setExistRobotNum(i);
                            spaces[TouchRow][TouchCol].setBlocked(true);
                            workers.get(i).setRow(TouchRow);
                            workers.get(i).setCol(TouchCol);
                            workers.get(i).setX(start + len * TouchCol);
                            workers.get(i).setY(start + len * TouchRow);
                        }
                    }
                    // 로봇 삭제 모드-----------------------------------------------------------------------------------------------------------------
                    else if (spaceRobot >= 0 && drawmapArrange.isClickedRobot() == false && FragmentRobot.getSelectStatus() == -3)
                    {
                        Update_SpaceStatus(2, TouchRow, TouchCol);
                        // 연결된 프로그램 존재
                        if (workers.get(spaceRobot).getIndexProgram() >= 0)
                        {
                            Program DelRobProgram = programs.get(workers.get(spaceRobot).getIndexProgram());
                            // 리스트에서 제거
                            for (int i = 0; i < DelRobProgram.getNum_RobotUse().size(); ++i)
                            {
                                if (spaceRobot == DelRobProgram.getNum_RobotUse().get(i))
                                {
                                    DelRobProgram.getNum_RobotUse().remove(i);
                                    // 삭제 로봇 번호 뒤 재정렬
                                    for (int j = i; j < DelRobProgram.getNum_RobotUse().size(); ++j)
                                        DelRobProgram.getNum_RobotUse().set(j, DelRobProgram.getNum_RobotUse().get(j) - 1);
                                    break;
                                }
                            }
                        }
                        workers.remove(spaceRobot);
                        // 삭제된 번호부터 차례로 공간 재정렬
                        for (int i = spaceRobot; i < workers.size(); ++i) {
                            spaces[workers.get(i).getRow()][workers.get(i).getCol()].setExistRobotNum(i);
                        }
                    }

                }

                // 맵 업데이트
                drawmapArrange.invalidate();
                break;
            }
        }
        return true;
    }

    public void Update_SpaceStatus(int numberMode, int row, int col)
    {
        // 추가
        if (numberMode == 1)
        {
            spaces[row][col].setExistRobotNum(workers.size() - 1);
            spaces[row][col].setBlocked(true);    // 공간 상태 변경
            Robot.setNumOfRobot(Robot.getNumOfRobot() + 1);
        }
        // 삭제
        else if (numberMode == 2)
        {
            spaces[row][col].setExistRobotNum(-1);
            spaces[row][col].setBlocked(false);
            Robot.setNumOfRobot(Robot.getNumOfRobot() -1);
        }
    }

    @Override
    public void AddRobot(Worker w, int row, int col) {
        workers.add(new Worker(w.getName(), w.getEnergy(), row, col, w.getX(),
                w.getY(), w.getCost(), w.getStrength(), w.getSpeed()));
    }

    @Override
    public void ViewScript() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, new FragmentScript());
        fragmentTransaction.commit();
    }

    @Override
    public void ViewProgram() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, new FragmentProgramming());
        fragmentTransaction.commit();
    }

    @Override
    public void OnLongClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Game_ArrangeActivity.this);
        builder.setTitle("범위 삭제");
        builder.setIcon(R.drawable.icon_warning);
        dialog = builder.setMessage("선택한 범위를 삭제하시겠습니까?")
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                // 삭제 후 갱신
                                for (int j = ScriptManager.getSel1(); j <= ScriptManager.getSel2(); ++j)
                                    FragmentScript.getList_script().remove(ScriptManager.getSel1());
                                FragmentScript.getAdapter_ScriptList().notifyDataSetChanged();

                                ScriptManager.setSel1(-1);
                                ScriptManager.setSel2(-1);
                            }
                        })
                .setNegativeButton("취소", null)
                .create();
        dialog.show();
    }

    @Override
    public void GetIndexRobot(int indexRobotAdd) {
        this.indexRobotAdd = indexRobotAdd;
    }

    @Override
    public void RobotMoveStatus(boolean status) {
        drawmapArrange.setClickedRobot(status);
        drawmapArrange.invalidate();
    }

    public static LinkedList<Worker> getWorkers() {
        return workers;
    }

    public static LinkedList<Program> getPrograms() {
        return programs;
    }

    public static ArrangeDrawMap getDrawmapArrange() {
        return drawmapArrange;
    }

    public static Space[][] getSpaces() {
        return spaces;
    }


}