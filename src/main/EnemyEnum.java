package main;

public enum EnemyEnum { //in brackets int health, int movementSpeed, pic name, int weaponDamage
    SANDWICH(1, 5, "enemy1.png", 10), WHALE(15, 1, "enemy2.png", 15), LEFT_EYE(1, 20, "enemy3.png", 10), RIGHT_EYE(5, 30, "enemy4.png", 100), BOX(20, 2, "enemy5.png", 100),
    TRIANGLE(15, 3, "enemy6.png", 15), SAUCER(10, 20, "enemy7.png", 15), BALLOON(50, 1, "enemy8.png", 100), OTHER_SHIP(15, 3, "enemy9.png", 15), DART(5, 45, "enemy10.png", 30),
    APPEARANCE_VERSUS_REALITY(30, 50, "enemy11.png", 200), GREEN_BOI(10, 2, "enemy12.png", 5), PURPLE_RAIN(20, 8, "enemy13.png", 100), AIRFORCE_TWO(15, 5, "enemy14.png", 15), HELICOPTER(40, 25, "enemy15.png", 100);

    private final int health;
    private final int movementSpeed;
    private final String name;
    private final double pointValue;

    EnemyEnum(int health, int movementSpeed, String name, double pointValue) {
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
