package jp.co.systena.tigerscave.rpg_similar_db.application.model;

public class SaveDataList {

  private String game_no;

  private String player_name;

  private String job_name;

  private int level;

  private String rank;

  private int money;

  private String index_no;

  // setter
  public void setGame_no(String game_no) {
    this.game_no = game_no;
  }

  public void setPlayer_name(String player_name) {
    this.player_name = player_name;
  }

  public void setJob_name(String job_name) {
    this.job_name = job_name;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public void setRank(String rank) {
    this.rank = rank;
  }

  public void setMoney(int money) {
    this.money = money;
  }

  public void setIndex_no(String index_no) {
    this.index_no = index_no;
  }

  //getter
  public String getGame_no() {
    return this.game_no;
  }

  public String getPlayer_name() {
    return this.player_name;
  }

  public String getJob_name() {
    return this.job_name;
  }

  public int getLevel() {
    return this.level;
  }

  public String getRank() {
    return this.rank;
  }

  public int getMoney() {
    return this.money;
  }

  public String getIndex_no() {
    return this.index_no;
  }

}
