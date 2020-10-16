package com.unrealdinnerbone.delogger;

import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.command.Commands;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.GameData;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Optional;

@Mod(DeLogger.MOD_ID)
public class DeLogger
{
    public static final String MOD_ID = "delogger";

    public DeLogger() {
        LoggerHacks.disableLogger(Commands.LOGGER);
        LoggerHacks.disableLogger(Util.LOGGER);
        getLogger(GameData.class).ifPresent(LoggerHacks::disableLogger);
        getLogger(VersionChecker.class).ifPresent(LoggerHacks::disableLogger);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            LoggerHacks.disableLogger(ClientRecipeBook.field_241555_k_);
            LoggerHacks.disableLogger(ModelBakery.LOGGER);
            LoggerHacks.disableLogger(AtlasTexture.LOGGER);
        });
    }


    public static Optional<Logger> getLogger(Class<?> clazz) {
        try {
            Field field = clazz.getDeclaredField("LOGGER");
            field.setAccessible(true);
            return Optional.of((Logger) field.get(clazz));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return Optional.empty();
        }
    }
}
