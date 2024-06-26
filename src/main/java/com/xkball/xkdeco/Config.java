package com.xkball.xkdeco;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.Loader;

public class Config {

    public static boolean gtnhLoaded = false;
    public static String greeting = "Hello World";

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        greeting = configuration.getString("greeting", Configuration.CATEGORY_GENERAL, greeting, "How shall I greet?");

        if (configuration.hasChanged()) {
            configuration.save();
        }
        // noinspection SpellCheckingInspection
        if (Loader.isModLoaded("dreamcraft")) {
            gtnhLoaded = true;
        }
    }
}
