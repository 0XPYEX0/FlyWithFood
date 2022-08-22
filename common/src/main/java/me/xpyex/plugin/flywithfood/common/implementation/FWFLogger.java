package me.xpyex.plugin.flywithfood.common.implementation;

import java.util.Locale;
import me.xpyex.plugin.flywithfood.common.types.LoggerInfoType;

public class FWFLogger {
    private final FWFSender consoleSender;
    private final String prefixInfo;
    private final String prefixWarning;
    private final String prefixError;
    private static final String PLUGIN_PREFIX = " &b[FlyWithFood] &f";

    public FWFLogger(FWFSender consoleSender) {
        this.consoleSender = consoleSender;
        Locale locale = Locale.getDefault();

        if (Locale.SIMPLIFIED_CHINESE.equals(locale)) {
            prefixInfo = "[信息]";
            prefixWarning = "[警告]";
            prefixError = "[错误]";
        } else if (Locale.TRADITIONAL_CHINESE.equals(locale)) {
            prefixInfo = "[信息]";
            prefixWarning = "[警告]";
            prefixError = "[錯誤]";
        } else {
            prefixInfo = "[INFO]";
            prefixWarning = "[WARNING]";
            prefixError = "[ERROR]";
        }
    }

    private void log(LoggerInfoType type, String... messages) {
        for (String s : messages) {
            String out;
            switch (type) {
                case INFO:
                    out = "&a" + prefixInfo + PLUGIN_PREFIX + s;
                    break;
                case WARNING:
                    out = "&6" + prefixWarning + PLUGIN_PREFIX + s;
                    break;
                case ERROR:
                    out = "&4" + prefixError + PLUGIN_PREFIX + s;
                    break;
                default:
                    out = "";
            }
            consoleSender.autoSendMsg(out);
        }
    }

    public void info(String... messages) {
        log(LoggerInfoType.INFO, messages);
        //
    }

    public void warning(String... messages) {
        log(LoggerInfoType.WARNING, messages);
        //
    }

    public void warn(String... messages) {
        log(LoggerInfoType.WARNING, messages);
        //
    }

    public void error(String... messages) {
        log(LoggerInfoType.ERROR, messages);
        //
    }

    public void severe(String... messages) {
        log(LoggerInfoType.ERROR, messages);
        //
    }
}
