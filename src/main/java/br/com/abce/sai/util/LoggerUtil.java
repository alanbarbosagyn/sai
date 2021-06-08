package br.com.abce.sai.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerUtil {

    private LoggerUtil(){
        super();
    }

    public static void info(final String mensagem){
        Logger.getLogger(Thread.currentThread().getStackTrace()[1].getClassName()).info(mensagem);
    }

    public static void error(final String mensagem) {
        Logger.getLogger(Thread.currentThread().getStackTrace()[1].getClassName()).log(Level.SEVERE, mensagem);
    }

    public static void error(final Throwable throwable) {
        Logger.getLogger(Thread.currentThread().getStackTrace()[1].getClassName()).log(Level.SEVERE, throwable.getMessage(), throwable);
    }

    public static void error(final String mensagem, final Throwable throwable) {
        Logger.getLogger(Thread.currentThread().getStackTrace()[1].getClassName()).log(Level.SEVERE, mensagem, throwable);
    }

    public static void debug(String mensagem) {
        Logger.getLogger(Thread.currentThread().getStackTrace()[1].getClassName()).log(Level.CONFIG, mensagem);
    }
}
