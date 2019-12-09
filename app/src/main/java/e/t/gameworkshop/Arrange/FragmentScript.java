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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import java.util.LinkedList;
import e.t.gameworkshop.AudioService_Click;
import e.t.gameworkshop.Popup_Script_ConditionActivity;
import e.t.gameworkshop.Popup_Script_MoveActivity;
import e.t.gameworkshop.Popup_SetValueActivity;
import e.t.gameworkshop.Program;
import e.t.gameworkshop.R;
import e.t.gameworkshop.ScrType;
import e.t.gameworkshop.Script;
import e.t.gameworkshop.ScriptManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentProgramming.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentProgramming#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentScript extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static int indexProgram;
    Button[] Button, ButtonLayout;
    LinearLayout[] layout;
    private static int statusBtn, statusLayout = -1;
    private final static int NONE = -1, COMMAND = 0, CONDITION = 1, MODIFY = 2;
    private static Program seledtedProgram;
    private static ListView listViewScript;
    private static LinkedList<Script> List_script;
    private static e.t.gameworkshop.Arrange.Adapter_ScriptList Adapter_ScriptList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private static InterfaceScript mScriptListener;

    public interface InterfaceScript {
        void ViewProgram();
        void OnLongClick();
    }

    public FragmentScript() {
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
    public static FragmentScript newInstance(String param1, String param2) {
        FragmentScript fragment = new FragmentScript();
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
        if (context instanceof FragmentScript.InterfaceScript) {
            mScriptListener = (FragmentScript.InterfaceScript) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    // 조건, 반복문 꼬리 생성
    public void tailCommand(int status, int index) {
        switch (status) {
            case Arrange_ScrType.IF:
                List_script.add(index, new Script("조건꼬리",  ScrType.IFTAIL, 0));
                break;
            case Arrange_ScrType.FOR:
                List_script.add(index, new Script("꼬리", ScrType.FORTAIL, 0));
                break;
            case Arrange_ScrType.WHILE:
                List_script.add(index, new Script("꼬리", ScrType.WHILETAIL, 0));
                break;
        }
    }

    // 명령 삽입
    public void InsertCommand(int status, String name, int num) {
        // 버튼 색상 업데이트
        if (statusBtn != status) {
            Button[statusBtn].setBackgroundResource(R.drawable.button_borderline2);
            statusBtn = status;
            Button[statusBtn].setBackgroundResource(R.drawable.button_borderline_selected);
            // 리스트에 데이터가 없음
            if (List_script.size() == 0) {
                // 새로운 스크립트 생성
                List_script.add(new Script(name, status, num));
                // 꼬리 여부
                tailCommand(status, 1);

                Adapter_ScriptList.notifyDataSetChanged();
            } else
                Toast.makeText(getActivity(), "명령의 위치를 선택하세요.", Toast.LENGTH_SHORT).show();
        } else {
            Button[statusBtn].setBackgroundResource(R.drawable.button_borderline2);
            statusBtn = Arrange_ScrType.EMPTY;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        seledtedProgram = Game_ArrangeActivity.getPrograms().get(indexProgram);
        listViewScript = (ListView) getView().findViewById(R.id.listScript);
        List_script = seledtedProgram.getScripts();
        Adapter_ScriptList = new Adapter_ScriptList(getActivity(), List_script);
        listViewScript.setAdapter(Adapter_ScriptList);

        listViewScript.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                int s1 = ScriptManager.getSel1();
                int s2 = ScriptManager.getSel2();

                if (s1 != -1 && s2 != -1 && statusBtn == ScrType.RDELETE)
                    mScriptListener.OnLongClick();
                return false;
            }
        });

        listViewScript.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 스크립트 수정 모드
                if (statusBtn != ScrType.EMPTY) {
                    // 이전 사이즈 (꼬리 거리 체크용)
                    int prevSize = List_script.size();

                    switch (statusBtn) {
                        case ScrType.IF:
                            List_script.add(i + 1, new Script("만약", ScrType.IF, i + 2));
                            tailCommand(statusBtn, i + 2);
                            ScriptManager.Update_Inclusion(List_script);
                            break;
                        case ScrType.FOR:
                            if (ScriptManager.Addible_Script(List_script, i)) {
                                List_script.add(i + 1, new Script("반복", ScrType.FOR, 1));
                                tailCommand(statusBtn, i + 2);
                                ScriptManager.Update_Inclusion(List_script);
                            }
                            break;
                        case ScrType.WHILE:
                            if (ScriptManager.Addible_Script(List_script, i)) {
                                List_script.add(i + 1, new Script("조건 반복", ScrType.WHILE, i + 2));
                                tailCommand(statusBtn, i + 2);
                                ScriptManager.Update_Inclusion(List_script);
                            }
                            break;
                        case ScrType.MOVE:
                            if (ScriptManager.Addible_Script(List_script, i)) {
                                List_script.add(i + 1, new Script("이동하기", ScrType.MOVE, -1));
                                ScriptManager.Update_Inclusion(List_script);
                            }
                            break;
                        case Arrange_ScrType.PICK:
                            if (ScriptManager.Addible_Script(List_script, i)) {
                                List_script.add(i + 1, new Script("집어들기", Arrange_ScrType.PICK, 0));
                                ScriptManager.Update_Inclusion(List_script);
                            }
                            break;
                        case Arrange_ScrType.PUT:
                            if (ScriptManager.Addible_Script(List_script, i)) {
                                List_script.add(i + 1, new Script("내려놓기", Arrange_ScrType.PUT, 0));
                                ScriptManager.Update_Inclusion(List_script);
                            }
                            break;
                        case ScrType.DELETE: {
                            ScriptManager.Delete_OneScript(List_script, i);
                            break;
                        }
                        case ScrType.RDELETE: {
                            ScriptManager.Delete_RangeScript(List_script, i);
                            break;
                        }
                        case ScrType.SHIFT: {
                                ScriptManager.Move_OneScript(List_script, i);
                                break;
                            }
                        case ScrType.RSHIFT: {
                            ScriptManager.Move_RangeScript(List_script, i);
                            break;
                        }
                    }
                }

                // 스크립트 속성 수정 모드
                // 초기화 액티비티 호출
                else {
                    switch (List_script.get(i).getType()) {
                        case Arrange_ScrType.IF:
                        case Arrange_ScrType.WHILE:
                        case Arrange_ScrType.ELSEIF:
                        case Arrange_ScrType.ELSE:{
                            Intent intent = new Intent(getActivity(), Popup_Script_ConditionActivity.class);
                            getActivity().startActivity(intent);
                            Popup_Script_ConditionActivity.setIndexCommand(i);
                            break;
                        }
                        case Arrange_ScrType.MOVE: {
                            Intent intent = new Intent(getActivity(), Popup_Script_MoveActivity.class);
                            getActivity().startActivity(intent);
                            Popup_Script_MoveActivity.setIndexCommand(i);
                            break;
                        }
                        case Arrange_ScrType.FOR: {
                            // for문 액티비티 켜기
                            Intent intent = new Intent(getActivity(), Popup_SetValueActivity.class);
                            getActivity().startActivity(intent);
                            // 횟수 저장
                            Popup_SetValueActivity.setCallActivity(Popup_SetValueActivity.getForNumber());
                            Popup_SetValueActivity.setIndexFromList(i);
                            break;
                        }
                    }
                }
                Adapter_ScriptList.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_script, container, false);

        layout = new LinearLayout[3];
        layout[COMMAND] = (LinearLayout) v.findViewById(R.id.LayoutCommand);
        layout[CONDITION] = (LinearLayout) v.findViewById(R.id.LayoutCondition);
        layout[MODIFY] = (LinearLayout) v.findViewById(R.id.LayoutModify);

        ButtonLayout = new Button[3];
        ButtonLayout[COMMAND] = (Button) v.findViewById(R.id.BtnCommand);
        ButtonLayout[COMMAND].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();
                // 타 기능 비활성화
                if (statusBtn != 0) Button[statusBtn].setBackgroundResource(R.drawable.button_borderline2);
                statusBtn = 0;
                if (ScriptManager.getSel1() != -1)
                {
                    ScriptManager.SelectedScript_Off(List_script);
                    Adapter_ScriptList.notifyDataSetChanged();
                }

                if (statusLayout != COMMAND) {
                    if (statusLayout != NONE) {
                        ButtonLayout[statusLayout].setBackgroundResource(R.drawable.button_borderline2);
                        layout[statusLayout].setVisibility(View.GONE);
                    }
                    statusLayout = COMMAND;
                    ButtonLayout[statusLayout].setBackgroundResource(R.drawable.button_borderline_selected);
                    layout[statusLayout].setVisibility(View.VISIBLE);
                }
                else
                {
                    ButtonLayout[statusLayout].setBackgroundResource(R.drawable.button_borderline2);
                    layout[statusLayout].setVisibility(View.GONE);
                    statusLayout = NONE;
                }
            }
        });

        ButtonLayout[CONDITION] = (Button) v.findViewById(R.id.BtnCondition);
        ButtonLayout[CONDITION].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();

                if (statusBtn != 0) Button[statusBtn].setBackgroundResource(R.drawable.button_borderline2);
                statusBtn = 0;
                if (ScriptManager.getSel1() != -1)
                {
                    ScriptManager.SelectedScript_Off(List_script);
                    Adapter_ScriptList.notifyDataSetChanged();
                }

                // 버튼 클릭 갱신
                if (statusLayout != CONDITION) {
                    if (statusLayout != NONE) {
                        ButtonLayout[statusLayout].setBackgroundResource(R.drawable.button_borderline2);
                        layout[statusLayout].setVisibility(View.GONE);
                    }
                    statusLayout = CONDITION;
                    ButtonLayout[statusLayout].setBackgroundResource(R.drawable.button_borderline_selected);
                    layout[statusLayout].setVisibility(View.VISIBLE);
                }
                else
                {
                    ButtonLayout[statusLayout].setBackgroundResource(R.drawable.button_borderline2);
                    layout[statusLayout].setVisibility(View.GONE);
                    statusLayout = NONE;
                }
            }
        });

        ButtonLayout[MODIFY] = (Button) v.findViewById(R.id.BtnModify);
        ButtonLayout[MODIFY].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();

                if (statusBtn != 0) Button[statusBtn].setBackgroundResource(R.drawable.button_borderline2);
                statusBtn = 0;
                if (ScriptManager.getSel1() != -1)
                {
                    ScriptManager.SelectedScript_Off(List_script);
                    Adapter_ScriptList.notifyDataSetChanged();
                }

                if (statusLayout != MODIFY) {
                    if (statusLayout != NONE) {
                        ButtonLayout[statusLayout].setBackgroundResource(R.drawable.button_borderline2);
                        layout[statusLayout].setVisibility(View.GONE);
                    }
                    statusLayout = MODIFY;
                    ButtonLayout[statusLayout].setBackgroundResource(R.drawable.button_borderline_selected);
                    layout[statusLayout].setVisibility(View.VISIBLE);
                }
                else
                {
                    ButtonLayout[statusLayout].setBackgroundResource(R.drawable.button_borderline2);
                    layout[statusLayout].setVisibility(View.GONE);
                    statusLayout = NONE;
                }
            }
        });

        Button = new Button[11];
        Button[0] = (Button) v.findViewById(R.id.BtnBack);
        Button[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();
                statusBtn = 0; statusLayout = NONE;
                if (ScriptManager.getSel1() != -1)
                {
                    ScriptManager.SelectedScript_Off(List_script);
                    Adapter_ScriptList.notifyDataSetChanged();
                }
                mScriptListener.ViewProgram();
            }
        });

        Button[ScrType.MOVE] = (Button) v.findViewById(R.id.BtnMove);
        Button[ScrType.MOVE].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();
                InsertCommand(ScrType.MOVE, "이동하기", -1);
            }
        });

        Button[ScrType.IF] = (Button) v.findViewById(R.id.BtnIF);
        Button[ScrType.IF].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();
                InsertCommand(ScrType.IF, "만약", 1);
            }
        });

        Button[ScrType.FOR] = (Button) v.findViewById(R.id.BtnFor);
        Button[ScrType.FOR].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();
                InsertCommand(ScrType.FOR, "횟수 반복", 1);
            }
        });

        Button[ScrType.WHILE] = (Button) v.findViewById(R.id.BtnWhile);
        Button[ScrType.WHILE].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();
                InsertCommand(ScrType.WHILE, "조건 반복", 1);
            }
        });

        Button[ScrType.DELETE] = (Button) v.findViewById(R.id.BtnDelete);
        Button[ScrType.DELETE].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();

                if (statusBtn != ScrType.DELETE)
                    Button_On(ScrType.DELETE);
                else
                    Button_Off();
            }
        });

        Button[ScrType.RDELETE] = (Button) v.findViewById(R.id.BtnDelete2);
        Button[ScrType.RDELETE].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statusBtn != ScrType.RDELETE)
                    Button_On(ScrType.RDELETE);
                else Button_Off();
            }
        });

        Button[ScrType.SHIFT] = (Button) v.findViewById(R.id.BtnShift);
        Button[ScrType.SHIFT].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();

                if (statusBtn != ScrType.SHIFT)
                    Button_On(ScrType.SHIFT);
                else Button_Off();
            }
        });

        Button[ScrType.RSHIFT] = (Button) v.findViewById(R.id.BtnShift2);
        Button[ScrType.RSHIFT].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statusBtn != ScrType.RSHIFT)
                    Button_On(ScrType.RSHIFT);
                else Button_Off();
            }
        });

        Button[Arrange_ScrType.PICK] = (Button) v.findViewById(R.id.BtnPick);
        Button[Arrange_ScrType.PICK].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();
                InsertCommand(Arrange_ScrType.PICK,"집어들기", 0);
            }
        });

        Button[Arrange_ScrType.PUT] = (Button) v.findViewById(R.id.BtnPut);
        Button[Arrange_ScrType.PUT].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSound();
                InsertCommand(Arrange_ScrType.PUT, "내려놓기", 0);
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
        mScriptListener = null;
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

    public static Adapter_ScriptList getAdapter_ScriptList() {
        return Adapter_ScriptList;
    }

    public static int getIndexProgram() {
        return indexProgram;
    }

    public static void setIndexProgram(int indexProgram) {
        FragmentScript.indexProgram = indexProgram;
    }

    public static LinkedList<Script> getList_script() {
        return List_script;
    }

    public void Button_On(int type)
    {
        Button[statusBtn].setBackgroundResource(R.drawable.button_borderline2);
        statusBtn = type;
        Button[statusBtn].setBackgroundResource(R.drawable.button_borderline_selected);
        // 리스트에 데이터가 있음
        if (List_script.size() > 0)
            Toast.makeText(getActivity(), "삭제할 명령을 선택하세요.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), "명령이 존재하지 없습니다.", Toast.LENGTH_SHORT).show();
    }

    public void Button_Off()
    {
        Button[statusBtn].setBackgroundResource(R.drawable.button_borderline2);
        statusBtn = ScrType.EMPTY;
        if (ScriptManager.getSel1() != -1)
        {
            ScriptManager.SelectedScript_Off(List_script);
            Adapter_ScriptList.notifyDataSetChanged();
        }
    }

    // 클릭 효과음
    public void ClickSound() {
        if (AudioService_Click.mute == false) {
            Intent intent_audio = new Intent(getActivity(), AudioService_Click.class);
            getActivity().startService(intent_audio);
        }
    }

    public static Program getSeledtedProgram() {
        return seledtedProgram;
    }
}
