package com.unrealdinnerbone.delogger;

import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.command.Commands;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTableManager;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.Logging;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.GameData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Optional;

@Mod(DeLogger.MOD_ID)
public class DeLogger
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "delogger";

    public DeLogger() {
        LOGGER.info("!!! LOGGERS BEING DISABLED - SOME IMPORTANT INFO MIGHT BE MISSING, REMOVED IF NEEDED IN BUG REPORTS !!!");
        LoggerHacks.disableLogger(Commands.LOGGER);
        LoggerHacks.disableLogger(Util.LOGGER);
        getLogger(GameData.class).ifPresent(LoggerHacks::disableLogger);
        getLogger(VersionChecker.class).ifPresent(LoggerHacks::disableLogger);
        getLogger(ForgeConfigSpec.class).ifPresent(LoggerHacks::disableLogger);
        getLogger(LootTableManager.class).ifPresent(LoggerHacks::disableLogger);
        getLogger(EntityType.class).ifPresent(LoggerHacks::disableLogger);
        getLogger(SimpleReloadableResourceManager.class).ifPresent(LoggerHacks::disableLogger);
        getLogger(YggdrasilAuthenticationService.class).ifPresent(LoggerHacks::disableLogger);
        LoggerHacks.disableLogger(LogManager.getLogger(ForgeConfigSpec.class));
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
