package com.unrealdinnerbone.delogger;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilter;

public class LoggerFilter extends AbstractFilter {

    private final DeLoggerConfig deLoggerConfig;

    public LoggerFilter(DeLoggerConfig deLoggerConfig) {
        this.deLoggerConfig = deLoggerConfig;
    }

    @Override
    public Result filter(LogEvent event) {
        if (deLoggerConfig.loggers.contains(event.getLoggerName())) {
            return Result.DENY;
        }
        return super.filter(event);
    }
}
