package com.rakaneth.wbm.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rakaneth.wbm.WerewolfGame;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
    public static void main(String[] args) {
        createApplication();
    }

    private static LwjglApplication createApplication() {
        return new LwjglApplication(new WerewolfGame(), getDefaultConfiguration());
    }

    private static LwjglApplicationConfiguration getDefaultConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "werewolf-blood-moon";
        configuration.width = 80 * 10;
        configuration.height = 31 * 20;
        for (int size : new int[] { 128, 64, 32, 16 }) {
            configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
        }
        return configuration;
    }
}