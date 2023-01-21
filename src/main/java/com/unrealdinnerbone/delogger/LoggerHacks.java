package com.unrealdinnerbone.delogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.AbstractFilterable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LoggerHacks
{
    private static final org.slf4j.Logger LOGGER = LogUtils.getLogger();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void init() {
        Path configFile = FMLPaths.CONFIGDIR.get().resolve("delogger2.json");
        if(createFile(configFile)) {
            try {
                DeLoggerConfig config = createConfig(configFile);
                LoggerHacks.disableLogger(config);
                if (config.printWarningMessage) {
                    LOGGER.info("!!! SOME LOGGERS HAVE MOVED TO THE DEBUG FILE (Please Include them in your bug reports) !!!");
                }
            } catch (IOException e) {
                LOGGER.error("Error reading json from delogger.json", e);
            }
        }
    }

    public static DeLoggerConfig createConfig(Path path) throws IOException {
        DeLoggerConfig deLoggerConfig = GSON.fromJson(Files.newBufferedReader(path), DeLoggerConfig.class);
        if (deLoggerConfig == null || deLoggerConfig.dontChangeMe == null) {
            deLoggerConfig = new DeLoggerConfig();
        }
        Files.writeString(path, GSON.toJson(deLoggerConfig));
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


    public static void disableLogger(DeLoggerConfig deLoggerConfig) {
        final org.apache.logging.log4j.core.LoggerContext ctx = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        final LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        LoggerFilter filter = new LoggerFilter(deLoggerConfig);
        loggerConfig.getAppenders().values().stream()
                .peek(value -> { if(deLoggerConfig.printLoggerTypes) LOGGER.info("Logger Type: {}", value.getName());
                    })
                .filter(value -> !deLoggerConfig.whitelistLoggersTypes.contains(value.getName()) && value instanceof AbstractFilterable)
                .map(value -> (AbstractFilterable) value)
                .forEach(filterable -> filterable.addFilter(filter));

    }
}
