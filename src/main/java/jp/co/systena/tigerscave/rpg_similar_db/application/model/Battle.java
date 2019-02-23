package jp.co.systena.tigerscave.rpg_similar_db.application.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Battle {

  @Autowired
  HttpSession session; // セッション管理

  public List<String> battle(Job job, Wimp wimp, Weather weather) {

    // メッセージの作成
    List<String> message = new ArrayList<String>();

    int damage;
    int new_HP;

    if (job.getSpeed() >= wimp.getSpeed()) {
      // 味方先制時
      damage = CalDamage(job, wimp, weather);
      new_HP = wimp.getHit_point() - damage;
      wimp.setHit_point(new_HP);
      message.add("てき　に　 " + damage + "ダメージ　をあたえました");

      // 敵のHP判定
      if(wimp.getHit_point() <= 0) {
        // 経験値の取得
        job.addExperience_point(wimp.getExperience_point());
        // マニーの取得
        job.setMoney(wimp.getMoney());
        // メッセージのセットや設定の削除
        message = battle_win(message, job, wimp);
        return message;
      }

      damage = CalDamage(wimp, job, weather);
      new_HP = job.getHit_point() - damage;
      job.setHit_point(new_HP);
      message.add("てき　から " + damage + "ダメージ　うけました");

      // 味方のHP判定
      if(job.getHit_point() <= 0) {
        job.setHit_point(0);
        session.setAttribute("battle_status", "gameover");
        // メッセージのセット
        message.add("てき　にまけました。。。");
        return message;
      }

    }else{
      // 敵先制時
      damage = CalDamage(wimp, job, weather);
      new_HP = job.getHit_point() - damage;
      job.setHit_point(new_HP);
      message.add("てき　から " + damage + "ダメージ　うけました");

      // 味方のHP判定
      if(job.getHit_point() <= 0) {
        job.setHit_point(0);
        session.setAttribute("battle_status", "gameover");
        // メッセージのセット
        message.add("てき　にまけました。。。");
        return message;
      }

      damage = CalDamage(job, wimp, weather);
      new_HP = wimp.getHit_point() - damage;
      wimp.setHit_point(new_HP);
      message.add("てき　に " + damage + "ダメージ　を　あたえました");

      // 敵のHP判定
      if(wimp.getHit_point() <= 0) {
        // 経験値の取得
        job.addExperience_point(wimp.getExperience_point());
        // マニーの取得
        job.setMoney(wimp.getMoney());
        // メッセージのセットや設定の削除
        message = battle_win(message,job,wimp);
        return message;
      }

    }

    System.out.println(job.getHit_point());

    // sessionに現在のjobをセット
    session.setAttribute("character", job);

    // sessionに現在のenemyをセット
    session.setAttribute("enemy", wimp);

    return message;

  }

  /****************:
   * バトル中の回復処理
   * @param job
   * @param wimp
   * @param weather
   * @return
   */
  public List<String> battle_recover(Job job, Wimp wimp, Weather weather) {

 // メッセージの作成
    List<String> message = new ArrayList<String>();

    int damage;
    int new_HP;

    if (job.getSpeed() >= wimp.getSpeed()) {
      // 味方先制時
      int heal_point = job.getHeal_power();
      new_HP = job.getHit_point() + heal_point;
      if(job.getMax_hit_point() < new_HP) {
        new_HP = job.getMax_hit_point();
      }
      job.setHit_point(new_HP);
      message.add("ＨＰ　が　 " + heal_point + " かいふくしました");

      damage = CalDamage(wimp, job, weather);
      new_HP = job.getHit_point() - damage;
      job.setHit_point(new_HP);
      message.add("てき　から " + damage + "ダメージ　うけました");

      // 味方のHP判定
      if(job.getHit_point() <= 0) {
        job.setHit_point(0);
        session.setAttribute("battle_status", "gameover");
        // メッセージのセット
        message.add("てき　にまけました。。。");
        return message;
      }

    }else{
      // 敵先制時
      damage = CalDamage(wimp, job, weather);
      new_HP = job.getHit_point() - damage;
      job.setHit_point(new_HP);
      message.add("てき　から " + damage + "ダメージ　うけました");

      // 味方のHP判定
      if(job.getHit_point() <= 0) {
        job.setHit_point(0);
        session.setAttribute("battle_status", "gameover");
        // メッセージのセット
        message.add("てき　にまけました。。。");
        return message;
      }

      int heal_point = job.getHeal_power();
      new_HP = job.getHit_point() + heal_point;
      if(job.getMax_hit_point() < new_HP) {
        new_HP = job.getMax_hit_point();
      }
      job.setHit_point(new_HP);
      message.add("ＨＰ　が　 " + heal_point + " かいふくしました");

    }

    // sessionに現在のjobをセット
    session.setAttribute("character", job);

    // sessionに現在のenemyをセット
    session.setAttribute("enemy", wimp);

    return message;
  }


  /**********************************
   * 敵を倒したときにいろいろと設定を削除したり、セットしたりするメソッド
   */
  public List<String> battle_win(List<String> message, Job job, Wimp wimp) {
    // メッセージの設定
    message.add("てき　を　たおしました");
    message.add("経験値を " + wimp.getExperience_point() + " 取得しました");
    // sessionから敵や設定を削除
    session.removeAttribute("enemy");
    session.removeAttribute("enemy_create_flag");
    session.removeAttribute("weather");

    // sessionに現在のjobをセット
    session.setAttribute("character", job);

    session.setAttribute("battle_status", "win");

    return message;
  }

  /**********************************
   * クリティカルの抽選メソッド
   */
  public int critical() {
    Random rnd = new Random();
    int num = rnd.nextInt(12);
    return num;
  }

  /**********************************
   * ダメージ計算をするメソッド
   * 味方⇒敵にダメージを与えるパターン
   * @return
   */
  public int CalDamage(Job job, Wimp wimp, Weather weather) {

    int attack = job.getOffensive_power();
    int level = job.getLevel();
    int power = 90;
    int defence = wimp.getDefense_power();
    String character_name = job.getJob_name();

    double weather_cor = weather.getWeather_cor(character_name);

    // 天候が雨の場合、味方の防御力も補正をかける
    if(weather.getWeather_name().equals("雨") && character_name.equals("剣士")) {
      defence = (int)(defence * 0.75);
    }

    // クリティカル判定
    if(critical() == 1 && ! character_name.equals("ザコ")) {
      attack = job.Attack(weather_cor) * 2;
    }

    Random rnd = new Random();
    double hosei = 1.0;

    if ( level >= 5 && character_name.equals("ザコ")) {
      double hosei_org = rnd.nextInt(30) + 120;
      hosei = hosei_org / 100.0;
      attack = (int)(attack * hosei);
    }

    int damage = (int) ( ( ( (level * 2 / 5 + 2 ) * ( power * attack / defence ) / 50 + 2 ) ) * weather_cor) ;

    return damage;

  }

  /**********************************
   * ダメージ計算をするメソッド
   * 敵⇒味方にダメージを与えるパターン
   * @return
   */
  public int CalDamage(Wimp wimp, Job job, Weather weather) {

    int attack = wimp.getOffensive_power();
    int level = wimp.getLevel();
    int power = 70;
    int defence = job.getDefense_power();
    String character_name = wimp.getJob_name();

    double weather_cor = weather.getWeather_cor(character_name);

    // 天候が雨の場合、味方の防御力も補正をかける
    if(weather.getWeather_name().equals("雨") && character_name.equals("剣士")) {
      defence = (int)(defence * 0.75);
    }

    // クリティカル判定
    //if(critical() == 1 && ! character_name.equals("ザコ")) {
    //  attack = job.Attack(weather_cor) * 2;
    //}

    Random rnd = new Random();
    double hosei = 1.0;

    if ( level >= 5 && character_name.equals("ザコ")) {
      double hosei_org = rnd.nextInt(30) + 120;
      hosei = hosei_org / 100.0;
      attack = (int)(attack * hosei);
    }

    int damage = (int) ( ( ( (level * 2 / 5 + 2 ) * ( power * attack / defence ) / 50 + 2 ) ) * weather_cor) ;

    return damage;

  }




}
