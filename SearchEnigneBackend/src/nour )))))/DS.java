//
//import java.util.ArrayList;
//import java.util.Hashtable;
//import java.util.Set;
//
//public class DS {
//    public Hashtable<String, Integer> hash;
//    public Hashtable<String, ArrayList> hashDocs;
//
//    DS() {
//        this.hash = new Hashtable<String, Integer>();
//        this.hashDocs = new Hashtable<String, ArrayList>();
//    }
//
//    public void add(Page t) {
//        Hashtable<String, Integer> wordat = new Hashtable<String, Integer>();
//        wordat = t.getwords();
//        int j;
//        Set<String> keys = wordat.keySet();
//        for (String key : keys) {
//            if (hash.containsKey(key)) {
//                j = hash.get(key);
//                j++;
//                hash.replace(key, j);
//                hashDocs.get(key).add(t.url);
//            } else {
//                hash.put(key, 1);
//                hashDocs.put(key, new ArrayList<String>());
//                hashDocs.get(key).add(t.url);
//            }
//        }
//    }
//
//    public boolean findWord(String find) {
//        if (hash.containsKey(find)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public Integer count(String find) {
//        return hash.get(find);
//    }
//
//    public ArrayList<String> Docs(String find) {
//        return hashDocs.get(find);
//    }
//}
