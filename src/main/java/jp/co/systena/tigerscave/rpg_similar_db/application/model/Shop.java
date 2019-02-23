package jp.co.systena.tigerscave.rpg_similar_db.application.model;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class Shop {

  @Autowired
  HttpSession session;   // セッション管理

  @Autowired
  private JdbcTemplate jdbcTemplate;

  /*********************
   * アイテムの一覧を取得
   * ただし、主人公のレベルによって一覧が変化する。
   * @param release_level
   * @return
   */
  public List<ItemList> getItemList(int release_level , String job_name){

    // ジョブによって表示する内容が異なる
    // 剣士    ⇒ 区分01,02,04
    // 魔法使い⇒ 区分01,03,05

    String kbn;

    if(job_name.equals("剣士")) {
      kbn = "'01' , '02' , '04'";
    }else if (job_name.equals("魔法使い")){
      kbn = "'01' , '03' , '05'";
    }else {
      kbn = "'01' , '02' , '03' , '04' , '05'";
    }

    List<ItemList> itemlist = jdbcTemplate.query("SELECT * FROM m_item_master WHERE release_level <= '" + release_level + "' "
        + "and item_kbn in (" +  kbn + ") ORDER BY item_no",
        new BeanPropertyRowMapper<ItemList>(ItemList.class));
    return itemlist;

  }

  /********************
   * 実際の購入処理
   * メッセージを返す。
   */
  public String action_purchase(String item_no, int qty, int item_price) {

    // 返すメッセージ
    String message;

    Job job = (Job)session.getAttribute("character");

    // 現在の所持金を取得
    int have_money = job.getMoney();

    // 購入金額の計算
    int item_money = qty*item_price;

    System.out.println(item_no);


    if ( have_money < item_money) {
      message = "所持マニーが足りません";
    }else {

      // 購入アイテムのマスターデータを取得
      List<ItemList> itemlist = jdbcTemplate.query("SELECT * FROM m_item_master WHERE item_no = '" + item_no + "'",
          new BeanPropertyRowMapper<ItemList>(ItemList.class));

      // 購入アイテムのマスター
      ItemList item = itemlist.get(0);

      // アイテム所持テーブルから、購入アイテムの情報を取得
      List<Haveitem> itemhavelist = jdbcTemplate.query("SELECT * FROM m_possession_item WHERE game_no = '" + job.getGame_no() + "' and item_no = '" + item_no + "'",
          new BeanPropertyRowMapper<Haveitem>(Haveitem.class));

      if (itemhavelist.size() == 0) {
        // 新規登録
        int insertCount = jdbcTemplate.update("INSERT INTO m_possession_item "
            + "VALUES(?, ?, ?, ?, ?, ?, ?)",
            job.getGame_no(),
            item.getItem_no(),
            item.getItem_name(),
            qty,
            item.getItem_kbn(),
            item.getValue(),
            item.getDescription());

        if(insertCount == 0) {
          message = "購入に失敗しました";
        }else {
          item_money = (-1)*item_money;
          job.setMoney(item_money);
          session.setAttribute("character", job);
          message = "購入しました";
        }

      }else {
        // 追加するとき
        int sum_item_qty = itemhavelist.get(0).getItem_qty() + qty;

        int updateCount = jdbcTemplate.update("UPDATE m_possession_item "
            + "SET item_qty = ? WHERE game_no = ? and item_no = ?",
            sum_item_qty,
            job.getGame_no(),
            item.getItem_no());

        if (updateCount == 0) {
          message = "購入に失敗しました";
        }else {
          item_money = (-1)*item_money;
          job.setMoney(item_money);
          session.setAttribute("character", job);
          message = "購入しました";
        }

      }

    }

    return message;

  }

}
