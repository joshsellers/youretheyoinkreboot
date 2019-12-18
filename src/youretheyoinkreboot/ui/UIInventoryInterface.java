package youretheyoinkreboot.ui;

import com.amp.mathem.Statc;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.core.gfx.SpriteSheet;
import youretheyoinkreboot.util.Mouse;
import youretheyoinkreboot.world.items.DroppedItem;
import youretheyoinkreboot.world.items.Inventory;
import youretheyoinkreboot.world.items.Item;

/**
 *
 * @author josh
 */
public class UIInventoryInterface extends UIObject {
    
    private Inventory source;
    
    private Mouse m;
    
    public UIInventoryInterface(int x, int y, Inventory source, Mouse m) {
        super((byte) 0x02, x, y);
        this.source = source;
        this.m = m;
        
        this.width = 22;
    }

    @Override
    protected void tick() {
        if (System.currentTimeMillis() - m.lastTimeClicked() < 20) {
            int j = 0;
            for (int i = 0; i < Inventory.INV_SIZE; i++) {
                if (source.getItem(i)[0] != Item.PLACEHOLDER.id) {
                    Rectangle mouse = new Rectangle(m.lastClickCoordsOnScreen()[0], m.lastClickCoordsOnScreen()[1], 2, 2);
                    Rectangle icon = new Rectangle(x, (y + (12 * j)) - 1, SpriteSheet.TILE_SIZE, SpriteSheet.TILE_SIZE);
                    if (mouse.intersects(icon)) {
                        if (m.lastClickedButton() == 1) {
                            if (source.isEquipped(i)) {
                                source.deEquip(i);
                            } else {
                                source.equip(i);
                            }
                        } else if (m.lastClickedButton() == 3) {
                            int xr = source.getParent().getX() + Statc.intRandom(-20, 20);
                            int yr = source.getParent().getY() + Statc.intRandom(-20, 20);
                            source.getParent().getWorld().addEntity(new DroppedItem(xr, yr, Item.ITEMS[source.getItem(i)[0]], source.getParent().getWorld()));
                            source.removeItem(source.getItem(i)[0], 1);
                        }
                    }
                    j++;
                }
            }
        }
    }

    @Override
    protected void draw(Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        
        g.setColor(Color.magenta.darker().darker().darker().darker());
        g.fillRect(x-3, y-3, width+2, height+5);
        g.setColor(Color.magenta.darker().darker());
        g.fillRect(x-2, y-2, width, height+3);
        
        int hl = 0;
        int j = 0;
        for (int i = 0; i < Inventory.INV_SIZE; i++) {
            if (source.getItem(i)[0] != Item.PLACEHOLDER.id) {
                if (source.isEquipped(i)) {
                    if (Item.ITEMS[source.getItem(i)[0]].type == Item.TYPE_USEABLE || Item.ITEMS[source.getItem(i)[0]].type == Item.TYPE_CONSUMABLE) {
                        g.setColor(Color.green);
                    } else g.setColor(Color.blue);
                    g.fillRect(x - 1, (y + (12 * j)) - 1, 10, 10);
                }
                g.drawImage(getIcon(Item.ITEMS[source.getItem(i)[0]].tile), x, y + (12 * j), null);
                g.setColor(Color.white);
                if (Item.ITEMS[source.getItem(i)[0]].stackable) {
                    g.drawString(String.valueOf(source.getItem(i)[1]), x + 12, y + (12 * j) + 8);
                    if (String.valueOf(source.getItem(i)[1]).length() > String.valueOf(source.getItem(hl)[1]).length()) {
                        hl = i;
                    }
                }
                
                Rectangle mouse = new Rectangle(m.currentCoordsOnScreen()[0], m.currentCoordsOnScreen()[1], 2, 2);
                Rectangle icon = new Rectangle(x, (y + (12 * j)) - 1, SpriteSheet.TILE_SIZE, SpriteSheet.TILE_SIZE);
                if (mouse.intersects(icon)) {
                    String name = Item.ITEMS[source.getItem(i)[0]].name;
                    String desc = Item.ITEMS[source.getItem(i)[0]].getDescription();
                    
                    int wName = fm.stringWidth(name);
                    int wDesc = fm.stringWidth(desc);
                    int heightMod = 0;
                    if (desc.contains("&n")) {
                        wDesc = fm.stringWidth(desc.split("&n")[Item.ITEMS[source.getItem(i)[0]].getLongestDescriptionLineIndex()]);
                        heightMod = (desc.split("&n").length - 1) * 10;
                    }

                    int w = wName > wDesc ? wName : wDesc;
                    
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(mouse.x - 2, mouse.y - 2, w + 6, 28 + heightMod);
                    g.setColor(Color.WHITE);
                    g.fillRect(mouse.x, mouse.y, w + 2, 24 + heightMod);
                    g.setColor(Color.BLACK);
                    g.drawString(name, mouse.x + 1, mouse.y + 11);
                    g.setColor(Color.GRAY);
                    if (!desc.contains("&n")) g.drawString(desc, mouse.x + 1, mouse.y + 22);
                    else {
                        for (int a = 0; a < desc.split("&n").length; a++) {
                            g.drawString(desc.split("&n")[a], mouse.x + 1, mouse.y + 22 + (10 * a));
                        }
                    }
                }
                
                j++;
            }
        }
        
        width = 15 + fm.stringWidth(String.valueOf(source.getItem(hl)[1]));
        height = (12 * j) - 2;
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
                if (sheet.pixels[(xTile + xa) + (yTile + ya) * (sheet.width * SpriteSheet.TILE_SIZE)] != 0xff000001) {
                    icon.setRGB(xa, ya, sheet.pixels[(xTile + xa) + (yTile + ya) * (sheet.width * SpriteSheet.TILE_SIZE)]);
                }
            }
        }
        
        return icon;
    }
    
    public void toggle() {
        show = !show;
    }
}
