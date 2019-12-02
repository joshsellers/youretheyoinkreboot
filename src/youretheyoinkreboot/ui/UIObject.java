package youretheyoinkreboot.ui;

import java.awt.Graphics;

/**
 *
 * @author josh
 */
public abstract class UIObject {
    public final byte ID;
    
    protected int x, y;
    protected int width, height;
    
    protected boolean active = true;
    protected boolean show = true;
    
    public UIObject(byte ID, int x, int y) {
        this.ID = ID;
        this.x = x;
        this.y = y;
    }
    
    protected abstract void tick();
    protected abstract void draw(Graphics g);
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getWidth() {
        return width;
    }
    
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void show() {
        show = true;
    }
    
    public void hide() {
        show = false;
    }
    
    public void setActive(boolean a) {
        this.active = a;
    }
    
    public boolean isVisible() {
        return show;
    }
    
    public boolean isActive() {
        return active;
    }
}
