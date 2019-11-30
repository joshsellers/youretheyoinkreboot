package youretheyoinkreboot.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.core.gfx.SpriteSheet;
import youretheyoinkreboot.util.Mouse;
import youretheyoinkreboot.world.items.Inventory;
import youretheyoinkreboot.world.items.Item;

/**
 *
 * @author josh
 */
public class InventoryInterface extends UIObject {
    
    private Inventory source;
    
    private Mouse m;
    
    public InventoryInterface(int x, int y, Inventory source, Mouse m) {
        super((byte) 0x02, x, y);
        this.source = source;
        this.m = m;
    }

    @Override
    protected void tick() {
        if (System.currentTimeMillis() - m.lastTimeClicked() < 10) {
            for (int i = 0; i < Inventory.INV_SIZE; i++) {
                Rectangle mouse = new Rectangle(m.lastClickCoordsOnScreen()[0], m.lastClickCoordsOnScreen()[1], 2, 2);
                Rectangle icon = new Rectangle(x, y + (10 * i), SpriteSheet.TILE_SIZE, SpriteSheet.TILE_SIZE);
                if (mouse.intersects(icon)) {
                    if (m.lastClickedButton() == 1) {
                        if (source.isEquipped(i)) {
                            source.deEquip(i);
                        } else source.equip(i);
                    } else if (m.lastClickedButton() == 3) {
                        source.removeItem(source.getItem(i)[0], 1);
                    }
                }
            }
        } 
    }

    @Override
    protected void draw(Graphics g) {
        for (int i = 0; i < Inventory.INV_SIZE; i++) {
            if (source.getItem(i)[0] != Item.PLACEHOLDER.id) {
                if (source.isEquipped(i)) {
                    g.setColor(Color.blue);
                    g.fillRect(x - 1, (y + (12 * i)) - 1, 10, 10);
                }
                g.drawImage(getIcon(Item.ITEMS[source.getItem(i)[0]].tile), x, y + (12 * i), null);
                g.setColor(Color.white);
                g.drawString(String.valueOf(source.getItem(i)[1]), x + 12, y + (12 * i) + 8);
            }
        }
    }
    
    public Inventory getSourceInventory() {
        return source;
    }
    
    public void setSourceInventory(Inventory source) {
        this.source = source;
    }
    
    private BufferedImage getIcon(int tile) {
        BufferedImage icon = new BufferedImage(SpriteSheet.TILE_SIZE, SpriteSheet.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
        SpriteSheet sheet = source.getParent().getWorld().getScreen().sheet;
        int xTile = (tile % (sheet.width)) << Screen.SHIFT;
        int yTile = (tile / (sheet.width)) << Screen.SHIFT;
        for (int ya = 0; ya < icon.getHeight(); ya++) {
            for (int xa = 0; xa < icon.getWidth(); xa++) {
                icon.setRGB(xa, ya, sheet.pixels[(xTile + xa) + (yTile + ya) * (sheet.width * SpriteSheet.TILE_SIZE)]);
            }
        }
        
        return icon;
    }
    
    public void toggle() {
        show = !show;
    }
}
