package e.t.gameworkshop.Arrange;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.LinkedList;
import java.util.List;
import e.t.gameworkshop.AudioService_Click;
import e.t.gameworkshop.Popup_SetValueActivity;
import e.t.gameworkshop.Program;
import e.t.gameworkshop.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentProgramming.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentProgramming#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentProgramming extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static ListView ListView_program;
    private static Adapter_ProgramList Adapter_programList;
    private static List<Program> List_program;
    private static int indexProgram = -1;
    private boolean selDelete = false;
    Button BtnNew, BtnDelete;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private static InterfaceProgram mProgramListener;

    public interface InterfaceProgram {
        void ViewScript();
    }

    public FragmentProgramming() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentProgramming.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentProgramming newInstance(String param1, String param2) {
        FragmentProgramming fragment = new FragmentProgramming();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView_program = (ListView) getView().findViewById(R.id.listProgram);
        List_program = Game_ArrangeActivity.getPrograms();
        Adapter_programList = new Adapter_ProgramList(getActivity(), List_program);
        ListView_program.setAdapter(Adapter_programList);
        ListView_program.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // 프로그램 클릭
                if (!selDelete) {
                    FragmentScript.setIndexProgram(i);
                    mProgramListener.ViewScript();
                }
                // 삭제 모드
                else {
                    // 연결된 로봇 모두 해제
                    LinkedList<Integer> list = List_program.get(i).getNum_RobotUse();
                    if (list.size() > 0) {
                        for (int j = 0; j < list.size(); ++j)
                            Game_ArrangeActivity.getWorkers().get(list.get(j)).setIndexProgram(-1);
                    }
                    // 제거
                    List_program.remove(i);
                    Adapter_programList.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_programming, container, false);

        BtnNew = (Button) v.findViewById(R.id.BtnNew);
        BtnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 삭제 버튼 비활성화
                selDelete = false;
                BtnDelete.setBackgroundResource(R.drawable.button_borderline2);
                // 프로그램 추가 액티비티 켜기
                Intent intent = new Intent(getActivity(), Popup_SetValueActivity.class);
                getActivity().startActivity(intent);
                // 프로그램 이름 저장 용도 호출
                Popup_SetValueActivity.setCallActivity( Popup_SetValueActivity.getProgramName() );
            }
        });

        BtnDelete = (Button) v.findViewById(R.id.BtnDelete);
        BtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();

                if (!selDelete) {
                    selDelete = true;
                    BtnDelete.setBackgroundResource(R.drawable.button_borderline_selected2);
                    Toast.makeText(getContext(), "삭제할 프로그램을 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    selDelete = false;
                    BtnDelete.setBackgroundResource(R.drawable.button_borderline2);
                }
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfaceProgram) {
            mProgramListener = (InterfaceProgram) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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

    public static Adapter_ProgramList getAdapter_programList() {
        return Adapter_programList;
    }

    public static int getIndexProgram() {
        return indexProgram;
    }

    public static InterfaceProgram getmProgramListener() {
        return mProgramListener;
    }

    // 클릭 효과음
    public void ClickSound() {
        if (AudioService_Click.mute == false) {
            Intent intent_audio = new Intent(getActivity(), AudioService_Click.class);
            getActivity().startService(intent_audio);
        }
    }
}
