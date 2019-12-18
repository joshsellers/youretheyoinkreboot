package youretheyoinkreboot.world.entities;

import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.core.gfx.SpriteSheet;
import youretheyoinkreboot.world.World;
import youretheyoinkreboot.world.items.Inventory;

/**
 *
 * @author josh
 */
public abstract class Yoink extends AnimatedSprite {
    
    protected int[][][] animations;
    
    private static final int[][] SPRITE_INDICES = new int[][] {
        {
            0 + 2 * 32, 
            2 + 2 * 32, 
            4 + 2 * 32,
            6 + 2 * 32, 
            8 + 2 * 32, 
            10 + 2 * 32,
            12 + 2 * 32
        }, {
            0 + 4 * 32, 
            2 + 4 * 32, 
            4 + 4 * 32,
            6 + 4 * 32, 
            8 + 4 * 32, 
            10 + 4 * 32,
            12 + 4 * 32
        }, {
            0 + 6 * 32, 
            2 + 6 * 32, 
            4 + 6 * 32,
            6 + 6 * 32, 
            8 + 6 * 32, 
            10 + 6 * 32,
            12 + 6 * 32
        }, {
            0 + 8 * 32, 
            2 + 8 * 32, 
            4 + 8 * 32,
            6 + 8 * 32, 
            8 + 8 * 32, 
            10 + 8 * 32,
            12 + 8 * 32
        }, {
            0 + 10 * 32, 
            2 + 10 * 32, 
            4 + 10 * 32,
            6 + 10 * 32, 
            8 + 10 * 32, 
            10 + 10 * 32,
            12 + 10 * 32
        }, {
            0 + 12 * 32, 
            2 + 12 * 32, 
            4 + 12 * 32,
            6 + 12 * 32, 
            8 + 12 * 32, 
            10 + 12 * 32,
            12 + 12 * 32
        }, {
            0 + 14 * 32, 
            2 + 14 * 32, 
            4 + 14 * 32,
            6 + 14 * 32, 
            8 + 14 * 32, 
            10 + 14 * 32,
            12 + 14 * 32
        }, {
            0 + 16 * 32, 
            2 + 16 * 32, 
            4 + 16 * 32,
            6 + 16 * 32, 
            8 + 16 * 32, 
            10 + 16 * 32,
            12 + 16 * 32
        }
    };
    
    protected Inventory inv;
    
    protected int movingDir;
    
    protected int targetX, targetY;

    public Yoink(String id, int x, int y, int size, int tileBase, World world) {
        super(id, x, y, size, size, SPRITE_INDICES[0], 125, world);
        int baseX = tileBase % sheet.width;
        int baseY = tileBase / sheet.width;
        for (int[] SPRITE_INDICES1 : SPRITE_INDICES) {
            for (int j = 0; j < SPRITE_INDICES1.length; j++) {
                int tileX = SPRITE_INDICES1[j] % sheet.width;
                int tileY = SPRITE_INDICES1[j] / sheet.width;
                SPRITE_INDICES1[j] = (tileX + baseX) + (tileY + baseY) * sheet.width;
            }
        }
        
        int tw = size / SpriteSheet.TILE_SIZE;
        int th = size / SpriteSheet.TILE_SIZE;
        animations = new int[SPRITE_INDICES.length][SPRITE_INDICES[0].length][tw * th];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                int xTile = SPRITE_INDICES[j][i] % ((sheet.width));
                int yTile = SPRITE_INDICES[j][i] / ((sheet.width));
                for (int ya = yTile; ya < th + yTile; ya++) {
                    for (int xa = xTile; xa < tw + xTile; xa++) {
                        animations[j][i][(xa - xTile) + (ya - yTile) * tw] = xa + ya * sheet.width;
                    }
                }
            }
        }
        
        this.hue = 0x0053EE;
        
        inv = new Inventory(this);
    }

    @Override
    protected void draw(Screen s) {
        frames = animations[movingDir];
    }
    
    public void setColor(int color) {
        this.hue = color;
    }
    
    public int getColor() {
        return this.hue;
    }
    
    public Inventory getInventory() {
        return inv;
    }
    
    public int getTargetX() {
        return targetX;
    }
    
    public int getTargetY() {
        return targetY;
    }
    
    public int getMovingDirection() {
        return movingDir;
    }
    
}
