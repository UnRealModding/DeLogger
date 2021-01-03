package com.unrealdinnerbone.delogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.command.Commands;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTableManager;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.Util;
import net.minecraft.world.chunk.storage.ChunkSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.Logging;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.GameData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Mod(DeLogger.MOD_ID)
public class DeLogger
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "delogger";
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public DeLogger() {
        LOGGER.info("!!! LOGGERS BEING DISABLED - SOME IMPORTANT INFO MIGHT BE MISSING, REMOVED IF NEEDED IN BUG REPORTS !!!");
        Path configFile = FMLPaths.CONFIGDIR.get().resolve("delogger.json");
        if(!Files.exists(configFile)) {
            try {
                Files.createFile(configFile);
            } catch (IOException e) {
                LOGGER.error("Error creating delogger.json", e);
            }
        }
        try {
            DeLoggerConfig deLoggerConfig = GSON.fromJson(Files.newBufferedReader(configFile), DeLoggerConfig.class);
            if(deLoggerConfig == null) {
                deLoggerConfig = new DeLoggerConfig();
            }
            Files.write(configFile, GSON.toJson(deLoggerConfig).getBytes());
            if(deLoggerConfig.commands) {
                LoggerHacks.disableLogger(Commands.LOGGER);
            }
            if(deLoggerConfig.util) {
                LoggerHacks.disableLogger(Util.LOGGER);
            }
            if(deLoggerConfig.entityType) {
                LoggerHacks.disableLogger(EntityType.LOGGER);
            }
            if(deLoggerConfig.lootTableManger) {
                LoggerHacks.disableLogger(LootTableManager.LOGGER);
            }
            if(deLoggerConfig.simpleReloadableResourceManager) {
                LoggerHacks.disableLogger(SimpleReloadableResourceManager.LOGGER);
            }
            if(deLoggerConfig.chunkSerializer) {
                LoggerHacks.disableLogger(ChunkSerializer.LOGGER);
            }

            if(deLoggerConfig.gameData) {
                getLogger(GameData.class).ifPresent(LoggerHacks::disableLogger);
            }
            if(deLoggerConfig.versionChecker) {
                getLogger(VersionChecker.class).ifPresent(LoggerHacks::disableLogger);
            }
            if(deLoggerConfig.forgeConfigSpec) {
                getLogger(ForgeConfigSpec.class).ifPresent(LoggerHacks::disableLogger);
            }
            if(deLoggerConfig.yggdrasilAuthenticationService) {
                getLogger(YggdrasilAuthenticationService.class).ifPresent(LoggerHacks::disableLogger);
            }

            if(deLoggerConfig.forgeConfigSpec) {
                LoggerHacks.disableLogger(LogManager.getLogger(ForgeConfigSpec.class));
            }

            DeLoggerConfig finalDeLoggerConfig = deLoggerConfig;
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                if(finalDeLoggerConfig.clientRecipeBook) {
                    LoggerHacks.disableLogger(ClientRecipeBook.field_241555_k_);
                }
                if(finalDeLoggerConfig.modelBakery) {
                    LoggerHacks.disableLogger(ModelBakery.LOGGER);
                }
                if(finalDeLoggerConfig.atlasTexture) {
                    LoggerHacks.disableLogger(AtlasTexture.LOGGER);
                }
            });

        } catch (IOException e) {
            LOGGER.error("Error reading json from delogger.json", e);
        }


    }


    public static class DeLoggerConfig {
        private boolean commands = true;
        private boolean util = true;
        private boolean entityType = true;
        private boolean lootTableManger = true;
        private boolean simpleReloadableResourceManager = true;
        private boolean chunkSerializer = true;
        private boolean gameData = true;
        private boolean versionChecker = true;
        private boolean forgeConfigSpec = true;
        private boolean yggdrasilAuthenticationService = true;
        private boolean clientRecipeBook = true;
        private boolean modelBakery = true;
        private boolean atlasTexture = true;

    }



    public static Optional<Logger> getLogger(Class<?> clazz) {
        try {
            Field field = clazz.getDeclaredField("LOGGER");
            field.setAccessible(true);
            return Optional.of((Logger) field.get(clazz));
        } catch (NoSuchFieldException | ClassCastException | IllegalAccessException e) {
            return Optional.empty();
        }
    }
}
