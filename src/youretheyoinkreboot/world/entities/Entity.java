package youretheyoinkreboot.world.entities;

import java.awt.Rectangle;
import youretheyoinkreboot.core.gfx.Screen;

/**
 *
 * @author josh
 */
public abstract class Entity {
    protected int x, y;
    protected float vx, vy;
    
    protected int width, height;
    
    protected Rectangle bounds = new Rectangle(x, y, width, height);
    
    protected boolean active = true;
    
    protected int maxHitPoints;
    protected int hp;
    
    public Entity(int x, int y, int width, int height, int maxHitPoints) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxHitPoints = maxHitPoints;
        
        hp = maxHitPoints;
    }
    
    public void superTick() {
        bounds.x = x;
        bounds.y = y;
        tick();
    }
    
    public void superRender(Screen s) {
        
        render(s);
    }
    
    protected abstract void tick();
    protected abstract void render(Screen s);
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public Rectangle getBounds() {
        return bounds;
    }
    
    public boolean intersects(Entity e) {
        return bounds.intersects(e.getBounds());
    }
    
    public int getHP() {
        return hp;
    }
    
    public int getMaxHP() {
        return maxHitPoints;
    }
}
