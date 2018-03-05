package com.rakaneth.wbm.ui.screens;

import java.util.HashMap;
import java.util.Map;

public final class ScreenManager {
  private static Map<String, WolfScreen> screens = new HashMap<>();
  private static WolfScreen curScreen;

  public static void register(WolfScreen... screensToAdd) {
    for (WolfScreen screen : screensToAdd) {
      screens.put(screen.getName(), screen);
    }
  }

  public static void setScreen(String screenName) {
    if (curScreen != null) curScreen.exit();
    curScreen = screens.get(screenName);
    curScreen.enter();
  }

  public static WolfScreen getCurScreen() {
    return curScreen;
  }
}
