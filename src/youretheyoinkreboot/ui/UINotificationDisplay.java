package youretheyoinkreboot.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author josh
 */
public class UINotificationDisplay extends UIObject {
    
    private final static int MAX_DISPLAYED_MESSAGES = 10;
    private final Message[] messages = new Message[MAX_DISPLAYED_MESSAGES];
    
    private final Timer messageTimer = new Timer();
    
    private Color col = Color.white;
    
    public boolean showDebug = true;

    public UINotificationDisplay(int x, int y) {
        super((byte) 0x03, x, y);
    }

    @Override
    protected void tick() {

    }

    @Override
    protected void draw(Graphics g) {
        int j = 0;
        for (Message m : messages) {
            if (m != null && m.isActive() && (!m.msg.contains("ERROR") ^ showDebug)) {
                g.setColor(col);
                g.drawString(m.msg, x, y + (12 * j));
                j++;
            }
        }
    }
    
    public void showMessage(String msg, long dispTime) {
        for (int i = 1; i < messages.length; i++) {
            Message m = messages[i];
            messages[i-1] = m;
        }
        messages[messages.length-1] = new Message(msg, dispTime);
        Message newMsg = messages[messages.length-1];
        messageTimer.schedule(newMsg, newMsg.lifeTime);
    }
    
    public class Message extends TimerTask {
        public final String msg;
        public final long lifeTime;
        
        private boolean active = true;
        
        public Message(String msg, long lifeTime) {
            this.msg = msg;
            this.lifeTime = lifeTime;
        }

        @Override
        public void run() {
            active = false;
        }
        
        public boolean isActive() {
            return active;
        }
    }
    
    public void setColor(int col) {
        this.col = new Color(col);
    }
    
    public void setColor(Color col) {
        this.col = col;
    }
    
}
