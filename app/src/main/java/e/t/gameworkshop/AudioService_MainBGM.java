package e.t.gameworkshop;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import e.t.gameworkshop.Arrange.Game_ArrangeActivity;

// 서비스 클래스를 구현하려면, Service 를 상속받는다
public class AudioService_MainBGM extends Service {

    MediaPlayer mp_Main; // 음악 재생을 위한 객체
    public static boolean mute = false;

    @Override
    public IBinder onBind(Intent intent) {
        // Service 객체와 (화면단 Activity 사이에서)
        // 통신(데이터를 주고받을) 때 사용하는 메서드
        // 데이터를 전달할 필요가 없으면 return null;
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        mp_Main = MediaPlayer.create(this, R.raw.free2);
        mp_Main.setLooping(true); // 반복재생
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        mp_Main.start(); // 노래 시작
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행
        mp_Main.stop(); // 음악 종료
    }
}
