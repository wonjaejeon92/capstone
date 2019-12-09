package e.t.gameworkshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Popup_MailActivity extends Activity {

    private static ListView ListView_mail;
    private static Adapter_MailList Adapter_mailList;
    private static List<Mail> List_mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 타이틀바 삭제
        setContentView(R.layout.activity_popup_mail);

        Intent intent = getIntent();
        List_mail = new ArrayList<Mail>();
        ListView_mail = (ListView) findViewById(R.id.listMail);
        Adapter_mailList = new Adapter_MailList(getApplicationContext(), List_mail);
        ListView_mail.setAdapter(Adapter_mailList);

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("mailList"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            String receiverID, senderID;
            int tag, senderLevel, senderExp, senderWIN, senderLOSE, stageNUMBER, recordCost, recordTime, recordAction;
            while (count < jsonArray.length())
            {
                JSONObject object = jsonArray.getJSONObject(count++);
                tag = object.getInt("tag");
                receiverID = object.getString("receiverID");
                if (!receiverID.equals(CurUserData.userID)) continue;
                senderID = object.getString("senderID");
                senderLevel = object.getInt("senderLevel");
                senderExp = object.getInt("senderExp");
                senderWIN = object.getInt("senderWIN");
                senderLOSE = object.getInt("senderLOSE");
                stageNUMBER = object.getInt("stageNUMBER");
                recordCost = object.getInt("recordCost");
                recordTime = object.getInt("recordTime");
                recordAction = object.getInt("recordAction");
                Mail mail = new Mail(tag, senderID, senderLevel, senderExp, senderWIN, senderLOSE, stageNUMBER, recordCost, recordTime, recordAction);
                List_mail.add(mail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
