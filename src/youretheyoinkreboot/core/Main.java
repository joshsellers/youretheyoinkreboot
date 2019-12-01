package youretheyoinkreboot.core;

import com.amp.mathem.Statc;
import com.amp.pre.ABFrame;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.core.gfx.SpriteSheet;
import youretheyoinkreboot.ui.UIControl;
import static youretheyoinkreboot.ui.UIControl.MSG_DISP;
import youretheyoinkreboot.ui.UIInventoryInterface;
import youretheyoinkreboot.ui.UITextLabel;
import youretheyoinkreboot.util.Key;
import youretheyoinkreboot.util.KeyToggleListener;
import youretheyoinkreboot.util.Mouse;
import youretheyoinkreboot.world.World;
import youretheyoinkreboot.world.entities.Camera;
import youretheyoinkreboot.world.entities.Entity;
import youretheyoinkreboot.world.entities.Player;
import youretheyoinkreboot.world.entities.Sprite;
import youretheyoinkreboot.world.entities.Yoink;
import youretheyoinkreboot.world.items.DroppedItem;
import youretheyoinkreboot.world.items.Item;

/**
 *
 * @author josh
 */
public class Main extends ABFrame implements KeyToggleListener {
    
    /* TODO by v0.1:
    *  - add command interface
    *  - set Player.movingDir based on mouse coords when aiming proj. item
    *  - fix camera pos before player moves
    *  - add enemies
    */
    public final static String VERSION = "0.096";
    
    private Screen s;
    private SpriteSheet sheet;
    
    private final BufferedImage image = new BufferedImage(Screen.WIDTH / Screen.SCALE,
            Screen.HEIGHT / Screen.SCALE, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    private Key k;
    private Mouse m;
    
    private World w;
    private Camera cam;
    private Player p;
    private UIInventoryInterface ii;
    
    private UITextLabel fpsMeter;
    private UITextLabel camCoords;
    private UITextLabel playerInfo;
    private UITextLabel particleCount;
    
    private boolean showDebug;
    
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Screen.WIDTH = (int) (screenSize.width);
        Screen.HEIGHT = (int) (screenSize.height) - 35;
        new Main(60, "", true, true, 1.0f, Screen.WIDTH, Screen.HEIGHT, 1, 
            false).start();
    }

    public Main(int MAX_TICK_SPEED, String NAME, boolean resizeable, 
            boolean decorated, float opacity, int WIDTH, int HEIGHT, 
            int bufferStrategy, boolean useDebug) {
        super(MAX_TICK_SPEED, NAME, resizeable, decorated, opacity, WIDTH, 
            HEIGHT, bufferStrategy, useDebug);
    }
    
    @Override
    public void init() {
        sheet = new SpriteSheet();
        s = new Screen(sheet);
        
        k = new Key();
        f.addKeyListener(k);
        k.addKeyToggleListener(this);
        m = new Mouse(s);
        c.addMouseListener(m);
        c.addMouseMotionListener(m);
        
        w = new World(s);
        
        cam = new Camera(0, 0, s, w);
        w.addEntity(cam);
        cam.hide();
        
        p = new Player(0, -120, k, m, w);
        p.enableCollision();
        w.addEntity(p);
        cam.track(p);
        cam.updateScreen();
    
        int len = 400;
        for (int i = 0; i < len; i++) {
            Sprite sprite = new Sprite("PURPLEORB", Statc.intRandom(-1000, 1000), 
                Statc.intRandom(-1000, 1000), 8, 8, 4 + 0 * sheet.width, w) {
                @Override
                protected void tick() {
                    this.resistance = 1f;
                }

                @Override
                protected void render(Screen s) {

                }
                
                @Override
                protected void onCollision(Entity with) {
                    damage(10, with);
                }
                
                @Override
                protected void onDie(Entity source) {
                    w.addEntity(new DroppedItem(x, y, Item.PURPLEORB, w));
                    if (Statc.intRandom(0, 5) == 0) {
                        w.addEntity(new DroppedItem(x, y + 10, Item.RAINBOWSHARD, w));
                    }
                }

            };
            w.addEntity(sprite);
            sprite.enableCollision();
        }
        
        fpsMeter = new UITextLabel(fps, 10, 20);
        UIControl.addUIObject(fpsMeter);
        camCoords = new UITextLabel("CAM X: " + cam.getX() + " CAM Y: " + cam.getY(), 10, 32);
        UIControl.addUIObject(camCoords);
        playerInfo = new UITextLabel("player", 10, 44);
        UIControl.addUIObject(playerInfo);
        particleCount = new UITextLabel("particles", 10, 56);
        UIControl.addUIObject(particleCount);
        
        ii = new UIInventoryInterface(((Screen.WIDTH / Screen.SCALE) / 2) - 10, 10, p.getInventory(), m);
        ii.hide();
        UIControl.addUIObject(ii);
        
        p.getInventory().addItem(Item.TPLAUNCHER.id, 1);
    }

    @Override
    protected void tick() {
        f.requestFocusInWindow();
        
        w.tick();
        
        UIControl.tick();
    }

    @Override
    protected void draw(Graphics grphcs) {
        w.render(s);
        
        for (int y = 0; y < Screen.HEIGHT / Screen.SCALE; y++) {
            for (int x = 0; x < Screen.WIDTH / Screen.SCALE; x++) {
                int colorCode = s.pixels[x + y * (Screen.WIDTH / Screen.SCALE)];
                if (colorCode < 255) {
                    pixels[x + y * image.getWidth()] = colorCode;
                }
            }
        }
        
        Graphics g = image.getGraphics();
        
        if (showDebug) {
            fpsMeter.show();
            camCoords.show();
            playerInfo.show();
            particleCount.show();
        }
        else {
            fpsMeter.hide();
            camCoords.hide();
            playerInfo.hide();
            particleCount.hide();
        }
        fpsMeter.setText(fps + " FPS");
        camCoords.setText("CAM X: " + cam.getX() + " Y: " + cam.getY());
        playerInfo.setText("PLAYER X: " + p.getX() + " Y: " + p.getY() + " VELOCITY: " + ((int) p.getVelocity()));
        particleCount.setText(String.valueOf("PARTICLE COUNT: " + w.getParticleHandler().getParticleCount()));
        UIControl.draw(g);
        grphcs.drawImage(image, 0, 0, f.getWidth(), f.getHeight(), f);
    }

    @Override
    public void keyToggled(int keyCode) {
        if (keyCode == KeyEvent.VK_F3) showDebug = !showDebug;
        if (keyCode == k.i.keyCode) {
            ii.toggle();
        }
        
        if (keyCode == KeyEvent.VK_F4) {
            MSG_DISP.showDebug = !MSG_DISP.showDebug;
        }
    }
    
}
