package youretheyoinkreboot.world;

import com.amp.mathem.Statc;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.core.gfx.SpriteSheet;
import youretheyoinkreboot.world.entities.Entity;

/**
 *
 * @author josh
 */
public class World {
    private final List<Entity> entities = new ArrayList<>();
    
    private int bx, by;
    private int[][] oldBackground;
    private int[][] background;
    
    public World(Screen s) {
        generateBackground(s);
    }
    
    public void tick() {
        for (Entity e : getEntities()) {
            if (e.isActive()) e.superTick();
        }
    }
    
    public void render(Screen s) {
        drawBackground(s.getXOffset(), s.getYOffset(), s);
        for (Entity e : getEntities()) {
            if (e.isActive()) e.superRender(s);
        }
    }
    
    private void drawBackground(int xa, int ya, Screen s) {
        moveBackground(s);
        
        int w = ((Screen.WIDTH / Screen.SCALE) / SpriteSheet.TILE_SIZE) + 2;
        int h = ((Screen.HEIGHT / Screen.SCALE) / SpriteSheet.TILE_SIZE) + 2;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                s.render(
                    xa + ((x-1) << Screen.SHIFT), 
                    ya + ((y-1) << Screen.SHIFT), 
                    background[x + y * w][0], background[x + y * w][1], 0, 1);
            }
        }
    }
    
    private int lastxo = 0;
    private int lastyo = 0;
    private void moveBackground(Screen s) {
        oldBackground = background;
        
        int w = ((Screen.WIDTH / Screen.SCALE) / SpriteSheet.TILE_SIZE) + 2;
        int h = ((Screen.HEIGHT / Screen.SCALE) / SpriteSheet.TILE_SIZE) + 2;
        
        int xDif = ((lastxo) - (s.getXOffset()));
        int yDif = ((lastyo) - (s.getYOffset()));
  
        if (xDif < 0 || yDif < 0) {
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int nx = x - xDif;
                    int ny = y - yDif;

                    if (nx >= 0 && ny >= 0 && nx < w && ny < h) {
                        background[x + y * w][0] = oldBackground[nx + ny * w][0];
                        background[x + y * w][1] = oldBackground[nx + ny * w][1];
                    } else {
                        int xTile = Statc.intRandom(0, 1);
                        int yTile = Statc.intRandom(0, 1);
                        int tile = xTile + yTile * s.sheet.width;
                        int mirrorDir = Statc.intRandom(0, 2);

                        int xa = x;
                        int ya = y;

                        background[xa + ya * w][0] = tile;
                        background[xa + ya * w][1] = mirrorDir;
                    }
                }
            }
        } else if (xDif > 0 || yDif > 0) {
            for (int y = h - 1; y >= 0; y--) {
                for (int x = w - 1; x >= 0; x--) {
                    int nx = x - xDif;
                    int ny = y - yDif;

                    if (nx > 0 && ny > 0 && nx < w && ny < h) {
                        background[x + y * w][0] = oldBackground[nx + ny * w][0];
                        background[x + y * w][1] = oldBackground[nx + ny * w][1];
                    } else {
                        int xTile = Statc.intRandom(0, 1);
                        int yTile = Statc.intRandom(0, 1);
                        int tile = xTile + yTile * s.sheet.width;
                        int mirrorDir = Statc.intRandom(0, 2);

                        int xa = x;
                        int ya = y;

                        background[xa + ya * w][0] = tile;
                        background[xa + ya * w][1] = mirrorDir;
                    }
                }
            }
        }
        
        lastxo = s.getXOffset();
        lastyo = s.getYOffset();
    }
    
    private void generateBackground(Screen s) {
        int w = ((Screen.WIDTH / Screen.SCALE) / SpriteSheet.TILE_SIZE) + 2;
        int h = ((Screen.HEIGHT / Screen.SCALE) / SpriteSheet.TILE_SIZE) + 2;
        background = new int[w * h][2];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int xTile = Statc.intRandom(0, 1);
                int yTile = Statc.intRandom(0, 1);
                int tile = xTile + yTile * s.sheet.width;
                int mirrorDir = Statc.intRandom(0, 2);
                
                background[x + y * w][0] = tile;
                background[x + y * w][1] = mirrorDir;
            }
        }
    }
    
    public synchronized List<Entity> getEntities() {
        return this.entities;
    }
    
    public synchronized void addEntity(Entity e) {
        this.getEntities().add(e);
    }
}
