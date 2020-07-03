//import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.sql.SQLException;
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.tartarus.snowball.ext.PorterStemmer;
//
//public class Page implements Serializable {
//
//    public Document doc;
//    public String title;
//    public int ID;
//    public int wordCount;
//    public Double pop;
//    public String url;
//    public ArrayList<String> links ;
//    public String description;
//    public Set<Crawler.image> referencedImages;
//    public Hashtable<String, Integer> words ;
//    public Hashtable<String, ArrayList<String>> images ;
//    static public List<String> stopwords = Arrays.asList("abroad", "according", "accordingly", "across", "actually", "adj", "after", "afterwards", "again", "against", "ago", "ahead", "ain’t", "all", "allow", "allows",
//            "almost", "alone", "along", "alongside", "already", "also", "although", "always", "am", "amid", "amidst", "among", "amongst","a", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "aren’t", "around", "as", "a’s", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully",
//            "back","backward","backwards","be","became","because","become","becomes","becoming","been","before","beforehand","begin","behind","being","believe","below","beside","besides","best","better","between","beyond","both","brief","but","by",
//            "came","can","cannot","cant","can’t","caption","cause","causes","certain","certainly","changes","clearly","c’mon","co","co.","com","come","comes","concerning","consequently","consider","considering","contain","containing","contains","corresponding","could","couldn’t","course","c’s","currently",
//            "dare","daren’t","definitely","described","despite","did","didn’t","different","directly","do","does","doesn’t","doing","done","don’t","down","downwards","durin",
//            "each","edu","eg","eight","eighty","either","else","elsewhere","end","ending","enough","entirely","especially","et","etc","even","ever","evermore","every","everybody","everyone","everything","everywhere","ex","exactly","example","excep",
//            "fairly","far","farther","few","fewer","fifth","first","five","followed","following","follows","for","forever","former","formerly","forth","forward","found","four","from","further","furthermore",
//            "get","gets","getting","given","gives","go","goes","going","gone","got","gotten","greetings",
//            "had","hadn’t","half","happens","hardly","has","hasn’t","have","haven’t","having","he","he’d","he’ll","hello","help","hence","her","here","hereafter","hereby","herein","here’s","hereupon","hers","herself","he’s","hi","him","himself","his","hither","hopefully","how","howbeit","however","hundred",
//            "i’d","ie","if","ignored","i’ll","i’m","immediate","in","inasmuch","inc","inc.","indeed","indicate","indicated","indicates","inner","inside","insofar","instead","into","inward","is","isn’t","it","it’d","it’ll","its","it’s","itself","i’ve",
//            "just","keep","keeps","kept","know","known","knows","last","lately","later","latter","latterly","least","less","lest","let","let’s","like","liked","likely","likewise","little","look","looking","looks","low","lower","ltd",
//            "made","mainly","make","makes","many","may","maybe","mayn’t","me","mean","meantime","meanwhile","merely","might","mightn’t","mine","minus","miss","more","moreover","most","mostly","mr","mrs","much","must","mustn’t","my","mysel",
//            "name","namely","nd","near","nearly","necessary","need","needn’t","needs","neither","never","neverf","neverless","nevertheless","new","next","nine","ninety","no","nobody","non","none","nonetheless","noone","no-one","nor","normally","not","nothing","notwithstanding","novel","now","nowhere",
//            "obviously","of","off","often","oh","ok","okay","old","on","once","one","ones","one’s","only","onto","opposite","or","other","others","otherwise","ought","oughtn’t","our","ours","ourselves","out","outside","over","overall","own",
//            "particular","particularly","past","per","perhaps","placed","please","plus","possible","presumably","probably","provided","provides",
//            "rather","rd","re","really","reasonably","recent","recently","regarding","regardless","regards","relatively","respectively","right","round",
//            "said","same","saw","say","saying","says","second","secondly","see","seeing","seem","seemed","seeming","seems","seen","self","selves","sensible","sent","serious","seriously","seven","several","shall","shan’t","she","she’d","she’ll","she’s","should","shouldn’t","since","six","so","some","somebody","someday","somehow","someone","something","sometime","sometimes","somewhat","somewhere","soon","sorry","specified","specify","specifying","still","sub","such","sup","sure",
//            "take","taken","taking","tell","tends","th","than","thank","thanks","thanx","that","that’ll","thats","that’s","that’ve","the","their","theirs","them","themselves","then","thence","there","thereafter","thereby","there’d","therefore","therein","there’ll","there’re","theres","there’s","thereupon","there’ve","these","they","they’d","they’ll","they’re","they’ve","thing","things","think","third","thirty","this","thorough","thoroughly","those","though","three","through",
//            "throughout","thru","thus","till","to","together","too","took","toward","towards","tried","tries","truly","try","trying","t’s","twice","two",
//            "un","under","underneath","undoing","unfortunately","unless","unlike","unlikely","until","unto","up","upon","upwards","us","use","used","useful","uses","using","usually",
//            "value","various","versus","very","via","viz","vs",
//            "want","wants","was","wasn’t","way","we","we’d","welcome","well","we’ll","went","were","we’re","weren’t","we’ve","what","whatever","what’ll","what’s","what’ve","when","whence","whenever","where","whereafter","whereas","whereby","wherein","where’s","whereupon","wherever","whether","which","whichever","while","whilst","whither","who","who’d","whoever","whole","who’ll","whom","whomever","who’s","whose","why","will","willing","wish","with","within","without","wonder","won’t","would","wouldn’",
//            "yes","yet","you","you’d","you’ll","your","you’re","yours","yourself","yourselves","you’ve",
//            "zero","the", "and", "are", "is","i" , "he" ,"she" , "over" , "and", "it" , "will" , "be"
//    );
//
//    public Page(Document doc, String urll) {
//        //this.hash = new Hashtable<String, Integer >();
//        this.doc = doc;
//        this.url = urll;
//        this.words = new Hashtable<String, Integer>();
////        this.links = new ArrayList<String>();
//        this.referencedImages = new HashSet<Crawler.image>();
//    }
//    public Page(String url, String tit, String dis) {
//        this.title = tit;
//        this.url = url;
//        this.description =dis;
//    }
//    public Page(String url, String tit, String dis,int id,int count) {
//        this.title = tit;
//        this.url = url;
//        this.description =dis;
//        this.ID = id;
//        this.wordCount = count;
//    }
//    public Page(String url, String tit, String dis,int id,int count,Double pop) {
//        this.title = tit;
//        this.url = url;
//        this.description =dis;
//        this.ID = id;
//        this.wordCount = count;
//        this.pop = pop;
//    }
//    public Page(String url, String tit, String dis,int id) {
//        this.title = tit;
//        this.url = url;
//        this.description =dis;
//        this.ID = id;
//    }
//
//    public Page(String url, String tit,int id) {
//        this.title = tit;
//        this.url = url;
//        this.ID = id;
//    }
//    public void ExtractWords() {
//        wordCount=0;
//        String text = title+" "+description;
//
//
//        ///////////////////////////////////stop words////////////////////////////////////
//        //String query = "mu name is a fish who can sleep at night it means beautiful world will alwyas be beautiful";
//        ArrayList<String> allWords = Stream.of(text.toLowerCase().split(" "))
//                .collect(Collectors.toCollection(ArrayList<String>::new));
//        allWords.removeAll(stopwords);
//        String result = allWords.stream().collect(Collectors.joining(" "));
//        ///////////////////////////////////stop words///////////////////////////////////
//
//        ///////////////////////////////////punctuation//////////////////////////////////
//        //String st = "hey, you, yes, you?";
//        String stu =  result.replaceAll("[^a-zA-Z ]", "");
//        ///////////////////////////////////punctuation//////////////////////////////////
//
//        PorterStemmer porterStemmer = new PorterStemmer();
//        String[] arr = stu.split(" ");
//        wordCount = arr.length;
//        int j;
//        for (String word : arr) {
//            porterStemmer.setCurrent(word);
//            boolean stemy = porterStemmer.stem();
//            String stem = porterStemmer.getCurrent();
//            if (stemy) {
//                if(!stem.equals("")){
//                    if (words.containsKey(stem)) {
//                        j = words.get(stem);
//                        j++;
//                        words.replace(stem, j);
//                    } else {
//                        words.put(stem, 1);
//                    }
//                }
//            }
//            else {
//                if (words.containsKey(word)) {
//                    j = words.get(word);
//                    j++;
//                    words.replace(word, j);
//                } else {
//                    words.put(word, 1);
//                }
//            }
//        }
//    }
//
//    public ArrayList<String> ExtractImagesWords(Crawler.image img){
//
//        ArrayList<String> allWords = Stream.of(img.imageCaption.toLowerCase().split(" "))
//                .collect(Collectors.toCollection(ArrayList<String>::new));
//        allWords.removeAll(stopwords);
//        ArrayList<String>str = new ArrayList<>();
//        str.add(img.imageCaption);
//        String result = allWords.stream().collect(Collectors.joining(" "));
//        ///////////////////////////////////stop words///////////////////////////////////
//
//        ///////////////////////////////////punctuation//////////////////////////////////
//        String stu =  result.replaceAll("[^a-zA-Z ]", " ");
//        PorterStemmer porterStemmer = new PorterStemmer();
//        String[] arr = stu.split(" ");
//        wordCount = arr.length;
//        int j;
//        for (String word : arr) {
//            porterStemmer.setCurrent(word);
//            boolean stemy = porterStemmer.stem();
//            String stem = porterStemmer.getCurrent();
//            if (stemy) {
//                if(!stem.equals("")){
//                    if(!str.contains(stem)){
//                        str.add(stem);
//                    }
//                }
//            }
//            else {
//                if(!str.contains(stem)){
//                    str.add(stem);
//                }
//            }
//        }
//        return str;
//    }
//
//    public void metaData(){
//        this.title = doc.title();
//        if(!doc.select("meta[name=description]").isEmpty()){
//            String descrip = doc.select("meta[name=description]").get(0).attr("content");
//            this.description = descrip;
//        }
//        else{
//            this.description = "";
//        }
//    }
//    public ArrayList<String> getLinks(){
//        return links;
//    }
//    public Hashtable<String, Integer> getwords(){
//        return words;
//    }
//
//    public void setLinks(ArrayList<String> l){
//        links =new ArrayList<>();
//        this.links = l;}
//    public void setLinksSet(Set<String> l){
//        links =new ArrayList<>();
//        Iterator<String> itr = l.iterator();
//        while(itr.hasNext()){
//            links.add(itr.next());
//        }
//    }
//    public void setwords(Hashtable<String, Integer> w){this.words = w;}
//
//    public Double getPop(){
//        return pop;
//    }
//    public int getWordCount(){
//        return wordCount;
//    }
//    public void setPop(Double p){
//        pop = p;
//    }
//
//    public int getID(){
//        return ID;
//    }
//
//    public void setID(int id){
//        ID = id;
//    }
//
//    public String getURL(){
//        return url;
//    }
//
//    public String gettitle(){
//        return title;
//    }
//
//    public String getDescription(){
//        return description;
//    }
//    public Hashtable<String,ArrayList<String>> getImages(){
//        return this.images;
//    }
//
//    public void setImages(Set<Crawler.image> imge){
//        this.referencedImages = imge;
//        this.images = new Hashtable<>();
//        if(!referencedImages.isEmpty()) {
//            for (Crawler.image img : referencedImages) {
//                this.images.put(img.imageSrc,ExtractImagesWords(img));
//            }
//        }
//    }
//}