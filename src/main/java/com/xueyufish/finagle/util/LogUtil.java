package com.xueyufish.finagle.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {

    private final Logger logger;

    private LogUtil(Class<?> clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    public static LogUtil getLogger(final Class<?> clazz) {
        return new LogUtil(clazz);
    }

    /**
     * Log a message at the TRACE level.
     *
     * @param message
     */
    public void logTrace(final String message) {
        if (logger.isTraceEnabled()) {
            logger.trace(message);
        }
    }

    /**
     * Log a message at the TRACE level according to the specified format and
     * arguments.
     *
     * @param format
     * @param args
     */
    public void logTrace(final String format,
                         final Object... args) {
        if (logger.isTraceEnabled()) {
            logger.trace(format, args);
        }
    }

    /**
     * Log a message at the TRACE level according to the specified format and
     * argument.
     *
     * @param format
     * @param arg
     */
    public void logTrace(final String format,
                         final Object arg) {
        if (logger.isTraceEnabled()) {
            logger.trace(format, arg);
        }
    }

    /**
     * Log a message at the TRACE level according to the specified format and
     * arguments.
     *
     * @param format
     * @param arg1
     */
    public void logTrace(final String format,
                         final Object arg1, final Object arg2) {
        if (logger.isTraceEnabled()) {
            logger.trace(format, arg1, arg2);
        }
    }

    /**
     * Log an exception (throwable) at the TRACE level with an accompanying
     * message.
     *
     * @param message
     * @param t
     */
    public void logTrace(final String message,
                         Throwable t) {
        if (logger.isTraceEnabled()) {
            logger.trace(message, t);
        }
    }

    /**
     * Log a message at the DEBUG level.
     *
     * @param message
     */
    public void logDebug(final String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    /**
     * Log a message at the DEBUG level according to the specified format and
     * arguments.
     *
     * @param format
     * @param args
     */
    public void logDebug(final String format,
                         Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(format, args);
        }
    }

    /**
     * Log a message at the DEBUG level according to the specified format and
     * argument.
     *
     * @param format
     * @param arg
     */
    public void logDebug(final String format,
                         Object arg) {
        if (logger.isDebugEnabled()) {
            logger.debug(format, arg);
        }
    }

    /**
     * Log a message at the DEBUG level according to the specified format and
     * arguments.
     *
     * @param format
     * @param arg1
     * @param arg2
     */
    public void logDebug(final String format,
                         Object arg1, Object arg2) {
        if (logger.isDebugEnabled()) {
            logger.debug(format, arg1, arg2);
        }
    }

    /**
     * Log an exception (throwable) at the DEBUG level with an accompanying
     * message.
     *
     * @param message
     * @param t
     */
    public void logDebug(final String message,
                         Throwable t) {
        if (logger.isDebugEnabled()) {
            logger.debug(message, t);
        }
    }

    /**
     * Log a message at the INFO level.
     *
     * @param message
     */
    public void logInfo(final String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    /**
     * Log a message at the INFO level according to the specified format and
     * arguments.
     *
     * @param format
     * @param args
     */
    public void logInfo(final String format,
                        final Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(format, args);
        }
    }

    /**
     * Log a message at the INFO level according to the specified format and
     * argument.
     *
     * @param clazz
     * @param format
     * @param arg
     */
    public static void logInfo(final Class<?> clazz, final String format,
                               final Object arg) {
        Logger log = LoggerFactory.getLogger(clazz);
        if (log.isInfoEnabled()) {
            log.info(format, arg);
        }
    }

    /**
     * Log a message at the INFO level according to the specified format and
     * arguments.
     *
     * @param format
     * @param arg1
     * @param arg2
     */
    public void logInfo(final String format,
                        final Object arg1, final Object arg2) {
        if (logger.isInfoEnabled()) {
            logger.info(format, arg1, arg2);
        }
    }

    /**
     * Log an exception (throwable) at the INFO level with an accompanying
     * message.
     *
     * @param message
     * @param t
     */
    public void logInfo(final String message,
                        Throwable t) {
        if (logger.isInfoEnabled()) {
            logger.info(message, t);
        }
    }

    /**
     * Log a message at the WARN level.
     *
     * @param message
     */
    public void logWarn(final String message) {
        if (logger.isWarnEnabled()) {
            logger.warn(message);
        }
    }

    /**
     * Log a message at the WARN level according to the specified format and
     * arguments.
     *
     * @param format
     * @param args
     */
    public void logWarn(final String format,
                        Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(format, args);
        }
    }

    /**
     * Log a message at the WARN level according to the specified format and
     * argument.
     *
     * @param format
     * @param arg
     */
    public void logWarn(final String format,
                        Object arg) {
        if (logger.isWarnEnabled()) {
            logger.warn(format, arg);
        }
    }

    /**
     * Log a message at the WARN level according to the specified format and
     * arguments.
     *
     * @param format
     * @param arg1
     * @param arg2
     */
    public void logWarn(final String format,
                        Object arg1, Object arg2) {
        if (logger.isWarnEnabled()) {
            logger.warn(format, arg1, arg2);
        }
    }

    /**
     * Log an exception (throwable) at the WARN level with an accompanying
     * message.
     *
     * @param message
     * @param t
     */
    public void logWarn(final String message,
                        Throwable t) {
        if (logger.isWarnEnabled()) {
            logger.warn(message, t);
        }
    }

    /**
     * Log a message at the ERROR level.
     *
     * @param message
     */
    public void logError(final String message) {
        if (logger.isErrorEnabled()) {
            logger.error(message);
        }
    }

    /**
     * Log a message at the ERROR level according to the specified format and
     * arguments.
     *
     * @param format
     * @param args
     */
    public void logError(final String format,
                         Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(format, args);
        }
    }

    /**
     * Log a message at the ERROR level according to the specified format and
     * argument.
     *
     * @param format
     * @param arg
     */
    public void logError(final String format,
                         Object arg) {
        if (logger.isErrorEnabled()) {
            logger.error(format, arg);
        }
    }

    /**
     * Log a message at the ERROR level according to the specified format and
     * arguments.
     *
     * @param format
     * @param arg1
     * @param arg2
     */
    public void logError(final String format,
                         Object arg1, Object arg2) {
        if (logger.isErrorEnabled()) {
            logger.error(format, arg1, arg2);
        }
    }

    /**
     * Log an exception (throwable) at the ERROR level with an accompanying
     * message.
     *
     * @param message
     * @param t
     */
    public void logError(final String message,
                         final Throwable t) {
        if (logger.isErrorEnabled()) {
            logger.error(message, t);
        }
    }

}
