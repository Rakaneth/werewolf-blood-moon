package com.rakaneth.wbm.ui.screens;

import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.rakaneth.wbm.system.*;
import com.rakaneth.wbm.system.commands.Command;
import com.rakaneth.wbm.system.commands.MoveCommand;
import com.rakaneth.wbm.system.commands.TransformCommand;
import com.rakaneth.wbm.system.commands.WaitCommand;
import com.rakaneth.wbm.ui.UiUtils;
import squidpony.squidgrid.Direction;
import squidpony.squidgrid.gui.gdx.*;
import squidpony.squidgrid.mapping.SectionDungeonGenerator;
import squidpony.squidgrid.mapping.SerpentMapGenerator;
import squidpony.squidmath.Coord;


public class MainScreen extends WolfScreen {
  private SparseLayers    mapLayers;
  private SquidMessageBox msgs;
  private SquidPanel      beastPanel;
  private SquidPanel      timePanel;
  private GameState       gameState;
  private final int mapW   = 100;
  private final int mapH   = 36;
  private final int msgW   = 34;
  private final int msgH   = 4;
  private final int beastW = 33;
  private final int beastH = 4;
  private final int timeW  = 33;
  private final int timeH  = 4;

  public MainScreen(SpriteBatch batch) {
    super("main");
    float cellWidth = UiUtils.cellWidth;
    float cellHeight = UiUtils.cellHeight;
    vport = new StretchViewport(100 * UiUtils.cellWidth, 40 * UiUtils.cellHeight);
    stage = new Stage(vport, batch);
    input = new SquidInput((key, alt, ctrl, shift) -> {
      Werewolf player = gameState.getPlayer();
      Direction direction;
      Command cmd = new WaitCommand();
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
        case 'T':
          direction = Direction.NONE;
          cmd = new TransformCommand();
        default:
          direction = Direction.NONE;
          break;
      }
      if (direction == Direction.NONE) {
        gameState.processCmd(player, cmd);
      } else {
        gameState.processCmd(player, new MoveCommand(direction));
        //gameState.processCmd(player, new WaitCommand(), gameState);
      }
      //gameState.hudDirty = true;
    });
    TextCellFactory slab = UiUtils.tweakTCF(DefaultResources.getSlabFamily(), 1.1f, 1.15f);
    mapLayers = new SparseLayers(mapW, mapH, cellWidth, cellHeight, slab);
    mapLayers.setBounds(0, cellHeight * 4, cellWidth * mapW, cellHeight * mapH);
    msgs = new SquidMessageBox(msgW, msgH, slab.copy());
    msgs.setBounds(0, 0, msgW * cellWidth, msgH * cellHeight);
    beastPanel = new SquidPanel(beastW, beastH, slab.copy());
    beastPanel.setBounds(msgW * cellWidth, 0, beastW * cellWidth, beastH * cellWidth);
    timePanel = new SquidPanel(timeW, timeH, slab.copy());
    timePanel.setBounds((msgW + beastW) * cellWidth, 0, timeW * cellWidth, timeH * cellHeight);
    stage.addActor(msgs);
    stage.addActor(beastPanel);
    stage.addActor(timePanel);
    stage.addActor(mapLayers);
  }

  private void newGame() {
    gameState = new GameState(msgs);
    SerpentMapGenerator smg = new SerpentMapGenerator(250, 250, WolfRNG.getRNG(), 0.2);
    SectionDungeonGenerator sdg = new SectionDungeonGenerator(250, 250, WolfRNG.getRNG());
    smg.putCaveCarvers(10);
    char[][] baseMap = smg.generate();
    sdg.addBoulders(SectionDungeonGenerator.CAVE, 15);
    sdg.addLake(5);
    sdg.addGrass(SectionDungeonGenerator.CAVE, 25);
    char[][] finalMap = sdg.generate(baseMap, smg.getEnvironment());
    gameState.setMap(finalMap);
    gameState.addPlayer(gameState.randomFloor());
    for (int i = 0; i < 30; i++) {
      String[] choices = new String[]{"deer", "rabbit", "bear"};
      String choice = WolfRNG.getRNG().getRandomElement(choices);
      Creature newCritter = Creature.makeAnimal(choice);
      newCritter.setPos(gameState.randomFloor());
      gameState.addEntity(newCritter);
    }
  }

  private boolean isOOB(int x, int y) {
    return x < 0 || x >= 250 || y < 0 || y >= 250;
  }

  private void drawGameState() {
    mapLayers.clear();
    Werewolf player = gameState.getPlayer();
    int left = MathUtils.clamp(player.getPos().x - mapW / 2, 0, Math.max(0, 250 - mapW));
    int top = MathUtils.clamp(player.getPos().y - mapH / 2, 0, Math.max(0, 250 - mapH));

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
          mapLayers.put(x - left, y - top, toDisplay, Colors.get(toColor));
        }
      }
    }

    for (GameObject thing : gameState.getThings()) {
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
    msgs.putBordersCaptioned(SColor.WHITE, UiUtils.toICString("Messages"));
  }

  private void drawBeast() {
    Werewolf player = gameState.getPlayer();
    beastPanel.erase();
    beastPanel.putBordersCaptioned(SColor.WHITE, UiUtils.toICString("Beast"));
    beastPanel.put(1, 1, "Man");
    beastPanel.put(28, 1, "Wolf");
    UiUtils.drawBar(beastPanel, 1, 2, 31, player.getBeast(), 100f, SColor.CRIMSON,
                    SColor.DARK_BLUE_DYE);
  }

  private void drawClock() {
    timePanel.erase();
    int time = gameState.getClock();
    timePanel.put(1, 1, UiUtils.toICString("Current time: [Light Blue]" + String.valueOf(time) + "[]"));
    timePanel.put(1, 2,
                  UiUtils.toICString("Current position: [Light Blue]" + gameState.getPlayer().getPos()));
  }

  private void drawHUD() {
    drawMsgs();
    drawBeast();
    drawClock();
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
  public void render() {
    if (gameState.mapDirty) {
      drawGameState();
      gameState.mapDirty = false;
    }
    if (gameState.hudDirty) {
      drawHUD();
      gameState.hudDirty = false;
    }
    if (gameState.isPaused()) {
      if (input.hasNext()) {
        input.next();
      }
    }
    gameState.update();

    stage.act();
    stage.draw();
  }
}
