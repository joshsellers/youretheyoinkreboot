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
    public Button e = new Button(KeyEvent.VK_E);
    public Button i = new Button(KeyEvent.VK_I);
    public Button f4 = new Button(KeyEvent.VK_F4);
    public Button h = new Button(KeyEvent.VK_H);
    public Button f5 = new Button(KeyEvent.VK_F5);
    public Button q = new Button(KeyEvent.VK_Q);
    public Button r = new Button(KeyEvent.VK_R);
    public Button t = new Button(KeyEvent.VK_T);
    public Button y = new Button(KeyEvent.VK_Y);
    public Button u = new Button(KeyEvent.VK_U);
    public Button o = new Button(KeyEvent.VK_O);
    public Button p = new Button(KeyEvent.VK_P);
    public Button f = new Button(KeyEvent.VK_F);
    public Button g = new Button(KeyEvent.VK_G);
    public Button j = new Button(KeyEvent.VK_J);
    public Button k = new Button(KeyEvent.VK_K);
    public Button l = new Button(KeyEvent.VK_L);
    public Button z = new Button(KeyEvent.VK_Z);
    public Button x = new Button(KeyEvent.VK_X);
    public Button c = new Button(KeyEvent.VK_C);
    public Button v = new Button(KeyEvent.VK_V);
    public Button n = new Button(KeyEvent.VK_N);
    public Button m = new Button(KeyEvent.VK_M);
    public Button comma = new Button(KeyEvent.VK_COMMA);
    public Button colon = new Button(KeyEvent.VK_SEMICOLON);
    public Button enter = new Button(KeyEvent.VK_ENTER);
    public Button f6 = new Button(KeyEvent.VK_F6);
    public Button zero = new Button(KeyEvent.VK_0);
    public Button one = new Button(KeyEvent.VK_1);
    public Button two = new Button(KeyEvent.VK_2);
    public Button three = new Button(KeyEvent.VK_3);
    public Button four = new Button(KeyEvent.VK_4);
    public Button five = new Button(KeyEvent.VK_5);
    public Button six = new Button(KeyEvent.VK_6);
    public Button seven = new Button(KeyEvent.VK_7);
    public Button eight = new Button(KeyEvent.VK_8);
    public Button nine = new Button(KeyEvent.VK_9);
    public Button space = new Button(KeyEvent.VK_SPACE);
    public Button backspace = new Button(KeyEvent.VK_BACK_SPACE);
    public Button escape = new Button(KeyEvent.VK_ESCAPE);
    
    public Key() {
        buttons = new Button[] {w, a, s, d, b, f3, e, i, f4, h, f5, q, r, t, y, u,
                                o, p, f, g, j, k, l, z, x, c, v, n, m, comma, 
                                colon, enter, f6, zero, one, two, three, four,
                                five, six, seven, eight, nine, space, backspace,
                                escape};
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
                getKeyToggleListeners().forEach((k) -> {
                    k.keyToggled(button.keyCode);
                });
                break;
            }
        }
    }
    
    public class Button {
        public final int keyCode;
        public final String charVal;
        private boolean down;
        
        public Button(int keyCode) {
            this.keyCode = keyCode;
            charVal = KeyEvent.getKeyText(keyCode);
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
