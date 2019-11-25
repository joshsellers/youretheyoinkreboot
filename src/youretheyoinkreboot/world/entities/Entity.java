package youretheyoinkreboot.world.entities;

import java.awt.Rectangle;
import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.world.World;

/**
 *
 * @author josh
 */
public abstract class Entity {
    
    protected World w;
    
    protected int x, y;
    protected float vx, vy;
    
    protected int width, height;
    
    protected Rectangle bounds = new Rectangle(x, y, width, height);
    
    protected boolean active = true;
    
    protected int maxHitPoints;
    protected int hp;
    
    protected boolean collides = false;
    
    protected boolean show = true;
    
    protected int maxSpeed;
    
    protected boolean moving;
    
    public Entity(int x, int y, int width, int height, int maxHitPoints, World w) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxHitPoints = maxHitPoints;
        this.w = w;
        
        hp = maxHitPoints;
    }
    
    public void superTick() {
        tick();
        move();
        bounds.x = x;
        bounds.y = y;
    }
    
    public void superRender(Screen s) {
        if (isVisible()) {
            render(s);
        }
    }
    
    protected abstract void tick();
    protected abstract void render(Screen s);
    
    private void move() {
        int oldx = x;
        int oldy = y;
        x += vx;
        y += vy;
        
        for (Entity e : w.getEntities()) {
            if (e != this && e.active && this.intersects(e)) {
                x = oldx;
                y = oldy;
                vx = (-vx / 2);
                vy = (-vy / 2);
            }
        }
        
        moving = Math.abs(vx) > 0 || Math.abs(vy) > 0;
    }
    
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
    
    public void show() {
        show = true;
    }
    
    public void hide() {
        show = false;
    }
    
    public boolean isVisible() {
        return show;
    }
    
    public World getWorld() {
        return w;
    }
    
    public boolean collides() {
        return collides;
    }
    
    public void enableCollision() {
        collides = true;
    }
    
    public void noClip() {
        collides = false;
    }
    
    public boolean isMoving() {
        return moving;
    }
}
