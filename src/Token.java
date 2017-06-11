
/**
 * Created by Anyang on 2017/6/11.
 */
public class Token {
    public static final Token
            DOUBLE_QUOTATION_TOKEN=new Token("\""),                //双引号
            SINGLE_QUOTATION_TOKEN=new Token("\'"),                //单引号
            LEFT_STAR_ANNOTATION_TOKEN=new Token("/*"),        //左注释
            RIGHT_STAR_ANNOTATION_TOKEN=new Token("*/"),      //右注释
            DOUBLE_ANNOTATION_SLASH_TOKEN=new Token("//"),  //双斜杠注释
//            NEW_LINE_TOKEN=new Token(AnnotationFileUtil.NEW_LINE+"",Tag.NEW_LINE),      //换行符
            EOF_TOKEN=new Token("");                                            //文件读取结束
    enum Type{

    }
    public String value;
    public int tag;


    public Token(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
