package fly;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Font {
  Sprite sprite;
  int width,height;
  int[] charhash;
  Graphics g;

  public Font(Graphics g,Image img, int width,  int height, char[] chars) {
    this.g=g;
    sprite=new Sprite(img,width,height);
    this.width=width;
    this.height=height;
    charhash=new int[128];
    for (int i = 0; i < charhash.length; i++) {
      charhash[i]=-1;
    }
    Character c;
    for (int i = 0; i < chars.length; i++) {
      c=new Character(chars[i]);
      charhash[c.hashCode()]=i;
    }
  }

  public void drawChar(char ch, int x, int y){
    Character c=new Character(ch);
    int hashcode=c.hashCode();
    sprite.setPosition(x,y);
    if(hashcode>=0){
      sprite.setFrame(charhash[hashcode]);
      sprite.paint(g);
    }
  }

  public void drawString(String str, int x, int y){
    int length=str.length();
    for (int i = 0; i < length; i++) {
      drawChar(str.charAt(i),x+width*i,y);
    }
  }
}