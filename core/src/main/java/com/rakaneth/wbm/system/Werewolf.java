package com.rakaneth.wbm.system;

import com.badlogic.gdx.math.MathUtils;
import squidpony.squidmath.Coord;

public class Werewolf
    extends Creature {

  private boolean transformed;
  private float   beast;
  private float   beastGain;


  Werewolf(Coord pos) {
    super('@', "White", "Werewolf", "A wild-looking person", pos, 3, 5);
    transformed = false;
    beast = 30f;
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
    return (int) beast / 10;
  }

  @Override
  public int getStr() {
    return transformed ? str + beastBonus() : str;
  }

  @Override
  public int getSpeed() {
    return Math.max(beastBonus(), speed);
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


}
