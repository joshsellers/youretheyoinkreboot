package youretheyoinkreboot.world.entities;

import com.amp.mathem.Statc;
import java.awt.Rectangle;
import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.world.World;
import youretheyoinkreboot.world.particles.Particle;

/**
 *
 * @author josh
 */
public abstract class Entity {
    
    public final String id;
    
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
    protected double acc = 1d;
    protected double accMod = acc;
    
    protected int shield = 0;
    protected int damageMod = 0;
    protected float knockBack = 1;
    
    protected float drag = 0.5f;
    protected float resistance = 1;
    
    protected boolean moving;
    
    protected boolean camTarget = false;
    protected Camera cam = null;
    
    protected boolean hasDied = false;
    
    public Entity(String id, int x, int y, int width, int height, int maxHitPoints, World w) {
        this.id = id;
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
        if (isMoving()) move();
    }
    
    public void superRender(Screen s) {
        if (isVisible()) {
            render(s);
        }
    }
    
    protected abstract void tick();
    protected abstract void render(Screen s);
    
    protected void move() {
        int oldx = x;
        int oldy = y;
        x += vx;
        y += vy;
        
        bounds.x = x;
        bounds.y = y;
        
        if (collides()) {
            for (Entity e : w.getEntities()) {
                if (e != this && e.active && this.intersects(e) && e.collides()) {
                    x = oldx;
                    y = oldy;
                    if (e.resistance <= this.resistance) {
                        e.vx += Math.floor(((vx) * knockBack));
                        e.vy += Math.floor(((vy) * knockBack));
                    }
                    vx = Math.floor(-((vx/2)));
                    vy = Math.floor(-((vy/2)));

                    bounds.x = x;
                    bounds.y = y;
                    
                    e.onCollision(this);
                    this.onCollision(e);
                }
            }
        }

        moving = isMoving();
        
        if (isCameraTarget()) cam.updateScreen();
        
        if (vx > 0) vx -= drag;
        else if (vx < 0) vx += drag;
        if (vy > 0) vy -= drag;
        else if (vy < 0) vy += drag;
    }
    
    protected void onCollision(Entity with) {
        
    }
    
    protected void moveUp() {
        if (vy > -maxSpeed) vy -= accMod;
    }
    
    protected void moveDown() {
        if (vy < maxSpeed) vy += accMod;
    }
    
    protected void moveLeft() {
        if (vx > -maxSpeed) vx -= accMod;
    }
    
    protected void moveRight() {
        if (vx < maxSpeed) vx += accMod;
    }
    
    protected void thrustUp() {
        moveUp();
        thrustParticles(x + Statc.intRandom(0, width), y + Statc.intRandom(0, height));
    }
    
    protected void thrustDown() {
        moveDown();
        thrustParticles(x + Statc.intRandom(0, width), y + Statc.intRandom(0, height));
    }
    
    protected void thrustLeft() {
        moveLeft();
        thrustParticles(x + Statc.intRandom(0, width), y + Statc.intRandom(0, height));
    }
    
    protected void thrustRight() {
        moveRight();
        thrustParticles(x + Statc.intRandom(0, width), y + Statc.intRandom(0, height));
        
    }
    
    protected void die(Entity source) {
        active = false;
        hasDied = true;
        onDie(source);
    }
    
    //optional override
    protected void onDie(Entity source) {};
    
    private void thrustParticles(int x, int y) {
        Particle p = new Particle(
                (byte) 1,
                Statc.intRandom(50, 600),
                (x)-4,
                (y)-4,
                4 + 1 * w.getScreen().sheet.width, w) {
            @Override
            protected void tick() {}

            @Override
            protected void render(Screen s) {
                mirrorDir = Statc.intRandom(0, Screen.BIT_MIRROR_Y);
            }
        };

        p.vx = vx;
        p.vy = vy;
        w.addParticle(p);
    }
    
    public void damage(int damage, Entity source) {
        int net = damage;
        if (shield > 0) net -= (damage / shield);
        
        if (hp - net < 0) hp = 0;
        else hp -= net;
        
        if (hp == 0) die(source);
        
        for (int i = 0; i < String.valueOf(net).length(); i++) {
            Particle p = new Particle((byte)1, 500, x + (8*i) - (width / 2), y - 10, (5+(Integer.parseInt("" + String.valueOf(net).toCharArray()[i]))) + 0 * 32, w) {
                @Override
                protected void tick() {
                    y--;
                }

                @Override
                protected void render(Screen s) {

                }
                
            };
            p.vx = vx;
            p.vy = vy;
            p.hue = 0xEEA000;
            w.addParticle(p);
        }
    }
    
    public void addHP(int incr) {
        if (incr + this.getHP() > this.getMaxHP()) this.hp = maxHitPoints;
        else if (incr > 0) this.hp += incr;
        if (hasDied) {
            active = true;
            hasDied = false;
        }
    }
    
    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    
    public int getMaxSpeed() {
        return this.maxSpeed;
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
        return getVelocity() != 0;
    }
    
    public double getVelocity() {
        return Math.hypot(vx, vy);
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
    
    public void setMaxHitPoints(int val) {
        if (hp == maxHitPoints) hp = val;
        this.maxHitPoints = val;
    }
    
    public void setDamageMod(int damage) {
        this.damageMod = damage;
    }
    
    public int getDamageMod() {
        return damageMod;
    }
    
    public void setShield(int val) {
        this.shield = val;
    }
    
    public int getShield() {
        return shield;
    }
    
    public void setKnockback(float val) {
        this.knockBack = val;
    }
    
    public float getKnockback() {
        return this.knockBack;
    }
    
    public boolean hasDied() {
        return this.hasDied;
    }
}
