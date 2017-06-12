import com.anyang.codeCounter.counter.AnnotationCounter;
import com.anyang.codeCounter.counter.FileFilter;
import com.anyang.codeCounter.lexer.CplusLexerFactory;
import com.anyang.codeCounter.lexer.HtmlLexerFactory;
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
        //设置线程池带线大小
        counter.setThreadPoolSize(20);
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
    public void test2() throws Exception {
        String htmlPath="E:\\web\\angular-strap-extension";
        AnnotationCounter counter=new AnnotationCounter(htmlPath);
        counter.setThreadPoolSize(20);
        FileFilter htmlFilter=new FileFilter() {
            @Override
            public boolean match(File file) {
                return file.getName().endsWith(".html");
            }
        };
        counter.setFileFilter(htmlFilter);
        counter.setLexerFactory(new HtmlLexerFactory());
        counter.count();
    }
}
