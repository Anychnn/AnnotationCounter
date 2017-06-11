import org.junit.Test;

/**
 * Created by Anyang on 2017/6/11.
 */
public class AnnotationCounterTest {

    @Test
    public void test(){
        String path="E:\\c++project\\AnnotateTest\\AnnotateTest\\AnnotateTest";
        AnnotationCounter counter=new AnnotationCounter(path);
        counter.count();
    }

    @Test
    public void test2(){
        System.out.println("#include <stdio.h>\n" +
                "#include <cstdlib>\n");
        System.out.println(Token.LEFT_STAR_ANNOTATION_TOKEN);
    }

    void parse(){
//        Lexer reader=new Lexer();
    }
}
