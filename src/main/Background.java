package main;

import javax.swing.ImageIcon;

public class Background {

    protected int y;
    protected ImageIcon pic;

    public Background(int y, ImageIcon pic) {
        this.y = y;
        this.pic = pic;
    }

    public int scrollY() {
        if (y == 900) {
            return y = -899;
        } else {
            return y = y + 1;
        }
    }

    public ImageIcon getPic() {
        return pic;
    }
}
