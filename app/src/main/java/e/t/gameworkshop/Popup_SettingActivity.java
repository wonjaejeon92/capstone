package e.t.gameworkshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class Popup_SettingActivity extends Activity {

    ImageView ImgAudio, ImgMusic;
    private int activityNumber;
    private static final int MAIN = 1;
    private static final int GAME = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 타이틀바 삭제
        setContentView(R.layout.activity_popup_settings);

        ImgAudio = (ImageView) findViewById(R.id.ImgAudio);
        ImgMusic = (ImageView) findViewById(R.id.ImgMusic);

        // 배경음 초기 이미지
        if (AudioService_MainBGM.mute == false && AudioService_GameBGM.mute == false)
            ImgMusic.setImageResource(R.drawable.icon_music_on);
        else ImgMusic.setImageResource(R.drawable.icon_music_off);

        // 효과음 초기 이미지
        if (AudioService_Click.mute == false) ImgAudio.setImageResource(R.drawable.icon_audio_on);
        else ImgAudio.setImageResource(R.drawable.icon_audio_off);


        // 효과음
        ImgAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_audio = new Intent(getApplicationContext(), AudioService_Click.class);
                if (AudioService_Click.mute == true) {
                    AudioService_Click.mute = false;
                    ImgAudio.setImageResource(R.drawable.icon_audio_on);
                    startService(intent_audio);
                } else {
                    AudioService_Click.mute = true;
                    ImgAudio.setImageResource(R.drawable.icon_audio_off);
                    stopService(intent_audio);
                }
            }
        });

        // 배경음
        ImgMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 배경음 on / off
                Intent intent = getIntent();
                activityNumber = intent.getIntExtra("activity", 0);
                switch (activityNumber) {
                    case MAIN: {
                        Intent intent_main = new Intent(getApplicationContext(), AudioService_MainBGM.class);
                        if (AudioService_MainBGM.mute == true && AudioService_GameBGM.mute == true) {
                            AudioService_MainBGM.mute = false;
                            AudioService_GameBGM.mute = false;
                            ImgMusic.setImageResource(R.drawable.icon_music_on);
                            startService(intent_main);
                        } else {
                            AudioService_MainBGM.mute = true;
                            AudioService_GameBGM.mute = true;
                            ImgMusic.setImageResource(R.drawable.icon_music_off);
                            stopService(intent_main);
                        }
                        break;
                    }
                    case GAME: {
                        Intent intent_game = new Intent(getApplicationContext(), AudioService_GameBGM.class);
                        if (AudioService_MainBGM.mute == true && AudioService_GameBGM.mute == true) {
                            AudioService_MainBGM.mute = false;
                            AudioService_GameBGM.mute = false;
                            ImgMusic.setImageResource(R.drawable.icon_music_on);
                            startService(intent_game);
                        } else {
                            AudioService_MainBGM.mute = true;
                            AudioService_GameBGM.mute = true;
                            ImgMusic.setImageResource(R.drawable.icon_music_off);
                            stopService(intent_game);
                        }
                    }
                }
            }
        });
    }

    //확인 버튼 클릭
    public void mOnClose(View v) {
        ClickSound();
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
