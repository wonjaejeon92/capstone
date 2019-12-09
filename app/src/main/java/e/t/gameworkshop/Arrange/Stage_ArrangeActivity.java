package e.t.gameworkshop.Arrange;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import e.t.gameworkshop.AudioService_Click;
import e.t.gameworkshop.AudioService_MainBGM;
import e.t.gameworkshop.MainActivity;
import e.t.gameworkshop.MapInfo;
import e.t.gameworkshop.R;

public class Stage_ArrangeActivity extends AppCompatActivity {

    Button Btn1, Btn2, Btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 세로 모드 고정
        setContentView(R.layout.activity_stage_arrange);

        Btn1 = (Button) findViewById(R.id.Btn1);
        // 게임 스테이지 창으로 이동
        Btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 상태 정보
                MapInfo.stage(1, MapInfo.SINGLE_MODE, null);
                Intent intent = new Intent(Stage_ArrangeActivity.this, Game_ArrangeActivity.class);
                Stage_ArrangeActivity.this.startActivity(intent);
                finish();
            }
        });

        Btn2 = (Button) findViewById(R.id.Btn2);
        Btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 상태 정보
                MapInfo.stage(2, MapInfo.SINGLE_MODE, null);
                Intent intent = new Intent(Stage_ArrangeActivity.this, Game_ArrangeActivity.class);
                Stage_ArrangeActivity.this.startActivity(intent);
                finish();
            }
        });

        Btn3 = (Button) findViewById(R.id.Btn3);
        Btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapInfo.stage(3, MapInfo.SINGLE_MODE, null);
                Intent intent = new Intent(Stage_ArrangeActivity.this, Game_ArrangeActivity.class);
                Stage_ArrangeActivity.this.startActivity(intent);
                finish();
            }
        });
    }

    // 뒤로 가기 버튼 클릭
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Stage_ArrangeActivity.this, MainActivity.class);
        Stage_ArrangeActivity.this.startActivity(intent);
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
