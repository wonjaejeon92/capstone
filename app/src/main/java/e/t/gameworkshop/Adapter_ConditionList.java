package e.t.gameworkshop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import e.t.gameworkshop.Arrange.FragmentScript;

public class Adapter_ConditionList extends BaseAdapter {

    int index;
    private Button BtnDel;
    private Context context;
    private List<Condition> ConditionList;

    public Adapter_ConditionList(Context context, List<Condition> ConditionList) {
        this.context = context;
        this.ConditionList = ConditionList;
    }

    @Override
    public int getCount() {
        return ConditionList.size();
    }

    @Override
    public Object getItem(int i) {
        return ConditionList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(context, R.layout.list_condition, null);
        TextView TextNum = (TextView) v.findViewById(R.id.TextNum);
        TextView TextConnect = (TextView) v.findViewById(R.id.TextConnect);
        TextView TextCompare = (TextView) v.findViewById(R.id.TextCompare);
        ImageView ImgLeft = (ImageView) v.findViewById(R.id.ImgLeft);
        ImageView ImgRight = (ImageView) v.findViewById(R.id.ImgRight);
        index = i;

        TextNum.setText(String.valueOf(i + 1));

        if (ConditionList.get(i).getConnectType() == 1)
            TextConnect.setText("그리고");
        else if (ConditionList.get(i).getConnectType() == 2)
            TextConnect.setText("또는");

        if (ConditionList.get(i).getCompare() == 0) TextCompare.setText("=");
        else if (ConditionList.get(i).getCompare() == 1) TextCompare.setText("≠");

        switch (ConditionList.get(i).getConLeft()) {
            case 0: {
                ImgLeft.setImageResource(R.drawable.icon_center);
                break;
            }
            case 1: {
                ImgLeft.setImageResource(R.drawable.icon_arrow);
                ImgLeft.setRotation(-90);
                break;
            }
            case 2: {
                ImgLeft.setImageResource(R.drawable.icon_arrow);
                ImgLeft.setRotation(90);
                break;
            }
            case 3: {
                ImgLeft.setImageResource(R.drawable.icon_arrow);
                ImgLeft.setRotation(180);
                break;
            }
            case 4: {
                ImgLeft.setImageResource(R.drawable.icon_arrow);
                break;
            }
        }

        switch (ConditionList.get(i).getConRight()) {
            case SpaceType.EMPTY: {
                ImgRight.setImageResource(R.drawable.icon_empty);
                break;
            }
            case SpaceType.WALL: {
                ImgRight.setImageResource(R.drawable.icon_wall);
                break;
            }
            case SpaceType.SAVE: {
                ImgRight.setImageResource(R.drawable.icon_save);
                break;
            }
            case SpaceType.OBSTACLE: {
                ImgRight.setImageResource(R.drawable.icon_obstacle);
                break;
            }
            case SpaceType.OBJECT: {
                ImgRight.setImageResource(R.drawable.icon_object);
                break;
            }
            case SpaceType.ROBOT: {
                ImgRight.setImageResource(R.drawable.icon_robot);
                break;
            }
        }

        BtnDel = (Button) v.findViewById(R.id.BtnConditionDelete);
        BtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConditionList.remove(index);
                notifyDataSetChanged();
                FragmentScript.getAdapter_ScriptList().notifyDataSetChanged();
            }
        });

        return v;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
