import java.io.File;

/**
 * Created by Anyang on 2017/6/11.
 * 非线程安全类
 */
public class LineCounter {
    private File file;      //对应的文件
    public int total=0;      //总物理行数
    public int empty=0;      //空行行数
    public int effective=0;  //有效代码行数
    public int comment=0;    //注释行数
    public int currtLine=0;   //当前行数
    private Lexer lexer;

    public LineCounter(File file) {
        this.file = file;
    }


    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    public int getTotal() {
        return total;
    }

    public int getComment() {
        return comment;
    }


    @Override
    public String toString() {
        StringBuffer buffer=new StringBuffer();
        buffer.append(file.getAbsolutePath()+file.getName());
        buffer.append('\t');
        buffer.append("total:"+total);
        buffer.append('\t');
        buffer.append("empty:"+empty);
        buffer.append('\t');
        buffer.append("effective:"+effective);
        buffer.append('\t');
        buffer.append("comment:"+comment);
        return buffer.toString();
    }
}
