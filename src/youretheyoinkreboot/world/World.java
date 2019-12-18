package youretheyoinkreboot.world;

import com.amp.mathem.Statc;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.ui.UIControl;
import static youretheyoinkreboot.ui.UIControl.MSG_DISP;
import youretheyoinkreboot.world.entities.AngryYoink;
import youretheyoinkreboot.world.entities.Entity;
import youretheyoinkreboot.world.entities.Garble;
import youretheyoinkreboot.world.entities.Player;
import youretheyoinkreboot.world.items.Item;
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
    
    public static final int GARBLE_SPAWNRATE = 200;
    public static final int ENEMY_SPAWNRATE = 2100;
    
    private long garbleStagger;
    private long nextGarbleInterval = Statc.intRandom(0, GARBLE_SPAWNRATE);
    
    private long enemyStagger;
    private long nextEnemyInterval = Statc.intRandom(0, ENEMY_SPAWNRATE);
    
    private final int sw = Screen.WIDTH / Screen.SCALE;
    private final int sh = Screen.HEIGHT / Screen.SCALE;
    public void generateObjects(Player p) {
        int x = p.getX();
        int y = p.getY();
        boolean nx = true;
        boolean ny = true;
        
        if (p.getX() < p.getLeftExtreme()) {
            x -= sw;
        } else if (p.getX() > p.getRightExtreme()) {
            x += sw;
        } else nx = false;
        
        if (p.getY() < p.getNorthExtreme()) {
            y -= sh;
        } else if (p.getY() > p.getSouthExtreme()) {
            y += sh;
        } else ny = false;
        
        boolean n = nx || ny;
        
        int dfc = (int) Math.sqrt(Math.pow((p.getX()), 2) + Math.pow(p.getY(), 2));
        if (n) {
            //MSG_DISP.showMessage("DEBUG(WORLDGEN): PDFC " + dfc, 0x00FF11, 10);
            p.setDistanceFromCenter(dfc);
        }
        
        if (dfc > 7000 && enemyStagger >= nextEnemyInterval) {
            MSG_DISP.showMessage("DEBUG(WORLDGEN): SPAWNING ENEMY", 0x00FF11, 5000);
            
            if (nx) addEnemies(x, p.getY(), false, p);
            if (ny) addEnemies(p.getX(), y, true, p);
            
            nextEnemyInterval = enemyStagger + Statc.intRandom(0, ENEMY_SPAWNRATE);
            enemyStagger++;
        } else {
            enemyStagger++;
        }
        
        if (n && garbleStagger >= nextGarbleInterval) {
            MSG_DISP.showMessage("DEBUG(WORLDGEN): SPAWNING GARBLE", 0x00FF11,5000);
            
            if (nx) addGarbles(x, p.getY(), false);
            if (ny) addGarbles(p.getX(), y, true);
            
            nextGarbleInterval = garbleStagger + Statc.intRandom(0, GARBLE_SPAWNRATE);
            garbleStagger++;
        } else {
            garbleStagger++;
        }
    }
    
    private void addEnemies(int x, int y, boolean dir, Player p) {
        if (!dir) {
            y += Statc.intRandom(-sh / 2, sh / 2);
        } else {
            x += Statc.intRandom(-sw / 2, sw / 2);
        }
 
        AngryYoink yoink = new AngryYoink(x, y, Statc.intRandom(0, 1) == 0, p);
        yoink.setColor(Statc.intRandom(0xFF0000, 0xFF1010));
        yoink.setDamageMod(Statc.intRandom(1, 5));
        yoink.setKnockback(1);
        yoink.enableCollision();
        yoink.setMaxSpeed(7);
        yoink.getInventory().addItem(Item.ORBLAUNCHER.id, 1);
        yoink.getInventory().addItem(Item.PURPLEORB.id, Statc.intRandom(10, 75));
        yoink.getInventory().equip(0);
        addEntity(yoink);
    }

    private void addGarbles(int x, int y, boolean dir) {
        if (!dir) {
            y += Statc.intRandom(-sh / 2, sh / 2);
        } else {
            x += Statc.intRandom(-sw / 2, sw / 2);
        }

        Garble garb = new Garble(x, y, this);
        garb.enableCollision();
        addEntity(garb);
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
