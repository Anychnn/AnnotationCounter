import com.anyang.codeCounter.counter.AnnotationCounter;
import com.anyang.codeCounter.counter.FileFilter;
import com.anyang.codeCounter.lexer.CplusLexerFactory;
import org.junit.Test;

import java.io.File;

/**
 * Created by Anyang on 2017/6/11.
 */
public class AnnotationCounterTest {

    @Test
    public void test() throws Exception {
        String path="E:\\c++project\\AnnotateTest\\AnnotateTest\\AnnotateTest";
        AnnotationCounter counter=new AnnotationCounter(path);
        FileFilter filter=new FileFilter() {
            @Override
            public boolean match(File file) {
                return file.getName().endsWith(".cpp");
            }
        };
        counter.setFileFilter(filter);
        counter.setLexerFactory(new CplusLexerFactory());
        counter.count();
    }

    @Test
    public void test2(){
        System.out.println("#include <stdio.h>\n" +
                "#include <cstdlib>\n");
    }
}
