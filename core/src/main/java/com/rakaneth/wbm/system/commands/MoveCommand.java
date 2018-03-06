package com.rakaneth.wbm.system.commands;

import com.rakaneth.wbm.system.Actor;
import com.rakaneth.wbm.system.GameState;
import com.rakaneth.wbm.system.Werewolf;
import squidpony.squidgrid.Direction;
import squidpony.squidmath.Coord;

public class MoveCommand implements Command {
  private Direction direction;

  public MoveCommand(Direction d) { this.direction = d; }

  @Override
  public int execute(Actor actor, GameState state) {
    if (actor instanceof Werewolf) {
      return actor.tryMove(direction, state.getMap()) ? 10 - ((Werewolf)actor).getStr() : 0;
    } else {
      return actor.tryMove(direction, state.getMap()) ? 5 : 0;
    }
  }
}
