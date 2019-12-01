package youretheyoinkreboot.world.items;

import com.amp.mathem.Statc;
import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.core.gfx.SpriteSheet;
import youretheyoinkreboot.world.World;
import youretheyoinkreboot.world.entities.*;

/**
 *
 * @author josh
 */
public class DroppedItem extends Sprite {
    
    public static final int HOVER_RANGE = 6;
    
    private Item item;
    
    private int originY;
    private int goalY;

    public DroppedItem(int x, int y, Item item, World world) {
        super("DROPPED " + item.name, x, y, SpriteSheet.TILE_SIZE, SpriteSheet.TILE_SIZE, item.tile, world);
        this.enableCollision();
        this.item = item;
        
        originY = y + Statc.intRandom(0, 15);
        goalY = originY + HOVER_RANGE;
    }

    @Override
    protected void tick() {
        if (y < goalY) y++;
        else if (y == goalY && goalY == originY + HOVER_RANGE) goalY = originY - HOVER_RANGE;
        else if (y > goalY) y--;
        else if (y == goalY && goalY == originY - HOVER_RANGE) goalY = originY + HOVER_RANGE;
    }
    

    @Override
    protected void render(Screen s) {}
    
    @Override 
    protected void onCollision(Entity source) {
        this.active = false;
        this.onDie(source);
    }
    
    @Override
    protected void onDie(Entity source) {
        if (source instanceof Yoink) {
            ((Yoink) source).getInventory().addItem(item.id, 1);
        }
    }
    
    public Item getItem() {
        return item;
    }
    
}
