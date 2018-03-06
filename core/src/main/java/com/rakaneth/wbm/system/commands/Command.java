package com.rakaneth.wbm.system.commands;

import com.rakaneth.wbm.system.Actor;
import com.rakaneth.wbm.system.GameState;

public interface Command {
  int execute(Actor actor, GameState state);
}
