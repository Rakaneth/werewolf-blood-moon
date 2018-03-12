package com.rakaneth.wbm.system;

import squidpony.squidmath.Coord;

import java.util.Map;


public class Creature
    extends GameObject
    implements Actor, Fighter {
  int        energy;
  int        speed;
  int        str;
  double     vision;
  double[][] visible;
  boolean isPredator;
  static Map<String, Creature> bluePrints;

  public Creature() {
  }

  public Creature(char glyph, String color, String name, String desc, Coord pos, int str, int speed, double vision) {
    this.glyph = glyph;
    this.color = color;
    this.name = name;
    this.desc = desc;
    this.pos = pos;
    this.str = str;
    this.speed = speed;
    this.vision = vision;
    hasScent = true;
    layer = 2;
  }

  public Creature(Creature c) {
    glyph = c.glyph;
    color = c.color;
    name = c.name;
    desc = c.desc;
    pos = c.pos;
    str = c.str;
    speed = c.speed;
    vision = c.vision;
    hasScent = true;
  }

  public int getEnergy() {
    return energy;
  }

  public void setEnergy(int val) {
    energy = val;
  }

  public int getSpeed() {
    return speed;
  }

  public int getStr() {
    return str;
  }

  public double getVision() {
    return vision;
  }

  public boolean getIsPredator() { return isPredator; }

  public double[][] getVisible() {
    return visible;
  }

  public void setVisible(double[][] visMap) {
    visible = visMap;
  }

  public boolean canSee(int x, int y) {
    return visible[x][y] > 0.0;
  }

  public boolean canSee(Coord c) {
    return canSee(c.x, c.y);
  }

  public boolean canSee(GameObject other) {
    return canSee(other.pos);
  }

  public int getDef() { return str + speed; }

  public boolean canSmell(GameObject thing) {
    return thing.hasScent && pos.distance(thing.pos) <= getVision() * 2;
  }

}
