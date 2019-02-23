package jp.co.systena.tigerscave.rpg_similar_db.application.model;

public class EquipmentList {

  private String game_no;

  private String item_no_1;

  private String item_name_1;

  private String item_kbn_1;

  private int value_1;

  private String item_no_2;

  private String item_name_2;

  private String item_kbn_2;

  private int value_2;


  // setter
  public void setGame_no(String game_no) {
    this.game_no = game_no;
  }

  public void setItem_no_1(String item_no_1){
    this.item_no_1 = item_no_1;
  }

  public void setItem_name_1(String item_name_1) {
    this.item_name_1 = item_name_1;
  }

  public void setItem_kbn_1(String item_kbn_1) {
    this.item_kbn_1 = item_kbn_1;
  }

  public void setValue_1(int value_1) {
    this.value_1 = value_1;
  }

  public void setItem_no_2(String item_no_2){
    this.item_no_2 = item_no_2;
  }

  public void setItem_name_2(String item_name_2) {
    this.item_name_2 = item_name_2;
  }

  public void setItem_kbn_2(String item_kbn_2) {
    this.item_kbn_2 = item_kbn_2;
  }

  public void setValue_2(int value_2) {
    this.value_2 = value_2;
  }

  // getter
  public String getGame_no() {
    return this.game_no;
  }

  public String getItem_no_1() {
    return this.item_no_1;
  }

  public String getItem_name_1() {
    return this.item_name_1;
  }

  public String getItem_kbn_1() {
    return this.item_kbn_1;
  }

  public int getValue_1() {
    return this.value_1;
  }

  public String getItem_no_2() {
    return this.item_no_2;
  }

  public String getItem_name_2() {
    return this.item_name_2;
  }

  public String getItem_kbn_2() {
    return this.item_kbn_2;
  }

  public int getValue_2() {
    return this.value_2;
  }
}
