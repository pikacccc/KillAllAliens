package fly;
import javax.microedition.lcdui.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class Navigate {
  private static Navigate instance;
  public static MyGameCanvas mc;
  public static FlyMidlet midlet;
  public static Display display;

  protected Navigate(FlyMidlet midlet) {
    Navigate.midlet=midlet;
    Navigate.mc=MyGameCanvas.getInstance();
    Navigate.display=Display.getDisplay(midlet);
  }

  synchronized public static Navigate getInstance(FlyMidlet midlet){
    if (instance == null) {
      instance = new Navigate(midlet);
      System.out.println("new Navigate");
    }
    return instance;
  }

  public static void cleanJob(){
    instance=null;
  }
}