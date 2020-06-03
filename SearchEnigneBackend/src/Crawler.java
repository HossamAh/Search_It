import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//Crawler name is Spider for testing the robots.txt file ,
public class Crawler {
    private   final int PAGES_LIIMIT = 100;
    private  HashSet<String> linksSet;
    public  AtomicInteger pagescount;
    public  ArrayList<Integer> LocksSet;
    private  Set<String> notAllowableURLs;
    private  ArrayList<Thread> ThreadList;
    private ArrayList<ExecutorService> EXList;
    private  int threadsNumber;
    private  boolean firstIterationCheck=false;
    public  HashSet<OutputDoc> crawlerOutput;
    public  class image
    {
        String imageSrc;
        String imageCaption;

        public image(String imageSrc, String imageCaption) {
            this.imageSrc = imageSrc;
            this.imageCaption = imageCaption;
        }
    }
    public static class OutputDoc
    {
        URL url;
        Set<String> referencedLinks;
        Set<image> referencedImages;
        Document doc;

        public OutputDoc(URL url, Set<String> referencedLinks, Set<image> referencedImages, Document doc) {
            this.url = url;
            this.referencedLinks = referencedLinks;
            this.referencedImages = referencedImages;
            this.doc = doc;
        }

        public Set<image> getReferencedImages() {
            return referencedImages;
        }

        public void setReferencedImages(Set<image> referencedImages) {
            this.referencedImages = referencedImages;
        }

        public URL getUrl() {
            return url;
        }

        public void setUrl(URL url) {
            this.url = url;
        }

        public Set<String> getReferencedLinks() {
            return referencedLinks;
        }

        public void setReferencedLinks(Set<String> referencedLinks) {
            this.referencedLinks = referencedLinks;
        }
    }
    //class that store link url string and the number of references in it ,
    // to determined whether to use it in the new iteration or not
    public  class Link implements Comparable<Link>
    {
        String linkUrl;
        int referencesNumber;

        public Link(String linkUrl, int referencesNumber) {
            this.linkUrl = linkUrl;
            this.referencesNumber = referencesNumber;
        }

        @Override
        public int compareTo(Link link) {
            if(this.referencesNumber>link.referencesNumber)
                return 1;
            else if(this.referencesNumber<link.referencesNumber)
                return -1;

            return 0;
        }
    }
    private  PriorityQueue<Link> newSeed;
    public  String Custom_Normalize(String Link) throws MalformedURLException {
        if(Link.endsWith("/"))
        {
            Link=Link.substring(0,Link.length()-1);
        }
        URL url = new URL(Link);
        if(url.getProtocol().equals("http"))
        {
            Link=Link.replace("http","https");
        }
        return Link;
    }

    //function to get host url to check the host robot.txt file
    public  URL normalizeURL(URL url) throws MalformedURLException {
        String link="";
        link+=url.getProtocol();
        link+="://";
        link+=url.getHost();
        return new URL(link);
    }
    //check link to crawl according to host robots.txt
    public  boolean checkLink(String Url)
    {
        synchronized (notAllowableURLs) {
            for (String link :notAllowableURLs)
            {
                if(!link.equals("")) {
                    if (Url.toString().contains(link))
                        return false;
                }
            }
        }
        return true;
    }
    //function to parse robots.txt file and add not allowed to not allowable urls
    // and return if the robots.txt file has constraints to my crawler or not.
    public  boolean parseRobotTxt(URL robotsTxtURL) throws IOException {
        notAllowableURLs.clear();
        URLConnection urlcon = robotsTxtURL.openConnection();
        InputStream stream = urlcon.getInputStream();
        Scanner scanner = new Scanner(stream);
        Set<String> URLS =new HashSet<>();
        String line;
        //to check if the coming lines is for my crawler or not.
        boolean myConstraintsCheck = false;
        while(scanner.hasNext())
        {
            line = scanner.nextLine();
            if(line.startsWith("#"))
            {
                continue;
            }
            else if(line.startsWith("User-agent:"))
            {
                if(myConstraintsCheck == false) {
                    //for all crawlers
                    if (line.contains("User-agent: *")) {
                        myConstraintsCheck = true;
                    }
                }
                else
                {
                    break;
                }
            }
            else if(line.startsWith("Disallow: ") && myConstraintsCheck)
            {
                URLS.add((line.split("Disallow: "))[1].replaceAll("/*",""));
            }
        }
        synchronized (notAllowableURLs)
        {
            notAllowableURLs.clear();
            if(myConstraintsCheck)
            {
                notAllowableURLs.addAll(URLS);
            }
        }
        return myConstraintsCheck;
    }

    public void Crawl(Set<String> links) throws MalformedURLException, InterruptedException {

        if (links.size() < 1)
            return;
        while (pagescount.intValue() < PAGES_LIIMIT) {
            while (!links.isEmpty()){
                Iterator<String> itr = links.iterator();
                if (itr.hasNext()) {
                    URL link = new URL(itr.next());

                    try {
                        if(pagescount.intValue()<PAGES_LIIMIT)
                            linksExtraction(link, links);
                        else return;

                    } catch (Exception e) {
                        links.remove(link.toString());
                        if(pagescount.intValue()<PAGES_LIIMIT) {
                            synchronized (linksSet) {
                                linksSet.remove(link.toString());
                                //pagescount.decrementAndGet();
                            }
                        }
                        else return;
                    }
                }
            }
            Crawl(links);

        }
        return;
    }

    public  void linksExtraction(URL link,Set<String>links) throws IOException, URISyntaxException {
        Document doc;
        try {
            doc = Jsoup.connect(link.toString()).get();
        } catch (Exception e) {
            linksSet.remove(link.toString());
            links.remove(link.toString());
            return;
        }
        Set<String> referencedLinks = new HashSet<>();
        Set<image> referencedImages = new HashSet<>();
        int referencesNumber = 0;
        Elements Links = doc.select("a[href]");
        Elements images = doc.select("img");
        for (Element image : images)
        {
            String caption = image.attr("alt");
            if(caption == "" ||caption == "..." )
            {
                caption = doc.title();}
            String absoluteUrl = image.absUrl("src");  //absolute URL on src
//                System.out.println("image SRC:"+absoluteUrl);
//                System.out.println("image Caption:"+caption);

            referencedImages.add(new image(absoluteUrl,caption));
        }

        for (Element newLink : Links) {
            if (pagescount.intValue() < PAGES_LIIMIT) {
                String Link = newLink.attr("abs:href");
                //Link= UrlCleaner.normalizeUrl(Link);
                Link = new URI(Link).normalize().toString();
                Link = Custom_Normalize(Link);
                URL URLLink = new URL(Link);
                if (URLLink != null) {
                    if (linksSet.contains(Link) == false && links.contains(Link) == false && referencedLinks.contains(Link) == false) {
                        String robotsTxtURL = normalizeURL(URLLink).toString() + "/robots.txt";
                        URL robotstxt = new URL(robotsTxtURL);
                        boolean ConstraintsCheck = false;
                        try {
                            ConstraintsCheck = parseRobotTxt(robotstxt);
                        } catch (IOException e) {
                            ConstraintsCheck = false;
                        }
                        if (checkLink(URLLink.toString()) == false && ConstraintsCheck) {
                            continue;
                        }
                        referencedLinks.add(Link);
                        if(pagescount.intValue()<PAGES_LIIMIT)
                        {
                            synchronized (linksSet) {
                                linksSet.add(Link);
                            }
                        }
                        else return;
                        links.add(Link);
                        //System.out.println(Link + "\n" + pagescount.intValue() + "#thread: " + Thread.currentThread().getName());
                        referencesNumber++;
                    }
                }
            }
            else
                return;
        }
        synchronized (newSeed) {
            newSeed.add(new Link(link.toString(), referencesNumber));
        }
        synchronized (crawlerOutput) {
            crawlerOutput.add(new OutputDoc(link, referencedLinks,referencedImages,doc));
        }
        links.remove(link.toString());
        if (firstIterationCheck == true) {
            linksSet.remove(link.toString());
        }
        pagescount.incrementAndGet();

    }
    public  void CrawlerProcess(int threads ) throws IOException, InterruptedException, URISyntaxException
    {
        pagescount  =new AtomicInteger(0);
        LocksSet = new ArrayList();
        linksSet = new HashSet<String>();
        notAllowableURLs = new HashSet<>();
//        Scanner scanner = new Scanner(System.in);
//        threadsNumber = scanner.nextInt();
        threadsNumber = threads;
        for(int i =0;i<threadsNumber;i++)
        {
            LocksSet.add(0);
        }
        linksSet.add(new URI("https://dmoz-odp.org").normalize().toString());
        newSeed = new PriorityQueue<>();
        crawlerOutput = new HashSet<>();
        crawlingBase(linksSet);
        //linksSet.add("http://gutenberg.org");
    }
//    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
//        pagescount  =new AtomicInteger(0);
//        linksSet = new HashSet<String>();
//        notAllowableURLs = new HashSet<>();
//        Scanner scanner = new Scanner(System.in);
//        threadsNumber = scanner.nextInt();
//        linksSet.add(new URI("https://dmoz-odp.org").normalize().toString());
//        newSeed = new PriorityQueue<>();
//        crawlerOutput = new HashSet<>();
//        crawlingBase(linksSet);
//        //linksSet.add("http://gutenberg.org");
//
//    }

    public  void crawlingBase(Set<String> seedSet) throws IOException, URISyntaxException, InterruptedException {
        Iterator<String> itr;
        if(firstIterationCheck ==false && seedSet.size()<threadsNumber) {
            while (seedSet.size() < threadsNumber) {
                itr = seedSet.iterator();
                if (itr.hasNext()) {
                    linksExtraction(new URL(itr.next()), seedSet);
                    pagescount.incrementAndGet();
                }
            }
        }
        ArrayList<Set<String>> linksSets = new ArrayList<>();
        itr=seedSet.iterator();
        for (int i=0;i<threadsNumber;i++) {
            Set<String>tempSet=new HashSet<>();
            for (int j = 0; j < seedSet.size() / threadsNumber; j++) {
                synchronized (seedSet) {
                    tempSet.add(itr.next());
                }
            }
            if(i==threadsNumber-1)
            {
                for(int k=0;k<seedSet.size()-threadsNumber*(seedSet.size() / threadsNumber);k++)
                    synchronized (seedSet) {
                        tempSet.add(itr.next());
                    }
            }
            linksSets.add(tempSet);
        }

        if (firstIterationCheck == false) {
            EXList = new ArrayList<>();
        }
        else
            EXList.clear();

        ExecutorService es=null;
        for (int i=0;i<threadsNumber-1;i++) {
            es = Executors.newCachedThreadPool();
            es.execute(new Extraction(linksSets.get(i)));
            EXList.add(es);
        }


        while(pagescount.intValue()<PAGES_LIIMIT);
        for (int i=0;i<threadsNumber-1;i++) {
            synchronized (linksSets) {
                linksSets.notifyAll();
            }
            while(!es.isShutdown())
                es.shutdownNow();
        }
        firstIterationCheck = true;
        linksSet.clear();
        seedSet.clear();
        synchronized (newSeed) {
            for (Link newlink : newSeed) {
                if (newlink.referencesNumber > 10) {
                    seedSet.add(newlink.linkUrl);
                }
            }
        }
        newSeed.clear();
        //clear crawler output is done by other process after processing on the link.
//        synchronized (crawlerOutput) {
//            crawlerOutput.clear();
//        }

        System.out.println("end of crawling iteration");
        if(seedSet.size()<1)
            seedSet.add((new URI("https://dmoz-odp.org").normalize().toString()));
        pagescount.set(0);

        crawlingBase(seedSet);
    }
    private  class Extraction implements Runnable {
        Set<String> links;

        public Extraction(Set<String> links) {
            this.links = links;
        }

        public void setLinks(Set<String> links) {
            this.links = links;
        }

        public void run()
        {

            try {
                Crawl(this.links);
            } catch (MalformedURLException e) {
                e.printStackTrace();

            }
            catch (InterruptedException I)
            {
                System.out.println("first cycle of Crawler is done, Thread "+Thread.currentThread().getName()+" is terminated");
                return;
            }


        }

    }



}