package com.unrealdinnerbone.delogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.OnStartupTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LoggerHacks
{
    private static final Logger LOGGER = LogManager.getLogger();
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
        Path oldConfig = FMLPaths.CONFIGDIR.get().resolve("delogger.json");
        if(Files.exists(oldConfig)) {
            try {
                Files.delete(oldConfig);
            } catch (IOException e) {
                LOGGER.error("Error removing old json config", e);
            }
        }
    }

    public static  DeLoggerConfig createConfig(Path path) throws IOException {
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


    public static void disableLogger(DeLoggerConfig deLoggerConfig) {
        if (LOGGER instanceof org.apache.logging.log4j.core.Logger) {
            org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) LOGGER;
            if(deLoggerConfig.enableDebugLog) {
                getRoot(logger).addAppender(createAppender());
            }
            LoggerFilter filter = new LoggerFilter(deLoggerConfig);
            logger.getAppenders().values().stream()
                    .filter(value -> !deLoggerConfig.whitelistLoggersTypes.contains(value.getName()))
                    .filter(value -> value instanceof AbstractFilterable)
                    .forEach(value -> ((AbstractFilterable) value).addFilter(filter));
        } else {
            LOGGER.error("Unsupported Logger used cant disable them!");
        }
    }

    public static org.apache.logging.log4j.core.Logger getRoot(org.apache.logging.log4j.core.Logger logger) {
        if(logger.getParent() != null) {
            return getRoot(logger.getParent());
        }else {
            return logger;
        }
    }


    public static RollingRandomAccessFileAppender createAppender() {
        RollingRandomAccessFileAppender rollingRandomAccessFileAppender = RollingRandomAccessFileAppender.newBuilder()
                .setName("DeLoggerDebug")
                .withFileName("delogger/debug.log")
                .withFilePattern("delogger/%d{yyyy-MM-dd}-%i.log.gz")
                .setLayout(PatternLayout.newBuilder()
                        .withPattern("[%d{ddMMMyyyy HH:mm:ss.SSS}] [%t/%level] [%logger{36}/%markerSimpleName]: %minecraftFormatting{%msg}{strip}%n%xEx")
                        .build())
                .withPolicy(CompositeTriggeringPolicy.createPolicy(OnStartupTriggeringPolicy.createPolicy(1),
                        TimeBasedTriggeringPolicy.newBuilder().build()
                ))
                .withStrategy(DefaultRolloverStrategy.newBuilder().withMax("99").withFileIndex("min").build())
                .build();
        rollingRandomAccessFileAppender.addFilter(new AbstractFilter() {
            @Override
            public Result filter(LogEvent event) {
                return event.getLevel() == Level.TRACE || event.getLevel() == Level.DEBUG ? Result.DENY : Result.NEUTRAL;
            }
        });
        rollingRandomAccessFileAppender.start();
        return rollingRandomAccessFileAppender;
    }
}
