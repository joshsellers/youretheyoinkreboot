package youretheyoinkreboot.core;

import com.amp.pre.ABFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.core.gfx.SpriteSheet;
import youretheyoinkreboot.util.Key;
import youretheyoinkreboot.world.World;

/**
 *
 * @author josh
 */
public class Main extends ABFrame {
    
    public final static String VERSION = "0.01";
    
    private Screen s;
    private SpriteSheet sheet;
    
    private final BufferedImage image = new BufferedImage(Screen.WIDTH / Screen.SCALE,
            Screen.HEIGHT / Screen.SCALE, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    private Key k;
    
    private World w;
    
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
    }

    @Override
    protected void tick() {
        f.requestFocusInWindow();
        
        w.tick();
        
        if (k.w.isPressed()) s.setOffset(s.getXOffset(), s.getYOffset()-1);
        else if (k.s.isPressed()) s.setOffset(s.getXOffset(), s.getYOffset()+1);
        else if (k.a.isPressed()) s.setOffset(s.getXOffset()-1, s.getYOffset());
        else if (k.d.isPressed()) s.setOffset(s.getXOffset()+1, s.getYOffset());
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
