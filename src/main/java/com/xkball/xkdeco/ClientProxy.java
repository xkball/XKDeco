package com.xkball.xkdeco;

import com.xkball.xkdeco.client.BlockModelModifiedManager;
import com.xkball.xkdeco.client.ItemModelModifiedManager;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

import com.xkball.xkdeco.client.ClientInit;
import com.xkball.xkdeco.client.event.GuiListener;
import com.xkball.xkdeco.client.render.ThirdPersonRender;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    // Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
    // Don't forget to call the super methods as well.

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        MinecraftForge.EVENT_BUS.register(new ThirdPersonRender());
        MinecraftForge.EVENT_BUS.register(new GuiListener());
        ClientInit.init();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        ItemModelModifiedManager.INSTANCE.reload();
        ItemModelModifiedManager.INSTANCE.regRenderToForge();
        BlockModelModifiedManager.INSTANCE.reload();
    }
}
