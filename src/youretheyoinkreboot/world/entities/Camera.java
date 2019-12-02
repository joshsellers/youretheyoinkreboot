package youretheyoinkreboot.world.entities;

import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.core.gfx.SpriteSheet;
import youretheyoinkreboot.util.Key;
import youretheyoinkreboot.world.World;

/**
 *
 * @author josh
 */
public class Camera extends Entity {
    private final Screen s;
        
    private boolean trackWASD = false;
    private boolean tracking = false;
    private Key k = null;
    private Entity target = null;

    public Camera(int x, int y, Screen s, World w) {
        super("CAM", x, y, 1, 1, 0, w);
        this.s = s;
        noClip();
    }

    @Override
    protected void tick() {
        if (isTracking()) {
            if (trackWASD && k != null) {
                if (k.w.isPressed()) y--;
                else if (k.s.isPressed()) y++;
                else if (k.a.isPressed()) x--;
                else if (k.d.isPressed()) x++;
            } else if (target != null) {
                setPos(target.getX(), target.getY());
                
            }
        }
    }

    @Override
    protected void render(Screen s) {
        s.render(x, y, 6, 0, 0, 1);
    }
    
    public void updateScreen() {
        double xOffset = SpriteSheet.TILE_SIZE;
        double yOffset = SpriteSheet.TILE_SIZE;
        if (target != null && !trackWASD) {
            xOffset = -target.getWidth() / 2;
            yOffset = -target.getHeight() / 2;
            if (target.isMoving()) {
                xOffset -= target.vx;
                yOffset -= target.vy;
            }
        }
        
        s.setOffset(x - ((Screen.WIDTH / Screen.SCALE) / 2) - (int)(xOffset), 
            y - ((Screen.HEIGHT / Screen.SCALE) / 2) - (int)(yOffset));
    }
    
    public void track(Entity e) {
        target = e;
        tracking = true;
        trackWASD = false;
        
        target.setCameraTarget(true, this);
    }
    
    public void trackWASD(Key k) {
        this.k = k;
        tracking = true;
        trackWASD = true;
        
        if (target != null) target.setCameraTarget(false, null);
    }
    
    public boolean isTracking() {
        return tracking;
    }
    
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
        tracking = true;
        trackWASD = false;        
    }
    
    public void stopTracking() {
        tracking = false;
        trackWASD = false;
        
        if (target != null) target.setCameraTarget(false, null);
    }
    
    public Key getKeyInterface() {
        return k;
    }
    
    public Entity getTarget() {
        return target;
    }
    
    public Screen getScreen() {
        return s;
    }
    
}
