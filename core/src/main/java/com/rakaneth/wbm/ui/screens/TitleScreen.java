package com.rakaneth.wbm.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.rakaneth.wbm.ui.UiUtils;
import squidpony.squidgrid.gui.gdx.*;

public class TitleScreen extends WolfScreen {
  private SpriteBatch     batch;
  private SquidInput      input;
  private Stage           stage;
  private StretchViewport vport;
  private TextCellFactory slab;
  private SparseLayers    display;

  public TitleScreen(SpriteBatch batch) {
    super("title");
    this.batch = batch;
    slab = UiUtils.tweakTCF(DefaultResources.getSlabFamily(), 1.1f, 1.35f);
    vport = new StretchViewport(UiUtils.cellWidth * 100, UiUtils.cellHeight * 40);
    stage = new Stage(vport, this.batch);
    input = new SquidInput((key, alt, ctrl, shift) -> {
      System.out.println("Key was pressed: " + String.valueOf(key));
    });
    display = new SparseLayers(100, 40, UiUtils.cellWidth, UiUtils.cellHeight, slab);
    stage.addActor(display);
    Gdx.input.setInputProcessor(new InputMultiplexer(stage, input));
  }

  @Override
  public void render() {
    display.put(0, 0, "Werewolf: Blood Moon", SColor.WHITE, null);
    if (input.hasNext()) input.next();
    stage.act();
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    vport.update(width, height, false);
  }
}
