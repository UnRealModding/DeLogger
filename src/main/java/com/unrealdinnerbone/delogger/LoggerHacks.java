package com.unrealdinnerbone.delogger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilter;

public class LoggerHacks
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static boolean printDisableMessage = false;

    public static void disableLogger(Logger theLogger, boolean doDisable) {
        if(doDisable) {
            try {
                if(theLogger instanceof org.apache.logging.log4j.core.Logger) {
                    org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) theLogger;
                    logger.setLevel(Level.OFF);
                    logger.addFilter(new AbstractFilter() {
                        @Override
                        public Result filter(LogEvent event) {
                            return Result.DENY;
                        }
                    });
                }else {
                    throw new Exception();
                }
                if(printDisableMessage) {
                    LOGGER.info("Disabled Logger: {}", theLogger.getName());
                }
            }catch (Exception e) {
                LOGGER.error("Could not disable Logger: {}", theLogger.getName());
            }
        }
    }

    public static void setPrintDisableMessage(boolean printDisableMessage) {
        LoggerHacks.printDisableMessage = printDisableMessage;
    }
}
