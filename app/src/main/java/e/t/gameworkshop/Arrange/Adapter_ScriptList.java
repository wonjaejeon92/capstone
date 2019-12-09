package e.t.gameworkshop.Arrange;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import e.t.gameworkshop.MoveDir;
import e.t.gameworkshop.R;
import e.t.gameworkshop.ScrType;
import e.t.gameworkshop.Script;
import e.t.gameworkshop.SpaceType;

public class Adapter_ScriptList extends BaseAdapter {

    private Context context;
    private List<Script> scriptList;
    TextView TextInclude, TextName;
    ImageView ImgStatus, ImgStatus2, ImgStatus3;

    public Adapter_ScriptList(Context context, List<Script> scriptList) {
        this.context = context;
        this.scriptList = scriptList;
    }

    @Override
    public int getCount() {
        return scriptList.size();
    }

    @Override
    public Object getItem(int i) {
        return scriptList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(context, R.layout.list_script, null);
        TextInclude = (TextView) v.findViewById(R.id.TextInclude);
        TextName = (TextView) v.findViewById(R.id.TextName);
        ImgStatus = (ImageView) v.findViewById(R.id.ImgStatus);
        ImgStatus2 = (ImageView) v.findViewById(R.id.ImgStatus2);
        ImgStatus3 = (ImageView) v.findViewById(R.id.ImgStatus3);

        TextName.setText(String.valueOf(scriptList.get(i).getName()));

        switch (scriptList.get(i).getType()) {
            // 명령하기
            case Arrange_ScrType.MOVE: {
                v.setBackgroundResource(000000);
                // 중앙
                if (scriptList.get(i).getNum() == 0)
                    ImgStatus.setImageResource(R.drawable.icon_center);
                    // 방향
                else {
                    ImgStatus.setImageResource(R.drawable.icon_arrow);
                    if (scriptList.get(i).getNum() == -1) ImgStatus.setRotation(-90);
                    else if (scriptList.get(i).getNum() == -2) ImgStatus.setRotation(90);
                    else if (scriptList.get(i).getNum() == -3) ImgStatus.setRotation(180);
                }
                break;
            }

            case Arrange_ScrType.IF:
            case Arrange_ScrType.ELSEIF: {
                v.setBackgroundColor(Color.CYAN);
                Set_ConditionList(i);
                break;
            }

            case Arrange_ScrType.FOR: {
                v.setBackgroundColor(Color.GREEN);
                // 무한 여부
                if (scriptList.get(i).getNum() != -1)
                    TextName.append(" : " + String.valueOf(scriptList.get(i).getNum()));
                else
                    TextName.append(" : ∞");
                break;
            }

            case Arrange_ScrType.WHILE: {
                v.setBackgroundColor(Color.YELLOW);
                Set_ConditionList(i);
                break;
            }

            case Arrange_ScrType.IFTAIL:
            case Arrange_ScrType.ELSETAIL: {
                v.setBackgroundColor(Color.CYAN);
                break;
            }

            case Arrange_ScrType.FORTAIL: {
                v.setBackgroundColor(Color.GREEN);
                break;
            }

            case Arrange_ScrType.WHILETAIL: {
                v.setBackgroundColor(Color.YELLOW);
                break;
            }

            case Arrange_ScrType.PICK:
            case Arrange_ScrType.PUT: {
                v.setBackgroundColor(000000);
                break;
            }
        }

        // 선택 표시
        if (scriptList.get(i).isSelected())
            v.setBackgroundColor(Color.RED);

        String s = scriptList.get(i).inclusion;
        SpannableString str = new SpannableString(s);
        for (int j = s.length() - 1; j >= 0; --j)
        {
            if (s.charAt(j) - 48 == ScrType.IF) {
                str.setSpan(new BackgroundColorSpan(Color.CYAN), j, j+1, 0);
                str.setSpan(new ForegroundColorSpan(Color.CYAN), j, j+1, 0);
            }
            else if (s.charAt(j) - 48 == ScrType.FOR) {
                str.setSpan(new BackgroundColorSpan(Color.GREEN), j, j+1, 0);
                str.setSpan(new ForegroundColorSpan(Color.GREEN), j, j+1, 0);
            }
            else if (s.charAt(j) - 48 == ScrType.WHILE) {
                str.setSpan(new BackgroundColorSpan(Color.YELLOW), j, j+1, 0);
                str.setSpan(new ForegroundColorSpan(Color.YELLOW), j, j+1, 0);
            }
        }
        TextInclude.append(str);
//        if (scriptList.get(i).getType() <= -3) TextInclude.setTextSize(15);

        return v;
    }

    public void Set_ConditionList(int i)
    {
        for (int j = 0; j < scriptList.get(i).getConditions().size(); ++j)
        {
            if (j != 0)
            {
                if (scriptList.get(i).getConditions().get(j).getConnectType() == 1)
                    TextName.append(" 그리고");
                else TextName.append(" 또는");
            }
            TextName.append("\n");
            switch (scriptList.get(i).getConditions().get(j).getConLeft())
            {
                case 0:
                    TextName.append("현재 위치");
                    break;
                case 1:
                    TextName.append("↑");
                    break;
                case 2:
                    TextName.append("↓");
                    break;
                case 3:
                    TextName.append("←");
                    break;
                case 4:
                    TextName.append("→");
                    break;
            }

            if (scriptList.get(i).getConditions().get(j).getCompare() == 0)
                TextName.append(" = ");
            else TextName.append(" ≠ ");
            switch (scriptList.get(i).getConditions().get(j).getConRight())
            {
                case SpaceType.EMPTY:
                    TextName.append("비어 있음");
                    break;
                case SpaceType.WALL:
                    TextName.append("벽");
                    break;
                case SpaceType.SAVE:
                    TextName.append("저장고");
                    break;
                case SpaceType.OBSTACLE:
                    TextName.append("장애물");
                    break;
                case SpaceType.OBJECT:
                    TextName.append("물건");
                    break;
                case SpaceType.ROBOT:
                    TextName.append("로봇");
                    break;
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
