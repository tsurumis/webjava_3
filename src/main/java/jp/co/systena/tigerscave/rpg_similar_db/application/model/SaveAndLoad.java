package jp.co.systena.tigerscave.rpg_similar_db.application.model;

import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SaveAndLoad {

  @Autowired
  HttpSession session;  // セッション管理

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  Job job;

  /*************
   * セーブデータの取得
   * @return
   */
  public List<SaveDataList> getSaveData(){
    List<SaveDataList> savedatalist = jdbcTemplate.query("SELECT * FROM m_data_master ORDER BY index_no", new BeanPropertyRowMapper<SaveDataList>(SaveDataList.class));
    return savedatalist;
  }

  /*************
   * 新しいIndex_noの取得
   */
  public String getIndex_no() {
    List<SaveDataList> savedatalist = jdbcTemplate.query("SELECT * FROM m_data_master ORDER BY index_no DESC limit 1", new BeanPropertyRowMapper<SaveDataList>(SaveDataList.class));
    String new_index_no;
    if(savedatalist.size() == 0) {
      new_index_no = "0001";
    }else {
      String latest_index_no = savedatalist.get(0).getIndex_no();
      int i_latest_index_no = Integer.parseInt(latest_index_no) + 1;
      new_index_no = String.format("%04d", i_latest_index_no);
    }

    return new_index_no;

  }

  /*************
   * データのロード
   *
   */
  public void LoadGame(String uniq_no) {

    List<Job> savedata = jdbcTemplate.query("SELECT * FROM m_character_kanri WHERE game_no = '" + uniq_no + "'", new BeanPropertyRowMapper<Job>(Job.class));
    // 旧uniq_noを取得
    String old_uniq_no = savedata.get(0).getGame_no();
    // 新uniq_noを取得
    String new_uniq_no = New_uniq_no();

    // 手持ちのアイテムをコピーする。
    int insertdatamasterCount = jdbcTemplate.update("INSERT INTO m_possession_item "
        + "(game_no , item_no, item_name, item_qty, item_kbn, value, description) "
        + "select '" + new_uniq_no + "', item_no, item_name, item_qty, item_kbn, value, description from m_possession_item "
        + "where game_no = '" + old_uniq_no + "'");

    if (insertdatamasterCount == 0) {
      ;
    }

    // 装備テーブルをコピーする。
    int insertdataequipmentCount = jdbcTemplate.update("INSERT INTO m_equipment "
        + "(game_no , item_no_1, item_name_1, item_kbn_1, value_1,"
        + " item_no_2, item_name_2, item_kbn_2, value_2) "
        + "select '" + new_uniq_no + "', item_no_1, item_name_1, item_kbn_1, value_1,"
        + "item_no_2, item_name_2, item_kbn_2, value_2 from m_equipment "
        + "where game_no = '" + old_uniq_no + "'");

    if(insertdataequipmentCount == 0) {
      ;
    }

    savedata.get(0).setGame_no(new_uniq_no);
    session.setAttribute("character", savedata.get(0));

  }

  /*************
   * 新しいuniq_noの取得
   * 本来はJobクラスで出来るはずだが、できないので別で定義する。
   */
  private String New_uniq_no() {

    Random rnd = new Random();
    int new_game_no;
    String string_game_no;

    while(true) {
      new_game_no = rnd.nextInt(8999) + 1000;
      System.out.println("new_game_no is " + new_game_no);
      string_game_no =  String.valueOf(new_game_no);

      List<GameNo> game_nolist = jdbcTemplate.query("SELECT distinct game_no FROM m_character_kanri UNION ALL SELECT 'no_data' as game_no WHERE NOT EXISTS(SELECT distinct game_no FROM m_character_kanri)",
                                  new BeanPropertyRowMapper<GameNo>(GameNo.class));

      int cnt = 0;

      for(int i = 0 ; i < game_nolist.size() ; i++){

        String game_no = game_nolist.get(0).getGame_no();

        if (game_no.equals(string_game_no)) {
          cnt = cnt + 1;
        }

      }

      if(cnt == 0) {
        break;
      }else {
        ;
      }

    }

    return string_game_no;

  }

  /*************
   * データのセーブ
   * m_data_master と m_character_kanri にデータを登録する。
   */
  public int SaveGame(String uniq_no, String game_no) {

    // セッションからキャラを取得
    Job job = (Job)session.getAttribute("character");

    if(game_no.equals("new")) {
      // 新規データの登録時
      int insertdatamasterCount = jdbcTemplate.update("INSERT INTO m_data_master "
          + "VALUES(?, ?, ?, ?, ?, ?, ?)",
          uniq_no,
          job.getGame_no(),
          job.getPlayer_name(),
          job.getJob_name(),
          job.getLevel(),
          job.getRank(),
          job.getMoney());

      int insertcharckanriCount = jdbcTemplate.update(
          "INSERT INTO m_character_kanri VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
          job.getGame_no(),
          job.getJob_name(),
          job.getLevel(),
          job.getHit_point(),
          job.getMax_hit_point(),
          job.getOffensive_power(),
          job.getDefense_power(),
          job.getHeal_power(),
          job.getSpeed(),
          job.getRank(),
          job.getExperience_point(),
          job.getMax_Experience_point(),
          job.getPlayer_name(),
          job.getMoney());

      if(insertdatamasterCount != 1 || insertcharckanriCount != 1) {
        return 1;
      }
    }else {
      // セーブデータの上書き時
      int updatemasterCount = jdbcTemplate.update(
          "UPDATE m_data_master SET "
          + "game_no = ?, player_name = ?, job_name = ?, level = ?, rank = ?, money = ? WHERE index_no = ?",
          job.getGame_no(),
          job.getPlayer_name(),
          job.getJob_name(),
          job.getLevel(),
          job.getRank(),
          job.getMoney(),
          uniq_no);

      // キャラ管理テーブルの情報更新
      /*
      int updatekanriCount = jdbcTemplate.update(
          "UPDATE m_character_kanri SET "
          + "game_no = ?, job_name = ?, level = ?, hit_point = ?, max_hit_point = ?,"
          + "offensive_power = ?, defense_power = ?, heal_power = ?, speed = ?,"
          + "rank = ?, experience_point = ?, max_experience_point = ?,"
          + "player_name = ?, money = ? WHERE game_no = ?",
          job.getGame_no(),
          job.getJob_name(),
          job.getLevel(),
          job.getHit_point(),
          job.getMax_hit_point(),
          job.getOffensive_power(),
          job.getDefense_power(),
          job.getHeal_power(),
          job.getSpeed(),
          job.getRank(),
          job.getExperience_point(),
          job.getMax_Experience_point(),
          job.getPlayer_name(),
          job.getMoney(),
          game_no);
      */

      List<Job> savedata = jdbcTemplate.query("SELECT * FROM m_character_kanri WHERE game_no = '" + job.getGame_no() + "'", new BeanPropertyRowMapper<Job>(Job.class));
      int insertcharckanriCount = 1;
      if (savedata.size() == 0) {
        insertcharckanriCount = jdbcTemplate.update(
            "INSERT INTO m_character_kanri VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            job.getGame_no(),
            job.getJob_name(),
            job.getLevel(),
            job.getHit_point(),
            job.getMax_hit_point(),
            job.getOffensive_power(),
            job.getDefense_power(),
            job.getHeal_power(),
            job.getSpeed(),
            job.getRank(),
            job.getExperience_point(),
            job.getMax_Experience_point(),
            job.getPlayer_name(),
            job.getMoney());
      }

      if(updatemasterCount != 1 || insertcharckanriCount != 1) {
        return 1;
      }
    }

    return 0;

  }

}
