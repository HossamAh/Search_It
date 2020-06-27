import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

public class Manager {
    //public static manageDB DB;
    private static HashSet<Crawler.OutputDoc> crawlerOutput;
    private static long processedCrawledPages;
    private static HashSet<Crawler.OutputDoc> outputSection;

    public static void main(String[] arg) throws IOException, URISyntaxException, InterruptedException {
        //crawlerOutput = new HashSet<>();
        //DB = new manageDB();
        Crawler crawler = new Crawler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    crawler.CrawlerProcess(5);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }).start();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                processedCrawledPages=0;
//                outputSection = new HashSet<>();
//                while (true) {
//                    crawlerOutput = crawler.crawlerOutput;
//                    if (crawlerOutput != null)
//                    {
//                        if (crawlerOutput.size() > 0)
//                        {
//                            Iterator<Crawler.OutputDoc> itr = crawlerOutput.iterator();
//                            if(itr.hasNext())
//                            {
//                                Crawler.OutputDoc output;
//                                synchronized (crawlerOutput) {
//                                    output = itr.next();
//                                    outputSection.add(output);
//                                }
//                                if(processedCrawledPages%10==0)
//                                {
//                                    processedCrawledPages=0;
//                                    try {
//                                        Thread popThread=new Thread(new POPClass(outputSection));
//                                        popThread.start();
//                                        popThread.join();
//
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                    outputSection.clear();
//                                }
//
//                                processedCrawledPages +=1;
//                                DB.docProcess(output);
//                                synchronized (crawlerOutput) {
//                                    crawlerOutput.remove(output);
//                                }
//                            }
//
//                        }
//                    }
//                }
//            }
//
//        }).start();
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    ServerSocket serverSocket = new ServerSocket(7800);
//                    System.out.println("server is up and running...");
//                    while (true) {
//                        Socket s = serverSocket.accept();
//                        System.out.println("server accepted the query...");
//                        DataInputStream DIS = new DataInputStream(s.getInputStream());
//                        String query_and_flags = DIS.readUTF();
//                        Boolean imageSearch = false;
//                        Boolean phraseSearch = false;
//                        System.out.println("query_and_flags=" + query_and_flags);
//                        String[] list_query_and_flags = query_and_flags.split("@");
//                        String query = list_query_and_flags[0];
//                        if (list_query_and_flags[1].equals("phrase"))
//                            phraseSearch = true;
//                        if (list_query_and_flags[2].equals("yes"))
//                            imageSearch = true;
//
//                        if (imageSearch)
//                            manageDB.imageSearch(query);
//                        else manageDB.rank(query,phraseSearch);
//                        if (phraseSearch)
//                            System.out.println("it's phrase search");
//                        else System.out.println("no phrase search");
//                        DIS.close();
//                        s.close();
//
//                    }
//                } catch (IOException | SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//
//    }
//    public static class POPClass implements Runnable
//    {
//        private HashSet<Crawler.OutputDoc>PagesSet;
//
//        public POPClass(HashSet<Crawler.OutputDoc> pagesSet) {
//            PagesSet = pagesSet;
//        }
//
//        public HashSet<Crawler.OutputDoc> getPagesSet() {
//            return PagesSet;
//        }
//
//        public void setPagesSet(HashSet<Crawler.OutputDoc> pagesSet) {
//            PagesSet = pagesSet;
//        }
//
//        @Override
//        public void run() {
//            manageDB.returnPop();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    }
}