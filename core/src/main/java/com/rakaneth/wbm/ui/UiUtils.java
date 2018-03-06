package com.rakaneth.wbm.ui;

import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SquidPanel;
import squidpony.squidgrid.gui.gdx.TextCellFactory;

public final class UiUtils {
  public static final float cellWidth  = 16f;
  public static final float cellHeight = 20f;

  public static TextCellFactory tweakTCF(TextCellFactory tcf, float tweakWidth, float tweakHeight) {
    return tcf.width(cellWidth)
              .height(cellHeight)
              .tweakWidth(cellWidth * tweakWidth)
              .tweakHeight(cellHeight * tweakHeight)
              .initBySize();
  }

  public static void drawBar(SquidPanel panel, int x, int y, int length, float curNum, float maxNum, SColor fillColor,
                             SColor
      emptyColor) {
    int toFill = (int)(curNum * length / maxNum);
    for (int i = x; i < x + length; i++) {
      panel.put(i, y, emptyColor);
    }
    for (int j = x; j < x + toFill; j++) {
      panel.put(j, y, fillColor);
    }
  }
}
