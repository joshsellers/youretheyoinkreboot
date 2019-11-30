package youretheyoinkreboot.world.items;

import com.amp.mathem.Statc;
import youretheyoinkreboot.world.entities.Yoink;

/**
 *
 * @author josh
 */
public abstract class Item {
    public static final Item[] ITEMS = new Item[256];
    public final static int TYPE_CONSUMABLE = 0;
    public final static int TYPE_WEARABLE = 1;
    public final static int TYPE_USEABLE = 2;
    public final static int TYPE_OTHER = 4;
    
    public static final Item PLACEHOLDER = new Item(0, 0, "NULL", -1, false) {
        @Override
        public void activateHoldEffects(Yoink holder) {}

        @Override
        public void deactivateHoldEffects(Yoink holder) {}

        @Override
        public void use(Yoink holder) {}
    };
    public static final Item RAINBOWSHARD = new Item(1, 2 + 0 * 32, "Rainbow Shard", TYPE_CONSUMABLE, true) {
        @Override
        public void activateHoldEffects(Yoink holder) {}

        @Override
        public void deactivateHoldEffects(Yoink holder) {}

        @Override
        public void use(Yoink holder) {
            holder.setColor(Statc.intRandom(0xFF000000, 0xFFFFFFFF));
        }
    };
    public static final Item PURPLEORB = new Item(2, 4 + 0 * 32, "Purple Orb", TYPE_OTHER, true) {
        @Override
        public void activateHoldEffects(Yoink holder) {

        }

        @Override
        public void deactivateHoldEffects(Yoink holder) {

        }

        @Override
        public void use(Yoink holder) {

        }
    };
    
    public final int id;
    public final int type;
    public final boolean stackable;
    public final String name;
    public final int tile;
    
    public Item(int id, int tile, String name, int type, boolean stackable) {
        if (ITEMS[id] != null) throw new RuntimeException("Dupe ID! " + id);
        ITEMS[id] = this;
        this.id = id;
        this.tile = tile;
        this.name = name;
        this.type = type;
        this.stackable = stackable;
    }
    
    public abstract void activateHoldEffects(Yoink holder);
    public abstract void deactivateHoldEffects(Yoink holder);
    
    public abstract void use(Yoink holder);
}
