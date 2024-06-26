package com.xkball.xkdeco.client.model;

import net.minecraft.util.ResourceLocation;

public class TextureSymbol {

    public ResourceLocation location = null;
    public String symbol;

    public TextureSymbol(String symbol) {
        this.symbol = symbol;
    }

    public TextureSymbol(ResourceLocation location, String symbol) {
        this.location = location;
        this.symbol = symbol;
    }

    boolean validCheck() {
        return false;
    }
}
