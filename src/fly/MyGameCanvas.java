package fly;


import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class MyGameCanvas extends GameCanvas
        implements Runnable, CommandListener {
    private static MyGameCanvas instance;
    Graphics g;
    boolean running;
    Thread t;
    Command startcmd, exitcmd, restartcmd;
    int keystate;
    boolean keyevent;
    boolean key_up, key_down, key_left, key_right, key_fire;
    private boolean allowinput;
    public int screenwidth;
    public int screenheight;
    boolean gameover;
    //define your variable here
    long gametimeoffset;
    long gametime;
    int bombnum;
    int[] bombaward;
    int bombawardtop;
    GameObject plane;
    int planedirection;
    TiledLayer background;
    Bullets bullets;
    GameObject explosion;
    GameObject bomb;
    Image bomb_ico;
//    Font fontbig,fontsmall;

    protected MyGameCanvas() {
        super(true);
        setFullScreenMode(true);
        g = getGraphics();
        running = false;
        t = null;
        addCommand(startcmd = new Command("开始", Command.OK, 1));
        addCommand(exitcmd = new Command("退出", Command.EXIT, 1));
        setCommandListener(this);
        screenwidth = getWidth();
        screenheight = getHeight();

        Image img = ImageTools.getImage("/pic/MyPlaneFrames.png");
        plane = new GameObject(img, 87, 68);
        planedirection = 0;
        img = ImageTools.getImage("/pic/back_water.png");
        int backcolumns = screenwidth / img.getWidth() + 1;
        int backrows = screenheight / img.getHeight() + 1;
        background = new TiledLayer(backcolumns, backrows, img, img.getWidth(), img.getHeight());
        int x, y;
        for (int i = 0; i < backcolumns * backrows; i++) {
            x = i % backcolumns;
            y = i / backcolumns;
            background.setCell(x, y, 1);
        }
        img = ImageTools.getImage("/pic/bullet.png");
        bullets = new Bullets(img, img.getWidth(), img.getHeight(), 20, screenwidth, screenheight);
        img = ImageTools.getImage("/pic/explosion.png");
        explosion = new GameObject(img, 96, 96);
        bomb_ico = ImageTools.getImage("/pic/icon_bomb.png");
        img = ImageTools.getImage("/pic/b_number.png");
//        fontbig=new Font(g,img,10,15,new char[]{'0','1','2','3','4','5','6','7','8','9'});
        img = ImageTools.getImage("/pic/s_number.png");
//        fontsmall=new Font(g,img,5,7,new char[]{'0','1','2','3','4','5','6','7','8','9'});
        img = ImageTools.getImage("/pic/explosion.png");
        bomb = new GameObject(img, 96, 96);
        bombaward = new int[]{0, 1, 1, 1, 1, 1};
        bombawardtop = bombaward.length - 1;
    }

    synchronized public static MyGameCanvas getInstance() {
        if (instance == null) {
            instance = new MyGameCanvas();
        }
        return instance;
    }

    public void run() {
        long st = 0, et = 0, diff = 0;
        int rate = 50;
        while (running) {
            st = System.currentTimeMillis();
            gameinput();
            gameMain();
            et = System.currentTimeMillis();
            diff = et - st;
            if (diff < rate) {
                try {
                    Thread.sleep(rate - diff);
                } catch (InterruptedException ex) {
                }
            }
        }
        System.out.println("MyGameCanvas run end");
    }

    public void start() {
        if (!running) {
            running = true;
            t = new Thread(this);
            t.start();
        }
    }

    private void gameMain() {
        g.setColor(0, 0, 0);//clear screen
        g.fillRect(0, 0, getWidth(), getHeight());

        background.paint(g);//draw background
        if (bomb.alive) {
            bomb.moveto(plane.sprite.getX(), plane.sprite.getY());
            bomb.paint(g);
            bomb.update();
            bullets.killbullets(plane.sprite, 96);
        }
        bullets.paint(g);
        plane.paint(g);
        bullets.refreshBullets(plane.sprite, !gameover && !bomb.alive);
        g.drawImage(bomb_ico, 0, screenheight - 1, g.BOTTOM | g.LEFT);
//        fontbig.drawString(String.valueOf(gametime),screenwidth/2-15,10);
//        fontsmall.drawString(String.valueOf(bombnum),bomb_ico.getWidth(),screenheight-fontsmall.height);
        if (gameover) {
            explosion.paint(g);
            explosion.update();
            if (!explosion.alive) {
                plane.alive = false;
                g.setColor(255, 255, 255);
                g.drawString(StringTools.timeOpinion(gametime), 5, 22, g.LEFT | g.TOP);
            }
        } else {
            gametime = (System.currentTimeMillis() - gametimeoffset) / 1000;
            int awardindex = (int) gametime / 20;
            if (awardindex > bombawardtop)
                awardindex = bombawardtop;
            if (bombaward[awardindex] != 0) {
                bombnum += bombaward[awardindex];
                bombaward[awardindex] = 0;
            }
            if (keyevent) {
                if (key_up) {
                    plane.move(0, -6);
                    plane.sprite.setFrame(0);
                }
                if (key_down) {
                    plane.move(0, 6);
                    plane.sprite.setFrame(0);
                }
                if (key_left) {
                    plane.move(-6, 0);
                    plane.sprite.setFrame(1);
                }
                if (key_right) {
                    plane.move(6, 0);
                    plane.sprite.setFrame(2);
                }
                if (key_fire) {
                    if (!bomb.alive && bombnum > 0) {//bomb isn't actived and there's enough bomb .
                        bomb.reset();
                        bomb.alive = true;
                        bombnum--;
                    }
                }
            } else {
                plane.sprite.setFrame(0);
            }

        }


        flushGraphics();
    }

    private void gameInit() {
        gameover = false;
        gametime = 0;
        gametimeoffset = System.currentTimeMillis();
        allowinput = true;
        key_up = key_down = key_left = key_right = key_fire = false;
        plane.moveto((screenwidth - plane.sprite.getWidth()) / 2,
                (screenheight - plane.sprite.getHeight()) / 2);
        bullets.initBullets();
        plane.reset();
        explosion.reset();
        explosion.lifetime = 3;
        bomb.reset();
        bomb.lifetime = 6;
        bomb.alive = false;
        bombnum = 3;
        for (int i = 0; i < bombaward.length; i++) {
            bombaward[i] = 1;
        }
        bombaward[0] = 0;
    }

    public void stop() {
        if (running) {
            running = false;
        }
    }

    public void StartGame() {
        gameInit();
        start();
        removeCommand(startcmd);
        addCommand(restartcmd = new Command("重新开始", Command.OK, 1));
    }

    public void commandAction(Command c, Displayable d) {
        String cmdstr = c.getLabel();
        if (cmdstr.equals("开始")) {
            gameInit();
            start();
            removeCommand(startcmd);
            addCommand(restartcmd = new Command("重新开始", Command.OK, 1));
        } else if (cmdstr.equals("重新开始")) {
            stop();
            while (t.isAlive()) ;
            gameInit();
            start();
        } else if (cmdstr.equals("退出")) {
            stop();
            Navigate.midlet.destroyApp(false);
            Navigate.midlet.notifyDestroyed();
        }
    }

    private void gameinput() {
        if (allowinput) {
            keystate = getKeyStates();
            keyevent = false;
            if ((keystate & UP_PRESSED) != 0) {//up
                key_up = true;
                keyevent = true;
                planedirection = 1;
            } else if ((keystate & UP_PRESSED) == 0) {//release key
                if (key_up == true) {
                    key_up = false;
                }
            }

            if ((keystate & DOWN_PRESSED) != 0) {//down
                key_down = true;
                keyevent = true;
                planedirection = 2;
            } else if ((keystate & DOWN_PRESSED) == 0) {//release key
                if (key_down == true) {
                    key_down = false;
                }
            }

            if ((keystate & LEFT_PRESSED) != 0) {//left
                key_left = true;
                keyevent = true;
                planedirection = 3;
            } else if ((keystate & LEFT_PRESSED) == 0) {//release key
                if (key_left == true) {
                    key_left = false;
                }
            }

            if ((keystate & RIGHT_PRESSED) != 0) {//right
                key_right = true;
                keyevent = true;
                planedirection = 4;
            } else if ((keystate & RIGHT_PRESSED) == 0) {//release key
                if (key_right == true) {
                    key_right = false;
                }
            }

            if ((keystate & FIRE_PRESSED) != 0) {//fire
                key_fire = true;
                keyevent = true;
                planedirection = 0;
            } else if ((keystate & FIRE_PRESSED) == 0) {//release key
                if (key_fire == true) {
                    key_fire = false;
                }
            }
            if (!keyevent) {

            }
        }
    }

    public static void cleanJob() {
        instance = null;
    }


}