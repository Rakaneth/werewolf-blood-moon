package com.rakaneth.wbm.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import squidpony.panel.IColoredString;
import squidpony.squidgrid.gui.gdx.GDXMarkup;
import squidpony.squidgrid.gui.gdx.SquidInput;

public abstract class WolfScreen {
  private String name;

  StretchViewport vport;
  Stage           stage;
  SquidInput      input;

  WolfScreen(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public abstract void render();

  public void resize(int width, int height) {
    vport.update(width, height, false);
  }

  public void enter() {
    System.out.println("Entered " + name + " screen.");
  }

  public void exit() {
    System.out.println("Exited " + name + " screen.");
  }

  void activateInput() {
    Gdx.input.setInputProcessor(new InputMultiplexer(stage, input));
  }

  protected IColoredString<Color> toICString(String markup) {
    return GDXMarkup.instance.colorString(markup);
  }
}