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

public class ImageTools {
  protected ImageTools() {
  }

  public static Image getImage(String str){
    Image img=null;
    try {
      img = Image.createImage(str);
    }
    catch (Exception ex) {
      System.out.println(ex);
    }
    finally{
      return img;
    }
  }
}