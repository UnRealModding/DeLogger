package com.unrealdinnerbone.delogger.mixin;

import com.unrealdinnerbone.delogger.LoggerHacks;
import net.minecraft.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrashReport.class)
public class CrashReportMixin
{
    @Inject(method = "preload", at = @At("HEAD"))
    private static void iWantToLoadEarly(CallbackInfo ci) {
        LoggerHacks.init();
    }
}
