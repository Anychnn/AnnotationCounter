
/**
 * Created by Anyang on 2017/6/11.
 */
public class Token {
    public static final Token
            DOUBLE_QUOTATION_TOKEN=new Token("\"",Tag.DOUBLE_QUOTATION),                //双引号
            SINGLE_QUOTATION_TOKEN=new Token("\'",Tag.SINGLE_QUOTATION),                //单引号
            LEFT_STAR_ANNOTATION_TOKEN=new Token("/*",Tag.LEFT_STAR_ANNOTATION),        //左注释
            RIGHT_STAR_ANNOTATION_TOKEN=new Token("*/",Tag.RIGHT_STAR_ANNOTATION),      //右注释
            DOUBLE_ANNOTATION_SLASH_TOKEN=new Token("//",Tag.DOUBLE_ANNOTATION_SLASH),  //双斜杠注释
//            NEW_LINE_TOKEN=new Token(AnnotationFileUtil.NEW_LINE+"",Tag.NEW_LINE),      //换行符
            EOF_TOKEN=new Token("",Tag.EOF);                                            //文件读取结束
    enum Type{

    }
    public String value;
    public int tag;


    public Token(String value, int tag) {
        this.value = value;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return value;
    }
}
