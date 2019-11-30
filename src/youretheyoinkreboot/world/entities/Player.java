package youretheyoinkreboot.world.entities;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.util.Key;
import youretheyoinkreboot.util.KeyToggleListener;
import youretheyoinkreboot.world.World;

/**
 *
 * @author josh
 */
public class Player extends Yoink implements KeyToggleListener {
    private Key k;

    public Player(int x, int y, Key k, World world) {
        super(x, y, 16, world);

        this.k = k;
        k.addKeyToggleListener(this);
        
        this.maxSpeed = 6;
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
        
        if (k.w.isPressed()) thrustUp();
        else if (k.s.isPressed()) thrustDown();
        if (k.a.isPressed()) thrustLeft();
        else if (k.d.isPressed()) thrustRight();
        
        while (getVelocity() > maxSpeed) {
            if (vx > 0) vx--;
            else if (vx < 0) vx++;
            if (vy > 0) vy--;
            else if (vy < 0) vy++;
        }
        
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
    
    public void drawUI(Graphics g) {
        
    }

    @Override
    public void keyToggled(int keyCode) {
        if (keyCode == k.e.keyCode) {
            inv.useItem(inv.getEquipped()[0]);
        }
    }
    
}
