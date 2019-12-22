package youretheyoinkreboot.world.entities;

import java.awt.Color;
import java.awt.Graphics;
import youretheyoinkreboot.core.gfx.Screen;
import static youretheyoinkreboot.ui.UIControl.MSG_DISP;
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
    
    private boolean showStats = false;
    
    private Key k;
    private Mouse m;
    
    private int leftExt = 0;
    private int rightExt = 0;
    private int upExt = 0;
    private int downExt = 0;
    
    private int dfc;
    private int nextDist = 20_000;
    
    private int dispScore;

    public Player(int x, int y, Key k, Mouse m, World world) {
        super("PLAYER", x, y, 16, 0, world);
        
        this.m = m;
        this.k = k;
        k.addKeyToggleListener(this);
        
        this.maxSpeed = 6;
        this.setMaxHitPoints(150);
        this.resistance = 2;
        
        this.setColor(0x000000);
    }

    @Override
    protected void tick() {

        if (isMoving()) {
            this.w.generateObjects(this);
            if (this.getY() < this.getNorthExtreme()) {
                upExt = this.getY();
            } else if (this.getY() > this.getSouthExtreme()) {
                downExt = this.getY();
            }
            if (this.getX() < this.getLeftExtreme()) {
                leftExt = this.getX();
            } else if (this.getX() > this.getRightExtreme()) {
                rightExt = this.getX();
            }

        }
        
        //youretheyoinkreboot.ui.UIControl.MSG_DISP.showMessage(String.valueOf(upExt + " " + downExt + " " + leftExt + " " + rightExt), 0xff0000, 10);
        
        
        if (maxSpeed <= 5) drag = 0;
        //boolean lookAtCursor = Item.ITEMS[getInventory().getEquipped()[0]].type == Item.TYPE_RANGED;
        
        int mx = m.currentCoordsInWorld()[0];
        int my = m.currentCoordsInWorld()[1];

        int w = (Screen.WIDTH / Screen.SCALE);
        int h = (Screen.HEIGHT / Screen.SCALE);
        
        int centerPadding = 50;
        
        //if (!lookAtCursor) {
            if (k.w.isPressed() && k.a.isPressed()) movingDir = 0; 
            else if (k.a.isPressed() && k.s.isPressed()) movingDir = 6;
            else if (k.w.isPressed() && k.d.isPressed()) movingDir = 2;
            else if (k.d.isPressed() && k.s.isPressed()) movingDir = 4;
            else if (k.w.isPressed()) movingDir = 1;
            else if (k.s.isPressed()) movingDir = 5;
            else if (k.a.isPressed()) movingDir = 7;
            else if (k.d.isPressed()) movingDir = 3;
        /*} else {
            if (x < w / 2 && y < h / 2) movingDir = 0;
            else if (x > w / 2 && y < h / 2) movingDir = 2;
            else if (x > w / 2 && y > h / 2) movingDir = 4;
            
        }*/
        
        if (k.w.isPressed()) moveUp();
        else if (k.s.isPressed()) moveDown();
        if (k.a.isPressed()) moveLeft();
        else if (k.d.isPressed()) moveRight();
        
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
        int w = 200;
        int h = 10;
        int x = (((Screen.WIDTH / Screen.SCALE) / 2) - (w / 2));
        int y = 10;
        g.setColor(Color.red.darker());
        g.fillRect(x - 1, y - 1, w + 2, h + 2);
        g.setColor(Color.red);
        g.fillRect(x, y, (int) (w * ((float)getHP()/(float)getMaxHP())), h);
        
        g.setColor(Color.green);
        g.drawString("Score: " + getDisplayScore(), x + w + 8, y + 8);
        if (showStats) {
            g.drawString("Distance from center: " + getDistanceFromCenter(), x + w + 8, y + 19);
            g.drawString("Next milestone: " + getNextDistanceMilestone(), x + w + 8, y + 31);

            g.setColor(Color.magenta);
            g.drawString("Max HP: " + getMaxHP(), x + w + 8, y + 42);
            g.drawString("Damage: " + getDamageMod(), x + w + 8, y + 53);
            g.drawString("Max speed: " + getMaxSpeed(), x + w + 8, y + 64);
        }
    }

    @Override
    public void keyToggled(int keyCode) {
        if (keyCode == k.e.keyCode) {
            inv.useItem(inv.getEquipped()[0]);
        }
        
        if (keyCode == k.p.keyCode) {
            showStats = !showStats;
        }
    }
    
    @Override
    protected void onDie(Entity source) {
        MSG_DISP.showMessage("You were killed by " + source.id, 0xEE2255, 5000);
        this.addHP(this.getMaxHP());
        x = 0;
        y = 0;
        this.setColor(0x000000);
        this.getInventory().clear();
        this.move();
    }
    
    public Key getKeyboardInterface() {
        return k;
    }
    
    public Mouse getMouseInterface() {
        return m;
    }
    
    public int getLeftExtreme() {
        return this.leftExt;
    }
    
    public int getRightExtreme() {
        return this.rightExt;
    }
    
    public int getNorthExtreme() {
        return this.upExt;
    }
    
    public int getSouthExtreme() {
        return this.downExt;
    }
    
    public void setDistanceFromCenter(int dfc) {
        this.dfc = dfc;
        if (dfc >= nextDist) {
            int incr = (Integer.MAX_VALUE - dfc) / 65_000;
            nextDist = dfc + incr;
            MSG_DISP.showMessage("DEBUG(PSCORE): incr" + incr + " nextDist" + nextDist, 0x33FF55, 10000);
        }
    }
    
    public int getScore() {
        int score = (int) (((((float) getDistanceFromCenter()) / 
                ((float) getNextDistanceMilestone())) * 45f) + 
                ((((float) getDistanceFromCenter()) / 
                ((float) Integer.MAX_VALUE)) * 1000) * 55f);
        
        if (score > dispScore) dispScore = score;
        return score;
    }
    
    public int getDisplayScore() {
        getScore();
        return dispScore + killCount();
    }
    
    public int getNextDistanceMilestone() {
        return nextDist;
    }
    
    public int getDistanceFromCenter() {
        return dfc;
    }
    
    public void setAccelerationModifier(int set) {
        this.accMod = set;
    }
    
}
