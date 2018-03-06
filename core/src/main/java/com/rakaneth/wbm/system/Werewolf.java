package com.rakaneth.wbm.system;

import com.badlogic.gdx.math.MathUtils;
import squidpony.squidgrid.Direction;

public class Werewolf
    extends GameObject
    implements Fighter, Actor {

  private boolean transformed;
  private float   beast;
  private float beastGain;
  private int turn;

  public Werewolf() {
    transformed = false;
    beast = 0f;
    name = "Werewolf";
    desc = "A wild-looking person";
    glyph = '@';
    color = "White";
    beastGain = 0.1f;
  }

  public float getBeast() { return beast; }
  public void changeBeast(float amt) { beast = MathUtils.clamp(beast + amt, 0f, 100f); }
  public void beastTick() { changeBeast(beastGain); }
  public int getStr() { return (int)beast / 10;}

  public boolean isTransformed() { return transformed; }
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

  public int nextTurn() { return turn; }
  public void changeNextTurn(int amt) { turn += amt; }

}
