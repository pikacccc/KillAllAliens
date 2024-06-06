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

public class FlyMidlet extends MIDlet implements CommandListener, ItemStateListener {
    Navigate ng;

    public FlyMidlet() {
        ng = Navigate.getInstance(this);
        CreateForms();
    }

    protected void startApp() {

    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean parm1) {
        Navigate.mc.stop();
        MyGameCanvas.cleanJob();
        Navigate.cleanJob();
    }

    private void printInfo() {
    }

    private List m_MainList;

    public void CreateForms() {
        ng.display.setCurrent(ng.mc);
        ng.mc.StartGame();
    }

    public void commandAction(Command command, Displayable displayable) {
        int iSelect;
        if ((command == List.SELECT_COMMAND)) {

            iSelect = m_MainList.getSelectedIndex();
            switch (iSelect) {
                case 0:
                    ng.display.setCurrent(ng.mc);
                    ng.mc.StartGame();
                    break;
                case 1:
                    exitMIDlet();
                    break;

                default:
                    break;
            }
        }
    }

    public void itemStateChanged(Item item) {

    }

    public void exitMIDlet() {
        MyGameCanvas.cleanJob();
        Navigate.cleanJob();
        destroyApp(true);
        notifyDestroyed();
    }
}