import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2017/6/11.
 */
public class AnnotationCounter {

    public static final String C_PLUS_suffix=".cpp";

    String path;

    public AnnotationCounter(String path) {
        this.path = path;
    }

    public void count(){
        count(path);
    }
    private void count(String filePath){
        File file=new File(filePath);
        if(file.isDirectory()){
            File[] files=file.listFiles();
            if(files!=null){
                for(File f:files){
                    if(f.getName().endsWith(".cpp")){
                        Lexer lexer=new Lexer(f);
                        Token token=null;
                        StringBuffer buffer=new StringBuffer();
                        try {
                            while ((token=lexer.nextToken())!=Token.EOF_TOKEN){
//                                if(token.tag==Tag.ANNOTATION||token.tag==Tag.String||token.tag==Tag.Char){
//                                    System.err.print(token);
//                                    System.err.flush();
//                                }else{
                                    buffer.append(token);
//                                    System.out.flush();
//                                }
                            }
                            System.out.println(AnnotationFileUtil.getFileStr(f).equals(buffer.toString()));
                            System.out.println(lexer.getCountResult());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    count(f.getPath());
                }
            }
        }
    }

}
