package fly;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;

public class GameOver extends GameCanvas implements CommandListener, Runnable {
    private static GameOver instance;
    private boolean isRunning = false;
    private Graphics g;
    private int selectedOption = 0;
    private Command selectCommand;

    private Image Bg;
    private Image GameOver;
    private Image Restart;
    private Image Exit;

    private int width;
    private int height;
    private int bg_x;
    private int bg_y;
    private int gameOver_x;
    private int gameOver_y;
    private int restart_x;
    private int restart_y;
    private int exit_x;
    private int exit_y;

    private int score_x;
    private int score_y;
    private int hiScore_x;
    private int hiScore_y;

    private DrawNumberHandler drawNumberHandler;

    public GameOver() {
        super(true);
        setFullScreenMode(true);
        g = getGraphics();
        selectCommand = new Command("", Command.OK, 0);
        addCommand(selectCommand);
        setCommandListener(this);
        LoadImages();
        InitCoordinates();
    }

    private void LoadImages() {
        Bg = Util.LoadImg("/pic/background.png");
        GameOver = Util.LoadImg("/pic/title_end.png");
        Restart = Util.LoadImg("/pic/btn_restart.png");
        Exit = Util.LoadImg("/pic/btn_quit.png");
        this.drawNumberHandler = new DrawNumberHandler("/pic/number.png", 32, 48);
    }

    public void InitCoordinates() {
        width = getWidth();
        height = getHeight();

        int center_x = width / 2;
        int center_y = height / 2;

        bg_x = center_x - Bg.getWidth() / 2;
        bg_y = center_y - Bg.getHeight() / 2;
        gameOver_x = center_x - GameOver.getWidth() / 2;
        gameOver_y = center_y - GameOver.getHeight() / 2 - 100;
        restart_x = center_x - Restart.getWidth() / 2;
        restart_y = center_y - Restart.getHeight() / 2 + 135;
        exit_x = center_x - Exit.getWidth() / 2;
        exit_y = center_y - Exit.getHeight() / 2 + 210;

        score_x = center_x;
        score_y = center_y - 60;

        hiScore_x = center_x;
        hiScore_y = center_y + 50;
    }

    public void start() {
        isRunning = true;
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        while (isRunning) {
            tick();
            if (!isRunning) break;
            draw();
        }
    }

    private int keyTrigger = 0;

    private void tick() {
        int keys = getKeyStates();

        int inv = 0xffffffff - keyTrigger;
        int key = inv & keys;
        keyTrigger &= keys;

        if ((key & DOWN_PRESSED) != 0) {
            selectedOption = (selectedOption + 1) % 2;
            keyTrigger |= DOWN_PRESSED;
        }
        if ((key & UP_PRESSED) != 0) {
            selectedOption = (selectedOption - 1 + 2) % 2;
            keyTrigger |= UP_PRESSED;
        }
        if ((key & FIRE_PRESSED) != 0) {
            executeSelectedOption();
            keyTrigger |= FIRE_PRESSED;
        }
    }

    public void stop() {
        isRunning = false;
    }

    private void draw() {
        g.setColor(0);
        g.fillRect(0, 0, width, height);
        g.drawImage(Bg, bg_x, bg_y, 0);
        g.drawImage(GameOver, gameOver_x, gameOver_y, Graphics.TOP | Graphics.LEFT);
        if (selectedOption == 0) {
            g.setColor(0xFADF5F);
        } else {
            g.setColor(0xFFFFCF);
        }
        g.fillRect(restart_x - 70, restart_y - 15, 300, 56);
        g.drawImage(Restart, restart_x, restart_y, 0);
        if (selectedOption == 1) {
            g.setColor(0xFADF5F);
        } else {
            g.setColor(0xFFFFCF);
        }
        g.fillRect(exit_x - 70, exit_y - 15, 300, 56);
        g.drawImage(Exit, exit_x, exit_y, 0);
        flushGraphics();
    }

    protected void keyPressed(int keyCode) {
//        int gameAction = getGameAction(keyCode);
//        if (gameAction == UP || gameAction == LEFT) {
//            selectedOption = (selectedOption - 1 + 2) % 2;
//        } else if (gameAction == DOWN || gameAction == RIGHT) {
//            selectedOption = (selectedOption + 1) % 2;
//        } else if (gameAction == FIRE) {
//            executeSelectedOption();
//        }
    }

    private void executeSelectedOption() {
        if (selectedOption == 0) {
            Navigate.StartGame();
            Navigate.CloseGameOver();
        } else if (selectedOption == 1) {
            Navigate.exitMIDlet();
        }
    }

    public void commandAction(Command command, Displayable displayable) {
//        if (command == selectCommand) {
//            executeSelectedOption();
//        }
    }

    synchronized public static GameOver getInstance() {
        if (instance == null) {
            instance = new GameOver();
        }
        return instance;
    }

    public static void cleanJob() {
        instance = null;
    }
}