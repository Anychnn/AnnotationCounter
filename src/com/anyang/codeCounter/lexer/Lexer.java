package com.anyang.codeCounter.lexer;

import com.anyang.codeCounter.counter.AnnotationFileUtil;
import com.anyang.codeCounter.counter.LexicalException;
import com.anyang.codeCounter.counter.LineCounter;

import java.io.File;

/**
 * Created by Anyang on 2017/6/12.
 */
public abstract class Lexer {
    public static final char END_CHAR = '\u0000';
    public static final int Normal = 1;
    public String temp;
    char peek;
    int index = 0;
    int currentLine = 0;
    boolean hasCommentInCurrentLine = false;
    boolean hasCodeInCurrentLine = false;
    int state;

    LineCounter lineCounter = null;

    public Lexer() {
        this.state=Normal;
    }

    public void loadFile(File file){
        this.temp=AnnotationFileUtil.getFileStr(file);
        this.temp = AnnotationFileUtil.getFileStr(file);
        this.lineCounter = new LineCounter(file);
        try {
            read();
        } catch (LexicalException e) {
            e.printStackTrace();
        }
        this.lineCounter.setLexer(this);
    }

    public char read() throws LexicalException {
        if (index == temp.length() && index != 0 && peek == END_CHAR) {
            throw new LexicalException("文件已经读取到结尾,语法错误 at line " + currentLine + ",cuerrentState:" + state + " 未到达终止状态Eof");
        }
        if (temp == null || index == temp.length()) {
            peek = END_CHAR;
        } else {
            peek = temp.charAt(index);
            index++;
        }
        return peek;
    }

    //检查注释  两种注释写法 // 和 /* */
    public void checkComment() {
        if (peek == AnnotationFileUtil.NEW_LINE) {
            countCurrentByNewLine();
        } else if (peek != ' ' && peek != '\t') {
            hasCommentInCurrentLine = true;
        }
    }

    //检查有效代码
    public void checkEffectiveCode() {
        if (peek == AnnotationFileUtil.NEW_LINE) {
            countCurrentByNewLine();
        } else {
            if (peek != ' ' && peek != '\t') {
                hasCodeInCurrentLine = true;
            }
        }
    }

    //根据当前行的状态统计代码注释
    public void countCurrentByNewLine() {
        lineCounter.total++;
        currentLine++;
        if (hasCommentInCurrentLine) {
            lineCounter.comment++;
        }
        if (hasCodeInCurrentLine) {
            lineCounter.effective++;
        }
        if (!hasCommentInCurrentLine && !hasCodeInCurrentLine) {
            lineCounter.empty++;
        }
        resetLineRecord();
    }

    //重置当前行的状态
    public void resetLineRecord() {
        hasCodeInCurrentLine = false;
        hasCommentInCurrentLine = false;
    }

    public LineCounter getCountResult() {
        return lineCounter;
    }


    public abstract boolean move() throws LexicalException ;
}
