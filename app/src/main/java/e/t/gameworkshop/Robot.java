package e.t.gameworkshop;

public class Robot {

    private static int numOfRobot = 0;
    private String name;
    private int energy;
    private int x, y;
    private int cost;
    private int numAct = 0;
    private int row, col;
    private int numberAnimation = 5;
    private int indexProgram = -1;
    private boolean selected = false;

    public Robot() {}

    public Robot(String name, int energy, int row, int col, int x, int y, int cost) {
        this.name = name;
        this.energy = energy;
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.row = row;
        this.col = col;
    }

    public static int getNumOfRobot() {
        return numOfRobot;
    }

    public static void setNumOfRobot(int numOfRobot) {
        Robot.numOfRobot = numOfRobot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getNumAct() {
        return numAct;
    }

    public void setNumAct(int numAct) {
        this.numAct = numAct;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setIndexProgram(int indexProgram) {
        this.indexProgram = indexProgram;
    }

    public int getIndexProgram() {
        return indexProgram;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getNumberAnimation() {
        return numberAnimation;
    }

    public void setNumberAnimation(int numberAnimation) {
        this.numberAnimation = numberAnimation;
    }
}
