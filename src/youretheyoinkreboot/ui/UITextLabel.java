package youretheyoinkreboot.ui;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author josh
 */
public class UITextLabel extends UIObject {
    protected String text = "";
    protected Color col = Color.white;

    public UITextLabel(String text, int x, int y) {
        super((byte) 0x01, x, y);
        this.text = text;
    }

    @Override
    protected void tick() {

    }

    @Override
    protected void draw(Graphics g) {
        g.setColor(col);
        g.drawString(text, x, y);
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public String getText() {
        return text;
    }
    
    public void setColor(int col) {
        this.col = new Color(col);
    }
    
}
