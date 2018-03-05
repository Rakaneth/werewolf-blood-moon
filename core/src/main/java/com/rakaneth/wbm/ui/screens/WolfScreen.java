package com.rakaneth.wbm.ui.screens;

import com.badlogic.gdx.utils.viewport.StretchViewport;

public abstract class WolfScreen {
  private String name;
  private StretchViewport vport;

  WolfScreen(String name) {
    this.name = name;
  }

  public String getName() { return name; }

  public abstract void render();
  public abstract void resize(int width, int height);

  public void enter() { System.out.println("Entered " + name + " screen."); }
  public void exit() { System.out.println("Exited " + name + " screen."); }

}