package com.rakaneth.wbm;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rakaneth.wbm.ui.screens.MainScreen;
import com.rakaneth.wbm.ui.screens.ScreenManager;
import com.rakaneth.wbm.ui.screens.TitleScreen;
import squidpony.squidgrid.gui.gdx.SColor;

public class WerewolfGame extends ApplicationAdapter {
  private SColor bgColor = SColor.DARK_SLATE_GRAY;

  @Override
  public void create() {
    SpriteBatch batch = new SpriteBatch();
    ScreenManager.register(new TitleScreen(batch),
                           new MainScreen(batch));
    ScreenManager.setScreen("title");
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(bgColor.r / 255.0f, bgColor.g / 255.0f, bgColor.b / 255.0f, 1.0f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    ScreenManager.getCurScreen().render();
  }

  @Override
  public void resize(int width, int height) {
    ScreenManager.getCurScreen().resize(width, height);
  }
}
