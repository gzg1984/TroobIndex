package index;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.Directory;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;

public class store {
    static String[] defaultDetectingPath = {
        "/Users/gaozhigang/Downloads/Index",
        "/home/zhiganggao/Index"
    };

    public static Directory Open() throws IOException {
        Directory d;
        /*理论上不应该用默认索引地址，而应该先检查参数传入的地址 */
        for (String file:defaultDetectingPath){
            Path p;
            try {
                 p = Paths.get(file);
            } catch (Exception e) {
                 System.out.println("[store.Open]Paths.get["+file+"] Erroe:"+
                    e.getMessage());
                continue;
            }

            try {
                d = FSDirectory.open(p);
            }catch (Exception e) {
                System.out.println("[store.Open]FSDirectory.open["+file+"] Erroe:"+
                   e.getMessage());
               continue;
           }
           System.out.println("Current file is "+ file);
           return d;

        }
        /* 所有默认地址都无法索引到  */
        return FSDirectory.open(Paths.get("/"));
    }


}
