package com.rakaneth.wbm.system;

import squidpony.squidgrid.Direction;
import squidpony.squidmath.Coord;

public interface Actor {
  Coord getPos();

  void setPos(Coord c);

  int getSpeed();

  int getEnergy();

  void setEnergy(int val);

  default boolean tryMove(Direction d, char[][] map) {
    String walkables = "\".,:";
    Coord dest = getPos().translate(d);
    if (dest.x < 0 || dest.x >= map.length || dest.y < 0 || dest.y >= map[0].length) {
      return false;
    } else if (!walkables.contains(String.valueOf(map[dest.x][dest.y]))) {
      return false;
    } else {
      setPos(dest);
      return true;
    }
  }
}
