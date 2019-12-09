package e.t.gameworkshop;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class Adapter_UserList extends BaseAdapter {

    private Context context;
    private List<UserData> userList;
    public static int index_select = -1;

    public Adapter_UserList(Context context, List<UserData> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }
    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(context, R.layout.list_compete_user, null);
        ImageView ImgUser = (ImageView) v.findViewById(R.id.ImgUser);
        TextView TextUserIDLevel = (TextView) v.findViewById(R.id.TextUserIDLevel);
        TextView TextUserScore = (TextView) v.findViewById(R.id.TextUserScore);

        if (index_select == i)
            v.setBackgroundColor(Color.CYAN);

        // 사진
        ImgUser.setImageResource(R.drawable.profile_man);
        ImgUser.getLayoutParams().width = 100;
        ImgUser.getLayoutParams().height = 100;

        TextUserIDLevel.append(String.valueOf(userList.get(i).getUserLevel() + " " + userList.get(i).getUserID()));
        TextUserScore.setText(String.valueOf(userList.get(i).getUserWIN()) + " 승 " + String.valueOf(userList.get(i).getUserLOSE()) + " 패");

        return v;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
