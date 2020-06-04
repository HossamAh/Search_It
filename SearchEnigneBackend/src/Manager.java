import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;

public class Manager {
    //public static Page.manageDB DB;
    private static HashSet<Crawler.OutputDoc> crawlerOutput;
    private static long processedCrawledPages;
    private static HashSet<Crawler.OutputDoc> outputSection;
    public static void main(String[] arg) throws IOException, URISyntaxException, InterruptedException {
        //crawlerOutput = new HashSet<>();
        //DB = new Page.manageDB();
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                processedCrawledPages=0;
                outputSection = new HashSet<>();
                while (true) {
                    crawlerOutput = crawler.crawlerOutput;
                    if (crawlerOutput != null)
                    {
                        if (crawlerOutput.size() > 0)
                        {
                            Iterator<Crawler.OutputDoc> itr = crawlerOutput.iterator();
                            if(itr.hasNext())
                            {
                                Crawler.OutputDoc output;
                                synchronized (crawlerOutput) {
                                    output = itr.next();
                                    outputSection.add(output);
                                }
                                if(processedCrawledPages%10==0)
                                {
                                    processedCrawledPages=0;
                                    try {
                                          Thread popThread=new Thread(new POPClass(outputSection));
                                          popThread.start();
                                          popThread.join();

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    outputSection.clear();
                                }

                                processedCrawledPages +=1;
                                //DB.docProcess(output);
                                synchronized (crawlerOutput) {
                                    System.out.println(output.url);
                                    crawlerOutput.remove(output);
                                }
                            }

                        }
                    }
                }
            }

        }).start();
    }
    public static class POPClass implements Runnable
    {
        private HashSet<Crawler.OutputDoc>PagesSet;

        public POPClass(HashSet<Crawler.OutputDoc> pagesSet) {
            PagesSet = pagesSet;
        }

        public HashSet<Crawler.OutputDoc> getPagesSet() {
            return PagesSet;
        }

        public void setPagesSet(HashSet<Crawler.OutputDoc> pagesSet) {
            PagesSet = pagesSet;
        }

        @Override
        public void run() {
            System.out.println("in side pop function");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}