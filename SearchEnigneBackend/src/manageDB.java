import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class manageDB{
    public static SQLiteJDBC DB;
    public static final long serialVersionuibUID = 1L;
    public manageDB() {
        DB = new SQLiteJDBC();
        try {
            DB.open();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //takes a document from the crawler then do the operation on each document then store them in the database.
    public static void docProcess(Crawler.OutputDoc crawlerOutput) {
        Page t = new Page(crawlerOutput.doc, crawlerOutput.getUrl().toString());

        Set<Crawler.image> rImages = crawlerOutput.referencedImages;
        Set<String> links = crawlerOutput.referencedLinks ;

        t.setImages(rImages);
        System.out.println("finish fucking 1");
        t.metaData();
        System.out.println("finish fucking 2");
        t.ExtractWords();
        System.out.println("finish fucking 3");
        t.setLinksSet(links);
        t.setPop(-111111111.0);
        System.out.println("finish fucking 4");
        DB.createPage(t);
        System.out.println("finish fucking 6");
    }

    //search for image then send them.
    public static void imageSearch (String query) throws IOException, SQLException {
        Hashtable<String, ArrayList<Crawler.image>> retrievedImages;
        retrievedImages = DB.retriveImages(query);

        List<String> images = new ArrayList<String>();
        Set<String> keys = retrievedImages.keySet();
        for (String key : keys) {
            for(int j=0;j<retrievedImages.get(key).size();j++){
                images.add(retrievedImages.get(key).get(j).imageSrc);
                images.add(retrievedImages.get(key).get(j).imageCaption);
                images.add(key);
            }
        }
        //Server will be started on 1700 port number
        ServerSocket server=new ServerSocket(1700);

        // Server listening for connection
        Socket s=server.accept();

        // Stream object for sending object
        ObjectOutputStream os=new ObjectOutputStream(s.getOutputStream());


        os.writeObject(images);
        System.out.println("hello it's me");
        os.flush();
        s.close();
        server.close();

    }

    //ranking retrieve all the pages that contains the query and rank them then send them.
    public static void rank (String query,boolean phrase) throws IOException, SQLException {
        List<Page> p = new ArrayList<Page>();
        List<String> pagess = new ArrayList<String>();
        if(phrase)
            p = DB.PhraseSearching(query);
        else
            p = DB.retrievePagesSetWords(query);
        if(p.isEmpty()) {
            pagess.add("");
        }
        else {
            int numDocs = p.size();
            String[] quer = query.split(" ");
            double[] TF_IDF = new double[numDocs];
            double tf = 0;
            double idf = 0;
            int count;
            for (int i = 0; i < p.size(); i++) {
                for (int j = 0; j < quer.length; j++) {
                    if (p.get(i).getwords().get(quer[j]) != null && p.get(i).getWordCount()!=0 ) {
                        tf = p.get(i).getwords().get(quer[j]) / p.get(i).getWordCount() + tf;
                    } else {
                        tf = 0;
                    }
                    count = DB.retrieveWordsCount(quer[j]);
                    if (count != 0) {
                        idf = Math.log(p.size() / count) + idf;
                    }
                }
                TF_IDF[i] = tf * idf;
                idf = 0;
                tf = 0;
            }
            double[] rank = new double[numDocs];
            for (int i = 0; i < numDocs; i++) {
                rank[i] = DB.getPopularity(p.get(i).getID()) + TF_IDF[i];
            }
            List<Page> ranked = new ArrayList<Page>();
            for (int j = 0; j < numDocs; j++) {
                int maxAt = 0;

                for (int i = 0; i < rank.length; i++) {
                    maxAt = rank[i] > rank[maxAt] ? i : maxAt;
                }
                ranked.add(p.get(maxAt));
                rank[maxAt] = -1;
            }

            for (int i = 0; i < ranked.size(); i++) {
                pagess.add(ranked.get(i).gettitle());
                pagess.add(ranked.get(i).getURL());
                pagess.add(ranked.get(i).getDescription());
            }
            System.out.println("end of create");
        }
        //Server will be started on 1700 port number
        ServerSocket server=new ServerSocket(1700);

        // Server listening for connection
        Socket s=server.accept();

        // Stream object for sending object
        ObjectOutputStream os=new ObjectOutputStream(s.getOutputStream());


        os.writeObject(pagess);
        System.out.println("hello it's me");
        os.flush();
        s.close();
        server.close();
    }

    //set the popularity for the pages stored in the database
    public static void returnPop(){
        System.out.println("pop entered!");    //all pages in database
        List<Page> allPage = DB.retrieveAllPages();
        //get all links in every page
        ArrayList<Integer>tmp = new ArrayList<>();
        ArrayList<ArrayList<Integer>> adj = new ArrayList<ArrayList<Integer>>();
        ArrayList<String> links = new ArrayList<>();
        int numDocs = allPage.size();
        for(int i=0;i<allPage.size();i++){
            links = new ArrayList<>();
            links=allPage.get(i).getLinks();
            tmp = new ArrayList<>();
            for(int j=0;j<links.size();j++) {
                if (DB.retrievePage(links.get(j)) != null) {

                    tmp.add(DB.retrievePage(links.get(j)).getID());
                }
            }
            adj.add(tmp);
        }

        Pagerank p = new Pagerank(adj);
        p.computRank();
        double[] popularity =new double[numDocs];
        popularity = p.getRank();
        for(int i=0;i<allPage.size();i++){
            DB.setPopularity(allPage.get(i).getID(),popularity[i]);
        }
    }
}