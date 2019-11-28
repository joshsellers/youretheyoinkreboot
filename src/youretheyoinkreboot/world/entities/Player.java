package youretheyoinkreboot.world.entities;

import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.util.Key;
import youretheyoinkreboot.world.World;

/**
 *
 * @author josh
 */
public class Player extends AnimatedSprite {
    
    private Key k;
    
    private int movingDir;

    public Player(int x, int y, Key k, World world) {
        super(x, y, 16, 16, new int[] {
            0 + 2 * world.getScreen().sheet.width, 
            2 + 2 * world.getScreen().sheet.width, 
            4 + 2 * world.getScreen().sheet.width,
            6 + 2 * world.getScreen().sheet.width, 
            8 + 2 * world.getScreen().sheet.width, 
            10 + 2 * world.getScreen().sheet.width,
            12 + 2 * world.getScreen().sheet.width
        }, 125, world);

        this.k = k;
        
        this.maxSpeed = 5;
        this.maxHitPoints = 50;
        this.resistance = 2;
    }

    @Override
    protected void tick() {
        if (maxSpeed <= 5) drag = 0;
        if (k.w.isPressed() && k.a.isPressed()) movingDir = 0;
        else if (k.a.isPressed() && k.s.isPressed()) movingDir = 6;
        else if (k.w.isPressed() && k.d.isPressed()) movingDir = 2;
        else if (k.d.isPressed() && k.s.isPressed()) movingDir = 4;
        else if (k.w.isPressed()) movingDir = 1;
        else if (k.s.isPressed()) movingDir = 5;
        else if (k.a.isPressed()) movingDir = 7;
        else if (k.d.isPressed()) movingDir = 3;
        
        //if (movingDir % 2 == 0) accMod = acc/2;
        //else accMod = acc;
        
        if (k.w.isPressed()) thrustUp();
        else if (k.s.isPressed()) thrustDown();
        if (k.a.isPressed()) thrustLeft();
        else if (k.d.isPressed()) thrustRight();
        
        if (k.b.isPressed()) {
            if (vx > 0) {
                vx--;
            } else if (vx < 0) {
                vx++;
            }
            if (vy > 0) {
                vy--;
            } else if (vy < 0) {
                vy++;
            }
        }
    }

    @Override
    protected void draw(Screen s) {
        
    }
    
}
