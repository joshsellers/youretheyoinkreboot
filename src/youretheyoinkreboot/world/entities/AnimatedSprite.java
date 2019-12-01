package youretheyoinkreboot.world.entities;

import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.core.gfx.SpriteSheet;
import youretheyoinkreboot.world.World;

/**
 *
 * @author josh
 */
public abstract class AnimatedSprite extends Sprite {
    
    protected int[][] frames;
    
    private int currentFrame;
    private long lastIterationTime;
    protected int animationSpeed;;
    
    protected boolean animate = true;
    
    public AnimatedSprite(String id, int x, int y, int w, int h, int[] spriteIndices, int animSpeed, World world) {
        super(id, x, y, w, h, spriteIndices[0], world);
        int tw = w / SpriteSheet.TILE_SIZE;
        int th = h / SpriteSheet.TILE_SIZE;
        frames = new int[spriteIndices.length][tw * th];
        for (int i = 0; i < frames.length; i++) {
            int xTile = spriteIndices[i] % ((sheet.width));
            int yTile = spriteIndices[i] / ((sheet.width));
            for (int ya = yTile; ya < th + yTile; ya++) {
                for (int xa = xTile; xa < tw + xTile; xa++) {
                    frames[i][(xa - xTile) + (ya - yTile) * tw] = xa + ya * sheet.width;
                }
            }
        }
        
        this.lastIterationTime = System.currentTimeMillis();
        this.animationSpeed = animSpeed;
    }
    
    @Override
    protected void render(Screen s) {
        if (isAnimating() && (System.currentTimeMillis() - lastIterationTime) >= (animationSpeed)) {
            lastIterationTime = System.currentTimeMillis();
            currentFrame = (currentFrame + 1) % frames.length;
            tiles = frames[currentFrame];
        }
        
        draw(s);
    }
    
    protected abstract void draw(Screen s);
    
    public int[] getFrame(int frame) {
        return frames[frame];
    }
    
    public void startAnimation() {
        animate = true;
    }
    
    public void stopAnimation() {
        animate = false;
    }
    
    public boolean isAnimating() {
        return animate;
    }
}
