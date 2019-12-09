package e.t.gameworkshop.Arrange;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import e.t.gameworkshop.AudioService_Click;
import e.t.gameworkshop.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentRobot.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentRobot#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRobot extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button BtnMoveRobot, BtnDeleteRobot;
    private static ListView ListView_worker;
    private static Adapter_WorkerList Adapter_robotList;
    private static List<Worker> List_worker;
    private final static int EMPTY = -1, MOVE = -2, DELETE = -3;
    private static int selectStatus = EMPTY;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Data_FragToActivity mRobotListener;


    public FragmentRobot() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentRobot.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRobot newInstance(String param1, String param2) {
        FragmentRobot fragment = new FragmentRobot();
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
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
        ListView_worker = (ListView) getView().findViewById(R.id.listRobot);
        List_worker = new ArrayList<Worker>();

        List_worker.add(new Worker("Normal", 120, 0, 0, 0, 0, 150, 1, 1));
        List_worker.add(new Worker("Strong", 220, 0, 0, 0, 0, 250, 3, 1));
        List_worker.add(new Worker("Fast", 75, 0, 0, 0, 0, 120,  1, 3));
        Adapter_robotList = new Adapter_WorkerList(getActivity(), List_worker);
        ListView_worker.setAdapter(Adapter_robotList);

        ListView_worker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 로봇 클릭 상태가 아님
                if (selectStatus < 0)
                {
                    selectStatus = i;
                    List_worker.get(i).setSelected(true);
                    Adapter_robotList.notifyDataSetChanged();
                    Toast.makeText(getContext(), "로봇 위치를 맵에 설정하세요.", Toast.LENGTH_SHORT).show();
                    // 다른 클릭 비활성화
                    mRobotListener.RobotMoveStatus(false);
                }
                // 로봇 클릭 상태
                else
                {
                    // 다른 로봇 클릭
                    if (i != selectStatus)
                    {
                        List_worker.get(selectStatus).setSelected(false);
                        RobotSelectStatus(i, true);
                        selectStatus = i;
                    }
                    else
                    {
                        RobotSelectStatus(i, false);
                        selectStatus = EMPTY;
                    }
                }

                // 다른 기능 비활성화
                BtnDeleteRobot.setBackgroundResource(R.drawable.button_borderline2);
                BtnMoveRobot.setBackgroundResource(R.drawable.button_borderline2);
                // 선택된 로봇 인덱스 보내기
                mRobotListener.GetIndexRobot(i);
            }
        });
    }

    public interface Data_FragToActivity {
        // GameActivity 에서 Override 할 함수
        void AddRobot(Worker worker, int row, int col);

        void GetIndexRobot(int indexRobotAdd);

        void RobotMoveStatus(boolean status);
    }

    public void RobotSelectStatus(int index, boolean status)
    {
        List_worker.get(index).setSelected(status);
        Adapter_robotList.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_robot, container, false);

        BtnMoveRobot = (Button) v.findViewById(R.id.BtnMoveRobot);
        BtnDeleteRobot = (Button) v.findViewById(R.id.BtnDeleteRobot);
        BtnDeleteRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();
                // 삭제 버튼
                if (selectStatus != DELETE)
                {
                    // 다른 기능 비활성화
                    if (selectStatus >= 0) RobotSelectStatus(selectStatus, false);
                    BtnMoveRobot.setBackgroundResource(R.drawable.button_borderline2);
                    mRobotListener.RobotMoveStatus(false);
                    // 삭제 활성화
                    selectStatus = DELETE;
                    BtnDeleteRobot.setBackgroundResource(R.drawable.button_borderline_selected2);
                    Toast.makeText(getActivity(), "삭제할 로봇을 선택하세요.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    selectStatus = EMPTY;
                    BtnDeleteRobot.setBackgroundResource(R.drawable.button_borderline2);
                }
            }


        });

        BtnMoveRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();
                // 이동 버튼
                if (selectStatus != MOVE) {
                    // 다른 기능 비활성화
                    if (selectStatus >= 0) RobotSelectStatus(selectStatus, false);
                    BtnDeleteRobot.setBackgroundResource(R.drawable.button_borderline2);
                    // 이동 활성화
                    selectStatus = MOVE;
                    BtnMoveRobot.setBackgroundResource(R.drawable.button_borderline_selected2);
                    Toast.makeText(getActivity(), "이동할 로봇을 선택하세요.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    selectStatus = EMPTY;
                    BtnMoveRobot.setBackgroundResource(R.drawable.button_borderline2);
                    mRobotListener.RobotMoveStatus(false);
                }

            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Data_FragToActivity) {
            mRobotListener = (Data_FragToActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
        mRobotListener = null;
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

    public static List<Worker> getWorkerList() {
        return List_worker;
    }

    // 클릭 효과음
    public void ClickSound() {
        if (AudioService_Click.mute == false) {
            Intent intent_audio = new Intent(getActivity(), AudioService_Click.class);
            getActivity().startService(intent_audio);
        }
    }

    public static int getSelectStatus() {
        return selectStatus;
    }
}
