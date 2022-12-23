package org.rizki.mufrizal.baseline.exception;

import lombok.extern.log4j.Log4j2;

import java.io.PrintWriter;
import java.io.StringWriter;

@Log4j2
public class ExceptionHelper {
    public static void logging(Exception ex) {
        StringWriter stringWriter = new StringWriter();
        log.error("Exception {} {}", ex.getClass().getName(), ex.getMessage());
        ex.printStackTrace(new PrintWriter(stringWriter));
        log.error("Exception {} {}", ex.getClass().getName(), stringWriter);
    }

    public static void logging(Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        log.error("Exception {} {}", ex.getClass().getName(), ex.getMessage());
        ex.printStackTrace(new PrintWriter(stringWriter));
        log.error("Exception {} {}", ex.getClass().getName(), stringWriter);
    }
}