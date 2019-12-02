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
import youretheyoinkreboot.world.entities.Yoink;

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
                            Yoink testEnemy = new Yoink("ANGRYBOI", Statc.intRandom(p.getX() - 1000, p.getX() + 1000), Statc.intRandom(p.getY() - 1000, p.getY() + 1000), 16, 0, w) {
                                @Override
                                protected void tick() {
                                    this.maxSpeed = 2;
                                    boolean up = false;
                                    boolean down = false;
                                    boolean left = false;
                                    boolean right = false;
                                    if (p.getX() + p.getWidth() < this.x) {
                                        thrustLeft();
                                        left = true;
                                    } else if (p.getX() > this.x) {
                                        thrustRight();
                                        right = true;
                                    }
                                    if (p.getY() + p.getHeight() < this.y) {
                                        thrustUp();
                                        up = true;
                                    } else if (p.getY() > this.y) {
                                        thrustDown();
                                        down = true;
                                    }

                                    if (up && left) {
                                        movingDir = 0;
                                    } else if (up && right) {
                                        movingDir = 2;
                                    } else if (down && right) {
                                        movingDir = 4;
                                    } else if (down && left) {
                                        movingDir = 6;
                                    } else if (up) {
                                        movingDir = 1;
                                    } else if (right) {
                                        movingDir = 3;
                                    } else if (down) {
                                        movingDir = 5;
                                    } else if (left) {
                                        movingDir = 7;
                                    }
                                }

                                private int damageStagger = 0;

                                @Override
                                protected void onCollision(Entity with) {
                                    if (!with.id.equals(this.id) && damageStagger % 4 == 0) {
                                        with.damage(damageMod, this);
                                    }
                                    damageStagger++;
                                }
                            };

                            testEnemy.setColor(Statc.intRandom(0xFF0000, 0xFF1010));
                            testEnemy.setDamageMod(Statc.intRandom(1, 5));
                            testEnemy.setKnockback(1);
                            testEnemy.enableCollision();
                            w.addEntity(testEnemy);
                        }
                        UIControl.MSG_DISP.showMessage("DEBUG(CMD): spawned " + times + " angryboi(s)", 0x00FF00, 5000);
                        break;
                }
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
