package jp.co.systena.tigerscave.rpg_similar_db.application.model;

import java.util.Random;

public abstract class Human {
  // ゲームNo
  private String game_no;

  // プレイヤーの名前
  private String player_name;
  // 名前
  private String job_name;
  // HP
  private int hit_point;
  // 最大HP
  private int Max_hit_point;
  // 攻撃力
  private int Offensive_power;
  // 防御力
  private int Defense_power;
  // 回復力
  private int heal_power;
  // 素早さ
  private int speed;
  // レベル
  private int level;
  // ランク
  private String rank;
  // 現在の経験値(敵キャラでは倒すともらえる経験値とする)
  private int Experience_point;
  // 最大経験値
  private int Max_Experience_point;
  // 性別
  private String sex;
  // お金
  private int money;

  public Human() {

    // キャラを作成時にランダムでステータスを生成
    // ステータス生成後、各クラスでステータスに補正をかける。
    Random rnd = new Random();
    this.hit_point        = rnd.nextInt(40) + 80;
    this.Offensive_power  = rnd.nextInt(40) + 80;
    this.Defense_power    = rnd.nextInt(40) + 80;
    this.heal_power       = rnd.nextInt(40) + 80;
    this.speed            = rnd.nextInt(40) + 80;
    this.level            = 1;
    this.Experience_point = 0;
    this.money            = 0;
  }

  /*****************
   * 各種ステータスをセットする
   * @return
   *****************/
  // ゲームNoをセット
  public void setGame_no(String game_no) {
    this.game_no = game_no;
  }

  // キャラクターの名前をセット
  public void setPlayer_name(String player_name) {
    this.player_name = player_name;
  }

  // ジョブの名前をセット
  public void setJob_name(String job_name) {
    this.job_name = job_name;
  }

  // HPをセット
  public void setHit_point(int hit_point) {
    this.hit_point = hit_point;
  }

  // 最大HPをセット
  public void setMax_hit_point(int Max_hit_point) {
    this.Max_hit_point = Max_hit_point;
  }

  // 攻撃力をセット
  public void setOffensive_power(int attack) {
    this.Offensive_power = attack;
  }

  // 防御力をセット
  public void setDefense_power(int Defense_power) {
    this.Defense_power = Defense_power;
  }

  // 回復力をセット
  public void setHeal_power(int heal_power) {
    this.heal_power = heal_power;
  }

  // 素早さをセット
  public void setSpeed(int speed) {
    this.speed = speed;
  }

  // レベルをセット
  public void setLevel(int level) {
    this.level = level;
  }

  // ランクをセット
  public void setRank(String rank) {
    this.rank = rank;
  }

  // 経験値をセット
  public void setExperience_point(int Experience_point) {
    this.Experience_point = Experience_point;
  }

  // 最大経験値をセット
  public void setMax_Experience_point(int Max_Experience_point) {
    this.Max_Experience_point = Max_Experience_point;
  }

  // 性別をセット
  public void setSex(int sex) {
    if (sex == 1) {
      this.sex = "男性";
    }else {
      this.sex = "女性";
    }
  }

  // お金をセット
  // 追加 ⇒ +xxx
  // 消費 ⇒ -xxx
  public void setMoney(int money) {
    this.money = this.money + money;
  }

  // エネミーを倒した時にもらえるマニーをセット
  public void setEnemy_money(int money) {
    this.money = money;
  }



  /*****************
   * 各種ステータスを返す
   * @return
   *****************/
  // ゲームNoを返す
  public String getGame_no() {
    return this.game_no;
  }

  // キャラクターの名前を返す
  public String getPlayer_name() {
    return this.player_name;
  }

  // ジョブの名前を返す
  public String getJob_name() {
    return this.job_name;
  }

  // HPを返す
  public int getHit_point() {
    return this.hit_point;
  }

  // 最大HPを返す
  public int getMax_hit_point() {
    return this.Max_hit_point;
  }

  // 攻撃力を返す
  public int getOffensive_power() {
    return this.Offensive_power;
  }

  // 防御力を返す
  public int getDefense_power() {
    return this.Defense_power;
  }

  // 回復力を返す
  public int getHeal_power() {
    return this.heal_power;
  }

  // 素早さを返す
  public int getSpeed() {
    return this.speed;
  }

  // レベルを返す
  public int getLevel() {
    return this.level;
  }

  // ランクを返す
  public String getRank() {
    return this.rank;
  }

  //経験値を返す
  public int getExperience_point() {
    return this.Experience_point;
  }

  // 最大経験値を返す
  public int getMax_Experience_point() {
    return this.Max_Experience_point;
  }

  // 性別を返す
  public String getSex() {
    return this.sex;
  }

  // お金を返す
  // 返済という意味ではない
  public int getMoney() {
    return this.money;
  }

  // レベルアップまでの経験値を返す。
  public int getNext_level_point() {
    int next_ecperience = this.Max_Experience_point - this.Experience_point;
    return next_ecperience;
  }


  /*********************
   * 攻撃をする
   * 計算方法などは、各種クラスで設定
   */
  abstract int Attack(double weater_cor);

}
