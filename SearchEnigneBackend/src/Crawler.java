import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//Crawler name is Spider for testing the robots.txt file ,
public class Crawler {
    private   final int PAGES_LIMIT = 5000;
    private  HashSet<String> linksSet;
    public  AtomicInteger pagesCount;
    public  ArrayList<Integer> LocksSet;
    private ArrayList<ExecutorService> EXList;
    private HashSet<String>alreadyCrawled;
    private HashSet<String>imagesLinks;
    private  int threadsNumber;
    private  boolean firstIterationCheck=false;
    public  HashSet<OutputDoc> crawlerOutput;
    private List<String>languageCodes;
    public static class image
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
    // function to terminate the threads pool to recreate another pool
    public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
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
    public  Boolean Custom_Normalize(String Link) throws MalformedURLException {

        if(Link.endsWith("/"))
        {
            Link=Link.substring(0,Link.length()-1);
        }
        if(Link.endsWith("/#"))
        {
            Link=Link.substring(0,Link.length()-2);
        }
        URL url = new URL(Link);
        if(url.getProtocol().equals("http"))
        {
            Link=Link.replace("http","https");
        }
        if(Link.indexOf(".")<16)
        {
            if(!(Link.substring(0,16).contains("//m.")||Link.substring(0,16).contains("//en.")||Link.substring(0,16).contains("//ar.")))
            {
                String langSection=Link.substring(8,Link.indexOf(".")+1);
                Link=Link.replace(langSection,"");
                return false;
            }
        }
        return true;
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
    public  boolean checkLink(String Url,Set<String>NotAllowableURLs)
    {
        for (String link :NotAllowableURLs)
        {
            if(!link.equals("")) {
                if (Url.toLowerCase().contains(link.toLowerCase()))
                    return false;
            }
        }
        return true;
    }
    //function to parse robots.txt file and add not allowed to not allowable urls
    // and return if the robots.txt file has constraints to my crawler or not.
    public  boolean parseRobotTxt(URL robotsTxtURL,Set<String>notAllowable) throws IOException {
        URLConnection urlcon = robotsTxtURL.openConnection();
        InputStream stream = urlcon.getInputStream();
        Scanner scanner = new Scanner(stream);
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
                String Disallowable =(line.split("Disallow: "))[1].replaceAll("/*","");
                if(Disallowable.length()>2)
                    notAllowable.add(Disallowable);
            }
        }
        return myConstraintsCheck;
    }

    public void Crawl(Set<String> links) throws MalformedURLException, InterruptedException {

        if (links.size() < 1)
            return;
        while (pagesCount.intValue() < PAGES_LIMIT) {
            while (!links.isEmpty()){
                if(links.size()>1){
                    Random rand = new Random(); //instance of random class
//                    //generate random values from 0-links set size
//                    int int_random = rand.nextInt(links.size());
//                    URL link = new URL((String) links.toArray()[int_random]);
                    int index = rand.nextInt(links.size());
                    Iterator<String> iter = links.iterator();
                    for (int i = 0; i < index; i++) {
                        iter.next();
                    }
                    URL link = new URL(iter.next().toString());
                    try {
                        if(pagesCount.intValue()< PAGES_LIMIT) {
                            boolean alreadyCrawledContain;
                            boolean LinksContain;
                            synchronized (alreadyCrawled)
                            {
                                alreadyCrawledContain = alreadyCrawled.contains(link.toString());
                            }
                            LinksContain = links.contains(link.toString());
                            //check if page is exist in set of this thread and not crawled before.
                            if(LinksContain==true && alreadyCrawledContain==false) {
                                linksExtraction(link, links);
                                System.out.println(link.toString() + "\n" + pagesCount.intValue() + "#thread: " + Thread.currentThread().getName());
                                synchronized (alreadyCrawled) {
                                    alreadyCrawled.add(link.toString());
                                }
                                synchronized (linksSet) {
                                    linksSet.remove(link.toString());
                                }
                                links.remove(link.toString());
                            }
                        }
                        else return;

                    } catch (Exception e) {
                        links.remove(link.toString());
                        if(pagesCount.intValue()< PAGES_LIMIT) {
                            synchronized (linksSet) {
                                linksSet.remove(link.toString());
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
    //function to extract links and images from specific link.
    public  void linksExtraction(URL link,Set<String>links) throws IOException, URISyntaxException {
        Document doc;
        try {
            doc = Jsoup.connect(link.toString()).get();
        } catch (Exception e) {
            linksSet.remove(link.toString());
            links.remove(link.toString());
            return;
        }
        String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
        Set<String> referencedLinks = new HashSet<>();
        Set<image> referencedImages = new HashSet<>();
        int referencesNumber = 0;
        Elements Links = doc.select("a[href]");
        Elements images = doc.select("img");
        for (Element image : images)
        {
            String caption = image.attr("alt");
            //filter captions .. removing emotions and empty captions
            if(caption == "" ||caption == "..." )
            {
                caption = doc.title();}
            String absoluteUrl = image.absUrl("src");  //absolute URL on src
//                System.out.println("image SRC:"+absoluteUrl);
//                System.out.println("image Caption:"+caption);

            String emotionless = caption.replaceAll(characterFilter,"");
            if(imagesLinks.contains(absoluteUrl)==false) {
                referencedImages.add(new image(absoluteUrl, emotionless));
                imagesLinks.add(absoluteUrl);
            }
        }

        //get robots.txt link from the link host
        String robotsTxtURL = normalizeURL(link).toString() + "/robots.txt";
        URL robotstxt = new URL(robotsTxtURL);
        Set<String>NotAllowable=new HashSet<>();
        boolean ConstraintsCheck = false;
        try {
            // to check if the robots.txt has any constraints for my crawler (all crawlers)
            ConstraintsCheck = parseRobotTxt(robotstxt,NotAllowable);
        } catch (IOException e) {
            ConstraintsCheck = false;
        }

        for (Element newLink : Links) {
            if (pagesCount.intValue() < PAGES_LIMIT) {
                String Link = newLink.attr("abs:href");
                Link = new URI(Link).normalize().toString();
                //to normalize link and check if it is different language link.
                boolean redundancyCheck = Custom_Normalize(Link);
                URL URLLink = new URL(Link);
                boolean linksSetContain;
                boolean alreadyCrawledContain;
                synchronized (linksSet) {
                    linksSetContain = linksSet.contains(Link);
                }
                synchronized (alreadyCrawled){
                    alreadyCrawledContain = alreadyCrawled.contains(Link);
                }
                if (URLLink != null &&redundancyCheck==true) {
                    if (linksSetContain == false && links.contains(Link) == false && alreadyCrawledContain ==false && referencedLinks.contains(Link) == false) {
                        // check if link is not allowed in the robots.txt
                        if (checkLink(URLLink.toString(),NotAllowable) == false && ConstraintsCheck) {
                            continue;
                        }
                        referencedLinks.add(Link);
                        if(pagesCount.intValue()< PAGES_LIMIT)
                        {
                            synchronized (alreadyCrawled)
                            {
                                alreadyCrawledContain=alreadyCrawled.contains(Link);
                            }
                            synchronized (linksSet) {
                                //add pages to crawled only if it hasn't been already crawled and not in links set
                                if(linksSet.contains(Link)==false&& alreadyCrawledContain==false) {
                                    linksSet.add(Link);
                                    links.add(Link);
                                    referencesNumber++;
                                }
                            }
                        }
                        else return;
                        //System.out.println(Link + "\n" + pagescount.intValue() + "#thread: " + Thread.currentThread().getName());

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
        pagesCount.incrementAndGet();
    }
    //main function of crawler
    public  void CrawlerProcess(int threads) throws IOException, InterruptedException, URISyntaxException
    {
        pagesCount =new AtomicInteger(0);
        linksSet = new HashSet<String>();
        imagesLinks = new HashSet<>();
        String[] languages=Locale.getISOLanguages();
        languageCodes=Arrays.asList(languages);

        threadsNumber = threads;
        linksSet.add(new URI("https://dmoz-odp.org").normalize().toString());
        linksSet.add(new URI("https://wikipedia.org").normalize().toString());

        newSeed = new PriorityQueue<>();
        crawlerOutput = new HashSet<>();
        alreadyCrawled = new HashSet<>();
        crawlingBase(linksSet);
    }

    //base function responsible for creating threads and assign results of seed set to each thread=>set of pages for each thread
    public  void crawlingBase(Set<String> seedSet) throws IOException, URISyntaxException, InterruptedException {
        List<String> tempSeed;
        Iterator<String>itr;
        if(firstIterationCheck ==false && seedSet.size()<threadsNumber) {
            tempSeed =List.copyOf(seedSet);
            int i=0;
            while (seedSet.size() < threadsNumber||i<tempSeed.size()) {
                linksExtraction(new URL(tempSeed.get(i)), seedSet);
                i++;
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
        linksSet.clear();
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

        //busy waiting for main thread to wait pages limit.
        while(pagesCount.intValue()< PAGES_LIMIT);


        Iterator<ExecutorService>i;
        int size = EXList.size();
        //looping for all threads to ensure that it finished its work and terminate
        for (int k=0;k<size;k++) {
            i=EXList.iterator();
            ExecutorService e = i.next();
            while (!es.isTerminated()) {
                synchronized (linksSet) {
                    linksSet.notifyAll();
                }
                awaitTerminationAfterShutdown(es);
            }

        }
        firstIterationCheck = true;
        //clearing crawled pages set to crawl it in the next iteration of crawler
        linksSet.removeAll(alreadyCrawled);
        alreadyCrawled.clear();
        //check if all pages in links set is already crawled in the previous iteration then use the pages that has
        // many pages in it.
        if(linksSet.size()<1)
        {
            seedSet.clear();
            synchronized (newSeed) {
                for (Link newlink : newSeed) {
                    if (newlink.referencesNumber > 10) {
                        seedSet.add(newlink.linkUrl);
                    }
                }
            }
        }
        else
        {
            //else make the new seed for crawler is the pages in links set that hasn't been crawled
            seedSet.clear();
            seedSet.addAll(linksSet);
        }
        newSeed.clear();
        imagesLinks.clear();

        System.out.println("end of crawling iteration");
        if(seedSet.size()<1) {
            seedSet.add((new URI("https://dmoz-odp.org").normalize().toString()));
            seedSet.add(new URI("https://wikipedia.org").normalize().toString());
        }
        //clear counter to start new iteration.
        pagesCount.set(0);

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