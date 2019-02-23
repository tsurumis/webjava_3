package jp.co.systena.tigerscave.rpg_similar_db.application.model;

public class ItemList {
  // アイテムNo
  private String item_no;
  // アイテム名
  private String item_name;
  // アイテム区分
  private String item_kbn;
  // 金額
  private int price;
  // 解放レベル
  private int release_level;
  // 値
  private int value;
  // 説明文
  private String description;

  // setter
  public void setItem_no(String item_no) {
    this.item_no = item_no;
  }

  public void setItem_name(String item_name) {
    this.item_name = item_name;
  }

  public void setItem_kbn(String item_kbn) {
    this.item_kbn = item_kbn;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public void setRelease_level(int release_level) {
    this.release_level = release_level;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  // getter
  public String getItem_no() {
    return this.item_no;
  }

  public String getItem_name() {
    return this.item_name;
  }

  public String getItem_kbn() {
    return this.item_kbn;
  }

  public int getPrice() {
    return this.price;
  }

  public int getRelease_level() {
    return this.release_level;
  }

  public int getValue() {
    return this.value;
  }

  public String getDescription() {
    return this.description;
  }

}
