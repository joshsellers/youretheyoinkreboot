package youretheyoinkreboot.world.particles;

import java.util.Timer;
import java.util.TimerTask;
import youretheyoinkreboot.core.gfx.SpriteSheet;
import youretheyoinkreboot.world.World;
import youretheyoinkreboot.world.entities.Sprite;

/**
 *
 * @author josh
 */
public abstract class Particle extends Sprite {
    
    public static final byte PLACEHOLDER_PARTICLE = 0;
    
    protected byte id = PLACEHOLDER_PARTICLE;
    
    protected long lifeTime = 0;
    
    public Particle(byte id, long lifeTime, int x, int y, int tile, World world) {
        super(x, y, SpriteSheet.TILE_SIZE, SpriteSheet.TILE_SIZE, tile, 1, world);
        
        this.id = id;
        this.lifeTime = lifeTime;
    }
    
    public void decay() {
        noClip();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                active = false;
                id = PLACEHOLDER_PARTICLE;
            }
        }, getLifeTime());
    }
    
    public long getLifeTime() {
        return lifeTime;
    }
    
    public byte getID() {
        return id;
    }
}
