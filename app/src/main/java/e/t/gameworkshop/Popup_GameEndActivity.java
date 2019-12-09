package e.t.gameworkshop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import e.t.gameworkshop.Arrange.FragmentProgramming;
import e.t.gameworkshop.Arrange.FragmentScript;
import e.t.gameworkshop.Arrange.Game_ArrangeActivity;

public class Popup_GameEndActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 타이틀바 삭제
        setContentView(R.layout.activity_popup_gameend);

        TextView TextResult = (TextView) findViewById(R.id.TextResult);
        TextView TextCost = (TextView) findViewById(R.id.TextCost);
        TextView TextTime = (TextView) findViewById(R.id.TextTime);
        TextView TextAct = (TextView) findViewById(R.id.TextAct);

        Intent intent = getIntent();
        int cost = intent.getIntExtra("cost", 0);
        int time = intent.getIntExtra("time", 0);
        int act = intent.getIntExtra("act", 0);
        int result = intent.getIntExtra("result", -1);


        if (result == 1)
        {
            TextResult.setText("승리하였습니다!");
            TextResult.setTextColor(Color.CYAN);
        }
        else if (result == 2)
        {
            TextResult.setText("패배하였습니다..");
            TextResult.setTextColor(Color.LTGRAY);
        }
        else if (result == 3)
        {
            TextResult.setText("무승부입니다.");
            TextResult.setTextColor(Color.BLACK);
        }
        else TextResult.setVisibility(View.GONE);

        TextCost.append(String.valueOf(cost));
        TextTime.append(String.valueOf(time));
        TextAct.append(String.valueOf(act));
    }

    public void mOnConfirm(View v) {
        ClickSound();
        ((SimulationActivity)SimulationActivity.mContext).GameExit();
        ((SimulationActivity)SimulationActivity.mContext).Stop_Thread();
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
}
