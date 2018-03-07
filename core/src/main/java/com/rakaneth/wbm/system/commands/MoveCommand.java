package com.rakaneth.wbm.system.commands;

import com.rakaneth.wbm.system.Actor;
import com.rakaneth.wbm.system.GameState;
import squidpony.squidgrid.Direction;

public class MoveCommand implements Command {
  private Direction direction;

  public MoveCommand(Direction d) {
    this.direction = d;
  }

  @Override
  public int execute(Actor actor, GameState state) {
    boolean result = actor.tryMove(direction, state.getMap());
    if (result) state.mapDirty = true;
    if (actor == state.getPlayer()) state.hudDirty = true;
    return result ? 10 : 0;
  }

  public String toString() {
    return "moving";
  }
}
