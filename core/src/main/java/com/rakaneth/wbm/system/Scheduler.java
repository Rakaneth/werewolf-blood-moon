package com.rakaneth.wbm.system;

import javafx.util.Pair;
import squidpony.squidmath.OrderedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Scheduler {
  private OrderedMap<Integer, List<Actor>> schedule = new OrderedMap<>();
  private int clock = 0;

  public void add(Actor actor) {
    int nextTurn = actor.nextTurn();
    if (schedule.get(nextTurn) == null) {
      List<Actor> newList = new ArrayList<Actor>();
      newList.add(actor);
      schedule.put(nextTurn, newList);
    } else {
      schedule.get(nextTurn).add(actor);
    }
  }

  public void remove(Actor actor) {
    int actorsTurn = actor.nextTurn();
    List<Actor> theList = schedule.get(actorsTurn);
    theList.remove(actor);
    if (theList.isEmpty()) schedule.remove(actorsTurn);
  }

  public Pair<Actor, Integer> getNext() {
    Map.Entry<Integer, List<Actor>> entry = schedule.entryAt(0);
    int ticksToProcess = entry.getKey() - clock;
    clock += ticksToProcess;
    Actor toAct = entry.getValue().remove(0);
    if (entry.getValue().isEmpty()) schedule.removeAt(0);
    return new Pair<>(toAct, ticksToProcess);
  }
}
