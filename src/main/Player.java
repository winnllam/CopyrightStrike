package main;   

import javax.swing.ImageIcon;

public class Player extends GameObject {

    protected int bombAmount;
    protected int weaponDamage;
    protected int lives;

    public Player(int health, int movementSpeed, boolean isAlive, ImageIcon pic, int x, int y, int bombAmount, int weaponDamage, int lives) {
        super(health, movementSpeed, isAlive, pic, x, y);
        this.bombAmount = bombAmount;
        this.weaponDamage = weaponDamage;
        this.lives = lives;
    }

    public Bullets shoot() {
        return new Bullets(1, 5, true, new ImageIcon("pew.png"), (this.returnPositionX() + ((this.getPic().getIconWidth()) / 2)), 700, weaponDamage);
    }

    public void right() { //must stay within screen
        if (x + movementSpeed < 450) {
            x = x + movementSpeed;
        }
    }

    public void left() { //must stay within screen
        if (x - movementSpeed > -55) {
            x = x - movementSpeed;
        }
    }
    
    public void setX(int xCord) {
        x = xCord;
    }
    
    public void setY(int yCord) {
        y = yCord;
    }

    public int getBombs() {
        return bombAmount;
    }

    public int getLives() {
        return lives;
    }

    public Bullets bombsAway() {
        if (bombAmount > 0) {
            bombAmount = bombAmount - 1;
            return new Bullets(1, 1, true, new ImageIcon("bomb.png"), this.returnPositionX(), 700, 10 * weaponDamage);
        } else {
            return null;
        }
    }

    public void calibrated() { //increases weapon damage, a reference to Mass Effect (because it made since to Elizabeth)
        weaponDamage = weaponDamage + 10;
    }

    public void bombUp() {
        bombAmount = bombAmount + 3;
    }
    
    public void setBombs(int bombs) {
        bombAmount = bombs;
    }

    public void oneUp() {
        lives++;
    }
    
    public void setLives(int life) {
        lives = life;
    }
    
    public void setDamage(int damage) {
        weaponDamage = damage;
    }

    public void hit() {
        if (lives - 1 != -1) {
            lives--;
        } else {
            this.isAlive = false;
        }
    }

}
