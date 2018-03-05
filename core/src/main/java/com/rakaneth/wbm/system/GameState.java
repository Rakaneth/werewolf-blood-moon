package com.rakaneth.wbm.system;

import squidpony.squidgrid.mapping.DungeonUtility;
import squidpony.squidmath.Coord;

import java.util.ArrayList;
import java.util.List;

public class GameState {
  private List<GameObject> things = new ArrayList<>();
  private char[][] gameMap;
  private Scheduler engine = new Scheduler();
  private Werewolf player;
  private DungeonUtility utility = new DungeonUtility();

  public void addEntity(GameObject thing) { things.add(thing); }
  public void removeEntity(GameObject thing) { things.remove(thing); }

  public void addActor(Actor actor) {
    addEntity((GameObject)actor);
    engine.add(actor);
  }

  public void removeActor(Actor actor) {
    removeEntity((GameObject)actor);
    engine.remove(actor);
  }

  public void addPlayer() {
    player = new Werewolf();
    addActor(player);
  }

  public Werewolf getPlayer() { return player; }

  public char[][] getMap() { return gameMap; }
  public void setMap(char[][] newMap) { gameMap = newMap; }

  public Coord randomFloor() { return utility.randomFloor(gameMap); }

  public List<GameObject> getThings() { return things; }

}
