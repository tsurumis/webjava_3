package jp.co.systena.tigerscave.rpg_similar_db.application.controller;


import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import jp.co.systena.tigerscave.rpg_similar_db.application.model.Battle;
import jp.co.systena.tigerscave.rpg_similar_db.application.model.Job;
import jp.co.systena.tigerscave.rpg_similar_db.application.model.Weather;
import jp.co.systena.tigerscave.rpg_similar_db.application.model.Wimp;

@Controller // Viewあり。Viewを返却するアノテーション
public class FightController {

  @Autowired
  HttpSession session; // セッション管理

  @Autowired
  Battle battle;

  @RequestMapping(value="/Fight" , params="bottom=battle", method = RequestMethod.POST)
  public ModelAndView show(ModelAndView mav) {

    // キャラの取得
    Job job = (Job)session.getAttribute("character");
    // キャラのセット
    mav.addObject("fighter",job);

    // 敵キャラの取得
    Wimp new_wimp = new Wimp(job.getLevel());
    // 敵キャラをsessionにセット
    session.setAttribute("enemy", new_wimp);
    // 敵キャラのセット
    mav.addObject("enemy", new_wimp);

    // メッセージの作成
    List<String> message = new ArrayList<String>();
    message.add("てき　が　あらわれた！");
    mav.addObject("message", message);

    // たたかうコマンドの表示
    session.setAttribute("battle_status", "battle");

    // 天候の取得
    Weather weather = new Weather();
    // sessionにセットしておく。
    session.setAttribute("weather", weather);
    mav.addObject("weather", weather);

    // Viewのテンプレート名を設定
    mav.setViewName("Fight");

    return mav;

  }

  /**************************************:
   * 戦闘メソッド
   * @param mav
   * @return
   */
  @RequestMapping(value="/Fight", params="battle=たたかう", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView battle(ModelAndView mav) {

    // メッセージの作成
    List<String> message = new ArrayList<String>();

    // キャラの取得
    Job job = (Job)session.getAttribute("character");

    // 敵キャラの取得
    Wimp wimp = (Wimp)session.getAttribute("enemy");

    // 天候の取得
    Weather weather = (Weather)session.getAttribute("weather");

    // 実際のバトル処理↓
    message = battle.battle(job, wimp, weather);

    /*******
     * キャラなどの再取得
     *******/

    // キャラの取得
    Job new_job = (Job)session.getAttribute("character");
    // キャラのセット
    mav.addObject("fighter", new_job);

    // 敵キャラの取得
    Wimp new_enemy = (Wimp)session.getAttribute("enemy");

    if(new_enemy == null) {

    }else {
      // キャラのセット
      mav.addObject("enemy", new_enemy);
    }


    // 天候の取得
    Weather new_weather = (Weather)session.getAttribute("weather");
    mav.addObject("weather", new_weather);

    // メッセージのセット
    mav.addObject("message", message);

    // Viewのテンプレート名を設定
    mav.setViewName("Fight");

    return mav;
  }

  /**************************************:
   * (戦闘中)回復メソッド
   * @param mav
   * @return
   */
  @RequestMapping(value="/Fight", params="battle=回復", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView battle_recover(ModelAndView mav) {

    // メッセージの作成
    List<String> message = new ArrayList<String>();

    // キャラの取得
    Job job = (Job)session.getAttribute("character");

    // 敵キャラの取得
    Wimp wimp = (Wimp)session.getAttribute("enemy");

    // 天候の取得
    Weather weather = (Weather)session.getAttribute("weather");

    // 実際のバトル処理↓
    message = battle.battle_recover(job, wimp, weather);

    /*******
     * キャラなどの再取得
     *******/

    // キャラの取得
    Job new_job = (Job)session.getAttribute("character");
    // キャラのセット
    mav.addObject("fighter", new_job);

    // 敵キャラの取得
    Wimp new_enemy = (Wimp)session.getAttribute("enemy");
    // キャラのセット
    mav.addObject("enemy", new_enemy);

    // 天候の取得
    Weather new_weather = (Weather)session.getAttribute("weather");
    mav.addObject("weather", new_weather);

    // メッセージのセット
    mav.addObject("message", message);

    // Viewのテンプレート名を設定
    mav.setViewName("Fight");

    return mav;
  }

  /**********************************
   * 味方のHPをかいふくする
   */
  @RequestMapping(value="/Fight", params="battle=回復する", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView recover(ModelAndView mav) {

    // キャラの取得
    Job fencer = (Job)session.getAttribute("character");
    int recover_hit_point = fencer.getMax_hit_point();
    fencer.setHit_point(recover_hit_point);

    return show(mav);
  }

  /*********************************
   * 敵を出現させる
   */
  @RequestMapping(value="/Fight", params="battle=次にすすむ", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView next_battle(ModelAndView mav) {

    // セッションに敵作成フラグをセット
    session.setAttribute("enemy_create_flag", "create");

    return show(mav);
  }

  /*********************************
   * ステータスを見る
   */
  @RequestMapping(value="/Fight", params="battle=ステータスをみる", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView status(ModelAndView mav) {

    // メッセージの作成
    List<String> message = new ArrayList<String>();

    // キャラの取得
    Job fencer = (Job)session.getAttribute("character");

    message.add("Lv" + fencer.getLevel());
    message.add("ＨＰ　：" + fencer.getHit_point() + " / " + fencer.getMax_hit_point());
    message.add("攻撃力：" + fencer.getOffensive_power());
    message.add("防御力：" + fencer.getDefense_power());
    message.add("素早さ：" + fencer.getSpeed());
    message.add("ランク：" + fencer.getRank());
    message.add("現在の経験値：" + fencer.getExperience_point() + " / " + fencer.getMax_Experience_point());
    int Ex_point = fencer.getMax_Experience_point() - fencer.getExperience_point();
    message.add("次のレベルアップまで：" + Ex_point);

    // メッセージのセット
    mav.addObject("status_list", message);

    return show(mav);
  }


  /*********************************
   * デバックのために敵のステータスを見る
   */
  @RequestMapping(value="/Fight", params="battle=敵のステータスをみる", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView enemy_status(ModelAndView mav) {

    // メッセージの作成
    List<String> message = new ArrayList<String>();

    // 敵キャラの取得
    Wimp enemy = (Wimp)session.getAttribute("enemy");
    message.add("攻撃力：" + enemy.getOffensive_power());
    message.add("防御力：" + enemy.getDefense_power());
    message.add("素早さ：" + enemy.getSpeed());

    // メッセージのセット
    mav.addObject("message", message);

    return show(mav);
  }


}
