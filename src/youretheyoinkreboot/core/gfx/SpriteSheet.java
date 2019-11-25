package youretheyoinkreboot.core.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import youretheyoinkreboot.core.Main;

/**
 *
 * @author josh
 */
public class SpriteSheet {
    
    private BufferedImage sheet;
    
    public int width;
    public int height;
    
    public static final int TILE_SIZE = 8;
    
    public static final String filePath = "res/spritesheet.png";
    
    public int[] pixels;
    
    public SpriteSheet() {
        try {
            sheet = ImageIO.read(Main.class.getResource(filePath).toURI().toURL());
            width = sheet.getWidth() / TILE_SIZE;
            height = sheet.getHeight() / TILE_SIZE;
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(SpriteSheet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        pixels = sheet.getRGB(0, 0, width * TILE_SIZE, height * TILE_SIZE, null, 0, width * TILE_SIZE);
    }
}
