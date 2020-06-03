import java.util.ArrayList;

import static java.lang.Math.abs;
public class Pagerank {

    public int numDocs = 0;   //--number of documnets
    private final static double EPSILON = 0.0001; //--convergence , if error is less than of equal epsilon from iteration to another than stop iterate
    public double[] rank;
    public double startingProb;
    public double[] contribution;
    private final static int MAX_NUMBER_OF_ITERATIONS = 100;
    public double damping_factor = 0.85;
    public ArrayList<ArrayList<Integer>> adjList;

    public Pagerank(ArrayList<ArrayList<Integer>>list) {
        // TODO Auto-generated constructor stub
        this.adjList = list;
        numDocs = adjList.size();

    }

    //-------compute rank of each page
    public void computRank() {
        //--initalize rank of all document with 1/numDocs
        startingProb = 1.0/numDocs;

        rank = new double[numDocs];
        double[] prevProb = new double[numDocs];
        double[] currentProb = new double[numDocs];
        for(int i=0;i<numDocs;i++) {
            rank[i] = startingProb;

        }

        //--start interations ----
        int iter = 0;
        double error =0;
        int index = 0;
        boolean CheackConverge = false;
        calcContribution();
        while(iter++<MAX_NUMBER_OF_ITERATIONS && !CheackConverge) {
            //---loop through all docuemnts --
            printPageRank();
            prevProb = rank;//--save prevProb
            currentProb = new double[numDocs];
            for(int i=0;i<numDocs;i++) {
                //--calc page rank

                for(int j=0;j<adjList.get(i).size();j++){
                    index = adjList.get(i).get(j);
                    currentProb[index] += (damping_factor*prevProb[i]/contribution[i]);


                }


            }
            //--adding suffer
            for(int i=0;i<numDocs;i++) {
                currentProb[i] += (1-damping_factor)/numDocs;
            }
            rank = currentProb;

            CheckSink(iter);
            // printPageRank();
            double sum=0;
            for(int i=0;i<numDocs;i++){
                sum+=rank[i];

            }

            //  normalize(sum);

            rank = currentProb;
            CheackConverge = didConverge(prevProb,currentProb);
        }


    }

    //-----create list contains all pages every list contains indices of refLinks

    //--check if the pagerank of all pages are converge or not
    public boolean didConverge(double []oldProb, double []currentProb) {

        for(int i=0;i<currentProb.length;i++) {

            if(abs(oldProb[i]-currentProb[i]) > EPSILON) {
                return false;
            }

        }
        return true;
    }
    //--calculate contribution of every page --number of links in every page
    public void calcContribution(){
        contribution = new double[numDocs];
        for(int i=0;i<numDocs;i++){
            contribution[i] = adjList.get(i).size();

        }

    }
    public void normalize(double num){
        for(int i=0;i<numDocs;i++){
            rank[i] = rank[i]/num;

        }

    }
    public void CheckSink(int iter){
        // if(iter>1){return;}
        for(int i=0;i<numDocs;i++){
            if(rank[i]==0){
                rank[i] = startingProb;
            }

        }

    }
    public void printPageRank(){
        for(int i=0;i<numDocs;i++){
            System.out.println(rank[i]);
        }
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ArrayList<ArrayList<Integer>> adj = new ArrayList<ArrayList<Integer>>();


        ArrayList<Integer>tmp = new ArrayList<>();
        tmp.add(1);
        tmp.add(2);
        tmp.add(3);

        adj.add(tmp); //-A-
        //    tmp.clear();
        tmp = new ArrayList<>();
        tmp.add(0);


        adj.add(tmp);

        //tmp.clear();
        tmp = new ArrayList<>();
        tmp.add(0);
        adj.add(tmp);

        // tmp.clear();
        tmp = new ArrayList<>();
        tmp.add(0);


        adj.add(tmp);

        Pagerank p = new Pagerank(adj);
        p.computRank();
        p.printPageRank();

    }

}
