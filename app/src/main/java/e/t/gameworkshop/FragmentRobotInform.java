package e.t.gameworkshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import e.t.gameworkshop.Arrange.Worker;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentRobotInform.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentRobotInform#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRobotInform extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static int indexRobot;
    private static Worker w;
    private static Program p;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private static InterfaceRobotInform mInformListener;

    public interface InterfaceRobotInform {
        void TurnOff_Inform();
    }

    public FragmentRobotInform() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentConnect.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRobotInform newInstance(String param1, String param2) {
        FragmentRobotInform fragment = new FragmentRobotInform();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentRobotInform.InterfaceRobotInform) {
            mInformListener = (FragmentRobotInform.InterfaceRobotInform) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_robot_inform, container, false);

        TextView TextRobotNum = (TextView) v.findViewById(R.id.TextRobotNum);
        ImageView ImgRobot = (ImageView) v.findViewById(R.id.ImgRobot);
        TextView TextProgram = (TextView) v.findViewById(R.id.TextProgram);
        TextView TextName = (TextView) v.findViewById(R.id.TextName);
        TextView TextLocation = (TextView) v.findViewById(R.id.TextLocation);
        TextView TextEnergy = (TextView) v.findViewById(R.id.TextEnergy);
        TextView TextNumPerAct = (TextView) v.findViewById(R.id.TextNumPerAct);
        TextView TextNumHoldObject = (TextView) v.findViewById(R.id.TextNumHoldObject);
        TextView TextStrength = (TextView) v.findViewById(R.id.TextStrength);
        TextView TextSpeed = (TextView) v.findViewById(R.id.TextSpeed);
        Button BtnExit = (Button) v.findViewById(R.id.BtnExit);

        // 로봇 번호
        TextRobotNum.setText(String.valueOf(indexRobot + 1));
        // 사진
        if (w.getName() == "Normal")
            ImgRobot.setImageResource(R.drawable.icon_orangerobot_left1);
        else if (w.getName() == "Strong")
            ImgRobot.setImageResource(R.drawable.icon_redrobot_left1);
        else if (w.getName() == "Fast")
            ImgRobot.setImageResource(R.drawable.icon_bluerobot_left1);
        ImgRobot.getLayoutParams().width = 100;
        ImgRobot.getLayoutParams().height = 100;

        // 연결된 프로그램
        if (w.getIndexProgram() != -1) {
            TextProgram.setTextColor(Color.BLUE);
            if (p != null)
                TextProgram.setText(String.valueOf(w.getIndexProgram() + 1) + "\n" + String.valueOf(p.getName()));
            else {
                TextProgram.setTextSize(12);
                TextProgram.setText(String.valueOf(w.getIndexProgram() + 1) + "\n");
                TextProgram.setTextColor(Color.RED);
                TextProgram.append("UNSERVICEABLE");
            }
        }
        else {
            TextProgram.setTextSize(12);
            TextProgram.setTextColor(Color.RED);
            TextProgram.setText("NO CONNECTION");
        }

        TextName.setText(w.getName());
        TextLocation.setText(String.valueOf(w.getRow() + 1) + " 줄 " + String.valueOf(w.getCol() + 1) + " 칸");
        // 에너지 0?
        if (w.getEnergy() == 0)
            TextEnergy.setTextColor(Color.RED);
        TextEnergy.setText(String.valueOf(w.getEnergy()));
        TextNumPerAct.setText(String.valueOf(w.getNumAct()));
        TextNumHoldObject.setText(String.valueOf(w.getNumHoldObject()));
        TextStrength.setText(String.valueOf(w.getStrength()));
        TextSpeed.setText(String.valueOf(w.getSpeed()));

        BtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInformListener.TurnOff_Inform();
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // 클릭 효과음
    public void ClickSound() {
        if (AudioService_Click.mute == false) {
            Intent intent_audio = new Intent(getActivity(), AudioService_Click.class);
            getActivity().startService(intent_audio);
        }
    }

    public static void setIndexRobot(int indexRobot) {
        FragmentRobotInform.indexRobot = indexRobot;
    }

    public static void setWorker(Worker w) {
        FragmentRobotInform.w = w;
    }

    public static void setProgram(Program p) {
        FragmentRobotInform.p = p;
    }
}
