package e.t.gameworkshop;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import e.t.gameworkshop.Arrange.Stage_ArrangeActivity;

public class PlayActivity extends AppCompatActivity {

    Button BtnArrange, BtnProduction, BtnWar, BtnDIY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 세로 모드 고정
        setContentView(R.layout.activity_play);

        BtnArrange = (Button) findViewById(R.id.BtnArrange);
        BtnProduction = (Button) findViewById(R.id.BtnProduction);
        BtnWar = (Button) findViewById(R.id.BtnWar);
        BtnDIY = (Button) findViewById(R.id.BtnDIY);

        BtnArrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();
                Intent intent = new Intent(PlayActivity.this, Stage_ArrangeActivity.class);
                PlayActivity.this.startActivity(intent);
                finish();
            }
        });

        BtnProduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();
            }
        });

        BtnWar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();
            }
        });

        BtnDIY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();
            }
        });
    }

    // 뒤로 가기 = Main으로 이동
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(PlayActivity.this, MainActivity.class);
        PlayActivity.this.startActivity(intent);
        finish();
    }

    // 클릭 효과음
    public void ClickSound() {
        if (AudioService_Click.mute == false) {
            Intent intent_audio = new Intent(getApplicationContext(), AudioService_Click.class);
            startService(intent_audio);
        }
    }
}