package com.mosseygremlin.utiliwheel.mixin;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(KeyboardHandler.class)
public interface KeyboardHandlerInvoker {

    @Invoker("handleDebugKeys")
    boolean utiliwheel$handleDebugKeys(KeyEvent event);
}