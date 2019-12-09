package e.t.gameworkshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import e.t.gameworkshop.Arrange.FragmentScript;

public class Popup_Script_MoveActivity extends Activity {

    ImageButton BtnUp, BtnDown, BtnLeft, BtnRight, BtnCenter;
    private static int indexCommand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 타이틀바 삭제
        setContentView(R.layout.activity_popup_script_move);
        BtnUp = (ImageButton) findViewById(R.id.BtnUp);
        BtnDown = (ImageButton) findViewById(R.id.BtnDown);
        BtnLeft = (ImageButton) findViewById(R.id.BtnLeft);
        BtnRight = (ImageButton) findViewById(R.id.BtnRight);
        BtnCenter = (ImageButton) findViewById(R.id.BtnCenter);

        BtnCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentScript.getList_script().get(indexCommand).setNum(0);
                FragmentScript.getAdapter_ScriptList().notifyDataSetChanged();
                finish();
            }
        });

        BtnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 위 1
                FragmentScript.getList_script().get(indexCommand).setNum(-1);
                FragmentScript.getAdapter_ScriptList().notifyDataSetChanged();
                finish();
            }
        });

        BtnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 아래 2
                FragmentScript.getList_script().get(indexCommand).setNum(-2);
                FragmentScript.getAdapter_ScriptList().notifyDataSetChanged();
                finish();
            }
        });

        BtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 좌 3
                FragmentScript.getList_script().get(indexCommand).setNum(-3);
                FragmentScript.getAdapter_ScriptList().notifyDataSetChanged();
                finish();
            }
        });

        BtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 우 4
                FragmentScript.getList_script().get(indexCommand).setNum(-4);
                FragmentScript.getAdapter_ScriptList().notifyDataSetChanged();
                finish();
            }
        });

    }

    // 클릭 효과음
    public void ClickSound() {
        if (AudioService_Click.mute == false) {
            Intent intent_audio = new Intent(getApplicationContext(), AudioService_Click.class);
            startService(intent_audio);
        }
    }

    public static void setIndexCommand(int indexCommand) {
        Popup_Script_MoveActivity.indexCommand = indexCommand;
    }
}
