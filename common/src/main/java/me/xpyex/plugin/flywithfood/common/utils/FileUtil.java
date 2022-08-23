package me.xpyex.plugin.flywithfood.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileUtil {
    /**
     * 读取目标文本文件
     *
     * @param target 目标文本文件
     * @return 目标文件的文本
     * @throws Exception 文件异常
     */
    public static String readFile(File target) throws Exception {
        StringBuilder text = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                Files.newInputStream(target.toPath()), StandardCharsets.UTF_8));
        String line;
        while ((line = in.readLine()) != null) {
            if (line.contains("//")) {
                line = line.split("//")[0];  //不读取注释
            }
            text.append(line);
        }
        in.close();
        return text.toString();
    }

    /**
     * 向目标文件写出文本
     *
     * @param target 目标文本
     * @param content 要写出的内容
     * @param attend 是否在原文本的内容基础续写新文本，否则覆写整个文件
     * @throws Exception 文件异常
     */
    public static void writeFile(File target, String content, boolean attend) throws Exception {
        PrintWriter out = new PrintWriter(target, "UTF-8");
        if (attend) {
            out.println(content);
        } else {
            out.write(content);
        }
        out.flush();
        out.close();
    }

    /**
     * 覆写目标文件
     *
     * @param target 目标文件
     * @param content 覆写的内容
     * @throws Exception 文件异常
     */
    public static void writeFile(File target, String content) throws Exception {
        writeFile(target, content, false);
    }
}