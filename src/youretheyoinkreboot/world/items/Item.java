package youretheyoinkreboot.world.items;

import com.amp.mathem.Statc;
import java.awt.Color;
import java.util.Random;
import static youretheyoinkreboot.ui.UIControl.MSG_DISP;
import youretheyoinkreboot.util.Mouse;
import youretheyoinkreboot.world.entities.Player;
import youretheyoinkreboot.world.entities.Yoink;
import youretheyoinkreboot.world.particles.Projectile;

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
    public final static int TYPE_AMMO = 6;
    
    public static final Item PLACEHOLDER = new Item(0, 0, "NULL/NULL", -1, false) {
        @Override
        public void activateHoldEffects(Yoink holder, int index) {}

        @Override
        public void deactivateHoldEffects(Yoink holder, int index) {}

        @Override
        public void use(Yoink holder) {}
    };
    public static final Item RAINBOWSHARD = new Item(1, 2 + 0 * 32, "Rainbow Shard/Changes the holder's color when equipped", TYPE_OTHER, false) {
        
        @Override
        public void activateHoldEffects(Yoink holder, int index) {
            if (holder instanceof Player && holder.getInventory().getItem(index)[2] == 0) {
                Player plyr = ((Player) holder);
                int pScore = plyr.getScore();
                
                int r = Statc.intRandom(0, (int)((((float) 255f)) * (((float) pScore) / 100f)));
                int g = Statc.intRandom(0, (int)((((float) 255f)) * (((float) pScore) / 100f)));
                int b = Statc.intRandom(0, (int)((((float) 255f)) * (((float) pScore) / 100f)));
                int col = ((r & 0xFF) << 16) |
                          ((g & 0xFF) << 8)  |
                          ((b & 0xFF));
                
                holder.getInventory().getItem(index)[2] = col;
                MSG_DISP.showMessage(r + " " + g + " " + b + " col" + Integer.toHexString(col), 0x00FF99, 5000);
                /*TODO
                * - divide colors by something like 10 to apply to stats;
                *   = red-hp green-damageMod blue-maxSpeed
                * - remove stat changes on deactivateHoldEffects
                */
            }
            holder.setColor(holder.getColor() | holder.getInventory().getItem(index)[2]);
        }

        @Override
        public void deactivateHoldEffects(Yoink holder, int index) {
            holder.setColor((holder.getColor() & ~holder.getInventory().getItem(index)[2]));
        }

        @Override
        public void use(Yoink holder) {
            
        }
    };
    public static final Item PURPLEORB = new Item(2, 4 + 0 * 32, "Purple Orb/Heals from 5 to 10 HP&nCan also be used as ammo for Purple Orb Cannon", TYPE_CONSUMABLE, true) {
        @Override
        public void activateHoldEffects(Yoink holder, int index) {

        }

        @Override
        public void deactivateHoldEffects(Yoink holder, int index) {

        }

        @Override
        public void use(Yoink holder) {
            if (holder.getHP() < holder.getMaxHP()) {
                int heal = Statc.intRandom(5, 10);
                holder.addHP(heal);
                if (holder instanceof Player) MSG_DISP.showMessage("+" + heal + " hp", 0x8800CC, 5000);
                return;
            }
            if (holder instanceof Player) MSG_DISP.showMessage("No effect", 3000);
        }
    };
    public static final Item TPLAUNCHER = new Item(3, 0, "Test Projectile Launcher/Shoots test projectiles", TYPE_USEABLE, false) {
        @Override
        public void activateHoldEffects(Yoink holder, int index) {

        }

        @Override
        public void deactivateHoldEffects(Yoink holder, int index) {

        }

        @Override
        public void use(Yoink holder) {
            if (holder instanceof Player) {
                Player p = (Player) holder;
                Mouse m = p.getMouseInterface();
                int mx = m.currentCoordsInWorld()[0];
                int my = m.currentCoordsInWorld()[1];
                p.getWorld().addParticle(new Projectile(p.getX() + p.getWidth()/2 - 4, p.getY() + p.getHeight()/2 - 4, mx, my, Projectile.TYPE_TEST, p));
            } else {
                int mx = holder.getTargetX();
                int my = holder.getTargetY();
                holder.getWorld().addParticle(new Projectile(holder.getX() + holder.getWidth()/2 - 4, holder.getY() + holder.getHeight()/2 - 4, mx, my, Projectile.TYPE_TEST, holder));
            }
        }
    };
    public static final Item ORBLAUNCHER = new Item(4, 3 + 0 * 32, "Purple Orb Cannon/Launches Purple Orbs", TYPE_USEABLE, false) {
        @Override
        public void activateHoldEffects(Yoink holder, int index) {

        }

        @Override
        public void deactivateHoldEffects(Yoink holder, int index) {

        }

        @Override
        public void use(Yoink holder) {
            if (holder.getInventory().hasItem(PURPLEORB)) {
                if (holder instanceof Player) {
                    Player p = (Player) holder;
                    Mouse m = p.getMouseInterface();
                    int mx = m.currentCoordsInWorld()[0];
                    int my = m.currentCoordsInWorld()[1];
                    p.getWorld().addParticle(new Projectile(p.getX() + p.getWidth() / 2 - 4, p.getY() + p.getHeight() / 2 - 4, mx, my, Projectile.TYPE_PURPLEORB, p));
                } else {
                    int xmod = 0;
                    int ymod = 0;
                    if (holder.isMoving()) {
                        int angle = Statc.intRandom(60, 150);
                        switch (holder.getMovingDirection()) {
                            case 1:
                                ymod = -angle;
                                break;
                            case 5:
                                ymod = +angle;
                                break;
                            case 7:
                                xmod = -angle;
                                break;
                            case 3:
                                xmod = +angle;
                                break;
                            default:
                                break;
                        }
                    }
                    
                    int mx = (int) (holder.getTargetX() + xmod);
                    int my = (int) (holder.getTargetY() + ymod);
                    holder.getWorld().addParticle(new Projectile(holder.getX() + holder.getWidth() / 2 - 4, holder.getY() + holder.getHeight() / 2 - 4, mx, my, Projectile.TYPE_PURPLEORB, holder));
                }
                holder.getInventory().removeItem(PURPLEORB.id, 1);
            } else {
                if (holder instanceof Player) MSG_DISP.showMessage("Not enough ammo!", 5000);
            }
        }
        
    };
    
    public final int id;
    public final int type;
    public final boolean stackable;
    public final String name;
    private String desc;
    private int longestLineIndex = 0;
    public final int tile;
    
    public Item(int id, int tile, String name, int type, boolean stackable) {
        if (ITEMS[id] != null) throw new RuntimeException("Duplicate ID! " + id);
        ITEMS[id] = this;
        this.id = id;
        this.tile = tile;
        this.name = name.split("/")[0];
        this.desc = name.split("/")[1];
        this.type = type;
        this.stackable = stackable;
        
        if (desc.contains("&n")) {
            String[] descLines = desc.split("&n");
            for (int i = 0; i < descLines.length; i++) {
                if (descLines[i].length() > descLines[longestLineIndex].length()) {
                    longestLineIndex = i;
                }
            }
        }
    }
    
    public abstract void activateHoldEffects(Yoink holder, int index);
    public abstract void deactivateHoldEffects(Yoink holder, int index);
    
    public abstract void use(Yoink holder);
    
    public String getDescription() {
        return desc;
    }
    
    public void setDescription(String desc) {
        if (desc.contains("&n")) {
            String[] descLines = desc.split("&n");
            for (int i = 0; i < descLines.length; i++) {
                if (descLines[i].length() > descLines[longestLineIndex].length()) {
                    longestLineIndex = i;
                }
            }
        }
        this.desc = desc;
    }
    
    public int getLongestDescriptionLineIndex() {
        return this.longestLineIndex;
    }
}
