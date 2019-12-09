package e.t.gameworkshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import e.t.gameworkshop.Arrange.FragmentConnect;
import e.t.gameworkshop.Arrange.Game_ArrangeActivity;
import e.t.gameworkshop.Arrange.Worker;

public class Popup_ConnectActivity extends Activity {

    private static ListView ListView_robot;
    private static Adapter_PopupWorkerList Adapter_popup_workerList;
    private static List<Worker> List_robot;
    private static int indexProgram;
    private Program program;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 타이틀바 삭제
        setContentView(R.layout.activity_popup_connect);

        // 로봇 리스트
        List_robot = Game_ArrangeActivity.getWorkers();
        // 연결할 프로그램
        program = Game_ArrangeActivity.getPrograms().get(indexProgram);
        ListView_robot = (ListView) findViewById(R.id.listConnect);
        Adapter_popup_workerList = new Adapter_PopupWorkerList(getApplicationContext(), List_robot);
        ListView_robot.setAdapter(Adapter_popup_workerList);
        ListView_robot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 추가
                if (List_robot.get(i).getIndexProgram() != indexProgram) {
                    // 다른 프로그램에 연결된 로봇 -> 제거
                    if (List_robot.get(i).getIndexProgram() != -1) {
                        // 프로그램 번호의 로봇 리스트 탐색하여 제거
                        // 연결되어있던 프로그램
                        Program p = Game_ArrangeActivity.getPrograms().get(List_robot.get(i).getIndexProgram());
                        for (int j = 0; j < i + 1; ++j) {
                            if (p.getNum_RobotUse().size() <= j) break;
                            if (p.getNum_RobotUse().get(j) == i)
                                p.getNum_RobotUse().remove(j);
                        }
                    }
                    // 새로운 연결
                    program.getNum_RobotUse().add(i);
                    List_robot.get(i).setIndexProgram(indexProgram);
                    Adapter_popup_workerList.notifyDataSetChanged();
                }
                // 삭제
                else {
                    // 연결 해제할 로봇이 프로그램 탐색
                    for (int j = 0; j < program.getNum_RobotUse().size(); ++j) {
                        if (program.getNum_RobotUse().get(j) == i) {
                            program.getNum_RobotUse().remove(j);
                            List_robot.get(i).setIndexProgram(-1);
                            Adapter_popup_workerList.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    public void mOnConfirm(View v) {
        ClickSound();

        // 로봇 순서 정렬(삽입)
        LinkedList<Integer> list = program.getNum_RobotUse();
        for (int i = 1; i < list.size(); ++i) {
            for (int j = i; j > 0; --j) {
                if (list.get(j - 1) > list.get(j)) {
                    int temp = list.get(j - 1);
                    list.set(j - 1, list.get(j));
                    list.set(j, temp);
                } else
                    break;
            }
        }
        // 리스트 갱신
        FragmentConnect.getAdapter_programConnectList().notifyDataSetChanged();

        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }


    // 클릭 효과음
    public void ClickSound() {
        if (AudioService_Click.mute == false) {
            Intent intent_audio = new Intent(getApplicationContext(), AudioService_Click.class);
            startService(intent_audio);
        }
    }

    public static void setIndexProgram(int indexProgram) {
        Popup_ConnectActivity.indexProgram = indexProgram;
    }
}
