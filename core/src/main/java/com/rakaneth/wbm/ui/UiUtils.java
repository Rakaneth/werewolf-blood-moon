package com.rakaneth.wbm.ui;

import squidpony.squidgrid.gui.gdx.TextCellFactory;

public final class UiUtils {
  public static final float cellWidth  = 16f;
  public static final float cellHeight = 24f;

  public static TextCellFactory tweakTCF(TextCellFactory tcf, float tweakWidth, float tweakHeight) {
    return tcf.width(cellWidth)
              .height(cellHeight)
              .tweakWidth(cellWidth * tweakWidth)
              .tweakHeight(cellHeight * tweakHeight)
              .initBySize();
  }
}
