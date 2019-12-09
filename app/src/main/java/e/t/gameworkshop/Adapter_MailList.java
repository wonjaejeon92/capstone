package e.t.gameworkshop;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

import e.t.gameworkshop.Arrange.Game_ArrangeActivity;

public class Adapter_MailList extends BaseAdapter {

    private Context context;
    private List<Mail> mailList;

    public Adapter_MailList(Context context, List<Mail> mailList) {
        this.context = context;
        this.mailList = mailList;
    }

    @Override
    public int getCount() {
        return mailList.size();
    }
    @Override
    public Object getItem(int i) {
        return mailList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(context, R.layout.list_mail, null);
        final int index = i;
        ImageView ImgUser = (ImageView) v.findViewById(R.id.ImgUser);
        TextView TextSenderIDLevel = (TextView) v.findViewById(R.id.TextSenderIDLevel);
        TextView TextSenderScore = (TextView) v.findViewById(R.id.TextSenderScore);
        Button BtnApproval = (Button) v.findViewById(R.id.BtnApproval);
        Button BtnReject = (Button) v.findViewById(R.id.BtnReject);

        // 사진
        ImgUser.setImageResource(R.drawable.profile_man);
        ImgUser.getLayoutParams().width = 100;
        ImgUser.getLayoutParams().height = 100;

        Mail m = mailList.get(i);
        TextSenderIDLevel.append(String.valueOf(m.getSenderLevel() + " " + m.getSenderID()));
        TextSenderScore.setText(String.valueOf(m.getSenderWIN()) + " 승 " + String.valueOf(m.getSenderLOSE()) + " 패");

        BtnApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Mail m = mailList.get(index);
                // 메일 제거 + db 제거
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { }
                };
                MailRequest mailRequest = new MailRequest(m.getTag(), responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(mailRequest);
                // 게임 실행
                Challenger_Record.set(m.getSenderID(), m.getSenderLevel(), m.getSenderExp(), m.getSenderWIN(), m.getSenderLOSE(), m.getStageNumber(), m.getRecordCost(), m.getRecordTime(), m.getRecordAction());
                MapInfo.stage(Challenger_Record.getStageNUMBER(), MapInfo.APPROVAL_MODE, Challenger_Record.getSenderID());
                mailList.remove(index);
                Intent intent = new Intent(context, Game_ArrangeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        BtnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메일 제거
                // db에서 제거 + 상대 싱글 경험치 획득
                Mail m = mailList.get(index);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { }
                };

                RecordRequest recordRequest = new RecordRequest(m.getSenderID(), GameExp.SINGLE, responseListener);
                MailRequest mailRequest = new MailRequest(m.getTag(), responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(recordRequest);
                requestQueue.add(mailRequest);

                mailList.remove(index);
                notifyDataSetInvalidated();

            }
        });
        return v;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
