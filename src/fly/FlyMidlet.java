package fly;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class FlyMidlet extends MIDlet {
    Navigate ng;

    public FlyMidlet() {
        ng = Navigate.getInstance(this);
    }

    protected void startApp() {
        Init();
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean parm1) {
        Navigate.mc.stop();
        MyGameCanvas.cleanJob();
        Navigate.cleanJob();
    }

    public void Init() {
        Navigate.OpenMenu();
    }

    public void exitMIDlet() {
        MyGameCanvas.cleanJob();
        Navigate.cleanJob();
        destroyApp(true);
        notifyDestroyed();
    }
}