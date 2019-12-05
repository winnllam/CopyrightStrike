package main;

import java.awt.Color;
import java.util.Random;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;

public class Screen extends JPanel implements ActionListener, Runnable {

    private static JFrame frame = new JFrame();
    private static Player player = new Player(100, 25, true, new ImageIcon("plane.png"), 200, 750, 3, 1, 50);
    private static Graphics2D g2;
    private boolean run = false;
    private static JLabel plane = new JLabel(new ImageIcon("plane.png"));
    private ArrayList<Bullets> bullet = new ArrayList<Bullets>();
    private ArrayList<Bullets> bulletFromEnemy = new ArrayList<Bullets>();
    private ArrayList<Enemy> aliveEnemy = new ArrayList<Enemy>();
    private ArrayList<PowerUp> pUp = new ArrayList<PowerUp>();
    private ArrayList<CombustEffect> explosion = new ArrayList<CombustEffect>();
    private Timer spawnTime = new Timer(2000, new Spawn());
    private static Background backdrop1 = new Background(0, new ImageIcon("background.jpg"));
    private static Background backdrop2 = new Background(-900, new ImageIcon("background.jpg"));
    private int enemyFire = 2000; // changes after each boss, decreasing fire rate delay
    private static double score = 0;
    private static String highscore = "";
    private static int bossCounter = 0;
    private static int difficultyMultiplier = 1;
    private static int enemyKilled = 0;
    private static File fontFile = new File("PressStart2P.TTF");

    private static final String LEFT = "Left";
    private Action left = new AbstractAction(LEFT) {
        @Override
        public void actionPerformed(ActionEvent e) {
            player.left();
        }
    };

    private static final String RIGHT = "Right";
    private Action right = new AbstractAction(RIGHT) {
        @Override
        public void actionPerformed(ActionEvent e) {
            player.right();
        }
    };

    private static final String SPACE = "Space";
    private Action space = new AbstractAction(SPACE) {
        @Override
        public void actionPerformed(ActionEvent e) {
            bullet.add(player.shoot());
            try {
                InputStream in = new FileInputStream("LaserBlasts.wav");
  //              AudioStream audioStream = new AudioStream(in);
  //              AudioPlayer.player.start(audioStream);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    };

    private static final String B = "B";
    private Action bombIt = new AbstractAction(B) {
        @Override
        public void actionPerformed(ActionEvent e) {
            Bullets bomb = player.bombsAway();
            if (bomb != null) {
                bullet.add(bomb);
            }
        }
    };

    private static final String R = "R";
    private Action restart = new AbstractAction(R) { //resets everything to default values
        @Override
        public void actionPerformed(ActionEvent e) {
            player.setLives(3);
            player.setBombs(3);
            player.setDamage(1);
            enemyFire = 2000;
            score = 0;
            difficultyMultiplier = 1;
            bossCounter = 0;
            enemyKilled = 0;
            spawnTime.setDelay(2000);
            aliveEnemy.clear();
            bullet.clear();
            bulletFromEnemy.clear();
            pUp.clear();
            explosion.clear();
            player.setX(200);
            player.setY(750);
            spawnTime.start();
            if (!run) { //if it was gameover that led to this restart, it resets the thread.
                run = true;
                start();
            }
        }
    };

    //initialized the key presses (keybindings to avoid lag)
    public Screen() {
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), LEFT);
        this.getActionMap().put(LEFT, left);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), RIGHT);
        this.getActionMap().put(RIGHT, right);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), SPACE);
        this.getActionMap().put(SPACE, space);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0), B);
        this.getActionMap().put(B, bombIt);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), R);
        this.getActionMap().put(R, restart);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(1000, 800);
        frame.setVisible(true);
        this.setPreferredSize(new Dimension(500, 900));
        spawnTime.start();
        frame.add(this);
        frame.pack();
        start();
    }

    public void start() {
        Thread thread = new Thread(this);
        run = true;
        thread.start();
    }

    //stops game for a gameover
    public void stop() throws FontFormatException, IOException {
        findHighScore();
        run = false;

        aliveEnemy.clear();
        bullet.clear();
        bulletFromEnemy.clear();
        pUp.clear();
        explosion.clear();

        Font font1 = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(30f);
        AttributedString gameOver = new AttributedString("GAME OVER");
        gameOver.addAttribute(TextAttribute.FONT, font1);
        gameOver.addAttribute(TextAttribute.FOREGROUND, Color.red);
        g2.drawString(gameOver.getIterator(), 120, 300);

        Font font2 = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(20f);
        AttributedString restart = new AttributedString("Press R to restart");
        restart.addAttribute(TextAttribute.FONT, font2);
        restart.addAttribute(TextAttribute.FOREGROUND, Color.black);
        g2.drawString(restart.getIterator(), 80, 500);
    }

    public void run() {
        while (run) {
            repaint();
            if (enemyKilled > ((bossCounter + 1) * 10) && bossCounter < 5) {
                aliveEnemy.add(spawnBoss(bossCounter));
                bossCounter++;
                enemyKilled = 0;
                if (enemyFire > 50) { //or eventually the delay goes absolutely balls to the wall insane
                    enemyFire = enemyFire - 100;
                }
            } else if (bossCounter == 5) {
                bossCounter = 0;
                difficultyMultiplier++;
                spawnTime.setDelay(2000 - (difficultyMultiplier * 200));
            }
            try {
                Thread.sleep(3);
            } catch (Exception e) {
            }
            if ("".equals(highscore)) {
                try {  //initialize high score
                    highscore = this.getHighScore();
                } catch (IOException ex) {
                    Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        try {
            drawBackground(g2);
            drawPlane(g2);
            drawBullet(g2);
            drawPowerUp(g2);
            drawEnemyBullet(g2);
            drawEnemy(g2);
            drawDeathEffect(g2);
            drawText(g2);
        } catch (Exception e) {
            System.out.println("Caught: " + e);
        }
    }

    public void findHighScore() {
        double hscore = Double.parseDouble(highscore);
        if (score > hscore) {
            highscore = Double.toString(score);
            File fileWithHS = new File("highscore.txt");
            if (!fileWithHS.exists()) {
                try {
                    fileWithHS.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            FileWriter file = null;
            BufferedWriter buffFile = null;
            try {
                file = new FileWriter(fileWithHS);
                buffFile = new BufferedWriter(file);
                buffFile.write(this.highscore);
            } catch (IOException ex) {
                Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (buffFile != null) {
                    try {
                        buffFile.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public void drawPlane(Graphics2D g2) {
        g2.drawImage(player.getPic().getImage(), player.returnPositionX(), player.returnPositionY(), frame);
    }

    public void drawBullet(Graphics2D g2) {
        for (Iterator<Bullets> iterator = bullet.iterator(); iterator.hasNext();) { //prevents an exception
            Bullets value = iterator.next();
            if (value.returnPositionY() > 0) {
                g2.drawImage(value.getPic().getImage(), value.returnPositionX(), value.returnPositionY(), frame);
                value.movement();
            } else {
                iterator.remove();
            }
        }
    }

    public void drawDeathEffect(Graphics2D g2) {
        if (explosion.size() > 0) {
            for (Iterator<CombustEffect> iterator = explosion.iterator(); iterator.hasNext();) { //prevents an exception
                CombustEffect value = iterator.next();
                if (!value.disappearTime()) {
                    g2.drawImage(value.getPic().getImage(), value.returnPositionX(), value.returnPositionY(), frame);
                } else {
                    iterator.remove();
                }
            }
        }
    }

    public void drawEnemyBullet(Graphics2D g2) throws FontFormatException, IOException {
        for (Iterator<Bullets> iterator = bulletFromEnemy.iterator(); iterator.hasNext();) { //prevents an exception
            Bullets bullet = iterator.next();
            if (collision(bullet.returnPositionX(), bullet.returnPositionY(), bullet.getPic().getIconWidth(), bullet.getPic().getIconHeight(), player.returnPositionX(), player.returnPositionY(), player.getPic().getIconWidth(), player.getPic().getIconHeight())) {
                player.hit();
                iterator.remove();
                if ((player.getLives() - 1) == -1) {
                    stop();
                }
            } else if (bullet.returnPositionY() < 800) {
                g2.drawImage(bullet.getPic().getImage(), bullet.returnPositionX(), bullet.returnPositionY(), frame);
                bullet.movement();
            } else {
                iterator.remove();
            }
        }
    }

    public void drawPowerUp(Graphics2D g2) throws FileNotFoundException, IOException {
        for (Iterator<PowerUp> iterator = pUp.iterator(); iterator.hasNext();) { //prevents an exception
            PowerUp up = iterator.next();
            if (collision(up.returnPositionX(), up.returnPositionY(), up.getPic().getIconWidth(), up.getPic().getIconHeight(), player.returnPositionX(), player.returnPositionY(), player.getPic().getIconWidth(), player.getPic().getIconHeight())) {
                up.powerUp();
                iterator.remove();
                if (up.getPic().toString().equals("life.png")) {
                    InputStream in = new FileInputStream("1UP.wav");
        //            AudioStream audioStream = new AudioStream(in);
        //            AudioPlayer.player.start(audioStream);
                } else { // for POW and bombs
                    InputStream in = new FileInputStream("POW.wav");
        //            AudioStream audioStream = new AudioStream(in);
        //            AudioPlayer.player.start(audioStream);
                }
            } else if (up.returnPositionY() > 0) {
                g2.drawImage(up.getPic().getImage(), up.returnPositionX(), up.returnPositionY(), frame);
                up.movement();
            } else {
                iterator.remove();
            }
        }
    }

    public String getHighScore() throws IOException {
        FileReader file = null;
        BufferedReader buff = null;
        try {
            file = new FileReader("highscore.txt");// change to .dat later
            buff = new BufferedReader(file);
            return buff.readLine();
        } catch (FileNotFoundException ex) {
            return "0";
        } finally {
            try {
                if (buff != null) {
                    buff.close();
                }
            } catch (NullPointerException e) {
            }
        }
    }

    public void drawText(Graphics2D g2) throws FontFormatException, IOException { // add bomb count!!!
        Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(15f);
        String points = String.format("%07.0f", score);
        String highScorePoints = String.format("%07.0f", Double.parseDouble(highscore));

        AttributedString pointsText = new AttributedString(points);
        pointsText.addAttribute(TextAttribute.FONT, font);
        pointsText.addAttribute(TextAttribute.FOREGROUND, Color.white);
        g2.drawString(pointsText.getIterator(), 50, 45);

        AttributedString lifeText = new AttributedString(player.getLives() + "UP");
        lifeText.addAttribute(TextAttribute.FONT, font);
        lifeText.addAttribute(TextAttribute.FOREGROUND, Color.white);
        g2.drawString(lifeText.getIterator(), 10, 25);

        AttributedString bombsText = new AttributedString("Bombs:" + player.getBombs());
        bombsText.addAttribute(TextAttribute.FONT, font);
        bombsText.addAttribute(TextAttribute.FOREGROUND, Color.white);
        g2.drawString(bombsText.getIterator(), 385, 25);

        AttributedString highScoreText = new AttributedString("HIGH SCORE");
        highScoreText.addAttribute(TextAttribute.FONT, font);
        highScoreText.addAttribute(TextAttribute.FOREGROUND, Color.white);
        g2.drawString(highScoreText.getIterator(), 190, 25);

        AttributedString highScoreNumText = new AttributedString(highScorePoints);
        highScoreNumText.addAttribute(TextAttribute.FONT, font);
        highScoreNumText.addAttribute(TextAttribute.FOREGROUND, Color.white);
        g2.drawString(highScoreNumText.getIterator(), 210, 45);
    }

    public void drawBackground(Graphics2D g2) throws IOException {
        g2.drawImage(backdrop1.getPic().getImage(), 0, backdrop1.scrollY(), frame); // change position y
        g2.drawImage(backdrop2.getPic().getImage(), 0, backdrop2.scrollY(), frame);
    }

    public boolean collision(int x1, int y1, int width1, int height1, int x2, int y2, int width2, int height2) {
        Rectangle item1 = new Rectangle(x1, y1, width1, height1);
        Rectangle item2 = new Rectangle(x2, y2, width2, height2);
        if (item1.intersects(item2)) {
            return true;
        } else {
            return false;
        }
    }

    public void drawEnemy(Graphics2D g2) throws FileNotFoundException, IOException {
        Random ran = new Random(); //makes movement and bullet firing sporatic (but since this method is called so frequently the odds of it happening increase drastically!
        if (aliveEnemy.size() > 0) {
            for (Iterator<Enemy> enIterator = aliveEnemy.iterator(); enIterator.hasNext();) { //prevents an exception
                Enemy current = enIterator.next();
                if (bullet.size() != 0) {
                    for (Iterator<Bullets> iterator = bullet.iterator(); iterator.hasNext();) { //prevents an exception
                        Bullets bullet = iterator.next();
                        if (collision(bullet.returnPositionX(), bullet.returnPositionY(), bullet.getPic().getIconWidth(), bullet.getPic().getIconHeight(), current.returnPositionX(), current.returnPositionY(), current.getPic().getIconWidth(), current.getPic().getIconHeight())) {
                            current.hit(bullet.getDamage());
                            if (bullet.getPic().toString().equals("bomb.png")) { // play sound when bomb hits
                                try {
                                    InputStream in = new FileInputStream("Bomb.wav");
    //                                AudioStream audioStream = new AudioStream(in);
    //                                AudioPlayer.player.start(audioStream);
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            iterator.remove();
                        }
                    }
                }
                if (collision(player.returnPositionX(), player.returnPositionY(), player.getPic().getIconWidth(), player.getPic().getIconHeight(), current.returnPositionX(), current.returnPositionY(), current.getPic().getIconWidth(), current.getPic().getIconHeight())) {
                    enIterator.remove();
                    player.hit();
                }
                if (current.returnPositionY() < 900 && current.isDead()) { // when it gets to the end, points are earned??? ?? FIX!!!
                    g2.drawImage(current.getPic().getImage(), current.returnPositionX(), current.returnPositionY(), frame);
                    if (ran.nextInt(30) == 0) {
                        current.movement();
                    }
                    if (ran.nextInt(enemyFire) == 0) {
                        InputStream in = new FileInputStream("LaserBlasts.wav");
   //                     AudioStream audioStream = new AudioStream(in);
   //                     AudioPlayer.player.start(audioStream);
                        bulletFromEnemy.add(current.shoot());
                    }
                } else {
                    PowerUp pp = current.dead();
                    if (current.getHealth() <= 0) {
                        enemyKilled++;
                        score += current.getPointValue(); // get the point value
                        InputStream in = new FileInputStream("Death.wav");
    //                    AudioStream audioStream = new AudioStream(in);
    //                   AudioPlayer.player.start(audioStream);
                    }
                    if (pp != null) {
                        pUp.add(pp);
                    }
                    explosion.add(current.boom());
                    enIterator.remove();
                }
            }
        }
    }

    private Enemy spawnBoss(int which) {
        BossEnum en = BossEnum.values()[which]; //takes a random enum value from an enumeration of enemies
        return (new Enemy(en.getHealth() * difficultyMultiplier, en.getMovementSpeed(), true, new ImageIcon(en.getPictureName()), 0, 0, en.getPointValue() + 20 * difficultyMultiplier, player));
    }

    private class scrollBackground implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            try {
                drawBackground(g2);
            } catch (IOException ex) {
                Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class Spawn implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            Random ran = new Random();
            if (aliveEnemy.size() < 10) {
                EnemyEnum en = EnemyEnum.values()[ran.nextInt(15)]; //takes a random enum value from an enumeration of enemies
                aliveEnemy.add(new Enemy(en.getHealth() * difficultyMultiplier, en.getMovementSpeed(), true, new ImageIcon(en.getPictureName()), ran.nextInt(450), 0, en.getPointValue() * difficultyMultiplier, player));
            }
        }
    }
}
