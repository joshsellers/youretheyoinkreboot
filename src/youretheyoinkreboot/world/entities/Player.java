package youretheyoinkreboot.world.entities;

import java.awt.Color;
import java.awt.Graphics;
import youretheyoinkreboot.ui.UIControl;
import youretheyoinkreboot.util.Key;
import youretheyoinkreboot.util.KeyToggleListener;
import youretheyoinkreboot.util.Mouse;
import youretheyoinkreboot.world.World;

/**
 *
 * @author josh
 */
public class Player extends Yoink implements KeyToggleListener {
    private final static boolean allowMouseTrigger = false;
    
    private Key k;
    private Mouse m;

    public Player(int x, int y, Key k, Mouse m, World world) {
        super("PLAYER", x, y, 16, 0, world);
        
        this.m = m;
        this.k = k;
        k.addKeyToggleListener(this);
        
        this.maxSpeed = 6;
        this.setMaxHitPoints(150);
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
        
        if (allowMouseTrigger && System.currentTimeMillis() - m.lastTimeClicked() < 20 && m.lastClickedButton() == 1) {
            inv.useItem(inv.getEquipped()[0]);
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
    
    public Key getKeyboardInterface() {
        return k;
    }
    
    public Mouse getMouseInterface() {
        return m;
    }
    
}
