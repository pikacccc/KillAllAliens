package fly;

import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.midlet.MIDlet;

public class MyGameCanvas extends GameCanvas implements Runnable, IRestartGame {
    private static MyGameCanvas instance;
    Graphics g;
    boolean running;
    Thread t;
    int keystate;
    boolean keyevent;
    boolean key_up, key_down, key_left, key_right, key_fire, key_fire2;
    private boolean allowinput;
    public int screenwidth;
    public int screenheight;
    boolean gameover;
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
    DrawNumberHandler number;

    Image bgPause;

    private boolean pause = false;
    private PausePannel pp;
    private FlyMidlet midlet;

    protected MyGameCanvas(FlyMidlet midlet) {
        super(false);
        this.midlet = midlet;
        setFullScreenMode(true);
        g = getGraphics();
        running = false;
        t = null;
        screenwidth = getWidth();
        screenheight = getHeight();
        pp = new PausePannel(midlet, this, screenwidth, screenheight);
        Image img = ImageTools.getImage("/pic/MyPlaneFrames.png");
        plane = new GameObject(img, 54, 42);
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
        img = Util.LoadImg("/pic/bullet.png");
        bullets = new Bullets(img, img.getWidth(), img.getHeight(), 20, screenwidth, screenheight);
        img = ImageTools.getImage("/pic/explosion.png");
        explosion = new GameObject(img, 96, 96);
        bomb_ico = ImageTools.getImage("/pic/icon_bomb.png");
        img = ImageTools.getImage("/pic/explosion.png");
        bomb = new GameObject(img, 96, 96);
        bombaward = new int[]{0, 1, 1, 1, 1, 1};
        bombawardtop = bombaward.length - 1;
        number = new DrawNumberHandler("/pic/number.png", 16, 24);

        bgPause = Util.LoadImg("/pic/bg_pause.png");
    }

    synchronized public static MyGameCanvas getInstance(FlyMidlet midlet) {
        if (instance == null) {
            instance = new MyGameCanvas(midlet);
        }
        return instance;
    }

    public void run() {
        long st = 0, et = 0, diff = 0;
        int rate = 50;
        while (running) {
            if (pause) continue;
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
    }

    public void start() {
        gameInit();
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
            if (!pause) {
                bomb.moveto(plane.sprite.getX() - 24, plane.sprite.getY() - 21);
                bomb.update();
                bullets.killbullets(plane.sprite, 96);
            }
            bomb.paint(g);
        }
        bullets.paint(g);
        plane.paint(g);
        bullets.refreshBullets(plane.sprite, !gameover && !bomb.alive, !pause);
        g.drawImage(bomb_ico, 15, screenheight - 15, g.BOTTOM | g.LEFT);
        number.ShowNumber(g, (int) gametime, screenwidth / 2 - 15, 50, AlignmentType.Center);
        number.ShowNumber(g, (int) bombnum, 80, screenheight - 30, AlignmentType.Left);
        if (pause) {
            pp.Draw(g);
            flushGraphics();
            return;
        }

        if (gameover) {
            explosion.paint(g);
            explosion.update();
            if (!explosion.alive) {
                plane.alive = false;
                g.setColor(255, 255, 255);
                g.drawString(StringTools.timeOpinion(gametime), 5, 22, g.LEFT | g.TOP);
                Navigate.OpenGameOver();
                Navigate.CloseGame();
            }
        } else {
            if ((System.currentTimeMillis() - gametimeoffset) / 1000 >= 1) {
                gametimeoffset = System.currentTimeMillis();
                gametime += 1;
            }
            int awardindex = (int) (gametime / 20) % bombaward.length;
            if (awardindex > bombawardtop)
                awardindex = bombawardtop;
            if (bombaward[awardindex] != 0) {
                bombnum += bombaward[awardindex];
                bombaward[awardindex] = 0;
            }
            if (key_fire2) {
                key_fire2 = false;
                if (!bomb.alive && bombnum > 0) {//bomb isn't actived and there's enough bomb .
                    bomb.reset();
                    bomb.alive = true;
                    bombnum--;
                }
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
            } else {
                plane.sprite.setFrame(0);
            }

        }


        flushGraphics();
    }

    private void gameInit() {
        gameover = false;
        pause = false;
        gametime = 0;
        gametimeoffset = System.currentTimeMillis();
        allowinput = true;
        key_up = key_down = key_left = key_right = key_fire = key_fire2 = false;
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
    }

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);
        if (keyCode == -6 || keyCode == 8 || keyCode == 96 || keyCode == -8 || keyCode == -7) {
            if (!pause) {
                pause = true;
                gameMain();
            }
        }
        if (!pause) {
            if (action == FIRE) {
                key_fire2 = true;
            }
        } else {
            pp.keyPressed(action);
            if (pause) gameMain();
        }
    }

    private void gameinput() {
        if (allowinput && !pause) {
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


    public void RestartGame() {
        pause = false;
        gametimeoffset = System.currentTimeMillis();
    }
}