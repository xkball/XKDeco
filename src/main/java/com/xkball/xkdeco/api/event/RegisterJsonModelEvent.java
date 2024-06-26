package com.xkball.xkdeco.api.event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class RegisterJsonModelEvent extends Event {

    public final List<ResourceLocation> append;

    public RegisterJsonModelEvent(List<ResourceLocation> append) {
        this.append = append;
    }
}
