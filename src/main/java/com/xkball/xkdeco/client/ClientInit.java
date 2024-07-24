package com.xkball.xkdeco.client;

import com.xkball.xkdeco.client.render.model.JsonModelBlockRender;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientInit {

    public static void init() {
        RenderingRegistry.registerBlockHandler(114514, new JsonModelBlockRender());
    }
}
