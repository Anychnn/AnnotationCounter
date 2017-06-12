package com.anyang.codeCounter.lexer;

import com.anyang.codeCounter.counter.LexicalException;

/**
 * Created by Anyang on 2017/6/12.
 * 这里简化处理 html只有一种注释 <!-- annotate --> 中间包含部分可以为多行
 */
public class HtmlLexer extends Lexer {
    public static final int Normal = 1;
    public static final int FirstAnnotate=2;    //  <
    public static final int SecondAnnotate=3;   //  <!
    public static final int ThirdAnnotate=4;    //  <!-
    public static final int Annotate=5;         //  <!--
    public static final int FitstEndAnnotate=6; //  <!--    -
    public static final int SecondEndAnnotate=7;//  <!--    --
    //                                              注释的最后状态 <!-- annotate  -->
    public static final int Eof=8;              //  状态机正常结束状态



    @Override
    public boolean move() throws LexicalException {
        if(state==Normal){
            if(peek=='<'){
                read();
                state=FirstAnnotate;
            }else if(peek==END_CHAR){
                state=Eof;
                return false;
            }else{
                checkEffectiveCode();
                read();
            }
        }else if(state==FirstAnnotate){
            expectOrBackToState('!',SecondAnnotate,Normal);
        }else if(state==SecondAnnotate){
            expectOrBackToState('-',ThirdAnnotate,Normal);
        }else if(state==ThirdAnnotate){
            expectOrBackToState('-',Annotate,Normal);
            if(state==Annotate){
                hasCommentInCurrentLine=true;
            }
        }else if(state==Annotate){
            checkComment();
            expectOrBackToState('-',FitstEndAnnotate,Annotate);
        }else if(state==FitstEndAnnotate){
            expectOrBackToState('-',SecondEndAnnotate,Annotate);
        }else if(state==SecondEndAnnotate){
            expectOrBackToState('>',Normal,Annotate);
        }
        return true;
    }

    //检测peek 并且核查注释状态   看是否是期望的字符 否则返回初始Normal状态
    private void expectOrBackToState(char expected, int nextState, int backState) throws LexicalException {
        if(this.peek==expected){
            read();
            state=nextState;
        }else{
            read();
            state=backState;
        }
    }
}
