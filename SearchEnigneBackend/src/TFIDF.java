import java.util.Arrays;
import java.util.List;
public class TFIDF {
    //---this function calc the term frequency given a document and term
    public double tf(List<String>doc, String term) {
        double res=0;

        for(String word:doc) {

            if(term.equalsIgnoreCase(word)) {
                res++;

            }

        }

        return res/doc.size();
    }

    //---this function calc the inverse term frequency
    public double idf(List<List<String>>docs, String term) {
        double n = 0;
        for(List<String>doc:docs) {
            for(String word:doc) {
                if(term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log(docs.size()/n);
    }

    //---this function calc the tfidf
    public double tfidf(List<List<String>>docs,List<String>doc,String term) {
        return tf(doc,term)*idf(docs,term);
    }
    public TFIDF() {
        // TODO Auto-generated constructor stub



    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        List<String> doc1 = Arrays.asList("Lorem", "ipsum", "dolor", "ipsum", "sit", "ipsum");
        List<String> doc2 = Arrays.asList("Vituperata", "incorrupte", "at", "ipsum", "pro", "quo");
        List<String> doc3 = Arrays.asList("Has", "persius", "disputationi", "id", "simul");
        List<List<String>> documents = Arrays.asList(doc1, doc2, doc3);

        TFIDF calculator = new TFIDF();
        double tfidf = calculator.tfidf(documents,doc1, "ipsum");
        System.out.println("TF-IDF (ipsum) = " + tfidf);

    }

}
