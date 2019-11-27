package youretheyoinkreboot.world.entities;

import com.amp.mathem.Statc;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;
import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.world.World;

/**
 *
 * @author josh
 */
public abstract class Entity {
    
    protected World w;
    
    protected int x, y;
    protected double vx, vy;
    
    protected int width, height;
    
    protected Rectangle bounds;
    
    protected boolean active = true;
    
    protected int maxHitPoints;
    protected int hp;
    
    protected boolean collides = false;
    
    protected boolean show = true;
    
    protected int maxSpeed;
    protected double acc = 1;
    
    protected boolean moving;
    
    protected boolean camTarget = false;
    protected Camera cam = null;
    
    public Entity(int x, int y, int width, int height, int maxHitPoints, World w) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxHitPoints = maxHitPoints;
        this.w = w;
        
        hp = maxHitPoints;
        
        bounds = new Rectangle(x, y, width, height);
    }
    
    public void superTick() {
        tick();
        move();
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
        
        bounds.x = x;
        bounds.y = y;
        
        for (Entity e : w.getEntities()) {
            if (e != this && e.active && this.intersects(e) && e.collides() && collides()) {
                x = oldx;
                y = oldy;
                e.vx += Math.floor(vx);
                e.vy += Math.floor(vy);
                vx = Math.floor(-(vx/2));
                vy = Math.floor(-(vy/2));
                
                
                bounds.x = x;
                bounds.y = y;
            }
        }
        
        moving = Math.abs(vx) > 0 || Math.abs(vy) > 0;
        
        if (isCameraTarget()) cam.updateScreen();
        
        float drag = 0.5f;
        if (vx > 0) vx -= drag;
        else if (vx < 0) vx += drag;
        if (vy > 0) vy -= drag;
        else if (vy < 0) vy += drag;
    }
    
    protected void thrustUp() {
        if (vy > -maxSpeed) vy -= acc;
        thrustParticles(x + Statc.intRandom(-2, width/2), y + height);
    }
    
    protected void thrustDown() {
        if (vy < maxSpeed) vy += acc;
        thrustParticles(x + Statc.intRandom(-2, width/2), y);
    }
    
    protected void thrustLeft() {
        if (vx > -maxSpeed) vx -= acc;
        thrustParticles(x + width, y + Statc.intRandom(-2, height/2));
    }
    
    protected void thrustRight() {
        if (vx < maxSpeed) vx += acc;
        thrustParticles(x, y + Statc.intRandom(-2, height/2));
        
    }
    
    private void thrustParticles(int x, int y) {
            for (int i = 0; i < 3; i++) {
            Sprite sp = new Sprite((x) + (int) vx, 
                (y) + (int) vy, 8, 8, 
                4 + 1 * w.getScreen().sheet.width, 1, w) {
                @Override
                protected void tick() {
                    mirrorDir = Statc.intRandom(0, Screen.BIT_MIRROR_Y);
                }

                @Override
                protected void render(Screen s) {

                }
            };
            sp.noClip();
            sp.vx = vx;
            sp.vy = vy;
            w.addEntity(sp);

            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    sp.active = false;
                }
            }, Statc.intRandom(100, 500));
        }
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
    
    public void setCameraTarget(boolean val, Camera cam) {
        camTarget = val;
        if (cam != null && camTarget) this.cam = cam;
        else {
            this.cam = null;
            camTarget = false;
        }
    }
    
    public boolean isCameraTarget() {
        return camTarget;
    }
    
    public Camera getCamera() {
        if (isCameraTarget()) {
            return cam;
        }
        return null;
    }
}
