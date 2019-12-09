package e.t.gameworkshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Popup_ProfileActivity extends Activity {

    TextView textID, textLevel, textExp, textScore;
    ProgressBar progressExp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 타이틀바 삭제
        setContentView(R.layout.activity_popup_profile);

        textID = (TextView) findViewById(R.id.textID);
        textLevel = (TextView) findViewById(R.id.textLevel);
        textExp = (TextView) findViewById(R.id.textExp);
        textScore = (TextView) findViewById(R.id.textScore);
        progressExp = (ProgressBar) findViewById(R.id.progressExp);

        // 사용자 정보 출력
        textID.setText(CurUserData.userID);
        textLevel.setText(String.valueOf(CurUserData.userLevel));
        textExp.setText(" " + String.valueOf(CurUserData.userExp));
        int win = CurUserData.userWIN;
        int lose = CurUserData.userLOSE;
        float winrate = 0.0f;
        if (win + lose != 0.0f) winrate = (float)win / (win + lose);
        textScore.append(String.valueOf(win) + " 승 " + String.valueOf(lose) + " 패 (" + String.valueOf(winrate) + ")");
        progressExp.setProgress(CurUserData.userExp);
    }

    // 클릭 효과음
    public void ClickSound() {
        if (AudioService_Click.mute == false) {
            Intent intent_audio = new Intent(getApplicationContext(), AudioService_Click.class);
            startService(intent_audio);
        }
    }
}
