package com.xkball.xkdeco.core;

import net.minecraft.launchwrapper.IClassTransformer;

import com.xkball.xkdeco.transformer.TransformerRegistry;

@SuppressWarnings("unused")
public class XKDecoCoreTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] byteCode) {
        if (XKDecoCorePlugin.INITIALIZED) {
            for (var transformer : TransformerRegistry
                .getTransformers(name.equals(transformedName) ? name : transformedName)) {
                byteCode = transformer.transform(byteCode);
            }
            return byteCode;
        }
        return byteCode;
    }
}
