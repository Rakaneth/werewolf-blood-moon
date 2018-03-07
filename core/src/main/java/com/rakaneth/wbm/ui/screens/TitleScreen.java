package com.rakaneth.wbm.ui.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.rakaneth.wbm.ui.UiUtils;
import squidpony.squidgrid.gui.gdx.*;

public class TitleScreen extends WolfScreen {
  private SparseLayers display;

  public TitleScreen(SpriteBatch batch) {
    super("title");
    TextCellFactory slab = UiUtils.tweakTCF(DefaultResources.getSlabFamily(), 1.1f, 1.35f);
    vport = new StretchViewport(UiUtils.cellWidth * 100, UiUtils.cellHeight * 40);
    stage = new Stage(vport, batch);
    input = new SquidInput((key, alt, ctrl, shift) -> {
      ScreenManager.setScreen("main");
    });
    display = new SparseLayers(100, 40, UiUtils.cellWidth, UiUtils.cellHeight, slab);
    stage.addActor(display);
  }

  @Override
  public void render() {
    display.put(0, 0, "Werewolf: Blood Moon", SColor.WHITE, null);
    if (input.hasNext()) input.next();
    stage.act();
    stage.draw();
  }

  @Override
  public void enter() {
    activateInput();
    super.enter();
  }
}
