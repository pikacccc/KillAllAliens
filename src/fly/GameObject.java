package fly;
import javax.microedition.lcdui.game.*;
import javax.microedition.lcdui.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class GameObject {
  public Sprite sprite;
  public boolean alive;
  private int lifecount=0;
  public int lifetime=0;
  public int speed=0;
  private int animcount=0;

  public GameObject(Image img,int width,int height){
    sprite=new Sprite(img,width,height);
    reset();
  }

  public void move(int dx,int dy){
    sprite.move(dx,dy);
  }

  public void moveto(int x,int y){
    sprite.setPosition(x,y);
  }

  public void update(){
    if(!alive)
      return;
    if(++animcount>speed){
      animcount=0;
      sprite.nextFrame();
      if(lifetime!=0 && ++lifecount>lifetime)
        alive=false;
    }
  }

  public void paint(Graphics g){
    if(!alive)
      return;
    sprite.paint(g);
  }
  public void reset(){
    alive=true;
    lifecount=0;
    animcount=0;
    sprite.setFrame(0);
  }
}