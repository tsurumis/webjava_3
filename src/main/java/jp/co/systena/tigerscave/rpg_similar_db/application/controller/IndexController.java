package jp.co.systena.tigerscave.rpg_similar_db.application.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import jp.co.systena.tigerscave.rpg_similar_db.application.model.Form;
import jp.co.systena.tigerscave.rpg_similar_db.application.model.Job;
import jp.co.systena.tigerscave.rpg_similar_db.application.model.Shop;

@Controller // Viewあり。Viewを返却するアノテーション
public class IndexController {

  @Autowired
  HttpSession session;   // セッション管理

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  Job job;

  @Autowired
  Shop shop;

  /***************
   * Gameスタート画面を表示
   */
  @RequestMapping(value="/Fight" , method = RequestMethod.GET) // URLとのマッピング
  public ModelAndView start(ModelAndView mav) {

    // ロード画面に遷移用のFormを作成
    mav.addObject("From", new Form());

    mav.setViewName("Start_Game");
    return mav;
  }


  /****************
   * キャラの作成
   * @param mav
   * @return
   ****************/
  @RequestMapping(value="/Fight" , params="bottom=New_Game", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView index(ModelAndView mav) {

    Job new_job = new Job();

    // メッセージの作成
    List<String> message = new ArrayList<String>();
    message.add(new_job.getJob_name() + " を さくせい しました。");
    message.add("ＨＰは　　　" + new_job.getHit_point() + "　です");
    message.add("攻撃力は　　" + new_job.getOffensive_power() + "　です");
    message.add("防御力は　　" + new_job.getDefense_power() + "　です");
    message.add("すばやさは　" + new_job.getSpeed() + "　です");
    message.add("ランクは　　" + new_job.getRank() + "　です。");
    // messageをセット
    mav.addObject("message", message);

    // AutowiredのJobにデータをセット
    job.setJob_name(new_job.getJob_name());
    job.setMax_hit_point(new_job.getMax_hit_point());
    job.setHit_point(new_job.getHit_point());
    job.setOffensive_power(new_job.getOffensive_power());
    job.setDefense_power(new_job.getDefense_power());
    job.setHeal_power(new_job.getHeal_power());
    job.setSpeed(new_job.getSpeed());
    job.setMax_Experience_point(new_job.getMax_Experience_point());
    job.setRank(new_job.getRank());

    // セッションに作成キャラをセット
    session.setAttribute("character", job);

    // セッションに敵作成フラグをセット
    session.setAttribute("enemy_create_flag", "create");
    session.setAttribute("battle_status", "battle");
    session.removeAttribute("enemy");

    // 次に遷移するControllerをセット
    String controller = "Fight";
    mav.addObject("controller", controller);

    String chara_menu = "menu_1";
    mav.addObject("chara_menu", chara_menu);

    // Viewのテンプレート名を設定
    mav.setViewName("Index");

    return mav;
  }

  /****************
   * 名前や性別を決定させるメニューを表示させる
   */
  @RequestMapping(value="/Fight" , params="bottom=Next", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView index2(ModelAndView mav) {

    // 入力用のFormを作成
    mav.addObject("From", new Form());

    String chara_menu = "menu_2";
    mav.addObject("chara_menu", chara_menu);

    // Viewのテンプレート名を設定
    mav.setViewName("Index");
    return mav;
  }

  /***************
   * キャラクター名や性別をセットする。
   */
  @RequestMapping(value="/Fight" , params="bottom=Decision", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView Decision_caracter(ModelAndView mav, @Valid Form form) {

    job.setPlayer_name(form.getCharacter_name());
    job.setSex(form.getSex());
    job.Decision_game_no();

    session.setAttribute("character", job);

    return menu(mav);
  }

  /******************
   * メニュー画面の表示
   * @param mav
   * @return
   */
  @RequestMapping(value="/Fight" , params="bottom=menu", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView menu(ModelAndView mav) {

    // Viewのテンプレート名を設定
    mav.setViewName("Game_Menu");
    return mav;
  }

  /********************
   * ステータス画面の表示
   */
  @RequestMapping(value="/Fight" , params="bottom=status", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView show_status(ModelAndView mav) {

    // セッションからキャラを取得し、modelに格納
    Job job = (Job)session.getAttribute("character");
    mav.addObject("character", job);

    // Viewのテンプレート名を設定
    mav.setViewName("Status");
    return mav;

  }

}