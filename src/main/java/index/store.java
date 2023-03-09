package index;

import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.Directory;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;

/* One Instance Storage Handler */
public class store {
    static String[] defaultDetectingPath = {
            "/Users/gaozhigang/Downloads/Index",
            "/home/zhiganggao/Index",
            "/opt/file_root/index_base/spdk_v17_10_1/"
    };

    static Path p;
    static Directory d;
    static IndexReader reader;
    static IndexSearcher searcher;

    public static IndexSearcher Open(String IndexPath) throws IOException {
        /* 理论上不应该用默认索引地址，而应该先检查参数传入的地址 */
        String[] loopParam;
        if (IndexPath != "") {
            loopParam = new String[] { IndexPath };
        } else {
            loopParam = defaultDetectingPath;
        }
        for (String file : loopParam) {
            try {
                p = Paths.get(file);
            } catch (Exception e) {
                System.out.println("[store.Open]Paths.get[" + file + "] Erroe:" +
                        e.getMessage());
                continue;
            }

            try {
                d = FSDirectory.open(p);
            } catch (Exception e) {
                System.out.println("[store.Open]FSDirectory.open[" + file + "] Erroe:" +
                        e.getMessage());
                continue;
            }

            try {
                reader = DirectoryReader.open(d);
            } catch (Exception e) {
                System.out.println("[DirectoryReader.open]DirectoryReader.open[" + file + "] Erroe:" +
                        e.getMessage());
                continue;
            } finally {
                System.out.println("DirectoryReader.open Finally");
            }

            try {
                searcher = new IndexSearcher(reader);
            } catch (Exception e) {
                System.out.println("[IndexSearcher]IndexSearcher[" + file + "] Erroe:" +
                        e.getMessage());
                continue;
            }

            System.out.println("Current file is " + file);
            return searcher;

        }
        /* 所有默认地址都无法索引到 */
        throw new IOException("无法打开索引目录");
    }

    public static void Close() throws IOException {
        d.close();
        reader.close();
    }

}
