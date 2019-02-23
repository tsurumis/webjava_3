package jp.co.systena.tigerscave.rpg_similar_db.application.model;

import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class Job extends Human {

  // 種族値
  private int Base_status = 120;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public Job() {

    Random rnd = new Random();
    int num = rnd.nextInt(2);

    if(num == 0) {

      super.setJob_name("剣士");
      // 攻撃力を取得し、補正(上方)をかける (120 - 150)
      int attack = super.getOffensive_power();
      double new_attack = attack * 1.25;
      attack = (int)new_attack;
      super.setOffensive_power(attack);

      // HPを取得し、補正(上方)をかける (120 - 150)
      int HP = super.getHit_point();
      double new_HP = HP * 1.25;
      HP = (int)new_HP;
      super.setHit_point(HP);
      super.setMax_hit_point(HP);

      // 素早さを取得し、補正(下方)をかける (72 - 108)
      int speed = super.getSpeed();
      double new_speed = speed * 0.9;
      speed = (int)new_speed;
      super.setSpeed(speed);

      // 最初の経験値をセット

      super.setExperience_point(0);
      // 最初の最大経験値をセット
      super.setMax_Experience_point(120);

    }else {

      super.setJob_name("魔法使い");
      // 防御力を取得し、補正(上方)をかける (120 - 150)
      int defense = super.getDefense_power();
      double new_defense = defense * 1.25;
      defense = (int)new_defense;
      super.setDefense_power(defense);;

      // HPを取得し、補正(下方)をかける (120 - 150)
      int HP = super.getHit_point();
      double new_HP = HP * 0.9;
      HP = (int)new_HP;
      super.setHit_point(HP);
      super.setMax_hit_point(HP);

      // 素早さを取得し、補正(上方)をかける (72 - 108)
      int speed = super.getSpeed();
      double new_speed = speed * 1.25;
      speed = (int)new_speed;
      super.setSpeed(speed);

      // 最初の経験値をセット
      super.setExperience_point(0);

      // 最初の最大経験値をセット
      super.setMax_Experience_point(80);

    }

    // 総ステータスを計算しランクを決定
    int total_status = super.getHit_point() + super.getOffensive_power() + super.getDefense_power() + super.getSpeed();
    super.setRank(getRank(total_status));

  }

  /***********************
   * game_noを決定し親クラスにセットするメソッド
   ***********************/
  public void Decision_game_no() {

    Random rnd = new Random();
    int new_game_no;
    String string_game_no;

    while(true) {
      new_game_no = rnd.nextInt(8999) + 1000;
      System.out.println("new_game_no is " + new_game_no);
      string_game_no =  String.valueOf(new_game_no);

      List<GameNo> game_nolist = jdbcTemplate.query("SELECT distinct game_no FROM m_character_kanri UNION ALL SELECT 'no_data' as game_no WHERE NOT EXISTS(SELECT distinct game_no FROM m_character_kanri)",
                                  new BeanPropertyRowMapper<GameNo>(GameNo.class));

      int cnt = 0;

      for(int i = 0 ; i < game_nolist.size() ; i++){

        String game_no = game_nolist.get(0).getGame_no();

        if (game_no.equals(string_game_no)) {
          cnt = cnt + 1;
        }

      }

      if(cnt == 0) {
        break;
      }else {
        ;
      }


    }

    super.setGame_no(string_game_no);

  }

  /************************
   *  経験値を追加する
   * @param Experience_point
   ************************/
  public void addExperience_point(int Experience_point) {
    // 現在の経験値を取得する。
    int now_Experience_point = super.getExperience_point();
    // 取得経験値を足す
    now_Experience_point = now_Experience_point + Experience_point;
    // 最大経験値を取得する。
    int Max_Experience_point = super.getMax_Experience_point();

    if(Max_Experience_point <= now_Experience_point) {
      // レベルアップ時
      // あふれた経験値を取得し現在経験値とする。
      now_Experience_point = now_Experience_point - Max_Experience_point;
      super.setExperience_point(now_Experience_point);
      // 次回のレベルをセット
      int next_level = super.getLevel() + 1;
      super.setLevel(next_level);
      // 次回の最大経験値をセット
      int next_max_ex_point = (int)(Max_Experience_point + (1.25 * next_level * next_level));
      super.setMax_Experience_point(next_max_ex_point);

      // キャラのランクを取得(ランクによってレベルアップ時の補正をかける。)
      String rank = super.getRank();
      double hosei = 1.0;
      if (rank.equals("SS")) {
        hosei = 1.5;
      }else if(rank.equals("S")) {
        hosei = 1.25;
      }else if(rank.equals("A")) {
        hosei = 1.1;
      }else if(rank.equals("B")) {
        hosei = 1.0;
      }else if(rank.equals("C")) {
        hosei = 0.9;
      }else {
        ;
      }

      // 各種新しいステータスをセット
      super.setMax_hit_point(new_status_hp(super.getMax_hit_point(),super.getLevel(),hosei));
      super.setOffensive_power(new_status_not_hp(super.getOffensive_power(),super.getLevel(),hosei));
      super.setDefense_power(new_status_not_hp(super.getDefense_power(),super.getLevel(),hosei));
      super.setSpeed(new_status_not_hp(super.getSpeed(),super.getLevel(),hosei));
      // 回復力のアップは、奇数レベルにレベルアップしたときのみ
      if(next_level%2 == 1) {
        super.setHeal_power(new_status_not_hp(super.getHeal_power(),super.getLevel(),hosei));
      }

      // レベルアップ時には、HPを回復する
      super.setHit_point(super.getMax_hit_point());

    }else {
      super.setExperience_point(now_Experience_point);
    }
  }


  /****************************
   * レベルアップ時のステータスを返す(HP)
   */
  public int new_status_hp(int hit_point,int level, double hosei) {
    Random rnd = new Random();
    int new_hit_point = (int)(hit_point +(15 * (rnd.nextInt(20)+80)/100 * hosei) + level);
    return new_hit_point;

  }

  /*****************************
   * レベルアップ時のステータスを返す(HP以外)
   */
  public int new_status_not_hp(int status, int level, double hosei) {
    Random rnd = new Random();
    int new_status = (int)(status + (10 * (rnd.nextInt(20)+80)/100 * hosei) + level);
    return new_status;
  }

  /*****************************
   * キャラのステータス合計値で
   * Rankを返す
   * @return rank
   */
  public String getRank(int total_status) {

    String rank;

    if ( total_status > 500) {
      rank = "SS";
    }else if ( 485 < total_status && total_status <= 500 ) {
      rank = "S";
    }else if ( 457 < total_status && total_status <= 485 ) {
      rank = "A";
    }else if ( 433 < total_status && total_status <= 457 ) {
      rank = "B";
    }else {
      rank = "C";
    }
    return rank;
  }


  /**************************
   * 武器装備を行う。
   */
  public void equipment_buki(int value) {
    int new_offensive_power = super.getOffensive_power() + value;
    super.setOffensive_power(new_offensive_power);
  }

  /**************************
   * 防具装備を行う。
   */
  public void equipment_bougu(int value) {
    int new_defense_power = super.getDefense_power() + value;
    super.setDefense_power(new_defense_power);
  }

  @Override
  public int Attack(double weather_cor) {
    // 攻撃力を取得
    int attack = super.getOffensive_power();

    // 敵に与えるダメージを計算
    double base_damage = attack * this.Base_status * weather_cor / 100;
    int damage = (int)base_damage;

    return damage;
  }

}
