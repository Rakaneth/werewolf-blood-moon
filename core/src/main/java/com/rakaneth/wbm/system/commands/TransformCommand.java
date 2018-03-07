package com.rakaneth.wbm.system.commands;

import com.rakaneth.wbm.system.Actor;
import com.rakaneth.wbm.system.GameState;
import com.rakaneth.wbm.system.Werewolf;

public class TransformCommand implements Command {
  @Override
  public int execute(Actor actor, GameState state) {
    Werewolf ww = (Werewolf) actor;
    float bst = ww.getBeast();
    state.hudDirty = true;
    if (bst < 90) {
      ww.toggleTransform();
      state.addMessageF("You take on the [%s]%s[]-shape!", ww.getColorString(),
                        ww.isTransformed() ? "wolf" : "man");
      state.mapDirty = true;
      return 10;
    } else {
      if (!ww.isTransformed()) {
        ww.shiftUp();
        state.addMessageF("You take on the [%s]%s[]-shape!", ww.getColorString(), "wolf");
        state.mapDirty = true;
        return 10;
      } else {
        state.addMessage("The Wolf within has too strong of a hold; you cannot transform!");
        return 0;
      }
    }
  }

  @Override
  public String toString() {
    return "transforming";
  }
}
