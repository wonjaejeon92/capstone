package e.t.gameworkshop;

public class Challenger_Record {

    private static String senderID;
    private static int senderLevel, senderExp, senderWIN, senderLOSE, stageNUMBER, recordCost, recordTime, recordAction;

    public static void set(String senderid, int senderlevel, int senderexp, int senderwin, int senderlose, int stagenumber, int recordcost, int recordtime, int recordaction)
    {
        senderID = senderid;
        senderLevel = senderlevel;
        senderExp = senderexp;
        senderWIN = senderwin;
        senderLOSE = senderlose;
        stageNUMBER = stagenumber;
        recordCost = recordcost;
        recordTime = recordtime;
        recordAction = recordaction;
    }

    public static void LevelUp(int exp)
    {
        senderExp += exp;
        if (senderExp >= GameExp.MAXEXP)
        {
            senderExp -= GameExp.MAXEXP;
            ++senderLevel;
        }
    }

    public static String getSenderID() {
        return senderID;
    }

    public static int getSenderLevel() {
        return senderLevel;
    }

    public static int getSenderExp() {
        return senderExp;
    }

    public static int getSenderWIN() {
        return senderWIN;
    }

    public static int getSenderLOSE() {
        return senderLOSE;
    }

    public static int getStageNUMBER() {
        return stageNUMBER;
    }

    public static int getRecordCost() {
        return recordCost;
    }

    public static int getRecordTime() {
        return recordTime;
    }

    public static int getRecordAction() {
        return recordAction;
    }

    public static void setSenderLevel(int senderLevel) {
        Challenger_Record.senderLevel = senderLevel;
    }

    public static void setSenderExp(int senderExp) {
        Challenger_Record.senderExp = senderExp;
    }

    public static void setSenderWIN(int senderWIN) {
        Challenger_Record.senderWIN = senderWIN;
    }

    public static void setSenderLOSE(int senderLOSE) {
        Challenger_Record.senderLOSE = senderLOSE;
    }
}
