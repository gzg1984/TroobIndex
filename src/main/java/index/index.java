package index;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

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
//import org.apache.lucene.util.Version;


import java.io.IOException;

public class index {

    public static void main(String[] args) throws IOException {
       
        queryRAM(args);
        querySPDK(args);
        hilightSPDK(args);
    }

    private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));

        // use a string field for isbn because we don't want it tokenized
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        w.addDocument(doc);
    }
    private static void queryRAM(String[] args) throws IOException{
                // 0. Specify the analyzer for tokenizing text.
        //    The same analyzer should be used for indexing and searching
        StandardAnalyzer analyzer = new StandardAnalyzer();

        // 1. create the index
        Directory index = new RAMDirectory();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        IndexWriter w = new IndexWriter(index, config);
        addDoc(w, "Lucene in Action", "193398817");
        addDoc(w, "Lucene for Dummies", "55320055Z");
        addDoc(w, "Managing Gigabytes", "55063554A");
        addDoc(w, "The Art of Computer Science", "9900333X");
        w.close();

        // 2. query
        String queryString = args.length > 0 ? args[0] : "lucene";

        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        Query query = null;
        try {
            String[] fields = {"title"};
            query = new MultiFieldQueryParser(fields, analyzer).parse(queryString);
        } catch (org.apache.lucene.queryparser.classic.ParseException e) {
            e.printStackTrace();
        }

        // 3. search

        int hitsPerPage = 10;
        //IndexReader reader = DirectoryReader.open(index);
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        //searcher.search(query, collector);
        searcher.search(query, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". Title " + d.get("title") + "\t" + d.get("isbn") ) ;
        }

        // reader can only be closed when there
        // is no need to access the documents any more.
        reader.close();
    }

    private static void querySPDK(String[] args) throws IOException{
        Analyzer lxranalyzer = new SourceFileAnalyzer();
        Query lxrquery = null;
        String lxrqueryString = args.length > 0 ? args[0] : "LICENSE";

        try {
            String[] fields = {"content"};
            lxrquery = new MultiFieldQueryParser(fields, lxranalyzer).parse(lxrqueryString);
        } catch (org.apache.lucene.queryparser.classic.ParseException e) {
            e.printStackTrace();
        }
        String path = "/opt/file_root/index_base/spdk_v17_10_1/";
        Directory diskIndex = FSDirectory.open(Paths.get(path));




        int hitsPerPage = 10;
        //IndexReader reader = DirectoryReader.open(index);
        IndexReader reader = DirectoryReader.open(diskIndex);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        //searcher.search(query, collector);
        searcher.search(lxrquery, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". File " + d.get("filePath") + "\t" + d.get("fileName")  + "\t" + d.get("projectId")) ;
        }

        // reader can only be closed when there
        // is no need to access the documents any more.
        reader.close();
    }

    private static void hilightSPDK(String[] args) throws IOException{
        Analyzer lxranalyzer = new SourceFileAnalyzer();
        Query lxrquery = null;
        String lxrqueryString = args.length > 0 ? args[0] : "LICENSE";

        try {
            String[] fields = {"content"};
            lxrquery = new MultiFieldQueryParser(fields, lxranalyzer).parse(lxrqueryString);
        } catch (org.apache.lucene.queryparser.classic.ParseException e) {
            e.printStackTrace();
        }
        String path = "/opt/file_root/index_base/spdk_v17_10_1/";
        Directory diskIndex = FSDirectory.open(Paths.get(path));




        int hitsPerPage = 10;
        //IndexReader reader = DirectoryReader.open(index);
        IndexReader reader = DirectoryReader.open(diskIndex);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        //searcher.search(query, collector);
        /*
        searcher.search(lxrquery, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". File " + d.get("filePath") + "\t" + d.get("fileName")  + "\t" + d.get("projectId")) ;
        }

        // reader can only be closed when there
        // is no need to access the documents any more.
        reader.close();
        */
        //String[] fields = {"content"};
        //Query query = parser.parse("阿法狗");

        String field="content";
        //QueryParser parser = new QueryParser(Version.LATEST, field, lxranalyzer);
        QueryParser parser = new QueryParser(field, lxranalyzer);
        // 查询字符串
        Query query=null;
        try{
            query = parser.parse("LICENSE");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (query == null){
            System.out.println("查询为空");
            return ;
        }

        QueryScorer scorer=new QueryScorer(lxrquery,field);
        SimpleHTMLFormatter fors=new SimpleHTMLFormatter("<span style=\"color:red;\">", "</span>");
        Highlighter highlighter=new Highlighter(fors, scorer);

        System.out.println("query:" + query.toString());
        // 返回前10条
        TopDocs topDocs = searcher.search(query, 10);
        if (topDocs != null) {
            System.out.println("符合条件第文档总数：" + topDocs.totalHits);

            for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                Document doc = searcher.doc(topDocs.scoreDocs[i].doc);
                System.out.println((i + 1) + " .  "+ doc.get("filePath") + " 当前文件名字" );
                //public static TokenStream getTokenStream(String field, Fields tvFields, String text, Analyzer analyzer,
                //int maxStartOffset)
                try {
                    String contents = doc.get("content");

                    TokenStream tokenStream=TokenSources.getTokenStream( field, null, contents,lxranalyzer,-1);
                    Fragmenter  fragment=new SimpleSpanFragmenter(scorer);
                    highlighter.setTextFragmenter(fragment); 
                    //高亮news_title域
                    String str=highlighter.getBestFragment(tokenStream, contents);//获取高亮的片段，可以对其数量进行限制  
                    if (str != null) {
                        //System.out.println((i + 1) + " .  "+ doc.get("filePath") +"/"+ doc.get("fileName")+ " 高亮 内容 ： "+str);
                        System.out.println((i + 1) + " .  "+ doc.get("filePath") + " 高亮 内容 ： "+str);
                       // tokenStream=TokenSources.getTokenStream( field, null, contents,lxranalyzer,-1);
                        //str=highlighter.getBestFragment(tokenStream, doc.get("content"));//获取高亮的片段，可以对其数量进行限制  
                        //System.out.println("高亮 内容++："+str);
                    }else{
                        System.out.println((i + 1) + " . 结果为空");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        diskIndex.close();
        reader.close();

    }
}
