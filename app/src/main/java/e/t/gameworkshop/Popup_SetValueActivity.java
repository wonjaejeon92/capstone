package e.t.gameworkshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import e.t.gameworkshop.Arrange.FragmentConnect;
import e.t.gameworkshop.Arrange.FragmentProgramming;
import e.t.gameworkshop.Arrange.FragmentScript;
import e.t.gameworkshop.Arrange.Game_ArrangeActivity;

public class Popup_SetValueActivity extends Activity {

    private TextView TextQuestion;
    private EditText EditValue;
    private final static int PROGRAM_NAME = 0, FOR_NUMBER = 1;
    private static int callActivity;
    private static int indexFromList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 타이틀바 삭제
        setContentView(R.layout.activity_popup_set_value);

        TextQuestion = (TextView) findViewById(R.id.TextQuestion);
        EditValue = (EditText) findViewById(R.id.EditProgramName);

        switch (callActivity) {
            case PROGRAM_NAME:
                TextQuestion.setText("프로그램 이름을 입력하세요.");
                break;
            case FOR_NUMBER:
                TextQuestion.setText("반복 횟수를 입력하세요.\n(무한 : -1)");
                EditValue.setInputType(0x00001002);     // 양수, 음수만 입력 가능
                break;
        }
    }

    public void mOnConfirm(View v) {
        ClickSound();

        // 확인
        String value = EditValue.getText().toString();
        switch (callActivity) {
            case PROGRAM_NAME: {
                // 저장 및 갱신
                Game_ArrangeActivity.getPrograms().add(new Program(value));
                FragmentProgramming.getAdapter_programList().notifyDataSetChanged();
                break;
            }
            case FOR_NUMBER: {
                // 값이 존재하고 0보다 클 경우에만 저장
                if (!value.equals("") && Integer.parseInt(value) > 0)
                    FragmentScript.getList_script().get(indexFromList).setNum(Integer.parseInt(value));
                else if (value.equals(""))
                    Toast.makeText(getApplicationContext(), "저장 실패 : 값이 없습니다.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "저장 실패 : 값이 1보다 작을 수 없습니다.", Toast.LENGTH_SHORT).show();
                FragmentScript.getAdapter_ScriptList().notifyDataSetChanged();
                break;
            }
        }

        finish();
    }

    public void mOnCencel(View v) {
        ClickSound();
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

    public static void setCallActivity(int callActivity) {
        Popup_SetValueActivity.callActivity = callActivity;
    }

    public static int getProgramName() {
        return PROGRAM_NAME;
    }

    public static int getForNumber() {
        return FOR_NUMBER;
    }

    public static void setIndexFromList(int indexFromList) {
        Popup_SetValueActivity.indexFromList = indexFromList;
    }
}
