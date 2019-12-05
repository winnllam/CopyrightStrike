/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javax.swing.ImageIcon;

public class CombustEffect extends GameObject {
    
    public CombustEffect(int health, int movementSpeed, boolean isAlive, ImageIcon pic, int x, int y) {
        super(health, movementSpeed, isAlive, pic, x, y);
    }
    
    public boolean disappearTime() {
        health--;
        if (health == 0) {
            return true;
        } else {
            return false;
        }
    }
    
}
