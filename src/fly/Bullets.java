package fly;
import javax.microedition.lcdui.game.*;
import javax.microedition.lcdui.*;
import java.util.Random;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Bullets extends GameObject {
  private int[][] bullets;
  private int bulletstotal;
  private Random rnd;
  public static final int BULLET_TYPE_LEFT=0;
  public static final int BULLET_TYPE_RIGHT=1;
  public static final int BULLET_TYPE_TOP=2;
  public static final int BULLET_TYPE_BOTTOM=3;
  private int width,height;

  public Bullets(Image img,int picwidth,int picheight,int bulletstotal,int width,int height) {
    super(img,picwidth,picheight);
    this.bulletstotal=bulletstotal;
    bullets=new int[bulletstotal][6];
    rnd=new Random();
    this.width=width;
    this.height=height;
  }

  public void initBullets(){
    for (int i = 0; i < bullets.length; i++) {
      initBullet(i);
    }
  }

  private void initBullet(int i) {
    bullets[i][0] = (rnd.nextInt() & 0x7fffffff) % 4; //type
    bullets[i][5] = 1; //alive
    switch (bullets[i][0]) {
      case BULLET_TYPE_LEFT:
        bullets[i][1] = -5;
        bullets[i][2] = (rnd.nextInt() & 0x7fffffff) % height;
        bullets[i][3] = (rnd.nextInt() & 0x7fffffff) % 3 + 1; //vx
        bullets[i][4] = (rnd.nextInt()) % 3; //vy
        break;
      case BULLET_TYPE_RIGHT:
        bullets[i][1] = width + 5;
        bullets[i][2] = (rnd.nextInt() & 0x7fffffff) % height;
        bullets[i][3] = ( (rnd.nextInt() & 0x7fffffff) % 3 + 1) * -1; //vx
        bullets[i][4] = (rnd.nextInt()) % 3; //vy
        break;
      case BULLET_TYPE_TOP:
        bullets[i][1] = (rnd.nextInt() & 0x7fffffff) % width;
        bullets[i][2] = -5;
        bullets[i][3] = (rnd.nextInt()) % 3; //vx
        bullets[i][4] = (rnd.nextInt() & 0x7fffffff) % 3 + 1; //vy
        break;
      case BULLET_TYPE_BOTTOM:
        bullets[i][1] = (rnd.nextInt() & 0x7fffffff) % width;
        bullets[i][2] = height + 5;
        bullets[i][3] = (rnd.nextInt()) % 3; //vx
        bullets[i][4] = ( (rnd.nextInt() & 0x7fffffff) % 3 + 1) * -1; //vy
        break;
    }
  }

  public void updata(int i){
    bullets[i][1]+=bullets[i][3];
    bullets[i][2]+=bullets[i][4];
    if(bullets[i][1]<-5 || bullets[i][1]>width+5){
      bullets[i][3]*=-1;
    }
    if(bullets[i][2]<-5 || bullets[i][2]>height+5){
      bullets[i][4]*=-1;
    }
  }

  private void paint(Graphics g,int i){
    updataspritepos(i);
    sprite.paint(g);
  }

  public void paint(Graphics g) {
    for (int i = 0; i < bullets.length; i++) {
      if(bullets[i][5]==0){
        continue;
      }
      sprite.setPosition(bullets[i][1],bullets[i][2]);
      sprite.paint(g);
    }
  }

  public void refreshBullets(Sprite planesprite, boolean needcollision){
    for (int i = 0; i < bullets.length; i++) {
      if(bullets[i][5]==0){
        continue;
      }
      if(needcollision){
        //System.out.println("needcollision ");
        if (isCollision(planesprite, i, 10)) {
          //System.out.println("collision ");
          Navigate.mc.gameover = true;
          Navigate.mc.explosion.sprite.setPosition(bullets[i][1] - 16,
              bullets[i][2] - 16);
          bullets[i][5] = 0;
          continue;
        }
      }
      updata(i);
    }
  }

  private boolean isCollision(Sprite sprite,int i,int range){
    //updataspritepos(i);
    //return sprite.collidesWith(this.sprite,true);
    boolean result=false;
    int planeXCenter=sprite.getX()+12;
    int planeYCenter=sprite.getY()+12;
    int bulletXCenter=bullets[i][1]+3;
    int bulletYCenter=bullets[i][2]+3;
    if(Math.abs(planeXCenter-bulletXCenter) < range){
      if (Math.abs(planeYCenter - bulletYCenter )< range) {
        result = true;
      }
    }
    return result;
  }

  private void updataspritepos(int i){
    sprite.setPosition(bullets[i][1],bullets[i][2]);
  }

/* no use now
  public void resetDeadBullet(){
    for (int i = 0; i < bullets.length; i++) {
      if(bullets[i][5]==0){//dead bullet
        initBullet(i);
      }
    }
  }
  */

  public void killbullets(Sprite planesprite,int range){
    for (int i = 0; i < bullets.length; i++) {
      if(bullets[i][5]!=0){//alive bullets
        if(isCollision(planesprite, i, range)){
          bullets[i][5]=0;
          initBullet(i);
        }
      }
    }
  }
}