package com.rakaneth.wbm.system;

import com.badlogic.gdx.graphics.Color;
import com.rakaneth.wbm.system.commands.Command;
import com.rakaneth.wbm.system.commands.WaitCommand;
import com.rakaneth.wbm.ui.UiUtils;
import squidpony.panel.IColoredString;
import squidpony.squidgrid.gui.gdx.SquidMessageBox;
import squidpony.squidgrid.mapping.DungeonUtility;
import squidpony.squidmath.Coord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GameState {
  private List<GameObject> things = new ArrayList<>();
  private char[][] gameMap;
  private Werewolf player;
  private DungeonUtility utility  = new DungeonUtility();
  public  boolean        hudDirty = true;
  public  boolean        mapDirty = true;
  private boolean paused;
  private int clock = 0;
  private SquidMessageBox msgs;

  public GameState(SquidMessageBox msgs) {
    this.msgs = msgs;
  }

  public void addEntity(GameObject thing) {
    things.add(thing);
  }

  public void removeEntity(GameObject thing) {
    things.remove(thing);
  }

  public void addActor(Actor actor) {
    addEntity((GameObject) actor);
  }

  public void removeActor(Actor actor) {
    removeEntity((GameObject) actor);
  }

  public void addPlayer(Coord pos) {
    player = new Werewolf(pos);
    addActor(player);
  }

  public Werewolf getPlayer() {
    return player;
  }

  public char[][] getMap() {
    return gameMap;
  }

  public void setMap(char[][] newMap) {
    gameMap = newMap;
  }

  public Coord randomFloor() {
    return utility.randomFloor(gameMap);
  }

  public List<GameObject> getThings() {
    return things;
  }


  public void doUpkeep(Integer ticks) {
    player.beastTick(ticks);
  }

  public Command getAction(Actor actor) {
    return new WaitCommand();
  }

  public void processCmd(Actor actor, Command cmd) {
    int energySpent = cmd.execute(actor, this);
    actor.setEnergy(actor.getEnergy() - energySpent);
    System.out.printf("%s spends %d energy %s; has %d\n", actor, energySpent, cmd, actor.getEnergy());
    if (energySpent > 0 && paused) paused = false;
  }

  public void update() {
    List<Actor> toAct = things.stream()
                              .filter(Actor.class::isInstance)
                              .map(f -> (Actor) f)
                              .sorted(Comparator.comparing(Actor::getSpeed).reversed())
                              .collect(Collectors.toList());

    if (!paused) {
      for (Actor actor : toAct) {
        int nrg = actor.getEnergy();
        if (nrg >= 10) {
          System.out.println(actor.toString() + " acts with " + String.valueOf(nrg) + " energy.");
          if (actor == player) {
            paused = true;
            return;
          } else {
            processCmd(actor, getAction(actor));
          }
        } else {
          int spd = actor.getSpeed();
          int newNRG = spd + actor.getEnergy();
          System.out.printf("%s gains %d energy; has %d\n", actor, spd, newNRG);
          actor.setEnergy(newNRG + spd);
        }
      }
      clock++;
      doUpkeep(1);
    }
  }

  public int getClock() {
    return clock;
  }

  public boolean isPaused() {
    return paused;
  }

  public void addMessage(IColoredString<Color> msg) {
    msgs.appendWrappingMessage(msg);
  }

  public void addMessage(String msg) {
    msgs.appendWrappingMessage(msg);
  }

  public void addMessageF(String template, Object... args) {
    IColoredString<Color> toWrite = UiUtils.toICString(String.format(template, args));
    addMessage(toWrite);
  }
}
