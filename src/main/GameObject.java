package main;

import javax.swing.ImageIcon;

public class GameObject {

    protected int health;
    protected int movementSpeed;
    protected boolean isAlive;
    protected ImageIcon pic;
    protected int x;
    protected int y;

    public GameObject(int health, int movementSpeed, boolean isAlive, ImageIcon pic, int x, int y) {
        this.health = health;
        this.movementSpeed = movementSpeed;
        this.isAlive = isAlive;
        this.pic = pic;
        this.x = x;
        this.y = y;
    }

    public ImageIcon getPic() {
        return pic;
    }

    public int returnPositionX() {
        return x;
    }

    public int returnPositionY() {
        return y;
    }
    
    public int getHealth() { //returns its current health for convienience
        return health;
    }
    
    public boolean isDead() {
        return isAlive;
    }
}
