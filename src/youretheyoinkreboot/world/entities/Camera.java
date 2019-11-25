package youretheyoinkreboot.world.entities;

import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.core.gfx.SpriteSheet;
import youretheyoinkreboot.util.Key;

/**
 *
 * @author josh
 */
public class Camera extends Entity {
    private final Screen s;
    
    public boolean show;
    
    private boolean trackWASD = false;
    private boolean tracking = false;
    private Key k = null;
    private Entity target = null;

    public Camera(int x, int y, Screen s) {
        super(x, y, 1, 1, 0);
        this.s = s;
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
        
        s.setOffset(x - ((Screen.WIDTH / Screen.SCALE) / 2) + SpriteSheet.TILE_SIZE, 
                y - ((Screen.HEIGHT / Screen.SCALE) / 2) + SpriteSheet.TILE_SIZE);
    }

    @Override
    protected void render(Screen s) {
        if (show) s.render(x, y, 6, 0, 0, 1);
    }
    
    public void track(Entity e) {
        target = e;
        tracking = true;
        trackWASD = false;
    }
    
    public void trackWASD(Key k) {
        this.k = k;
        tracking = true;
        trackWASD = true;
    }
    
    public boolean isTracking() {
        return tracking;
    }
    
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
        tracking = false;
        trackWASD = false;
    }
    
    public void stopTracking() {
        tracking = false;
        trackWASD = false;
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
