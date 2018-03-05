package com.rakaneth.wbm.ui.screens;

import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.rakaneth.wbm.system.GameState;
import com.rakaneth.wbm.system.Werewolf;
import com.rakaneth.wbm.system.WolfRNG;
import com.rakaneth.wbm.ui.UiUtils;
import com.rakaneth.wbm.system.GameObject;
import squidpony.squidgrid.gui.gdx.*;
import squidpony.squidgrid.mapping.DungeonUtility;
import squidpony.squidgrid.mapping.SectionDungeonGenerator;
import squidpony.squidgrid.mapping.SerpentMapGenerator;
import squidpony.squidmath.Coord;
import squidpony.squidmath.GreasedRegion;


public class MainScreen extends WolfScreen {
  private SparseLayers mapLayers;
  private GameState gameState;
  private final int mapW = 100;
  private final int mapH = 36;

  public MainScreen(SpriteBatch batch) {
    super("main");
    vport = new StretchViewport(100 * UiUtils.cellWidth, 40 * UiUtils.cellHeight);
    stage = new Stage(vport, batch);
    input = new SquidInput((key, alt, ctrl, shift) -> {
      System.out.println("Key was pressed on main screen: " + key);
    });
    TextCellFactory slab = UiUtils.tweakTCF(DefaultResources.getSlabFamily(), 1.1f, 1.35f);
    mapLayers = new SparseLayers(mapW, mapH, UiUtils.cellWidth, UiUtils.cellHeight, slab);
    mapLayers.setBounds(0, UiUtils.cellHeight * 4, UiUtils.cellWidth * mapW, UiUtils.cellHeight * mapH);
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
    if (input.hasNext()) input.next();
    drawGameState();
    stage.act();
    stage.draw();
  }
}
