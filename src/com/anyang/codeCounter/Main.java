package com.anyang.codeCounter;

import com.anyang.codeCounter.counter.AnnotationCounter;
import com.anyang.codeCounter.counter.FileFilter;
import com.anyang.codeCounter.lexer.CplusLexerFactory;
import com.anyang.codeCounter.lexer.HtmlLexerFactory;

import java.io.File;

/**
 * Created by Anyang on 2017/6/12.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        if(args==null||args.length==0){
            System.out.println("参数不能为空,程序退出.");
            return;
        }
        if(args[0]==null){
            System.out.println("路径不能为空,程序退出.");
            return;
        }
        //设置文件路径
        String path=args[0];
        AnnotationCounter counter=new AnnotationCounter(path);
        //设置线程池带线大小
        counter.setThreadPoolSize(20);

        //设置文件过滤器 为c++文件
        FileFilter filter=new FileFilter() {
            @Override
            public boolean match(File file) {
                return file.getName().endsWith(".cpp");
            }
        };
        counter.setFileFilter(filter);
        //设置解析工程  c++解析器
        counter.setLexerFactory(new CplusLexerFactory());

//        String htmlPath="E:\\web\\angular-strap-extension";
//        AnnotationCounter counter=new AnnotationCounter(htmlPath);
//        FileFilter htmlFilter=new FileFilter() {
//            @Override
//            public boolean match(File file) {
//                return file.getName().endsWith(".html");
//            }
//        };
//        counter.setFileFilter(htmlFilter);
//        counter.setLexerFactory(new HtmlLexerFactory());

        counter.count();
    }
}
