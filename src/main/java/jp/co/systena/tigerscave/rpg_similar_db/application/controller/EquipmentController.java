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
import jp.co.systena.tigerscave.rpg_similar_db.application.model.Equipment;
import jp.co.systena.tigerscave.rpg_similar_db.application.model.EquipmentList;
import jp.co.systena.tigerscave.rpg_similar_db.application.model.Form;
import jp.co.systena.tigerscave.rpg_similar_db.application.model.Haveitem;
import jp.co.systena.tigerscave.rpg_similar_db.application.model.Job;



@Controller // Viewあり。Viewを返却するアノテーション
public class EquipmentController {


  @Autowired
  HttpSession session;  // セッション管理

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  Equipment equipment;

  @RequestMapping(value="/Fight" , params="bottom=equipment", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView equipmentmenu(ModelAndView mav) {

    // 現在の装備を取得
    List<EquipmentList> equipmentlist = equipment.getEquipment();
    if(equipmentlist.size() != 0) {
      mav.addObject("equipment_buki" , equipmentlist.get(0).getItem_no_1());
      mav.addObject("equipment_bougu", equipmentlist.get(0).getItem_no_2());
    }else {
      mav.addObject("equipment_buki" , "00");
      mav.addObject("equipment_bougu", "00");
    }


    // 武器アイテム一覧を取得
    List<Haveitem> bukilist = equipment.getbukilist();
    mav.addObject("bukilist", bukilist);

    // 防具アイテム一覧を取得
    List<Haveitem> soubilist = equipment.getsoubilist();
    mav.addObject("soubilist", soubilist);

    // ステータス表示のため
    Job job = (Job)session.getAttribute("character");
    mav.addObject("job", job);

    // 装備入力を受け付ける
    Form form = new Form();
    mav.addObject("Form", form);

    // View名をセット
    mav.setViewName("Equipment");

    return mav;
  }

  @RequestMapping(value="/Fight" , params="action=装備", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView equipment(ModelAndView mav, @Valid Form form) {

    System.out.println(form.getSoubi_kbn());
    System.out.println(form.getSoubi());

    String message = equipment.equipment_action(form.getGame_no(), form.getSoubi_kbn(), form.getSoubi());
    mav.addObject("message", message);

    return equipmentmenu(mav);
  }






}
