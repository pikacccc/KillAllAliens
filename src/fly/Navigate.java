package fly;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class Navigate {
    private static Navigate instance;
    public static MyGameCanvas mc;
    public static FlyMidlet midlet;
    public static Display display;
    public static Menu gameMenu;
    public static GameOver gameOver;


    protected Navigate(FlyMidlet midlet) {
        Navigate.midlet = midlet;
        Navigate.mc = MyGameCanvas.getInstance(midlet);

        Navigate.gameMenu = Menu.getInstance();
        Navigate.gameOver = GameOver.getInstance();
        Navigate.display = Display.getDisplay(midlet);
    }


    synchronized public static Navigate getInstance(FlyMidlet midlet) {
        if (instance == null) {
            instance = new Navigate(midlet);
            System.out.println("new Navigate");
        }
        return instance;
    }


    public static void exitMIDlet() {
        midlet.exitMIDlet();
    }

    public static void OpenMenu() {
        gameMenu.start();
        display.setCurrent(gameMenu);
    }

    public static void CloseMenu() {
        gameMenu.stop();
    }

    public static void StartGame() {

        mc.start();
        display.setCurrent(mc);
    }

    public static void CloseGame() {
        mc.stop();
        mc.cleanJob();
        mc = MyGameCanvas.getInstance(midlet);
    }

    public static void OpenGameOver() {
        gameOver.start();
        display.setCurrent(gameOver);
    }

    public static void CloseGameOver() {
        gameOver.stop();
    }

    public static void cleanJob() {
        instance = null;
    }
}