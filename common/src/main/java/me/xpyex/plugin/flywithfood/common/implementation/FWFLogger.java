package me.xpyex.plugin.flywithfood.common.implementation;

import me.xpyex.plugin.flywithfood.common.config.FWFConfig;
import me.xpyex.plugin.flywithfood.common.types.LoggerInfoType;

public class FWFLogger {
    private final FWFSender consoleSender;
    private final String prefixInfo;
    private final String prefixWarning;
    private final String prefixError;
    private static final String PLUGIN_PREFIX = " &b[FlyWithFood] &f";

    public FWFLogger(FWFSender consoleSender) {
        this.consoleSender = consoleSender;
        prefixInfo = FWFConfig.CONFIG.isChinese ? "[信息]" : "[INFO]";
        prefixWarning = FWFConfig.CONFIG.isChinese ? "[警告]" : "[WARNING]";
        prefixError = FWFConfig.CONFIG.isChinese ? "[错误]" : "[ERROR]";
        //
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
