package youretheyoinkreboot.core;

import com.amp.mathem.Statc;
import com.amp.pre.ABFrame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.core.gfx.SpriteSheet;
import youretheyoinkreboot.util.Key;
import youretheyoinkreboot.world.World;
import youretheyoinkreboot.world.entities.Camera;
import youretheyoinkreboot.world.entities.Sprite;

/**
 *
 * @author josh
 */
public class Main extends ABFrame {
    
    public final static String VERSION = "0.04";
    
    private Screen s;
    private SpriteSheet sheet;
    
    private final BufferedImage image = new BufferedImage(Screen.WIDTH / Screen.SCALE,
            Screen.HEIGHT / Screen.SCALE, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    private Key k;
    
    private World w;
    
    private Camera cam;
    
    public static void main(String[] args) {
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
        
        w = new World(s);
        
        cam = new Camera(0, 0, s, w);
        w.addEntity(cam);
        cam.hide();
        
        Sprite testSprite = new Sprite(0, 0, 2<<Screen.SHIFT, 2<<Screen.SHIFT, 2 + 0 * sheet.width, 1000, sheet, w) {
            @Override
            protected void tick() {
                this.maxSpeed = 20;
                if (k.w.isPressed()) thrustUp();
                else if (k.s.isPressed()) thrustDown();
                if (k.a.isPressed()) thrustLeft();
                else if (k.d.isPressed()) thrustRight();
                if (k.b.isPressed()) {
                    if (vx > 0) vx--;
                    else if (vx < 0) vx++;
                    if (vy > 0) vy--;
                    else if (vy < 0) vy++;
                } 
            }

            @Override
            protected void render(Screen s) {
                
            }
        };
        w.addEntity(testSprite);
        testSprite.show();
        cam.track(testSprite);
        
        int len = 200;
        for (int i = 0; i < len; i++) {
            Sprite sprite = new Sprite(Statc.intRandom(-100, 100), 
                Statc.intRandom(-100, 100), 8, 8, 4 + 0 * sheet.width, 10, sheet, w) {
                @Override
                protected void tick() {

                }

                @Override
                protected void render(Screen s) {

                }
                
            };
            w.addEntity(sprite);
            sprite.enableCollision();
        }
        testSprite.enableCollision();
    }

    @Override
    protected void tick() {
        f.requestFocusInWindow();
        
        w.tick();
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
        //UI
        grphcs.drawImage(image, 0, 0, f.getWidth(), f.getHeight(), f);
    }
    
}
