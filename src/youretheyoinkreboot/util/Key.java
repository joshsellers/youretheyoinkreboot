package youretheyoinkreboot.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author josh
 */
public class Key implements KeyListener {
    
    private final Button[] buttons;
    
    public Button w = new Button(KeyEvent.VK_W);
    public Button a = new Button(KeyEvent.VK_A);
    public Button s = new Button(KeyEvent.VK_S);
    public Button d = new Button(KeyEvent.VK_D);
    public Button b = new Button(KeyEvent.VK_B);
    
    public Key() {
        buttons = new Button[] {w, a, s, d, b};
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        for (Button button : buttons) {
            if (e.getKeyCode() == button.keyCode) {
                button.down = true;
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        for (Button button : buttons) {
            if (e.getKeyCode() == button.keyCode) {
                button.down = false;
                break;
            }
        }
    }
    
    public class Button {
        public final int keyCode;
        private boolean down;
        
        public Button(int keyCode) {
            this.keyCode = keyCode;
        }
        
        public boolean isPressed() {
            return down;
        }
    }
    
}
