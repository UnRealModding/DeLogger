package com.unrealdinnerbone.delogger.mixin;

import com.unrealdinnerbone.delogger.LoggerHacks;
import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SharedConstants.class)
public class CrashReportMixin
{
    @Inject(method = "tryDetectVersion", at = @At("HEAD"))
    private static void iWantToLoadEarly(CallbackInfo ci)
    {
        LoggerHacks.init();
    }
}
