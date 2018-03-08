package com.rakaneth.wbm.system;

import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.rakaneth.wbm.system.commands.Command;
import com.rakaneth.wbm.system.commands.WaitCommand;
import com.rakaneth.wbm.ui.UiUtils;
import squidpony.ArrayTools;
import squidpony.panel.IColoredString;
import squidpony.squidgrid.FOV;
import squidpony.squidgrid.gui.gdx.SquidMessageBox;
import squidpony.squidgrid.mapping.DungeonUtility;
import squidpony.squidgrid.mapping.SectionDungeonGenerator;
import squidpony.squidgrid.mapping.SerpentMapGenerator;
import squidpony.squidmath.Coord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameState {
  private char[][]        gameMap;
  private Werewolf        player;
  private boolean         paused;
  private SquidMessageBox msgs;
  private double[][]      resistances;
  private              DungeonUtility    utility     = new DungeonUtility();
  public               boolean           hudDirty    = true;
  public               boolean           mapDirty    = true;
  private              List<GameObject>  things      = new ArrayList<>();
  private              int               clock       = 0;
  private static final String            seTemplate  = "%s spends %d energy %s; has %d";
  private static final String            actTemplate = "%s acts with %d energy.";
  private static final String            geTemplate  = "%s gains %d energy; has %d";
  private static final ApplicationLogger logger      = Gdx.app.getApplicationLogger();

  public GameState(SquidMessageBox msgs) {
    this.msgs = msgs;
  }

  public void addThing(GameObject thing) {
    things.add(thing);
  }

  public void addCreature(Creature creature) {
    creature.visible = ArrayTools.fill(0.0, gameMap.length, gameMap[0].length);
    addThing(creature);
    updateFOV(creature);
  }

  public void removeEntity(GameObject thing) {
    things.remove(thing);
  }

  public void addPlayer(Coord pos) {
    player = new Werewolf(pos);
    addCreature(player);
  }

  public Werewolf getPlayer() {
    return player;
  }

  public char[][] getMap() {
    return gameMap;
  }

  public void newMap() {
    SerpentMapGenerator smg = new SerpentMapGenerator(250, 250, WolfRNG.getRNG(), 0.2);
    SectionDungeonGenerator sdg = new SectionDungeonGenerator(250, 250, WolfRNG.getRNG());
    smg.putCaveCarvers(10);
    char[][] baseMap = smg.generate();
    sdg.addBoulders(SectionDungeonGenerator.CAVE, 15);
    sdg.addLake(5);
    sdg.addGrass(SectionDungeonGenerator.CAVE, 25);
    gameMap = sdg.generate(baseMap, smg.getEnvironment());
    resistances = DungeonUtility.generateResistances(gameMap);
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
    log(seTemplate, actor, energySpent, cmd, actor.getEnergy());
    if (energySpent > 0 && paused) paused = false;
  }

  public void update() {
    List<Actor> toAct = things.stream()
                              .filter(Actor.class::isInstance)
                              .map(f -> (Actor) f)
                              .sorted(Comparator.comparing(Actor::getEnergy).reversed())
                              .collect(Collectors.toList());

    if (!paused) {
      for (Actor actor : toAct) {
        int nrg = actor.getEnergy();
        if (nrg >= 10) {
          log(actTemplate, actor, nrg);
          if (actor == player) {
            paused = true;
            return;
          } else {
            processCmd(actor, getAction(actor));
          }
        } else {
          int spd = actor.getSpeed();
          int newNRG = spd + actor.getEnergy();
          log(geTemplate, actor, spd, newNRG);
          actor.setEnergy(newNRG);
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

  private void log(String template, Object... args) {
    logger.log("Game State", String.format(template, args));
  }

  public void updateFOV(Creature creature) {
    FOV.reuseFOV(resistances, creature.visible, creature.pos.x, creature.pos.y, creature.getVision());
  }

  public List<GameObject> thingsAt(Coord c) {
    return things.stream()
                 .filter(f -> f.pos == c)
                 .collect(Collectors.toList());
  }

  public Optional<Creature> creatureAt(Coord c) {
    return thingsAt(c).stream()
                      .filter(f -> f instanceof Creature)
                      .map(m -> (Creature) m)
                      .findAny();
  }
}
