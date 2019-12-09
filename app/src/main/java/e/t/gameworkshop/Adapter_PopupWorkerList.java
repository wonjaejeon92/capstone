package e.t.gameworkshop;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import e.t.gameworkshop.Arrange.Worker;

public class Adapter_PopupWorkerList extends BaseAdapter {

    private Context context;
    private List<Worker> workerList;

    public Adapter_PopupWorkerList(Context context, List<Worker> workerList) {
        this.context = context;
        this.workerList = workerList;
    }

    @Override
    public int getCount() {
        return workerList.size();
    }
    @Override
    public Object getItem(int i) {
        return workerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(context, R.layout.list_robot_popup, null);
        ImageView ImgRobot = (ImageView) v.findViewById(R.id.ImgRobot);
        TextView TextRobotNum = (TextView) v.findViewById(R.id.TextRobotNum);
        TextView TextName = (TextView) v.findViewById(R.id.TextName);
        TextView TextCost = (TextView) v.findViewById(R.id.TextCost);
        TextView TextEnergy = (TextView) v.findViewById(R.id.TextEnergy);
        TextView TextProgramNum = (TextView) v.findViewById(R.id.TextProgramNum);

        // 사진
        if (workerList.get(i).getName() == "Normal")
            ImgRobot.setImageResource(R.drawable.icon_orangerobot_left1);
        else if (workerList.get(i).getName() == "Strong")
            ImgRobot.setImageResource(R.drawable.icon_redrobot_left1);
        else if (workerList.get(i).getName() == "Fast")
            ImgRobot.setImageResource(R.drawable.icon_bluerobot_left1);
        ImgRobot.getLayoutParams().width = 100;
        ImgRobot.getLayoutParams().height = 100;

        TextRobotNum.setText(String.valueOf(i + 1));
        TextName.setText(workerList.get(i).getName());
        TextEnergy.setText(String.valueOf(workerList.get(i).getEnergy()));
        TextCost.setText(String.valueOf(workerList.get(i).getCost()));
        if (workerList.get(i).getIndexProgram() >= 0)
            TextProgramNum.setText(String.valueOf(workerList.get(i).getIndexProgram() + 1));
        else
            TextProgramNum.setText("X");
        return v;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
