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
import jp.co.systena.tigerscave.rpg_similar_db.application.model.Job;
import jp.co.systena.tigerscave.rpg_similar_db.application.model.SaveAndLoad;
import jp.co.systena.tigerscave.rpg_similar_db.application.model.SaveDataList;

@Controller // Viewあり。Viewを返却するアノテーション
public class SaveAndLoadController {

  @Autowired
  HttpSession session;   // セッション管理

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  SaveAndLoad saveandload;

  @Autowired
  Job job;

  /********************
   * セーブ＆ロード画面の表示
   */
  @RequestMapping(value="/Fight" , params="bottom=save_and_load", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView save_and_load(ModelAndView mav, @Valid Form form) {
    // セーブデータの一覧を取得
    List<SaveDataList> savedatalist = saveandload.getSaveData();
    mav.addObject("savedatalist", savedatalist);

    // 新しいindex_noの取得
    mav.addObject("new_index", saveandload.getIndex_no());

    // セーブボタンを出すかロードボタンを出すか
    mav.addObject("save_or_load", form.getSave_or_load());

    // Viewのテンプレート名を設定
    mav.setViewName("Save&Load");
    return mav;
  }

  /*******************
   * ロードの実行
   */
  @RequestMapping(value="/Fight" , params="bottom=load", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView load(ModelAndView mav, @Valid Form form) {

    saveandload.LoadGame(form.getGame_no());

    // Viewのテンプレート名を設定
    mav.setViewName("Game_Menu");

    return mav;
  }

  /*******************
   * セーブの実行
   */
  @RequestMapping(value="/Fight" , params="bottom=save", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView savedata(ModelAndView mav, @Valid Form form) {

    int ret = saveandload.SaveGame(form.getUniq_no(), form.getGame_no());

    mav.addObject("save_or_load", "save");

    // セーブの実行が終わったら、またセーブ＆ロード画面に戻す。
    return save_and_load(mav, form);
  }

}
