package youretheyoinkreboot.world.entities;

import youretheyoinkreboot.world.items.Item;

/**
 *
 * @author josh
 */
public class AngryYoink extends Yoink {
    
    public final static boolean ATTACKMETHOD_RAM = false;
    public final static boolean ATTACKMETHOD_RANGED = true;
    
    private boolean attackMethod;
    
    private final Player p;

    public AngryYoink(int x, int y, boolean attackMethod, Player p) {
        super("ANGRYBOI", x, y, 16, 0, p.getWorld());
        this.p = p;
        this.attackMethod = attackMethod;
    }

    @Override
    protected void tick() {
        boolean up = false;
        boolean down = false;
        boolean left = false;
        boolean right = false;

        int dist = 1;
        if (attackMethod) {
            dist = 100;
        }
        
        if (shouldAttack()) {
            if (p.getX() + (p.getWidth() / 2) + dist - 1 < this.x + (getWidth() / 2)) {
                thrustLeft();
                left = true;
            } else if (p.getX() + (p.getWidth() / 2) - dist + 1 > this.x + (this.getWidth() / 2)) {
                thrustRight();
                right = true;
            }
            if (p.getY() + (p.getHeight() / 2) + dist - 1 < this.y + (this.getHeight() / 2)) {
                thrustUp();
                up = true;
            } else if (p.getY() + (p.getHeight() / 2) - dist + 1 > this.y + (this.getHeight() / 2)) {
                thrustDown();
                down = true;
            }
        }

        if (up && left) {
            movingDir = 0;
        } else if (up && right) {
            movingDir = 2;
        } else if (down && right) {
            movingDir = 4;
        } else if (down && left) {
            movingDir = 6;
        } else if (up) {
            movingDir = 1;
        } else if (right) {
            movingDir = 3;
        } else if (down) {
            movingDir = 5;
        } else if (left) {
            movingDir = 7;
        }

        if (attackMethod && !(left || right || up || down)) {
            targetX = p.getX();
            targetY = p.getY();
            if (damageStagger % 32 == 0) {
                this.inv.useItem(inv.getEquipped()[0]);
                if (!getInventory().hasItem(Item.PURPLEORB)) {
                    attackMethod = ATTACKMETHOD_RAM;
                }
            }
            damageStagger++;
        } else {
            damageStagger++;
        }
    }
    
    private int damageStagger = 0;

    @Override
    protected void onCollision(Entity with) {
        if (!with.id.equals(this.id) && damageStagger % 8 == 0) {
            with.damage(damageMod, this);
            if (getInventory().hasItem(Item.PURPLEORB)) {
                attackMethod = ATTACKMETHOD_RANGED;
            }
        }
    }
    
    public boolean shouldAttack() {
        int dist = (int) Math.sqrt(Math.pow(p.getX() - getX(), 2) + Math.pow(p.getY() - getY(), 2));
        return dist < 9000;
    }
    
    public Player getPlayer() {
        return p;
    }
    
    public boolean getAttackMethod() {
        return attackMethod;
    }
}
