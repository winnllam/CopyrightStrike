package main;

import javax.swing.ImageIcon;

public class Bullets  extends GameObject {
    private int size;
    private int damage;
    
    public Bullets(int health, int movementSpeed, boolean isAlive, ImageIcon pic, int x, int y, int damage){
        super(health, movementSpeed, isAlive, pic, x, y);
        this.damage = damage;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public int getSize() {
        return size;
    }
    
    public void movement() {
        this.y = this.y - movementSpeed;
    }
}
