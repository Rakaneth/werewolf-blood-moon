package com.rakaneth.wbm.system;

import com.badlogic.gdx.graphics.Colors;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidmath.Coord;
import squidpony.squidmath.SquidID;

public abstract class GameObject {
  Coord pos = Coord.get(0, 0);
  final protected String id = SquidID.randomUUID().toString();
  String name;
  String desc;
  char glyph;
  String color;

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getDesc() { return desc; }
  public void setDesc(String desc) { this.desc = desc; }

  public char getGlyph() { return glyph; }
  public void setGlyph(char glyph) { this.glyph = glyph; }

  public SColor getColor() { return (SColor)Colors.get(color); }
  public String getColorString() { return color; }
  public void setColor(String colorName) { color = colorName; }

  public Coord getPos() { return pos; }
  public void setPos(Coord c) { pos = c; }
}
