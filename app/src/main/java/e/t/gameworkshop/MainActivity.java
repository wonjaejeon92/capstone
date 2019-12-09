package e.t.gameworkshop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import e.t.gameworkshop.Arrange.Stage_ArrangeActivity;

public class MainActivity extends AppCompatActivity {

    public static Button BtnPlay, BtnProfile, BtnSettings, BtnLogout, BtnMail;
    private AlertDialog dialog;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 세로 모드 고정
        setContentView(R.layout.activity_main);

        BtnPlay = (Button) findViewById(R.id.BtnPlay);
        BtnProfile = (Button) findViewById(R.id.BtnProfile);
        BtnSettings = (Button) findViewById(R.id.BtnSettings);
        BtnLogout = (Button) findViewById(R.id.BtnLogout);
        BtnMail = (Button) findViewById(R.id.BtnMail);

        // Service 소리 켜기
        if (AudioService_MainBGM.mute == false) {
            Intent intent_audio = new Intent(getApplicationContext(), AudioService_MainBGM.class);
            startService(intent_audio);
        }

        BtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();

                Intent intent = new Intent(MainActivity.this, Popup_GameModeActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        BtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();

                Intent intent = new Intent(MainActivity.this, Popup_ProfileActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        BtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();

                Intent intent = new Intent(MainActivity.this, Popup_SettingActivity.class);
                intent.putExtra("activity", 1);
                MainActivity.this.startActivity(intent);
            }
        });

        BtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("로그아웃");
                builder.setIcon(R.drawable.icon_warning);
                dialog = builder.setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 노래 끄기
                                        if (AudioService_MainBGM.mute == false) {
                                            Intent intent_audio = new Intent(getApplicationContext(), AudioService_MainBGM.class);
                                            stopService(intent_audio);
                                        }

                                        Toast.makeText(getApplicationContext(), "로그아웃되셨습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        MainActivity.this.startActivity(intent);
                                        finish();
                                    }
                                })
                        .setNegativeButton("취소", null)
                        .create();
                dialog.show();
            }
        });

        BtnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundTask2().execute();
            }
        });
    }

    class BackgroundTask2 extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected void onPreExecute() {
            target = "https://start1a.cafe24.com/ReceiveMail.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result) {
            Intent intent3 = new Intent(MainActivity.this, Popup_MailActivity.class);
            intent3.putExtra("mailList", result);
            MainActivity.this.startActivity(intent3);
        }
    }

    // 2번 누르면 종료
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로 가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // 클릭 효과음
    public void ClickSound() {
        if (AudioService_Click.mute == false) {
            Intent intent_audio = new Intent(getApplicationContext(), AudioService_Click.class);
            startService(intent_audio);
        }
    }
}
