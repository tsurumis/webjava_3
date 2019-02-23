package jp.co.systena.tigerscave.rpg_similar_db.application.model;

public class Haveitem {

  private String game_no;

  private String item_no;

  private String item_name;

  private int item_qty;

  private String item_kbn;

  private int value;


  // setter
  public void setGame_no(String game_no) {
    this.game_no = game_no;
  }

  public void setItem_no(String item_no) {
    this.item_no = item_no;
  }

  public void setItem_name(String item_name) {
    this.item_name = item_name;
  }

  public void setItem_qty(int item_qty) {
    this.item_qty = item_qty;
  }

  public void setItem_kbn(String item_kbn) {
    this.item_kbn = item_kbn;
  }

  public void setValue(int value) {
    this.value = value;
  }

  // getter
  public String getGame_no() {
    return this.game_no;
  }

  public String getItem_no() {
    return this.item_no;
  }

  public String getItem_name() {
    return this.item_name;
  }

  public int getItem_qty() {
    return this.item_qty;
  }

  public String getItem_kbn() {
    return this.item_kbn;
  }

  public int getValue() {
    return this.value;
  }



}
