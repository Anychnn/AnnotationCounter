package com.anyang.codeCounter.lexer;

/**
 * Created by Anyang on 2017/6/12.
 */
public class HtmlLexerFactory extends LexerFactory {
    @Override
    public Lexer newInstance() {
        return new HtmlLexer();
    }
}
