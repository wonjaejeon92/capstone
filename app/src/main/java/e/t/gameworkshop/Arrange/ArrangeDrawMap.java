package e.t.gameworkshop.Arrange;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import e.t.gameworkshop.IndexSpace;
import e.t.gameworkshop.NumberRobotAnimation;
import e.t.gameworkshop.R;
import e.t.gameworkshop.SpaceType;

public class ArrangeDrawMap extends View {

    Paint paint;
    Bitmap bitmap[];
    private LinkedList<Worker> workers;
    private int SPACE_LENGTH = 100, START_GRID = 30, row, col;
    private boolean clickedRobot;
    private int objecttype;
    public int clickRow = 0, clickCol = 0;
    private LinkedList<ArrangeObject> objects;
    private IndexSpace[] indexSpaces;

    public ArrangeDrawMap(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public void setMapInfo(int stagenumber, int row, int col, IndexSpace[] indexSpace, ArrayList<ArrangeObject> arrangeObjects, LinkedList<Worker> workers) {
        // 크기
        this.row = row;
        this.col = col;
        if (row > col)
            START_GRID += ((10 - row) / 2) * SPACE_LENGTH;
        else
            START_GRID += ((10 - col) / 2) * SPACE_LENGTH;

        // 맵 그림 초기화
        InitMap();

        // 지형
        indexSpaces = indexSpace;
        objecttype = (stagenumber - 1) / 3;
        objects = new LinkedList<ArrangeObject>();
        for (int i = 0; i < arrangeObjects.size(); ++i)
            objects.add(arrangeObjects.get(i));
        // 로봇
        this.workers = workers;
    }

    public void setMapInfo(int row, int col, IndexSpace[] indexSpace, LinkedList<ArrangeObject> arrangeObjects, LinkedList<Worker> workers) {
        // 크기
        this.row = row;
        this.col = col;
        if (row > col)
            START_GRID += ((10 - row) / 2) * SPACE_LENGTH;
        else
            START_GRID += ((10 - col) / 2) * SPACE_LENGTH;

        // 맵 그림 초기화
        InitMap();

        // 지형 정보
        indexSpaces = indexSpace;
        objects = arrangeObjects;
        // 로봇 리스트
        this.workers = workers;
    }

    public void InitMap() {
        // 맵
        bitmap = new Bitmap[33];
        bitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_wall);
        bitmap[1] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_save);
        bitmap[2] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_obstacle);
        for (int i = 0; i < 3; ++i)
            bitmap[i] = Bitmap.createScaledBitmap(bitmap[i], SPACE_LENGTH, SPACE_LENGTH, true);

        // 로봇
        // 빨강
        bitmap[3] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_redrobot_up1);
        bitmap[4] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_redrobot_up2);
        bitmap[5] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_redrobot_down1);
        bitmap[6] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_redrobot_down2);
        bitmap[7] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_redrobot_left1);
        bitmap[8] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_redrobot_left2);
        bitmap[9] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_redrobot_right1);
        bitmap[10] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_redrobot_right2);
        // 노랑
        bitmap[11] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_orangerobot_up1);
        bitmap[12] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_orangerobot_up2);
        bitmap[13] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_orangerobot_down1);
        bitmap[14] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_orangerobot_down2);
        bitmap[15] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_orangerobot_left1);
        bitmap[16] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_orangerobot_left2);
        bitmap[17] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_orangerobot_right1);
        bitmap[18] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_orangerobot_right2);
        // 파랑
        bitmap[19] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_bluerobot_up1);
        bitmap[20] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_bluerobot_up2);
        bitmap[21] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_bluerobot_down1);
        bitmap[22] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_bluerobot_down2);
        bitmap[23] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_bluerobot_left1);
        bitmap[24] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_bluerobot_left2);
        bitmap[25] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_bluerobot_right1);
        bitmap[26] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_bluerobot_right2);
        for (int i = 3; i < 27; ++i)
            bitmap[i] = Bitmap.createScaledBitmap(bitmap[i], SPACE_LENGTH - 10, SPACE_LENGTH - 10, true);

        // 물건
        bitmap[27] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_object1);
        bitmap[28] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_object2);
        bitmap[29] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_object3);
        bitmap[30] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_object4);
        for (int i = 27; i < 31; ++i)
            bitmap[i] = Bitmap.createScaledBitmap(bitmap[i], SPACE_LENGTH / 3, SPACE_LENGTH / 3, true);
        // 로봇 사망 마크(X)
        bitmap[31] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_x);
        bitmap[31] = Bitmap.createScaledBitmap(bitmap[31], SPACE_LENGTH, SPACE_LENGTH, true);
    }

    public void DrawSpace(Canvas canvas) {
        Rect rect = new Rect();
        for (int i = 0; i < indexSpaces.length; ++i) {
            int posX = START_GRID + (SPACE_LENGTH * indexSpaces[i].getCol());
            int posY = START_GRID + (SPACE_LENGTH * indexSpaces[i].getRow());
            int tag = indexSpaces[i].getSpace().getTag();

            // 태그 = bitmap 인덱스
            switch (tag) {
                case SpaceType.WALL:
                    paint.setColor(Color.LTGRAY);
                    break;

                case SpaceType.SAVE:
                    paint.setColor(Color.CYAN);
                    break;

                case SpaceType.OBSTACLE:
                    paint.setColor(Color.RED);
                    break;

                default:
                    continue;
            }
            rect.set(posX, posY, posX + SPACE_LENGTH, posY + SPACE_LENGTH);
            canvas.drawRect(rect, paint);
            canvas.drawBitmap(bitmap[tag - 1], posX, posY, null);
        }
    }

    public void DrawClick(Canvas canvas) {
        Rect rect = new Rect();
        int clickX = START_GRID + (SPACE_LENGTH * clickCol);
        int clickY = START_GRID + (SPACE_LENGTH * clickRow);
        paint.setColor(Color.YELLOW);
        rect.set(clickX, clickY, clickX + SPACE_LENGTH, clickY + SPACE_LENGTH);
        canvas.drawRect(rect, paint);
    }

    public void DrawRobot(Canvas canvas) {
        int size = workers.size();
        int index_bitmap, posX, posY;
        // 맵에 로봇이 존재
        if (size >= 1) {
            for (int i = 0; i < size; ++i) {
                // 로봇
                if (workers.get(i).getName() == "Normal")
                    index_bitmap = Set_OrangeRobot_Animation(i);
                else if (workers.get(i).getName() == "Strong")
                    index_bitmap = Set_RedRobot_Animation(i);
                else
                    index_bitmap = Set_BlueRobot_Animation(i);

                canvas.drawBitmap(bitmap[index_bitmap], workers.get(i).getX() + 5, workers.get(i).getY() + 5, null);
                // 사망 여부
                if (workers.get(i).getEnergy() <= 0) {
                    canvas.drawBitmap(bitmap[31], workers.get(i).getX(), workers.get(i).getY(), null);
                }

                // 번호 매기기
                paint.setColor(Color.BLUE);
                paint.setTextSize(SPACE_LENGTH / 3);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));        // 텍스트 굵기
                posX = workers.get(i).getX();
                posY = workers.get(i).getY() + 30;
                canvas.drawText(String.valueOf(i + 1), posX, posY, paint);

                // 들고 있는 물건의 수
                paint.setColor(Color.GREEN);
                canvas.drawText(String.valueOf(workers.get(i).getNumHoldObject()), posX + 30 * 2, posY, paint);
            }
        }
    }

    public void DrawObject(Canvas canvas) {
        for (int i = 0; i < objects.size(); ++i) {
            int x = START_GRID + SPACE_LENGTH * objects.get(i).getCol();
            int y = START_GRID + (SPACE_LENGTH * (objects.get(i).getRow() + 1)) - (SPACE_LENGTH / 3);

            // 물건 여러 개 그리기
            int obj_size = objects.get(i).getNum();
            canvas.drawBitmap(bitmap[objecttype + 27], x, y, null);
            paint.setTextSize(SPACE_LENGTH / 3);
            paint.setColor(Color.BLACK);
            canvas.drawText(String.valueOf(obj_size), x + SPACE_LENGTH / 3, y + SPACE_LENGTH / 4, paint);
            /*if (obj_size >= 5)
            {
                canvas.drawBitmap(bitmap[objecttype + 28], x, y, null);
                paint.setTextSize(SPACE_LENGTH / 3);
                paint.setColor(Color.BLACK);
                canvas.drawText(String.valueOf(obj_size), x + SPACE_LENGTH / 3, y + SPACE_LENGTH / 4, paint);
            }
            else
                {
                for (int j = 0; j < objects.get(i).getNum(); ++j) {
                    int gapObject = 22;
                    // 물건 겹쳐 그리기
                    canvas.drawBitmap(bitmap[objecttype + 28], x + (gapObject * j), y, null);       // type + 26번째 객체부터 object 그림
                }
            }*/
        }
    }

    public void DrawGrid(Canvas canvas) {
        paint.setColor(Color.BLACK);
        for (int i = 0; i <= row; ++i)
            canvas.drawLine(START_GRID, START_GRID + (SPACE_LENGTH * i), START_GRID + (SPACE_LENGTH * col), START_GRID + (SPACE_LENGTH * i), paint);
        for (int i = 0; i <= col; ++i)
            canvas.drawLine(START_GRID + SPACE_LENGTH * i, START_GRID, START_GRID + SPACE_LENGTH * i, START_GRID + (SPACE_LENGTH * row), paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // paint 객체 초기화
        paint.setStrokeWidth(5.0f);

        // 맵 지형
        DrawSpace(canvas);

        // 격자선
        DrawGrid(canvas);

        // 클릭된 칸이 있는가
        if (clickedRobot) DrawClick(canvas);

        // 로봇
        DrawRobot(canvas);

        // 물건
        DrawObject(canvas);
    }

    public int Set_RedRobot_Animation(int index) {
        switch (workers.get(index).getNumberAnimation()) {
            case NumberRobotAnimation.UP1:
                return 3;
            case NumberRobotAnimation.UP2:
                return 4;
            case NumberRobotAnimation.DOWN1:
                return 5;
            case NumberRobotAnimation.DOWN2:
                return 6;
            case NumberRobotAnimation.LEFT1:
                return 7;
            case NumberRobotAnimation.LEFT2:
                return 8;
            case NumberRobotAnimation.RIGHT1:
                return 9;
            case NumberRobotAnimation.RIGHT2:
                return 10;
        }
        // 오류라는 의미
        return -1;
    }

    public int Set_OrangeRobot_Animation(int index) {

        switch (workers.get(index).getNumberAnimation()) {
            case NumberRobotAnimation.UP1:
                return 11;
            case NumberRobotAnimation.UP2:
                return 12;
            case NumberRobotAnimation.DOWN1:
                return 13;
            case NumberRobotAnimation.DOWN2:
                return 14;
            case NumberRobotAnimation.LEFT1:
                return 15;
            case NumberRobotAnimation.LEFT2:
                return 16;
            case NumberRobotAnimation.RIGHT1:
                return 17;
            case NumberRobotAnimation.RIGHT2:
                return 18;
        }
        // 오류라는 의미
        return -1;
    }

    public int Set_BlueRobot_Animation(int index) {

        switch (workers.get(index).getNumberAnimation()) {
            case NumberRobotAnimation.UP1:
                return 19;
            case NumberRobotAnimation.UP2:
                return 20;
            case NumberRobotAnimation.DOWN1:
                return 21;
            case NumberRobotAnimation.DOWN2:
                return 22;
            case NumberRobotAnimation.LEFT1:
                return 23;
            case NumberRobotAnimation.LEFT2:
                return 24;
            case NumberRobotAnimation.RIGHT1:
                return 25;
            case NumberRobotAnimation.RIGHT2:
                return 26;
        }
        // 오류라는 의미
        return -1;
    }

    public int getSPACE_LENGTH() {
        return SPACE_LENGTH;
    }

    public int getSTART_GRID() {
        return START_GRID;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setClickedRobot(boolean clickedRobot) {
        this.clickedRobot = clickedRobot;
    }

    public boolean isClickedRobot() {
        return clickedRobot;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setIndexSpaces(IndexSpace[] indexSpaces) {
        this.indexSpaces = indexSpaces;
    }

    public IndexSpace[] getIndexSpaces() {
        return indexSpaces;
    }

    public LinkedList<ArrangeObject> getObjects() {
        return objects;
    }
}
