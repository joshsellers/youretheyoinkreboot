package youretheyoinkreboot.world.entities;

import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.core.gfx.SpriteSheet;
import youretheyoinkreboot.world.World;

/**
 *
 * @author josh
 */
public abstract class Sprite extends Entity {
    
    protected SpriteSheet sheet;
    
    protected int[] tiles;
    
    protected int mirrorDir;
    protected int hue;
    
    public Sprite(String id, int x, int y, int w, int h, int tile, World world) {
        super(id, x, y, w, h, 100, world);
        this.sheet = world.getScreen().sheet;
        int tw = w / SpriteSheet.TILE_SIZE;
        int th = h / SpriteSheet.TILE_SIZE;
        tiles = new int[tw * th];
        int xTile = tile % ((sheet.width));
        int yTile = tile / ((sheet.width));
        for (int ya = yTile; ya < th+yTile; ya++) {
            for (int xa = xTile; xa < tw+xTile; xa++) {
                tiles[(xa-xTile) + (ya-yTile) * tw] = xa + ya * sheet.width;
            }
        }
    }
    
    
    @Override
    public void superRender(Screen s) {
        if (isVisible()) {
            int rx = x;
            int ry = y;
            if (isCameraTarget()) {
                rx = s.getXOffset() + ((Screen.WIDTH / Screen.SCALE) / 2) - (this.getWidth() / 2);
                ry = s.getYOffset() + ((Screen.HEIGHT / Screen.SCALE) / 2) - (this.getHeight() / 2);
            }
            
            int w = this.getWidth() / SpriteSheet.TILE_SIZE;
            int h = this.getHeight() / SpriteSheet.TILE_SIZE;
            for (int ya = 0; ya < h; ya++) {
                for (int xa = 0; xa < w; xa++) {
                    int xOffset = xa * SpriteSheet.TILE_SIZE;
                    int yOffset = ya * SpriteSheet.TILE_SIZE;
                    int tile = tiles[xa + ya * w];
                    s.render(rx + xOffset, ry + yOffset, tile, mirrorDir, hue, 1);
                }
            }
            
            render(s);
        }
    }
}
