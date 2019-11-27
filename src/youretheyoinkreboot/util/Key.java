package youretheyoinkreboot.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josh
 */
public class Key implements KeyListener {
    
    private final List<KeyToggleListener> ktl = new ArrayList<>();
    
    private final Button[] buttons;
    
    public Button w = new Button(KeyEvent.VK_W);
    public Button a = new Button(KeyEvent.VK_A);
    public Button s = new Button(KeyEvent.VK_S);
    public Button d = new Button(KeyEvent.VK_D);
    public Button b = new Button(KeyEvent.VK_B);
    public Button f3 = new Button(KeyEvent.VK_F3);
    
    public Key() {
        buttons = new Button[] {w, a, s, d, b, f3};
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
                for (KeyToggleListener k : getKeyToggleListeners()) {
                    k.keyToggled(button.keyCode);
                }
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
    
    public synchronized List<KeyToggleListener> getKeyToggleListeners() {
        return ktl;
    }
    
    public synchronized void addKeyToggleListener(KeyToggleListener k) {
        this.getKeyToggleListeners().add(k);
    }
    
}
