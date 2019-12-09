package e.t.gameworkshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import e.t.gameworkshop.Arrange.Game_ArrangeActivity;

public class CompeteUserActivity extends AppCompatActivity {

    private static ListView ListView_user;
    private static Adapter_UserList Adapter_userList;
    private static List<UserData> List_user;
    Button BtnStart, BtnExit;
    private static boolean clicked_user = false;
    private static String compete_userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compete_user);

        BtnStart = (Button) findViewById(R.id.BtnStart);
        BtnExit = (Button) findViewById(R.id.BtnExit);

        Intent intent = getIntent();
        List_user = new ArrayList<UserData>();
        ListView_user = (ListView) findViewById(R.id.listUser);
        Adapter_userList = new Adapter_UserList(getApplicationContext(), List_user);
        ListView_user.setAdapter(Adapter_userList);

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("userList"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            String userID;
            int userLevel, userWIN, userLOSE;
            while (count < jsonArray.length())
            {
                JSONObject object = jsonArray.getJSONObject(count++);
                userID = object.getString("userID");
                if (userID.equals(CurUserData.userID)) continue;
                userLevel = object.getInt("userLevel");
                userWIN = object.getInt("userWIN");
                userLOSE = object.getInt("userLOSE");
                UserData userData = new UserData(userID, userLevel, userWIN, userLOSE);
                List_user.add(userData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView_user.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clicked_user = true;
                compete_userID = List_user.get(i).getUserID();
                Adapter_UserList.index_select = i;
                Adapter_userList.notifyDataSetChanged();
            }
        });

        BtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked_user) {
                    Random random = new Random();
                    // MapInfo.stage(random.nextInt(1) + 2, 2, compete_userID);
                    MapInfo.stage(2, MapInfo.CHALLENGE_MODE, compete_userID);
                    Intent intent1 = new Intent(CompeteUserActivity.this, Game_ArrangeActivity.class);
                    CompeteUserActivity.this.startActivity(intent1);
                } else {
                    Toast.makeText(getApplicationContext(), "대전 상대를 선택하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        BtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(CompeteUserActivity.this, MainActivity.class);
                CompeteUserActivity.this.startActivity(intent2);
                Adapter_UserList.index_select = -1;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Adapter_UserList.index_select = -1;
        finish();
        return;
    }

}
