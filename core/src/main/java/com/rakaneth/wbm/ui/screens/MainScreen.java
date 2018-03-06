package com.rakaneth.wbm.ui.screens;

import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.rakaneth.wbm.system.*;
import com.rakaneth.wbm.system.commands.Command;
import com.rakaneth.wbm.system.commands.MoveCommand;
import com.rakaneth.wbm.ui.UiUtils;
import javafx.util.Pair;
import squidpony.squidgrid.Direction;
import squidpony.squidgrid.gui.gdx.*;
import squidpony.squidgrid.mapping.DungeonUtility;
import squidpony.squidgrid.mapping.SectionDungeonGenerator;
import squidpony.squidgrid.mapping.SerpentMapGenerator;
import squidpony.squidmath.Coord;
import squidpony.squidmath.GreasedRegion;


public class MainScreen extends WolfScreen {
  private SparseLayers mapLayers;
  private SquidMessageBox msgs;
  private SquidPanel beastPanel;
  private GameState gameState;
  private final int mapW = 100;
  private final int mapH = 36;
  private final int msgW = 34;
  private final int msgH = 4;
  private final int beastW = 33;
  private final int beastH = 4;

  public MainScreen(SpriteBatch batch) {
    super("main");
    float cellWidth = UiUtils.cellWidth;
    float cellHeight = UiUtils.cellHeight;
    vport = new StretchViewport(100 * UiUtils.cellWidth, 40 * UiUtils.cellHeight);
    stage = new Stage(vport, batch);
    input = new SquidInput((key, alt, ctrl, shift) -> {
      Scheduler engine = gameState.getEngine();
      Direction direction = Direction.NONE;
      switch (key) {
        case SquidInput.RIGHT_ARROW:
          direction = Direction.RIGHT;
          break;
        case SquidInput.DOWN_RIGHT_ARROW:
          direction = Direction.DOWN_RIGHT;
          break;
        case SquidInput.DOWN_ARROW:
          direction = Direction.DOWN;
          break;
        case SquidInput.DOWN_LEFT_ARROW:
          direction = Direction.DOWN_LEFT;
          break;
        case SquidInput.LEFT_ARROW:
          direction = Direction.LEFT;
          break;
        case SquidInput.UP_LEFT_ARROW:
          direction = Direction.UP_LEFT;
          break;
        case SquidInput.UP_ARROW:
          direction = Direction.UP;
          break;
        case SquidInput.UP_RIGHT_ARROW:
          direction = Direction.UP_RIGHT;
          break;
        default:
          direction = Direction.NONE;
          break;
      }
      if (direction != Direction.NONE) {
        engine.processCmd(gameState.getPlayer(), new MoveCommand(direction), gameState);
      }
    });
    TextCellFactory slab = UiUtils.tweakTCF(DefaultResources.getSlabFamily(), 1.1f, 1.35f);
    mapLayers = new SparseLayers(mapW, mapH, cellWidth, cellHeight, slab);
    mapLayers.setBounds(0,cellHeight * 4, cellWidth * mapW, cellHeight * mapH);
    msgs = new SquidMessageBox(msgW, msgH, slab.copy());
    msgs.setBounds(0, 0, msgW * cellWidth, msgH * cellHeight);
    msgs.appendWrappingMessage(toICString("[Green][*]Welcome[] to Werewolf: Blood Moon!"));
    beastPanel = new SquidPanel(beastW, beastH, slab.copy());
    beastPanel.setBounds(msgW * cellWidth, 0, beastW * cellWidth, beastH * cellWidth);
    stage.addActor(msgs);
    stage.addActor(beastPanel);
    stage.addActor(mapLayers);
  }

  private void newGame() {
    gameState = new GameState();
    gameState.addPlayer();
    SerpentMapGenerator smg = new SerpentMapGenerator(250, 250, WolfRNG.getRNG(), 0.2);
    SectionDungeonGenerator sdg = new SectionDungeonGenerator(250, 250, WolfRNG.getRNG());
    smg.putCaveCarvers(10);
    char[][] baseMap = smg.generate();
    sdg.addBoulders(SectionDungeonGenerator.CAVE, 15);
    sdg.addLake(5);
    sdg.addGrass(SectionDungeonGenerator.CAVE, 25);
    char[][] finalMap = sdg.generate(baseMap, smg.getEnvironment());
    gameState.setMap(finalMap);
    gameState.getPlayer().setPos(gameState.randomFloor());
  }

  private boolean isOOB(int x, int y) {
    return x < 0 || x >= 250 || y < 0 || y >= 250;
  }

  private void drawGameState() {
    Werewolf player = gameState.getPlayer();
    int left = MathUtils.clamp(player.getPos().x - mapW/2, 0, Math.max(0, 250-mapW));
    int top = MathUtils.clamp(player.getPos().y - mapH/2, 0, Math.max(0, 250-mapH));

    for (int x = left; x < left + mapW; x++) {
      for (int y = top; y < top + mapH; y++) {
         if (!isOOB(x, y)) {
           char tile = gameState.getMap()[x][y];
           char toDisplay;
           String toColor;
           switch (tile) {
             case '#':
               toDisplay = '\u2663';
               toColor = "Green";
               break;
             case ',':
               toDisplay = '~';
               toColor = "CW Light Blue";
               break;
             case '~':
               toDisplay = '~';
               toColor = "Blue";
               break;
             case ':':
               toDisplay = ':';
               toColor = "Silver Grey";
               break;
             case '"':
               toDisplay = '"';
               toColor = "Green";
               break;
             default:
               toDisplay = '.';
               toColor = "CW Light Brown";
               break;
           }
           mapLayers.put(x-left, y-top, toDisplay, (SColor) Colors.get(toColor));
         }
      }
    }

    for (GameObject thing: gameState.getThings()) {
      Coord pos = thing.getPos();
      int toX = pos.x - left;
      int toY = pos.y - top;
      if (toX >= 0 && toX < mapW && toY >= 0 && toY < mapH) {
        mapLayers.put(toX, toY, thing.getGlyph(), thing.getColor());
      }
    }
  }

  private void drawMsgs() {
    msgs.erase();
    msgs.putBordersCaptioned(SColor.WHITE, toICString("Messages"));
  }

  private void drawBeast() {
    Werewolf player = gameState.getPlayer();
    beastPanel.erase();
    beastPanel.putBordersCaptioned(SColor.WHITE, toICString("Beast"));
    beastPanel.put(1, 1, "Man");
    beastPanel.put(28, 1, "Wolf");
    UiUtils.drawBar(beastPanel, 2, 2, 30, player.getBeast(), 100f, SColor.GOLD, SColor.CRIMSON);
  }

  private void drawClock() {

  }

  private void drawHUD() {
    drawMsgs();
    drawBeast();
    //drawClock();
  }

  @Override
  public void enter() {
    activateInput();
    if (gameState == null) {
      newGame();
    }
    super.enter();
  }

  @Override
  public void render () {
    Scheduler engine = gameState.getEngine();
    if (engine.isPaused()) {
      if (input.hasNext()) input.next();
    } else {
      Pair<Actor, Integer> upNext = engine.getNext();
      if (upNext.getKey() == gameState.getPlayer()) {
        engine.pause();
      } else {
        gameState.doUpkeep(upNext.getValue());
        Command cmd = gameState.getAction(upNext.getKey());
        engine.processCmd(upNext.getKey(), cmd, gameState);
      }
    }
    if (gameState.mapDirty) drawGameState();
    if (gameState.hudDirty) drawHUD();
    stage.act();
    stage.draw();
  }
}
