/**
 * Created by Anyang on 2017/6/11.
 */
public class Tag {
    public static final int
        CODE=1,                             //普通代码
        ANNOTATION=2,                       //注释主体部分
        String=3,                           //字符串部分
        Char=4,                             //字符 部分
        DOUBLE_QUOTATION=5,                 //双引号
        SINGLE_QUOTATION=6,                 //单引号
        LEFT_STAR_ANNOTATION=7,             // \*注释开始
        RIGHT_STAR_ANNOTATION=8,            //  */注释结束
        DOUBLE_ANNOTATION_SLASH=9,          //  //双斜杠注释\
        EOF=10;                              //文件结束符
}
