package e.t.gameworkshop;

import java.util.LinkedList;

public class ScriptManager {

    private static int sel1 = -1, sel2 = -1;

    public static int Index_NextScript(LinkedList<Script> inputList, int i) {
        int sum = 1;
        ++i;
        while (sum > 0)
        {
            if (inputList.get(i).getType() >= -2 && inputList.get(i).getType() <= 3)
                ++sum;
            else if (inputList.get(i).getType() <= -3)
                --sum;
            ++i;
        }
        return i;
    }

    public static int Index_Head(LinkedList<Script> inputList, int index) {
        int sum = 1;
        --index;
        while (sum > 0)
        {
            if (inputList.get(index).getType() >= -2 && inputList.get(index).getType() <= 3)
                --sum;
            else if (inputList.get(index).getType() <= -3)
                ++sum;
            --index;
        }
        return index + 1;
    }

    public static int Index_Tail(LinkedList<Script> inputList, int index) {
        int sum = 1;
        ++index;            // 머리 다음부터 검색
        while (sum > 0)
        {
            if (inputList.get(index).getType() >= -2 && inputList.get(index).getType() <= 3)
                ++sum;
            else if (inputList.get(index).getType() <= -3)
                --sum;
            ++index;
        }
        return index - 1;   // i가 꼬리 다음을 가리키므로
    }

    public static void ChangeTo_ELSE(LinkedList<Script> inputList, int indexCondition, int tailIndex) {
        Script s = inputList.get( indexCondition );
        s.getConditions().clear();
        Popup_Script_ConditionActivity.getAdapter_ConditionList().notifyDataSetChanged();
        s.setType(ScrType.ELSE);
        s.setName("그외");
        inputList.get(tailIndex).setType(ScrType.ELSETAIL);
    }

    public static void Delete_Next_ELSE(LinkedList<Script> inputList, int indexNext) {
        Script nextS = inputList.get( indexNext );
        int nextTail = Index_Tail(inputList, indexNext);
        inputList.remove(nextTail);
        inputList.remove(nextS);
    }

    public static void Update_Inclusion(LinkedList<Script> inputList) {
        int i = 0;
        LinkedList<Integer> list = new LinkedList<Integer>();
        while (i < inputList.size())
        {
            // 포함 비우기
            if (inputList.get(i).inclusion.length() > 0)
                inputList.get(i).inclusion = "";

            // 꼬리
            if (inputList.get(i).getType() <= -3)
                list.pop();

            // 새로 채우기
            if (list.size() > 0)
            {
                for (int j = list.size() - 1; j >= 0; --j)
                {
                    Script s = inputList.get(i);
                    // elseif, if 는 음수 -> '-' 가 출력됨
                    if (inputList.get(list.get(j)).getType() == ScrType.ELSEIF || inputList.get(list.get(j)).getType() == ScrType.ELSE)
                        s.inclusion += String.valueOf( ScrType.IF );
                    else s.inclusion += String.valueOf( inputList.get(list.get(j)).getType());
                }
            }
            // 머리
            if (inputList.get(i).getType() >= -2 && inputList.get(i).getType() <= 3)
                list.push(i);
            ++i;
        }
    }

    public static boolean Addible_Script(LinkedList<Script> inputList, int index)
    {
        if (index + 1 != inputList.size())
        {
            int next = inputList.get(index + 1).getType();
            if (next == ScrType.ELSEIF || next == ScrType.ELSE)
                return false;
        }
        return true;
    }


    public static void Change_IfToOthers(LinkedList<Script> inputList, int index) {
            if (index < inputList.size())
            {
                if (inputList.get(index).getType() == ScrType.ELSEIF && inputList.get(index -1).getType() != ScrType.IFTAIL) {
                    inputList.get(index).setType(ScrType.IF);
                    inputList.get(index).setName("만약");
                } else if (inputList.get(index).getType() == ScrType.ELSE && inputList.get(index -1).getType() != ScrType.IFTAIL)
                    Delete_Next_ELSE(inputList, index);
            }
    }

    public static boolean Check_HeadToTail(LinkedList<Script> inputList, int start, int des, int tail)
    {
        int sum = 0;
        if (start < des) ++sum;
        ++des;
        while (des <= tail)
        {
            if (inputList.get(des).getType() >= -2 && inputList.get(des).getType() <= 3)
                ++sum;
            else if (inputList.get(des).getType() <= -3)
            {
                if (sum == 0) return false;
                --sum;
            }
            ++des;
        }
        if (sum != 0) return false;
        else return true;
    }

    public static boolean Check_TailToHead(LinkedList<Script> inputList, int start, int des, int head)
    {
        int sum = 0;
        if (start > des) ++sum;

        while (des >= head)
        {
            if (inputList.get(des).getType() >= -2 && inputList.get(des).getType() <= 3)
            {
                if (sum == 0) return false;
                --sum;
            }
            else if (inputList.get(des).getType() <= -3)
                ++sum;
            --des;
        }
        if (sum != 0) return false;
        else return true;
    }

    public static void Update_InnerStruct(LinkedList<Script> inputList, int start, int des)
    {
        while (start < des)
        {
            if (inputList.get(start).getType() == ScrType.IF)
                break;
            else if (inputList.get(start).getType() == ScrType.ELSEIF)
            {
                Change_IfToOthers(inputList, start);
                break;
            }
            else if (inputList.get(start).getType() == ScrType.ELSE )
            {
                Delete_Next_ELSE(inputList, start);
                break;
            }
            ++start;
        }
    }

    public static boolean CheckDes_OneScript(LinkedList<Script> inputList, int start, int des)
    {
        switch (inputList.get(start).getType())
        {
            case ScrType.IF:
            {
                // 머리, 꼬리 위치가 맞는가
                int tail = Index_Tail(inputList, start);
                if (Check_HeadToTail(inputList, start, des, tail) && des < tail)
                    return true;
                else
                {
                    inputList.get(start).setSelected(false);
                    return false;
                }
            }

            case ScrType.ELSEIF:
            {
                // 머리, 꼬리 위치가 맞는가
                int tail = Index_Tail(inputList, start);
                if (Check_HeadToTail(inputList, start, des, tail) && des < tail)
                {
                    int prevType = inputList.get(des).getType();
                    int nextType = inputList.get(des+1).getType();

                    if (prevType != ScrType.IFTAIL && nextType == ScrType.IF ||
                            prevType == ScrType.ELSETAIL)
                    {
                        inputList.get(start).setType(ScrType.IF);
                        inputList.get(start).setName("만약");
                    }
                    break;
                }
                else
                {
                    inputList.get(start).setSelected(false);
                    return false;
                }
            }
            case ScrType.ELSE:
            {
                // 머리, 꼬리 위치가 맞는가
                int tail = Index_Tail(inputList, start);
                if (Check_HeadToTail(inputList, start, des, tail) && des < tail)
                {
                    int prevType = inputList.get(des).getType();

                    if (prevType != ScrType.IFTAIL)
                    {
                        inputList.add(start, new Script("만약", ScrType.IF, start + 2));
                        inputList.add(start+1, new Script("조건꼬리",  ScrType.IFTAIL, 0));
                    }
                    return true;
                }
                else
                {
                    inputList.get(start).setSelected(false);
                    return false;
                }
            }
            case ScrType.IFTAIL:
            {
                int head = Index_Head(inputList, start);
                if (Check_TailToHead(inputList, start, des, head) && des >= head)
                    return true;
                else
                {
                    inputList.get(start).setSelected(false);
                    return false;
                }
            }
            case ScrType.ELSETAIL:
            {
                int head = Index_Head(inputList, start);
                if (Check_TailToHead(inputList, start, des, head) && des >= head)
                {
                    if (des+1 < inputList.size())
                    {
                        int nextType = inputList.get(des+1).getType();
                        if (nextType == ScrType.ELSEIF || nextType == ScrType.ELSE)
                            Change_IfToOthers(inputList, des+1);
                    }
                    return true;
                }
                else
                {
                    inputList.get(start).setSelected(false);
                    return false;
                }
            }
            case ScrType.FOR:
            case ScrType.WHILE:
            {
                int tail = Index_Tail(inputList, start);
                if (Check_HeadToTail(inputList, start, des, tail) && des < tail)
                {
                    int nextType = inputList.get(des+1).getType();

                    if (nextType == ScrType.ELSEIF || nextType == ScrType.ELSE)
                    {
                        inputList.get(start).setSelected(false);
                        return false;
                    }
                }
                else
                {
                    inputList.get(start).setSelected(false);
                    return false;
                }
                break;
            }
            case ScrType.FORTAIL:
            case ScrType.WHILETAIL:
            {
                int head = Index_Head(inputList, start);
                if (Check_TailToHead(inputList, start, des, head) && des >= head)
                {
                    if (des+1 == inputList.size()) return true;
                    int nextType = inputList.get(des+1).getType();

                    if (nextType == ScrType.ELSEIF || nextType == ScrType.ELSE)
                    {
                        inputList.get(start).setSelected(false);
                        return false;
                    }
                }
                else
                {
                    inputList.get(start).setSelected(false);
                    return false;
                }
                break;
            }
            default:
            {
                if (des+1 == inputList.size()) return true;
                int nextType = inputList.get(des+1).getType();

                if (nextType == ScrType.ELSEIF || nextType == ScrType.ELSE)
                {
                    inputList.get(start).setSelected(false);
                    return false;
                }
                break;
            }
        }
        return true;
    }

    public static void Move_OneScript(LinkedList<Script> inputList, int des)
    {
        if (sel1 == -1)
        {
            sel1 = des;
            inputList.get(sel1).setSelected(true);          // 색상으로 선택 표시
        }
        else if (sel1 == des)
        {
            inputList.get(sel1).setSelected(false);
            sel1 = -1;
        }
        else
        {
            if (CheckDes_OneScript(inputList, sel1, des))
            {
                // 이동
                Script temp = inputList.get(sel1);
                if (sel1 > des)
                {
                        inputList.remove(sel1);
                        inputList.add(des + 1, temp);
                }
                else if (sel1 < des)
                {
                        inputList.add(des + 1, inputList.get(sel1));
                        inputList.remove(sel1);
                }

                // if 조건문 내부 오류 문법 수정
                if (temp.getType() == ScrType.IF || temp.getType() == ScrType.ELSEIF || temp.getType() == ScrType.ELSE)
                    Update_InnerStruct(inputList, des+2, sel1 + 1);
                else if (temp.getType() <= -3)
                    Update_InnerStruct(inputList, Index_Head(inputList, sel1)+1, des);

                // 이동 후 정리
                if (sel1 > des)
                    inputList.get(des +1).setSelected(false);
                else if (sel1 < des)
                    inputList.get(des).setSelected(false);
                sel1 = -1;
                Update_Inclusion(inputList);
            }
            else
            {
                inputList.get(des).setSelected(false);
                sel1 = -1;
            }
        }
    }

    public static boolean CheckDes_RangeScript(LinkedList<Script> inputList, int i1, int i2, int des)
    {
        if (des+1 == inputList.size()) return true;
        int typeDes = inputList.get(des).getType();
        int typeDesNext = inputList.get(des+1).getType();
        int typeHead = inputList.get(i1).getType();

        switch (inputList.get(i2).getType())
        {
            case ScrType.IFTAIL:
            {
                if (typeHead == ScrType.ELSEIF)
                {
                    if (typeDes == ScrType.IFTAIL)
                        return true;
                    else return false;
                }
                else if (typeHead == ScrType.ELSE)
                {
                    if (typeDes == ScrType.IFTAIL)
                        return true;
                    else return false;
                }
                else
                {
                    if (i2 + 1 == inputList.size()) return true;
                    Script typeTailNext = inputList.get(i2+1);

                    if (typeTailNext.getType() == ScrType.ELSEIF)
                    {
                        typeTailNext.setType(ScrType.IF);
                        typeTailNext.setName("만약");
                    }
                    else if (typeTailNext.getType() == ScrType.ELSE)
                        Delete_Next_ELSE(inputList, i2+1);
                }
                break;
            }
            case ScrType.ELSETAIL:
            {
                if (typeHead == ScrType.ELSEIF && typeDes != ScrType.IFTAIL ||
                    typeHead == ScrType.ELSE && typeDes != ScrType.IFTAIL)
                    return false;
                else if (typeDesNext == ScrType.ELSEIF) Change_IfToOthers(inputList, typeDesNext);
                else if (typeDesNext == ScrType.ELSE) Delete_Next_ELSE(inputList, typeDesNext);
                break;
            }
            default:
            {
                if (typeDesNext == ScrType.ELSEIF || typeDesNext == ScrType.ELSE)
                    return false;
                else if (typeHead == ScrType.ELSEIF && typeDes != ScrType.IFTAIL ||
                        typeHead == ScrType.ELSE && typeDes != ScrType.IFTAIL)
                    return false;
                break;
            }
        }
        return true;
    }


    public static void Move_RangeScript(LinkedList<Script> inputList, int indexSelect)
    {
        // 1개만 클릭됐을 때 같은 칸을 다시 클릭
        if (sel1 == indexSelect && sel2 == -1)
        {
            inputList.get(sel1).setSelected(false);
            sel1 = -1;
            return;
        }
        // 칸이 전혀 선택되지 않음
        if (sel1 == -1)
        {
            sel1 = indexSelect;
            inputList.get(sel1).setSelected(true);
        }
        // 1개만 클릭됨
        else if (sel2 == -1)
        {
            // 반드시 se1 < sel2
            if (sel1 > indexSelect)
            {
                sel2 = sel1;
                sel1 = indexSelect;
            }
            else sel2 = indexSelect;
            // 머리와 꼬리가 잘 포함되었는 지 확인
            int sum = 0;
            for (int j = sel1; j <= sel2; ++j)
            {
                if (inputList.get(j).getType() >= -2 && inputList.get(j).getType() <= 3)
                    ++sum;
                else if (inputList.get(j).getType() <= -3)
                {
                    if (sum == 0)
                    {
                        SelectedScript_Off(inputList);
                        return;
                    }
                    --sum;
                }
            }
            if (sum != 0)
            {
                SelectedScript_Off(inputList);
                return;
            }
            // 범위 선택 표시
            for (int j = sel1; j <= sel2; ++j)
                inputList.get(j).setSelected(true);
        }
        else
        {
            // 도착 == 범위 안
            if (indexSelect >= sel1 - 1 && indexSelect <= sel2)
            {
                for (int j = sel1; j <= sel2; ++j)
                    inputList.get(j).setSelected(false);

                inputList.get(indexSelect).setSelected(true);
                sel1 = indexSelect;
                sel2 = -1;
                return;
            }

            // 도착 칸에 이동 가능 여부
            if (CheckDes_RangeScript(inputList, sel1, sel2, indexSelect))
            {
                LinkedList<Script> list = new LinkedList<Script>();
                int k = 0;
                for (int j = sel1; j <= sel2; ++j)
                    list.add(inputList.get(j));

                if (sel1 > indexSelect)
                {
                    // 삭제 후 추가
                    for (int j = sel1; j <= sel2; ++j)
                        inputList.remove(sel1);
                    for (int j = 0; j < list.size(); ++j)
                    {
                        inputList.add(indexSelect + 1 + j, list.get(j));
                        inputList.get(indexSelect+1+j).setSelected(false);
                    }
                }
                else if (sel2 < indexSelect)
                {
                    // 추가 후 삭제
                    for (int j = 0; j < list.size(); ++j)
                    {
                        inputList.add(indexSelect + 1 + j, list.get(j));
                        inputList.get(indexSelect+1+j).setSelected(false);
                    }
                    for (int j = sel1; j <= sel2; ++j)
                        inputList.remove(sel1);
                }

                Update_Inclusion(inputList);
                sel1 = -1;
                sel2 = -1;
            }
            else SelectedScript_Off(inputList);
        }
    }

    public static void Delete_OneScript(LinkedList<Script> inputList, int indexsel)
    {
        // 꼬리x
        if (inputList.get(indexsel).getType() > -3)
        {
            // 머리
            if (inputList.get(indexsel).getType() >= -2 && inputList.get(indexsel).getType() <= 3)
            {
                int tail = Index_Tail(inputList, indexsel);
                inputList.remove(indexsel);
                inputList.remove(tail - 1);   // 머리가 지워짐
            }
            // 나머지 명령문
            else
            {
                inputList.remove(indexsel);
            }
        }
        Update_Inclusion(inputList);
    }

    public static void Delete_RangeScript(LinkedList<Script> inputList, int indexSelect)
    {
        // 1개만 클릭됐을 때 같은 칸을 다시 클릭
        if (sel1 == indexSelect && sel2 == -1)
        {
            inputList.get(sel1).setSelected(false);
            sel1 = -1;
            return;
        }
        // 칸이 전혀 선택되지 않음
        if (sel1 == -1)
        {
            sel1 = indexSelect;
            inputList.get(sel1).setSelected(true);
        }
        // 1개만 클릭됨
        else if (sel2 == -1)
        {
            // 반드시 se1 < sel2
            if (sel1 > indexSelect)
            {
                sel2 = sel1;
                sel1 = indexSelect;
            }
            else sel2 = indexSelect;
            // 머리와 꼬리가 잘 포함되었는 지 확인
            int sum = 0;
            for (int j = sel1; j <= sel2; ++j)
            {
                if (inputList.get(j).getType() >= -2 && inputList.get(j).getType() <= 3)
                    ++sum;
                else if (inputList.get(j).getType() <= -3)
                {
                    if (sum == 0)
                    {
                        SelectedScript_Off(inputList);
                        return;
                    }
                    --sum;
                }
            }
            if (sum != 0)
            {
                SelectedScript_Off(inputList);
                return;
            }
            // 범위 선택 표시
            for (int j = sel1; j <= sel2; ++j)
                inputList.get(j).setSelected(true);
        }
        else
        {
            // if 없는 elseif, else 처리
            if (sel2+1 < inputList.size())
            {
                int typeNext = inputList.get(sel2 + 1).getType();
                if (typeNext == ScrType.ELSEIF) {
                    inputList.get(sel2 + 1).setType(ScrType.IF);
                    inputList.get(sel2 + 1).setName("만약");
                } else if (typeNext == ScrType.ELSE)
                    Delete_Next_ELSE(inputList, sel2 + 1);
            }
            Update_Inclusion(inputList);
        }
    }

    public static void SelectedScript_Off(LinkedList<Script> inputList)
    {
        if (inputList.size() > 0)
        {
            if (sel2 != -1)
            {
                for (int j = sel1; j <= sel2; ++j)
                    inputList.get(j).setSelected(false);
                sel1 = -1;
                sel2 = -1;
            }
            else if (sel1 != -1)
            {
                inputList.get(sel1).setSelected(false);
                sel1 = -1;
            }
        }
    }

    public static int getSel1() {
        return sel1;
    }

    public static int getSel2() {
        return sel2;
    }

    public static void setSel1(int sel1) {
        ScriptManager.sel1 = sel1;
    }

    public static void setSel2(int sel2) {
        ScriptManager.sel2 = sel2;
    }
}
