package youretheyoinkreboot.world.items;

import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.core.gfx.SpriteSheet;
import youretheyoinkreboot.world.World;
import youretheyoinkreboot.world.entities.*;

/**
 *
 * @author josh
 */
public class DroppedItem extends Sprite {
    
    private Item item;

    public DroppedItem(int x, int y, Item item, World world) {
        super(x, y, SpriteSheet.TILE_SIZE, SpriteSheet.TILE_SIZE, item.tile, world);
        this.enableCollision();
        this.item = item;
    }

    @Override
    protected void tick() {}
    

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
