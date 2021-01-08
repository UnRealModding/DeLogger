package com.unrealdinnerbone.delogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.client.renderer.model.BlockModel;
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
        Path configFile = FMLPaths.CONFIGDIR.get().resolve("delogger.json");
        if(createFile(configFile)) {
            try {
                DeLoggerConfig config = createConfig(configFile);
                if (config.printWarningMessage) {
                    LOGGER.info("!!! LOGGERS BEING DISABLED - SOME IMPORTANT INFO MIGHT BE MISSING, REMOVED IF NEEDED IN BUG REPORTS !!!");
                }
                LoggerHacks.setPrintDisableMessage(config.printLoggersDisabled);

                LoggerHacks.disableLogger(Commands.LOGGER, config.commands);
                LoggerHacks.disableLogger(Util.LOGGER, config.util);
                LoggerHacks.disableLogger(EntityType.LOGGER, config.entityType);
                LoggerHacks.disableLogger(LootTableManager.LOGGER, config.lootTableManger);
                LoggerHacks.disableLogger(SimpleReloadableResourceManager.LOGGER, config.simpleReloadableResourceManager);
                LoggerHacks.disableLogger(ChunkSerializer.LOGGER, config.chunkSerializer);

                getLogger(GameData.class).ifPresent(logger -> LoggerHacks.disableLogger(logger, config.gameData));
                getLogger(VersionChecker.class).ifPresent(logger -> LoggerHacks.disableLogger(logger, config.versionChecker));
                getLogger(ForgeConfigSpec.class).ifPresent(logger -> LoggerHacks.disableLogger(logger, config.forgeConfigSpec));
                getLogger(YggdrasilAuthenticationService.class).ifPresent(logger -> LoggerHacks.disableLogger(logger, config.yggdrasilAuthenticationService));

                LoggerHacks.disableLogger(LogManager.getLogger(ForgeConfigSpec.class), config.forgeConfigSpec);

                DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                    LoggerHacks.disableLogger(ClientRecipeBook.field_241555_k_, config.clientRecipeBook);
                    LoggerHacks.disableLogger(ModelBakery.LOGGER, config.modelBakery);
                    LoggerHacks.disableLogger(AtlasTexture.LOGGER, config.atlasTexture);
                    LoggerHacks.disableLogger(BlockModel.LOGGER, config.blockModel);

                });

            } catch (IOException e) {
                LOGGER.error("Error reading json from delogger.json", e);
            }
        }
    }
    
    public DeLoggerConfig createConfig(Path path) throws IOException {
        DeLoggerConfig deLoggerConfig = GSON.fromJson(Files.newBufferedReader(path), DeLoggerConfig.class);
        if (deLoggerConfig == null) {
            deLoggerConfig = new DeLoggerConfig();
        }
        Files.write(path, GSON.toJson(deLoggerConfig).getBytes());
        return deLoggerConfig;
    }
    
    private static boolean createFile(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
                return true;
            } catch (IOException e) {
                LOGGER.error("Error creating delogger.json", e);
                return false;
            }
        }
        return true;
    }


    public static class DeLoggerConfig {

        private boolean printWarningMessage = true;
        private boolean printLoggersDisabled = false;
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
        private boolean blockModel = true;

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
