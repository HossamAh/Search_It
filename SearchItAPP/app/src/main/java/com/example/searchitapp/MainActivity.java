package com.example.searchitapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.searchitapp.models.QueryResultItem;

import org.apache.commons.lang3.StringUtils;
import org.tartarus.snowball.ext.PorterStemmer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {
    SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
            MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SearchView mainSearchView= findViewById(R.id.searchView);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mainSearchView.setIconifiedByDefault(false);
        mainSearchView.setQueryHint("type your query!");
        mainSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        Intent intent = getIntent();
        Button del_suggestions=findViewById(R.id.delete_sugg);
        del_suggestions.setOnClickListener(view -> {
            suggestions.clearHistory();
        });
        try {
            handleIntent(intent,suggestions);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        try {
            handleIntent(intent,suggestions);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void handleIntent(Intent intent,SearchRecentSuggestions suggestions) throws InterruptedException {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            // Do work using string
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                suggestions.saveRecentQuery(query, null);
                processMyQuery(query);
            }
        }}

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void processMyQuery(String query) throws InterruptedException {
        CheckBox imageSearch=findViewById(R.id.checkBox);
    // query processing
    /*Toast.makeText(com.example.MainActivity.this,
    "you typed: "+query, Toast.LENGTH_LONG).show();*/
    List<String> stopwords = Arrays.asList("abroad", "according", "accordingly", "across", "actually", "adj", "after", "afterwards", "again", "against", "ago", "ahead", "ain’t", "all", "allow", "allows",
                "almost", "alone", "along", "alongside", "already", "also", "although", "always", "am", "amid", "amidst", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "aren’t", "around", "as", "a’s", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully",
                "back","backward","backwards","be","became","because","become","becomes","becoming","been","before","beforehand","begin","behind","being","believe","below","beside","besides","best","better","between","beyond","both","brief","but","by",
                "came","can","cannot","cant","can’t","caption","cause","causes","certain","certainly","changes","clearly","c’mon","co","co.","com","come","comes","concerning","consequently","consider","considering","contain","containing","contains","corresponding","could","couldn’t","course","c’s","currently",
                "dare","daren’t","definitely","described","despite","did","didn’t","different","directly","do","does","doesn’t","doing","done","don’t","down","downwards","durin",
                "each","edu","eg","eight","eighty","either","else","elsewhere","end","ending","enough","entirely","especially","et","etc","even","ever","evermore","every","everybody","everyone","everything","everywhere","ex","exactly","example","excep",
                "fairly","far","farther","few","fewer","fifth","first","five","followed","following","follows","for","forever","former","formerly","forth","forward","found","four","from","further","furthermore",
                "get","gets","getting","given","gives","go","goes","going","gone","got","gotten","greetings",
                "had","hadn’t","half","happens","hardly","has","hasn’t","have","haven’t","having","he","he’d","he’ll","hello","help","hence","her","here","hereafter","hereby","herein","here’s","hereupon","hers","herself","he’s","hi","him","himself","his","hither","hopefully","how","howbeit","however","hundred",
                "i’d","ie","if","ignored","i’ll","i’m","immediate","in","inasmuch","inc","inc.","indeed","indicate","indicated","indicates","inner","inside","insofar","instead","into","inward","is","isn’t","it","it’d","it’ll","its","it’s","itself","i’ve",
                "just","keep","keeps","kept","last","lately","later","latter","latterly","least","less","lest","let","let’s","like","liked","likely","likewise","little","look","looking","looks","low","lower","ltd",
                "made","mainly","make","makes","many","may","maybe","mayn’t","me","mean","meantime","meanwhile","merely","might","mightn’t","mine","minus","miss","more","moreover","most","mostly","mr","mrs","much","must","mustn’t","my","mysel",
                "name","namely","nd","near","nearly","necessary","need","needn’t","needs","neither","never","neverf","neverless","nevertheless","new","next","nine","ninety","no","nobody","non","none","nonetheless","noone","no-one","nor","normally","not","nothing","notwithstanding","novel","now","nowhere",
                "obviously","of","off","often","oh","ok","okay","old","on","once","one","ones","one’s","only","onto","opposite","or","other","others","otherwise","ought","oughtn’t","our","ours","ourselves","out","outside","over","overall","own",
                "particular","particularly","past","per","perhaps","placed","please","plus","possible","presumably","probably","provided","provides",
                "rather","rd","re","really","reasonably","recent","recently","regarding","regardless","regards","relatively","respectively","right","round",
                "said","same","saw","say","saying","says","second","secondly","see","seeing","seem","seemed","seeming","seems","seen","self","selves","sensible","sent","serious","seriously","seven","several","shall","shan’t","she","she’d","she’ll","she’s","should","shouldn’t","since","six","so","some","someday","somehow","someone","something","sometime","sometimes","somewhat","somewhere","soon","sorry","specified","specify","specifying","still","sub","such","sup","sure",
                "take","taken","taking","tell","tends","th","than","thank","thanks","thanx","that","that’ll","thats","that’s","that’ve","the","their","theirs","them","themselves","then","thence","there","thereafter","thereby","there’d","therefore","therein","there’ll","there’re","theres","there’s","thereupon","there’ve","these","they","they’d","they’ll","they’re","they’ve","thing","things","think","third","thirty","this","thorough","thoroughly","those","though","three","through",
                "throughout","thru","thus","till","to","together","too","took","toward","towards","tried","tries","truly","try","trying","t’s","twice","two",
                "un","under","underneath","undoing","unfortunately","unless","unlike","unlikely","until","unto","up","upon","upwards","useful","uses","using","usually",
                "value","various","versus","very","via","viz","vs",
                "want","wants","was","wasn’t","way","we","we’d","welcome","well","we’ll","went","were","we’re","weren’t","we’ve","what","whatever","what’ll","what’s","what’ve","when","whence","whenever","where","whereafter","whereas","whereby","wherein","where’s","whereupon","wherever","whether","which","whichever","while","whilst","whither","who","who’d","whoever","whole","who’ll","whom","whomever","who’s","whose","why","will","willing","wish","with","within","without","wonder","won’t","would","wouldn’",
                "yes","yet","you","you’d","you’ll","your","you’re","yours","yourself","yourselves","you’ve",
                "zero","the", "and", "are", "is","i" , "he" ,"she" , "over" , "and", "it" , "will" , "be"
                );
        int firstIndexOfPhrase=-1;
        firstIndexOfPhrase=query.indexOf('"');
        int lastIndexOfPhrase=-1;
        lastIndexOfPhrase=query.lastIndexOf('"');
        String phrase="";
        if(firstIndexOfPhrase!=-1&&lastIndexOfPhrase!=-1)
        {
            phrase=query.substring(firstIndexOfPhrase+1,lastIndexOfPhrase);
            query=query.replace(phrase,"");
            query=query.replace("\"","");
        }

        ArrayList<String> allWords = Stream.of(query.toLowerCase().split(" "))
                        .collect(Collectors.toCollection(ArrayList<String>::new));
        allWords.removeAll(stopwords);

        String result = allWords.stream().collect(Collectors.joining(" "));

        Stemmer S = new Stemmer();
        String stemmed_query=S.stem(result);
        //sending stemmed query to the server
        final  String IP="192.168.194.92";
        final  int port=7800;
        String finalPhrase = phrase;
        String imgSearch="@";
        if(imageSearch.isChecked())
            imgSearch+="yes";
        else imgSearch+="non";
        String finalImgSearch = imgSearch;
        Thread querySender =
        new Thread(
                () -> {
                  try {
                    Socket S1 = new Socket(String.valueOf(IP), port);
                    DataOutputStream DOS = new DataOutputStream(S1.getOutputStream());
                      if(!finalPhrase.isEmpty())
                          DOS.writeUTF(stemmed_query+ finalPhrase+"@phrase"+finalImgSearch);
                      else DOS.writeUTF(stemmed_query+"@non"+finalImgSearch);
                   // DOS.writeUTF(stemmed_query);
                      System.out.println("query sent!");
                      Log.d("STATE", "query sent!");
                    DOS.flush();
                    DOS.close();
                    S1.close();
                 /*   if(finalImgSearch.equals("@yes"))//start image search activity
                    {
                        Intent intent = new Intent(this, imageResult.class);
                        startActivity(intent);
                    }
                    else //start result activity
                    {
                        Intent intent = new Intent(this, QueryResult.class);
                        startActivity(intent);
                    }*/
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                });
        querySender.start();
        querySender.join();
        if(finalImgSearch.equals("@yes"))//start image search activity
        {
            Intent intent = new Intent(this, imageResult.class);
            startActivity(intent);
        }
        else //start result activity
        {

            Intent intent = new Intent(this, QueryResult.class);

            startActivity(intent);
        }

    }
}

class Stemmer {

    private final static String ILLEGAL_REGEX_PATTERN = "([^a-zA-Z0-9])|(\\b\\d{1}\\b)|(\\b\\w{1}\\b)";

    public Stemmer(){
        //LoadStopWords();
    }

    public String stem(String s){
        StringBuilder stringBuilder = new StringBuilder();
        for (String word : replaceIllegalCharacter(s).split(" "))
        {
            String stemmedWord = stemPrivate(word);
            if (StringUtils.isNotEmpty(stemmedWord)) {
                if (stringBuilder.length() > 0)
                    stringBuilder.append(' ');

                stringBuilder.append(stemmedWord);
            }
        }
        return (stringBuilder.toString());
    }

    private static String stemPrivate(String word)
    {
        PorterStemmer porterStemmer = new PorterStemmer();
        porterStemmer.setCurrent(word);
        porterStemmer.stem();
        return porterStemmer.getCurrent();
    }
    private static String replaceIllegalCharacter(String string)
    {
        return string.replaceAll(ILLEGAL_REGEX_PATTERN, " ").replaceAll(" +", " ").trim().toLowerCase();
    }}
