import java.lang.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class Manager {
    private static HashSet<Crawler.OutputDoc> crawlerOutput;
    public static void main(String[] arg) throws IOException, URISyntaxException, InterruptedException {
        crawlerOutput = new HashSet<>();
        Crawler.CrawlerProcess(crawlerOutput);

    }
}
