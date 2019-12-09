package e.t.gameworkshop;

public class CurUserData {

    public static String userID;
    public static int userLevel, userExp, userWIN, userLOSE;

    public static void LevelUp(int exp)
    {
        userExp += exp;
        if (userExp >= GameExp.MAXEXP) {
            userExp -= GameExp.MAXEXP;
            ++userLevel;
        }
    }

}
