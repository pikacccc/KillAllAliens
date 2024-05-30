package fly;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class StringTools {
  protected StringTools() {
  }

  public static String timeOpinion(long gametime){
    if(gametime<10){
      return "再来一次";
    }else if(gametime<16){
      return "再来一次";
    }else if(gametime<20){
      return "不错 再来一次";
    }else if(gametime<25){
      return "很棒 再来一次";
    }else if(gametime<30){
      return "你玩这游戏很有天赋 再来一次";
    }else if(gametime<40){
      return "你太厉害了吧 ";
    }else{
      return "你简直就是这个游戏的神 ";
    }
  }
}