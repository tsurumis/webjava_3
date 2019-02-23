package jp.co.systena.tigerscave.rpg_similar_db.application.model;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class Equipment {


  @Autowired
  HttpSession session;  // セッション管理

  @Autowired
  private JdbcTemplate jdbcTemplate;

  /*********************
   * 現在の装備を取得
   * @return
   */
  public List<EquipmentList> getEquipment(){

    Job job = (Job)session.getAttribute("character");

    List<EquipmentList> equipmentlist = jdbcTemplate.query(""
        + "SELECT * FROM m_equipment "
        + "WHERE game_no = '" + job.getGame_no() +"'", new BeanPropertyRowMapper<EquipmentList>(EquipmentList.class));

    return equipmentlist;
  }

  /**********************
   * 所持武器一覧を取得
   * @return
   */
  public List<Haveitem> getbukilist(){

    Job job = (Job)session.getAttribute("character");
    String job_name = job.getJob_name();
    String item_kbn_buki;

    if(job_name.equals("剣士")) {
      item_kbn_buki = "'02'";
    }else if(job_name.equals("魔法使い")) {
      item_kbn_buki = "'03'";
    }else {
      item_kbn_buki = "'02'";
    }

    List<Haveitem> havebukilist = jdbcTemplate.query(""
        + "SELECT * FROM m_possession_item "
        + "WHERE game_no = '" + job.getGame_no() +"' and item_kbn = " + item_kbn_buki + "" , new BeanPropertyRowMapper<Haveitem>(Haveitem.class));

    return havebukilist;

  }

  /**********************
   * 所持防具一覧を取得
   * @return
   */
  public List<Haveitem> getsoubilist(){

    Job job = (Job)session.getAttribute("character");
    String job_name = job.getJob_name();
    String item_kbn_soubi;

    if(job_name.equals("剣士")) {
      item_kbn_soubi = "'04'";
    }else if(job_name.equals("魔法使い")) {
      item_kbn_soubi = "'05'";
    }else {
      item_kbn_soubi = "'04'";
    }

    List<Haveitem> havesooubilist = jdbcTemplate.query(""
        + "SELECT * FROM m_possession_item "
        + "WHERE game_no = '" + job.getGame_no() +"' and item_kbn = " + item_kbn_soubi + "" , new BeanPropertyRowMapper<Haveitem>(Haveitem.class));

    return havesooubilist;

  }

  /*************************
   * 装備アクション
   * soubi : アイテムNo
   * soubi_kbn : buki / bougu
   */
  public String equipment_action(String game_no, String soubi_kbn, String soubi) {

    // 表示メッセージ
    String message = "";

    if (! soubi.equals("00")) {
      // 装備をする。

      // soubi(アイテムNo)で、アイテムマスターを検索し名前などを取得
      List<ItemList> itemlist = jdbcTemplate.query("SELECT * FROM m_item_master WHERE item_no = '" + soubi + "'",
          new BeanPropertyRowMapper<ItemList>(ItemList.class));
      String item_name = itemlist.get(0).getItem_name();
      String item_kbn = itemlist.get(0).getItem_kbn();
      int value = itemlist.get(0).getValue();

      String num;
      // soubi_kbnで更新のカラムを変える。
      if (soubi_kbn.equals("buki")) {
        num = "1";
      }else if(soubi_kbn.equals("bougu")) {
        num = "2";
      }else {
        num = "0";
      }

      System.out.println("num is " + num);
      System.out.println("value is " + value);

      // game_noで更新をかける。
      int updatedatamasterCount = jdbcTemplate.update("UPDATE m_equipment SET"
          + " item_no_"+ num + " = ?,"
          + "item_name_" + num + " = ?,"
          + "item_kbn_" + num + " = ?,"
          + "value_" + num + " = ? WHERE game_no = ? ",
          soubi,
          item_name,
          item_kbn,
          value,
          game_no);

      if(updatedatamasterCount == 0) {
        // 更新がなければ、新規データとして登録を行う
        int insertdatamasterCount = jdbcTemplate.update("INSERT INTO m_equipment"
            + "(game_no, item_no_"+ num +", item_name_"+ num +", item_kbn_"+ num +", value_"+ num +") "
            + "VALUES(?, ?, ?, ?, ?)",
            game_no,
            soubi,
            item_name,
            item_kbn,
            value);

        System.out.println("insertdatamasterCount is " + insertdatamasterCount);
        if(insertdatamasterCount != 0) {
          message = "装備を変更しました。";
        }else {
          message = "装備を変更しました。";
        }

      }else {
        message = "装備が完了しました。";
      }

      // 装備によるステータスの上昇(および下降)を行う
      Job job = (Job)session.getAttribute("character");
      // soubi_kbnで呼び出す関数を変える
      if (soubi_kbn.equals("buki")) {
        job.equipment_buki(value);
      }else if(soubi_kbn.equals("bougu")) {
        job.equipment_bougu(value);
      }else {
        ;
      }

      session.setAttribute("character", job);

    }else {
      // 装備を解除する。
      // 現在の装備状態の取得
      List<EquipmentList> equipentlist = getEquipment();

      int value = 0;
      String num;
      // soubi_kbnで更新のカラムを変える。
      if (soubi_kbn.equals("buki")) {
        num = "1";
        value = equipentlist.get(0).getValue_1() * (-1);
      }else if(soubi_kbn.equals("bougu")) {
        num = "2";
        value = equipentlist.get(0).getValue_2() * (-1);
      }else {
        num = "0";
      }

      // game_noで更新をかける。
      int updatedatamasterCount = jdbcTemplate.update("UPDATE m_equipment SET"
          + " item_no_"+ num + " = ?,"
          + "item_name_" + num + " = ?,"
          + "item_kbn_" + num + " = ?,"
          + "value_" + num + " = ? WHERE game_no = ? ",
          "",
          "",
          "",
          0,
          game_no);

      if(updatedatamasterCount != 0) {
        message = "装備を解除しました。";

        // 装備によるステータスの上昇(および下降)を行う
        Job job = (Job)session.getAttribute("character");
        // soubi_kbnで呼び出す関数を変える
        if (soubi_kbn.equals("buki")) {
          job.equipment_buki(value);
        }else if(soubi_kbn.equals("bougu")) {
          job.equipment_bougu(value);
        }else {
          ;
        }

        session.setAttribute("character", job);

      }else {
        message = "装備の解除に失敗しました。";
      }

    }


    return message;
  }





}
