package e.t.gameworkshop;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import e.t.gameworkshop.Arrange.Stage_ArrangeActivity;

public class Popup_GameModeActivity extends Activity {

    Button BtnSingle, BtnCompete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 타이틀바 삭제
        setContentView(R.layout.activity_popup_gamemode);

        BtnSingle = (Button) findViewById(R.id.BtnSingle);
        BtnCompete = (Button) findViewById(R.id.BtnCompete);

        BtnSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();

                Intent intent = new Intent(Popup_GameModeActivity.this, Stage_ArrangeActivity.class);
                Popup_GameModeActivity.this.startActivity(intent);
                finish();
            }
        });

        BtnCompete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();

                new BackgroundTask().execute();
            }
        });
    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected void onPreExecute() {
            target = "https://start1a.cafe24.com/UserList.php";
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
            Intent intent = new Intent(Popup_GameModeActivity.this, CompeteUserActivity.class);
            intent.putExtra("userList", result);
            Popup_GameModeActivity.this.startActivity(intent);
            finish();
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
