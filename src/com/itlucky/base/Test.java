package com.itlucky.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class Test {
    public static void main(String[] args)
        throws IOException {

        FileOutputStream fos = null;
        OutputStreamWriter osw = null;

        try {
            String path = verifyFilePath("E:\\test\\802_2022.TXT");
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String separator = System.getProperty("line.separator");
            fos = new FileOutputStream(file,true);
            osw = new OutputStreamWriter(fos, "GB18030");
            osw.append(String.format("%-8s", "OFDCFDAT")).append(separator).append(separator);
            osw.append(String.format("%-4s", "22")).append(separator).append(separator);
            osw.append(String.format("%-3s", "000")).append(separator);
            osw.append("OFDCFEND").append(separator);
            osw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != osw) {
                osw.close();
            }
            if (null != fos) {
                fos.close();
            }
        }
    }

    public static String verifyFilePath(String str) {
        char[] originalChars = str.toCharArray();
        char[] chars = new char[originalChars.length];
        for (int i = 0; i<originalChars.length; i++) {
            chars[i] = originalChars[i];
        }
        return new String(chars);
    }
}
