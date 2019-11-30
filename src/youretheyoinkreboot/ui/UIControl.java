package youretheyoinkreboot.ui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josh
 */
public class UIControl {
    private static final List<UIObject> uiobjects = new ArrayList<>();
    
    public static final UINotificationDisplay MESSAGE_DISP = new UINotificationDisplay(10, 20);;
    
    public static void tick() {
        for (UIObject obj : getUIObjects()) {
            if (obj.isActive()) obj.tick();
        }
    }
    
    public static void draw(Graphics g) {
        for (UIObject obj : getUIObjects()) {
            if (obj.isActive() && obj.isVisible()) obj.draw(g);
        }
        MESSAGE_DISP.draw(g);
    }
    
    public static synchronized List<UIObject> getUIObjects() {
        return uiobjects;
    }
    
    public static synchronized void addUIObject(UIObject obj) {
        getUIObjects().add(obj);
    }
}
