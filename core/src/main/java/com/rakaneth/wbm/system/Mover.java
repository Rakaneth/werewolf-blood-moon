package com.rakaneth.wbm.system;

import squidpony.squidgrid.Direction;
import squidpony.squidmath.Coord;

public interface Mover {
  Coord getPos();
  void setPos(Coord c);
  default void moveTo(Coord c) {
    setPos(c);
  }
  default void moveBy(Direction d) {
    setPos(getPos().translate(d));
  }
}
