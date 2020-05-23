//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Hashtable;
//import java.util.Set;
//
//public class DS {
//    public Hashtable<String, Integer > hash ;
//    public Hashtable<String, ArrayList> hashDocs;
//    DS(){
//        this.hash = new Hashtable<String, Integer >();
//        this.hashDocs = new Hashtable<String, ArrayList>();
//    }
//    public void add(testest t){
//        Hashtable <String,Integer> wordat = new Hashtable <String,Integer>();
//        wordat = t.getwords();
//        int j;
//        Set<String> keys = wordat.keySet();
//        for(String key: keys){
//            if(hash.containsKey(key)){
//                j = hash.get(key);
//                j++;
//                hash.replace(key, j);
//                hashDocs.get(key).add(t.url);
//            }
//            else{
//                hash.put(key, 1);
//                hashDocs.put(key, new ArrayList<String>());
//                hashDocs.get(key).add(t.url);
//            }
//        }
//    }
//
//    public boolean findWord(String find){
//        if(hash.containsKey(find)){
//            return true;
//        }
//        else{
//            return false;
//        }
//    }
//
//    public Integer count(String find){
//        return hash.get(find);
//    }
//
//    public ArrayList<String> Docs(String find){
//        return hashDocs.get(find);
//    }
//
//    public static void main(String[] args) throws IOException {
//        DS ds = new DS();
//        testest t1 = new testest();
//        testest t2 = new testest();
//        testest t3 = new testest();
//
//        t1.ExtractWords("<html><head><title>a Sampling sontenting, is a stupid for being a fuck like zmalek is ugly Content</title></head>\"\n" +
//                "//                + \"<body>\"\n" +
//                "//                + \"<p>a Sampling sontenting, is a stupid for being a fuck like zmalek is ugly Content</p>\"\n" +
//                "//                + \"<div id='sampleDiv'><a href='www.google.com'>Google</a>\"\n" +
//                "//                + \"<div id='sampleDiv'><a href='www.google.com'>Google</a>\"\n" +
//                "//                + \"<div id='sampleDiv'><a href='www.google.com'>Google</a>\"\n" +
//                "//                + \"<h3><a>Sample</a><h3>\"\n" +
//                "//                +\"</div>\"\n" +
//                "//                +\"</body></html>");
//        t2.ExtractWords("<html><head><title>a Sampling sontenting, is a stupid for being a fuck like zmalek is ugly Content</title></head>\"\n" +
//                "//                + \"<body>\"\n" +
//                "//                + \"<p>a Sampling sontenting, is a stupid for being a fuck like zmalek is ugly Content</p>\"\n" +
//                "//                + \"<div id='sampleDiv'><a href='www.google.com'>Google</a>\"\n" +
//                "//                + \"<div id='sampleDiv'><a href='www.google.com'>Google</a>\"\n" +
//                "//                + \"<div id='sampleDiv'><a href='www.google.com'>Google</a>\"\n" +
//                "//                + \"<h3><a>Sample</a><h3>\"\n" +
//                "//                +\"</div>\"\n" +
//                "//                +\"</body></html>");
//        t3.ExtractWords("<html><head><title>a Sampling sontenting, is a stupid for being a fuck like zmalek is ugly Content</title></head>\"\n" +
//                "//                + \"<body>\"\n" +
//                "//                + \"<p>a Sampling sontenting, is a stupid for being a fuck like zmalek is ugly Content</p>\"\n" +
//                "//                + \"<div id='sampleDiv'><a href='www.google.com'>Google</a>\"\n" +
//                "//                + \"<div id='sampleDiv'><a href='www.google.com'>Google</a>\"\n" +
//                "//                + \"<div id='sampleDiv'><a href='www.google.com'>Google</a>\"\n" +
//                "//                + \"<h3><a>Sample</a><h3>\"\n" +
//                "//                +\"</div>\"\n" +
//                "//                +\"</body></html>");
//
//        ds.add(t1);ds.add(t2);ds.add(t3);
//        System.out.println(ds.count("fuck"));
//        System.out.println(ds.count("fuck"));
//        System.out.println(ds.count("yahoo"));
//    }
//}
