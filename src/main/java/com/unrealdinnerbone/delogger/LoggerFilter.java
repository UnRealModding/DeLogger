package com.unrealdinnerbone.delogger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.core.impl.MutableLogEvent;

public class LoggerFilter extends AbstractFilter {

    private final DeLoggerConfig deLoggerConfig;

    public LoggerFilter(DeLoggerConfig deLoggerConfig) {
        this.deLoggerConfig = deLoggerConfig;
    }

    @Override
    public Result filter(LogEvent event) {
        if (event.getLevel() == Level.ERROR && event.getThrown() != null) {
            if (deLoggerConfig.ignoredExceptions.contains(event.getThrown().getClass().getCanonicalName())) {
                return Result.DENY;
            } else if (deLoggerConfig.loggers.contains(event.getLoggerName())) {
                return Result.DENY;
            }
        } else if (deLoggerConfig.loggers.contains(event.getLoggerName())) {
            return Result.DENY;
        } else if (event.getLoggerName().equals("STDOUT")) {
            String s = event.getMessage().getFormattedMessage();
            for (String logger : deLoggerConfig.loggers) {
                logger = "[" + logger;
                if(s.startsWith(logger)) {
                    return Result.DENY;
                }
            }
        }else {
            return super.filter(event);
        }
        return super.filter(event);
    }
}
