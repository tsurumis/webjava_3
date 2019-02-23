package jp.co.systena.tigerscave.rpg_similar_db.application.model;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**********************
 * 所持アイテム一覧画面で使う処理をまとめたクラス
 * @author systena
 *
 */
@Service
public class ItemMenu {

  @Autowired
  HttpSession session;  // セッション管理

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public List<ProItem> getProitemlist(){

    // セッションから現在のgame_noを取得
    Job job = (Job)session.getAttribute("character");
    String game_no = job.getGame_no();

    List<ProItem> proitemlist = jdbcTemplate.query("SELECT * FROM m_possession_item "
        + "WHERE game_no = '" + game_no + "' ORDER BY item_no", new BeanPropertyRowMapper<ProItem>(ProItem.class));

    return proitemlist;
  }

  /******************
   * アイテムを使う処理
   * @param value
   * @return
   */
  public String useitem(String item_no, int value ,int item_qty) {

    String message;

    // sessionから現在のキャラを取得
    Job job = (Job)session.getAttribute("character");
    int hit_point = job.getHit_point();
    int max_hit_point = job.getMax_hit_point();

    if (hit_point == max_hit_point) {
      message = "ＨＰが最大値のため、使えません";
    }else {

      hit_point = hit_point + value;

      // 回復したHPが最大HPを超えないようにする。
      if (hit_point > max_hit_point) {
        hit_point = max_hit_point;
      }else {
        ;
      }

      // 回復したHPをセット
      job.setHit_point(hit_point);
      message = "ＨＰを回復しました。";
      // sessionにjobをセットする。
      session.setAttribute("character", job);

      /*************************:
       * 使ったアイテムを消費する処理
       */
      item_qty = item_qty - 1;
      if ( item_qty == 0) {
        // 削除処理
        int deleteCount = jdbcTemplate.update(""
            + "DELETE FROM m_possession_item "
            + "WHERE game_no = ? and item_no = ?",
            job.getGame_no(),
            item_no);
        if (deleteCount == 0) {
          message = "エラーが発生しました。";
        }

      }else {
        // 更新処理
        int updateCount = jdbcTemplate.update(""
            + "UPDATE m_possession_item SET item_qty = ? "
            + "WHERE game_no = ? and item_no = ?",
            item_qty,
            job.getGame_no(),
            item_no);

        if(updateCount == 0) {
          message = "エラーが発生しました。";
        }

      }

    }

    return message;

  }

}
