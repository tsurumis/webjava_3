package jp.co.systena.tigerscave.rpg_similar_db.application.model;

public class Form {

  private String character_name;

  private int sex;

  private String save_or_load;

  // 何番目のセーブデータか？
  private String game_no;

  // ショップでどのアイテムを購入したか？
  private String item_no;

  // ショップで何個買ったか？
  private int qty;

  // ショップで購入したアイテムの単価
  private int item_price;

  // プレイ時に作成される[m_character_kanri]のuniqなキー
  private String uniq_no;

  // 所持アイテムの値(効果値)
  private int value;

  // 所持アイテムの個数
  private int item_qty;

  /*********
   * 装備画面で使う
   *********/
  private String soubi;

  private String soubi_kbn;

  // setter
  public void setCharacter_name(String character_name) {
    this.character_name = character_name;
  }

  public void setSex(int sex) {
    this.sex = sex;
  }

  public void setSave_or_load(String save_or_load) {
    this.save_or_load = save_or_load;
  }

  public void setGame_no(String game_no) {
    this.game_no = game_no;
  }

  public void setItem_no(String item_no) {
    this.item_no = item_no;
  }

  public void setQty(int qty) {
    this.qty = qty;
  }

  public void setItem_price(int item_price) {
    this.item_price = item_price;
  }

  public void setUniq_no(String uniq_no) {
    this.uniq_no = uniq_no;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public void setItem_qty(int item_qty) {
    this.item_qty = item_qty;
  }

  public void setSoubi(String soubi) {
    this.soubi = soubi;
  }

  public void setSoubi_kbn(String soubi_kbn) {
    this.soubi_kbn = soubi_kbn;
  }

  // getter
  public String getCharacter_name() {
    return this.character_name;
  }

  public int getSex() {
    return this.sex;
  }

  public String getSave_or_load() {
    return this.save_or_load;
  }

  public String getGame_no() {
    return this.game_no;
  }

  public String getItem_no() {
    return this.item_no;
  }

  public int getQty() {
    return this.qty;
  }

  public int getItem_price() {
    return this.item_price;
  }

  public String getUniq_no() {
    return this.uniq_no;
  }

  public int getValue() {
    return this.value;
  }

  public int getItem_qty() {
    return this.item_qty;
  }

  public String getSoubi() {
    return this.soubi;
  }

  public String getSoubi_kbn() {
    return this.soubi_kbn;
  }

}
