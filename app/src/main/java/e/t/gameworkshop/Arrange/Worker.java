package e.t.gameworkshop.Arrange;

import e.t.gameworkshop.Robot;

public class Worker extends Robot {

    private int strength;
    private int speed;
    private int numHoldObject = 0;

    public Worker() {}

    public Worker(String name, int energy, int row, int col, int x, int y, int cost, int strength, int speed){
        super(name, energy, row, col, x, y, cost);
        this.strength = strength;
        this.speed = speed;
    }

    public int getStrength() {
        return strength;
    }

    public int getSpeed() {
        return speed;
    }

    public int getNumHoldObject() {
        return numHoldObject;
    }

    public void setNumHoldObject(int numHoldObject) {
        this.numHoldObject = numHoldObject;
    }
}
