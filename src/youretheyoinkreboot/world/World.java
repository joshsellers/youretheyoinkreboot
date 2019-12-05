package youretheyoinkreboot.world;

import com.amp.mathem.Statc;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.ui.UIControl;
import youretheyoinkreboot.world.entities.Entity;
import youretheyoinkreboot.world.particles.Particle;
import youretheyoinkreboot.world.particles.ParticleHandler;

/**
 *
 * @author josh
 */
public class World {
    private final List<Entity> entities = new ArrayList<>();
    private final Screen s;
    
    private int[] oldBackground;
    private int[] background;
    
    private final ParticleHandler ph;
    
    private boolean showHitBoxes = false;
    
    public EntityRemover remover;
    
    
    public World(Screen s) {
        this.s = s;
        generateBackground(s);
        ph = new ParticleHandler(this);
        
        remover = (e) -> {
            return false;
        };
    }
    
    public synchronized void tick() {
        try {
            boolean removalParametersChanged = false;

            for (Iterator<Entity> it = getEntities().iterator(); it.hasNext();) {
                Entity e = it.next();
                if (e.isActive()) {
                    e.superTick();
                    if (remover.needsRemoved(e)) {
                        removalParametersChanged = true;
                        e.damage(e.getMaxHP(), e);
                    }
                } else {
                    UIControl.MSG_DISP.showMessage("DEBUG: entity \"" + e.id + "\" was removed", 0x00FF88, 5000);
                    it.remove();
                }
            }

            if (removalParametersChanged) {
                remover = (e) -> {
                    return false;
                };
            }
        } catch (java.util.ConcurrentModificationException ex) {
            UIControl.MSG_DISP.showMessage("ERROR: " + ex.getLocalizedMessage() + ", CODE 0", 0xFF0000, 5000);
        }

        try {
            ph.tick();
        } catch (java.util.ConcurrentModificationException ex) {
            UIControl.MSG_DISP.showMessage("ERROR: " + ex.getLocalizedMessage() + ", CODE 1", 0xFF0000, 5000);
        }
    }

    public void render(Screen s) {
        drawBackground(s.getXOffset(), s.getYOffset(), s);
        try {
            getEntities().stream().filter((e) -> (e.isActive())).forEach((e) -> {
                e.superRender(s);
            });
        } catch (java.util.ConcurrentModificationException ex) {
            UIControl.MSG_DISP.showMessage("ERROR: " + ex.getLocalizedMessage() + ", CODE 2", 0xFF0000, 5000);
        }

        try {
            ph.render(s);
        } catch (java.util.ConcurrentModificationException ex) {
            UIControl.MSG_DISP.showMessage("ERROR: " + ex.getLocalizedMessage() + ", CODE 3", 0xFF0000, 5000);
        }
    }
    
    private void drawBackground(int xa, int ya, Screen s) {
        moveBackground(s);
        int w = (Screen.WIDTH / Screen.SCALE);
        int h = (Screen.HEIGHT / Screen.SCALE);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                s.pixels[x + y * w] = background[(x+padding) + (y+padding) * (w + padding)];
            }
        }
    }
    
    private int lastxo = 0;
    private int lastyo = 0;
    private final int padding = 2;
    private final int starProb = 10000;
    private void moveBackground(Screen s) {
        oldBackground = background;
        
        int w = (Screen.WIDTH / Screen.SCALE) + padding;
        int h = (Screen.HEIGHT / Screen.SCALE) + padding;
        
        boolean xDir = s.getXOffset() - lastxo > 0;
        boolean yDir = s.getYOffset() - lastyo > 0;
        int xDif = Math.abs(s.getXOffset() - lastxo);
        int yDif = Math.abs(s.getYOffset() - lastyo);
        
        if (xDif != 0) {
            if (!xDir) {
                for (int y = 0; y < h; y++) {
                    for (int x = w-1; x > 0; x--) {
                        if (x - xDif > 0) {
                            background[x + y * w] = oldBackground[(x - xDif) + y * w];
                        } else {
                            int col = 0xFF000000;
                            if (Statc.intRandom(0, starProb) == 0) col = 0xFFFFFFFF;
                            background[x + y * w] = col;
                        }
                    }
                }
            } else if (xDir) {
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        if (x + xDif < w) {
                            background[x + y * w] = oldBackground[(x + xDif) + y * w];
                        } else {
                            int col = 0xFF000000;
                            if (Statc.intRandom(0, starProb) == 0) col = 0xFFFFFFFF;
                            background[x + y * w] = col;
                        }
                    }
                }
            }
        }
        
        if (yDif != 0) {
            if (!yDir) {
                for (int y = h-1; y > 0; y--) {
                    for (int x = 0; x < w; x++) {
                        if (y - yDif > 0) {
                            background[x + y * w] = oldBackground[x + (y - yDif) * w];
                        } else {
                            int col = 0xFF000000;
                            if (Statc.intRandom(0, starProb) == 0) col = 0xFFFFFFFF;
                            background[x + y * w] = col;
                        }
                    }
                }
            } else if (yDir) {
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        if (y + yDif < h) {
                            background[x + y * w] = oldBackground[x + (y + yDif) * w];
                        } else {
                            int col = 0xFF000000;
                            if (Statc.intRandom(0, starProb) == 0) col = 0xFFFFFFFF;
                            background[x + y * w] = col;
                        }
                    }
                }
            }
        }

        lastxo = s.getXOffset();
        lastyo = s.getYOffset();
    }
    
    private void generateBackground(Screen s) {
        int w = (Screen.WIDTH / Screen.SCALE) + padding;
        int h = (Screen.HEIGHT / Screen.SCALE) + padding;
        background = new int[w * h];
        for (int i = 0; i < background.length; i++) {
            int col = 0xFF000000;
            if (Statc.intRandom(0, starProb) == 0) col = 0xFFFFFFFF;
            background[i] = col;
        }
    }
    
    public synchronized List<Entity> getEntities() {
        return this.entities;
    }
    
    public synchronized void addEntity(Entity e) {
        getEntities().add(e);
    }
    
    public Screen getScreen() {
        return s;
    }
    
    public void addParticle(Particle p) {
        this.getParticleHandler().addParticle(p);
    }
    
    public ParticleHandler getParticleHandler() {
        return ph;
    }
    
    public void toggleShowHitBoxes() {
        showHitBoxes = !showHitBoxes;
    }
    
    public boolean showHitBoxes() {
        return showHitBoxes;
    }
}
