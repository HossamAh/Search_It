import java.lang.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class Manager {
  //  public static Page.manageDB DB;
    private static HashSet<Crawler.OutputDoc> crawlerOutput;

    public static void main(String[] arg) throws IOException, URISyntaxException, InterruptedException {
        //crawlerOutput = new HashSet<>();
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
                while (true) {
                    crawlerOutput = crawler.crawlerOutput;
                    if (crawlerOutput != null) {
                        if (crawlerOutput.size() > 0) {
                            synchronized (crawler.crawlerOutput) {
                                for (Crawler.OutputDoc output : crawlerOutput) {
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
}
