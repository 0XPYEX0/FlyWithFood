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

    /**
     * 向后台输出普通信息
     *
     * @param messages 文本信息
     */
    public void info(String... messages) {
        log(LoggerInfoType.INFO, messages);
        //
    }

    /**
     * 向后台输出警告信息
     *
     * @param messages 文本信息
     */
    public void warning(String... messages) {
        log(LoggerInfoType.WARNING, messages);
        //
    }

    /**
     * 向后台输出警告信息
     *
     * @param messages 文本信息
     */
    public void warn(String... messages) {
        log(LoggerInfoType.WARNING, messages);
        //
    }

    /**
     * 向后台输出错误信息
     *
     * @param messages 文本信息
     */
    public void error(String... messages) {
        log(LoggerInfoType.ERROR, messages);
        //
    }

    /**
     * 向后台输出错误信息
     *
     * @param messages 文本信息
     */
    public void severe(String... messages) {
        log(LoggerInfoType.ERROR, messages);
        //
    }
}
