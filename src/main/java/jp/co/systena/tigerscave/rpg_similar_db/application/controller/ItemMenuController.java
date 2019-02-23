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
import jp.co.systena.tigerscave.rpg_similar_db.application.model.ItemMenu;
import jp.co.systena.tigerscave.rpg_similar_db.application.model.Job;
import jp.co.systena.tigerscave.rpg_similar_db.application.model.ProItem;

@Controller // Viewあり。Viewを返却するアノテーション
public class ItemMenuController {

  @Autowired
  HttpSession session;  // セッション管理

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  ItemMenu itemmenu;

  @RequestMapping(value="/Fight" , params="bottom=showitemlist", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView itemmenu(ModelAndView mav) {

    // 現在の所持アイテムを取得
    List<ProItem> proitemlist = itemmenu.getProitemlist();
    mav.addObject("proitemlist", proitemlist);

    // sessionから現在のキャラを取得
    Job job = (Job)session.getAttribute("character");
    mav.addObject("job", job);

    // 入力を受け取る用のFormを作成
    Form form = new Form();
    mav.addObject("Form", form);

    // Viewのテンプレート名を設定
    mav.setViewName("ProItem");

    return mav;

  }

  @RequestMapping(value="/Fight" , params="action=use_item", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView useitem(ModelAndView mav, @Valid Form form) {

    String message = itemmenu.useitem(form.getItem_no(), form.getValue(), form.getItem_qty());

    mav.addObject("message", message);

    return itemmenu(mav);
  }

}
