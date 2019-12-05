package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Start extends JPanel {

    private static JFrame frame = new JFrame();
    private static Graphics2D g2;

    public Start() {
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), SPACE);
        this.getActionMap().put(SPACE, space);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(1000, 800);
        frame.setVisible(true);
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(500, 900));
        frame.add(this);
        frame.pack();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        try {
            drawGame(g2);
            drawTitle(g2);
        } catch (Exception e) {
            System.out.println("Error opening file");
        }
    }

    public void drawGame(Graphics2D g2) throws FontFormatException, IOException {

        g2.drawImage((new ImageIcon("titlebg.png")).getImage(), 0, 0, frame);
        g2.setColor(new Color(255, 0, 255));

        AttributedString text = new AttributedString("Press SPACE to start");
        File fontFile = new File("PressStart2P.TTF");
        Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(24f);

        text.addAttribute(TextAttribute.FONT, font);
        text.addAttribute(TextAttribute.FOREGROUND, Color.red);
        g2.drawString(text.getIterator(), 5, 750);

    }

    public void drawTitle(Graphics2D g2) {
        g2.drawImage((new ImageIcon("Copyright.png")).getImage(), 10, 100, frame);
        g2.drawImage((new ImageIcon("Strike.png")).getImage(), 100, 350, frame);
    }

    private static final String SPACE = "Space";
    private Action space = new AbstractAction(SPACE) {
        @Override
        public void actionPerformed(ActionEvent e) {
            Screen game = new Screen();
            frame.setVisible(false);
        }
    };
}
