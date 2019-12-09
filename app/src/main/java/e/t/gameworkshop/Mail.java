package e.t.gameworkshop;

public class Mail {

    private String senderID;
    private int tag, senderLevel, senderExp, senderWIN, senderLOSE, stageNumber, recordCost, recordTime, recordAction;

    public Mail(int tag, String senderID, int senderLevel, int senderExp, int senderWIN, int senderLOSE, int stageNumber, int recordCost, int recordTime, int recordAction) {
        this.tag = tag;
        this.senderID = senderID;
        this.senderLevel = senderLevel;
        this.senderExp = senderExp;
        this.senderWIN = senderWIN;
        this.senderLOSE = senderLOSE;
        this.stageNumber = stageNumber;
        this.recordCost = recordCost;
        this.recordTime = recordTime;
        this.recordAction = recordAction;
    }

    public int getTag() {
        return tag;
    }

    public String getSenderID() {
        return senderID;
    }

    public int getSenderExp() {
        return senderExp;
    }

    public int getSenderLevel() {
        return senderLevel;
    }

    public int getSenderWIN() {
        return senderWIN;
    }

    public int getSenderLOSE() {
        return senderLOSE;
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public int getRecordCost() {
        return recordCost;
    }

    public int getRecordTime() {
        return recordTime;
    }

    public int getRecordAction() {
        return recordAction;
    }
}
