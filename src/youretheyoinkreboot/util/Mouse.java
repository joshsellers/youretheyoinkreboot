package youretheyoinkreboot.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import youretheyoinkreboot.core.gfx.Screen;

/**
 *
 * @author josh
 */
public class Mouse implements MouseListener, MouseMotionListener {
    
    private Screen s;
    
    private int currentX;
    private int currentY;
    
    private int lastClickX;
    private int lastClickY;
    
    private int lastClickButton;
    
    private long lastClickTime;
    
    public Mouse(Screen s) {
        this.s = s;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastClickTime = System.currentTimeMillis();
        lastClickX = e.getX() / Screen.SCALE;
        lastClickY = e.getY() / Screen.SCALE;
        lastClickButton = e.getButton();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currentX = e.getX() / Screen.SCALE;
        currentY = e.getY() / Screen.SCALE;
    }
    
    public int[] currentCoordsOnScreen() {
        return new int[] {currentX, currentY};
    }
    
    public int[] lastClickCoordsOnScreen() {
        return new int[] {lastClickX, lastClickY};
    }
    
    public int[] currentCoordsInWorld() {
        return new int[] {currentX + s.getXOffset(), currentY + s.getYOffset()};
    }
    
    public int[] lastClickCoordsInWorld() {
        return new int[] {lastClickX + s.getXOffset(), lastClickY + s.getYOffset()};
    }
    
    public long lastTimeClicked() {
        return lastClickTime;
    }
    
    public int lastClickedButton() {
        return lastClickButton;
    }
    
    public Screen getScreen() {
        return s;
    }
}
