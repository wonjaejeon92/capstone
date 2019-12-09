package e.t.gameworkshop;

public class UserData {

    private String userID;
    private int userLevel, userWIN, userLOSE;

    public UserData(String userID, int userLevel, int userWIN, int userLOSE) {
        this.userID = userID;
        this.userLevel = userLevel;
        this.userWIN = userWIN;
        this.userLOSE = userLOSE;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getUserWIN() {
        return userWIN;
    }

    public void setUserWIN(int userWIN) {
        this.userWIN = userWIN;
    }

    public int getUserLOSE() {
        return userLOSE;
    }

    public void setUserLOSE(int userLOSE) {
        this.userLOSE = userLOSE;
    }
}
