package fly;

import javax.microedition.lcdui.Image;
import java.io.IOException;

public class Util {
    public static Image LoadImg(String name) {
        Image tempImg = null;
        try {
            tempImg = Image.createImage(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempImg;
    }

    public static Image bg = Util.LoadImg("/pic/background.png");
}
