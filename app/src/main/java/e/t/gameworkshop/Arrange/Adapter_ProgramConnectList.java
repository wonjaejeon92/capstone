package e.t.gameworkshop.Arrange;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import e.t.gameworkshop.Program;
import e.t.gameworkshop.R;

public class Adapter_ProgramConnectList extends BaseAdapter {

    private Context context;
    private List<Program> programConnectList;

    public Adapter_ProgramConnectList(Context context, List<Program> programConnectList) {
        this.context = context;
        this.programConnectList = programConnectList;
    }

    @Override
    public int getCount() {
        return programConnectList.size();
    }

    @Override
    public Object getItem(int i) {
        return programConnectList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(context, R.layout.list_connect_program, null);
        TextView TextName = (TextView) v.findViewById(R.id.TextName);
        TextView TextNum = (TextView) v.findViewById(R.id.TextNum);
        TextView TextProgram = (TextView) v.findViewById(R.id.TextProgram);

        TextNum.setText(String.valueOf(i + 1));
        TextName.setText(programConnectList.get(i).getName());
        LinkedList<Integer> list = programConnectList.get(i).getNum_RobotUse();
        if (list.size() > 0) {
            for (int j = 0; j < list.size(); ++j)
                TextProgram.append(String.valueOf(list.get(j) + 1) + " ");
        } else {
            TextProgram.setText("None");
        }
        return v;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
