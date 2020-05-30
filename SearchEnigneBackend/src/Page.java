import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.tartarus.snowball.ext.PorterStemmer;

public class Page implements Serializable {

    public Document doc;
    public String title;
    public String name;
    public int ID;
    public Double pop;
    public String url;
    public ArrayList<String> links ;
    public String description;
    //public Set<Crawler.image> referencedImages;
    public Hashtable<String, Integer> words ;
    static public List<String> stopwords = Arrays.asList("abroad", "according", "accordingly", "across", "actually", "adj", "after", "afterwards", "again", "against", "ago", "ahead", "ain’t", "all", "allow", "allows",
            "almost", "alone", "along", "alongside", "already", "also", "although", "always", "am", "amid", "amidst", "among", "amongst","a", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "aren’t", "around", "as", "a’s", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully",
            "back","backward","backwards","be","became","because","become","becomes","becoming","been","before","beforehand","begin","behind","being","believe","below","beside","besides","best","better","between","beyond","both","brief","but","by",
            "came","can","cannot","cant","can’t","caption","cause","causes","certain","certainly","changes","clearly","c’mon","co","co.","com","come","comes","concerning","consequently","consider","considering","contain","containing","contains","corresponding","could","couldn’t","course","c’s","currently",
            "dare","daren’t","definitely","described","despite","did","didn’t","different","directly","do","does","doesn’t","doing","done","don’t","down","downwards","durin",
            "each","edu","eg","eight","eighty","either","else","elsewhere","end","ending","enough","entirely","especially","et","etc","even","ever","evermore","every","everybody","everyone","everything","everywhere","ex","exactly","example","excep",
            "fairly","far","farther","few","fewer","fifth","first","five","followed","following","follows","for","forever","former","formerly","forth","forward","found","four","from","further","furthermore",
            "get","gets","getting","given","gives","go","goes","going","gone","got","gotten","greetings",
            "had","hadn’t","half","happens","hardly","has","hasn’t","have","haven’t","having","he","he’d","he’ll","hello","help","hence","her","here","hereafter","hereby","herein","here’s","hereupon","hers","herself","he’s","hi","him","himself","his","hither","hopefully","how","howbeit","however","hundred",
            "i’d","ie","if","ignored","i’ll","i’m","immediate","in","inasmuch","inc","inc.","indeed","indicate","indicated","indicates","inner","inside","insofar","instead","into","inward","is","isn’t","it","it’d","it’ll","its","it’s","itself","i’ve",
            "just","keep","keeps","kept","know","known","knows","last","lately","later","latter","latterly","least","less","lest","let","let’s","like","liked","likely","likewise","little","look","looking","looks","low","lower","ltd",
            "made","mainly","make","makes","many","may","maybe","mayn’t","me","mean","meantime","meanwhile","merely","might","mightn’t","mine","minus","miss","more","moreover","most","mostly","mr","mrs","much","must","mustn’t","my","mysel",
            "name","namely","nd","near","nearly","necessary","need","needn’t","needs","neither","never","neverf","neverless","nevertheless","new","next","nine","ninety","no","nobody","non","none","nonetheless","noone","no-one","nor","normally","not","nothing","notwithstanding","novel","now","nowhere",
            "obviously","of","off","often","oh","ok","okay","old","on","once","one","ones","one’s","only","onto","opposite","or","other","others","otherwise","ought","oughtn’t","our","ours","ourselves","out","outside","over","overall","own",
            "particular","particularly","past","per","perhaps","placed","please","plus","possible","presumably","probably","provided","provides",
            "rather","rd","re","really","reasonably","recent","recently","regarding","regardless","regards","relatively","respectively","right","round",
            "said","same","saw","say","saying","says","second","secondly","see","seeing","seem","seemed","seeming","seems","seen","self","selves","sensible","sent","serious","seriously","seven","several","shall","shan’t","she","she’d","she’ll","she’s","should","shouldn’t","since","six","so","some","somebody","someday","somehow","someone","something","sometime","sometimes","somewhat","somewhere","soon","sorry","specified","specify","specifying","still","sub","such","sup","sure",
            "take","taken","taking","tell","tends","th","than","thank","thanks","thanx","that","that’ll","thats","that’s","that’ve","the","their","theirs","them","themselves","then","thence","there","thereafter","thereby","there’d","therefore","therein","there’ll","there’re","theres","there’s","thereupon","there’ve","these","they","they’d","they’ll","they’re","they’ve","thing","things","think","third","thirty","this","thorough","thoroughly","those","though","three","through",
            "throughout","thru","thus","till","to","together","too","took","toward","towards","tried","tries","truly","try","trying","t’s","twice","two",
            "un","under","underneath","undoing","unfortunately","unless","unlike","unlikely","until","unto","up","upon","upwards","us","use","used","useful","uses","using","usually",
            "value","various","versus","very","via","viz","vs",
            "want","wants","was","wasn’t","way","we","we’d","welcome","well","we’ll","went","were","we’re","weren’t","we’ve","what","whatever","what’ll","what’s","what’ve","when","whence","whenever","where","whereafter","whereas","whereby","wherein","where’s","whereupon","wherever","whether","which","whichever","while","whilst","whither","who","who’d","whoever","whole","who’ll","whom","whomever","who’s","whose","why","will","willing","wish","with","within","without","wonder","won’t","would","wouldn’",
            "yes","yet","you","you’d","you’ll","your","you’re","yours","yourself","yourselves","you’ve",
            "zero","the", "and", "are", "is","i" , "he" ,"she" , "over" , "and", "it" , "will" , "be"
    );

    public Page(Document doc, String urll) {
        //this.hash = new Hashtable<String, Integer >();
        this.doc = doc;
        this.url = urll;
        this.words = new Hashtable<String, Integer>();
        this.links = new ArrayList<String>();
        //this.referencedImages = new HashSet<Crawler.image>();
    }
    public Page(String url, String tit, String dis) {
        this.title = tit;
        this.url = url;
        this.description =dis;
    }
    public Page(String url, String tit, String dis,int id) {
        this.title = tit;
        this.url = url;
        this.description =dis;
        this.ID = id;
    }
    public Page(String url, String tit,int id) {
        this.title = tit;
        this.url = url;
        this.ID = id;
    }
    public void ExtractWords() {
        String text = title+" "+description;

        //System.out.println("after removing stop words "+text);
        ///////////////////////////////////stop words////////////////////////////////////
        //String query = "mu name is a fish who can sleep at night it means beautiful world will alwyas be beautiful";
        ArrayList<String> allWords = Stream.of(text.toLowerCase().split(" "))
                .collect(Collectors.toCollection(ArrayList<String>::new));
        allWords.removeAll(stopwords);
        String result = allWords.stream().collect(Collectors.joining(" "));
        //System.out.println("after removing stop words "+result);
        ///////////////////////////////////stop words///////////////////////////////////

        ///////////////////////////////////punctuation//////////////////////////////////
        //String st = "hey, you, yes, you?";
        String stu =  result.replaceAll("[^a-zA-Z ]", "");
        //System.out.println("after removing punc."+stu);
        ///////////////////////////////////punctuation//////////////////////////////////

        PorterStemmer porterStemmer = new PorterStemmer();
        String[] arr = stu.split(" ");
        int j;
        for (String word : arr) {
            porterStemmer.setCurrent(word);
            boolean stemy = porterStemmer.stem();
            String stem = porterStemmer.getCurrent();
            if (stemy) {
                if(stem !=" "){
                    //System.out.println("The stem of " + word + " is " + stem);
                    if (words.containsKey(stem)) {
                        j = words.get(stem);
                        j++;
                        words.replace(stem, j);
                    } else {
                        words.put(stem, 1);
                    }
                }
            }
            else {
                //System.out.println(word);
                if (words.containsKey(word)) {
                    j = words.get(word);
                    j++;
                    words.replace(word, j);
                } else {
                    words.put(word, 1);
                }
            }
        }
    }
    public void metaData(){
        //Elements metaTags = doc.getElementsByTag("meta");
        this.title = doc.title();
        //System.out.println(title);
        //this.name = doc.select("meta[name=name]").get(0).attr("content");
        if(!doc.select("meta[name=description]").isEmpty()){
            String descrip = doc.select("meta[name=description]").get(0).attr("content");
            this.description = descrip;
        }
        else{
            //String descrip = doc.select("meta[name=description]").get(0).attr("content");
            this.description = " ";
        }

    }
    public void Extractlinks() {
        Elements lins = doc.select("a[href]");
        for (Element link : lins) {
            if(!(link.attr("href").equals("/")))
            {
                if (!(links.contains(link.attr("href")))){
                    links.add(link.attr("href"));
                }
            }
        }
    }
    public ArrayList<String> getLinks(){
        return links;
    }
    public Hashtable<String, Integer> getwords(){
        return words;
    }

    public void setLinks(ArrayList<String> l){this.links = l;}
    public void setLinksSet(Set<String> l){
        Iterator<String> itr = l.iterator();
        while(itr.hasNext()){
            links.add(itr.next());
        }
    }
    public void setwords(Hashtable<String, Integer> w){this.words = w;}

    public Double getPop(){
        return pop;
    }

    public void setPop(Double p){
        pop = p;
    }

    public int getID(){
        return ID;
    }

    public void setID(int id){
        ID = id;
    }

    public String getURL(){
        return url;
    }

    public String gettitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }
//    public Set<Crawler.image> getImages(){
//        return referencedImages;
//    }
//
//    public void setImages(Set<Crawler.image> images){
//        if(!(images.isEmpty())) {
//            //System.out.println(images);
//            this.referencedImages = images;
//
//            //System.out.println(referencedImages);
//        }
//    }

    public static void printPages(List<Page> p){
        for (int i = 0; i < p.size(); i++) {
            System.out.println(p.get(i).gettitle());
            System.out.println(p.get(i).getURL());
            System.out.println(p.get(i).getLinks());
            System.out.println(p.get(i).getwords());
            System.out.println(p.get(i).getID());
        }
    }
    public static class manageDB{
        public static SQLiteJDBC DB;
        public static final long serialVersionuibUID = 1L;
        public static Boolean queryAccepted=false;
        public static void docProcess(Crawler.OutputDoc crawlerOutput) {
            //Iterator<Crawler.OutputDoc> itr = crawlerOutput.iterator();
            System.out.println("yes!");
            //itr.next().getUrl();
            DB = new SQLiteJDBC();
            try {
                DB.open();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Page t = new Page(crawlerOutput.doc, crawlerOutput.getUrl().toString());
            //t.setImages(crawlerOutput.referencedImages);

            t.metaData();
            t.ExtractWords();
            t.setLinksSet(crawlerOutput.referencedLinks);
            t.Extractlinks();
            DB.createPage(t);
            try {
                DB.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Thread T= new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ServerSocket serverSocket=new ServerSocket(7800);
                        System.out.println("server is up and running...");
                        while (true) {
                            Socket s=	serverSocket.accept();
                            queryAccepted=true;
                            System.out.println("server accepted the query...");
                            DataInputStream DIS=new DataInputStream(s.getInputStream());
                            String query_and_flags=DIS.readUTF();
                            DIS.close();
                            s.close();
                            Boolean imageSearch=false;
                            Boolean phraseSearch=false;
                            System.out.println("query_and_flags="+query_and_flags);
                            String[] list_query_and_flags=query_and_flags.split("@");
                            String query=list_query_and_flags[0];
                            if(list_query_and_flags[1].equals("phrase"))
                                phraseSearch=true;
                            if(list_query_and_flags[2].equals("yes"))
                                imageSearch=true;
                            DB.retrievePagesSetWords(query);
                            if(imageSearch)
                                System.out.println("it's image search");
                            else System.out.println("no image search");
                            if(phraseSearch)
                                System.out.println("it's phrase search");
                            else System.out.println("no phrase search");

                            queryAccepted=false;

                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                }
            }
            );
            T.start();
        }
        public static void main(String[] arg){
            Thread T= new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ServerSocket serverSocket=new ServerSocket(7800);
                        System.out.println("server is up and running...");
                        DB = new SQLiteJDBC();

                        while (true) {
                            DB.open();
                            Socket s=	serverSocket.accept();
                            queryAccepted=true;
                            System.out.println("server accepted the query...");
                            DataInputStream DIS=new DataInputStream(s.getInputStream());
                            String query_and_flags=DIS.readUTF();
                            Boolean imageSearch=false;
                            Boolean phraseSearch=false;
                            System.out.println("query_and_flags="+query_and_flags);
                            String[] list_query_and_flags=query_and_flags.split("@");
                            String query=list_query_and_flags[0];
                            if(list_query_and_flags[1].equals("phrase"))
                                phraseSearch=true;
                            if(list_query_and_flags[2].equals("yes"))
                                imageSearch=true;
                            DB.retrievePagesSetWords(query);
                            if(imageSearch)
                                System.out.println("it's image search");
                            else System.out.println("no image search");
                            if(phraseSearch)
                                System.out.println("it's phrase search");
                            else System.out.println("no phrase search");
                            DIS.close();
                            s.close();
                            queryAccepted=false;
                            DB.close();

                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }


                }
            }
            );
            T.start();
        }
//            DB = new SQLiteJDBC();
//            System.out.println("yes!");
//            List<Page> p;
//            try {
//                DB.open();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//            Hashtable<String, Integer> allWords;
//            //p = DB.retrievePagesSetWords("dmoz moz");
//            //allWords = DB.retrieveSetOfWordsCount("dmoz moz");
//            //DB.setPop(1,8999);
//            //printPages(p);
//            //System.out.println(allWords);
//            try {
//                DB.close();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
