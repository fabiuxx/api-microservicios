/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encapsula funcionaidades de logging.
 *
 * @author Fabio A. González Sosa
 */
public class AppLogger {

    private final Logger wrapped;

    AppLogger(Class<?> klass) {
        this.wrapped = LoggerFactory.getLogger(klass);
    }

    public static AppLogger get(Class<?> klass) {
        return new AppLogger(klass);
    }

    public void trace(String fmt, Object... args) {
        trace(null, fmt, args);
    }

    public void trace(Throwable thr, String fmt, Object... args) {
        String msg = String.format(fmt, args);
        if (thr == null) {
            wrapped.trace(msg);
        } else {
            wrapped.trace(msg, thr);
        }
    }

    public void debug(String fmt, Object... args) {
        debug(null, fmt, args);
    }

    public void debug(Throwable thr, String fmt, Object... args) {
        String msg = String.format(fmt, args);
        if (thr == null) {
            wrapped.debug(msg);
        } else {
            wrapped.debug(msg, thr);
        }
    }

    public void warning(String fmt, Object... args) {
        debug(null, fmt, args);
    }

    public void warning(Throwable thr, String fmt, Object... args) {
        String msg = String.format(fmt, args);
        if (thr == null) {
            wrapped.warn(msg);
        } else {
            wrapped.warn(msg, thr);
        }
    }

    public void error(String fmt, Object... args) {
        error(null, fmt, args);
    }

    public void error(Throwable thr, String fmt, Object... args) {
        String msg = String.format(fmt, args);
        if (thr == null) {
            wrapped.error(msg);
        } else {
            wrapped.error(msg, thr);
        }
    }

    public void info(String fmt, Object... args) {
        info(null, fmt, args);
    }

    public void info(Throwable thr, String fmt, Object... args) {
        String msg = String.format(fmt, args);
        if (thr == null) {
            wrapped.info(msg);
        } else {
            wrapped.info(msg, thr);
        }
    }
}
