package index;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;

import java.nio.file.Paths;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.Fragmenter;

import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.queryparser.classic.QueryParser;

import java.io.IOException;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;

public class index {
    static boolean verbose = false;
    //static String file = "/opt/file_root/index_base/spdk_v17_10_1/";

    //static String queryString = "LICENSE";

    static String file = "/Users/gaozhigang/Downloads/Index";

    static String queryString = "mysqlshCount";


    static String field = "content";

    private static void handleOpthins(String[] args) throws IOException {
        CommandLineParser parser = new BasicParser();
        Options options = new Options();
        options.addOption("h", "help", false, "Print this usage information");
        options.addOption("v", "verbose", false, "Print out VERBOSE information");
        options.addOption("f", "file", true, "Index Folder");
        options.addOption("q", "query", true, "Key word to query");
        // Parse the program arguments
        try {
            CommandLine commandLine = parser.parse(options, args);
            // Set the appropriate variables based on supplied options

            if (commandLine.hasOption('h')) {
                System.out.println("Help Message");
                System.exit(0);
            }
            if (commandLine.hasOption('v')) {
                verbose = true;
            }
            if (commandLine.hasOption('f')) {
                file = commandLine.getOptionValue('f');
            }

            if (commandLine.hasOption('q')) {
                queryString = commandLine.getOptionValue('q');
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        handleOpthins(args);

        hilightQuery();
    }

    private static void hilightShowDoc(Document doc, Analyzer analyzer, Highlighter highlighter, QueryScorer scorer)
            throws IOException {
        String contents = doc.get("content");

        TokenStream tokenStream = TokenSources.getTokenStream(field, null, contents, analyzer, -1);
        Fragmenter fragment = new SimpleSpanFragmenter(scorer);
        highlighter.setTextFragmenter(fragment);
        // 高亮news_title域
        try {
            String str = highlighter.getBestFragment(tokenStream, contents);// 获取高亮的片段，可以对其数量进行限制
            if (str != null) {
                // System.out.println((i + 1) + " . "+ doc.get("filePath") +"/"+
                // doc.get("fileName")+ " 高亮 内容 ： "+str);
                //System.out.println("# "+doc.get("filePath"));
                System.out.println(str);
                // tokenStream=TokenSources.getTokenStream( field, null,
                // contents,lxranalyzer,-1);
                // str=highlighter.getBestFragment(tokenStream,
                // doc.get("content"));//获取高亮的片段，可以对其数量进行限制
                // System.out.println("高亮 内容++："+str);
            } else {
                System.out.println("# 结果为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void hilightShowDocs(IndexSearcher searcher, Query query, Analyzer analyzer) throws IOException {
        Query lxrquery = null;

        try {
            String[] fields = { "content" };
            lxrquery = new MultiFieldQueryParser(fields, analyzer).parse(queryString);
        } catch (org.apache.lucene.queryparser.classic.ParseException e) {
            e.printStackTrace();
        }
        QueryScorer scorer = new QueryScorer(lxrquery, field);
        SimpleHTMLFormatter fors = new SimpleHTMLFormatter("\033[36m", "\033[0m ");

        Highlighter highlighter = new Highlighter(fors, scorer);

        TopDocs topDocs = searcher.search(query, 10);

        if (topDocs == null) {
            return;
        }

        System.out.println("= Hit Count：" + topDocs.totalHits);

        for (int i = 0; i < topDocs.scoreDocs.length; i++) {
            Document doc = searcher.doc(topDocs.scoreDocs[i].doc);
            System.out.println((i + 1) + " . " + doc.get("filePath"));
            //System.out.println((i + 1) + " . ");
            // public static TokenStream getTokenStream(String field, Fields tvFields,
            // String text, Analyzer analyzer,
            // int maxStartOffset)
            try {
                hilightShowDoc(doc, analyzer, highlighter, scorer);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void hilightQuery() throws IOException {
        Analyzer troobAnalyzer = new SourceFileAnalyzer();
        Directory diskIndex = FSDirectory.open(Paths.get(file));
        IndexReader reader = DirectoryReader.open(diskIndex);
        IndexSearcher searcher = new IndexSearcher(reader);

        String field = "content";
        QueryParser parser = new QueryParser(field, troobAnalyzer);
        // 查询字符串
        Query query = null;
        try {
            query = parser.parse(queryString);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (query == null) {
            System.out.println("查询为空");
            return;
        }

        System.out.println("= Query Field And Key Word[" + query.toString()+"]");
        // 返回前10条
        hilightShowDocs(searcher, query, troobAnalyzer);

        diskIndex.close();
        reader.close();

    }
}
