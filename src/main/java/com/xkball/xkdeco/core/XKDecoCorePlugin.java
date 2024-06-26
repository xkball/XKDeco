package com.xkball.xkdeco.core;

import java.util.Map;

import com.xkball.xkdeco.transformer.TransformerRegistry;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class XKDecoCorePlugin implements IFMLLoadingPlugin, IFMLCallHook {

    public static boolean INITIALIZED = false;

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "com.xkball.xkdeco.core.XKDecoCoreTransformer" };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return "com.xkball.xkdeco.core.XKDecoCorePlugin";
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public Void call() throws Exception {
        TransformerRegistry.init();
        INITIALIZED = true;
        return null;
    }
}
