package jp.co.systena.tigerscave.rpg_similar_db.application.controller;

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
import jp.co.systena.tigerscave.rpg_similar_db.application.model.ItemList;
import jp.co.systena.tigerscave.rpg_similar_db.application.model.Job;
import jp.co.systena.tigerscave.rpg_similar_db.application.model.Shop;

@Controller // Viewあり。Viewを返却するアノテーション
public class ShopController {

  @Autowired
  HttpSession session;   // セッション管理

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  Shop shop;

  /********************
   * ショップメニューに遷移する。
   */
  @RequestMapping(value="/Fight" , params="bottom=shop", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView show_shop(ModelAndView mav) {

    Job job = (Job)session.getAttribute("character");

    // アイテムの一覧を取得する。
    List<ItemList> itemlist = shop.getItemList(job.getLevel(), job.getJob_name());
    mav.addObject("itemlist", itemlist);

    // 現在の所持マニーを取得
    mav.addObject("havemoney", job.getMoney());

    // フォームの作成
    mav.addObject("From", new Form());

    // Viewのテンプレート名を設定
    mav.setViewName("Shop");

    return mav;
  }

  /********************
   * 購入処理（を実際にするメソッドを呼び出す）
   */
  @RequestMapping(value="/Fight" , params="bottom=purchase", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView purchase(ModelAndView mav, @Valid Form form) {

    String message = shop.action_purchase(form.getItem_no(), form.getQty(), form.getItem_price());

    mav.addObject("message", message);

    return show_shop(mav);
  }

}
