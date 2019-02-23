package jp.co.systena.tigerscave.rpg_similar_db.application.model;

import java.util.Random;

public class Weather extends Tempestas{

  // 新規作成時、ランダムで天候を作成する。
  public Weather() {

    Random rnd = new Random();
    int num = rnd.nextInt(3);
    if (num == 0) {
      // 晴れ
      // 有利：剣士
      // 不利：ザコ
      ;
    }else if(num == 1){
      // 雨
      super.setWeather_name("雨");
      super.setYuuri_name("ザコ");
      super.setHuri_name("剣士");

    }else {
      // 曇り
      super.setWeather_name("曇り");
      super.setYuuri_name("魔法使い");
      super.setHuri_name("ザコ");
    }

  }

  @Override
  public int recover(double weater_cor) {

    int recover_cor = 1;

    return recover_cor;
  }

}
