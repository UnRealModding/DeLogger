package com.unrealdinnerbone.delogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

@Mod(DeLogger.MOD_ID)
public class DeLogger
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "delogger";
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public DeLogger() {
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
        Path oldConfig = FMLPaths.CONFIGDIR.get().resolve("delogger.json");
        if(Files.exists(oldConfig)) {
            try {
                Files.delete(oldConfig);
            } catch (IOException e) {
                LOGGER.error("Error removing old json config", e);
            }
        }
    }
    
    public DeLoggerConfig createConfig(Path path) throws IOException {
        DeLoggerConfig deLoggerConfig = GSON.fromJson(Files.newBufferedReader(path), DeLoggerConfig.class);
        if (deLoggerConfig == null || deLoggerConfig.dontChangeMe == null) {
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


}
