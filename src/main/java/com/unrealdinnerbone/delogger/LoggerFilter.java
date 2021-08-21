package com.unrealdinnerbone.delogger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilter;

public class LoggerFilter extends AbstractFilter {

    private final DeLoggerConfig deLoggerConfig;

    public LoggerFilter(DeLoggerConfig deLoggerConfig) {
        this.deLoggerConfig = deLoggerConfig;
    }

    @Override
    public Result filter(LogEvent event) {
        return (event.getLevel() == Level.ERROR && event.getThrown() != null && deLoggerConfig.ignoredExceptions.contains(event.getThrown().getClass().getCanonicalName())) || deLoggerConfig.loggers.contains(event.getLoggerName()) ? Result.DENY : super.filter(event);
    }
}
