package com.anyang.codeCounter.lexer;

import com.anyang.codeCounter.counter.AnnotationFileUtil;
import com.anyang.codeCounter.counter.LexicalException;

import java.io.File;

/**
 * Created by Anyang on 2017/6/11.
 * c++ 自动状态机   状态转移图参考附录
 */
public class CplusLexer extends Lexer {

    //public static final int Normal = 1;   //初始状态为Normal 在父类构造方法中指定
    public static final int Star_Annotation = 2;            // 左注释/* 注释的开始  以 */结束
    public static final int Fin_Star_Annotation = 3;        // 结束带*状态需要两步  在STAR_ANNOTATION状态下  读到 * 进入该状态  如果下一个字符是 / 则结束 否则重新回到 Star_Annotation
    public static final int Single_Slash = 4;               // 一个/
    public static final int Double_Slash_Annotation = 5;    // 双斜杠注释 // 注释的开始  以换行符结束
    public static final int String = 6;                     // 左字符串  "  字符串的开始
    public static final int TransferredString = 7;          // 字符串转义开始 \
    public static final int Char = 8;                       // 字符     '  字符的开始
    public static final int TransferredChar = 9;            // 转义字符 \开始
    public static final int Eof = 10;                       // 读取结束

    public CplusLexer() {
        this.state = Normal;
    }


    /**
     * 本段逻辑需要参考 注释的状态转移图 ,利用自动状态机进行处理 起始状态:Normal 终结状态:Eof
     *
     * @return
     * @throws Exception
     */
    public boolean move() throws LexicalException {
        if (state == Normal) {
            if (peek == '/') {
                read();
                state = Single_Slash;
            } else if (peek == '"') {
                read();
                state = String;
            } else if (peek == '\'') {
                read();
                state = Char;
            } else if (peek == END_CHAR) {
                state = Eof;
                return false;
            } else {
                checkEffectiveCode();
                read();
            }
        } else if (state == Single_Slash) {
            if (peek == '*') {
                hasCommentInCurrentLine = true;
                read();
                state = Star_Annotation;
            } else if (peek == '/') {
                hasCommentInCurrentLine = true;
                read();
                state = Double_Slash_Annotation;
            } else {
                read();
                state= Normal;
//                throw new LexicalException("语法错误:/的后面必须为 /或者' :at line " + currentLine);
            }
        } else if (state == Star_Annotation) {
            checkComment();
            if (peek == '*') {
                read();
                state = Fin_Star_Annotation;
            } else {
                read();
            }
        } else if (state == Fin_Star_Annotation) {
            checkComment();
            if (peek == '*') {
                //状态不发生改变
                read();
            } else if (peek == '/') {
                read();
                state = Normal;
            } else {
                read();
                state = Star_Annotation;
            }
        } else if (state == Double_Slash_Annotation) {
            checkComment();
            if (peek == AnnotationFileUtil.NEW_LINE) {
                read();
                state = Normal;
            } else {
                //状态不发生改变
                read();
            }
        } else if (state == String) {
            if (peek == '"') {
                read();
                state = Normal;
            } else if (peek == '\\') {//处理转义字符
                state = TransferredString;
                read();
            } else {
                read();
            }
        } else if (state == TransferredString) {
            read();
            state = String;
        } else if (state == Char) {
            if (peek == '\'') {
                read();
                state = Normal;
            } else if (peek == '\\') {
                state = TransferredChar;
                read();
            } else {
                read();
            }
        } else if (state == TransferredChar) {
            read();
            state = Char;
        }
        return true;
    }

}
