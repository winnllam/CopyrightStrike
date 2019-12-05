package main;

import java.util.Random;
import javax.swing.ImageIcon;

public class Enemy extends GameObject {

    private double pointValue;
    private Player player;

    public Enemy(int health, int movementSpeed, boolean isAlive, ImageIcon pic, int x, int y, double pointValue, Player player) {
        super(health, movementSpeed, isAlive, pic, x, y);
        this.pointValue = pointValue;
        this.player = player;
    }

    public Bullets shoot() {
        return new Bullets(1, -5, true, new ImageIcon("pew.png"), (this.returnPositionX() + ((this.getPic().getIconWidth()) / 4)), this.returnPositionY(), 1);
    }

    public void movement() { //positive left, negative right. Stay within screen.
        Random ran = new Random();
        this.y = this.y + movementSpeed;

        if (ran.nextInt(2) == 0 && (this.x + movementSpeed) < 500) {
            this.x = this.x + movementSpeed;
        } else if ((this.x - movementSpeed) > 0) {
            this.x = this.x - movementSpeed;
        }

    }

    public void hit(int damage) {
        health = health - damage;
        if (health <= 0) {
            isAlive = false;
            //score increase by pointValue on screen
            //death stuff
        }
    }

    public PowerUp dead() {
        Random ran = new Random();
        if (ran.nextInt(2) == 0) { //50/50 chance of power up
            if (ran.nextInt(10) == 0) { // 1 in 10 power up (odds) of bomb, first priority
                return new PowerUp(1, -1, true, new ImageIcon("bomb.png"), this.returnPositionX(), this.returnPositionY(), "Bomb", player);
            } else if (ran.nextInt(5) == 0) { //1 in 5 power up (odds) of weapon upgrade, second priority
                return new PowerUp(1, -1, true, new ImageIcon("power.png"), this.returnPositionX(), this.returnPositionY(), "WeaponBuff", player);
            } else { //if not a bomb or a weapon upgrade, will be a life
                return new PowerUp(1, -1, true, new ImageIcon("life.png"), this.returnPositionX(), this.returnPositionY(), "Life", player);
            }
        }
        return null; //if no pUp is given
    }
    
    public CombustEffect boom(){
        return new CombustEffect(90, 0, true, new ImageIcon("combust3.png"),this.x, this.y);
    }
    
    public double getPointValue() {
        return pointValue;
    }
}
