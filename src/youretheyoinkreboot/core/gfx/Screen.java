package youretheyoinkreboot.core.gfx;

/**
 *
 * @author josh
 */
public class Screen {

    public static int WIDTH = 900;
    public static int HEIGHT = 600;
    public final static int SCALE = 2;
    
    public final static int SHIFT = 3;
    
    public static final byte BIT_MIRROR_X = 0b01;
    public static final byte BIT_MIRROR_Y = 0b10;

    public final int[] pixels;

    public final SpriteSheet sheet;

    private int xOffset = 0;
    private int yOffset = 0;

    public Screen(SpriteSheet sheet) {
        pixels = new int[(WIDTH / SCALE) * (HEIGHT / SCALE)];
        this.sheet = sheet;
    }

    public void render(int xPos, int yPos, int tile, int mirrorDir, int hue, int scale) {
        xPos -= xOffset;
        yPos -= yOffset;

        boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
        boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;

        int scaleMap = scale - 1;
        int xTile = tile % ((sheet.width));
        int yTile = tile / ((sheet.width));
        int tileOffset = (xTile << SHIFT) + (yTile << SHIFT) * (sheet.width * SpriteSheet.TILE_SIZE);
        for (int y = 0; y < SpriteSheet.TILE_SIZE; y++) {
            int ySheet = y;
            if (mirrorY) {
                ySheet = (SpriteSheet.TILE_SIZE - 1) - y;
            }

            int yPixel = y + yPos + (y * scaleMap) - ((scaleMap << SHIFT) / 2);

            for (int x = 0; x < SpriteSheet.TILE_SIZE; x++) {
                int xSheet = x;
                if (mirrorX) {
                    xSheet = (SpriteSheet.TILE_SIZE - 1) - x;
                }
                int xPixel = x + xPos + (x * scaleMap) - ((scaleMap << SHIFT) / 2);
                int col = sheet.pixels[xSheet + ySheet * (sheet.width * SpriteSheet.TILE_SIZE) + tileOffset];
                if (col < 255) {
                    for (int yScale = 0; yScale < scale; yScale++) {
                        if (yPixel + yScale < 0 || yPixel + yScale >= HEIGHT / SCALE) {
                            continue;
                        }
                        for (int xScale = 0; xScale < scale; xScale++) {
                            if (xPixel + xScale < 0 || xPixel + xScale >= WIDTH / SCALE) {
                                continue;
                            }
                            if (col != 0xFF000001) {
                                pixels[(xPixel + xScale) + (yPixel + yScale) * (WIDTH / SCALE)] = col | hue;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }
}
