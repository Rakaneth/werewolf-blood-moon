package com.rakaneth.wbm.system;

import com.badlogic.gdx.math.MathUtils;
import squidpony.squidmath.Coord;

public class Werewolf
    extends Creature {

  private boolean transformed;
  private float   beast;
  private float   beastGain;


  Werewolf(Coord pos) {
    super('@', "White", "Werewolf", "A wild-looking person", pos, 3, 5, 6.0);
    transformed = false;
    beast = 30f;
    layer = 3;
  }

  public float getBeast() {
    return beast;
  }

  public void changeBeast(float amt) {
    beast = MathUtils.clamp(beast + amt, 0f, 100f);
  }

  public void beastTick(int ticks) {
    changeBeast(beastGain * ticks);
  }

  private int beastBonus() {
    return transformed ? (int) beast / 10 : 0;
  }

  @Override
  public int getStr() {
    return str + beastBonus();
  }

  @Override
  public int getSpeed() {
    return speed + beastBonus();
  }

  @Override
  public int getDef() {
    return getStr() + getSpeed();
  }

  @Override
  public double getVision() {
    return vision + beastBonus();
  }

  public boolean isTransformed() {
    return transformed;
  }

  public void shiftUp() {
    transformed = true;
    color = "Burnt Umber";
    beastGain += 0.5f;
  }

  public void shiftDown() {
    transformed = false;
    color = "White";
    beastGain -= 0.5f;
  }

  public void toggleTransform() {
    if (transformed)
      shiftDown();
    else
      shiftUp();
  }

  public boolean canSmell(GameObject thing) {
    return thing.hasScent && pos.distance(thing.pos) <= getVision() * 2;
  }


}
