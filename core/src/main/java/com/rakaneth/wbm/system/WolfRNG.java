package com.rakaneth.wbm.system;

import squidpony.squidmath.StatefulRNG;

public final class WolfRNG {
  private        StatefulRNG rng;
  private static WolfRNG     instance;

  private WolfRNG(long seed) {
    rng = new StatefulRNG(seed);
  }

  private WolfRNG() {
    rng = new StatefulRNG();
  }

  public static StatefulRNG getRNG(long seed) {
    if (instance == null)
      instance = new WolfRNG(seed);

    return instance.rng;
  }

  public static StatefulRNG getRNG() {
    if (instance == null)
      instance = new WolfRNG();

    return instance.rng;
  }
}
