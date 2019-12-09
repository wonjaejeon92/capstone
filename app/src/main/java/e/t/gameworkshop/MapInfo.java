package e.t.gameworkshop;

import java.util.ArrayList;

import e.t.gameworkshop.Arrange.ArrangeObject;

public class MapInfo {

    public final static int SINGLE_MODE = 1;
    public final static int CHALLENGE_MODE = 2;
    public final static int APPROVAL_MODE = 3;

    private static int stagenumber, row, col;
    private static String competeUser;
    private static IndexSpace[] indexSpaces;
    private static int GameMode;

    private static ArrayList<ArrangeObject> arrangeObject;

    public static void stage(int n, int mode, String competeuser)
    {
        GameMode = mode;
        competeUser = competeuser;

        switch (n)
        {
            case 1:
            {
                stagenumber = 1;
                row = 3; col = 4;

                indexSpaces = new IndexSpace[4];
                indexSpaces[0] = new IndexSpace(1, 1, new Space(SpaceType.WALL,0,true));
                indexSpaces[1] = new IndexSpace(1, 2, new Space(SpaceType.WALL,0,true));
                indexSpaces[2] = new IndexSpace(2, 2, new Space(SpaceType.SAVE,0,false));
                indexSpaces[3] = new IndexSpace(2, 3, new Space(SpaceType.OBSTACLE,0,false));

                arrangeObject = new ArrayList<ArrangeObject>();
                arrangeObject.add(new ArrangeObject(0,2,2));
                arrangeObject.add(new ArrangeObject(2,0,1));
                break;
            }

            case 2:
            {
                stagenumber = 2;
                row = 5; col = 6;

                indexSpaces = new IndexSpace[6];
                indexSpaces[0] = new IndexSpace(0, 4, new Space(SpaceType.WALL,0,true));
                indexSpaces[1] = new IndexSpace(1, 1, new Space(SpaceType.SAVE,0,false));
                indexSpaces[2] = new IndexSpace(1, 2, new Space(SpaceType.WALL,0,true));
                indexSpaces[3] = new IndexSpace(4, 3, new Space(SpaceType.WALL,0,true));
                indexSpaces[4] = new IndexSpace(2, 4, new Space(SpaceType.WALL,0,true));
                indexSpaces[5] = new IndexSpace(2, 2, new Space(SpaceType.OBSTACLE,0,false));

                arrangeObject = new ArrayList<ArrangeObject>();
                arrangeObject.add(new ArrangeObject(0,3,4));
                arrangeObject.add(new ArrangeObject(3,1,2));
                arrangeObject.add(new ArrangeObject(3,3,1));
                arrangeObject.add(new ArrangeObject(3,5,2));
                break;
            }

            case 3:
            {
                stagenumber = 3;
                row = 10; col = 10;

                indexSpaces = new IndexSpace[28];
                indexSpaces[0] = new IndexSpace(0, 1, new Space(SpaceType.WALL,0,true));
                indexSpaces[1] = new IndexSpace(3, 5, new Space(SpaceType.WALL,0,true));
                indexSpaces[2] = new IndexSpace(4, 5, new Space(SpaceType.WALL,0,true));
                indexSpaces[3] = new IndexSpace(4, 6, new Space(SpaceType.WALL,0,true));
                indexSpaces[4] = new IndexSpace(5, 3, new Space(SpaceType.WALL,0,true));
                indexSpaces[5] = new IndexSpace(5, 4, new Space(SpaceType.WALL,0,true));
                indexSpaces[6] = new IndexSpace(6, 4, new Space(SpaceType.WALL,0,true));
                indexSpaces[7] = new IndexSpace(3, 1, new Space(SpaceType.WALL,0,true));
                indexSpaces[8] = new IndexSpace(4, 1, new Space(SpaceType.WALL,0,true));
                indexSpaces[9] = new IndexSpace(7, 1, new Space(SpaceType.WALL,0,true));
                indexSpaces[10] = new IndexSpace(8, 3, new Space(SpaceType.WALL,0,true));
                indexSpaces[11] = new IndexSpace(9, 8, new Space(SpaceType.WALL,0,true));
                indexSpaces[12] = new IndexSpace(1, 8, new Space(SpaceType.WALL,0,true));
                indexSpaces[13] = new IndexSpace(2, 7, new Space(SpaceType.WALL,0,true));
                indexSpaces[14] = new IndexSpace(4, 4, new Space(SpaceType.SAVE,0,false));
                indexSpaces[15] = new IndexSpace(5, 5, new Space(SpaceType.SAVE,0,false));
                indexSpaces[16] = new IndexSpace(3, 3, new Space(SpaceType.OBSTACLE,0,false));
                indexSpaces[17] = new IndexSpace(7, 5, new Space(SpaceType.OBSTACLE,0,false));
                indexSpaces[18] = new IndexSpace(5, 0, new Space(SpaceType.OBSTACLE,0,false));
                indexSpaces[19] = new IndexSpace(6, 9, new Space(SpaceType.OBSTACLE,0,false));
                indexSpaces[20] = new IndexSpace(3, 0, new Space(SpaceType.EMPTY,2,false));
                indexSpaces[21] = new IndexSpace(4, 0, new Space(SpaceType.EMPTY,1,false));
                indexSpaces[22] = new IndexSpace(1, 7, new Space(SpaceType.EMPTY,1,false));
                indexSpaces[23] = new IndexSpace(8, 8, new Space(SpaceType.EMPTY,2,false));
                indexSpaces[24] = new IndexSpace(9, 1, new Space(SpaceType.EMPTY,1,false));
                indexSpaces[25] = new IndexSpace(4, 9, new Space(SpaceType.EMPTY,2,false));
                indexSpaces[26] = new IndexSpace(7, 2, new Space(SpaceType.EMPTY,2,false));
                indexSpaces[27] = new IndexSpace(8, 2, new Space(SpaceType.EMPTY,1,false));

                arrangeObject = new ArrayList<ArrangeObject>();
                arrangeObject.add(new ArrangeObject(3,0,2));
                arrangeObject.add(new ArrangeObject(4,0,1));
                arrangeObject.add(new ArrangeObject(1,7,1));
                arrangeObject.add(new ArrangeObject(8,8,2));
                arrangeObject.add(new ArrangeObject(9,1,1));
                arrangeObject.add(new ArrangeObject(4,9,2));
                arrangeObject.add(new ArrangeObject(7,2,2));
                arrangeObject.add(new ArrangeObject(8,2,1));
                break;
            }
        }
    }

    public static int getGameMode() {
        return GameMode;
    }

    public static int getStagenumber() {
        return stagenumber;
    }

    public static int getRow() {
        return row;
    }

    public static int getCol() {
        return col;
    }

    public static IndexSpace[] getIndexSpaces() {
        return indexSpaces;
    }

    public static ArrayList<ArrangeObject> getArrangeObject() {
        return arrangeObject;
    }

    public static String getCompeteUser() {
        return competeUser;
    }

}
