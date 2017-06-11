import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Anyang on 2017/6/11.
 * Lexer自动状态机 produce:Token
 */
public class Lexer {
    public static final char END_CHAR = '\u0000';

    enum State {
        Normal,
        Star_Annotation,            // 左注释/* 注释的开始  以 */结束
        Fin_Star_Annotation,        // 结束带*状态需要两步  在STAR_ANNOTATION状态下  读到 * 进入该状态  如果下一个字符是 / 则结束 否则重新回到 Star_Annotation
        Double_Slash_Annotation,    // 双斜杠注释 // 注释的开始  以换行符结束
        String,                     // 左字符串  "  字符串的开始
        TransferredString,          // 字符串转义开始 \
        Char,                       // 字符     '  字符的开始
        TransferredChar,            // 转义字符 \开始
        Eof                         // 读取结束
    }

    public String temp;
    private char peek;
    private int index = 0;
    private State state = State.Normal;
    private Queue<Token> tokenCache = new LinkedList<>();
    private LineCounter lineCounter = null;
    public int currentLine = 0;

    boolean hasCommentInCurrentLine = false;
    boolean hasCodeInCurrentLine = false;


    public Lexer(File file) {
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

    /**
     * 本段逻辑需要参考 注释的状态转移图 ,利用自动状态机进行处理 起始状态:Normal 终结状态:Eof
     *
     * @return
     * @throws Exception
     */
    Token nextToken() throws Exception {
        //首先检查缓存队列
        if (!tokenCache.isEmpty()) {
            return tokenCache.poll();
        }
        if (state == State.Normal) {
            StringBuffer buffer = new StringBuffer();
            while (true) {
                if (peek == '/') {
                    read();
                    if (peek == '/') {
                        hasCommentInCurrentLine = true;
                        read();
                        state = State.Double_Slash_Annotation;
                        tokenCache.offer(Token.DOUBLE_ANNOTATION_SLASH_TOKEN);
                        return new Token(buffer.toString(), Tag.CODE);
                    } else if (peek == '*') {
                        hasCommentInCurrentLine = true;
                        read();
                        state = State.Star_Annotation;
                        tokenCache.offer(Token.LEFT_STAR_ANNOTATION_TOKEN);
                        return new Token(buffer.toString(), Tag.CODE);
                    }
                    throw new LexicalException("语法错误:/的后面必须为 /或者' :at line " + currentLine);
                } else if (peek == '\"') {
                    state = State.String;
                    read();
                    tokenCache.offer(Token.DOUBLE_QUOTATION_TOKEN);
                    return new Token(buffer.toString(), Tag.CODE);
                } else if (peek == '\'') {
                    state = State.Char;
                    read();
                    tokenCache.offer(Token.SINGLE_QUOTATION_TOKEN);
                    return new Token(buffer.toString(), Tag.CODE);
                } else if (peek == END_CHAR) {
                    state = State.Eof;
                    tokenCache.offer(Token.EOF_TOKEN);
                    return new Token(buffer.toString(), Tag.CODE);
                } else {
                    if (peek == AnnotationFileUtil.NEW_LINE) {
                        countCurrentByNewLine();
                    } else {
                        if (peek != ' ' && peek != '\t') {
                            hasCodeInCurrentLine = true;
                        }
                    }
                    buffer.append(peek);
                    read();
                }
            }
        } else if (state == State.Star_Annotation) {
            // /*注释以 */结束
            StringBuffer buffer = new StringBuffer();
            while (true) {
                if (state == State.Star_Annotation) {
                    if (peek == '*') {
                        state = State.Fin_Star_Annotation;
                        hasCommentInCurrentLine = true;
                        read();
                    } else {
                        if (peek == AnnotationFileUtil.NEW_LINE) {
                            countCurrentByNewLine();
                        } else {
                            if (peek != ' ' && peek != '\t') {
                                hasCommentInCurrentLine = true;
                            }
                        }
                        //状态不改变
                        buffer.append(peek);
                        read();
                    }
                } else if (state == State.Fin_Star_Annotation) {
                    //进入该状态表示已经读了一个* 但是还未存入buffer
                    if (peek == '*') {
                        //状态不改变 仍然是Fin_Star_Annotation
                        buffer.append("*");
                        read();
                    } else if (peek == '/') {
                        read();
                        state = State.Normal;
                        tokenCache.offer(Token.RIGHT_STAR_ANNOTATION_TOKEN);
                        return new Token(buffer.toString(), Tag.ANNOTATION);
                    } else {
                        if (peek == AnnotationFileUtil.NEW_LINE) {
                            countCurrentByNewLine();
                        } else {
                            if (peek != ' ' && peek != '\t') {
                                hasCodeInCurrentLine = true;
                            }
                        }
                        buffer.append("*" + peek);
                        state = State.Star_Annotation;
                        read();
                    }
                }
            }
        } else if (state == State.Double_Slash_Annotation) {
            //双斜杠注释以换行结束
            StringBuffer buffer = new StringBuffer();
            while (true) {
                if (peek == AnnotationFileUtil.NEW_LINE) {
                    state = State.Normal;
                    return new Token(buffer.toString(), Tag.ANNOTATION);
                } else {
                    buffer.append(peek);
                    read();
                }
            }
        } else if (state == State.String) {
            //还需要考虑转义字符
            StringBuffer buffer = new StringBuffer();
            while (true) {
                if (state == State.String) {
                    if (peek == '\"') {
                        state = State.Normal;
                        read();
                        tokenCache.offer(Token.DOUBLE_QUOTATION_TOKEN);
                        return new Token(buffer.toString(), Tag.String);
                    } else if (peek == '\\') {
                        state = State.TransferredString;
                        read();
                        buffer.append("\\");
                    } else {
                        buffer.append(peek);
                        read();
                    }
                } else if (state == State.TransferredString) {
                    state = State.String;
                    buffer.append(peek);
                    read();
                }
            }
        } else if (state == State.Char) {
            StringBuffer buffer = new StringBuffer();
            while (true) {
                if (state == State.Char) {
                    if (peek == '\'') {
                        state = State.Normal;
                        read();
                        tokenCache.offer(Token.SINGLE_QUOTATION_TOKEN);
                        return new Token(buffer.toString(), Tag.Char);
                    } else if (peek == '\\') {
                        state = State.TransferredChar;
                        buffer.append("\\");
                        read();
                    } else {
                        buffer.append(peek);
                        read();
                    }
                } else if (state == State.TransferredChar) {
                    state = State.Char;
                    buffer.append(peek);
                    read();
                }

            }
        } else if (state == State.Eof) {
            throw new Exception("Token状态机已经结束,无法继续读取 at line:" + currentLine);
        }
        throw new Exception("未匹配任何状态:" + state + " at line:" + currentLine);
    }

    public LineCounter getCountResult() {
        return lineCounter;
    }

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

    public void resetLineRecord() {
        hasCodeInCurrentLine = false;
        hasCommentInCurrentLine = false;
    }
}
