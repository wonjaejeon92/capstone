package e.t.gameworkshop.Arrange;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import e.t.gameworkshop.Program;
import e.t.gameworkshop.R;

public class Adapter_ProgramList extends BaseAdapter {

    private Context context;
    private List<Program> programList;

    public Adapter_ProgramList(Context context, List<Program> programList) {
        this.context = context;
        this.programList = programList;
    }

    @Override
    public int getCount() {
        return programList.size();
    }

    @Override
    public Object getItem(int i) {
        return programList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(context, R.layout.list_program, null);
        TextView TextNum = (TextView) v.findViewById(R.id.TextNum);
        TextView TextName = (TextView) v.findViewById(R.id.TextName);
        TextView TexProgram = (TextView) v.findViewById(R.id.TextProgram);

        TextNum.setText(String.valueOf(i + 1));
        TextName.setText(String.valueOf(programList.get(i).getName()));
        if (programList.get(i).getNum_RobotUse() != null)
        {
            int numRobot = programList.get(i).getNum_RobotUse().size();
            for (int j = 0; j < numRobot; ++j)
                TexProgram.append(String.valueOf(programList.get(i).getNum_RobotUse().get(j) + 1) + " ");
        }
        return v;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
