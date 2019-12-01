package youretheyoinkreboot.world.particles;

import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.ui.UIControl;
import youretheyoinkreboot.world.World;
import youretheyoinkreboot.world.entities.Entity;
import youretheyoinkreboot.world.items.DroppedItem;

/**
 *
 * @author josh
 */
public class Projectile extends Particle {
    
    //format: VELOCITY, DAMAGE, SPRITE
    public static final int[][] TYPE_DATA = new int[][] {
        {5, 20, 0} //TYPE_TEST
    };
    
    public static final int TYPE_TEST = 0;
    
    private final int type;
    
    private final Entity source;
    
    private final long spawnTimeStamp;

    private int currentTime = 0;
    
    private final double v;
    
    private final int initX;
    private final int initY;
    
    public Projectile(int x, int y, int targetX, int targetY, int type, Entity source) {
        super(PROJECTILE, 5000, x, y, TYPE_DATA[type][2], source.getWorld());
        //(long) (((Math.sqrt(Math.pow(targetX - x, 2) + Math.pow(targetY - y, 2))/velocity)/60)*1000) + 50
        //^ lifetime value to be used to kill once target is reached
        this.type = type;
        this.spawnTimeStamp = System.currentTimeMillis();
        this.damageMod = TYPE_DATA[type][1];
        this.source = source;
        
        this.v = (double) TYPE_DATA[type][0];
        int xDist = targetX - x;
        int yDist = targetY - y;
        this.initX = x;
        this.initY = y;
        
        double initDist = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
        
        vx = v * (xDist / initDist);
        vy = v * (yDist / initDist);
        
        currentTime = 0;
    }
    
    @Override
    protected void move() {
        x = (int) ((vx * currentTime) + initX);
        y = (int) ((vy * currentTime) + initY);
        
        bounds.x = x;
        bounds.y = y;
   
        for (Entity e : w.getEntities()) {
            if (!(e instanceof DroppedItem) && e != source && e != this && e.isActive() && this.intersects(e) && e.collides()) {
                this.die(e);
                e.damage(damageMod, this);
                UIControl.MSG_DISP.showMessage("DEBUG: " + id + spawnTimeStamp + " hit " + e.id, 5000);
            }
        }

        currentTime++;
    }

    @Override
    protected void tick() {}

    @Override
    protected void render(Screen s) {}
    
    @Override 
    public boolean isMoving() {
        return true;
    }
    
    public Entity getSource() {
        return source;
    }
    
    public int getType() {
        return type;
    }
    
}
