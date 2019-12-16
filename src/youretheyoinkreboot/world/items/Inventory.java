package youretheyoinkreboot.world.items;

import youretheyoinkreboot.world.entities.Yoink;
import youretheyoinkreboot.ui.UIControl;
import youretheyoinkreboot.world.entities.Player;

/**
 *
 * @author josh
 */
public class Inventory {
    public final static int INV_SIZE = 64;
    
    private final int[][] items = new int[INV_SIZE][2];
    
    private final Yoink parent;
    
    private int equippedUseable = -1;
    
    private int[] equipped = new int[] {-1, -1, -1, -1};
    
    public Inventory(Yoink parent) {
        this.parent = parent;
    }
    
    public void addItem(int id, int amount) {
        if (amount > 0) {
            if (parent instanceof Player) UIControl.MSG_DISP.showMessage("Obtained " + Item.ITEMS[id].name, 5000);
            
            for (int i = 0; i < INV_SIZE; i++) {
                if (items[i][0] == id && Item.ITEMS[items[i][0]].stackable) {
                    items[i][1] += amount;
                    return;
                }
            }
            for (int i = 0; i < INV_SIZE; i++) {
                if (items[i][0] == Item.PLACEHOLDER.id) {
                    items[i][0] = id;
                    items[i][1] = 1;
                    addItem(id, amount - 1);
                    return;
                }
            }
        }
    }
    
    public void removeItem(int id, int amount) {
        if (amount > 0) {
            for (int i = 0; i < INV_SIZE; i++) {
                if (items[i][0] == id) {
                    if (items[i][1] > 1) {
                        if (isEquipped(i)) Item.ITEMS[id].deactivateHoldEffects(parent);
                        items[i][1]--;
                        removeItem(id, amount - 1);
                        return;
                    } else if (items[i][1] == 1) {
                        if (isEquipped(i)) Item.ITEMS[id].deactivateHoldEffects(parent);
                        items[i][0] = Item.PLACEHOLDER.id;
                        items[i][1] = 0;
                        if (amount > 1) removeItem(id, amount - 1);
                        return;
                    }
                }
            }
        }
    }
    
    public void useItem(int index) {
        if (index == -1) return;
        Item.ITEMS[items[index][0]].use(parent);
        if (Item.ITEMS[items[index][0]].type == Item.TYPE_CONSUMABLE) {
            removeItem(items[index][0], 1);
        }
    }
    
    public void equip(int index) {
        if (Item.ITEMS[items[index][0]].type != Item.TYPE_USEABLE 
            && Item.ITEMS[items[index][0]].type != Item.TYPE_CONSUMABLE 
            && Item.ITEMS[items[index][0]].type != Item.TYPE_RANGED) {
            for (int i = 0; i < equipped.length; i++) {
                if (equipped[i] == -1) {
                    equipped[i] = index;
                    Item.ITEMS[items[equipped[i]][0]].activateHoldEffects(parent);
                    return;
                }
            }
        } else {
            equippedUseable = index;
        }
    }
    
    public void deEquip(int index) {
        for (int i = 0; i < equipped.length; i++) {
            if (equipped[i] == index) {
                Item.ITEMS[items[equipped[i]][0]].deactivateHoldEffects(parent);
                equipped[i] = -1;
                return;
            }
        }
        if (equippedUseable == index) equippedUseable = -1;
    }
    
    public int[] getEquipped() {
        return new int[] {equippedUseable, equipped[0], equipped[1], equipped[2], equipped[3]};
    }
    
    public boolean isEquipped(int index) {
        for (int i = 0; i < getEquipped().length; i++) {
            if (getEquipped()[i] == index) return true;
        }
        return false;
    }
    
    public int[] getItem(int index) {
        return items[index];
    } 
    
    public Yoink getParent() {
        return parent;
    }
}
