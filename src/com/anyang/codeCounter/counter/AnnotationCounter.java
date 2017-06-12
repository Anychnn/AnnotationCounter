package com.anyang.codeCounter.counter;

import com.anyang.codeCounter.lexer.CplusLexer;
import com.anyang.codeCounter.lexer.Lexer;
import com.anyang.codeCounter.lexer.LexerFactory;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/6/11.
 */
public class AnnotationCounter {

    private String path;
    private FileFilter fileFilter;
    private LexerFactory lexerFactory;
    private ExecutorService executorService;
    private AtomicInteger fileCounter=new AtomicInteger();
    private int threadPoolSize;

    public AnnotationCounter(String path) {
        this.path = path;
    }

    public void setFileFilter(FileFilter fileFilter) {
        this.fileFilter = fileFilter;
    }

    public void setLexerFactory(LexerFactory lexerFactory) {
        this.lexerFactory = lexerFactory;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public void count() throws Exception {
        this.executorService= Executors.newFixedThreadPool(threadPoolSize);
        count(new File(path));
        this.executorService.shutdown();
        while (!executorService.isTerminated()){}
        //等待所有任务执行完毕
        System.out.println("total file:"+fileCounter.get());
        this.executorService=null;

    }
    private void count(File file) throws Exception {
        if(fileFilter==null) throw new Exception("fileFilter cannot be null");
        if(lexerFactory==null) throw new Exception("lexerFactory cannot be null");
        if(file.isDirectory()){
            File[] files=file.listFiles();
            if(files!=null){
                for(File f:files){
                    count(f); //递归
                }
            }
        }else{
            countFile(file);
        }
    }

    private void countFile(File f){
        if(fileFilter.match(f)){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Lexer lexer=lexerFactory.newInstance();
                    lexer.loadFile(f);
                    try {
                        while (lexer.move()){}
                        resolveResult(lexer.getCountResult());
                    } catch (LexicalException e) {
                        System.out.println("文件语法异常:"+f.getAbsolutePath());
                        e.printStackTrace();
                    }
                    fileCounter.incrementAndGet();
                }
            });
        }
    }
    private void resolveResult(LineCounter result){
        System.out.println(result);
    }

}
