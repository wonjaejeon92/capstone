package e.t.gameworkshop;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.LinkedList;

import e.t.gameworkshop.Arrange.FragmentScript;

public class Popup_Script_ConditionActivity extends Activity {

    private TextView TextName;
    private Button BtnAnd, BtnOr, BtnAdd, BtnChange;
    private Spinner SpinLeft, SpinRight, SpinCompare, SpinIF;
    private static ListView listViewCondition;
    private static LinkedList<Condition> List_condition;
    private static Adapter_ConditionList Adapter_ConditionList;
    private ArrayList<String> conLeftArrayList, conRightArrayList, compareArrayList, ifArrayList;
    private ArrayAdapter<String> compareArrayAdapter, conLeftArrayAdapter, conRightArrayAdapter, ifArrayAdapter;
    private static int indexCommand, indexSpinLeft, indexSpinCompare, indexSpinRight, indexSpinIF;
    private final static int IF = 0, ELSEIF = 1, ELSE = 2;
    private static boolean selAnd = false, selOr = false;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 타이틀바 삭제
        setContentView(R.layout.activity_popup_script_condition);

        TextName = (TextView) findViewById(R.id.TextName);
        SpinLeft = (Spinner) findViewById(R.id.SpinLeft);
        SpinRight = (Spinner) findViewById(R.id.SpinRight);
        SpinCompare = (Spinner) findViewById(R.id.SpinCompare);

        // Left Spinner
        conLeftArrayList = new ArrayList<String>();
        conLeftArrayList.add("현재 위치");
        conLeftArrayList.add("↑");
        conLeftArrayList.add("↓");
        conLeftArrayList.add("←");
        conLeftArrayList.add("→");
        conLeftArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, conLeftArrayList);
        SpinLeft.setAdapter(conLeftArrayAdapter);
        // Right Spinner
        conRightArrayList = new ArrayList<String>();
        conRightArrayList.add("없음");
        conRightArrayList.add("벽");
        conRightArrayList.add("저장고");
        conRightArrayList.add("장애물");
        conRightArrayList.add("물건");
        conRightArrayList.add("로봇");
        conRightArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, conRightArrayList);
        SpinRight.setAdapter(conRightArrayAdapter);
        // Middle Spinner
        compareArrayList = new ArrayList<String>();
        compareArrayList.add("=");
        compareArrayList.add("≠");
        compareArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, compareArrayList);
        SpinCompare.setAdapter(compareArrayAdapter);

        // 스크립트 종류 구별
        BtnAnd = (Button) findViewById(R.id.BtnAnd);
        BtnOr = (Button) findViewById(R.id.BtnOr);
        SpinIF = (Spinner) findViewById(R.id.SpinIF);
        BtnChange = (Button) findViewById(R.id.BtnChange);
        switch (FragmentScript.getList_script().get(indexCommand).getType()) {
            case ScrType.IF:
            case ScrType.ELSEIF:
            case ScrType.ELSE: {
                TextName.setText("조건문");
                ifArrayList = new ArrayList<String>();
                ifArrayList.add("만약");
                ifArrayList.add("아니면");
                ifArrayList.add("그외");
                ifArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ifArrayList);
                SpinIF.setAdapter(ifArrayAdapter);
                indexSpinIF = SpinIF.getSelectedItemPosition();
                indexSpinLeft = SpinLeft.getSelectedItemPosition();
                indexSpinCompare = SpinCompare.getSelectedItemPosition();
                indexSpinRight = SpinRight.getSelectedItemPosition();
                break;
            }
            case ScrType.WHILE: {
                TextName.setText("조건 반복문");
                SpinIF.setVisibility(View.GONE);
                BtnChange.setVisibility(View.GONE);
                break;

            }
        }

        // 리스트 표시
        listViewCondition = (ListView) findViewById(R.id.listCondition);
        List_condition = FragmentScript.getList_script().get(indexCommand).getConditions();
        Adapter_ConditionList = new Adapter_ConditionList(getApplicationContext(), List_condition);
        listViewCondition.setAdapter(Adapter_ConditionList);


        // if - else if - else 변환 버튼
        BtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이전 스크립트 정보
                Script curScript = FragmentScript.getList_script().get(indexCommand);
                int indexNext = ScriptManager.Index_NextScript(FragmentScript.getList_script(),indexCommand);

                switch (FragmentScript.getList_script().get(indexCommand).getType())
                {
                    case ScrType.IF:
                    {
                        if (indexSpinIF == IF) {
                            Toast.makeText(getApplicationContext(), "동일한 문장입니다.", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        if ((indexCommand - 1) >= 0)
                        {
                            if (FragmentScript.getList_script().get(indexCommand - 1).getType() == ScrType.IFTAIL)
                            {
                                if (indexSpinIF == ELSEIF)
                                {
                                    curScript.setType(ScrType.ELSEIF);
                                    curScript.setName("아니면");
                                }
                                else if (indexSpinIF == ELSE) {
                                    // 꼬리가 마지막 스크립트
                                    if ( indexNext == FragmentScript.getList_script().size() )
                                        ScriptManager.ChangeTo_ELSE(FragmentScript.getList_script(), indexCommand, indexNext - 1);

                                    else
                                    {
                                        Script nextScript = FragmentScript.getList_script().get( indexNext );
                                        if (nextScript.getType() == ScrType.ELSEIF) {
                                            nextScript.setType(ScrType.IF);
                                            nextScript.setName("만약");
                                            ScriptManager.ChangeTo_ELSE(FragmentScript.getList_script(), indexCommand, indexNext - 1);
                                        }
                                        else if (nextScript.getType() == ScrType.ELSE) {
                                            // 삭제 된다는 경고 메시지
                                            ScriptManager.Delete_Next_ELSE(FragmentScript.getList_script(), indexNext);
                                            ScriptManager.ChangeTo_ELSE(FragmentScript.getList_script(), indexCommand, indexNext - 1);
                                        }
                                        else
                                            ScriptManager.ChangeTo_ELSE(FragmentScript.getList_script(), indexCommand, indexNext - 1);
                                    }
                                }
                            }
                            else
                                Toast.makeText(getApplicationContext(), "문장 위에 만약 조건문이 필요합니다.", Toast.LENGTH_SHORT).show();

                        }
                        else
                            Toast.makeText(getApplicationContext(), "문장 위에 만약 조건문이 필요합니다.", Toast.LENGTH_SHORT).show();
                        // 갱신
                        FragmentScript.getAdapter_ScriptList().notifyDataSetChanged();
                        break;
                    }

                    case ScrType.ELSEIF:
                    {
                        if (indexSpinIF == ELSEIF) {
                            Toast.makeText(getApplicationContext(), "동일한 문장입니다.", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        if (indexSpinIF == ELSE) {
                            // 다음 스크립트 없음
                            if (indexNext == FragmentScript.getList_script().size())
                                ScriptManager.ChangeTo_ELSE(FragmentScript.getList_script(), indexCommand, indexNext - 1);
                            else
                            {
                                Script nextScript = FragmentScript.getList_script().get( indexNext );
                                if (nextScript.getType() == ScrType.ELSEIF) {
                                    nextScript.setType(ScrType.IF);
                                    nextScript.setName("만약");
                                    ScriptManager.ChangeTo_ELSE(FragmentScript.getList_script(), indexCommand, indexNext - 1);
                                }
                                else if (nextScript.getType() == ScrType.ELSE) {
                                    // 삭제 된다는 경고 메시지
                                    ScriptManager.Delete_Next_ELSE(FragmentScript.getList_script(), indexNext);
                                    ScriptManager.ChangeTo_ELSE(FragmentScript.getList_script(), indexCommand, indexNext - 1);
                                }
                                else if (nextScript.getType() == ScrType.IF || nextScript.getNum() <= 0) {
                                    ScriptManager.ChangeTo_ELSE(FragmentScript.getList_script(), indexCommand, indexNext - 1);
                                }
                            }
                        }
                        else if (indexSpinIF == IF) {
                            curScript.setType(ScrType.IF);
                            curScript.setName("만약");
                        }
                        else
                            Toast.makeText(getApplicationContext(), "문장 아래에 조건문이 없어야 합니다.", Toast.LENGTH_SHORT).show();
                        // 갱신
                        FragmentScript.getAdapter_ScriptList().notifyDataSetChanged();
                        break;
                    }

                    case ScrType.ELSE:
                    {
                        if (indexSpinIF == ELSE) {
                            Toast.makeText(getApplicationContext(), "동일한 문장입니다.", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        if (indexSpinIF == IF)
                        {
                            curScript.setType(ScrType.IF);
                            curScript.setName("만약");
                            FragmentScript.getList_script().get( indexNext - 1 ).setType(ScrType.IFTAIL);
                        }
                        else if (indexSpinIF == ELSEIF)
                        {
                            curScript.setType(ScrType.ELSEIF);
                            curScript.setName("아니면");
                            FragmentScript.getList_script().get( indexNext - 1 ).setType(ScrType.IFTAIL);
                        }
                        // 갱신
                        FragmentScript.getAdapter_ScriptList().notifyDataSetChanged();
                        break;
                    }
                }
            }
        });

        SpinIF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                indexSpinIF = SpinIF.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SpinLeft.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                indexSpinLeft = SpinLeft.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SpinCompare.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                indexSpinCompare = SpinCompare.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SpinRight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                indexSpinRight = SpinRight.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // 추가 버튼
        BtnAdd = (Button) findViewById(R.id.BtnAdd);
        BtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // else 문이 아님
                Script script = FragmentScript.getList_script().get(indexCommand);
                if (script.getType() != ScrType.ELSE) {
                    // 저장
                    if (selAnd || script.getConditions().size() == 0)
                        script.getConditions().add(new Condition(indexSpinLeft, indexSpinCompare, indexSpinRight, 1));
                    else if (selOr)
                        script.getConditions().add(new Condition(indexSpinLeft, indexSpinCompare, indexSpinRight, 2));
                    else
                        Toast.makeText(getApplicationContext(), "조건식의 관계 버튼을 설정해주세요. (그리고, 또는)", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "해당 조건문은 조건식을 가질 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                // 갱신
                FragmentScript.getAdapter_ScriptList().notifyDataSetChanged();
                Adapter_ConditionList.notifyDataSetChanged();
            }
        });

        BtnAnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selOr = false;
                BtnOr.setBackgroundResource(R.drawable.button_borderline2);

                if (!selAnd) {
                    selAnd = true;
                    BtnAnd.setBackgroundResource(R.drawable.button_borderline_selected2);
                } else {
                    selAnd = false;
                    BtnAnd.setBackgroundResource(R.drawable.button_borderline2);
                }
            }
        });

        BtnOr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selAnd = false;
                BtnAnd.setBackgroundResource(R.drawable.button_borderline2);

                if (!selAnd) {
                    selOr = true;
                    BtnOr.setBackgroundResource(R.drawable.button_borderline_selected2);
                } else {
                    selOr = false;
                    BtnOr.setBackgroundResource(R.drawable.button_borderline2);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    public static void setIndexCommand(int indexCommand) {
        Popup_Script_ConditionActivity.indexCommand = indexCommand;
    }

    public static e.t.gameworkshop.Adapter_ConditionList getAdapter_ConditionList() {
        return Adapter_ConditionList;
    }
}