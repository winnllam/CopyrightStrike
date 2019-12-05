package main;

import javax.swing.ImageIcon;

public class PowerUp extends GameObject{
    
    private Player target;
    private String type;
    
    public PowerUp(int health, int movementSpeed, boolean isAlive, ImageIcon pic, int x, int y, String type, Player target) {
        super(health, movementSpeed, isAlive, pic, x, y);
        this.target = target;
        this.type = type;
    }
    
    public void movement() {
        this.y = this.y - movementSpeed;
    }
    
    public void powerUp() {
        if (type == "Bomb") {
            target.bombUp();
        } else if (type == "Life"){
            target.oneUp();
        } else {    
            target.calibrated();
        }
    }
}
