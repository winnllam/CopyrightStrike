package main;

public enum BossEnum {
    FIRST(500, 1, "boss1.png", 300), SECOND(1000, 2, "boss2.png", 500), THIRD(1500, 15, "boss3.png", 700), FOURTH(2000, 2, "boss4.gif", 900),
    FINAL(5000, 2, "finalBoss.png", 10000);
    
    private final int health;
    private final int movementSpeed;
    private final String name;
    private final double pointValue;
    
    BossEnum (int health, int movementSpeed, String name, double pointValue) {
        this.health = health;
        this.movementSpeed = movementSpeed;
        this.name = name;
        this.pointValue = pointValue;
    }

    public int getHealth() {
        return health;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public String getPictureName() {
        return name;
    }

    public double getPointValue() {
        return pointValue;
    }
}
