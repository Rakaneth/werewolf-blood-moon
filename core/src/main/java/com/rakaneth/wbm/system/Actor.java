package com.rakaneth.wbm.system;

import squidpony.squidgrid.Direction;
import squidpony.squidmath.Coord;

import java.util.HashSet;
import java.util.Set;

public interface Actor {
  int nextTurn();
  Coord getPos();
  void setPos(Coord c);
  default boolean tryMove(Direction d, char[][] map) {
    String walkables = "\".,:";
    Coord dest = getPos().translate(d);
    if (dest.x < 0 || dest.x >=  map.length || dest.y < 0 || dest.y >= map[0].length) {
      return false;
    } else if (!walkables.contains(String.valueOf(map[dest.x][dest.y]))) {
      return false;
    } else {
      setPos(dest);
      return true;
    }
  }
}
