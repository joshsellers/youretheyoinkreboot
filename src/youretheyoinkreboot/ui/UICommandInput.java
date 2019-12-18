package youretheyoinkreboot.ui;

import com.amp.mathem.Statc;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import youretheyoinkreboot.core.Main;
import youretheyoinkreboot.util.Key;
import youretheyoinkreboot.util.KeyToggleListener;
import youretheyoinkreboot.world.World;
import youretheyoinkreboot.world.entities.Entity;
import youretheyoinkreboot.world.entities.Player;
import youretheyoinkreboot.world.entities.AngryYoink;
import youretheyoinkreboot.world.items.Item;

/**
 *
 * @author joshsellers
 */
public class UICommandInput extends UIObject implements KeyToggleListener {
    
    private final World w;
    private final Player p;
    private final Key k;
    
    private String currentCommand = "";

    public UICommandInput(int x, int y, Player p) {
        super((byte) 0x04, x, y);
        this.p = p;
        this.w = p.getWorld();
        this.k = p.getKeyboardInterface();
        
        this.k.addKeyToggleListener(this);
    }
    
    private void processCommand() {
        String command = currentCommand;
        String[] args = {"NULL"};
        if (currentCommand.contains(":")) {
            command = currentCommand.split(":")[0];
            args = currentCommand.split(":")[1].split(",");
        }
                
        switch(command) {
            case "SPAWN":
                switch (args[0]) {
                    case "ANGRYBOI":
                        int times = 1;
                        if (args.length > 1) times = Integer.parseInt(args[1]);
                        for (int i = 0; i < times; i++) {
                            AngryYoink testEnemy = new AngryYoink(Statc.intRandom(p.getX() - 1000, p.getX() + 1000), Statc.intRandom(p.getY() - 1000, p.getY() + 1000), Statc.intRandom(0, 1) == 0, p);

                            testEnemy.setColor(Statc.intRandom(0xFF0000, 0xFF1010));
                            testEnemy.setDamageMod(Statc.intRandom(1, 5));
                            testEnemy.setKnockback(1);
                            testEnemy.enableCollision();
                            testEnemy.setMaxSpeed(7);
                            testEnemy.getInventory().addItem(Item.ORBLAUNCHER.id, 1);
                            testEnemy.getInventory().addItem(Item.PURPLEORB.id, Statc.intRandom(0, 15));
                            testEnemy.getInventory().equip(0);
                            w.addEntity(testEnemy);
                        }
                        UIControl.MSG_DISP.showMessage("DEBUG(CMD): spawned " + times + " angryboi(s)", 0x00FF00, 5000);
                        break;
                }
                break;
            case "PLAYER":
                switch (args[0]) {
                    case "HEAL":
                        int by = p.getMaxHP() - p.getHP();
                        if (args.length > 1) {
                            by = Integer.parseInt(args[1]);
                        }
                        p.addHP(by);
                        break;
                    case "KILL":
                        currentCommand = "KILL:PLAYER";
                        processCommand();
                        return;
                    case "SETMAXHP": 
                        if (args.length > 1) {
                            p.setMaxHitPoints(Integer.parseInt(args[1]));
                        } else {
                            UIControl.MSG_DISP.showMessage("DEBUG(CMD): SETMAXHP failed; missing arg 2", 0xFF0000, 5000);
                        }
                        break;
                    case "SETMAXSPEED":
                        if (args.length > 1) {
                            p.setMaxSpeed(Integer.parseInt(args[1]));
                        } else {
                            UIControl.MSG_DISP.showMessage("DEBUG(CMD): SEMAXSPEED failed; missing arg 2", 0xFF0000, 5000);
                        }
                        break;
                }
                break;
            case "KILL":
                switch (args[0]) {
                    case "ALL":
                        w.remover = (e) -> {
                            return e.isActive() && !e.id.contains("PLAYER") && !e.id.contains("CAM");
                        };
                        break;
                    case "PLAYER":
                        p.damage(p.getMaxHP(), p);
                        break;
                }
                break;
            case "GIVE":
                int id = -1;
                int c = 1;
                if (args.length > 1) c = Integer.parseInt(args[1]);
                for (Item i : Item.ITEMS) if (i.name.equalsIgnoreCase(args[0])) {
                    id = i.id;
                    break;
                }
                if (id == -1) id = Integer.parseInt(args[0]);
                if (id != -1) {
                    p.getInventory().addItem(id, c);
                    UIControl.MSG_DISP.showMessage("DEBUG(CMD): player given " + Item.ITEMS[id].name, 0x00FF00, 5000);
                    break;
                }
                UIControl.MSG_DISP.showMessage("DEBUG(CMD): GIVE failed; invalid arguments", 0xFF0000, 5000);
                break;
            default: 
                UIControl.MSG_DISP.showMessage("DEBUG(CMD): unrecognized command", 0xFF0000, 5000);
                break;
        }
        int argCount = args.length;
        if (argCount == 1 && args[0].equals("NULL")) argCount = 0; 
        UIControl.MSG_DISP.showMessage("DEBUG(CMD): executed: " + command + " # args: " + argCount, 0x00FF00, 5000);

        currentCommand = "";
    }

    @Override
    protected void tick() {
    }

    @Override
    protected void draw(Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(currentCommand);

        g.setColor(Color.gray);
        g.fillRect((x - (textWidth / 2)) - 1, y - 1, textWidth + 6, 16);
        g.setColor(Color.black);
        g.fillRect(x - (textWidth / 2), y, textWidth + 4, 14);
        g.setColor(Color.green);
        g.drawString(currentCommand, (x - textWidth / 2) + 2, y + 12);
    }

    @Override
    public void keyToggled(int keyCode) {
        if (keyCode == k.f6.keyCode) {
            if (this.isVisible()) {
                Main.pause = false;
                this.hide();
                currentCommand = "";
            } else if (!this.isVisible()) {
                Main.pause = true;
                this.show();
            }
        }
        
        if (isVisible() && keyCode == k.escape.keyCode) {;
            currentCommand = "";
            this.hide();
        }

        if (this.isVisible()) {
            String keyChar = KeyEvent.getKeyText(keyCode).toUpperCase();
            if (keyChar.matches("\\w")) {
                currentCommand += keyChar;
            } 
            else if (keyCode == KeyEvent.VK_SPACE) currentCommand += " ";
            else if (keyCode == KeyEvent.VK_SEMICOLON) currentCommand += ":";
            else if (keyCode == KeyEvent.VK_COMMA) currentCommand += ",";
            else if (keyCode == KeyEvent.VK_ENTER) {
                processCommand();
                Main.pause = false;
                hide();
            }
            else if (keyCode == KeyEvent.VK_BACK_SPACE && currentCommand.length() > 0) {
                currentCommand = currentCommand.substring(0, currentCommand.length() - 1);
            }
        }
    }
    
}
