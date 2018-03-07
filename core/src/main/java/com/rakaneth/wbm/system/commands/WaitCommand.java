package com.rakaneth.wbm.system.commands;

import com.rakaneth.wbm.system.Actor;
import com.rakaneth.wbm.system.GameState;

public class WaitCommand implements Command {

  @Override
  public int execute(Actor actor, GameState state) {
    if (actor == state.getPlayer()) {
      state.addMessage("You wait.");
      state.hudDirty = true;
    }
    return 10;
  }

  @Override
  public String toString() {
    return "waiting";
  }
}
