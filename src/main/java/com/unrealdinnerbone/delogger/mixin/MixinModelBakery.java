package com.unrealdinnerbone.delogger.mixin;

import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.SpriteMap;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.profiler.IProfiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelBakery.class)
public class MixinModelBakery
{
    @Inject(method = "uploadTextures(Lnet/minecraft/client/renderer/texture/TextureManager;Lnet/minecraft/profiler/IProfiler;)Lnet/minecraft/client/renderer/texture/SpriteMap;", at = @At(value = "INVOKE", target = "Ljava/lang/Exception;printStackTrace()V", ordinal = 0, shift = At.Shift.BEFORE), cancellable = true)
    public void onUpload(TextureManager resourceManagerIn, IProfiler profilerIn, CallbackInfoReturnable<SpriteMap> callbackInfo) {
        callbackInfo.cancel();
    }
}
