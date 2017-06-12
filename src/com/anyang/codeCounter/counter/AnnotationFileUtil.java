package com.anyang.codeCounter.counter;

import java.io.*;

/**
 * Created by Anyang on 2017/6/11.
 */
public class AnnotationFileUtil {
    public static final char NEW_LINE='\n';

    public static String getFileStr(File file){
        StringBuffer buffer=new StringBuffer();
        String line=null;
        try {
            BufferedReader reader=new BufferedReader(new FileReader(file));
            while ((line=reader.readLine())!=null){
                buffer.append(line+NEW_LINE);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
