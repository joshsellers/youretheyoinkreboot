package youretheyoinkreboot.world.entities;

import com.amp.mathem.Statc;
import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.core.gfx.SpriteSheet;
import youretheyoinkreboot.world.World;
import youretheyoinkreboot.world.items.DroppedItem;
import youretheyoinkreboot.world.items.Item;

/**
 *
 * @author joshsellers
 */
public class Garble extends AnimatedSprite {
    
    public Garble(int x, int y, World w) {
        super("GARBLE", x, y, 8, 8, new int[] {
            4 + 0 * SpriteSheet.SIZE_TILES
        }, 125, w);
        
        this.resistance = 1f;
    }

    @Override
    protected void tick() {

    }

    @Override
    protected void draw(Screen s) {

    }

    @Override
    protected void onCollision(Entity with) {
        damage(10, with);
    }

    @Override
    protected void onDie(Entity source) {
        w.addEntity(new DroppedItem(x, y, Item.PURPLEORB, w));
        if (Statc.intRandom(0, 5) == 0) {
            w.addEntity(new DroppedItem(x, y + 10, Item.RAINBOWSHARD, w));
        }
    }
}
